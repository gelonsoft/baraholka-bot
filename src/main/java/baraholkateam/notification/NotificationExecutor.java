package baraholkateam.notification;

import baraholkateam.bot.BaraholkaBotProperties;
import baraholkateam.database.SQLExecutor;
import baraholkateam.telegram_api_requests.TelegramAPIRequests;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static baraholkateam.command.Command.ADVERTISEMENT_DELETE;
import static baraholkateam.command.Command.NOTIFICATION_CALLBACK_DATA;

public class NotificationExecutor {
    private static final String DELETE_IF_NOT_UPDATE = """
            Вы пропустили 2 уведомления с вопросом об актуальности объявления.
            Через %s часов объявление будет автоматически удалено, если не подтвердить его актуальность.""";
    private static final String ASK_NEXT_UPDATE = """
            Является ли данное объявление актуальным?""";
    private static final int MAXIMUM_POOL_SIZE = Runtime.getRuntime().availableProcessors();
    private static final int CORE_POOL_SIZE = MAXIMUM_POOL_SIZE > 2 ? MAXIMUM_POOL_SIZE - 2 : MAXIMUM_POOL_SIZE;
    public static final Long FIRST_REPEAT_NOTIFICATION_PERIOD = 14L;
    public static final TimeUnit FIRST_REPEAT_NOTIFICATION_TIME_UNIT = TimeUnit.DAYS;
    private static final Long REPEAT_NOTIFICATION_PERIOD = 24L;
    private static final TimeUnit REPEAT_NOTIFICATION_TIME_UNIT = TimeUnit.HOURS;
    private static final Long SCHEDULE_NOTIFICATION_EXECUTOR_PERIOD = 1L;
    private static final TimeUnit SCHEDULE_NOTIFICATION_EXECUTOR_TIME_UNIT = TimeUnit.HOURS;
    private static final ScheduledExecutorService notificationExecutor =
            Executors.newScheduledThreadPool(CORE_POOL_SIZE,
                    new ThreadFactoryBuilder().setNameFormat("Notification-Executor-%d").build());
    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationExecutor.class);

    public static void startNotificationExecutor(SQLExecutor sqlExecutor, AbsSender sender,
                                                 TelegramAPIRequests telegramAPIRequests,
                                                 Map<Long, Map<Long, List<Message>>> notificationMessages) {
        notificationExecutor.scheduleAtFixedRate(() -> {
            try (ResultSet askActualAdvertisements = sqlExecutor.askActualAdvertisements(System.currentTimeMillis())) {
                while (askActualAdvertisements.next()) {
                    int attemptNum = askActualAdvertisements.getInt("update_attempt");
                    long chatId = askActualAdvertisements.getLong("chat_id");
                    long messageId = askActualAdvertisements.getLong("message_id");
                    if (attemptNum == 3) {
                        sqlExecutor.removeAdvertisement(chatId, messageId);
                        // TODO добавить удаление объявления из канала
                        deleteMessages(sender, notificationMessages, chatId, messageId);
                        sendMessageWithoutDelete(sender, chatId, ADVERTISEMENT_DELETE, null);
                    } else if (attemptNum <= 2) {
                        sqlExecutor.updateAttemptNumber(chatId, messageId, attemptNum + 1);
                        Long forwardedMessageId =
                                telegramAPIRequests.forwardMessage(BaraholkaBotProperties.CHANNEL_USERNAME,
                                        String.valueOf(chatId), messageId);

                        if (attemptNum == 2) {
                            deleteMessages(sender, notificationMessages, chatId, messageId);
                            sendMessage(sender, chatId, messageId,
                                    String.format("%s\n%s", String.format(DELETE_IF_NOT_UPDATE,
                                            REPEAT_NOTIFICATION_PERIOD), ASK_NEXT_UPDATE), ifNextUpdate(chatId,
                                            messageId,
                                            REPEAT_NOTIFICATION_TIME_UNIT.toMillis(REPEAT_NOTIFICATION_PERIOD)),
                                    notificationMessages);
                        } else if (attemptNum == 1) {
                            deleteMessages(sender, notificationMessages, chatId, messageId);
                            sendMessage(sender, chatId, messageId, ASK_NEXT_UPDATE, ifNextUpdate(chatId, messageId,
                                            REPEAT_NOTIFICATION_TIME_UNIT.toMillis(REPEAT_NOTIFICATION_PERIOD)),
                                    notificationMessages);
                        } else {
                            sendMessage(sender, chatId, messageId, ASK_NEXT_UPDATE, ifNextUpdate(chatId, messageId,
                                            FIRST_REPEAT_NOTIFICATION_TIME_UNIT
                                                    .toMillis(FIRST_REPEAT_NOTIFICATION_PERIOD)),
                                    notificationMessages);
                        }

                        Message forwardedMessage = new Message();
                        forwardedMessage.setMessageId(Math.toIntExact(forwardedMessageId));
                        Chat chat = new Chat();
                        chat.setId(chatId);
                        forwardedMessage.setChat(chat);

                        addNotificationMessage(notificationMessages, forwardedMessage, chatId, messageId);
                    } else {
                        LOGGER.error(
                                String.format("Incorrect number of update attempt: %d. Chat id: %d, message id: %d",
                                        attemptNum, chatId, messageId)
                        );
                    }
                }
            } catch (SQLException e) {
                LOGGER.error(String.format("Cannot execute SQL: %s", e.getMessage()));
            }
        }, SCHEDULE_NOTIFICATION_EXECUTOR_PERIOD, SCHEDULE_NOTIFICATION_EXECUTOR_PERIOD,
        SCHEDULE_NOTIFICATION_EXECUTOR_TIME_UNIT);
    }

    private static void sendMessage(AbsSender sender, long chatId, long messageId, String text,
                                    InlineKeyboardMarkup buttons,
                                    Map<Long, Map<Long, List<Message>>> notificationMessages) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);
        if (buttons != null) {
            message.setReplyMarkup(buttons);
        }
        try {
            Message sendedMessage = sender.execute(message);
            addNotificationMessage(notificationMessages, sendedMessage, chatId, messageId);
        } catch (TelegramApiException e) {
            LOGGER.error(String.format("Cannot send message: %s", e.getMessage()));
        }
    }

    private static void sendMessageWithoutDelete(AbsSender sender, long chatId, String text,
                                                 InlineKeyboardMarkup buttons) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);
        if (buttons != null) {
            message.setReplyMarkup(buttons);
        }
        try {
            sender.execute(message);
        } catch (TelegramApiException e) {
            LOGGER.error(String.format("Cannot send message: %s", e.getMessage()));
        }
    }

    private static void addNotificationMessage(Map<Long, Map<Long, List<Message>>> notificationMessages,
                                               Message message, Long chatId, Long messageId) {
        if (notificationMessages.get(chatId) == null) {
            Map<Long, List<Message>> newMessagesMap = new ConcurrentHashMap<>();
            List<Message> newMessage = new CopyOnWriteArrayList<>();
            newMessage.add(message);
            newMessagesMap.put(messageId, newMessage);
            notificationMessages.put(chatId, newMessagesMap);
        } else {
            Map<Long, List<Message>> currentMessagesMap = notificationMessages.get(chatId);
            currentMessagesMap.computeIfAbsent(messageId, k -> new CopyOnWriteArrayList<>());
            currentMessagesMap.get(messageId).add(message);
            notificationMessages.put(chatId, currentMessagesMap);
        }
    }

    private static InlineKeyboardMarkup ifNextUpdate(long chatId, long messageId, long addNextUpdateTime) {
        List<List<InlineKeyboardButton>> answers = new ArrayList<>(1);
        List<InlineKeyboardButton> yesNoAnswer = new ArrayList<>(1);
        yesNoAnswer.add(InlineKeyboardButton.builder()
                .text("Да")
                .callbackData(String.format("%s %d %d %d 1", NOTIFICATION_CALLBACK_DATA, chatId, messageId,
                        addNextUpdateTime))
                .build());
        yesNoAnswer.add(InlineKeyboardButton.builder()
                .text("Нет")
                .callbackData(String.format("%s %d %d 0 0", NOTIFICATION_CALLBACK_DATA, chatId, messageId))
                .build());
        answers.add(yesNoAnswer);
        InlineKeyboardMarkup ikm = new InlineKeyboardMarkup();
        ikm.setKeyboard(answers);
        return ikm;
    }

    public static void deleteMessages(AbsSender absSender,
                                      Map<Long, Map<Long, List<Message>>> notificationMessages,
                                      Long chatId, Long messageId) {
        DeleteMessage deleteLastMessage = new DeleteMessage();
        if (notificationMessages.get(chatId) != null) {
            for (Message message : notificationMessages.get(chatId).get(messageId)) {
                deleteLastMessage.setMessageId(message.getMessageId());
                deleteLastMessage.setChatId(message.getChatId());
                try {
                    absSender.execute(deleteLastMessage);
                } catch (TelegramApiException e) {
                    LOGGER.error(String.format("Cannot delete message due to: %s", e.getMessage()));
                }
            }
            notificationMessages.get(chatId).remove(messageId);
        }
    }
}
