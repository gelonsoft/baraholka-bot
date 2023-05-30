package baraholkateam.rest.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.objects.Message;

/**
 * Последнее сообщение, отправленное ботом пользователю.
 */
@Entity
@Table(name = "last_sent_message")
public class LastSentMessage {

    private static final Logger LOGGER = LoggerFactory.getLogger(LastSentMessage.class);

    @Id
    @Column(name = "chat_id")
    private Long chatId;

    @Column(name = "message")
    private Message message;

    public LastSentMessage() {

    }

    public LastSentMessage(Long chatId, Message message) {
        this.chatId = chatId;
        this.message = message;
    }

    public Message getMessage() {
        if (message == null) {
            LOGGER.warn("Last message is null!");
        }
        return message;
    }
}
