package baraholkateam.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;

import java.util.List;

public class Advertisement {
    private Long chatId;
    private Long messageId;
    private final List<PhotoSize> photos;
    private final String description;
    private final List<Tag> tags;
    private final Long price;
    private final List<String> contacts;
    private Long creationTime;
    private Long nextUpdateTime;
    private final Logger logger = LoggerFactory.getLogger(Advertisement.class);

    public Advertisement(List<PhotoSize> photos, String description, List<Tag> tags, Long price,
                         List<String> contacts) {
        this.photos = photos;
        this.description = description;
        this.tags = tags;
        this.price = price;
        this.contacts = contacts;
    }

    public Long getChatId() {
        if (chatId == null) {
            logger.warn("Field 'chatId' of the advertisement is null!");
        }
        return chatId;
    }

    public Long getMessageId() {
        if (messageId == null) {
            logger.warn("Field 'messageId' of the advertisement is null!");
        }
        return messageId;
    }

    public List<PhotoSize> getPhotos() {
        return photos;
    }

    public String getDescription() {
        return description;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public Long getPrice() {
        return price;
    }

    public List<String> getContacts() {
        return contacts;
    }

    public Long getCreationTime() {
        if (creationTime == null) {
            logger.warn("Field 'creationTime' of the advertisement is null!");
        }
        return creationTime;
    }

    public Long getNextUpdateTime() {
        if (nextUpdateTime == null) {
            logger.warn("Field 'nextUpdateTime' of the advertisement is null!");
        }
        return nextUpdateTime;
    }

    public Advertisement setChatId(Long chatId) {
        this.chatId = chatId;
        return this;
    }

    public Advertisement setMessageId(Long messageId) {
        this.messageId = messageId;
        return this;
    }

    public Advertisement setCreationTime(Long creationTime) {
        this.creationTime = creationTime;
        return this;
    }

    public Advertisement setNextUpdateTime(Long nextUpdateTime) {
        this.nextUpdateTime = nextUpdateTime;
        return this;
    }
}
