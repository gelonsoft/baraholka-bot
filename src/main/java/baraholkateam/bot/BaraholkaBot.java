package baraholkateam.bot;

import baraholkateam.command.*;
import baraholkateam.command.HelpCommand;
import baraholkateam.command.MainMenuCommand;
import baraholkateam.command.NewAdvertisementCommand;
import baraholkateam.command.NewAdvertisement_AddAdvertisementTypes;
import baraholkateam.command.NewAdvertisement_AddCategories;
import baraholkateam.command.NewAdvertisement_AddCity;
import baraholkateam.command.NewAdvertisement_AddContacts;
import baraholkateam.command.NewAdvertisement_AddDescription;
import baraholkateam.command.NewAdvertisement_AddPhone;
import baraholkateam.command.NewAdvertisement_AddPhotos;
import baraholkateam.command.NewAdvertisement_AddPrice;
import baraholkateam.command.NewAdvertisement_AddSocial;
import baraholkateam.command.NewAdvertisement_Confirm;
import baraholkateam.command.NewAdvertisement_ConfirmPhone;
import baraholkateam.command.NewAdvertisement_ConfirmPhoto;
import baraholkateam.command.NewAdvertisement_ConfirmPrice;
import baraholkateam.command.NonCommand;
import baraholkateam.command.SearchAdvertisements;
import baraholkateam.command.SearchAdvertisements_AddAdvertisementTypes;
import baraholkateam.command.SearchAdvertisements_AddProductCategories;
import baraholkateam.command.SearchAdvertisements_ShowFoundAdvertisements;
import baraholkateam.command.StartCommand;
import baraholkateam.database.SQLExecutor;
import baraholkateam.notification.NotificationExecutor;
import baraholkateam.telegram_api_requests.TelegramAPIRequests;
import baraholkateam.util.Advertisement;
import baraholkateam.util.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.boot.autoconfigure.integration.IntegrationProperties;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.nio.channels.Channel;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static baraholkateam.command.Command.ADVERTISEMENT_CANCELLED_TEXT;
import static baraholkateam.command.Command.ADVERTISEMENT_SUCCESSFUL_DELETE;
import static baraholkateam.command.Command.ADVERTISEMENT_SUCCESSFUL_UPDATE;
import static baraholkateam.command.Command.CHOSEN_TAG;
import static baraholkateam.command.Command.CONFIRM_AD_CALLBACK_DATA;
import static baraholkateam.command.Command.NEXT_BUTTON_TEXT;
import static baraholkateam.command.Command.NOTIFICATION_CALLBACK_DATA;
import static baraholkateam.command.Command.NOT_CHOSEN_TAG;
import static baraholkateam.command.Command.PHONE_CALLBACK_DATA;
import static baraholkateam.command.Command.SOCIAL_CALLBACK_DATA;
import static baraholkateam.command.Command.SUCCESS_TEXT;
import static baraholkateam.command.Command.TAGS_CALLBACK_DATA;
import static baraholkateam.command.Command.TAG_CALLBACK_DATA;
import static baraholkateam.command.Command.UNSUCCESS_TEXT;
import static baraholkateam.notification.NotificationExecutor.FIRST_REPEAT_NOTIFICATION_PERIOD;
import static baraholkateam.notification.NotificationExecutor.FIRST_REPEAT_NOTIFICATION_TIME_UNIT;
import static baraholkateam.command.Command.*;

@Component
public class BaraholkaBot extends TelegramLongPollingCommandBot {
    public static final Integer SEARCH_ADVERTISEMENTS_LIMIT = 10;
    private final String botName;
    private final String botToken;
    private final NonCommand nonCommand;
    private final SQLExecutor sqlExecutor;
    private final TelegramAPIRequests telegramAPIRequests;
    private final NewAdvertisement_Confirm newAdvertisementConfirm;
    private final Map<Long, State> currentState = new ConcurrentHashMap<>();
    private final Map<Long, Message> lastSentMessage = new ConcurrentHashMap<>();
    private final Map<Long, String> chosenTags = new ConcurrentHashMap<>();
    private final Map<Long, State> previousState = new ConcurrentHashMap<>();
    private final Map<Long, Advertisement> advertisement = new ConcurrentHashMap<>();
    private final Map<Long, Map<Long, List<Message>>> notificationMessages = new ConcurrentHashMap<>();
    private final Logger logger = LoggerFactory.getLogger(BaraholkaBot.class);

