package baraholkateam.rest.repository;

import baraholkateam.rest.model.PreviousState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * JPA репозиторий сущности "PreviousState"
 */
@Repository
public interface PreviousStateRepository extends JpaRepository<PreviousState, Long> {

}
