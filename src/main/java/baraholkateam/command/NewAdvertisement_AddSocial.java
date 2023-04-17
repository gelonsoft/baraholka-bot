package baraholkateam.command;

import baraholkateam.bot.BaraholkaBot;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.Map;

public class NewAdvertisement_AddSocial extends Command {
    private static final String PHONE_TEXT = """
            Ваш номер телефона: %s
            """;
    private static final String ADD_SOCIAL_TEXT = """
            Добавьте ссылку на вашу социальную сеть.""";

    public NewAdvertisement_AddSocial(String commandIdentifier, String description, Map<Long, Message> lastSentMessage) {
        super(commandIdentifier, description, lastSentMessage);
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        String phone = BaraholkaBot.getNewAdvertisement(chat.getId()).getPhone();
        if (phone != null) {
            sendAnswer(absSender, chat.getId(), this.getCommandIdentifier(), user.getUserName(),
                    String.format(PHONE_TEXT, phone), null);
        }
        sendAnswer(absSender, chat.getId(), this.getCommandIdentifier(), user.getUserName(),
                ADD_SOCIAL_TEXT, null);
    }
}
