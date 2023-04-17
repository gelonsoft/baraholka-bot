package baraholkateam.bot;

import baraholkateam.command.*;
import baraholkateam.database.SQLExecutor;
import baraholkateam.util.Advertisement;
import baraholkateam.util.State;
import baraholkateam.util.Tag;
import baraholkateam.util.TagType;
import org.checkerframework.checker.units.qual.A;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import static baraholkateam.command.Command.*;
import static baraholkateam.command.NewAdvertisement_AddCityTags.ADD_CITY_CALLBACK_DATA;

@Component
public class BaraholkaBot extends TelegramLongPollingCommandBot {
    public static final Integer SEARCH_ADVERTISEMENTS_LIMIT = 10;
    public static final String CHOSEN_TAG = "✅ %s";
    public static final String SUCCESS_TEXT = "Объявление успешно добавлено";
    private final String botName;
    private final String botToken;
    private final NonCommand nonCommand;
    private final SQLExecutor sqlExecutor;
    private final Map<Long, State> currentState = new ConcurrentHashMap<>();
    private final Map<Long, Message> lastSentMessage = new ConcurrentHashMap<>();
    private final Map<Long, String> chosenTags = new ConcurrentHashMap<>();
    private final Map<Long, State> previousState = new ConcurrentHashMap<>();
    private final Logger logger = LoggerFactory.getLogger(BaraholkaBot.class);
    private static final Map<Long, Advertisement> newAdvertisements = new ConcurrentHashMap<>();

