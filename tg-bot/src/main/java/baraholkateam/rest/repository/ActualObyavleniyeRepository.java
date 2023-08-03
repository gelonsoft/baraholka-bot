package baraholkateam.rest.repository;

import baraholkateam.rest.model.ActualObyavleniye;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * JPA репозиторий сущности "ActualObyavleniye".
 */
@Repository
public interface ActualObyavleniyeRepository extends JpaRepository<ActualObyavleniye, Long> {

    List<ActualObyavleniye> findAllByNextUpdateTimeLessThanEqual(Long currentTime);

    List<ActualObyavleniye> findAllByOwnerChatId(Long ownerChatId);

    @Query(value = "SELECT * FROM \"actual_obyavleniye\" WHERE ?1 <@ tags ORDER BY creation_time DESC LIMIT ?2",
            nativeQuery = true)
    List<ActualObyavleniye> findAllByTagsIn(String[] tags, Integer limit);

}
