package baraholkateam.command;

import baraholkateam.util.Tag;
import baraholkateam.util.TagType;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class NewAdvertisement_AddCityTags extends Command {
    private static final String ADD_CITY_TAGS_TEXT = """
            Выберите город для публикации объявления.""";
    public static final String ADD_CITY_CALLBACK_DATA = "addCity";

    public NewAdvertisement_AddCityTags(String commandIdentifier, String description, Map<Long, Message> lastSentMessage) {
        super(commandIdentifier, description, lastSentMessage);
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        List<Tag> cities = new ArrayList<>();
        for (Tag tag : Tag.values()) {
            if (tag.getTagType() == TagType.City) {
                cities.add(tag);
            }
        }

        InlineKeyboardButton[] buttons = new InlineKeyboardButton[cities.size()];

        for (int i = 0; i < cities.size(); i++) {
            buttons[i] = new InlineKeyboardButton();
            buttons[i].setText(cities.get(i).getName());
            buttons[i].setCallbackData(String.format("%s %s", ADD_CITY_CALLBACK_DATA, cities.get(i).getName()));
        }

        List<InlineKeyboardButton> keyboardRow1 = new ArrayList<>();
        keyboardRow1.add(buttons[0]);
        keyboardRow1.add(buttons[1]);
        keyboardRow1.add(buttons[2]);

        List<InlineKeyboardButton> keyboardRow2 = new ArrayList<>();
        keyboardRow2.add(buttons[3]);
        keyboardRow2.add(buttons[4]);
        keyboardRow2.add(buttons[5]);

        List<InlineKeyboardButton> keyboardRow3 = new ArrayList<>();
        keyboardRow3.add(buttons[6]);
        keyboardRow3.add(buttons[7]);
        keyboardRow3.add(buttons[8]);

        List<InlineKeyboardButton> keyboardRow4 = new ArrayList<>();
        keyboardRow4.add(buttons[9]);
        keyboardRow4.add(buttons[10]);

        List<InlineKeyboardButton> keyboardRow5 = new ArrayList<>();
        keyboardRow5.add(buttons[11]);

        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(keyboardRow1);
        rowList.add(keyboardRow2);
        rowList.add(keyboardRow3);
        rowList.add(keyboardRow4);
        rowList.add(keyboardRow5);

        inlineKeyboardMarkup.setKeyboard(rowList);

        sendAnswer(absSender, chat.getId(), this.getCommandIdentifier(), user.getUserName(),
                ADD_CITY_TAGS_TEXT, inlineKeyboardMarkup);
    }
}
