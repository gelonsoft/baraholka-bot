package baraholkateam.command;

import baraholkateam.util.State;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class NewAdvertisementAddContacts extends Command {
    private static final String ADD_CONTACTS_TEXT = """
            Вы можете указать свои контактные данные.""";
    private static final String ADD_CONTACTS_QUESTION = """
            Желаете указать ваш номер телефона?""";

    public NewAdvertisementAddContacts() {
        super(State.NewAdvertisement_AddContacts.getIdentifier(), State.NewAdvertisement_AddContacts.getDescription());
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        sendAnswer(absSender, chat.getId(), this.getCommandIdentifier(), user.getUserName(),
                ADD_CONTACTS_TEXT, getReplyKeyboard(Collections.emptyList(), true));
        sendAnswer(absSender, chat.getId(), this.getCommandIdentifier(), user.getUserName(),
                ADD_CONTACTS_QUESTION, getAddPhone());
    }

    private InlineKeyboardMarkup getAddPhone() {
        InlineKeyboardButton yesButton = new InlineKeyboardButton();
        yesButton.setText("Да");
        String yesCallbackData = String.format("%s %s", PHONE_CALLBACK_DATA, "yes");
        yesButton.setCallbackData(yesCallbackData);

        InlineKeyboardButton noButton = new InlineKeyboardButton();
        noButton.setText("Нет");
        String noCallbackData = String.format("%s %s", PHONE_CALLBACK_DATA, "no");
        noButton.setCallbackData(noCallbackData);

        List<InlineKeyboardButton> keyboardFirstRow = new ArrayList<>();
        keyboardFirstRow.add(yesButton);
        keyboardFirstRow.add(noButton);

        List<List<InlineKeyboardButton>> keyboardRows = new ArrayList<>();
        keyboardRows.add(keyboardFirstRow);

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(keyboardRows);

        return inlineKeyboardMarkup;
    }
}
