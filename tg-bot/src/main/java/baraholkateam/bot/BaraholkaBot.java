package baraholkateam.bot;

import baraholkateam.command.DeleteObyavleniye;
import baraholkateam.command.HelpCommand;
import baraholkateam.command.MainMenuCommand;
import baraholkateam.command.NewObyavleniyeAddObyavleniyeTypes;
import baraholkateam.command.NewObyavleniyeAddCategories;
import baraholkateam.command.NewObyavleniyeAddCity;
import baraholkateam.command.NewObyavleniyeAddContacts;
import baraholkateam.command.NewObyavleniyeAddDescription;
import baraholkateam.command.NewObyavleniyeAddPhone;
import baraholkateam.command.NewObyavleniyeAddPhotos;
import baraholkateam.command.NewObyavleniyeAddPrice;
import baraholkateam.command.NewObyavleniyeAddSocial;
import baraholkateam.command.NewObyavleniyeCommand;
import baraholkateam.command.NewObyavleniyeConfirm;
import baraholkateam.command.NewObyavleniyeConfirmPhone;
import baraholkateam.command.NewObyavleniyeConfirmPhoto;
import baraholkateam.command.NewObyavleniyeConfirmPrice;
import baraholkateam.command.NonCommand;
import baraholkateam.command.SearchObyavleniyes;
import baraholkateam.command.SearchObyavleniyesAddObyavleniyeTypes;
import baraholkateam.command.SearchObyavleniyesAddProductCategories;
import baraholkateam.command.SearchObyavleniyesShowFoundObyavleniyes;
import baraholkateam.command.StartCommand;
import baraholkateam.command.UserObyavleniyes;
import baraholkateam.notification.NotificationExecutor;
import baraholkateam.rest.model.CurrentObyavleniye;
import baraholkateam.rest.service.ActualObyavleniyeService;
import baraholkateam.rest.service.ChosenTagsService;
import baraholkateam.rest.service.CurrentObyavleniyeService;
import baraholkateam.rest.service.CurrentStateService;
import baraholkateam.rest.service.LastSentMessageService;
import baraholkateam.rest.service.PreviousStateService;
import baraholkateam.telegram_api_requests.TelegramAPIRequests;
import baraholkateam.util.Converter;
import baraholkateam.util.State;
import baraholkateam.util.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageCaption;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static baraholkateam.command.Command.OBYAVLENIYE_CANCELLED_TEXT;
import static baraholkateam.command.Command.OBYAVLENIYE_SUCCESSFUL_DELETE;
import static baraholkateam.command.Command.OBYAVLENIYE_SUCCESSFUL_UPDATE;
import static baraholkateam.command.Command.AD_SWEAR_WORD_DETECTED;
import static baraholkateam.command.Command.BACK_BUTTON;
import static baraholkateam.command.Command.CHOSEN_HASHTAGS;
import static baraholkateam.command.Command.CHOSEN_TAG;
import static baraholkateam.command.Command.CONFIRM_AD_CALLBACK_DATA;
import static baraholkateam.command.Command.DELETE_AD_CALLBACK_TEXT;
import static baraholkateam.command.Command.DELETE_CALLBACK_TEXT;
import static baraholkateam.command.Command.NEXT_BUTTON_TEXT;
import static baraholkateam.command.Command.NOTIFICATION_CALLBACK_DATA;
import static baraholkateam.command.Command.NOT_CHOSEN_TAG;
import static baraholkateam.command.Command.NO_MORE_PHOTOS_ADD;
import static baraholkateam.command.Command.PHONE_CALLBACK_DATA;
import static baraholkateam.command.Command.PHOTOS_DELETE;
import static baraholkateam.command.Command.SOCIALS_DELETE;
import static baraholkateam.command.Command.SOCIAL_CALLBACK_DATA;
import static baraholkateam.command.Command.SUCCESS_DELETE_AD_TEXT;
import static baraholkateam.command.Command.SUCCESS_TEXT;
import static baraholkateam.command.Command.SWEAR_WORD_DETECTOR;
import static baraholkateam.command.Command.TAGS_CALLBACK_DATA;
import static baraholkateam.command.Command.TAG_CALLBACK_DATA;
import static baraholkateam.command.Command.UNSUCCESS_DELETE_AD_TEXT;
import static baraholkateam.command.Command.UNSUCCESS_TEXT;
import static baraholkateam.command.DeleteObyavleniye.DELETE_AD;
import static baraholkateam.command.DeleteObyavleniye.NOT_ACTUAL_TEXT;
import static baraholkateam.command.NewObyavleniyeConfirmPhone.DELETE_ALL_SOCIALS;
import static baraholkateam.command.NewObyavleniyeConfirmPhoto.DELETE_ALL_PHOTOS;
import static baraholkateam.notification.NotificationExecutor.FIRST_REPEAT_NOTIFICATION_PERIOD;
import static baraholkateam.notification.NotificationExecutor.FIRST_REPEAT_NOTIFICATION_TIME_UNIT;

