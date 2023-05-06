package baraholkateam.rest.repository;

import baraholkateam.rest.model.CurrentAdvertisement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * JPA репозиторий сущности "CurrentAdvertisement"
 */
@Repository
public interface CurrentAdvertisementRepository extends JpaRepository<CurrentAdvertisement, Long> {

}
