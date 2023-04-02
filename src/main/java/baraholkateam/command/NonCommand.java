package baraholkateam.command;

import baraholkateam.bot.BaraholkaBotProperties;
import baraholkateam.database.SQLExecutor;
import baraholkateam.telegram_api_requests.TelegramAPIRequests;
import baraholkateam.util.Advertisement;
import baraholkateam.util.IState;
import baraholkateam.util.State;
import baraholkateam.util.Substate;
import baraholkateam.util.Tag;
import baraholkateam.util.TagType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import static baraholkateam.bot.BaraholkaBot.SEARCH_ADVERTISEMENTS_LIMIT;

public class NonCommand {
    private static final String NO_CURRENT_STATE = """
            Ошибка в текущем состоянии бота.
            Пожалуйста, вернитесь в /%s и следуйте инструкциям.""";
    private static final String COMMAND_ERROR_MESSAGE = """
            Команда /%s не предполагает ввод сообщений.
            Пожалуйста, вернитесь в /%s и следуйте инструкциям.""";
    private static final String UNKNOWN_COMMAND = """
            Введеная команда не понятна боту.
            Пожалуйста, вернитесь в /%s и следуйте инструкциям.""";
    private static final String CHOOSE_ADVERTISEMENT_TYPES = "Пожалуйста, выберите типы объявления.";
    private static final String CHOSEN_HASHTAGS = "Текущие выбранные хэштеги: %s";
    private static final String CHOOSE_ADVERTISEMENT_TYPE = "Выберите тип объявления:";
    private static final String CHOOSE_PRODUCT_CATEGORIES = "Пожалуйста, выберите категории объявлений.";
    private static final String CHOOSE_PRODUCT_CATEGORY = "Выберите категории товаров:";
    private static final String CANNOT_FIND_ADVERTISEMENTS = """
            По Вашему запросу ничего не нашлось.
            Вы можете вернуться в главное меню /%s или найти объявления по другим хэштегам /%s.""";
    private static final String FOUND_ADVERTISEMENTS = """
            По Вашему запросу нашлось объявлений: %d.
            Показывается не более %s самых актуальных объявлений.
            Вы можете вернуться в главное меню /%s или найти объявления по другим хэштегам /%s.""";
    public static final String NOT_CHOSEN_TAG = "➖ %s";
    public static final String NEXT_BUTTON_TEXT = "Продолжить";
    public static final String TAG_CALLBACK_DATA = "tag";
    public static final String TAGS_CALLBACK_DATA = "tags";
    private final SQLExecutor sqlExecutor;
    private final Map<Long, String> chosenTags;
    private final TelegramAPIRequests telegramAPIRequests;
    private final Logger logger = LoggerFactory.getLogger(NonCommand.class);

    public NonCommand(SQLExecutor sqlExecutor, Map<Long, String> chosenTags) {
        this.sqlExecutor = sqlExecutor;
        this.chosenTags = chosenTags;
        telegramAPIRequests = new TelegramAPIRequests();
    }

