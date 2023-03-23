package baraholkateam.bot;

import baraholkateam.command.HelpCommand;
import baraholkateam.command.MainMenuCommand;
import baraholkateam.command.NewAdvertisementCommand;
import baraholkateam.command.NonCommand;
import baraholkateam.command.StartCommand;
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
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class BaraholkaBot extends TelegramLongPollingCommandBot {
    private final String botName;
    private final String botToken;
    private final NonCommand nonCommand;
    private final Map<Long, IState> currentState = new ConcurrentHashMap<>();
    private final Logger logger = LoggerFactory.getLogger(BaraholkaBot.class);

    public BaraholkaBot(@Value("${bot.name}") String botName, @Value("${bot.token}") String botToken) {
        super();
        this.botName = botName;
        this.botToken = botToken;

        nonCommand = new NonCommand();

        register(new StartCommand(State.Start.getIdentifier(), State.Start.getDescription()));
        register(new HelpCommand(State.Help.getIdentifier(), State.Help.getDescription(), getRegisteredCommands()));
        register(new MainMenuCommand(State.MainMenu.getIdentifier(), State.MainMenu.getDescription()));
        register(new NewAdvertisementCommand(State.NewAdvertisement.getIdentifier(),
                State.NewAdvertisement.getDescription()));
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
        State currentState = State.findState(message.getText().replace("/", ""));
        if (currentState != null) {
            this.currentState.put(message.getChatId(), currentState);
        }
        return false;
    }

    @Override
    public void processNonCommandUpdate(Update update) {
        Message msg = update.getMessage();
        Long chatId = msg.getChatId();
        String messageText = msg.getText();

        IState currState = currentState.get(chatId);
        if (currState instanceof State) { // если у команды есть подкоманды, то переходим сразу в первую из них
            currState = Substate.nextSubstate(currState);
            currentState.put(chatId, currState);
        }
        NonCommand.AnswerPair answer = nonCommand.nonCommandExecute(messageText, chatId, currState);
        if (answer.getError()) { // ошибка в обработке сообщения пользователя, необходимо повторить данный шаг
            msg.setText(answer.getAnswer());
            getRegisteredCommand(currState.getIdentifier()).processMessage(this, msg, null);
        } else { // ошибки в обработке сообщения пользователя нет, отправляем ответ и переходим на следующий шаг
            setAnswer(chatId, getUserName(msg), answer.getAnswer());
            currentState.put(chatId, Substate.nextSubstate(currState));
        }
    }

    private String getUserName(Message msg) {
        User user = msg.getFrom();
        String userName = user.getUserName();
        return (userName != null) ? userName : String.format("%s %s", user.getLastName(), user.getFirstName());
    }

    private void setAnswer(Long chatId, String userName, String text) {
        SendMessage answer = new SendMessage();
        answer.setText(text);
        answer.setParseMode(ParseMode.HTML);
        answer.setChatId(chatId.toString());
        answer.disableWebPagePreview();

        try {
            execute(answer);
        } catch (TelegramApiException e) {
            logger.error(String.format("Cannot execute command of user %s: %s", userName, e.getMessage()));
        }
    }
}
