package baraholkateam.command;

import baraholkateam.util.State;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.Collections;

@Component
public class NewObyavleniyeAddDescription extends Command {
    private static final String ADD_DESCRIPTION_TEXT = """
            Введите краткое описание товара (не более 800 символов):""";

    public NewObyavleniyeAddDescription() {
        super(State.NewObyavleniye_AddDescription.getIdentifier(),
                State.NewObyavleniye_AddDescription.getDescription());
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        sendAnswer(absSender, chat.getId(), this.getCommandIdentifier(), user.getUserName(),
                ADD_DESCRIPTION_TEXT, getReplyKeyboard(Collections.emptyList(), true));
    }
}
