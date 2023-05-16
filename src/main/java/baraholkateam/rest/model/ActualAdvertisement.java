package baraholkateam.rest.model;

import baraholkateam.util.Tag;
import baraholkateam.util.TagType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Опубликованное актуальное объявление.
 */
@Entity
@Table(name = "actual_advertisement")
public class ActualAdvertisement implements Serializable {

    public static final String DESCRIPTION_TEXT = "Описание: ";
    private static final String PRICE_TEXT = "Цена: %s руб.";
    private static final String PHONE_NUMBER = "Номер телефона: <span class=\"tg-spoiler\">%s</span>";
    private static final String CONTACTS = "Контакты: ";
    private static final String CONTACT = "<span class=\"tg-spoiler\">%s</span>";
    private static final Logger LOGGER = LoggerFactory.getLogger(ActualAdvertisement.class);

    @Id
    @Column(name = "message_id")
    @JsonProperty("message_id")
    private Long messageId;

    @Column(name = "owner_chat_id")
    @JsonIgnore
    private Long ownerChatId;

    /**
     * Список фотографий в виде file_id (в Телеграме).
     */
    @Column(name = "photos", length = 1024)
    @JsonIgnore
    private List<String> photoIds = new ArrayList<>();

    /**
     * Список фотографий в виде Base64 строк.
     */
    @Column(insertable = false, updatable = false)
    @JsonProperty("photos")
    private List<String> photos;

    @Column(name = "description", length = 1024)
    @JsonProperty("description")
    private String description;

    @Column(name = "tags", length = 1024)
    @JsonProperty("tags")
    private final List<String> tags = new ArrayList<>();

    @Column(name = "price")
    @JsonProperty("price")
    private Long price;

    @Column(name = "phone", length = 16)
    @JsonProperty("phone")
    private String phone;

    @Column(name = "contacts", length = 256)
    @JsonProperty("contacts")
    private List<String> contacts = new ArrayList<>();

    @Column(name = "creation_time")
    @JsonIgnore
    private Long creationTime;

    @Column(name = "next_update_time")
    @JsonIgnore
    private Long nextUpdateTime;

    @Column(name = "update_attempt")
    @JsonIgnore
    private Integer updateAttempt;

    public ActualAdvertisement() {

    }

    public ActualAdvertisement(Long ownerChatId) {
        this.ownerChatId = ownerChatId;
    }

    public ActualAdvertisement(List<String> photoIds, String description, List<Tag> tags, Long price, String phone,
                               List<String> contacts) {
        this.tags.add(Tag.Actual.getName());
        this.photoIds = photoIds;
        this.description = description;
        this.tags.addAll(tags.stream().map(Tag::getName).toList());
        this.price = price;
        this.phone = phone;
        this.contacts = contacts;
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

    public List<String> getPhotos() {
        return photos;
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

    public ActualAdvertisement addPhotoIds(String photoId) {
        this.photoIds.add(photoId);
        return this;
    }

    public ActualAdvertisement setPhotoIds(List<String> photoIds) {
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

    public ActualAdvertisement setPhotos(List<String> photos) {
        this.photos = photos;
        return this;
    }

    @JsonIgnore
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

        sb.append(tagsString);
        sb.append("\n\n");
        if (price != null) {
            sb.append(String.format(PRICE_TEXT, price)).append("\n\n");
        }
        sb.append(DESCRIPTION_TEXT).append(description);
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
