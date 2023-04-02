package baraholkateam.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Map;

abstract class Command extends BotCommand {
    private final Map<Long, Message> lastSentMessage;
    private final Logger logger = LoggerFactory.getLogger(Command.class);

    public Command(String commandIdentifier, String description, Map<Long, Message> lastSentMessage) {
        super(commandIdentifier, description);
        this.lastSentMessage = lastSentMessage;
    }

    void sendAnswer(AbsSender absSender, Long chatId, String commandName, String userName, String text,
                    InlineKeyboardMarkup inlineKeyboardMarkup) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        message.setParseMode(ParseMode.HTML);
        message.setText(text);
        if (inlineKeyboardMarkup != null) {
            message.setReplyMarkup(inlineKeyboardMarkup);
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
}
