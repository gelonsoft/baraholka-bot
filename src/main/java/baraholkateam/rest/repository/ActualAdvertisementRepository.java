package baraholkateam.rest.repository;

import baraholkateam.rest.model.ActualAdvertisement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * JPA репозиторий сущности "ActualAdvertisement".
 */
@Repository
public interface ActualAdvertisementRepository extends JpaRepository<ActualAdvertisement, Long> {

    List<ActualAdvertisement> findAllByNextUpdateTimeLessThanEqual(Long currentTime);

    List<ActualAdvertisement> findAllByOwnerChatId(Long ownerChatId);

    @Query(value = "SELECT * FROM \"actual_advertisement\" WHERE ?1 <@ tags ORDER BY creation_time DESC LIMIT ?2",
            nativeQuery = true)
    List<ActualAdvertisement> findAllByTagsIn(String[] tags, Integer limit);

}
