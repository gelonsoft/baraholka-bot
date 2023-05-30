package baraholkateam.command;

import baraholkateam.rest.service.LastSentMessageService;
import baraholkateam.util.State;
import baraholkateam.util.Tag;
import baraholkateam.util.TagType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
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

@Component
public abstract class Command extends BotCommand {
    public static final String BACK_BUTTON = "Назад";
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
    public static final String PHOTOS_DELETE = "Фотографии в создаваемом объявлении были успешно удалены.";
    public static final String SOCIALS_DELETE = "Социальные сети в создаваемом объявлении были успешно удалены.";
    public static final String NO_MORE_PHOTOS_ADD = """
            Число загруженных фотографий превышает 10 штук.
            В объявлении появятся первые 10 из всех загруженных фотографий.
            Если Вы хотите изменить загруженные фотографии, пожалуйста, нажмите кнопку 'Удалить все фотографии'.""";
    public static final String SWEAR_WORD_DETECTOR = "(?iu)\\b((у|[нз]а|(хитро|не)?вз?[ыьъ]|с[ьъ]|(и|ра)[зс]ъ?|"
            + "(о[тб]|под)[ьъ]?|(.\\B)+?[оаеи])?-?([её]б(?!о[рй])|и[пб][ае][тц]).*?|(н[иеа]|([дп]|верт)о|ра[зс]|"
            + "з?а|с(ме)?|о(т|дно)?|апч)?-?ху([яйиеёю]|ли(?!ган)).*?|(в[зы]|(три|два|четыре)жды|(н|сук)а)?"
            + "-?бл(я(?!(х|ш[кн]|мб)[ауеыио]).*?|[еэ][дт]ь?)|(ра[сз]|[зн]а|[со]|вы?|п(ере|р[оие]|од)|и[зс]ъ?|[ао]т)?п"
            + "[иеё]зд.*?|(за)?п[ие]д[аое]?р(ну.*?|[оа]м|(ас)?(и(ли)?[нщктл]ь?)?|(о(ч[еи])?|ас)?к(ой)|юг)[ауеы]?"
            + "|манд([ауеыи](л(и[сзщ])?[ауеиы])?|ой|[ао]вошь?(е?к[ауе])?|юк(ов|[ауи])?)|муд([яаио].*?|е?н([ьюия]|ей))"
            + "|мля([тд]ь)?|лять|([нз]а|по)х|м[ао]л[ао]фь([яию]|[еёо]й))\\b";
    public static final String AD_SWEAR_WORD_DETECTED = """
            Возможно, Ваше описание содержало ненормативную лексику.
            Пожалуйста, введите измененный текст""";
    public static final String CHOSEN_HASHTAGS = "Текущие выбранные хэштеги: %s";
    static final String INCORRECT_PREVIOUS_STATE = """
            Невозможно выполнить текущую команду.
            Пожалуйста, вернитесь в главное меню /%s и попробуйте снова.""";
    static final String NO_HASHTAGS = "➖";
    private static final Logger LOGGER = LoggerFactory.getLogger(Command.class);

    @Autowired
    private LastSentMessageService lastSentMessageService;

    public Command(String commandIdentifier, String description) {
        super(commandIdentifier, description);
    }

    void sendAnswer(AbsSender absSender, Long chatId, String commandName, String userName, String text,
                    ReplyKeyboard replyKeyboard) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        message.setParseMode(ParseMode.HTML);
        message.setText(text);
        if (replyKeyboard != null) {
            message.setReplyMarkup(replyKeyboard);
        } else {
            KeyboardButton back = new KeyboardButton();
            back.setText(BACK_BUTTON);
            KeyboardRow line = new KeyboardRow();
            line.add(back);
            List<KeyboardRow> lines = new ArrayList<>(1);
            lines.add(line);
            ReplyKeyboardMarkup rkm = new ReplyKeyboardMarkup();
            rkm.setKeyboard(lines);
            rkm.setResizeKeyboard(true);
            message.setReplyMarkup(rkm);
        }
        message.disableWebPagePreview();

        try {
            Message sentMessage = absSender.execute(message);
            lastSentMessageService.put(chatId, sentMessage);
        } catch (TelegramApiException e) {
            LOGGER.error(String.format("Cannot execute command %s of user %s: %s", commandName, userName,
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

        KeyboardButton back = new KeyboardButton();
        back.setText(BACK_BUTTON);
        KeyboardRow line = new KeyboardRow();
        line.add(back);
        nextList.add(line);

        KeyboardButton menu = new KeyboardButton();
        menu.setText(State.MainMenu.getDescription());
        KeyboardRow menuLine = new KeyboardRow();
        menuLine.add(menu);
        nextList.add(menuLine);

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

    ReplyKeyboardMarkup getReplyKeyboard(List<String> buttons, boolean isAddMenuButton) {
        List<KeyboardRow> lines = getLines(buttons);

        if (isAddMenuButton) {
            KeyboardButton menu = new KeyboardButton();
            menu.setText(State.MainMenu.getDescription());
            KeyboardRow menuLine = new KeyboardRow();
            menuLine.add(menu);
            lines.add(menuLine);
        }

        ReplyKeyboardMarkup rkm = new ReplyKeyboardMarkup();
        rkm.setKeyboard(lines);
        rkm.setResizeKeyboard(true);
        return rkm;
    }

    private List<KeyboardRow> getLines(List<String> buttons) {
        List<KeyboardRow> lines = new ArrayList<>(1);
        KeyboardRow line = new KeyboardRow();

        if (buttons.size() == 1) {
            line.add(buttons.get(0));
            lines.add(line);
        } else {
            for (int i = 0; i < buttons.size(); i++) {
                if (i % 2 == 0) {
                    line = new KeyboardRow();
                    line.add(buttons.get(i));
                } else {
                    line.add(buttons.get(i));
                    lines.add(line);
                }
            }
        }

        if (buttons.size() > 1 && buttons.size() % 2 == 1) {
            lines.add(line);
        }

        KeyboardButton back = new KeyboardButton();
        back.setText(BACK_BUTTON);
        line = new KeyboardRow();
        line.add(back);
        lines.add(line);

        return lines;
    }
}
