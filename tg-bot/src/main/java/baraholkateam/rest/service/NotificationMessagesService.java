package baraholkateam.rest.service;

import baraholkateam.rest.model.NotificationMessages;
import baraholkateam.rest.repository.NotificationMessagesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Сервис взаимодействия с сущностью "NotificationMessages".
 */
@Service
public class NotificationMessagesService {

    @Autowired
    private NotificationMessagesRepository notificationMessagesRepository;

    public Map<Long, List<Message>> get(Long chatId) {
        List<NotificationMessages> notificationMessages = notificationMessagesRepository.findAllByChatId(chatId);
        Map<Long, List<NotificationMessages>> resultNotification = notificationMessages.stream()
                .collect(
                        Collectors.groupingBy(
                                NotificationMessages::getMessageId,
                                LinkedHashMap::new,
                                Collectors.toList()
                        )
                );
        Map<Long, List<Message>> result = new LinkedHashMap<>();
        for (Map.Entry<Long, List<NotificationMessages>> notificationEntry : resultNotification.entrySet()) {
            result.put(notificationEntry.getKey(),
                    notificationEntry.getValue().stream().map(NotificationMessages::getNotificationMessage).toList());
        }
        return result;
    }

    public void put(Long chatId, Map<Long, List<Message>> messages) {
        for (Map.Entry<Long, List<Message>> message : messages.entrySet()) {
            for (Message msg : message.getValue()) {
                notificationMessagesRepository.save(new NotificationMessages(chatId, message.getKey(), msg));
            }
        }
    }

    public void removeMessage(Long chatId, Long messageId) {
        List<NotificationMessages> notificationMessages =
                notificationMessagesRepository.findAllByChatIdAndMessageId(chatId, messageId);
        for (NotificationMessages notificationMessage : notificationMessages) {
            notificationMessagesRepository.delete(notificationMessage);
        }
    }

}
