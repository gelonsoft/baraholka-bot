package baraholkateam.command;

import baraholkateam.util.Tag;
import baraholkateam.util.TagType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class Command extends BotCommand {
    public static final String NEXT_BUTTON_TEXT = "Продолжить";
    public static final String NOT_CHOSEN_TAG = "➖ %s";
    public static final String TAG_CALLBACK_DATA = "tag";
    public static final String TAGS_CALLBACK_DATA = "tags";

    public static final String PHONE_CALLBACK_DATA = "phone";
    public static final String SOCIAL_CALLBACK_DATA = "social";
    public static final String CONFIRM_AD_CALLBACK_DATA = "confirm_ad";
    public static final String NOTIFICATION_CALLBACK_DATA = "notification";
    public static final String DELETE_CALLBACK_TEXT = "delete";
    public static final String DELETE_AD_CALLBACK_TEXT = "delete_ad";
    public static final String CHOSEN_TAG = "✅ %s";
    public static final String SUCCESS_TEXT = "Объявление успешно добавлено.";
    public static final String UNSUCCESS_TEXT = "Объявление не было успешно добавлено.";
    public static final String SUCCESS_DELETE_AD_TEXT = """
            Объявление успешно удалено.
            Текущий статус объявления: неактуальное.""";
    public static final String UNSUCCESS_DELETE_AD_TEXT = "Удаление объявление было отменено.";
    public static final String ADVERTISEMENT_CANCELLED_TEXT = "Формирование объявления было отменено.";
    public static final String ADVERTISEMENT_SUCCESSFUL_UPDATE = "Актуальность объявления была продлена.";
    public static final String ADVERTISEMENT_SUCCESSFUL_DELETE = "Объявление было удалено.";
    public static final String ADVERTISEMENT_DELETE =
            "Объявление было автоматически удалено после 3 попыток уточнения его актуальности.";
    static final String INCORRECT_PREVIOUS_STATE = """
            Невозможно выполнить текущую команду.
            Пожалуйста, вернитесь в главное меню /%s и попробуйте снова.""";
    static final String NO_HASHTAGS = "➖";
    static final String CHOSEN_HASHTAGS = "Текущие выбранные хэштеги: %s";
    private final Map<Long, Message> lastSentMessage;
    private final Logger logger = LoggerFactory.getLogger(Command.class);

    public Command(String commandIdentifier, String description, Map<Long, Message> lastSentMessage) {
        super(commandIdentifier, description);
        this.lastSentMessage = lastSentMessage;
    }

    void sendAnswer(AbsSender absSender, Long chatId, String commandName, String userName, String text,
                    ReplyKeyboard replyKeyboard) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        message.setParseMode(ParseMode.HTML);
        message.setText(text);
        if (replyKeyboard != null) {
            message.setReplyMarkup(replyKeyboard);
        }
        message.disableWebPagePreview();

        try {
            Message sentMessage = absSender.execute(message);
            lastSentMessage.put(chatId, sentMessage);
        } catch (TelegramApiException e) {
            logger.error(String.format("Cannot execute command %s of user %s: %s", commandName, userName,
                    e.getMessage()));
        }
    }

    ReplyKeyboardMarkup showNextButton() {
        ReplyKeyboardMarkup rkm = new ReplyKeyboardMarkup();
        rkm.setSelective(true);
        rkm.setResizeKeyboard(true);
        rkm.setOneTimeKeyboard(true);
        List<KeyboardRow> nextList = new ArrayList<>(1);
        KeyboardRow next = new KeyboardRow();
        next.add(new KeyboardButton(NEXT_BUTTON_TEXT));
        nextList.add(next);
        rkm.setKeyboard(nextList);
        return rkm;
    }

    public static InlineKeyboardMarkup getTags(TagType tagType, Boolean isMultipleChoice) {
        InlineKeyboardMarkup ikm = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> tags = new ArrayList<>(1);
        int count = 0;
        int i = 0;
        List<InlineKeyboardButton> tagButton = new ArrayList<>(2);
        for (Tag tag : Tag.values()) {
            if (tag.getTagType() == tagType) {
                String text = tag.getName();
                String callbackData = String.format("%s %s", TAG_CALLBACK_DATA, tag.getName());
                if (isMultipleChoice) {
                    text = String.format(NOT_CHOSEN_TAG, text);
                    callbackData = String.format("%s %s %d %d 0", TAGS_CALLBACK_DATA, tag.getName(),
                            Math.floorDiv(count, 2), i % 2 == 0 ? 0 : 1);
                    count++;
                }

                tagButton.add(InlineKeyboardButton.builder()
                        .text(text)
                        .callbackData(callbackData)
                        .build());

                if (i++ % 2 == 1) {
                    tags.add(tagButton);
                    tagButton = new ArrayList<>(2);
                }
            }
        }
        if (i % 2 == 1) {
            tags.add(tagButton);
        }
        ikm.setKeyboard(tags);
        return ikm;
    }
}
