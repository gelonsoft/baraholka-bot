package baraholkateam.command;

import baraholkateam.rest.service.CurrentAdvertisementService;
import baraholkateam.util.State;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.ArrayList;
import java.util.List;

@Component
public class NewAdvertisementConfirmPhone extends Command {
    private static final String PHONE_TEXT = """
            Ваш номер телефона: %s
            """;
    private static final String CONFIRM_PHONE_TEXT = """
            Желаете добавить ссылку на вашу социальную сеть?""";

    @Autowired
    private CurrentAdvertisementService currentAdvertisementService;

    public NewAdvertisementConfirmPhone() {
        super(State.NewAdvertisement_ConfirmPhone.getIdentifier(),
                State.NewAdvertisement_ConfirmPhone.getDescription());
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        String phone = currentAdvertisementService.get(chat.getId()).getPhone();
        if (phone != null) {
            sendAnswer(absSender, chat.getId(), this.getCommandIdentifier(), user.getUserName(),
                    String.format(PHONE_TEXT, phone), null);
        }

        sendAnswer(absSender, chat.getId(), this.getCommandIdentifier(), user.getUserName(),
                CONFIRM_PHONE_TEXT, getAddSocial());
    }

    private InlineKeyboardMarkup getAddSocial() {
        InlineKeyboardButton yesButton = new InlineKeyboardButton();
        yesButton.setText("Да");
        String yesCallbackData = String.format("%s %s", SOCIAL_CALLBACK_DATA, "yes");
        yesButton.setCallbackData(yesCallbackData);

        InlineKeyboardButton noButton = new InlineKeyboardButton();
        noButton.setText("Нет");
        String noCallbackData = String.format("%s %s", SOCIAL_CALLBACK_DATA, "no");
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