    public List<AnswerPair> nonCommandExecute(Message msg, Long chatId, IState currentState) {
        if (currentState == null) {
            return List.of(new AnswerPair(String.format(NO_CURRENT_STATE, State.MainMenu.getIdentifier()), true, null));
        }

        if (currentState.equals(State.Start) || currentState.equals(State.Help) || currentState.equals(State.MainMenu)
                || currentState.equals(State.NewAdvertisement)) {
            return List.of(new AnswerPair(String.format(COMMAND_ERROR_MESSAGE, currentState.getIdentifier(),
                    State.MainMenu.getIdentifier()), true, null));
        } else if (currentState.equals(Substate.AddPhotos)) {
            // TODO сделать обработку добавленных фотографий согласно ТЗ. Может быть также нарушения правил добавления
            //  фотографий, тогда нужна соответствующая обработка
            return List.of(new AnswerPair("Фотографии успешно отправлены.", false, null));
        } else if (currentState.equals(Substate.AddCity)) {
            // TODO аналогично предыдущему
            return List.of(new AnswerPair("Город успешно добавлен.", false, null));
        } else if (currentState.equals(Substate.AddDescription)) {
            // TODO аналогично предыдущему
            return List.of(new AnswerPair("Описание успешно добавлено.", false, null));
        } else if (currentState.equals(State.SearchAdvertisements)) {
            return List.of(new AnswerPair("Пожалуйста, выберите город для поиска.", true, null));
        } else if (currentState.equals(Substate.SearchAdvertisements_ChooseAdvertisementType)) {
            // TODO убрать метод
            sqlExecutor.removeAllData();
            // TODO убрать метод-заглушку
            createAds();

            if (!msg.hasReplyMarkup() && !Objects.equals(msg.getText(), NEXT_BUTTON_TEXT)) {
                return List.of(new AnswerPair(CHOOSE_ADVERTISEMENT_TYPES, true, null));
            }

            return List.of(new AnswerPair(String.format(CHOSEN_HASHTAGS, chosenTags.get(chatId)),
                            false, showNextButton()),
                    new AnswerPair(CHOOSE_ADVERTISEMENT_TYPE, false, getTags(TagType.AdvertisementType, true)));
        } else if (currentState.equals(Substate.SearchAdvertisements_ChooseProductCategories)) {
            if (!msg.hasReplyMarkup() && !Objects.equals(msg.getText(), NEXT_BUTTON_TEXT)) {
                return List.of(new AnswerPair(CHOOSE_PRODUCT_CATEGORIES, true, null));
            }

            return List.of(new AnswerPair(String.format(CHOSEN_HASHTAGS, chosenTags.get(chatId)),
                            false, showNextButton()),
                    new AnswerPair(CHOOSE_PRODUCT_CATEGORY, false, getTags(TagType.ProductCategories, true)));
        } else if (currentState.equals(Substate.SearchAdvertisements_ShowFoundAdvertisements)) {
            ResultSet messageIds = sqlExecutor.tagsSearch(Arrays.stream(chosenTags.get(chatId).split(" "))
                    .map(Tag::getTagByName)
                    .toList());
            int count = 0;
            while (true) {
                try {
                    if (!messageIds.next()) break;
                    telegramAPIRequests.forwardMessage(BaraholkaBotProperties.CHANNEL_USERNAME, String.valueOf(chatId),
                            messageIds.getLong("message_id"));
                    count++;
                } catch (SQLException e) {
                    logger.error(String.format("Cannot handle sql: %s", e.getMessage()));
                    throw new RuntimeException("Failed to handle sql", e);
                }
            }
            AnswerPair chosenTagsAnswer = new AnswerPair(String.format(CHOSEN_HASHTAGS, chosenTags.get(chatId)),
                    false, null);
            if (count == 0) {
                return List.of(chosenTagsAnswer, new AnswerPair(String.format(CANNOT_FIND_ADVERTISEMENTS,
                        State.MainMenu.getIdentifier(),
                        State.SearchAdvertisements.getIdentifier()),
                        false,
                        showCommandButtons(List.of(State.MainMenu.getDescription(),
                                State.SearchAdvertisements.getDescription())))
                );
            }
            return List.of(chosenTagsAnswer, new AnswerPair(String.format(FOUND_ADVERTISEMENTS,
                    count,
                    SEARCH_ADVERTISEMENTS_LIMIT,
                    State.MainMenu.getIdentifier(),
                    State.SearchAdvertisements.getIdentifier()),
                    false,
                    showCommandButtons(List.of(State.MainMenu.getDescription(),
                            State.SearchAdvertisements.getDescription()))));
        }
        // TODO добавить обработку всех возможных состояний и подсостояний
        return List.of(new AnswerPair(String.format(UNKNOWN_COMMAND, State.MainMenu.getIdentifier()), true, null));
    }

    /**
     * Хранит ответ бота с индикатором ранее присланного ошибочного сообщения от пользователя.
     */
    public static class AnswerPair {
        private final String answer;
        private final Boolean isError;
        private final ReplyKeyboard inlineKeyboardMarkup;

        public AnswerPair(String answer, boolean isError, ReplyKeyboard inlineKeyboardMarkup) {
            this.answer = answer;
            this.isError = isError;
            this.inlineKeyboardMarkup = inlineKeyboardMarkup;
        }

