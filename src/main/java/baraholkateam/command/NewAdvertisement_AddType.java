package baraholkateam.command;

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

public class NewAdvertisement_AddType extends Command {
    private static final String ADD_TYPE_TAGS_TEXT = """
            Выберите тип объявления.""";

    public NewAdvertisement_AddType(String commandIdentifier, String description, Map<Long, Message> lastSentMessage) {
        super(commandIdentifier, description, lastSentMessage);
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        SendMessage message = suggestChoosingType(chat.getId());
        try {
            absSender.execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    public SendMessage suggestChoosingType(Long chatId) {
        InlineKeyboardMarkup inlineKeyboardMarkup = getTags(TagType.AdvertisementType, true);

        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(ADD_TYPE_TAGS_TEXT);
        message.setReplyMarkup(inlineKeyboardMarkup);
        return message;
    }
}
