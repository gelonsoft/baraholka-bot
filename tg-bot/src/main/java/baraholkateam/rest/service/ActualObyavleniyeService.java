package baraholkateam.rest.service;

import baraholkateam.rest.model.ActualObyavleniye;
import baraholkateam.rest.model.CurrentObyavleniye;
import baraholkateam.rest.repository.ActualObyavleniyeRepository;
import baraholkateam.util.TagType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static baraholkateam.bot.BaraholkaBot.SEARCH_OBYAVLENIYES_LIMIT;

/**
 * Сервис взаимодействия с сущностью "ActualObyavleniye".
 */
@Service
public class ActualObyavleniyeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ActualObyavleniyeService.class);

    @Autowired
    private ActualObyavleniyeRepository actualObyavleniyeRepository;

    public ActualObyavleniye get(Long messageId) {
        Optional<ActualObyavleniye> actualObyavleniyeOptional = actualObyavleniyeRepository.findById(messageId);
        if (actualObyavleniyeOptional.isPresent()) {
            return actualObyavleniyeOptional.get();
        } else {
            LOGGER.error(String.format("Actual obyavleniye for message id %d not found!", messageId));
            return null;
        }
    }

    public List<ActualObyavleniye> getByChatId(Long chatId) {
        return actualObyavleniyeRepository.findAllByOwnerChatId(chatId);
    }

    public Long ownerChatId(Long messageId) {
        Optional<ActualObyavleniye> actualObyavleniyeOptional = actualObyavleniyeRepository.findById(messageId);
        if (actualObyavleniyeOptional.isPresent()) {
            return actualObyavleniyeOptional.get().getOwnerChatId();
        } else {
            LOGGER.error(String.format("Actual obyavleniye's owner chat id for message id %d not found!", messageId));
            return null;
        }
    }

    public List<String> getPhotos(Long messageId) {
        Optional<ActualObyavleniye> actualObyavleniyeOptional = actualObyavleniyeRepository.findById(messageId);
        if (actualObyavleniyeOptional.isPresent()) {
            return actualObyavleniyeOptional.get().getPhotos();
        } else {
            LOGGER.error(String.format("Actual obyavleniye's photo ids for message id %d not found!", messageId));
            return null;
        }
    }

    public String getDescription(Long messageId) {
        Optional<ActualObyavleniye> actualObyavleniyeOptional = actualObyavleniyeRepository.findById(messageId);
        if (actualObyavleniyeOptional.isPresent()) {
            return actualObyavleniyeOptional.get().getDescription();
        } else {
            LOGGER.error(String.format("Actual obyavleniye's description for message id %d not found!", messageId));
            return null;
        }
    }

    public List<String> getTags(Long messageId) {
        Optional<ActualObyavleniye> actualObyavleniyeOptional = actualObyavleniyeRepository.findById(messageId);
        if (actualObyavleniyeOptional.isPresent()) {
            return actualObyavleniyeOptional.get().getTags();
        } else {
            LOGGER.error(String.format("Actual obyavleniye's tags for message id %d not found!", messageId));
            return null;
        }
    }

    public String getTagsOfType(Long messageId, TagType tagType) {
        Optional<ActualObyavleniye> actualObyavleniyeOptional = actualObyavleniyeRepository.findById(messageId);
        if (actualObyavleniyeOptional.isPresent()) {
            return actualObyavleniyeOptional.get().getTagsOfType(tagType);
        } else {
            LOGGER.error(String.format("Actual obyavleniye's tags of type %s for message id %d not found!",
                    tagType.name(), messageId));
            return null;
        }
    }

    public Long getPrice(Long messageId) {
        Optional<ActualObyavleniye> actualObyavleniyeOptional = actualObyavleniyeRepository.findById(messageId);
        if (actualObyavleniyeOptional.isPresent()) {
            return actualObyavleniyeOptional.get().getPrice();
        } else {
            LOGGER.error(String.format("Actual obyavleniye's price for message id %d not found!", messageId));
            return null;
        }
    }

    public String getPhone(Long messageId) {
        Optional<ActualObyavleniye> actualObyavleniyeOptional = actualObyavleniyeRepository.findById(messageId);
        if (actualObyavleniyeOptional.isPresent()) {
            return actualObyavleniyeOptional.get().getPhone();
        } else {
            LOGGER.error(String.format("Actual obyavleniye's phone for message id %d not found!", messageId));
            return null;
        }
    }

    public List<String> getContacts(Long messageId) {
        Optional<ActualObyavleniye> actualObyavleniyeOptional = actualObyavleniyeRepository.findById(messageId);
        if (actualObyavleniyeOptional.isPresent()) {
            return actualObyavleniyeOptional.get().getContacts();
        } else {
            LOGGER.error(String.format("Actual obyavleniye's contacts for message id %d not found!", messageId));
            return null;
        }
    }

    public String getObyavleniyeText(Long messageId) {
        Optional<ActualObyavleniye> actualObyavleniyeOptional = actualObyavleniyeRepository.findById(messageId);
        if (actualObyavleniyeOptional.isPresent()) {
            return actualObyavleniyeOptional.get().getObyavleniyeText();
        } else {
            LOGGER.error(String.format("Actual obyavleniye's text for message id %d not found!", messageId));
            return null;
        }
    }

    public Long getCreationTime(Long messageId) {
        Optional<ActualObyavleniye> actualObyavleniyeOptional = actualObyavleniyeRepository.findById(messageId);
        if (actualObyavleniyeOptional.isPresent()) {
            return actualObyavleniyeOptional.get().getCreationTime();
        } else {
            LOGGER.error(String.format("Actual obyavleniye's creation time for message id %d not found!", messageId));
            return null;
        }
    }

    public Long getNextUpdateTime(Long messageId) {
        Optional<ActualObyavleniye> actualObyavleniyeOptional = actualObyavleniyeRepository.findById(messageId);
        if (actualObyavleniyeOptional.isPresent()) {
            return actualObyavleniyeOptional.get().getNextUpdateTime();
        } else {
            LOGGER.error(String.format("Actual obyavleniye's next update time for message id %d not found!",
                    messageId));
            return null;
        }
    }

    public Integer getUpdateAttempt(Long messageId) {
        Optional<ActualObyavleniye> actualObyavleniyeOptional = actualObyavleniyeRepository.findById(messageId);
        if (actualObyavleniyeOptional.isPresent()) {
            return actualObyavleniyeOptional.get().getUpdateAttempt();
        } else {
            LOGGER.error(String.format("Actual obyavleniye's update attempt for message id %d not found!",
                    messageId));
            return null;
        }
    }

    public ActualObyavleniye setOwnerChatId(Long messageId, Long ownerChatId) {
        Optional<ActualObyavleniye> actualObyavleniyeOptional = actualObyavleniyeRepository.findById(messageId);
        if (actualObyavleniyeOptional.isPresent()) {
            ActualObyavleniye obyavleniye = actualObyavleniyeOptional.get();
            obyavleniye.setOwnerChatId(ownerChatId);
            return actualObyavleniyeRepository.save(obyavleniye);
        } else {
            LOGGER.error(String.format("Cannot set owner chat id for message id %d!", messageId));
            return null;
        }
    }

    public ActualObyavleniye setDescription(Long messageId, String description) {
        Optional<ActualObyavleniye> actualObyavleniyeOptional = actualObyavleniyeRepository.findById(messageId);
        if (actualObyavleniyeOptional.isPresent()) {
            ActualObyavleniye obyavleniye = actualObyavleniyeOptional.get();
            obyavleniye.setDescription(description);
            return actualObyavleniyeRepository.save(obyavleniye);
        } else {
            LOGGER.error(String.format("Cannot set description for message id %d!", messageId));
            return null;
        }
    }

    public ActualObyavleniye setPrice(Long messageId, Long price) {
        Optional<ActualObyavleniye> actualObyavleniyeOptional = actualObyavleniyeRepository.findById(messageId);
        if (actualObyavleniyeOptional.isPresent()) {
            ActualObyavleniye obyavleniye = actualObyavleniyeOptional.get();
            obyavleniye.setPrice(price);
            return actualObyavleniyeRepository.save(obyavleniye);
        } else {
            LOGGER.error(String.format("Cannot set price for message id %d!", messageId));
            return null;
        }
    }

    public ActualObyavleniye setPhone(Long messageId, String phone) {
        Optional<ActualObyavleniye> actualObyavleniyeOptional = actualObyavleniyeRepository.findById(messageId);
        if (actualObyavleniyeOptional.isPresent()) {
            ActualObyavleniye obyavleniye = actualObyavleniyeOptional.get();
            obyavleniye.setPhone(phone);
            return actualObyavleniyeRepository.save(obyavleniye);
        } else {
            LOGGER.error(String.format("Cannot set phone for message id %d!", messageId));
            return null;
        }
    }

    public ActualObyavleniye addSocial(Long messageId, String social) {
        Optional<ActualObyavleniye> actualObyavleniyeOptional = actualObyavleniyeRepository.findById(messageId);
        if (actualObyavleniyeOptional.isPresent()) {
            ActualObyavleniye obyavleniye = actualObyavleniyeOptional.get();
            obyavleniye.addSocial(social);
            return actualObyavleniyeRepository.save(obyavleniye);
        } else {
            LOGGER.error(String.format("Cannot add social for message id %d!", messageId));
            return null;
        }
    }

    public ActualObyavleniye setSocials(Long messageId, List<String> socials) {
        Optional<ActualObyavleniye> actualObyavleniyeOptional = actualObyavleniyeRepository.findById(messageId);
        if (actualObyavleniyeOptional.isPresent()) {
            ActualObyavleniye obyavleniye = actualObyavleniyeOptional.get();
            obyavleniye.setSocials(socials);
            return actualObyavleniyeRepository.save(obyavleniye);
        } else {
            LOGGER.error(String.format("Cannot set socials for message id %d!", messageId));
            return null;
        }
    }

    public ActualObyavleniye addPhoto(Long messageId, String photo) {
        Optional<ActualObyavleniye> actualObyavleniyeOptional = actualObyavleniyeRepository.findById(messageId);
        if (actualObyavleniyeOptional.isPresent()) {
            ActualObyavleniye obyavleniye = actualObyavleniyeOptional.get();
            obyavleniye.addPhotos(photo);
            return actualObyavleniyeRepository.save(obyavleniye);
        } else {
            LOGGER.error(String.format("Cannot add photo for message id %d!", messageId));
            return null;
        }
    }

    public ActualObyavleniye setPhotos(Long messageId, List<String> photos) {
        Optional<ActualObyavleniye> actualObyavleniyeOptional = actualObyavleniyeRepository.findById(messageId);
        if (actualObyavleniyeOptional.isPresent()) {
            ActualObyavleniye obyavleniye = actualObyavleniyeOptional.get();
            obyavleniye.setPhotos(photos);
            return actualObyavleniyeRepository.save(obyavleniye);
        } else {
            LOGGER.error(String.format("Cannot add photo for message id %d!", messageId));
            return null;
        }
    }

    public ActualObyavleniye addTags(Long messageId, List<String> tags) {
        Optional<ActualObyavleniye> actualObyavleniyeOptional = actualObyavleniyeRepository.findById(messageId);
        if (actualObyavleniyeOptional.isPresent()) {
            ActualObyavleniye obyavleniye = actualObyavleniyeOptional.get();
            obyavleniye.addTags(tags);
            return actualObyavleniyeRepository.save(obyavleniye);
        } else {
            LOGGER.error(String.format("Cannot add tags for message id %d!", messageId));
            return null;
        }
    }

    public ActualObyavleniye addTag(Long messageId, String tag) {
        Optional<ActualObyavleniye> actualObyavleniyeOptional = actualObyavleniyeRepository.findById(messageId);
        if (actualObyavleniyeOptional.isPresent()) {
            ActualObyavleniye obyavleniye = actualObyavleniyeOptional.get();
            obyavleniye.addTag(tag);
            return actualObyavleniyeRepository.save(obyavleniye);
        } else {
            LOGGER.error(String.format("Cannot add tag for message id %d!", messageId));
            return null;
        }
    }

    public ActualObyavleniye setCreationTime(Long messageId, Long creationTime) {
        Optional<ActualObyavleniye> actualObyavleniyeOptional = actualObyavleniyeRepository.findById(messageId);
        if (actualObyavleniyeOptional.isPresent()) {
            ActualObyavleniye obyavleniye = actualObyavleniyeOptional.get();
            obyavleniye.setCreationTime(creationTime);
            return actualObyavleniyeRepository.save(obyavleniye);
        } else {
            LOGGER.error(String.format("Cannot add creation time for message id %d!", messageId));
            return null;
        }
    }

    public ActualObyavleniye setNextUpdateTime(Long messageId, Long nextUpdateTime) {
        Optional<ActualObyavleniye> actualObyavleniyeOptional = actualObyavleniyeRepository.findById(messageId);
        if (actualObyavleniyeOptional.isPresent()) {
            ActualObyavleniye obyavleniye = actualObyavleniyeOptional.get();
            obyavleniye.setNextUpdateTime(nextUpdateTime);
            return actualObyavleniyeRepository.save(obyavleniye);
        } else {
            LOGGER.error(String.format("Cannot add next update time for message id %d!", messageId));
            return null;
        }
    }

    public ActualObyavleniye setUpdateAttempt(Long messageId, Integer updateAttempt) {
        Optional<ActualObyavleniye> actualObyavleniyeOptional = actualObyavleniyeRepository.findById(messageId);
        if (actualObyavleniyeOptional.isPresent()) {
            ActualObyavleniye obyavleniye = actualObyavleniyeOptional.get();
            obyavleniye.setUpdateAttempt(updateAttempt);
            return actualObyavleniyeRepository.save(obyavleniye);
        } else {
            LOGGER.error(String.format("Cannot add tag for message id %d!", messageId));
            return null;
        }
    }

    public void insertNewObyavleniye(CurrentObyavleniye currentObyavleniye) {
        ActualObyavleniye actualObyavleniye = new ActualObyavleniye(currentObyavleniye.getChatId())
                .setMessageId(currentObyavleniye.getMessageId())
                .setPhotos(currentObyavleniye.getPhotos())
                .setDescription(currentObyavleniye.getDescription())
                .setPrice(currentObyavleniye.getPrice())
                .addTags(currentObyavleniye.getTags())
                .setPhone(currentObyavleniye.getPhone())
                .setSocials(currentObyavleniye.getContacts())
                .setCreationTime(currentObyavleniye.getCreationTime())
                .setNextUpdateTime(currentObyavleniye.getNextUpdateTime())
                .setUpdateAttempt(currentObyavleniye.getUpdateAttempt());
        actualObyavleniyeRepository.save(actualObyavleniye);
    }

    public void removeObyavleniye(Long messageId) {
        actualObyavleniyeRepository.deleteById(messageId);
    }

    public List<ActualObyavleniye> askActualObyavleniyes(Long currentTime) {
        return actualObyavleniyeRepository.findAllByNextUpdateTimeLessThanEqual(currentTime);
    }

    public String adText(Long messageId) {
        Optional<ActualObyavleniye> actualObyavleniyeOptional = actualObyavleniyeRepository.findById(messageId);
        if (actualObyavleniyeOptional.isPresent()) {
            ActualObyavleniye obyavleniye = actualObyavleniyeOptional.get();
            return obyavleniye.getObyavleniyeText();
        } else {
            LOGGER.error(String.format("Cannot get obyavleniye text for message id %d!", messageId));
            return null;
        }
    }

    public List<ActualObyavleniye> tagsSearch(String[] tags) {
        return actualObyavleniyeRepository.findAllByTagsIn(tags, SEARCH_OBYAVLENIYES_LIMIT);
    }
}
