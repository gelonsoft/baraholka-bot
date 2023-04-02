package baraholkateam.command;

import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.Map;

public class NewAdvertisementCommand extends Command {
    // TODO дописать первое действие при создании объявления
    private static final String NEW_AD = """
            Давайте приступим к созданию нового объявления.
            Для начала Вам необходимо...""";

    public NewAdvertisementCommand(String commandIdentifier, String description, Map<Long, Message> lastSentMessage) {
        super(commandIdentifier, description, lastSentMessage);
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        sendAnswer(absSender, chat.getId(), this.getCommandIdentifier(), user.getUserName(), NEW_AD, null);
    }
}
