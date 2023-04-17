package baraholkateam.command;

import baraholkateam.util.State;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.Map;

public class NewAdvertisement_AddSocial extends Command {
    private static final String ADD_SOCIAL_TEXT = """
            Добавьте ссылку на вашу социальную сеть.
            Примеры таких ссылок:
            1. https://vk.com/123456789
            2. https://t.me/petya
            3. https://ok.ru/profile/123456789
            4. https://wa.me/89000000000""";

    public NewAdvertisement_AddSocial(Map<Long, Message> lastSentMessage) {
        super(State.NewAdvertisement_AddSocial.getIdentifier(),
                State.NewAdvertisement_AddSocial.getDescription(), lastSentMessage);
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        sendAnswer(absSender, chat.getId(), this.getCommandIdentifier(), user.getUserName(),
                ADD_SOCIAL_TEXT, null);
    }
}
