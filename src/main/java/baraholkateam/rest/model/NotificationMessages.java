package baraholkateam.rest.model;

import jakarta.persistence.*;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.List;

/**
 * Сообщения, отправляемые пользователю во время уточнения актуальности уведомлений
 */
@Entity
@IdClass(NotificationMessagesId.class)
@Table(name = "notification_messages")
public class NotificationMessages {
    @Id
    @Column(name = "chat_id")
    private Long chatId;

    @Id
    @Column(name = "message_id")
    private Long messageId;

    @Id
    @Column(name = "notification_message")
    private Message notificationMessage;

    public NotificationMessages() {

    }

    public NotificationMessages(Long chatId, Long messageId, Message message) {
        this.chatId = chatId;
        this.messageId = messageId;
        this.notificationMessage = message;
    }

    public Long getChatId() {
        return chatId;
    }

    public Long getMessageId() {
        return messageId;
    }

    public Message getNotificationMessage() {
        return notificationMessage;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    public void setNotificationMessages(Message notificationMessage) {
        this.notificationMessage = notificationMessage;
    }
}
