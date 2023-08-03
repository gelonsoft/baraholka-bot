package baraholkateam.command;

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
public class NewObyavleniyeConfirmPrice extends Command {
    private static final String CONFIRM_PRICE_TEXT = """
            Цена вашего товара: %s руб.
            Теперь необходимо добавить контакты для связи с Вами.""";

    @Autowired
    private CurrentObyavleniyeService currentObyavleniyeService;

    public NewObyavleniyeConfirmPrice() {
        super(State.NewObyavleniye_ConfirmPrice.getIdentifier(),
                State.NewObyavleniye_ConfirmPrice.getDescription());
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        sendAnswer(absSender, chat.getId(), this.getCommandIdentifier(), user.getUserName(),
                String.format(CONFIRM_PRICE_TEXT, currentObyavleniyeService.get(chat.getId()).getPrice()),
                getAddReplyKeyboard());
    }

    private ReplyKeyboardMarkup getAddReplyKeyboard() {
        return getReplyKeyboard(List.of(State.NewObyavleniye_AddContacts.getDescription()), true);
    }
}
