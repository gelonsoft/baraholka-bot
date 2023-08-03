package baraholkateam.command;

import baraholkateam.rest.model.CurrentObyavleniye;
import baraholkateam.rest.service.ChosenTagsService;
import baraholkateam.rest.service.CurrentObyavleniyeService;
import baraholkateam.util.State;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.List;

@Component
public class NewObyavleniyeCommand extends Command {
    private static final String NEW_AD = """
            Команда /%s позволяет перейти к процессу создания нового объявления.
            Вам необходимо ответить на вопросы и заполнить макет объявления.
            Чтобы прервать создание, нужно вернуться в главное меню /%s.
            Нажмите на кнопку '%s', чтобы начать создание объявления.""";

    @Autowired
    private CurrentObyavleniyeService currentObyavleniyeService;

    @Autowired
    private ChosenTagsService chosenTagsService;

    public NewObyavleniyeCommand() {
        super(State.NewObyavleniye.getIdentifier(), State.NewObyavleniye.getDescription());
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        currentObyavleniyeService.put(new CurrentObyavleniye(chat.getId()));
        chosenTagsService.delete(chat.getId());

        sendAnswer(absSender, chat.getId(), this.getCommandIdentifier(), user.getUserName(),
                String.format(NEW_AD, State.NewObyavleniye.getIdentifier(), State.NewObyavleniye.getIdentifier(),
                        State.NewObyavleniye_AddPhotos.getDescription()), getAddReplyKeyboard());
    }

    private ReplyKeyboardMarkup getAddReplyKeyboard() {
        return getReplyKeyboard(List.of(State.NewObyavleniye_AddPhotos.getDescription()), false);
    }
}
