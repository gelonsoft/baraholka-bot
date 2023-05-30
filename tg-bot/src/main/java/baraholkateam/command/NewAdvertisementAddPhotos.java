package baraholkateam.command;

import baraholkateam.util.State;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.Collections;

@Component
public class NewAdvertisementAddPhotos extends Command {
    private static final String ADD_PHOTOS_TEXT = """
            Добавьте от 1 до 10 фотографий к вашему объявлению. Рекомендуемое число - 5.""";

    public NewAdvertisementAddPhotos() {
        super(State.NewAdvertisement_AddPhotos.getIdentifier(), State.NewAdvertisement_AddPhotos.getDescription());
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        sendAnswer(absSender, chat.getId(), this.getCommandIdentifier(), user.getUserName(),
                ADD_PHOTOS_TEXT, getReplyKeyboard(Collections.emptyList(), true));
    }
}
