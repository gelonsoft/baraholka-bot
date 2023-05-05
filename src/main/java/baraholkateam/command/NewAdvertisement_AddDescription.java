package baraholkateam.command;

import baraholkateam.util.State;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.Map;

public class NewAdvertisement_AddDescription extends Command {
    private static final String ADD_DESCRIPTION_TEXT = """
            Введите описание товара:""";

    public NewAdvertisement_AddDescription(Map<Long, Message> lastSentMessage) {
        super(State.NewAdvertisement_AddDescription.getIdentifier(),
                State.NewAdvertisement_AddDescription.getDescription());
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        sendAnswer(absSender, chat.getId(), this.getCommandIdentifier(), user.getUserName(),
                ADD_DESCRIPTION_TEXT, null);
    }
}