    public BaraholkaBot(@Value("${bot.name}") String botName, @Value("${bot.token}") String botToken) {
        super();
        this.botName = botName;
        this.botToken = botToken;

        sqlExecutor = new SQLExecutor();
        nonCommand = new NonCommand();

        register(new StartCommand(State.Start.getIdentifier(), State.Start.getDescription(), lastSentMessage));
        register(new HelpCommand(State.Help.getIdentifier(), State.Help.getDescription(), lastSentMessage,
                getRegisteredCommands()));
        register(new MainMenuCommand(State.MainMenu.getIdentifier(), State.MainMenu.getDescription(),
                lastSentMessage));
        register(new NewAdvertisementCommand(State.NewAdvertisement.getIdentifier(),
                State.NewAdvertisement.getDescription(), lastSentMessage));
        register(new NewAdvertisement_AddPhotos(State.NewAdvertisement_AddPhotos.getIdentifier(),
                State.NewAdvertisement_AddPhotos.getDescription(), lastSentMessage));
        register(new NewAdvertisement_ConfirmPhoto(State.NewAdvertisement_ConfirmPhoto.getIdentifier(),
                State.NewAdvertisement_ConfirmPhoto.getDescription(), lastSentMessage));
        register(new NewAdvertisement_AddDescription(State.NewAdvertisement_AddDescription.getIdentifier(),
                State.NewAdvertisement_AddDescription.getDescription(), lastSentMessage));
        register(new NewAdvertisement_ConfirmDescription(State.NewAdvertisement_ConfirmDescription.getIdentifier(),
                State.NewAdvertisement_ConfirmDescription.getDescription(), lastSentMessage));
        register(new NewAdvertisement_AddCityTags(State.NewAdvertisement_AddTags.getIdentifier(),
                State.NewAdvertisement_AddTags.getDescription(), lastSentMessage));
        register(new NewAdvertisement_ConfirmCity(State.NewAdvertisement_ConfirmCity.getIdentifier(),
                State.NewAdvertisement_ConfirmCity.getDescription(), lastSentMessage));
        register(new NewAdvertisement_AddType(State.NewAdvertisement_AddType.getIdentifier(),
                State.NewAdvertisement_AddType.getDescription(), lastSentMessage, chosenTags,
                previousState));
        register(new NewAdvertisement_ConfirmType(State.NewAdvertisement_ConfirmType.getIdentifier(),
                State.NewAdvertisement_ConfirmType.getDescription(), lastSentMessage));
        register(new NewAdvertisement_AddCategories(State.NewAdvertisement_AddCategories.getIdentifier(),
                State.NewAdvertisement_AddCategories.getDescription(), lastSentMessage, chosenTags,
                previousState));
        register(new NewAdvertisement_ConfirmCategories(State.NewAdvertisement_ConfirmCategories.getIdentifier(),
                State.NewAdvertisement_ConfirmCategories.getDescription(), lastSentMessage));
        register(new NewAdvertisement_AddPrice(State.NewAdvertisement_AddPrice.getIdentifier(),
                State.NewAdvertisement_AddPrice.getDescription(), lastSentMessage));
        register(new NewAdvertisement_ConfirmPrice(State.NewAdvertisement_ConfirmPrice.getIdentifier(),
                State.NewAdvertisement_ConfirmPrice.getDescription(), lastSentMessage));
        register(new NewAdvertisement_AddContacts(State.NewAdvertisement_AddContacts.getIdentifier(),
                State.NewAdvertisement_AddContacts.getDescription(), lastSentMessage));
        register(new NewAdvertisement_AddPhone(State.NewAdvertisement_AddPhone.getIdentifier(),
                State.NewAdvertisement_AddPhone.getDescription(), lastSentMessage));
        register(new NewAdvertisement_ConfirmPhone(State.NewAdvertisement_ConfirmPhone.getIdentifier(),
                State.NewAdvertisement_ConfirmPhone.getDescription(), lastSentMessage));
        register(new NewAdvertisement_AddSocial(State.NewAdvertisement_AddSocial.getIdentifier(),
                State.NewAdvertisement_AddSocial.getDescription(), lastSentMessage));
        register(new NewAdvertisement_Confirm(State.NewAdvertisement_Confirm.getIdentifier(),
                State.NewAdvertisement_Confirm.getDescription(), lastSentMessage));
        register(new SearchAdvertisements(State.SearchAdvertisements.getIdentifier(),
                State.SearchAdvertisements.getDescription(), lastSentMessage, chosenTags));
        register(new SearchAdvertisements_AddAdvertisementType(
                State.SearchAdvertisements_AddAdvertisementType.getIdentifier(),
                State.SearchAdvertisements_AddAdvertisementType.getDescription(), lastSentMessage, chosenTags,
                sqlExecutor, previousState));
        register(new SearchAdvertisements_AddProductCategories(
                State.SearchAdvertisements_AddProductCategories.getIdentifier(),
                State.SearchAdvertisements_AddProductCategories.getDescription(), lastSentMessage, chosenTags,
                previousState));
        register(new SearchAdvertisements_ShowFoundAdvertisements(
                State.SearchAdvertisements_ShowFoundAdvertisements.getIdentifier(),
                State.SearchAdvertisements_ShowFoundAdvertisements.getDescription(), lastSentMessage, chosenTags,
                sqlExecutor, previousState));
    }

    public static Advertisement getNewAdvertisement(Long chatId) {
        return newAdvertisements.get(chatId);
    }

