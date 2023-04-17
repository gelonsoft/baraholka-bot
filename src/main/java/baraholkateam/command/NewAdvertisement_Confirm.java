package baraholkateam.command;

import baraholkateam.bot.BaraholkaBot;
import baraholkateam.util.Advertisement;
import baraholkateam.util.Tag;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class NewAdvertisement_Confirm extends Command {
    private static final String CONFIRM_AD_TEXT = """
            Желаете опубликовать ваше объявление в канале?""";

    public NewAdvertisement_Confirm(String commandIdentifier, String description, Map<Long, Message> lastSentMessage) {
        super(commandIdentifier, description, lastSentMessage);
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        Advertisement ad = BaraholkaBot.getNewAdvertisement(chat.getId());

        List<Tag> tags = BaraholkaBot.getNewAdvertisement(chat.getId()).getTags();
        StringBuilder tagsString = new StringBuilder();
        tagsString.append(ad.getCity()).append(", ");
        for (Tag tag : tags) {
            tagsString.append(tag.getName()).append(", ");
        }
        if (tagsString.length() > 0) {
            tagsString.setLength(tagsString.length() - 2);
        }

        Long price = ad.getPrice();
        String description = ad.getDescription();

        StringBuilder sb = new StringBuilder();

        sb.append(String.format("""
            %s
            
            Цена: %s
            
            Описание: %s""", tagsString, price, description));
        sb.append("\n");

        String phone = ad.getPhone();
        if (phone != null) {
            sb.append("\n").append(String.format("Номер телефона: %s", phone));
        }

        List<String> contacts = ad.getContacts();
        if (contacts.size() > 0) {
            sb.append("\n");
            StringBuilder contactsString = new StringBuilder();
            for (String contact : contacts) {
                contactsString.append(contact).append(", ");
            }
            if (contactsString.length() > 0) {
                contactsString.setLength(contactsString.length() - 2);
            }
            sb.append(String.format("Контакты: %s", contactsString));
        }

        sendAnswer(absSender, chat.getId(), this.getCommandIdentifier(), user.getUserName(),
                sb.toString(), null);
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardButton yesButton = new InlineKeyboardButton();
        yesButton.setText("Да");
        String yesCallbackData = String.format("%s %s", CONFIRM_AD_CALLBACK_DATA, "yes");
        yesButton.setCallbackData(yesCallbackData);

        InlineKeyboardButton noButton = new InlineKeyboardButton();
        noButton.setText("Нет");
        String noCallbackData = String.format("%s %s", CONFIRM_AD_CALLBACK_DATA, "no");
        noButton.setCallbackData(noCallbackData);

        List<InlineKeyboardButton> keyboardFirstRow = new ArrayList<>();
        keyboardFirstRow.add(yesButton);
        keyboardFirstRow.add(noButton);

        List<List<InlineKeyboardButton>> keyboardRows = new ArrayList<>();
        keyboardRows.add(keyboardFirstRow);
        inlineKeyboardMarkup.setKeyboard(keyboardRows);

        sendAnswer(absSender, chat.getId(), this.getCommandIdentifier(), user.getUserName(),
                CONFIRM_AD_TEXT, inlineKeyboardMarkup);
    }
}
