package baraholkateam.command;

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

public class NewAdvertisement_ConfirmCity extends Command {
    private static final String CONFIRM_CITY_TEXT = """
                Вы выбрали город: %s
                Теперь выберите тип объявления""";

    private final Map<Long, String> chosenCity;

    public NewAdvertisement_ConfirmCity(String commandIdentifier, String description,
                                        Map<Long, Message> lastSentMessage,
                                        Map<Long, String> chosenCity) {
        super(commandIdentifier, description, lastSentMessage);
        this.chosenCity = chosenCity;
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        String chosenCityString = chosenCity.get(chat.getId());

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();

        KeyboardButton addTypeButton = new KeyboardButton();
        addTypeButton.setText(State.NewAdvertisement_AddType.getDescription());

        KeyboardButton mainMenuButton = new KeyboardButton();
        mainMenuButton.setText(State.MainMenu.getDescription());

        KeyboardRow keyboardFirstRow = new KeyboardRow();
        keyboardFirstRow.add(addTypeButton);
        keyboardFirstRow.add(mainMenuButton);

        List<KeyboardRow> keyboardRows = new ArrayList<>();
        keyboardRows.add(keyboardFirstRow);

        replyKeyboardMarkup.setKeyboard(keyboardRows);
        replyKeyboardMarkup.setResizeKeyboard(true);

        sendAnswer(absSender, chat.getId(), this.getCommandIdentifier(), user.getUserName(),
                String.format(CONFIRM_CITY_TEXT, chosenCityString), replyKeyboardMarkup);
    }
}
