package baraholkateam.command;

import baraholkateam.util.Advertisement;
import baraholkateam.util.State;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class NewAdvertisementCommand extends Command {
    private static final String NEW_AD = """
            Команда /%s позволяет перейти к процессу создания нового объявления.
            Вам необходимо ответить на вопросы и заполнить макет объявления.
            Чтобы прервать создание, нужно вернуться в главное меню /%s.
            Добавьте фотографии к вашему объявлению.""";
    private final Map<Long, Advertisement> advertisement;
    private final Map<Long, String> chosenTags;

    public NewAdvertisementCommand(Map<Long, Message> lastSentMessage, Map<Long, Advertisement> advertisement,
                                   Map<Long, String> chosenTags) {
        super(State.NewAdvertisement.getIdentifier(), State.NewAdvertisement.getDescription(), lastSentMessage);
        this.advertisement = advertisement;
        this.chosenTags = chosenTags;
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        advertisement.put(chat.getId(), new Advertisement(chat.getId()));
        chosenTags.remove(chat.getId());

        SendMessage message = suggestAddingPhotos(chat.getId());
        try {
            absSender.execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private SendMessage suggestAddingPhotos(Long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(String.format(NEW_AD, State.NewAdvertisement.getIdentifier(),
                State.NewAdvertisement.getIdentifier()));
        message.setReplyMarkup(getAddReplyKeyboard());
        return message;
    }

    private ReplyKeyboardMarkup getAddReplyKeyboard() {
        KeyboardButton addPhotosButton = new KeyboardButton();
        addPhotosButton.setText(State.NewAdvertisement_AddPhotos.getDescription());

        KeyboardButton mainMenuButton = new KeyboardButton();
        mainMenuButton.setText(State.MainMenu.getDescription());

        KeyboardRow keyboardFirstRow = new KeyboardRow();
        keyboardFirstRow.add(addPhotosButton);
        keyboardFirstRow.add(mainMenuButton);

        List<KeyboardRow> keyboardRows = new ArrayList<>();
        keyboardRows.add(keyboardFirstRow);

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setKeyboard(keyboardRows);
        replyKeyboardMarkup.setResizeKeyboard(true);

        return replyKeyboardMarkup;
    }
}
