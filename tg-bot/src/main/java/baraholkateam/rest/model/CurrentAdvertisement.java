package baraholkateam.rest.model;

import baraholkateam.util.Tag;
import baraholkateam.util.TagType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Текущее создаваемое объявление.
 */
@Entity
@Table(name = "current_advertisement")
public class CurrentAdvertisement implements Serializable {

    public static final String DESCRIPTION_TEXT = "Описание: ";
    private static final String PRICE_TEXT = "Цена: %s руб.";
    private static final String PHONE_NUMBER = "Номер телефона: <span class=\"tg-spoiler\">%s</span>";
    private static final String CONTACTS = "Контакты: ";
    private static final String CONTACT = "<span class=\"tg-spoiler\">%s</span>";

    private static final Logger LOGGER = LoggerFactory.getLogger(CurrentAdvertisement.class);

    @Id
    @Column(name = "chat_id")
    @JsonProperty("user_id")
    private Long chatId;

    @Column(name = "message_id")
    @JsonIgnore
    private Long messageId;

    /**
     * Список фотографий в виде Base64 строк.
     */
    @Lob
    @Column(name = "photos", length = Integer.MAX_VALUE)
    @JsonProperty("photos")
    private List<String> photos = new ArrayList<>();

    @Column(name = "description", length = 1024)
    @JsonProperty("description")
    private String description;

    @Column(name = "tags", length = 1024)
    @JsonProperty("tags")
    private List<String> tags = new ArrayList<>();

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

    public CurrentAdvertisement() {

    }

    public CurrentAdvertisement(Long chatId) {
        this.tags.add(Tag.Actual.getName());
        this.chatId = chatId;
    }

    public CurrentAdvertisement(Long chatId, String description, List<Tag> tags, Long price,
                                String phone, List<String> contacts) {
        this.tags.add(Tag.Actual.getName());
        this.chatId = chatId;
        this.description = description;
        this.tags.addAll(tags.stream().map(Tag::getName).toList());
        this.price = price;
        this.phone = phone;
        this.contacts = contacts;
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

    public String getDescription() {
        if (description == null) {
            LOGGER.error("Field 'description' of the current advertisement is null!");
        }
        return description;
    }

    public List<String> getTags() {
        if (tags.isEmpty()) {
            LOGGER.error("Field 'tags' of the current advertisement is null!");
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

    public List<String> getPhotos() {
        return photos;
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

    public CurrentAdvertisement setTags(List<String> tags) {
        this.tags = tags;
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

    public CurrentAdvertisement setSocials(List<String> socials) {
        this.contacts = socials;
        return this;
    }

    public CurrentAdvertisement addPhoto(String photo) {
        this.photos.add(photo);
        return this;
    }

    public CurrentAdvertisement setPhotos(List<String> photos) {
        this.photos = photos;
        return this;
    }

    public CurrentAdvertisement addTags(List<String> tags) {
        this.tags.addAll(tags);
        return this;
    }

    public CurrentAdvertisement addTag(String tag) {
        tags.add(tag);
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
        List<String> tags = this.getTags();
        StringBuilder tagsString = new StringBuilder();
        for (String tag : tags) {
            tagsString.append(tag).append(" ");
        }
        if (tagsString.length() > 1) {
            tagsString.setLength(tagsString.length() - 1);
        }

        Long price = this.getPrice();

        StringBuilder sb = new StringBuilder();

        sb.append(tagsString);
        sb.append("\n\n");
        if (price != null) {
            sb.append(String.format(PRICE_TEXT, price)).append("\n\n");
        }
        String description = this.getDescription();
        sb.append(DESCRIPTION_TEXT).append(description);
        sb.append("\n");

        String phone = this.getPhone();
        if (phone != null) {
            sb.append("-".repeat(50));
            sb.append("\n").append(String.format(PHONE_NUMBER, phone));
        }

        List<String> contacts = this.getContacts();
        if (contacts.size() > 0) {
            if (phone == null) {
                sb.append("-".repeat(50));
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
