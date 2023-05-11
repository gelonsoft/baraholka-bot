package baraholkateam.rest.service;

import baraholkateam.rest.model.ActualAdvertisement;
import baraholkateam.rest.model.CurrentAdvertisement;
import baraholkateam.rest.repository.ActualAdvertisementRepository;
import baraholkateam.util.TagType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static baraholkateam.bot.BaraholkaBot.SEARCH_ADVERTISEMENTS_LIMIT;

/**
 * Сервис взаимодействия с сущностью "ActualAdvertisement".
 */
@Service
public class ActualAdvertisementService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ActualAdvertisementService.class);

    @Autowired
    private ActualAdvertisementRepository actualAdvertisementRepository;

    public ActualAdvertisement get(Long messageId) {
        Optional<ActualAdvertisement> actualAdvertisementOptional = actualAdvertisementRepository.findById(messageId);
        if (actualAdvertisementOptional.isPresent()) {
            return actualAdvertisementOptional.get();
        } else {
            LOGGER.error(String.format("Actual advertisement for message id %d not found!", messageId));
            return null;
        }
    }

    public List<ActualAdvertisement> getByChatId(Long chatId) {
        return actualAdvertisementRepository.findAllByOwnerChatId(chatId);
    }

    public Long ownerChatId(Long messageId) {
        Optional<ActualAdvertisement> actualAdvertisementOptional = actualAdvertisementRepository.findById(messageId);
        if (actualAdvertisementOptional.isPresent()) {
            return actualAdvertisementOptional.get().getOwnerChatId();
        } else {
            LOGGER.error(String.format("Actual advertisement's owner chat id for message id %d not found!", messageId));
            return null;
        }
    }

    public List<String> getPhotoIds(Long messageId) {
        Optional<ActualAdvertisement> actualAdvertisementOptional = actualAdvertisementRepository.findById(messageId);
        if (actualAdvertisementOptional.isPresent()) {
            return actualAdvertisementOptional.get().getPhotoIds();
        } else {
            LOGGER.error(String.format("Actual advertisement's photo ids for message id %d not found!", messageId));
            return null;
        }
    }

    public String getDescription(Long messageId) {
        Optional<ActualAdvertisement> actualAdvertisementOptional = actualAdvertisementRepository.findById(messageId);
        if (actualAdvertisementOptional.isPresent()) {
            return actualAdvertisementOptional.get().getDescription();
        } else {
            LOGGER.error(String.format("Actual advertisement's description for message id %d not found!", messageId));
            return null;
        }
    }

    public List<String> getTags(Long messageId) {
        Optional<ActualAdvertisement> actualAdvertisementOptional = actualAdvertisementRepository.findById(messageId);
        if (actualAdvertisementOptional.isPresent()) {
            return actualAdvertisementOptional.get().getTags();
        } else {
            LOGGER.error(String.format("Actual advertisement's tags for message id %d not found!", messageId));
            return null;
        }
    }

    public String getTagsOfType(Long messageId, TagType tagType) {
        Optional<ActualAdvertisement> actualAdvertisementOptional = actualAdvertisementRepository.findById(messageId);
        if (actualAdvertisementOptional.isPresent()) {
            return actualAdvertisementOptional.get().getTagsOfType(tagType);
        } else {
            LOGGER.error(String.format("Actual advertisement's tags of type %s for message id %d not found!",
                    tagType.name(), messageId));
            return null;
        }
    }

    public Long getPrice(Long messageId) {
        Optional<ActualAdvertisement> actualAdvertisementOptional = actualAdvertisementRepository.findById(messageId);
        if (actualAdvertisementOptional.isPresent()) {
            return actualAdvertisementOptional.get().getPrice();
        } else {
            LOGGER.error(String.format("Actual advertisement's price for message id %d not found!", messageId));
            return null;
        }
    }

    public String getPhone(Long messageId) {
        Optional<ActualAdvertisement> actualAdvertisementOptional = actualAdvertisementRepository.findById(messageId);
        if (actualAdvertisementOptional.isPresent()) {
            return actualAdvertisementOptional.get().getPhone();
        } else {
            LOGGER.error(String.format("Actual advertisement's phone for message id %d not found!", messageId));
            return null;
        }
    }

    public List<String> getContacts(Long messageId) {
        Optional<ActualAdvertisement> actualAdvertisementOptional = actualAdvertisementRepository.findById(messageId);
        if (actualAdvertisementOptional.isPresent()) {
            return actualAdvertisementOptional.get().getContacts();
        } else {
            LOGGER.error(String.format("Actual advertisement's contacts for message id %d not found!", messageId));
            return null;
        }
    }

    public String getAdvertisementText(Long messageId) {
        Optional<ActualAdvertisement> actualAdvertisementOptional = actualAdvertisementRepository.findById(messageId);
        if (actualAdvertisementOptional.isPresent()) {
            return actualAdvertisementOptional.get().getAdvertisementText();
        } else {
            LOGGER.error(String.format("Actual advertisement's text for message id %d not found!", messageId));
            return null;
        }
    }

    public Long getCreationTime(Long messageId) {
        Optional<ActualAdvertisement> actualAdvertisementOptional = actualAdvertisementRepository.findById(messageId);
        if (actualAdvertisementOptional.isPresent()) {
            return actualAdvertisementOptional.get().getCreationTime();
        } else {
            LOGGER.error(String.format("Actual advertisement's creation time for message id %d not found!", messageId));
            return null;
        }
    }

    public Long getNextUpdateTime(Long messageId) {
        Optional<ActualAdvertisement> actualAdvertisementOptional = actualAdvertisementRepository.findById(messageId);
        if (actualAdvertisementOptional.isPresent()) {
            return actualAdvertisementOptional.get().getNextUpdateTime();
        } else {
            LOGGER.error(String.format("Actual advertisement's next update time for message id %d not found!",
                    messageId));
            return null;
        }
    }

    public Integer getUpdateAttempt(Long messageId) {
        Optional<ActualAdvertisement> actualAdvertisementOptional = actualAdvertisementRepository.findById(messageId);
        if (actualAdvertisementOptional.isPresent()) {
            return actualAdvertisementOptional.get().getUpdateAttempt();
        } else {
            LOGGER.error(String.format("Actual advertisement's update attempt for message id %d not found!",
                    messageId));
            return null;
        }
    }

    public ActualAdvertisement setOwnerChatId(Long messageId, Long ownerChatId) {
        Optional<ActualAdvertisement> actualAdvertisementOptional = actualAdvertisementRepository.findById(messageId);
        if (actualAdvertisementOptional.isPresent()) {
            ActualAdvertisement advertisement = actualAdvertisementOptional.get();
            advertisement.setOwnerChatId(ownerChatId);
            return actualAdvertisementRepository.save(advertisement);
        } else {
            LOGGER.error(String.format("Cannot set owner chat id for message id %d!", messageId));
            return null;
        }
    }

    public ActualAdvertisement setDescription(Long messageId, String description) {
        Optional<ActualAdvertisement> actualAdvertisementOptional = actualAdvertisementRepository.findById(messageId);
        if (actualAdvertisementOptional.isPresent()) {
            ActualAdvertisement advertisement = actualAdvertisementOptional.get();
            advertisement.setDescription(description);
            return actualAdvertisementRepository.save(advertisement);
        } else {
            LOGGER.error(String.format("Cannot set description for message id %d!", messageId));
            return null;
        }
    }

    public ActualAdvertisement setPrice(Long messageId, Long price) {
        Optional<ActualAdvertisement> actualAdvertisementOptional = actualAdvertisementRepository.findById(messageId);
        if (actualAdvertisementOptional.isPresent()) {
            ActualAdvertisement advertisement = actualAdvertisementOptional.get();
            advertisement.setPrice(price);
            return actualAdvertisementRepository.save(advertisement);
        } else {
            LOGGER.error(String.format("Cannot set price for message id %d!", messageId));
            return null;
        }
    }

    public ActualAdvertisement setPhone(Long messageId, String phone) {
        Optional<ActualAdvertisement> actualAdvertisementOptional = actualAdvertisementRepository.findById(messageId);
        if (actualAdvertisementOptional.isPresent()) {
            ActualAdvertisement advertisement = actualAdvertisementOptional.get();
            advertisement.setPhone(phone);
            return actualAdvertisementRepository.save(advertisement);
        } else {
            LOGGER.error(String.format("Cannot set phone for message id %d!", messageId));
            return null;
        }
    }

    public ActualAdvertisement addSocial(Long messageId, String social) {
        Optional<ActualAdvertisement> actualAdvertisementOptional = actualAdvertisementRepository.findById(messageId);
        if (actualAdvertisementOptional.isPresent()) {
            ActualAdvertisement advertisement = actualAdvertisementOptional.get();
            advertisement.addSocial(social);
            return actualAdvertisementRepository.save(advertisement);
        } else {
            LOGGER.error(String.format("Cannot add social for message id %d!", messageId));
            return null;
        }
    }

    public ActualAdvertisement setSocials(Long messageId, List<String> socials) {
        Optional<ActualAdvertisement> actualAdvertisementOptional = actualAdvertisementRepository.findById(messageId);
        if (actualAdvertisementOptional.isPresent()) {
            ActualAdvertisement advertisement = actualAdvertisementOptional.get();
            advertisement.setSocials(socials);
            return actualAdvertisementRepository.save(advertisement);
        } else {
            LOGGER.error(String.format("Cannot set socials for message id %d!", messageId));
            return null;
        }
    }

    public ActualAdvertisement addPhoto(Long messageId, String photoId) {
        Optional<ActualAdvertisement> actualAdvertisementOptional = actualAdvertisementRepository.findById(messageId);
        if (actualAdvertisementOptional.isPresent()) {
            ActualAdvertisement advertisement = actualAdvertisementOptional.get();
            advertisement.addPhotoIds(photoId);
            return actualAdvertisementRepository.save(advertisement);
        } else {
            LOGGER.error(String.format("Cannot add photo for message id %d!", messageId));
            return null;
        }
    }

    public ActualAdvertisement setPhotos(Long messageId, List<String> photoIds) {
        Optional<ActualAdvertisement> actualAdvertisementOptional = actualAdvertisementRepository.findById(messageId);
        if (actualAdvertisementOptional.isPresent()) {
            ActualAdvertisement advertisement = actualAdvertisementOptional.get();
            advertisement.setPhotos(photoIds);
            return actualAdvertisementRepository.save(advertisement);
        } else {
            LOGGER.error(String.format("Cannot add photo for message id %d!", messageId));
            return null;
        }
    }

    public ActualAdvertisement addTags(Long messageId, List<String> tags) {
        Optional<ActualAdvertisement> actualAdvertisementOptional = actualAdvertisementRepository.findById(messageId);
        if (actualAdvertisementOptional.isPresent()) {
            ActualAdvertisement advertisement = actualAdvertisementOptional.get();
            advertisement.addTags(tags);
            return actualAdvertisementRepository.save(advertisement);
        } else {
            LOGGER.error(String.format("Cannot add tags for message id %d!", messageId));
            return null;
        }
    }

    public ActualAdvertisement addTag(Long messageId, String tag) {
        Optional<ActualAdvertisement> actualAdvertisementOptional = actualAdvertisementRepository.findById(messageId);
        if (actualAdvertisementOptional.isPresent()) {
            ActualAdvertisement advertisement = actualAdvertisementOptional.get();
            advertisement.addTag(tag);
            return actualAdvertisementRepository.save(advertisement);
        } else {
            LOGGER.error(String.format("Cannot add tag for message id %d!", messageId));
            return null;
        }
    }

    public ActualAdvertisement setCreationTime(Long messageId, Long creationTime) {
        Optional<ActualAdvertisement> actualAdvertisementOptional = actualAdvertisementRepository.findById(messageId);
        if (actualAdvertisementOptional.isPresent()) {
            ActualAdvertisement advertisement = actualAdvertisementOptional.get();
            advertisement.setCreationTime(creationTime);
            return actualAdvertisementRepository.save(advertisement);
        } else {
            LOGGER.error(String.format("Cannot add creation time for message id %d!", messageId));
            return null;
        }
    }

    public ActualAdvertisement setNextUpdateTime(Long messageId, Long nextUpdateTime) {
        Optional<ActualAdvertisement> actualAdvertisementOptional = actualAdvertisementRepository.findById(messageId);
        if (actualAdvertisementOptional.isPresent()) {
            ActualAdvertisement advertisement = actualAdvertisementOptional.get();
            advertisement.setNextUpdateTime(nextUpdateTime);
            return actualAdvertisementRepository.save(advertisement);
        } else {
            LOGGER.error(String.format("Cannot add next update time for message id %d!", messageId));
            return null;
        }
    }

    public ActualAdvertisement setUpdateAttempt(Long messageId, Integer updateAttempt) {
        Optional<ActualAdvertisement> actualAdvertisementOptional = actualAdvertisementRepository.findById(messageId);
        if (actualAdvertisementOptional.isPresent()) {
            ActualAdvertisement advertisement = actualAdvertisementOptional.get();
            advertisement.setUpdateAttempt(updateAttempt);
            return actualAdvertisementRepository.save(advertisement);
        } else {
            LOGGER.error(String.format("Cannot add tag for message id %d!", messageId));
            return null;
        }
    }

    public void insertNewAdvertisement(CurrentAdvertisement currentAdvertisement) {
        ActualAdvertisement actualAdvertisement = new ActualAdvertisement(currentAdvertisement.getChatId())
                .setMessageId(currentAdvertisement.getMessageId())
                .setPhotos(currentAdvertisement.getPhotoIds())
                .setDescription(currentAdvertisement.getDescription())
                .setPrice(currentAdvertisement.getPrice())
                .addTags(currentAdvertisement.getTags())
                .setPhone(currentAdvertisement.getPhone())
                .setSocials(currentAdvertisement.getContacts())
                .setCreationTime(currentAdvertisement.getCreationTime())
                .setNextUpdateTime(currentAdvertisement.getNextUpdateTime())
                .setUpdateAttempt(currentAdvertisement.getUpdateAttempt());
        actualAdvertisementRepository.save(actualAdvertisement);
    }

    public void removeAdvertisement(Long messageId) {
        actualAdvertisementRepository.deleteById(messageId);
    }

    public List<ActualAdvertisement> askActualAdvertisements(Long currentTime) {
        return actualAdvertisementRepository.findAllByNextUpdateTimeLessThanEqual(currentTime);
    }

    public String adText(Long messageId) {
        Optional<ActualAdvertisement> actualAdvertisementOptional = actualAdvertisementRepository.findById(messageId);
        if (actualAdvertisementOptional.isPresent()) {
            ActualAdvertisement advertisement = actualAdvertisementOptional.get();
            return advertisement.getAdvertisementText();
        } else {
            LOGGER.error(String.format("Cannot get advertisement text for message id %d!", messageId));
            return null;
        }
    }

    public List<ActualAdvertisement> tagsSearch(String[] tags) {
        return actualAdvertisementRepository.findAllByTagsIn(tags, SEARCH_ADVERTISEMENTS_LIMIT);
    }
}
