package baraholkateam.notification;

import baraholkateam.bot.BaraholkaBot;
import baraholkateam.bot.BaraholkaBotProperties;
import baraholkateam.rest.model.ActualAdvertisement;
import baraholkateam.rest.service.ActualAdvertisementService;
import baraholkateam.rest.service.NotificationMessagesService;
import baraholkateam.telegram_api_requests.TelegramAPIRequests;
import baraholkateam.util.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageCaption;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

import static baraholkateam.command.Command.ADVERTISEMENT_DELETE;
import static baraholkateam.command.Command.NOTIFICATION_CALLBACK_DATA;
import static baraholkateam.command.DeleteAdvertisement.NOT_ACTUAL_TEXT;

@Component
public class NotificationExecutor {
    private static final String DELETE_IF_NOT_UPDATE = """
            Вы пропустили 2 уведомления с вопросом об актуальности объявления.
            Через %s часов объявление будет автоматически удалено, если не подтвердить его актуальность.""";
    private static final String ASK_NEXT_UPDATE = """
            Является ли данное объявление актуальным?""";

    /**
     * Период времени до первого уведомления пользователя о подтверждении актуальности объявления.
     */
    public static final Long FIRST_REPEAT_NOTIFICATION_PERIOD = 10L;

    /**
     * Период времени до первого уведомления пользователя о подтверждении актуальности объявления.
     */
    public static final TimeUnit FIRST_REPEAT_NOTIFICATION_TIME_UNIT = TimeUnit.SECONDS;

    /**
     * Период времени до повторных уведомлений пользователя о подтверждении актуальности объявления.
     */
    private static final Long REPEAT_NOTIFICATION_PERIOD = 10L;

