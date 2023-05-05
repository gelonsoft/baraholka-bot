package baraholkateam.rest.repository;

import baraholkateam.rest.model.Advertisement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdvertisementRepository extends JpaRepository<Advertisement, Long> {
    List<Advertisement> findByOwnerChatId(Long ownerChatId);
}
