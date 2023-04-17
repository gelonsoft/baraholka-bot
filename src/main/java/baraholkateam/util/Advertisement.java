package baraholkateam.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;

import java.util.ArrayList;
import java.util.List;

public class Advertisement {
    private Long chatId;
    private Long messageId;
    private List<PhotoSize> photos = new ArrayList<>();
    private String description = null;
    private final List<Tag> tags = new ArrayList<>();
    private Long price = null;
    private String phone = null;
    private List<String> contacts = new ArrayList<>();
    private Long creationTime;
    private Long nextUpdateTime;
    private final Logger logger = LoggerFactory.getLogger(Advertisement.class);

    public Advertisement(Long chatId) {
        this.tags.add(Tag.Actual);
        this.chatId = chatId;
    }

    public Advertisement(List<PhotoSize> photos, String description, List<Tag> tags, Long price,
                         List<String> contacts) {
        this.tags.add(Tag.Actual);
        this.photos = photos;
        this.description = description;
        this.tags.addAll(tags);
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

    public String getTagsOfType(TagType tagType) {
        StringBuilder sb = new StringBuilder();
        for (Tag tag : tags) {
            if (tag.getTagType() == tagType) {
                sb
                        .append(tag.getName())
                        .append(" ");
            }
        }
        if (sb.length() > 1) {
            sb.setLength(sb.length() - 1);
        }
        return sb.toString();
    }

    public Long getPrice() {
        return price;
    }

    public String getPhone() {
        return phone;
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

    public Advertisement setDescription(String description) {
        this.description = description;
        return this;
    }

    public Advertisement setPrice(Long price) {
        this.price = price;
        return this;
    }

    public Advertisement setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public Advertisement addSocial(String social) {
        this.contacts.add(social);
        return this;
    }

    public Advertisement addPhoto(PhotoSize photo) {
        this.photos.add(photo);
        return this;
    }

    public Advertisement addTags(List<String> tags) {
        for (String tagName : tags) {
            this.tags.add(Tag.getTagByName(tagName));
        }
        return this;
    }

    public Advertisement addTag(String tag) {
        tags.add(Tag.getTagByName(tag));
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