    /**
     * Период времени до повторных уведомлений пользователя о подтверждении актуальности объявления.
     */
    private static final TimeUnit REPEAT_NOTIFICATION_TIME_UNIT = TimeUnit.SECONDS;

    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationExecutor.class);

    @Autowired
    private TelegramAPIRequests telegramAPIRequests;

    @Autowired
    private ActualAdvertisementService actualAdvertisementService;

    @Autowired
    private NotificationMessagesService notificationMessagesService;

    private final BaraholkaBot sender;

    @Lazy
    @Autowired
    public NotificationExecutor(@Qualifier("BaraholkaBot") BaraholkaBot sender) {
        this.sender = sender;
    }

    @Scheduled(initialDelayString = "${notificator.initial-delay-in-milliseconds}",
            fixedRateString = "${notificator.fixed-rate-in-milliseconds}")
    private void startNotificationExecutor() {
        List<ActualAdvertisement> actualAdvertisements =
                     actualAdvertisementService.askActualAdvertisements(System.currentTimeMillis());
        for (ActualAdvertisement actualAdvertisement : actualAdvertisements) {
            int attemptNum = actualAdvertisement.getUpdateAttempt();
            long chatId = actualAdvertisement.getOwnerChatId();
            long messageId = actualAdvertisement.getMessageId();
            if (attemptNum == 3) {
                editAdText(sender, String.valueOf(messageId));
                actualAdvertisementService.removeAdvertisement(messageId);
                deleteMessages(sender, chatId, messageId);
                sendMessageWithoutDelete(sender, chatId, ADVERTISEMENT_DELETE, null);
            } else if (attemptNum <= 2) {
                actualAdvertisementService.setUpdateAttempt(messageId, attemptNum + 1);
                Long forwardedMessageId =
                        telegramAPIRequests.forwardMessage(BaraholkaBotProperties.CHANNEL_USERNAME,
                                String.valueOf(chatId), messageId);

                if (attemptNum == 2) {
                    deleteMessages(sender, chatId, messageId);
                    sendMessage(sender, chatId, messageId,
                            String.format("%s\n%s", String.format(DELETE_IF_NOT_UPDATE,
                                    REPEAT_NOTIFICATION_PERIOD), ASK_NEXT_UPDATE), ifNextUpdate(chatId,
                                    messageId,
                                    REPEAT_NOTIFICATION_TIME_UNIT.toMillis(REPEAT_NOTIFICATION_PERIOD)));
                } else if (attemptNum == 1) {
                    deleteMessages(sender, chatId, messageId);
                    sendMessage(sender, chatId, messageId, ASK_NEXT_UPDATE, ifNextUpdate(chatId, messageId,
                                    REPEAT_NOTIFICATION_TIME_UNIT.toMillis(REPEAT_NOTIFICATION_PERIOD)));
                } else {
                    sendMessage(sender, chatId, messageId, ASK_NEXT_UPDATE, ifNextUpdate(chatId, messageId,
                                    FIRST_REPEAT_NOTIFICATION_TIME_UNIT
                                            .toMillis(FIRST_REPEAT_NOTIFICATION_PERIOD)));
                }

                Message forwardedMessage = new Message();
                forwardedMessage.setMessageId(Math.toIntExact(forwardedMessageId));
                Chat chat = new Chat();
                chat.setId(chatId);
                forwardedMessage.setChat(chat);

                addNotificationMessage(forwardedMessage, chatId, messageId);
            } else {
                LOGGER.error(
                        String.format("Incorrect number of update attempt: %d. Chat id: %d, message id: %d",
                                attemptNum, chatId, messageId)
                );
            }
        }
    }

    public void deleteMessages(AbsSender absSender, Long chatId, Long messageId) {
        DeleteMessage deleteLastMessage = new DeleteMessage();
        if (notificationMessagesService.get(chatId) != null) {
            for (Message message : notificationMessagesService.get(chatId).get(messageId)) {
                deleteLastMessage.setMessageId(message.getMessageId());
                deleteLastMessage.setChatId(message.getChatId());
                try {
                    absSender.execute(deleteLastMessage);
                } catch (TelegramApiException e) {
                    LOGGER.error(String.format("Cannot delete message due to: %s", e.getMessage()));
                }
            }
            notificationMessagesService.removeMessage(chatId, messageId);
        }
    }

    private void sendMessage(AbsSender sender, long chatId, long messageId, String text,
                                    InlineKeyboardMarkup buttons) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);
        if (buttons != null) {
            message.setReplyMarkup(buttons);
        }
        try {
            Message sendedMessage = sender.execute(message);
            addNotificationMessage(sendedMessage, chatId, messageId);
        } catch (TelegramApiException e) {
            LOGGER.error(String.format("Cannot send message: %s", e.getMessage()));
        }
    }

    private void sendMessageWithoutDelete(AbsSender sender, long chatId, String text,
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

    private void addNotificationMessage(Message message, Long chatId, Long messageId) {
        Map<Long, List<Message>> currentMessagesMap = notificationMessagesService.get(chatId);
        if (currentMessagesMap == null) {
            Map<Long, List<Message>> newMessagesMap = new ConcurrentHashMap<>();
            List<Message> newMessage = new CopyOnWriteArrayList<>();
            newMessage.add(message);
            newMessagesMap.put(messageId, newMessage);
            notificationMessagesService.put(chatId, newMessagesMap);
        } else {
            List<Message> findMessages = currentMessagesMap.get(messageId);
            List<Message> currentMessage;
            if (findMessages == null) {
                currentMessage = new CopyOnWriteArrayList<>();
            } else {
                currentMessage = new CopyOnWriteArrayList<>(currentMessagesMap.get(messageId));
            }
            currentMessage.add(message);
            currentMessagesMap.put(messageId, currentMessage);
            notificationMessagesService.put(chatId, currentMessagesMap);
        }
    }

    private InlineKeyboardMarkup ifNextUpdate(long chatId, long messageId, long addNextUpdateTime) {
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

    private void editAdText(AbsSender absSender, String messageId) {
        EditMessageCaption editMessage = new EditMessageCaption();
        String editedText = String.format("%s\n\n%s", NOT_ACTUAL_TEXT,
                actualAdvertisementService.adText(Long.parseLong(messageId))
                .substring(Tag.Actual.getName().length() + 1));
        editMessage.setChatId(BaraholkaBotProperties.CHANNEL_CHAT_ID);
        editMessage.setMessageId(Integer.parseInt(messageId));
        editMessage.setParseMode(ParseMode.HTML);
        editMessage.setCaption(editedText);

        try {
            absSender.execute(editMessage);
        } catch (TelegramApiException e) {
            LOGGER.error(String.format("Cannot edit deleted message: %s", e.getMessage()));
        }
    }
}
