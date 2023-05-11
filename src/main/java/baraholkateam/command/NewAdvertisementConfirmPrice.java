package baraholkateam.command;

import baraholkateam.rest.service.CurrentAdvertisementService;
import baraholkateam.util.State;
import org.springframework.beans.factory.annotation.Autowired;
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
public class NewAdvertisementConfirmPrice extends Command {
    private static final String CONFIRM_PRICE_TEXT = """
            Цена вашего товара: %s руб.
            Теперь необходимо добавить контакты для связи с вами.""";

    @Autowired
    private CurrentAdvertisementService currentAdvertisementService;

    public NewAdvertisementConfirmPrice() {
        super(State.NewAdvertisement_ConfirmPrice.getIdentifier(),
                State.NewAdvertisement_ConfirmPrice.getDescription());
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        sendAnswer(absSender, chat.getId(), this.getCommandIdentifier(), user.getUserName(),
                String.format(CONFIRM_PRICE_TEXT, currentAdvertisementService.get(chat.getId()).getPrice()),
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
