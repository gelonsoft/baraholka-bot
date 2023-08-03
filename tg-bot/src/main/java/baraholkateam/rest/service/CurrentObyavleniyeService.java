package baraholkateam.rest.service;

import baraholkateam.rest.model.CurrentObyavleniye;
import baraholkateam.rest.repository.CurrentObyavleniyeRepository;
import baraholkateam.util.TagType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Сервис взаимодействия с сущностью "CurrentObyavleniye".
 */
@Service
public class CurrentObyavleniyeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CurrentObyavleniyeService.class);

    @Autowired
    private CurrentObyavleniyeRepository currentObyavleniyeRepository;

    public CurrentObyavleniye get(Long chatId) {
        Optional<CurrentObyavleniye> currentObyavleniyeOptional = currentObyavleniyeRepository.findById(chatId);
        if (currentObyavleniyeOptional.isPresent()) {
            return currentObyavleniyeOptional.get();
        } else {
            LOGGER.error(String.format("Current obyavleniye for chat %d not found!", chatId));
            return null;
        }
    }

    public void put(CurrentObyavleniye currentObyavleniye) {
        currentObyavleniyeRepository.save(currentObyavleniye);
    }

    public Long getMessageId(Long chatId) {
        Optional<CurrentObyavleniye> currentObyavleniyeOptional = currentObyavleniyeRepository.findById(chatId);
        if (currentObyavleniyeOptional.isPresent()) {
            return currentObyavleniyeOptional.get().getMessageId();
        } else {
            LOGGER.error(String.format("Current obyavleniye's message id for chat %d not found!", chatId));
            return null;
        }
    }

    public List<String> getPhotos(Long chatId) {
        Optional<CurrentObyavleniye> currentObyavleniyeOptional = currentObyavleniyeRepository.findById(chatId);
        if (currentObyavleniyeOptional.isPresent()) {
            return currentObyavleniyeOptional.get().getPhotos();
        } else {
            LOGGER.error(String.format("Current obyavleniye's photo ids for chat %d not found!", chatId));
            return null;
        }
    }

    public String getDescription(Long chatId) {
        Optional<CurrentObyavleniye> currentObyavleniyeOptional = currentObyavleniyeRepository.findById(chatId);
        if (currentObyavleniyeOptional.isPresent()) {
            return currentObyavleniyeOptional.get().getDescription();
        } else {
            LOGGER.error(String.format("Current obyavleniye's description for chat %d not found!", chatId));
            return null;
        }
    }

    public List<String> getTags(Long chatId) {
        Optional<CurrentObyavleniye> currentObyavleniyeOptional = currentObyavleniyeRepository.findById(chatId);
        if (currentObyavleniyeOptional.isPresent()) {
            return currentObyavleniyeOptional.get().getTags();
        } else {
            LOGGER.error(String.format("Current obyavleniye's tags for chat %d not found!", chatId));
            return null;
        }
    }

    public String getTagsOfType(Long chatId, TagType tagType) {
        Optional<CurrentObyavleniye> currentObyavleniyeOptional = currentObyavleniyeRepository.findById(chatId);
        if (currentObyavleniyeOptional.isPresent()) {
            return currentObyavleniyeOptional.get().getTagsOfType(tagType);
        } else {
            LOGGER.error(String.format("Current obyavleniye's tags of type %s for chat %d not found!",
                    tagType.name(), chatId));
            return null;
        }
    }

    public Long getPrice(Long chatId) {
        Optional<CurrentObyavleniye> currentObyavleniyeOptional = currentObyavleniyeRepository.findById(chatId);
        if (currentObyavleniyeOptional.isPresent()) {
            return currentObyavleniyeOptional.get().getPrice();
        } else {
            LOGGER.error(String.format("Current obyavleniye's price for chat %d not found!", chatId));
            return null;
        }
    }

    public String getPhone(Long chatId) {
        Optional<CurrentObyavleniye> currentObyavleniyeOptional = currentObyavleniyeRepository.findById(chatId);
        if (currentObyavleniyeOptional.isPresent()) {
            return currentObyavleniyeOptional.get().getPhone();
        } else {
            LOGGER.error(String.format("Current obyavleniye's phone for chat %d not found!", chatId));
            return null;
        }
    }

    public List<String> getContacts(Long chatId) {
        Optional<CurrentObyavleniye> currentObyavleniyeOptional = currentObyavleniyeRepository.findById(chatId);
        if (currentObyavleniyeOptional.isPresent()) {
            return currentObyavleniyeOptional.get().getContacts();
        } else {
            LOGGER.error(String.format("Current obyavleniye's contacts for chat %d not found!", chatId));
            return null;
        }
    }

    public String getObyavleniyeText(Long chatId) {
        Optional<CurrentObyavleniye> currentObyavleniyeOptional = currentObyavleniyeRepository.findById(chatId);
        if (currentObyavleniyeOptional.isPresent()) {
            return currentObyavleniyeOptional.get().getObyavleniyeText();
        } else {
            LOGGER.error(String.format("Current obyavleniye's text for chat %d not found!", chatId));
            return null;
        }
    }

    public Long getCreationTime(Long chatId) {
        Optional<CurrentObyavleniye> currentObyavleniyeOptional = currentObyavleniyeRepository.findById(chatId);
        if (currentObyavleniyeOptional.isPresent()) {
            return currentObyavleniyeOptional.get().getCreationTime();
        } else {
            LOGGER.error(String.format("Current obyavleniye's creation time for chat %d not found!", chatId));
            return null;
        }
    }

    public Long getNextUpdateTime(Long chatId) {
        Optional<CurrentObyavleniye> currentObyavleniyeOptional = currentObyavleniyeRepository.findById(chatId);
        if (currentObyavleniyeOptional.isPresent()) {
            return currentObyavleniyeOptional.get().getNextUpdateTime();
        } else {
            LOGGER.error(String.format("Current obyavleniye's next update time for chat %d not found!", chatId));
            return null;
        }
    }

    public Integer getUpdateAttempt(Long chatId) {
        Optional<CurrentObyavleniye> currentObyavleniyeOptional = currentObyavleniyeRepository.findById(chatId);
        if (currentObyavleniyeOptional.isPresent()) {
            return currentObyavleniyeOptional.get().getUpdateAttempt();
        } else {
            LOGGER.error(String.format("Current obyavleniye's update attempt for chat %d not found!", chatId));
            return null;
        }
    }

    public CurrentObyavleniye setMessageId(Long chatId, Long messageId) {
        Optional<CurrentObyavleniye> currentObyavleniyeOptional = currentObyavleniyeRepository.findById(chatId);
        if (currentObyavleniyeOptional.isPresent()) {
            CurrentObyavleniye obyavleniye = currentObyavleniyeOptional.get();
            obyavleniye.setMessageId(messageId);
            return currentObyavleniyeRepository.save(obyavleniye);
        } else {
            LOGGER.error(String.format("Cannot set message id for chat %d!", chatId));
            return null;
        }
    }

    public CurrentObyavleniye setDescription(Long chatId, String description) {
        Optional<CurrentObyavleniye> currentObyavleniyeOptional = currentObyavleniyeRepository.findById(chatId);
        if (currentObyavleniyeOptional.isPresent()) {
            CurrentObyavleniye obyavleniye = currentObyavleniyeOptional.get();
            obyavleniye.setDescription(description);
            return currentObyavleniyeRepository.save(obyavleniye);
        } else {
            LOGGER.error(String.format("Cannot set description for chat %d!", chatId));
            return null;
        }
    }

    public CurrentObyavleniye setPrice(Long chatId, Long price) {
        Optional<CurrentObyavleniye> currentObyavleniyeOptional = currentObyavleniyeRepository.findById(chatId);
        if (currentObyavleniyeOptional.isPresent()) {
            CurrentObyavleniye obyavleniye = currentObyavleniyeOptional.get();
            obyavleniye.setPrice(price);
            return currentObyavleniyeRepository.save(obyavleniye);
        } else {
            LOGGER.error(String.format("Cannot set price for chat %d!", chatId));
            return null;
        }
    }

    public CurrentObyavleniye setTags(Long chatId, List<String> tags) {
        Optional<CurrentObyavleniye> currentObyavleniyeOptional = currentObyavleniyeRepository.findById(chatId);
        if (currentObyavleniyeOptional.isPresent()) {
            CurrentObyavleniye obyavleniye = currentObyavleniyeOptional.get();
            obyavleniye.setTags(tags);
            return currentObyavleniyeRepository.save(obyavleniye);
        } else {
            LOGGER.error(String.format("Cannot set price for chat %d!", chatId));
            return null;
        }
    }

    public CurrentObyavleniye setPhone(Long chatId, String phone) {
        Optional<CurrentObyavleniye> currentObyavleniyeOptional = currentObyavleniyeRepository.findById(chatId);
        if (currentObyavleniyeOptional.isPresent()) {
            CurrentObyavleniye obyavleniye = currentObyavleniyeOptional.get();
            obyavleniye.setPhone(phone);
            return currentObyavleniyeRepository.save(obyavleniye);
        } else {
            LOGGER.error(String.format("Cannot set phone for chat %d!", chatId));
            return null;
        }
    }

    public CurrentObyavleniye addSocial(Long chatId, String social) {
        Optional<CurrentObyavleniye> currentObyavleniyeOptional = currentObyavleniyeRepository.findById(chatId);
        if (currentObyavleniyeOptional.isPresent()) {
            CurrentObyavleniye obyavleniye = currentObyavleniyeOptional.get();
            obyavleniye.addSocial(social);
            return currentObyavleniyeRepository.save(obyavleniye);
        } else {
            LOGGER.error(String.format("Cannot add social for chat %d!", chatId));
            return null;
        }
    }

    public CurrentObyavleniye setSocials(Long chatId, List<String> socials) {
        Optional<CurrentObyavleniye> currentObyavleniyeOptional = currentObyavleniyeRepository.findById(chatId);
        if (currentObyavleniyeOptional.isPresent()) {
            CurrentObyavleniye obyavleniye = currentObyavleniyeOptional.get();
            obyavleniye.setSocials(socials);
            return currentObyavleniyeRepository.save(obyavleniye);
        } else {
            LOGGER.error(String.format("Cannot add social for chat %d!", chatId));
            return null;
        }
    }

    public CurrentObyavleniye addPhoto(Long chatId, String photo) {
        Optional<CurrentObyavleniye> currentObyavleniyeOptional = currentObyavleniyeRepository.findById(chatId);
        if (currentObyavleniyeOptional.isPresent()) {
            CurrentObyavleniye obyavleniye = currentObyavleniyeOptional.get();
            obyavleniye.addPhoto(photo);
            return currentObyavleniyeRepository.save(obyavleniye);
        } else {
            LOGGER.error(String.format("Cannot add photo for chat %d!", chatId));
            return null;
        }
    }

    public CurrentObyavleniye setPhotos(Long chatId, List<String> photos) {
        Optional<CurrentObyavleniye> currentObyavleniyeOptional = currentObyavleniyeRepository.findById(chatId);
        if (currentObyavleniyeOptional.isPresent()) {
            CurrentObyavleniye obyavleniye = currentObyavleniyeOptional.get();
            obyavleniye.setPhotos(photos);
            return currentObyavleniyeRepository.save(obyavleniye);
        } else {
            LOGGER.error(String.format("Cannot add photo for chat %d!", chatId));
            return null;
        }
    }

    public CurrentObyavleniye addTags(Long chatId, List<String> tags) {
        Optional<CurrentObyavleniye> currentObyavleniyeOptional = currentObyavleniyeRepository.findById(chatId);
        if (currentObyavleniyeOptional.isPresent()) {
            CurrentObyavleniye obyavleniye = currentObyavleniyeOptional.get();
            obyavleniye.addTags(tags);
            return currentObyavleniyeRepository.save(obyavleniye);
        } else {
            LOGGER.error(String.format("Cannot add tags for chat %d!", chatId));
            return null;
        }
    }

    public CurrentObyavleniye addTag(Long chatId, String tag) {
        Optional<CurrentObyavleniye> currentObyavleniyeOptional = currentObyavleniyeRepository.findById(chatId);
        if (currentObyavleniyeOptional.isPresent()) {
            CurrentObyavleniye obyavleniye = currentObyavleniyeOptional.get();
            obyavleniye.addTag(tag);
            return currentObyavleniyeRepository.save(obyavleniye);
        } else {
            LOGGER.error(String.format("Cannot add tag for chat %d!", chatId));
            return null;
        }
    }

    public CurrentObyavleniye setCreationTime(Long chatId, Long creationTime) {
        Optional<CurrentObyavleniye> currentObyavleniyeOptional = currentObyavleniyeRepository.findById(chatId);
        if (currentObyavleniyeOptional.isPresent()) {
            CurrentObyavleniye obyavleniye = currentObyavleniyeOptional.get();
            obyavleniye.setCreationTime(creationTime);
            return currentObyavleniyeRepository.save(obyavleniye);
        } else {
            LOGGER.error(String.format("Cannot add creation time for chat %d!", chatId));
            return null;
        }
    }

    public CurrentObyavleniye setNextUpdateTime(Long chatId, Long nextUpdateTime) {
        Optional<CurrentObyavleniye> currentObyavleniyeOptional = currentObyavleniyeRepository.findById(chatId);
        if (currentObyavleniyeOptional.isPresent()) {
            CurrentObyavleniye obyavleniye = currentObyavleniyeOptional.get();
            obyavleniye.setNextUpdateTime(nextUpdateTime);
            return currentObyavleniyeRepository.save(obyavleniye);
        } else {
            LOGGER.error(String.format("Cannot add next update time for chat %d!", chatId));
            return null;
        }
    }

    public CurrentObyavleniye setUpdateAttempt(Long chatId, Integer updateAttempt) {
        Optional<CurrentObyavleniye> currentObyavleniyeOptional = currentObyavleniyeRepository.findById(chatId);
        if (currentObyavleniyeOptional.isPresent()) {
            CurrentObyavleniye obyavleniye = currentObyavleniyeOptional.get();
            obyavleniye.setUpdateAttempt(updateAttempt);
            return currentObyavleniyeRepository.save(obyavleniye);
        } else {
            LOGGER.error(String.format("Cannot add tag for chat %d!", chatId));
            return null;
        }
    }

    public void remove(Long chatId) {
        currentObyavleniyeRepository.deleteById(chatId);
    }
}
