package baraholkateam.rest.repository;

import baraholkateam.rest.model.CurrentState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * JPA репозиторий сущности "CurrentState".
 */
@Repository
public interface CurrentStateRepository extends JpaRepository<CurrentState, Long> {

}
