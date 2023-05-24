package baraholkateam.rest.model;

import lombok.Data;
import org.telegram.telegrambots.meta.api.objects.Message;

@Data
public class NotificationMessagesId {
    private Long chatId;
    private Long messageId;
    private Message notificationMessage;
}
