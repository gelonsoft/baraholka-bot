package baraholkateam.rest.repository;

import baraholkateam.rest.model.CurrentObyavleniye;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * JPA репозиторий сущности "CurrentObyavleniye".
 */
@Repository
public interface CurrentObyavleniyeRepository extends JpaRepository<CurrentObyavleniye, Long> {

}
