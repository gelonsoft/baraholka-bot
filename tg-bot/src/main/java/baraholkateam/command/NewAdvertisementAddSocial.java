package baraholkateam.command;

import baraholkateam.util.State;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.Collections;

@Component
public class NewAdvertisementAddSocial extends Command {
    private static final String ADD_SOCIAL_TEXT = """
            Добавьте ссылку на вашу социальную сеть.
            Примеры таких ссылок:
            1. https://vk.com/12345678900
            2. https://t.me/petyapetya
            3. https://ok.ru/profile/12345678900
            4. https://wa.me/89000000000""";

    public NewAdvertisementAddSocial() {
        super(State.NewAdvertisement_AddSocial.getIdentifier(), State.NewAdvertisement_AddSocial.getDescription());
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        sendAnswer(absSender, chat.getId(), this.getCommandIdentifier(), user.getUserName(), ADD_SOCIAL_TEXT,
                getReplyKeyboard(Collections.emptyList(), true));
    }
}