    public BaraholkaBot(@Value("${bot.name}") String botName, @Value("${bot.token}") String botToken) {
        super();
        this.botName = botName;
        this.botToken = botToken;

        sqlExecutor = new SQLExecutor();
        nonCommand = new NonCommand();
        telegramAPIRequests = new TelegramAPIRequests();

        register(new StartCommand(State.Start.getIdentifier(), State.Start.getDescription(), lastSentMessage));
        register(new HelpCommand(State.Help.getIdentifier(), State.Help.getDescription(), lastSentMessage,
                getRegisteredCommands()));
        register(new MainMenuCommand(State.MainMenu.getIdentifier(), State.MainMenu.getDescription(),
                lastSentMessage));
        register(new NewAdvertisementCommand(State.NewAdvertisement.getIdentifier(),
                State.NewAdvertisement.getDescription(), lastSentMessage));
        register(new DeleteAd(State.DeleteAd.getIdentifier(), State.DeleteAd.getDescription(), sqlExecutor, lastSentMessage));
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
        NotificationExecutor.startNotificationExecutor(sqlExecutor, this, telegramAPIRequests,
                notificationMessages);

        newAdvertisementConfirm = new NewAdvertisement_Confirm(lastSentMessage, advertisement,
                telegramAPIRequests, this);
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

    @Override
    public void onRegister() {
        super.onRegister();

        register(new StartCommand(lastSentMessage));
        register(new HelpCommand(lastSentMessage));
        register(new MainMenuCommand(lastSentMessage));
        register(new NewAdvertisementCommand(lastSentMessage, advertisement, chosenTags));
        register(new NewAdvertisement_AddPhotos(lastSentMessage));
        register(new NewAdvertisement_ConfirmPhoto(lastSentMessage));
        register(new NewAdvertisement_AddDescription(lastSentMessage));
        register(new NewAdvertisement_AddCity(lastSentMessage));
        register(new NewAdvertisement_AddAdvertisementTypes(lastSentMessage, advertisement));
        register(new NewAdvertisement_AddCategories(lastSentMessage, advertisement));
        register(new NewAdvertisement_AddPrice(lastSentMessage, advertisement));
        register(new NewAdvertisement_ConfirmPrice(lastSentMessage, advertisement));
        register(new NewAdvertisement_AddContacts(lastSentMessage));
        register(new NewAdvertisement_AddPhone(lastSentMessage));
        register(new NewAdvertisement_ConfirmPhone(lastSentMessage, advertisement));
        register(new NewAdvertisement_AddSocial(lastSentMessage));
        register(newAdvertisementConfirm);
        register(new SearchAdvertisements(lastSentMessage, chosenTags));
        register(new SearchAdvertisements_AddAdvertisementTypes(lastSentMessage, chosenTags, sqlExecutor,
                previousState));
        register(new SearchAdvertisements_AddProductCategories(lastSentMessage, chosenTags, previousState));
        register(new SearchAdvertisements_ShowFoundAdvertisements(lastSentMessage, chosenTags, sqlExecutor,
                previousState, telegramAPIRequests));
    }

    @Override
    public void processNonCommandUpdate(Update update) {
        Message msg;

        // Случай получения информации с кнопок (инлайн-клавиатуры)
        if (update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();
            msg = callbackQuery.getMessage();
            String callbackQueryData = callbackQuery.getData();
            parseKeyboardData(callbackQueryData, msg);
            return;
        }

        msg = update.getMessage();
        Long chatId = msg.getChatId();

        // Случай обновления данных во время создания объявления
        if (msg.hasText() && newAdvertisementUpdateData(msg)) {
            return;
        }

        // Случай добавления новых фотографий во время создания объявления
        if (msg.hasPhoto() && addAdvertisementPhotos(msg)) {
            return;
        }

        // Случай нажатия на кнопку с описанием команды
        State stateByDescription = State.findStateByDescription(msg.getText());
        if (stateByDescription != null) {
            currentState.put(chatId, stateByDescription);
            getRegisteredCommand(stateByDescription.getIdentifier()).processMessage(this, msg, null);
            return;
        }

        // Случай нажатия на кнопку с продолжением во множественном выборе хэштегов
        if (Objects.equals(msg.getText(), NEXT_BUTTON_TEXT)) {
            addChosenTags(lastSentMessage.get(chatId));

            if (currentState.get(msg.getChatId()) == State.NewAdvertisement_AddAdvertisementTypes
                    || currentState.get(msg.getChatId()) == State.NewAdvertisement_AddCategories) {
                Advertisement ad = advertisement.get(chatId);
                if (chosenTags.get(chatId) != null) {
                    List<String> tags = Arrays.stream(chosenTags.get(chatId).split(" ")).toList();
                    ad.addTags(tags);
                    chosenTags.clear();
                }
            }

            deleteLastMessage(msg.getChatId());
            State nextState = State.nextState(currentState.get(msg.getChatId()));
            previousState.put(chatId, currentState.get(msg.getChatId()));
            currentState.put(chatId, nextState);
            getRegisteredCommand(nextState.getIdentifier()).processMessage(this, msg, null);
            return;
        }

        // Случай обработки текстовой информации от пользователя
        executeNonCommand(msg, chatId, currentState.get(chatId));
    }

    private boolean newAdvertisementUpdateData(Message msg) {
        State state = currentState.get(msg.getChatId());
        Advertisement ad = advertisement.get(msg.getChatId());

        if (state == State.NewAdvertisement_AddDescription) {
            if (!msg.getText().matches(".+")) {
                return false;
            }
            ad.setDescription(msg.getText());
            updateStateOnTextData(msg);
            return true;
        }

        if (state == State.NewAdvertisement_AddPrice) {
            if (!msg.getText().matches("\\d{1,18}")) {
                return false;
            }
            try {
                ad.setPrice(Long.parseLong(msg.getText()));
            } catch (Exception e) {
                logger.error("Invalid input from user");
            }
            updateStateOnTextData(msg);
            return true;
        }

        if (state == State.NewAdvertisement_AddPhone) {
            if (!msg.getText().matches("8-\\d{3}-\\d{3}-\\d{2}-\\d{2}")
                    && !msg.getText().matches("\\+7-\\d{3}-\\d{3}-\\d{2}-\\d{2}")) {
                return false;
            }
            ad.setPhone(msg.getText());
            updateStateOnTextData(msg);
            return true;
        }

        if (state == State.NewAdvertisement_AddSocial) {
            if (!msg.getText().matches("https://.+/.+")) {
                return false;
            }
            ad.addSocial(msg.getText());
            updateStateOnTextData(msg);
            return true;
        }

        // TODO убрать если хотим сохранять недоделанные объявления
        if (state == State.MainMenu) {
            advertisement.remove(msg.getChatId());
        }

        return false;
    }

    private boolean addAdvertisementPhotos(Message msg) {
        if (currentState.get(msg.getChatId()) == State.NewAdvertisement_AddPhotos) {
            Advertisement ad = advertisement.get(msg.getChatId());

            Map<String, TreeSet<PhotoSize>> photos = msg.getPhoto().stream()
                    .collect(
                            Collectors.groupingBy(
                                    // FIXME мапим пока только по первой половине символов id фотографий
                                    photo -> photo.getFileId().substring(0, photo.getFileId().length() / 2),
                                    Collectors.toCollection(
                                            () -> new TreeSet<>(Comparator.comparingInt(PhotoSize::getWidth))
                                    )
                            )
                    );

            photos.forEach((make, photo) -> ad.addPhoto(photo.last()));

            // TODO убрать эти две строчки чтобы менять стейт только после закидывания всех фоток
            getRegisteredCommand(State.NewAdvertisement_ConfirmPhoto.getIdentifier())
                    .processMessage(this, msg, null);
            currentState.put(msg.getChatId(), State.NewAdvertisement_ConfirmPhoto);
            return true;
        }
        return false;
    }

    private void updateStateOnTextData(Message msg) {
        State nextState = State.nextState(currentState.get(msg.getChatId()));
        previousState.put(msg.getChatId(), currentState.get(msg.getChatId()));
        currentState.put(msg.getChatId(), nextState);
        getRegisteredCommand(nextState.getIdentifier()).processMessage(this, msg, null);
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
                sendAnswer(chatId, answer.getAnswer(),
                        answer.getReplyKeyboard() == null ? null : answer.getReplyKeyboard());
            }
        }
    }

    private void sendAnswer(Long chatId, String text, ReplyKeyboard replyKeyboard) {
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
            logger.error(String.format("Cannot execute command: %s", e.getMessage()));
        }
    }

    private void addChosenTags(Message message) {
        List<List<InlineKeyboardButton>> buttons = message.getReplyMarkup().getKeyboard();
        for (List<InlineKeyboardButton> tag : buttons) {
            for (InlineKeyboardButton inlineKeyboardButton : tag) {
                String[] dataCallbackParts = inlineKeyboardButton.getCallbackData().split(" ");
                if (Objects.equals(dataCallbackParts[dataCallbackParts.length - 1], "1")) {
                    String chosenTagsString = chosenTags.get(message.getChatId());
                    String newTags = inlineKeyboardButton.getText().split(" ")[1];
                    chosenTags.put(message.getChatId(),
                            chosenTagsString == null ? newTags : String.format("%s %s", chosenTagsString, newTags));
                }
            }
        }
    }

    private void parseKeyboardData(String callbackQuery, Message msg) {
        String[] dataParts = callbackQuery.split(" ");
        switch (dataParts[0]) {
            case TAG_CALLBACK_DATA -> {
                if (currentState.get(msg.getChatId()) == State.NewAdvertisement_AddCity) {
                    advertisement.get(msg.getChatId()).addTag(dataParts[1]);
                } else {
                    String currentChosenTags = chosenTags.get(msg.getChatId());
                    chosenTags.put(msg.getChatId(), currentChosenTags == null
                            ? dataParts[1] : String.format("%s %s", currentChosenTags, dataParts[1]));
                }

                deleteLastMessage(msg.getChatId());
                State nextState = State.nextState(currentState.get(msg.getChatId()));
                previousState.put(msg.getChatId(), currentState.get(msg.getChatId()));
                currentState.put(msg.getChatId(), nextState);
                getRegisteredCommand(nextState.getIdentifier()).processMessage(this, msg, null);
            }
            case TAGS_CALLBACK_DATA -> {
                List<List<InlineKeyboardButton>> buttons =
                        lastSentMessage.get(msg.getChatId()).getReplyMarkup().getKeyboard();
                InlineKeyboardButton changeTag = buttons.get(Integer.parseInt(dataParts[2]))
                        .get(Integer.parseInt(dataParts[3]));
                if (Objects.equals(dataParts[4], "0")) {
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
            case PHONE_CALLBACK_DATA -> {
                if (Objects.equals(dataParts[1], "yes")) {
                    currentState.put(msg.getChatId(), State.NewAdvertisement_AddPhone);
                    getRegisteredCommand(State.NewAdvertisement_AddPhone.getIdentifier())
                            .processMessage(this, msg, null);
                } else if (Objects.equals(dataParts[1], "no")) {
                    currentState.put(msg.getChatId(), State.NewAdvertisement_ConfirmPhone);
                    getRegisteredCommand(State.NewAdvertisement_ConfirmPhone.getIdentifier())
                            .processMessage(this, msg, null);
                }
            }
            case SOCIAL_CALLBACK_DATA -> {
                if (Objects.equals(dataParts[1], "yes")) {
                    currentState.put(msg.getChatId(), State.NewAdvertisement_AddSocial);
                    getRegisteredCommand(State.NewAdvertisement_AddSocial.getIdentifier())
                            .processMessage(this, msg, null);
                } else if (Objects.equals(dataParts[1], "no")) {
                    currentState.put(msg.getChatId(), State.NewAdvertisement_Confirm);
                    getRegisteredCommand(State.NewAdvertisement_Confirm.getIdentifier())
                            .processMessage(this, msg, null);
                }
            }
            case CONFIRM_AD_CALLBACK_DATA -> {
                if (Objects.equals(dataParts[1], "yes")) {
                    Advertisement ad = advertisement.get(msg.getChatId());
                    Message sentAd;
                    if (Objects.equals(dataParts[2], "0")) {
                        sentAd = newAdvertisementConfirm.sendPhotoMessage(this,
                                Long.parseLong(BaraholkaBotProperties.CHANNEL_CHAT_ID),
                                newAdvertisementConfirm.downloadPhoto(this, ad.getPhotos().get(0)),
                                ad.getAdvertisementText());
                    } else {
                        List<File> photoFiles = new ArrayList<>();
                        for (PhotoSize photoSize : ad.getPhotos()) {
                            photoFiles.add(Objects.requireNonNull(newAdvertisementConfirm.downloadPhoto(this,
                                    photoSize)));
                        }
                        sentAd = newAdvertisementConfirm.sendPhotoMediaGroup(this,
                                Long.parseLong(BaraholkaBotProperties.CHANNEL_CHAT_ID),
                                photoFiles,
                                ad.getAdvertisementText()).get(0);
                    }
                    if (sentAd != null) {
                        ad.setMessageId(Long.parseLong(String.valueOf(sentAd.getMessageId())))
                                .setCreationTime(System.currentTimeMillis())
                                .setNextUpdateTime(System.currentTimeMillis() +
                                        FIRST_REPEAT_NOTIFICATION_TIME_UNIT.toMillis(FIRST_REPEAT_NOTIFICATION_PERIOD))
                                .setUpdateAttempt(0);
                        sqlExecutor.insertNewAdvertisement(ad);
                        sendAnswer(msg.getChatId(), SUCCESS_TEXT, null);
                    } else {
                        sendAnswer(msg.getChatId(), UNSUCCESS_TEXT, null);
                        logger.error("Error while sending advertisement to channel.");
                    }
                } else if (Objects.equals(dataParts[1], "no")) {
                    sendAnswer(msg.getChatId(), ADVERTISEMENT_CANCELLED_TEXT, null);
                }
                getRegisteredCommand(State.MainMenu.getIdentifier())
                        .processMessage(this, msg, null);
            }
            case NOTIFICATION_CALLBACK_DATA -> {
                if (Objects.equals(dataParts[4], "0")) {
                    sqlExecutor.removeAdvertisement(Long.parseLong(dataParts[1]), Long.parseLong(dataParts[2]));
                    // TODO добавить удаление объявления из канала
                    sendAnswer(Long.parseLong(dataParts[1]), ADVERTISEMENT_SUCCESSFUL_DELETE, null);
                } else {
                    sqlExecutor.updateNextUpdateTime(Long.parseLong(dataParts[1]), Long.parseLong(dataParts[2]),
                            System.currentTimeMillis() + Long.parseLong(dataParts[3]));
                    sqlExecutor.updateAttemptNumber(Long.parseLong(dataParts[1]), Long.parseLong(dataParts[2]), 0);
                    sendAnswer(Long.parseLong(dataParts[1]), ADVERTISEMENT_SUCCESSFUL_UPDATE, null);
                }
                NotificationExecutor.deleteMessages(this, notificationMessages, Long.parseLong(dataParts[1]),
                        Long.parseLong(dataParts[2]));
            }
            case DELETE_AD -> {
//                sqlExecutor.deleteAd(Long.parseLong(dataParts[1]));
                EditMessageText editMessage = new EditMessageText();

                // TODO id message - int type?
                editMessage.setChatId(BaraholkaBotProperties.CHANNEL_CHAT_ID);
                editMessage.setMessageId(Integer.parseInt(dataParts[1]));
                System.out.println("Текст объявления: ");
//                editMessage.setText(editAdText(editMessage.getText()));
                editMessage.setText("TEST1");
                try {
                    execute(editMessage);
                } catch (TelegramApiException e){
                    logger.error(String.format("Cannot edit deleted message: %s", e.getMessage()));
                }
            }
            default -> logger.error(String.format("Unknown command in callback data: %s", callbackQuery));
        }
    }
    public String editAdText(String message) {
//        TODO Раскомментировать основной функционал, удалить заглушку
//        message = new StringBuffer(message).insert(0, "<b style=\"color:#ff0000\"> НЕ АКТУАЛЬНО </b> \n").toString();
        StringBuffer sb = new StringBuffer(message);
        sb = sb.insert(0, "НЕ АКТУАЛЬНО\n");
        message = sb.toString();
        return message;
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
