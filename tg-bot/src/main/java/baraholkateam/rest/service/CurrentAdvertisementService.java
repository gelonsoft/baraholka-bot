package baraholkateam.rest.service;

import baraholkateam.rest.model.CurrentAdvertisement;
import baraholkateam.rest.repository.CurrentAdvertisementRepository;
import baraholkateam.util.TagType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Сервис взаимодействия с сущностью "CurrentAdvertisement".
 */
@Service
public class CurrentAdvertisementService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CurrentAdvertisementService.class);

    @Autowired
    private CurrentAdvertisementRepository currentAdvertisementRepository;

    public CurrentAdvertisement get(Long chatId) {
        Optional<CurrentAdvertisement> currentAdvertisementOptional = currentAdvertisementRepository.findById(chatId);
        if (currentAdvertisementOptional.isPresent()) {
            return currentAdvertisementOptional.get();
        } else {
            LOGGER.error(String.format("Current advertisement for chat %d not found!", chatId));
            return null;
        }
    }

    public void put(CurrentAdvertisement currentAdvertisement) {
        currentAdvertisementRepository.save(currentAdvertisement);
    }

    public Long getMessageId(Long chatId) {
        Optional<CurrentAdvertisement> currentAdvertisementOptional = currentAdvertisementRepository.findById(chatId);
        if (currentAdvertisementOptional.isPresent()) {
            return currentAdvertisementOptional.get().getMessageId();
        } else {
            LOGGER.error(String.format("Current advertisement's message id for chat %d not found!", chatId));
            return null;
        }
    }

    public List<String> getPhotos(Long chatId) {
        Optional<CurrentAdvertisement> currentAdvertisementOptional = currentAdvertisementRepository.findById(chatId);
        if (currentAdvertisementOptional.isPresent()) {
            return currentAdvertisementOptional.get().getPhotos();
        } else {
            LOGGER.error(String.format("Current advertisement's photo ids for chat %d not found!", chatId));
            return null;
        }
    }

    public String getDescription(Long chatId) {
        Optional<CurrentAdvertisement> currentAdvertisementOptional = currentAdvertisementRepository.findById(chatId);
        if (currentAdvertisementOptional.isPresent()) {
            return currentAdvertisementOptional.get().getDescription();
        } else {
            LOGGER.error(String.format("Current advertisement's description for chat %d not found!", chatId));
            return null;
        }
    }

    public List<String> getTags(Long chatId) {
        Optional<CurrentAdvertisement> currentAdvertisementOptional = currentAdvertisementRepository.findById(chatId);
        if (currentAdvertisementOptional.isPresent()) {
            return currentAdvertisementOptional.get().getTags();
        } else {
            LOGGER.error(String.format("Current advertisement's tags for chat %d not found!", chatId));
            return null;
        }
    }

    public String getTagsOfType(Long chatId, TagType tagType) {
        Optional<CurrentAdvertisement> currentAdvertisementOptional = currentAdvertisementRepository.findById(chatId);
        if (currentAdvertisementOptional.isPresent()) {
            return currentAdvertisementOptional.get().getTagsOfType(tagType);
        } else {
            LOGGER.error(String.format("Current advertisement's tags of type %s for chat %d not found!",
                    tagType.name(), chatId));
            return null;
        }
    }

    public Long getPrice(Long chatId) {
        Optional<CurrentAdvertisement> currentAdvertisementOptional = currentAdvertisementRepository.findById(chatId);
        if (currentAdvertisementOptional.isPresent()) {
            return currentAdvertisementOptional.get().getPrice();
        } else {
            LOGGER.error(String.format("Current advertisement's price for chat %d not found!", chatId));
            return null;
        }
    }

    public String getPhone(Long chatId) {
        Optional<CurrentAdvertisement> currentAdvertisementOptional = currentAdvertisementRepository.findById(chatId);
        if (currentAdvertisementOptional.isPresent()) {
            return currentAdvertisementOptional.get().getPhone();
        } else {
            LOGGER.error(String.format("Current advertisement's phone for chat %d not found!", chatId));
            return null;
        }
    }

    public List<String> getContacts(Long chatId) {
        Optional<CurrentAdvertisement> currentAdvertisementOptional = currentAdvertisementRepository.findById(chatId);
        if (currentAdvertisementOptional.isPresent()) {
            return currentAdvertisementOptional.get().getContacts();
        } else {
            LOGGER.error(String.format("Current advertisement's contacts for chat %d not found!", chatId));
            return null;
        }
    }

    public String getAdvertisementText(Long chatId) {
        Optional<CurrentAdvertisement> currentAdvertisementOptional = currentAdvertisementRepository.findById(chatId);
        if (currentAdvertisementOptional.isPresent()) {
            return currentAdvertisementOptional.get().getAdvertisementText();
        } else {
            LOGGER.error(String.format("Current advertisement's text for chat %d not found!", chatId));
            return null;
        }
    }

    public Long getCreationTime(Long chatId) {
        Optional<CurrentAdvertisement> currentAdvertisementOptional = currentAdvertisementRepository.findById(chatId);
        if (currentAdvertisementOptional.isPresent()) {
            return currentAdvertisementOptional.get().getCreationTime();
        } else {
            LOGGER.error(String.format("Current advertisement's creation time for chat %d not found!", chatId));
            return null;
        }
    }

    public Long getNextUpdateTime(Long chatId) {
        Optional<CurrentAdvertisement> currentAdvertisementOptional = currentAdvertisementRepository.findById(chatId);
        if (currentAdvertisementOptional.isPresent()) {
            return currentAdvertisementOptional.get().getNextUpdateTime();
        } else {
            LOGGER.error(String.format("Current advertisement's next update time for chat %d not found!", chatId));
            return null;
        }
    }

    public Integer getUpdateAttempt(Long chatId) {
        Optional<CurrentAdvertisement> currentAdvertisementOptional = currentAdvertisementRepository.findById(chatId);
        if (currentAdvertisementOptional.isPresent()) {
            return currentAdvertisementOptional.get().getUpdateAttempt();
        } else {
            LOGGER.error(String.format("Current advertisement's update attempt for chat %d not found!", chatId));
            return null;
        }
    }

    public CurrentAdvertisement setMessageId(Long chatId, Long messageId) {
        Optional<CurrentAdvertisement> currentAdvertisementOptional = currentAdvertisementRepository.findById(chatId);
        if (currentAdvertisementOptional.isPresent()) {
            CurrentAdvertisement advertisement = currentAdvertisementOptional.get();
            advertisement.setMessageId(messageId);
            return currentAdvertisementRepository.save(advertisement);
        } else {
            LOGGER.error(String.format("Cannot set message id for chat %d!", chatId));
            return null;
        }
    }

    public CurrentAdvertisement setDescription(Long chatId, String description) {
        Optional<CurrentAdvertisement> currentAdvertisementOptional = currentAdvertisementRepository.findById(chatId);
        if (currentAdvertisementOptional.isPresent()) {
            CurrentAdvertisement advertisement = currentAdvertisementOptional.get();
            advertisement.setDescription(description);
            return currentAdvertisementRepository.save(advertisement);
        } else {
            LOGGER.error(String.format("Cannot set description for chat %d!", chatId));
            return null;
        }
    }

    public CurrentAdvertisement setPrice(Long chatId, Long price) {
        Optional<CurrentAdvertisement> currentAdvertisementOptional = currentAdvertisementRepository.findById(chatId);
        if (currentAdvertisementOptional.isPresent()) {
            CurrentAdvertisement advertisement = currentAdvertisementOptional.get();
            advertisement.setPrice(price);
            return currentAdvertisementRepository.save(advertisement);
        } else {
            LOGGER.error(String.format("Cannot set price for chat %d!", chatId));
            return null;
        }
    }

    public CurrentAdvertisement setTags(Long chatId, List<String> tags) {
        Optional<CurrentAdvertisement> currentAdvertisementOptional = currentAdvertisementRepository.findById(chatId);
        if (currentAdvertisementOptional.isPresent()) {
            CurrentAdvertisement advertisement = currentAdvertisementOptional.get();
            advertisement.setTags(tags);
            return currentAdvertisementRepository.save(advertisement);
        } else {
            LOGGER.error(String.format("Cannot set price for chat %d!", chatId));
            return null;
        }
    }

    public CurrentAdvertisement setPhone(Long chatId, String phone) {
        Optional<CurrentAdvertisement> currentAdvertisementOptional = currentAdvertisementRepository.findById(chatId);
        if (currentAdvertisementOptional.isPresent()) {
            CurrentAdvertisement advertisement = currentAdvertisementOptional.get();
            advertisement.setPhone(phone);
            return currentAdvertisementRepository.save(advertisement);
        } else {
            LOGGER.error(String.format("Cannot set phone for chat %d!", chatId));
            return null;
        }
    }

    public CurrentAdvertisement addSocial(Long chatId, String social) {
        Optional<CurrentAdvertisement> currentAdvertisementOptional = currentAdvertisementRepository.findById(chatId);
        if (currentAdvertisementOptional.isPresent()) {
            CurrentAdvertisement advertisement = currentAdvertisementOptional.get();
            advertisement.addSocial(social);
            return currentAdvertisementRepository.save(advertisement);
        } else {
            LOGGER.error(String.format("Cannot add social for chat %d!", chatId));
            return null;
        }
    }

    public CurrentAdvertisement setSocials(Long chatId, List<String> socials) {
        Optional<CurrentAdvertisement> currentAdvertisementOptional = currentAdvertisementRepository.findById(chatId);
        if (currentAdvertisementOptional.isPresent()) {
            CurrentAdvertisement advertisement = currentAdvertisementOptional.get();
            advertisement.setSocials(socials);
            return currentAdvertisementRepository.save(advertisement);
        } else {
            LOGGER.error(String.format("Cannot add social for chat %d!", chatId));
            return null;
        }
    }

    public CurrentAdvertisement addPhoto(Long chatId, String photo) {
        Optional<CurrentAdvertisement> currentAdvertisementOptional = currentAdvertisementRepository.findById(chatId);
        if (currentAdvertisementOptional.isPresent()) {
            CurrentAdvertisement advertisement = currentAdvertisementOptional.get();
            advertisement.addPhoto(photo);
            return currentAdvertisementRepository.save(advertisement);
        } else {
            LOGGER.error(String.format("Cannot add photo for chat %d!", chatId));
            return null;
        }
    }

    public CurrentAdvertisement setPhotos(Long chatId, List<String> photos) {
        Optional<CurrentAdvertisement> currentAdvertisementOptional = currentAdvertisementRepository.findById(chatId);
        if (currentAdvertisementOptional.isPresent()) {
            CurrentAdvertisement advertisement = currentAdvertisementOptional.get();
            advertisement.setPhotos(photos);
            return currentAdvertisementRepository.save(advertisement);
        } else {
            LOGGER.error(String.format("Cannot add photo for chat %d!", chatId));
            return null;
        }
    }

    public CurrentAdvertisement addTags(Long chatId, List<String> tags) {
        Optional<CurrentAdvertisement> currentAdvertisementOptional = currentAdvertisementRepository.findById(chatId);
        if (currentAdvertisementOptional.isPresent()) {
            CurrentAdvertisement advertisement = currentAdvertisementOptional.get();
            advertisement.addTags(tags);
            return currentAdvertisementRepository.save(advertisement);
        } else {
            LOGGER.error(String.format("Cannot add tags for chat %d!", chatId));
            return null;
        }
    }

    public CurrentAdvertisement addTag(Long chatId, String tag) {
        Optional<CurrentAdvertisement> currentAdvertisementOptional = currentAdvertisementRepository.findById(chatId);
        if (currentAdvertisementOptional.isPresent()) {
            CurrentAdvertisement advertisement = currentAdvertisementOptional.get();
            advertisement.addTag(tag);
            return currentAdvertisementRepository.save(advertisement);
        } else {
            LOGGER.error(String.format("Cannot add tag for chat %d!", chatId));
            return null;
        }
    }

    public CurrentAdvertisement setCreationTime(Long chatId, Long creationTime) {
        Optional<CurrentAdvertisement> currentAdvertisementOptional = currentAdvertisementRepository.findById(chatId);
        if (currentAdvertisementOptional.isPresent()) {
            CurrentAdvertisement advertisement = currentAdvertisementOptional.get();
            advertisement.setCreationTime(creationTime);
            return currentAdvertisementRepository.save(advertisement);
        } else {
            LOGGER.error(String.format("Cannot add creation time for chat %d!", chatId));
            return null;
        }
    }

    public CurrentAdvertisement setNextUpdateTime(Long chatId, Long nextUpdateTime) {
        Optional<CurrentAdvertisement> currentAdvertisementOptional = currentAdvertisementRepository.findById(chatId);
        if (currentAdvertisementOptional.isPresent()) {
            CurrentAdvertisement advertisement = currentAdvertisementOptional.get();
            advertisement.setNextUpdateTime(nextUpdateTime);
            return currentAdvertisementRepository.save(advertisement);
        } else {
            LOGGER.error(String.format("Cannot add next update time for chat %d!", chatId));
            return null;
        }
    }

    public CurrentAdvertisement setUpdateAttempt(Long chatId, Integer updateAttempt) {
        Optional<CurrentAdvertisement> currentAdvertisementOptional = currentAdvertisementRepository.findById(chatId);
        if (currentAdvertisementOptional.isPresent()) {
            CurrentAdvertisement advertisement = currentAdvertisementOptional.get();
            advertisement.setUpdateAttempt(updateAttempt);
            return currentAdvertisementRepository.save(advertisement);
        } else {
            LOGGER.error(String.format("Cannot add tag for chat %d!", chatId));
            return null;
        }
    }

    public void remove(Long chatId) {
        currentAdvertisementRepository.deleteById(chatId);
    }
}