        public String getAnswer() {
            return answer;
        }

        public Boolean getError() {
            return isError;
        }

        public ReplyKeyboard getReplyKeyboard() {
            return inlineKeyboardMarkup;
        }
    }

    public static InlineKeyboardMarkup getTags(TagType tagType, Boolean isMultipleChoice) {
        InlineKeyboardMarkup ikm = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> tags = new ArrayList<>(1);
        int count = 0;
        for (Tag tag : Tag.values()) {
            if (tag.getTagType() == tagType) {
                List<InlineKeyboardButton> tagButton = new ArrayList<>(1);

                String text = tag.getName();
                String callbackData = String.format("%s %s", TAG_CALLBACK_DATA, tag.getName());
                if (isMultipleChoice) {
                    text = String.format(NOT_CHOSEN_TAG, text);
                    callbackData = String.format("%s %s %d 0", TAGS_CALLBACK_DATA, tag.getName(), count++);
                }

                tagButton.add(InlineKeyboardButton.builder()
                        .text(text)
                        .callbackData(callbackData)
                        .build());
                tags.add(tagButton);
            }
        }
        ikm.setKeyboard(tags);
        return ikm;
    }

    private ReplyKeyboardMarkup showNextButton() {
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

    private ReplyKeyboardMarkup showCommandButtons(List<String> commands) {
        ReplyKeyboardMarkup rkm = new ReplyKeyboardMarkup();
        rkm.setSelective(true);
        rkm.setResizeKeyboard(true);
        rkm.setOneTimeKeyboard(true);
        List<KeyboardRow> commandButtons = new ArrayList<>(commands.size());
        for (String command : commands) {
            KeyboardRow commandButton = new KeyboardRow();
            commandButton.add(new KeyboardButton(command));
            commandButtons.add(commandButton);
        }
        rkm.setKeyboard(commandButtons);
        return rkm;
    }

    // TODO убрать метод-заглушку
    private void createAds() {
        Advertisement ad1 = new Advertisement(new ArrayList<>(1), "описание1", List.of(Tag.Exchange, Tag.Sale, Tag.Belgorod, Tag.Hobby), 1000L, List.of("контакт 1", "контакт 2"))
                .setChatId(1L)
                .setMessageId(2L)
                .setCreationTime(123L)
                .setNextUpdateTime(456L);
        Advertisement ad2 = new Advertisement(new ArrayList<>(1), "описание2", List.of(Tag.Exchange, Tag.Sale, Tag.Belgorod), 2000L, List.of("контакт 3"))
                .setChatId(3L)
                .setMessageId(4L)
                .setCreationTime(789L)
                .setNextUpdateTime(101112L);
        Advertisement ad3 = new Advertisement(new ArrayList<>(1), "описание3", List.of(Tag.Sale, Tag.Belgorod, Tag.Hobby), 3000L, List.of("контакт 4", "контакт 5", "контакт 6"))
                .setChatId(5L)
                .setMessageId(6L)
                .setCreationTime(131415L)
                .setNextUpdateTime(161718L);
        Advertisement ad4 = new Advertisement(new ArrayList<>(1), "описание4", List.of(Tag.Exchange, Tag.Sale, Tag.Belgorod, Tag.Hobby, Tag.Books), 4000L, new ArrayList<>(1))
                .setChatId(7L)
                .setMessageId(8L)
                .setCreationTime(192021L)
                .setNextUpdateTime(222324L);
        Advertisement ad5 = new Advertisement(new ArrayList<>(1), "описание5", List.of(Tag.Exchange, Tag.Sale, Tag.Belgorod, Tag.Hobby, Tag.Ekaterinburg), 5000L, List.of("контакт 7"))
                .setChatId(9L)
                .setMessageId(10L)
                .setCreationTime(252627L)
                .setNextUpdateTime(282930L);

        sqlExecutor.insertNewAdvertisement(ad1);
        sqlExecutor.insertNewAdvertisement(ad2);
        sqlExecutor.insertNewAdvertisement(ad3);
        sqlExecutor.insertNewAdvertisement(ad4);
        sqlExecutor.insertNewAdvertisement(ad5);
    }
}
