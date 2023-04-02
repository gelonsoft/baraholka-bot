package baraholkateam.bot;

import baraholkateam.command.HelpCommand;
import baraholkateam.command.MainMenuCommand;
import baraholkateam.command.NewAdvertisementCommand;
import baraholkateam.command.NonCommand;
import baraholkateam.command.SearchAdvertisements;
import baraholkateam.command.StartCommand;
import baraholkateam.database.SQLExecutor;
import baraholkateam.util.IState;
import baraholkateam.util.State;
import baraholkateam.util.Substate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import static baraholkateam.command.NonCommand.NEXT_BUTTON_TEXT;
import static baraholkateam.command.NonCommand.NOT_CHOSEN_TAG;
import static baraholkateam.command.NonCommand.TAGS_CALLBACK_DATA;
import static baraholkateam.command.NonCommand.TAG_CALLBACK_DATA;

@Component
public class BaraholkaBot extends TelegramLongPollingCommandBot {
    private final String botName;
    private final String botToken;
    private final NonCommand nonCommand;
    private final SQLExecutor sqlExecutor;
    private final Map<Long, IState> currentState = new ConcurrentHashMap<>();
    private final Map<Long, Message> lastSentMessage = new ConcurrentHashMap<>();
    private final Map<Long, String> chosenTags = new ConcurrentHashMap<>();
    private final Logger logger = LoggerFactory.getLogger(BaraholkaBot.class);
    public static final String CHOSEN_TAG = "✅ %s";
    public static final Integer SEARCH_ADVERTISEMENTS_LIMIT = 10;

    public BaraholkaBot(@Value("${bot.name}") String botName, @Value("${bot.token}") String botToken) {
        super();
        this.botName = botName;
        this.botToken = botToken;

        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            logger.error(String.format("Cannot find JDBC driver: %s", e.getMessage()));
            throw new RuntimeException("Failed to load JDBC driver", e);
        }
        sqlExecutor = new SQLExecutor();
        nonCommand = new NonCommand(sqlExecutor, chosenTags);

        register(new StartCommand(State.Start.getIdentifier(), State.Start.getDescription(), lastSentMessage));
        register(new HelpCommand(State.Help.getIdentifier(), State.Help.getDescription(), lastSentMessage,
                getRegisteredCommands()));
        register(new MainMenuCommand(State.MainMenu.getIdentifier(), State.MainMenu.getDescription(),
                lastSentMessage));
        register(new NewAdvertisementCommand(State.NewAdvertisement.getIdentifier(),
                State.NewAdvertisement.getDescription(), lastSentMessage));
        register(new SearchAdvertisements(State.SearchAdvertisements.getIdentifier(),
                State.SearchAdvertisements.getDescription(), lastSentMessage, chosenTags));
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
        //  Случай ввода команды по идентификаторуtext = "/menu"
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

        // Случай нажатия на кнопку с описанием команды
        State stateByDescription = State.findStateByDescription(msg.getText());
        if (stateByDescription != null) {
            getRegisteredCommand(stateByDescription.getIdentifier()).processMessage(this, msg, null);
            return;
        }

        // Случай нажатия на кнопку с продолжением во множественном выборе хэштегов
        if (Objects.equals(msg.getText(), NEXT_BUTTON_TEXT)) {
            addSearchTags(lastSentMessage.get(chatId));
            deleteLastMessage(msg.getChatId());
        }

        executeNonCommand(msg, chatId, currentState.get(chatId));
    }

    private void executeNonCommand(Message msg, Long chatId, IState currState) {
        List<NonCommand.AnswerPair> answers = nonCommand.nonCommandExecute(msg, chatId, currState);
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
            if (currState instanceof State) {
                getRegisteredCommand(currState.getIdentifier()).processMessage(this, msg, null);
            } else if (Substate.prevSubstate(currState) instanceof State) {
                getRegisteredCommand(Substate.prevSubstate(currState).getIdentifier()).processMessage(this, msg, null);
            } else {
                executeNonCommand(msg, chatId, Substate.prevSubstate(currState));
            }
            return;
        } else {
            currentState.put(chatId, Substate.nextSubstate(currState));
        }
        for (NonCommand.AnswerPair answer : answers) {
            if (!answer.getError()) { // ошибки в обработке сообщения пользователя нет, отправляем ответ и переходим на следующий шаг
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
                chosenTags.put(message.getChatId(),
                        String.format("%s %s", chosenTags.get(message.getChatId()),
                                tag.get(0).getText().split(" ")[1]));
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
                currentState.put(msg.getChatId(), Substate.nextSubstate(currentState.get(msg.getChatId())));
                executeNonCommand(msg, msg.getChatId(), currentState.get(msg.getChatId()));
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