@Component("BaraholkaBot")
public class BaraholkaBot extends TelegramLongPollingCommandBot implements TgFileLoader {
    /**
     * Лимит выдачи объявлений в функции поиска объявлений по тегам.
     */
    public static final Integer SEARCH_OBYAVLENIYES_LIMIT = 10;
    private final String botName;
    private final String botToken;
    private static final Logger LOGGER = LoggerFactory.getLogger(BaraholkaBot.class);

    @Autowired
    private ActualObyavleniyeService actualObyavleniyeService;

    @Autowired
    private CurrentObyavleniyeService currentObyavleniyeService;

    @Autowired
    private CurrentStateService currentStateService;

    @Autowired
    private LastSentMessageService lastSentMessageService;

    @Autowired
    private ChosenTagsService chosenTagsService;

    @Autowired
    private PreviousStateService previousStateService;

    @Autowired
    private NonCommand nonCommand;

    @Autowired
    private NotificationExecutor notificationExecutor;

    @Autowired
    private TelegramAPIRequests telegramAPIRequests;

    @Autowired
    private StartCommand startCommand;

    @Autowired
    private HelpCommand helpCommand;

    @Autowired
    private MainMenuCommand mainMenuCommand;

    @Autowired
    private UserObyavleniyes userObyavleniyes;

    @Autowired
    private DeleteObyavleniye deleteObyavleniye;

    @Autowired
    private NewObyavleniyeAddPhotos newObyavleniyeAddPhotos;

    @Autowired
    private NewObyavleniyeConfirmPhoto newObyavleniyeConfirmPhoto;

    @Autowired
    private NewObyavleniyeAddDescription newObyavleniyeAddDescription;

    @Autowired
    private NewObyavleniyeAddCity newObyavleniyeAddCity;

    @Autowired
    private NewObyavleniyeAddObyavleniyeTypes newObyavleniyeAddObyavleniyeTypes;

    @Autowired
    private NewObyavleniyeAddCategories newObyavleniyeAddCategories;

    @Autowired
    private NewObyavleniyeAddPrice newObyavleniyeAddPrice;

    @Autowired
    private NewObyavleniyeConfirmPrice newObyavleniyeConfirmPrice;

    @Autowired
    private NewObyavleniyeCommand newObyavleniyeCommand;

    @Autowired
    private NewObyavleniyeAddContacts newObyavleniyeAddContacts;

    @Autowired
    private NewObyavleniyeAddPhone newObyavleniyeAddPhone;

    @Autowired
    private NewObyavleniyeConfirmPhone newObyavleniyeConfirmPhone;

    @Autowired
    private NewObyavleniyeAddSocial newObyavleniyeAddSocial;

    @Autowired
    private NewObyavleniyeConfirm newObyavleniyeConfirm;

    @Autowired
    private SearchObyavleniyes searchObyavleniyes;

    @Autowired
    private SearchObyavleniyesAddObyavleniyeTypes searchObyavleniyesAddObyavleniyeTypes;

    @Autowired
    private SearchObyavleniyesAddProductCategories searchObyavleniyesAddProductCategories;

    @Autowired
    private SearchObyavleniyesShowFoundObyavleniyes searchObyavleniyesShowFoundObyavleniyes;

    @Value("${channel.chat_id}")
    private String channelChatId;

    @Value("${channel.username}")
    private String channelUsername;

    public BaraholkaBot(
            @Value("${bot.name}") String botName,
            @Value("${bot.token}") String botToken
    ) {
        super();
        this.botName = botName;
        this.botToken = botToken;
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
            currentStateService.put(message.getChatId(), currentStateByIdentifier);
            return false;
        }

