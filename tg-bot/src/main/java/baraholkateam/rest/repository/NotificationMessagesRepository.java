package baraholkateam.rest.repository;

import baraholkateam.rest.model.NotificationMessages;
import baraholkateam.rest.model.NotificationMessagesId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 *  JPA репозиторий сущности "NotificationMessages".
 */
@Repository
public interface NotificationMessagesRepository extends JpaRepository<NotificationMessages, NotificationMessagesId> {

    List<NotificationMessages> findAllByChatId(Long chatId);

    List<NotificationMessages> findAllByChatIdAndMessageId(Long chatId, Long messageId);

}
