package baraholkateam.command;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;


public class NewAdvertisementCommand extends Command {
    // TODO дописать первое действие при создании объявления
    private static final String NEW_AD = """
            Давайте приступим к созданию нового объявления.
            Для начала Вам необходимо...""";

    public NewAdvertisementCommand(String commandIdentifier, String description) {
        super(commandIdentifier, description);
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        SendMessage message = suggestAddingPhotos(chat.getId());
        try {
            absSender.execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
//        sendAnswer(absSender, chat.getId(), this.getCommandIdentifier(), user.getUserName(), NEW_AD);
    }

    public SendMessage suggestAddingPhotos(Long chatId) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();

        KeyboardButton addPhotosButton = new KeyboardButton();
        addPhotosButton.setText("Добавить фотографии");

        KeyboardButton mainMenuButton = new KeyboardButton();
        mainMenuButton.setText("Главное меню");

        KeyboardRow keyboardFirstRow = new KeyboardRow();
        keyboardFirstRow.add(addPhotosButton);
        keyboardFirstRow.add(mainMenuButton);

        List<KeyboardRow> keyboardRows = new ArrayList<>();
        keyboardRows.add(keyboardFirstRow);

        replyKeyboardMarkup.setKeyboard(keyboardRows);
        replyKeyboardMarkup.setResizeKeyboard(true);

        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Добавьте фотографии");
        message.setReplyMarkup(replyKeyboardMarkup);
        return message;
    }
}