        // Случай нажатия на кнопку с описанием команды
        State currentStateByDescription = State.findStateByDescription(message.getText());
        if (currentStateByDescription != null) {
            currentStateService.put(message.getChatId(), currentStateByDescription);
            return false;
        }

        // Случай пропуска этапа добавления цены, если не были добавлены соответствующие теги
        if (currentStateService.get(message.getChatId()) == State.NewObyavleniye_AddPrice) {
            List<String> tags = currentObyavleniyeService.getTags(message.getChatId());
            if (!tags.contains(Tag.Sale.getName()) && !tags.contains(Tag.Bargaining.getName())) {
                currentStateService.put(message.getChatId(), State.NewObyavleniye_AddContacts);
            }
        }
        return false;
    }

    @Override
    public void onRegister() {
        super.onRegister();

        register(startCommand);
        register(helpCommand);
        register(mainMenuCommand);
        register(userObyavleniyes);
        register(deleteObyavleniye);
        register(newObyavleniyeCommand);
        register(newObyavleniyeAddPhotos);
        register(newObyavleniyeConfirmPhoto);
        register(newObyavleniyeAddDescription);
        register(newObyavleniyeAddCity);
        register(newObyavleniyeAddObyavleniyeTypes);
        register(newObyavleniyeAddCategories);
        register(newObyavleniyeAddPrice);
        register(newObyavleniyeConfirmPrice);
        register(newObyavleniyeAddContacts);
        register(newObyavleniyeAddPhone);
        register(newObyavleniyeConfirmPhone);
        register(newObyavleniyeAddSocial);
        register(newObyavleniyeConfirm);
        register(searchObyavleniyes);
        register(searchObyavleniyesAddObyavleniyeTypes);
        register(searchObyavleniyesAddProductCategories);
        register(searchObyavleniyesShowFoundObyavleniyes);
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

        if (msg == null) {
            return;
        }

        Long chatId = msg.getChatId();

        // Случай нажатия на кнопку "Назад"
        if (msg.hasText() && Objects.equals(msg.getText(), BACK_BUTTON)) {
            State currentState = currentStateService.get(chatId);
            State backState = State.previousState(currentState);
            if (backState != null) {
                if (lastSentMessageService.get(chatId).hasReplyMarkup()) {
                    deleteLastMessage(chatId);
                }
                if (currentState == State.NewObyavleniye_AddSocial) {
                    currentObyavleniyeService.setSocials(chatId, new ArrayList<>());
                }
                currentStateService.put(chatId, backState);
                if (backState == State.NewObyavleniye_AddContacts) {
                    currentObyavleniyeService.setSocials(chatId, new ArrayList<>());
                }
                getRegisteredCommand(backState.getIdentifier()).processMessage(this, msg, null);
                return;
            }
        }

        // Случай обновления данных во время создания объявления
        if (msg.hasText() && newObyavleniyeUpdateData(msg)) {
            return;
        }

        // Случай добавления новых фотографий во время создания объявления
        if (msg.hasPhoto() && addObyavleniyePhotos(msg)) {
            return;
        }

        // Случай нажатия на кнопку с описанием команды
        State stateByDescription = State.findStateByDescription(msg.getText());
        if (stateByDescription != null) {
            currentStateService.put(chatId, stateByDescription);
            getRegisteredCommand(stateByDescription.getIdentifier()).processMessage(this, msg, null);
            return;
        }

        // Случай удаления всех фотографий по кнопке
        if (msg.hasText() && Objects.equals(msg.getText(), DELETE_ALL_PHOTOS)
                && currentStateService.get(chatId) == State.NewObyavleniye_ConfirmPhoto) {
            currentObyavleniyeService.setPhotos(msg.getChatId(), new ArrayList<>());
            sendAnswer(chatId, PHOTOS_DELETE, null);
            currentStateService.put(chatId, State.NewObyavleniye_AddPhotos);
            getRegisteredCommand(State.NewObyavleniye_AddPhotos.getIdentifier()).processMessage(this, msg, null);
            return;
        }

        // Случай удаления всех социальных сетей по кнопке
        if (msg.hasText() && Objects.equals(msg.getText(), DELETE_ALL_SOCIALS)
                && currentStateService.get(chatId) == State.NewObyavleniye_ConfirmPhone) {
            currentObyavleniyeService.setSocials(msg.getChatId(), new ArrayList<>());
            sendAnswer(chatId, SOCIALS_DELETE, null);
            currentStateService.put(chatId, State.NewObyavleniye_ConfirmPhone);
            getRegisteredCommand(State.NewObyavleniye_ConfirmPhone.getIdentifier()).processMessage(this, msg, null);
            return;
        }

        // Случай нажатия на кнопку "Продолжить" во множественном выборе хэштегов
        if (msg.hasText() && Objects.equals(msg.getText(), NEXT_BUTTON_TEXT)) {
            addChosenTags(lastSentMessageService.get(chatId));

            if (chosenTagsService.get(chatId) != null
                    && State.nextState(currentStateService.get(msg.getChatId())) == State.NewObyavleniye_AddPrice) {
                List<String> addedTags = currentObyavleniyeService.getTags(chatId);
                // Если нужно пропустить добавление цены товара
                if (!addedTags.contains(Tag.Sale.getName()) && !addedTags.contains(Tag.Bargaining.getName())) {
                    List<String> tags = chosenTagsService.get(chatId).stream()
                            .map(Tag::getName)
                            .toList();
                    previousStateService.put(chatId, currentStateService.get(msg.getChatId()));
                    currentObyavleniyeService.addTags(chatId, tags);
                    currentStateService.put(msg.getChatId(), State.NewObyavleniye_AddContacts);
                    deleteLastMessage(msg.getChatId());
                    sendAnswer(
                            chatId,
                            String.format(
                                    CHOSEN_HASHTAGS,
                                    String.join(" ", currentObyavleniyeService.getTags(chatId))
                            ),
                            null);
                    getRegisteredCommand(State.NewObyavleniye_AddContacts.getIdentifier())
                            .processMessage(this, msg, null);
                    return;
                }
            }

            if (chosenTagsService.get(chatId) != null
                    && (currentStateService.get(msg.getChatId()) == State.NewObyavleniye_AddObyavleniyeTypes
                    || currentStateService.get(msg.getChatId()) == State.NewObyavleniye_AddCategories)) {
                List<String> tags = chosenTagsService.get(chatId).stream()
                        .map(Tag::getName)
                        .toList();
                currentObyavleniyeService.addTags(chatId, tags);
                chosenTagsService.delete(chatId);
            }

            deleteLastMessage(msg.getChatId());
            State nextState = State.nextState(currentStateService.get(msg.getChatId()));
            previousStateService.put(chatId, currentStateService.get(msg.getChatId()));
            currentStateService.put(chatId, nextState);
            getRegisteredCommand(nextState.getIdentifier()).processMessage(this, msg, null);
            return;
        }

        // Случай обработки текстовой информации от пользователя
        State currentState = currentStateService.get(chatId);
        if (currentState != null) {
            executeNonCommand(msg, chatId, currentState);
        }
    }

    private boolean newObyavleniyeUpdateData(Message msg) {
        State state = currentStateService.get(msg.getChatId());
        String text = msg.getText();
        if (state == State.NewObyavleniye_AddDescription) {
            if (text == null || text.length() > 800) {
                return false;
            }
            Pattern filter = Pattern.compile(SWEAR_WORD_DETECTOR, Pattern.CASE_INSENSITIVE);
            Matcher matcher = filter.matcher(text);
            if (matcher.find()) {
                sendAnswer(msg.getChatId(), AD_SWEAR_WORD_DETECTED, null);
                return true;
            }
            currentObyavleniyeService.setDescription(msg.getChatId(), text);
            updateStateOnTextData(msg);
            return true;
        }

        if (state == State.NewObyavleniye_AddPrice) {
            if (!text.matches("\\d{1,18}")) {
                return false;
            }
            try {
                currentObyavleniyeService.setPrice(msg.getChatId(), Long.parseLong(text));
            } catch (Exception e) {
                LOGGER.error("Invalid input from user");
            }
            updateStateOnTextData(msg);
            return true;
        }

        if (state == State.NewObyavleniye_AddPhone) {
            if (!text.matches("\\+7-\\d{3}-\\d{3}-\\d{2}-\\d{2}")) {
                return false;
            }
            currentObyavleniyeService.setPhone(msg.getChatId(), text);
            updateStateOnTextData(msg);
            return true;
        }

        if (state == State.NewObyavleniye_AddSocial) {
            if (!text.matches("https://.+/.+")) {
                return false;
            }
            currentObyavleniyeService.addSocial(msg.getChatId(), text);
            previousStateService.put(msg.getChatId(), State.NewObyavleniye_AddSocial);
            currentStateService.put(msg.getChatId(), State.NewObyavleniye_ConfirmPhone);
            getRegisteredCommand(State.NewObyavleniye_ConfirmPhone.getIdentifier())
                    .processMessage(this, msg, null);
            return true;
        }
        return false;
    }

    private boolean addObyavleniyePhotos(Message msg) {
        State currentState = currentStateService.get(msg.getChatId());
        if (currentState == State.NewObyavleniye_AddPhotos || currentState == State.NewObyavleniye_ConfirmPhoto) {

            AtomicInteger canAddPhotosCount =
                    new AtomicInteger(10 - currentObyavleniyeService.getPhotos(msg.getChatId()).size());
            AtomicBoolean isCanAdd = new AtomicBoolean(true);

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

            // добавляем фотографии самого высокого разрешения из числа одинаковых фотографий разного разрешения
            photos.forEach((make, photo) -> {
                if (canAddPhotosCount.getAndDecrement() <= 0) {
                    deleteLastMessage(msg.getChatId());
                    isCanAdd.set(false);
                    if (lastSentMessageService.get(msg.getChatId()).getText().substring(0, 20)
                            .equals(NO_MORE_PHOTOS_ADD.substring(0, 20))) {
                        deleteLastMessage(msg.getChatId());
                    }
                    sendAnswer(msg.getChatId(), NO_MORE_PHOTOS_ADD, null);
                    return;
                }
                currentObyavleniyeService.addPhoto(
                        msg.getChatId(),
                        Converter.convertPhotoToBase64String(
                                downloadFileByFilePath(telegramAPIRequests.getFilePath(photo.last().getFileId()))
                        )
                );
            });

            if (isCanAdd.get()) {
                getRegisteredCommand(State.NewObyavleniye_ConfirmPhoto.getIdentifier())
                        .processMessage(this, msg, null);
                currentStateService.put(msg.getChatId(), State.NewObyavleniye_ConfirmPhoto);
            }
            return true;
        }
        return false;
    }

    private void updateStateOnTextData(Message msg) {
        State nextState = State.nextState(currentStateService.get(msg.getChatId()));
        previousStateService.put(msg.getChatId(), currentStateService.get(msg.getChatId()));
        currentStateService.put(msg.getChatId(), nextState);
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
                    LOGGER.error(String.format("Cannot send message: %s", e.getMessage()));
                }
            }
            // ошибка в обработке сообщения пользователя, необходимо повторить данный шаг
            if (currState != null) {
                getRegisteredCommand(currState.getIdentifier()).processMessage(this, msg, null);
            }
            return;
        } else {
            State nextState = State.nextState(currentStateService.get(msg.getChatId()));
            previousStateService.put(chatId, currentStateService.get(msg.getChatId()));
            currentStateService.put(chatId, nextState);
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
            lastSentMessageService.put(chatId, sentMessage);
        } catch (TelegramApiException e) {
            LOGGER.error(String.format("Cannot execute command: %s", e.getMessage()));
        }
    }

    private void addChosenTags(Message message) {
        List<List<InlineKeyboardButton>> buttons = message.getReplyMarkup().getKeyboard();
        for (List<InlineKeyboardButton> tag : buttons) {
            for (InlineKeyboardButton inlineKeyboardButton : tag) {
                String[] dataCallbackParts = inlineKeyboardButton.getCallbackData().split(" ");
                if (Objects.equals(dataCallbackParts[dataCallbackParts.length - 1], "1")) {
                    String newTags = inlineKeyboardButton.getText().split(" ")[1];
                    List<Tag> tags = chosenTagsService.get(message.getChatId());
                    if (tags == null || tags.isEmpty()) {
                        chosenTagsService.put(message.getChatId(),
                                Arrays.stream(newTags.split(" "))
                                        .map(Tag::getTagByName)
                                        .toList());
                    } else {
                        String chosenTagsString = tags.stream()
                                .map(Tag::getName)
                                .collect(Collectors.joining(" "));
                        chosenTagsService.put(message.getChatId(),
                                Arrays.stream(String.format("%s %s", chosenTagsString, newTags)
                                        .split(" "))
                                        .map(Tag::getTagByName)
                                        .toList());
                    }
                }
            }
        }
    }

    private void parseKeyboardData(String callbackQuery, Message msg) {
        String[] dataParts = callbackQuery.split(" ");
        switch (dataParts[0]) {
            case TAG_CALLBACK_DATA -> {
                if (currentStateService.get(msg.getChatId()) == State.NewObyavleniye_AddCity) {
                    currentObyavleniyeService.addTag(msg.getChatId(), dataParts[1]);
                } else {
                    List<Tag> tags = chosenTagsService.get(msg.getChatId());
                    if (tags == null || tags.isEmpty()) {
                        chosenTagsService.put(msg.getChatId(),
                                Arrays.stream(dataParts[1].split(" "))
                                        .map(Tag::getTagByName)
                                        .toList());
                    } else {
                        String chosenTagsString = tags.stream()
                                .map(Tag::getName)
                                .collect(Collectors.joining(" "));
                        chosenTagsService.put(msg.getChatId(),
                                Arrays.stream(String.format("%s %s", chosenTagsString, dataParts[1]).split(" "))
                                        .map(Tag::getTagByName)
                                        .toList());
                    }
                }

                deleteLastMessage(msg.getChatId());
                State nextState = State.nextState(currentStateService.get(msg.getChatId()));
                previousStateService.put(msg.getChatId(), currentStateService.get(msg.getChatId()));
                currentStateService.put(msg.getChatId(), nextState);
                getRegisteredCommand(nextState.getIdentifier()).processMessage(this, msg, null);
            }
            case TAGS_CALLBACK_DATA -> {
                Message lastSentMessage = lastSentMessageService.get(msg.getChatId());
                List<List<InlineKeyboardButton>> buttons = lastSentMessage.getReplyMarkup().getKeyboard();
                InlineKeyboardButton changeTag = buttons.get(Integer.parseInt(dataParts[2]))
                        .get(Integer.parseInt(dataParts[3]));
                if (Objects.equals(dataParts[4], "0")) {
                    changeTag.setText(String.format(CHOSEN_TAG, changeTag.getText().split(" ")[1]));
                    changeTag.setCallbackData(changeTag
                            .getCallbackData()
                            .substring(0, changeTag.getCallbackData().length() - 2)
                            .concat(" 1"));
                } else {
                    changeTag.setText(String.format(NOT_CHOSEN_TAG, changeTag.getText().split(" ")[1]));
                    changeTag.setCallbackData(changeTag
                            .getCallbackData()
                            .substring(0, changeTag.getCallbackData().length() - 2)
                            .concat(" 0"));
                }
                buttons.get(Integer.parseInt(dataParts[2])).remove(Integer.parseInt(dataParts[3]));
                buttons.get(Integer.parseInt(dataParts[2])).add(Integer.parseInt(dataParts[3]), changeTag);
                InlineKeyboardMarkup ikm = new InlineKeyboardMarkup();
                ikm.setKeyboard(buttons);
                lastSentMessage.setReplyMarkup(ikm);
                lastSentMessageService.put(msg.getChatId(), lastSentMessage);
                editMessageReplyMarkup(msg.getChatId(), buttons);
            }
            case PHONE_CALLBACK_DATA -> {
                deleteLastMessage(msg.getChatId());
                if (Objects.equals(dataParts[1], "yes")) {
                    currentStateService.put(msg.getChatId(), State.NewObyavleniye_AddPhone);
                    getRegisteredCommand(State.NewObyavleniye_AddPhone.getIdentifier())
                            .processMessage(this, msg, null);
                } else if (Objects.equals(dataParts[1], "no")) {
                    currentStateService.put(msg.getChatId(), State.NewObyavleniye_ConfirmPhone);
                    getRegisteredCommand(State.NewObyavleniye_ConfirmPhone.getIdentifier())
                            .processMessage(this, msg, null);
                }
            }
            case SOCIAL_CALLBACK_DATA -> {
                deleteLastMessage(msg.getChatId());
                if (Objects.equals(dataParts[1], "yes")) {
                    currentStateService.put(msg.getChatId(), State.NewObyavleniye_AddSocial);
                    getRegisteredCommand(State.NewObyavleniye_AddSocial.getIdentifier())
                            .processMessage(this, msg, null);
                } else if (Objects.equals(dataParts[1], "no")) {
                    currentStateService.put(msg.getChatId(), State.NewObyavleniye_Confirm);
                    getRegisteredCommand(State.NewObyavleniye_Confirm.getIdentifier())
                            .processMessage(this, msg, null);
                }
            }
            case CONFIRM_AD_CALLBACK_DATA -> {
                deleteLastMessage(msg.getChatId());
                if (Objects.equals(dataParts[1], "yes")) {
                    if (currentObyavleniyeService.getContacts(msg.getChatId()).size() == 0
                            && currentObyavleniyeService.getPhone(msg.getChatId()) == null) {
                        currentObyavleniyeService.setSocials(msg.getChatId(),
                                List.of("@" + telegramAPIRequests.getUser(msg.getChatId()).username()));
                    }

                    Message sentAd;
                    if (Objects.equals(dataParts[2], "0")) {
                        sentAd = newObyavleniyeConfirm.sendPhotoMessage(
                                this,
                                Long.parseLong(channelChatId),
                                Converter.convertBase64StringToPhoto(
                                        currentObyavleniyeService.getPhotos(msg.getChatId()).get(0)
                                ),
                                currentObyavleniyeService.getObyavleniyeText(msg.getChatId())
                        );
                    } else {
                        List<File> photoFiles = new ArrayList<>();
                        for (String photo : currentObyavleniyeService.getPhotos(msg.getChatId())) {
                            photoFiles.add(Objects.requireNonNull(Converter.convertBase64StringToPhoto(photo)));
                        }
                        sentAd = newObyavleniyeConfirm.sendPhotoMediaGroup(this,
                                Long.parseLong(channelChatId),
                                photoFiles,
                                currentObyavleniyeService.getObyavleniyeText(msg.getChatId())).get(0);
                    }
                    if (sentAd != null) {
                        CurrentObyavleniye currentObyavleniye = currentObyavleniyeService.get(msg.getChatId());
                        currentObyavleniye
                                .setMessageId(Long.parseLong(String.valueOf(sentAd.getMessageId())))
                                .setCreationTime(System.currentTimeMillis())
                                .setNextUpdateTime(
                                        System.currentTimeMillis()
                                                + FIRST_REPEAT_NOTIFICATION_TIME_UNIT
                                                .toMillis(FIRST_REPEAT_NOTIFICATION_PERIOD)
                                )
                                .setUpdateAttempt(0);
                        currentObyavleniyeService.put(currentObyavleniye);
                        actualObyavleniyeService.insertNewObyavleniye(currentObyavleniye);
                        sendAnswer(msg.getChatId(), SUCCESS_TEXT, null);
                    } else {
                        sendAnswer(msg.getChatId(), UNSUCCESS_TEXT, null);
                        LOGGER.error("Error while sending obyavleniye to channel.");
                    }
                } else if (Objects.equals(dataParts[1], "no")) {
                    sendAnswer(msg.getChatId(), OBYAVLENIYE_CANCELLED_TEXT, null);
                }
                currentStateService.put(msg.getChatId(), State.MainMenu);
                getRegisteredCommand(State.MainMenu.getIdentifier())
                        .processMessage(this, msg, null);
            }
            case NOTIFICATION_CALLBACK_DATA -> {
                long chatId = Long.parseLong(dataParts[1]);
                long messageId = Long.parseLong(dataParts[2]);

                if (Objects.equals(dataParts[4], "0")) {
                    editAdText(dataParts[2]);
                    actualObyavleniyeService.removeObyavleniye(messageId);
                    sendAnswer(chatId, OBYAVLENIYE_SUCCESSFUL_DELETE, null);
                } else {
                    actualObyavleniyeService.setNextUpdateTime(messageId,
                            System.currentTimeMillis() + Long.parseLong(dataParts[3]));
                    actualObyavleniyeService.setUpdateAttempt(messageId, 0);
                    sendAnswer(chatId, OBYAVLENIYE_SUCCESSFUL_UPDATE, null);
                }
                notificationExecutor.deleteMessages(this, chatId, messageId);
            }
            case DELETE_CALLBACK_TEXT -> {
                long messageId = Long.parseLong(dataParts[1]);

                deleteLastMessage(msg.getChatId());
                telegramAPIRequests.forwardMessage(channelUsername, String.valueOf(msg.getChatId()), messageId);
                sendAnswer(msg.getChatId(), DELETE_AD, getDeleteAd(messageId));
            }
            case DELETE_AD_CALLBACK_TEXT -> {
                deleteLastMessage(msg.getChatId());
                if (Objects.equals(dataParts[1], "1")) {
                    editAdText(dataParts[2]);
                    actualObyavleniyeService.removeObyavleniye(Long.parseLong(dataParts[2]));
                    sendAnswer(msg.getChatId(), SUCCESS_DELETE_AD_TEXT, null);
                } else {
                    sendAnswer(msg.getChatId(), UNSUCCESS_DELETE_AD_TEXT, null);
                    currentStateService.put(msg.getChatId(), State.DeleteObyavleniye);
                    getRegisteredCommand(State.DeleteObyavleniye.getIdentifier())
                            .processMessage(this, msg, null);
                }
            }
            default -> LOGGER.error(String.format("Unknown command in callback data: %s", callbackQuery));
        }
    }

    public void editAdText(String messageId) {
        EditMessageCaption editMessage = new EditMessageCaption();
        String adText = actualObyavleniyeService.adText(Long.parseLong(messageId))
                .substring(Tag.Actual.getName().length() + 1);
        String editedText = String.format("%s\n\n%s", NOT_ACTUAL_TEXT, adText);
        editMessage.setChatId(channelChatId);
        editMessage.setMessageId(Integer.parseInt(messageId));
        editMessage.setParseMode(ParseMode.HTML);
        editMessage.setCaption(editedText);

        try {
            execute(editMessage);
        } catch (TelegramApiException e) {
            LOGGER.error(String.format("Cannot edit deleted message: %s", e.getMessage()));
        }
    }

    private void deleteLastMessage(Long chatId) {
        DeleteMessage deleteLastMessage = new DeleteMessage();
        deleteLastMessage.setMessageId(lastSentMessageService.get(chatId).getMessageId());
        deleteLastMessage.setChatId(chatId);
        try {
            execute(deleteLastMessage);
        } catch (TelegramApiException e) {
            LOGGER.error(String.format("Cannot delete last message due to: %s", e.getMessage()));
        }
    }

    private void editMessageReplyMarkup(Long chatId, List<List<InlineKeyboardButton>> buttons) {
        InlineKeyboardMarkup ikm = new InlineKeyboardMarkup();
        ikm.setKeyboard(buttons);
        EditMessageReplyMarkup editMessageReplyMarkup = new EditMessageReplyMarkup();
        editMessageReplyMarkup.setChatId(chatId);
        editMessageReplyMarkup.setMessageId(lastSentMessageService.get(chatId).getMessageId());
        editMessageReplyMarkup.setReplyMarkup(ikm);
        try {
            execute(editMessageReplyMarkup);
        } catch (TelegramApiException e) {
            LOGGER.error(String.format("Cannot edit message reply markup due to: %s", e.getMessage()));
        }
    }

    private InlineKeyboardMarkup getDeleteAd(Long messageId) {
        List<InlineKeyboardButton> buttons = new ArrayList<>(2);

        InlineKeyboardButton yes = new InlineKeyboardButton();
        yes.setText("Да");
        yes.setCallbackData(String.format("%s 1 %d", DELETE_AD_CALLBACK_TEXT, messageId));
        buttons.add(yes);

        InlineKeyboardButton no = new InlineKeyboardButton();
        no.setText("Нет");
        no.setCallbackData(String.format("%s 0", DELETE_AD_CALLBACK_TEXT));
        buttons.add(no);

        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(buttons);

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(rowList);

        return inlineKeyboardMarkup;
    }

    @Override
    public File downloadFileByFilePath(String filePath) {
        try {
            return downloadFile(filePath);
        } catch (TelegramApiException e) {
            LOGGER.error(String.format("Cannot download file %s", filePath), e);
            return null;
        }
    }
}
