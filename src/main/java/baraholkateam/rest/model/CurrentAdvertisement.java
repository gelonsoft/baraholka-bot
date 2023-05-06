package baraholkateam.rest.model;

import baraholkateam.util.Tag;
import baraholkateam.util.TagType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Текущее создаваемое объявление
 */
@Entity
@Table(name = "current_advertisement")
public class CurrentAdvertisement {

    public static final String DESCRIPTION_TEXT = "Описание:";
    private static final String DESCRIPTION_BODY = """
            %s

            Цена: %s руб.

            %s %s""";
    private static final String PHONE_NUMBER = "Номер телефона: <span class=\"tg-spoiler\">%s</span>";
    private static final String CONTACTS = "Контакты: ";
    private static final String CONTACT = "<span class=\"tg-spoiler\">%s</span>";

    private static final Logger LOGGER = LoggerFactory.getLogger(CurrentAdvertisement.class);

    @Id
    @Column(name = "chat_id")
    private Long chatId;

    @Column(name = "message_id")
    private Long messageId;

    @Column(name = "photos", length = 1024)
    private final List<String> photoIds = new ArrayList<>();

    @Column(name = "description", length = 1024)
    private String description;

    @Column(name = "tags", length = 1024)
    private final List<Tag> tags = new ArrayList<>();

    @Column(name = "price")
    private Long price;

    @Column(name = "phone", length = 16)
    private String phone;

    @Column(name = "contacts", length = 256)
    private final List<String> contacts = new ArrayList<>();

    @Column(name = "creation_time")
    private Long creationTime;

    @Column(name = "next_update_time")
    private Long nextUpdateTime;

    @Column(name = "update_attempt")
    private Integer updateAttempt;

    public CurrentAdvertisement() {

    }

    public CurrentAdvertisement(Long chatId) {
        this.tags.add(Tag.Actual);
        this.chatId = chatId;
    }

    public Long getChatId() {
        if (chatId == null) {
            LOGGER.error("Field 'chatId' of the current advertisement is null!");
        }
        return chatId;
    }

    public Long getMessageId() {
        if (messageId == null) {
            LOGGER.error("Field 'messageId' of the current advertisement is null!");
        }
        return messageId;
    }

    public List<String> getPhotoIds() {
        if (photoIds.isEmpty()) {
            LOGGER.error("Field 'photoIds' of the current advertisement is null!");
        }
        return photoIds;
    }

    public String getDescription() {
        if (description == null) {
            LOGGER.error("Field 'description' of the current advertisement is null!");
        }
        return description;
    }

    public List<Tag> getTags() {
        if (tags.isEmpty()) {
            LOGGER.error("Field 'tags' of the current advertisement is null!");
        }
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
            LOGGER.error("Field 'creationTime' of the current advertisement is null!");
        }
        return creationTime;
    }

    public Long getNextUpdateTime() {
        if (nextUpdateTime == null) {
            LOGGER.error("Field 'nextUpdateTime' of the current advertisement is null!");
        }
        return nextUpdateTime;
    }

    public Integer getUpdateAttempt() {
        if (updateAttempt == null) {
            LOGGER.error("Field 'updateAttempt' of the current advertisement is null!");
        }
        return updateAttempt;
    }

    public CurrentAdvertisement setMessageId(Long messageId) {
        this.messageId = messageId;
        return this;
    }

    public CurrentAdvertisement setDescription(String description) {
        this.description = description;
        return this;
    }

    public CurrentAdvertisement setPrice(Long price) {
        this.price = price;
        return this;
    }

    public CurrentAdvertisement setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public CurrentAdvertisement addSocial(String social) {
        this.contacts.add(social);
        return this;
    }

    public CurrentAdvertisement addPhoto(String photoId) {
        this.photoIds.add(photoId);
        return this;
    }

    public CurrentAdvertisement addTags(List<String> tags) {
        for (String tagName : tags) {
            this.tags.add(Tag.getTagByName(tagName));
        }
        return this;
    }

    public CurrentAdvertisement addTag(String tag) {
        tags.add(Tag.getTagByName(tag));
        return this;
    }

    public CurrentAdvertisement setCreationTime(Long creationTime) {
        this.creationTime = creationTime;
        return this;
    }

    public CurrentAdvertisement setNextUpdateTime(Long nextUpdateTime) {
        this.nextUpdateTime = nextUpdateTime;
        return this;
    }

    public CurrentAdvertisement setUpdateAttempt(Integer updateAttempt) {
        this.updateAttempt = updateAttempt;
        return this;
    }

    public String getAdvertisementText() {
        List<Tag> tags = this.getTags();
        StringBuilder tagsString = new StringBuilder();
        for (Tag tag : tags) {
            tagsString.append(tag.getName()).append(" ");
        }
        if (tagsString.length() > 1) {
            tagsString.setLength(tagsString.length() - 1);
        }

        Long price = this.getPrice();
        String description = this.getDescription();

        StringBuilder sb = new StringBuilder();

        sb.append(String.format(DESCRIPTION_BODY, tagsString, price, DESCRIPTION_TEXT, description));
        sb.append("\n");

        String phone = this.getPhone();
        if (phone != null) {
            sb.append("-".repeat(70));
            sb.append("\n").append(String.format(PHONE_NUMBER, phone));
        }

        List<String> contacts = this.getContacts();
        if (contacts.size() > 0) {
            if (phone == null) {
                sb.append("-".repeat(70));
            }

            sb.append("\n").append(CONTACTS);
            StringBuilder contactsString = new StringBuilder();
            for (String contact : contacts) {
                contactsString.append(String.format(CONTACT, contact)).append(",\n");
            }
            if (contactsString.length() > 0) {
                contactsString.setLength(contactsString.length() - 2);
            }
            sb.append(contactsString);
        }
        return sb.toString();
    }
}
