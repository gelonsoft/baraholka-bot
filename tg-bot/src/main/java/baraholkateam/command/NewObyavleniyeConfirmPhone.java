package baraholkateam.command;

import baraholkateam.rest.model.CurrentObyavleniye;
import baraholkateam.rest.service.CurrentObyavleniyeService;
import baraholkateam.rest.service.PreviousStateService;
import baraholkateam.util.State;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class NewObyavleniyeConfirmPhone extends Command {
    private static final String PHONE_TEXT = "Ваш номер телефона: %s";
    private static final String SOCIAL_TEXT = "Добавлена социальная сеть: %s";
    private static final String CONFIRM_PHONE_TEXT = "Желаете добавить ссылку на вашу социальную сеть?";
    public static final String DELETE_ALL_SOCIALS = "Удалить все социальные сети";

    @Autowired
    private CurrentObyavleniyeService currentObyavleniyeService;

    @Autowired
    private PreviousStateService previousStateService;

    public NewObyavleniyeConfirmPhone() {
        super(State.NewObyavleniye_ConfirmPhone.getIdentifier(),
                State.NewObyavleniye_ConfirmPhone.getDescription());
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        CurrentObyavleniye currentObyavleniye = currentObyavleniyeService.get(chat.getId());
        String phone = currentObyavleniye.getPhone();
        List<String> socials = currentObyavleniye.getContacts();
        if (phone != null && previousStateService.get(chat.getId()) != State.NewObyavleniye_AddSocial) {
            sendAnswer(absSender, chat.getId(), this.getCommandIdentifier(), user.getUserName(),
                    String.format(PHONE_TEXT, phone), getReplyKeyboard(Collections.emptyList(), true));
        } else if (!socials.isEmpty()) {
            sendAnswer(absSender, chat.getId(), this.getCommandIdentifier(), user.getUserName(),
                    String.format(SOCIAL_TEXT, socials.get(socials.size() - 1)), getDeleteButton());
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

    private ReplyKeyboardMarkup getDeleteButton() {
        return getReplyKeyboard(List.of(DELETE_ALL_SOCIALS), true);
    }
}
