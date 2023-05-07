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
import java.util.Objects;

/**
 * Опубликованное актуальное объявление.
 */
@Entity
@Table(name = "actual_advertisement")
public class ActualAdvertisement {

    public static final String DESCRIPTION_TEXT = "Описание:";
    private static final String DESCRIPTION_BODY = """
            %s

            Цена: %s руб.

            %s %s""";
    private static final String PHONE_NUMBER = "Номер телефона: <span class=\"tg-spoiler\">%s</span>";
    private static final String CONTACTS = "Контакты: ";
    private static final String CONTACT = "<span class=\"tg-spoiler\">%s</span>";
    private static final Logger LOGGER = LoggerFactory.getLogger(ActualAdvertisement.class);

    @Id
    @Column(name = "message_id")
    private Long messageId;

    @Column(name = "owner_chat_id")
    private Long ownerChatId;

    @Column(name = "photos", length = 1024)
    private List<String> photoIds = new ArrayList<>();

    @Column(name = "description", length = 1024)
    private String description;

    @Column(name = "tags", length = 1024)
    private final List<String> tags = new ArrayList<>();

    @Column(name = "price")
    private Long price;

    @Column(name = "phone", length = 16)
    private String phone;

    @Column(name = "contacts", length = 256)
    private List<String> contacts = new ArrayList<>();

    @Column(name = "creation_time")
    private Long creationTime;

    @Column(name = "next_update_time")
    private Long nextUpdateTime;

    @Column(name = "update_attempt")
    private Integer updateAttempt;

    public ActualAdvertisement() {

    }

    public ActualAdvertisement(Long ownerChatId) {
        this.ownerChatId = ownerChatId;
    }

    public ActualAdvertisement(List<String> photoIds, String description, List<Tag> tags, Long price,
                               List<String> contacts) {
        this.tags.add(Tag.Actual.getName());
        this.photoIds = photoIds;
        this.description = description;
        this.tags.addAll(tags.stream().map(Tag::getName).toList());
        this.price = price;
        this.contacts = contacts;
        updateAttempt = 0;
    }

    public Long getOwnerChatId() {
        if (ownerChatId == null) {
            LOGGER.warn("Field 'chatId' of the actual advertisement is null!");
        }
        return ownerChatId;
    }

    public Long getMessageId() {
        if (messageId == null) {
            LOGGER.warn("Field 'messageId' of the actual advertisement is null!");
        }
        return messageId;
    }

    public List<String> getPhotoIds() {
        if (photoIds == null || photoIds.isEmpty()) {
            LOGGER.warn("Field 'photos' of the actual advertisement is null!");
        }
        return photoIds;
    }

    public String getDescription() {
        if (description == null) {
            LOGGER.warn("Field 'description' of the actual advertisement is null!");
        }
        return description;
    }

    public List<String> getTags() {
        if (tags.isEmpty()) {
            LOGGER.warn("Field 'tags' of the actual advertisement is null!");
        }
        return tags;
    }

    public String getTagsOfType(TagType tagType) {
        StringBuilder sb = new StringBuilder();
        for (String tag : tags) {
            if (Objects.equals(Objects.requireNonNull(Tag.getTagByName(tag)).getTagType(), tagType)) {
                sb
                        .append(tag)
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
            LOGGER.warn("Field 'creationTime' of the actual advertisement is null!");
        }
        return creationTime;
    }

    public Long getNextUpdateTime() {
        if (nextUpdateTime == null) {
            LOGGER.warn("Field 'nextUpdateTime' of the actual advertisement is null!");
        }
        return nextUpdateTime;
    }

    public Integer getUpdateAttempt() {
        if (updateAttempt == null) {
            LOGGER.warn("Field 'updateAttempt' of the actual advertisement is null!");
        }
        return updateAttempt;
    }

    public ActualAdvertisement setOwnerChatId(Long ownerChatId) {
        this.ownerChatId = ownerChatId;
        return this;
    }

    public ActualAdvertisement setDescription(String description) {
        this.description = description;
        return this;
    }

    public ActualAdvertisement setPrice(Long price) {
        this.price = price;
        return this;
    }

    public ActualAdvertisement setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public ActualAdvertisement addSocial(String social) {
        this.contacts.add(social);
        return this;
    }

    public ActualAdvertisement setSocials(List<String> socials) {
        this.contacts = socials;
        return this;
    }

    public ActualAdvertisement addPhoto(String photoId) {
        this.photoIds.add(photoId);
        return this;
    }

    public ActualAdvertisement setPhotos(List<String> photoIds) {
        this.photoIds = photoIds;
        return this;
    }

    public ActualAdvertisement addTags(List<String> tags) {
        this.tags.addAll(tags);
        return this;
    }

    public ActualAdvertisement addTag(String tag) {
        tags.add(tag);
        return this;
    }

    public ActualAdvertisement setMessageId(Long messageId) {
        this.messageId = messageId;
        return this;
    }

    public ActualAdvertisement setCreationTime(Long creationTime) {
        this.creationTime = creationTime;
        return this;
    }

    public ActualAdvertisement setNextUpdateTime(Long nextUpdateTime) {
        this.nextUpdateTime = nextUpdateTime;
        return this;
    }

    public ActualAdvertisement setUpdateAttempt(Integer updateAttempt) {
        this.updateAttempt = updateAttempt;
        return this;
    }

    public String getAdvertisementText() {
        List<String> tags = this.getTags();
        StringBuilder tagsString = new StringBuilder();
        for (String tag : tags) {
            tagsString.append(tag).append(" ");
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
