package baraholkateam.command;

import baraholkateam.util.Advertisement;
import baraholkateam.util.State;
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

public class NewAdvertisement_ConfirmPrice extends Command {
    private static final String CONFIRM_PRICE_TEXT = """
            Цена вашего товара: %s руб.
            Теперь необходимо добавить контакты для связи с вами.""";
    private final Map<Long, Advertisement> advertisement;

    public NewAdvertisement_ConfirmPrice(Map<Long, Message> lastSentMessage, Map<Long, Advertisement> advertisement) {
        super(State.NewAdvertisement_ConfirmPrice.getIdentifier(),
                State.NewAdvertisement_ConfirmPrice.getDescription(), lastSentMessage);
        this.advertisement = advertisement;
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        sendAnswer(absSender, chat.getId(), this.getCommandIdentifier(), user.getUserName(),
                String.format(CONFIRM_PRICE_TEXT, advertisement.get(chat.getId()).getPrice()),
                getAddReplyKeyboard());
    }

    private ReplyKeyboardMarkup getAddReplyKeyboard() {
        KeyboardButton addContactsButton = new KeyboardButton();
        addContactsButton.setText(State.NewAdvertisement_AddContacts.getDescription());

        KeyboardButton mainMenuButton = new KeyboardButton();
        mainMenuButton.setText(State.MainMenu.getDescription());

        KeyboardRow keyboardFirstRow = new KeyboardRow();
        keyboardFirstRow.add(addContactsButton);
        keyboardFirstRow.add(mainMenuButton);

        List<KeyboardRow> keyboardRows = new ArrayList<>();
        keyboardRows.add(keyboardFirstRow);

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setKeyboard(keyboardRows);
        replyKeyboardMarkup.setResizeKeyboard(true);

        return replyKeyboardMarkup;
    }
}
