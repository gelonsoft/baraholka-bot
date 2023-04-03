package baraholkateam.command;

import baraholkateam.util.State;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class NewAdvertisement_AddCityTags extends Command {
    private static final String ADD_CITY_TAGS_TEXT = """
            Выберите город для публикации объявления.""";

    public NewAdvertisement_AddCityTags(String commandIdentifier, String description, Map<Long, Message> lastSentMessage) {
        super(commandIdentifier, description, lastSentMessage);
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        SendMessage message = suggestChoosingCity(chat.getId());
        try {
            absSender.execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    public SendMessage suggestChoosingCity(Long chatId) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        // TODO parse data from Tag class
        List<String> cities = new ArrayList<>();
        cities.add("Москва");
        cities.add("СПб");
        cities.add("Екатеринбург");
        cities.add("Челябинск");
        cities.add("Ульяновск");
        cities.add("Омск");
        cities.add("Белгород");
        cities.add("Пермь");
        cities.add("Волгоград");
        cities.add("Киров");
        cities.add("Хабаровск");
        cities.add("Петропавловск");

        InlineKeyboardButton[] buttons = new InlineKeyboardButton[cities.size()];

        for (int i = 0; i < cities.size(); i++) {
            buttons[i] = new InlineKeyboardButton();
            buttons[i].setText(cities.get(i));
            buttons[i].setCallbackData("Button" + cities.get(i) + "has been pressed");
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

        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(ADD_CITY_TAGS_TEXT);
        message.setReplyMarkup(inlineKeyboardMarkup);
        return message;
    }
}
