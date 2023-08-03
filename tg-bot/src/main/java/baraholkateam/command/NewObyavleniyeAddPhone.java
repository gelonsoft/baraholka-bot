package baraholkateam.command;

import baraholkateam.util.State;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.Collections;

@Component
public class NewObyavleniyeAddPhone extends Command {
    private static final String ADD_PHONE_TEXT = """
            Введите ваш номер в формате
            +7-xxx-xxx-xx-xx""";

    public NewObyavleniyeAddPhone() {
        super(State.NewObyavleniye_AddPhone.getIdentifier(), State.NewObyavleniye_AddPhone.getDescription());
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        sendAnswer(absSender, chat.getId(), this.getCommandIdentifier(), user.getUserName(),
                ADD_PHONE_TEXT, getReplyKeyboard(Collections.emptyList(), true));
    }
}
