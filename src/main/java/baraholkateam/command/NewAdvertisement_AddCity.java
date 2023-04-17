package baraholkateam.command;

import baraholkateam.util.State;
import baraholkateam.util.TagType;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class NewAdvertisement_AddCity extends Command {
    private static final String ADD_HASHTAGS_TEXT = """
            Описание успешно добавлено.
            Теперь необходимо добавить хэштеги.""";
    private static final String ADD_CITY_TEXT = """
            Выберите город, который хотите добавить:""";

    public NewAdvertisement_AddCity(Map<Long, Message> lastSentMessage) {
        super(State.NewAdvertisement_AddCity.getIdentifier(),
                State.NewAdvertisement_AddCity.getDescription(), lastSentMessage);
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        sendAnswer(absSender, chat.getId(), this.getCommandIdentifier(), user.getUserName(),
                ADD_HASHTAGS_TEXT, getAddReplyKeyboard());
        sendAnswer(absSender, chat.getId(), this.getCommandIdentifier(), user.getUserName(),
                ADD_CITY_TEXT, getTags(TagType.City, false));
    }
    
    private ReplyKeyboardMarkup getAddReplyKeyboard() {
        KeyboardButton mainMenuButton = new KeyboardButton();
        mainMenuButton.setText(State.MainMenu.getDescription());

        KeyboardRow keyboardFirstRow = new KeyboardRow();
        keyboardFirstRow.add(mainMenuButton);

        List<KeyboardRow> keyboardRows = new ArrayList<>();
        keyboardRows.add(keyboardFirstRow);

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setKeyboard(keyboardRows);
        replyKeyboardMarkup.setResizeKeyboard(true);

        return replyKeyboardMarkup;
    }
}
