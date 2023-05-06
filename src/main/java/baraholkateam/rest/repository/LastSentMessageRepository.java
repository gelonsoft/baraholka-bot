package baraholkateam.rest.repository;

import baraholkateam.rest.model.LastSentMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * JPA репозиторий сущности "LastSentMessage"
 */
@Repository
public interface LastSentMessageRepository extends JpaRepository<LastSentMessage, Long> {

}
