package baraholkateam.command;

import baraholkateam.util.State;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Component
public class NewAdvertisementAddPhone extends Command {
    private static final String ADD_PHONE_TEXT = """
            Введите ваш номер в формате
            +7-xxx-xxx-xx-xx""";

    public NewAdvertisementAddPhone() {
        super(State.NewAdvertisement_AddPhone.getIdentifier(), State.NewAdvertisement_AddPhone.getDescription());
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        sendAnswer(absSender, chat.getId(), this.getCommandIdentifier(), user.getUserName(),
                ADD_PHONE_TEXT, null);
    }
}
