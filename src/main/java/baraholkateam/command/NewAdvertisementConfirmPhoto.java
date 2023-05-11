package baraholkateam.command;

import baraholkateam.util.State;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.ArrayList;
import java.util.List;

@Component
public class NewAdvertisementConfirmPhoto extends Command {
    private static final String CONFIRM_PHOTOS_TEXT = """
            Фотографии успешно добавлены.
            Вы можете добавить еще или перейти к добавлению описания.""";

    public NewAdvertisementConfirmPhoto() {
        super(State.NewAdvertisement_ConfirmPhoto.getIdentifier(),
                State.NewAdvertisement_ConfirmPhoto.getDescription());
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        sendAnswer(absSender, chat.getId(), this.getCommandIdentifier(), user.getUserName(),
                CONFIRM_PHOTOS_TEXT, getAddReplyKeyboard());
    }

    private ReplyKeyboardMarkup getAddReplyKeyboard() {
        KeyboardButton addPhotosButton = new KeyboardButton();
        addPhotosButton.setText(State.NewAdvertisement_AddPhotos.getDescription());

        KeyboardButton addDescriptionButton = new KeyboardButton();
        addDescriptionButton.setText(State.NewAdvertisement_AddDescription.getDescription());

        KeyboardButton mainMenuButton = new KeyboardButton();
        mainMenuButton.setText(State.MainMenu.getDescription());

        KeyboardRow keyboardFirstRow = new KeyboardRow();
        keyboardFirstRow.add(addDescriptionButton);
        keyboardFirstRow.add(addPhotosButton);
        keyboardFirstRow.add(mainMenuButton);

        List<KeyboardRow> keyboardRows = new ArrayList<>();
        keyboardRows.add(keyboardFirstRow);

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setKeyboard(keyboardRows);
        replyKeyboardMarkup.setResizeKeyboard(true);

        return replyKeyboardMarkup;
    }
}