    @Override
    public String getBotUsername() {
        return botName;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    /**
     * Устанавливает бота в определенное состояние в зависимости от введенной пользователем команды.
     * @param message отправленное пользователем сообщение
     * @return false, так как боту необходимо всегда обработать входящее сообщение
     */
    @Override
    public boolean filter(Message message) {
        //  Случай ввода команды по идентификатору
        State currentStateByIdentifier = State.findState(message.getText().replace("/", ""));
        if (currentStateByIdentifier != null) {
            this.currentState.put(message.getChatId(), currentStateByIdentifier);
            return false;
        }
        // Случай нажатия на кнопку с описанием команды
        State currentStateByDescription = State.findStateByDescription(message.getText());
        if (currentStateByDescription != null) {
            this.currentState.put(message.getChatId(), currentStateByDescription);
        }
        return false;
    }

    private void updateStateOnTextData(Message msg, Long chatId) {
        State nextState = State.nextState(currentState.get(msg.getChatId()));
        previousState.put(chatId, currentState.get(msg.getChatId()));
        currentState.put(chatId, nextState);
        getRegisteredCommand(nextState.getIdentifier()).processMessage(this, msg, null);
    }

    @Override
    public void processNonCommandUpdate(Update update) {
        Message msg;
        if (update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();
            msg = callbackQuery.getMessage();
            String callbackQueryData = callbackQuery.getData();
            parseKeyboardData(callbackQueryData, msg);
            return;
        }

        msg = update.getMessage();
        Long chatId = msg.getChatId();

        if (msg.hasPhoto() && currentState.get(chatId) == State.NewAdvertisement_AddPhotos) {
            if (!newAdvertisements.containsKey(chatId)) {
                newAdvertisements.put(chatId, new Advertisement(chatId));
            }
            Advertisement ad = newAdvertisements.get(chatId);
            List<PhotoSize> photos = msg.getPhoto();
            for (PhotoSize photo : photos) {
                ad.addPhoto(photo);
            }
            // TODO убрать эти две строчки чтобы менять стейт только после закидывания всех фоток
            getRegisteredCommand(State.NewAdvertisement_ConfirmPhoto.getIdentifier()).processMessage(this, msg, null);
            currentState.put(chatId, State.NewAdvertisement_ConfirmPhoto);
            return;
        }

        if (msg.hasText()) {
            State state = currentState.get(msg.getChatId());
            Advertisement ad = newAdvertisements.get(chatId);
            if (state == State.NewAdvertisement_AddDescription) {
                ad.setDescription(msg.getText());
                updateStateOnTextData(msg, chatId);
                return;
            }
            if (state == State.NewAdvertisement_AddPrice) {
                try {
                    ad.setPrice(Long.parseLong(msg.getText()));
                } catch (Exception e) {
                    logger.error("Invalid input from user");
                }
                updateStateOnTextData(msg, chatId);
                return;
            }
            if (state == State.NewAdvertisement_AddPhone) {
                ad.setPhone(msg.getText());
                updateStateOnTextData(msg, chatId);
                return;
            }
            if (state == State.NewAdvertisement_AddSocial) {
                ad.addSocial(msg.getText());
                updateStateOnTextData(msg, chatId);
                return;
            }
        }

        // Случай нажатия на кнопку с описанием команды
        State stateByDescription = State.findStateByDescription(msg.getText());
        if (stateByDescription != null) {
            // TODO убрать если хотим сохранять недоделанные объявления
            if (stateByDescription == State.MainMenu) {
                newAdvertisements.remove(chatId);
            }
            currentState.put(chatId, stateByDescription);
            getRegisteredCommand(stateByDescription.getIdentifier()).processMessage(this, msg, null);
            return;
        }

        // Случай нажатия на кнопку с продолжением во множественном выборе хэштегов
        if (Objects.equals(msg.getText(), NEXT_BUTTON_TEXT)) {
            if (currentState.get(msg.getChatId()) == State.NewAdvertisement_AddType
                    || currentState.get(msg.getChatId()) == State.NewAdvertisement_AddCategories) {
                Advertisement ad = newAdvertisements.get(chatId);
                // TODO переписать под список тегов
                if (chosenTags.get(chatId) != null) {
                    List<String> tags = Arrays.stream(chosenTags.get(chatId).split(" ")).toList();
                    ad.addTags(tags);
                }
            }
            addSearchTags(lastSentMessage.get(chatId));
            deleteLastMessage(msg.getChatId());
            State nextState = State.nextState(currentState.get(msg.getChatId()));
            previousState.put(chatId, currentState.get(msg.getChatId()));
            currentState.put(chatId, nextState);
            getRegisteredCommand(nextState.getIdentifier()).processMessage(this, msg, null);
            return;
        }
        executeNonCommand(msg, chatId, currentState.get(chatId));
    }

    private void executeNonCommand(Message msg, Long chatId, State currState) {
        List<NonCommand.AnswerPair> answers = nonCommand.nonCommandExecute(msg, currState);
        if (answers.get(0).getError()) {
            for (NonCommand.AnswerPair answer : answers) {
                SendMessage sendMessage = new SendMessage();
                sendMessage.setText(answer.getAnswer());
                sendMessage.setParseMode(ParseMode.HTML);
                sendMessage.setChatId(chatId.toString());
                sendMessage.disableWebPagePreview();
                try {
                    execute(sendMessage);
                } catch (TelegramApiException e) {
                    logger.error(String.format("Cannot send message: %s", e.getMessage()));
                }
            }
            // ошибка в обработке сообщения пользователя, необходимо повторить данный шаг
            deleteLastMessage(msg.getChatId());
            getRegisteredCommand(currState.getIdentifier()).processMessage(this, msg, null);
            return;
        } else {
            State nextState = State.nextState(currentState.get(msg.getChatId()));
            previousState.put(chatId, currentState.get(msg.getChatId()));
            currentState.put(chatId, nextState);
        }
        // ошибки в обработке сообщения пользователя нет, отправляем ответ и переходим на следующий шаг
        for (NonCommand.AnswerPair answer : answers) {
            if (!answer.getError()) {
                sendAnswer(chatId, getUserName(msg), answer.getAnswer(),
                        answer.getReplyKeyboard() == null ? null : answer.getReplyKeyboard());
            }
        }
    }

    private String getUserName(Message msg) {
        User user = msg.getFrom();
        String userName = user.getUserName();
        return (userName != null) ? userName : String.format("%s %s", user.getLastName(), user.getFirstName());
    }

    private void sendAnswer(Long chatId, String userName, String text, ReplyKeyboard replyKeyboard) {
        SendMessage answer = new SendMessage();
        answer.setText(text);
        answer.setParseMode(ParseMode.HTML);
        answer.setChatId(chatId.toString());
        if (replyKeyboard != null) {
            answer.setReplyMarkup(replyKeyboard);
        }
        answer.disableWebPagePreview();

        try {
            Message sentMessage = execute(answer);
            lastSentMessage.put(chatId, sentMessage);
        } catch (TelegramApiException e) {
            logger.error(String.format("Cannot execute command of user %s: %s", userName, e.getMessage()));
        }
    }

    private void addSearchTags(Message message) {
        List<List<InlineKeyboardButton>> buttons = message.getReplyMarkup().getKeyboard();
        for (List<InlineKeyboardButton> tag : buttons) {
            String[] dataCallbackParts = tag.get(0).getCallbackData().split(" ");
            if (Objects.equals(dataCallbackParts[dataCallbackParts.length - 1], "1")) {
                String chosenTagsString = chosenTags.get(message.getChatId());
                String newTags = tag.get(0).getText().split(" ")[1];
                chosenTags.put(message.getChatId(),
                        chosenTagsString == null ? newTags : String.format("%s %s", chosenTagsString, newTags));
            }
        }
    }

    private void parseKeyboardData(String callbackQuery, Message msg) {
        String[] dataParts = callbackQuery.split(" ");
        switch (dataParts[0]) {
            case TAG_CALLBACK_DATA -> {
                String currentChosenTags = chosenTags.get(msg.getChatId());
                chosenTags.put(msg.getChatId(), currentChosenTags == null
                        ? dataParts[1] : String.format("%s %s", currentChosenTags, dataParts[1]));
                deleteLastMessage(msg.getChatId());
                State nextState = State.nextState(currentState.get(msg.getChatId()));
                previousState.put(msg.getChatId(), currentState.get(msg.getChatId()));
                currentState.put(msg.getChatId(), nextState);
                getRegisteredCommand(nextState.getIdentifier()).processMessage(this, msg, null);
            }
            case TAGS_CALLBACK_DATA -> {
                List<List<InlineKeyboardButton>> buttons =
                        lastSentMessage.get(msg.getChatId()).getReplyMarkup().getKeyboard();
                InlineKeyboardButton changeTag = buttons.get(Integer.parseInt(dataParts[2])).get(0);
                if (Objects.equals(dataParts[3], "0")) {
                    changeTag.setText(String.format(CHOSEN_TAG, changeTag.getText().split(" ")[1]));
                    changeTag.setCallbackData(changeTag
                            .getCallbackData()
                            .substring(0, changeTag.getCallbackData().length() - 2)
                            .concat(" 1"));
                    editMessageReplyMarkup(msg.getChatId(), buttons);
                } else {
                    changeTag.setText(String.format(NOT_CHOSEN_TAG, changeTag.getText().split(" ")[1]));
                    changeTag.setCallbackData(changeTag
                            .getCallbackData()
                            .substring(0, changeTag.getCallbackData().length() - 2)
                            .concat(" 0"));
                    editMessageReplyMarkup(msg.getChatId(), buttons);
                }
            }
            case ADD_CITY_CALLBACK_DATA -> {
                if (currentState.get(msg.getChatId()) == State.NewAdvertisement_AddTags) {
                    newAdvertisements.get(msg.getChatId()).setCity(Tag.getTagByName(dataParts[1]));
                }
                State nextState = State.nextState(currentState.get(msg.getChatId()));
                previousState.put(msg.getChatId(), currentState.get(msg.getChatId()));
                currentState.put(msg.getChatId(), nextState);
                getRegisteredCommand(nextState.getIdentifier()).processMessage(this, msg, null);
            }
            case PHONE_CALLBACK_DATA -> {
                if (Objects.equals(dataParts[1], "yes")) {
                    currentState.put(msg.getChatId(), State.NewAdvertisement_AddPhone);
                    getRegisteredCommand(State.NewAdvertisement_AddPhone.getIdentifier()).processMessage(this, msg, null);
                } else if (Objects.equals(dataParts[1], "no")) {
                    currentState.put(msg.getChatId(), State.NewAdvertisement_AddSocial);
                    getRegisteredCommand(State.NewAdvertisement_AddSocial.getIdentifier()).processMessage(this, msg, null);
                }
            }
            case SOCIAL_CALLBACK_DATA -> {
                if (Objects.equals(dataParts[1], "yes")) {
                    currentState.put(msg.getChatId(), State.NewAdvertisement_AddSocial);
                    getRegisteredCommand(State.NewAdvertisement_AddSocial.getIdentifier()).processMessage(this, msg, null);
                } else if (Objects.equals(dataParts[1], "no")) {
                    currentState.put(msg.getChatId(), State.NewAdvertisement_Confirm);
                    getRegisteredCommand(State.NewAdvertisement_Confirm.getIdentifier()).processMessage(this, msg, null);
                }
            }
            case CONFIRM_AD_CALLBACK_DATA -> {
                if (Objects.equals(dataParts[1], "yes")) {
                    sqlExecutor.insertNewAdvertisement(newAdvertisements.get(msg.getChatId()));
                    sendAnswer(msg.getChatId(), getUserName(msg), SUCCESS_TEXT, null);
                } else if (Objects.equals(dataParts[1], "no")) {
                    // TODO обработать сброс объявления
                }
            }
            default -> logger.error(String.format("Unknown command in callback data: %s", callbackQuery));
        }
    }

    private void deleteLastMessage(Long chatId) {
        DeleteMessage deleteLastMessage = new DeleteMessage();
        deleteLastMessage.setMessageId(lastSentMessage.get(chatId).getMessageId());
        deleteLastMessage.setChatId(chatId);
        try {
            execute(deleteLastMessage);
        } catch (TelegramApiException e) {
            logger.error(String.format("Cannot delete last message due to: %s", e.getMessage()));
        }
    }

    private void editMessageReplyMarkup(Long chatId, List<List<InlineKeyboardButton>> buttons) {
        InlineKeyboardMarkup ikm = new InlineKeyboardMarkup();
        ikm.setKeyboard(buttons);
        EditMessageReplyMarkup editMessageReplyMarkup = new EditMessageReplyMarkup();
        editMessageReplyMarkup.setChatId(chatId);
        editMessageReplyMarkup.setMessageId(lastSentMessage.get(chatId).getMessageId());
        editMessageReplyMarkup.setReplyMarkup(ikm);
        try {
            execute(editMessageReplyMarkup);
        } catch (TelegramApiException e) {
            logger.error(String.format("Cannot edit message reply markup due to: %s", e.getMessage()));
        }
    }
}
