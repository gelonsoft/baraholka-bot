package baraholkateam.command;

import baraholkateam.util.State;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.Map;

public class NewAdvertisement_AddPhone extends Command {
    private static final String ADD_PHONE_TEXT = """
            Введите ваш номер в формате
            +7-xxx-xxx-xx-xx""";

    public NewAdvertisement_AddPhone(Map<Long, Message> lastSentMessage) {
        super(State.NewAdvertisement_AddPhone.getIdentifier(),
                State.NewAdvertisement_AddPhone.getDescription());
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        sendAnswer(absSender, chat.getId(), this.getCommandIdentifier(), user.getUserName(),
                ADD_PHONE_TEXT, null);
    }
}
