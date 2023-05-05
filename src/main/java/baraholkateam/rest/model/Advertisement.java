package baraholkateam.rest.model;

import baraholkateam.util.Tag;
import baraholkateam.util.TagType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ActualAdvertisement")
public class Advertisement {

    @Id
    @Column(name = "message_id")
    private Long messageId;

    @Column(name = "owner_chat_id")
    private Long ownerChatId;

    @Column(name = "photos")
    private List<PhotoSize> photos = new ArrayList<>();

    @Column(name = "description")
    private String description;

    @Column(name = "tags")
    private final List<Tag> tags = new ArrayList<>();

    @Column(name = "price")
    private Long price;

    @Column(name = "phone")
    private String phone;

    @Column(name = "contacts")
    private List<String> contacts = new ArrayList<>();

    @Column(name = "creation_time")
    private Long creationTime;

    @Column(name = "next_update_time")
    private Long nextUpdateTime;

    @Column(name = "update_attempt")
    private Integer updateAttempt;

    public static final String DESCRIPTION_TEXT = "Описание:";
    private static final String DESCRIPTION_BODY = """
            %s

            Цена: %s руб.

            %s %s""";
    private static final String PHONE_NUMBER = "Номер телефона: <span class=\"tg-spoiler\">%s</span>";
    private static final String CONTACTS = "Контакты: ";
    private static final String CONTACT = "<span class=\"tg-spoiler\">%s</span>";
    private static final Logger LOGGER = LoggerFactory.getLogger(Advertisement.class);

    public Advertisement(Long ownerChatId) {
        this.tags.add(Tag.Actual);
        this.ownerChatId = ownerChatId;
        updateAttempt = 0;
    }

    public Advertisement(List<PhotoSize> photos, String description, List<Tag> tags, Long price,
                         List<String> contacts) {
        this.tags.add(Tag.Actual);
        this.photos = photos;
        this.description = description;
        this.tags.addAll(tags);
        this.price = price;
        this.contacts = contacts;
        updateAttempt = 0;
    }

    public Long getOwnerChatId() {
        if (ownerChatId == null) {
            LOGGER.warn("Field 'chatId' of the advertisement is null!");
        }
        return ownerChatId;
    }

    public Long getMessageId() {
        if (messageId == null) {
            LOGGER.warn("Field 'messageId' of the advertisement is null!");
        }
        return messageId;
    }

    public List<PhotoSize> getPhotos() {
        if (photos == null || photos.isEmpty()) {
            LOGGER.warn("Field 'photos' of the advertisement is null!");
        }
        return photos;
    }

    public String getDescription() {
        if (description == null) {
            LOGGER.warn("Field 'description' of the advertisement is null!");
        }
        return description;
    }

    public List<Tag> getTags() {
        if (tags.isEmpty()) {
            LOGGER.warn("Field 'tags' of the advertisement is null!");
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
            LOGGER.warn("Field 'creationTime' of the advertisement is null!");
        }
        return creationTime;
    }

    public Long getNextUpdateTime() {
        if (nextUpdateTime == null) {
            LOGGER.warn("Field 'nextUpdateTime' of the advertisement is null!");
        }
        return nextUpdateTime;
    }

    public Integer getUpdateAttempt() {
        if (updateAttempt == null) {
            LOGGER.warn("Field 'updateAttempt' of the advertisement is null!");
        }
        return updateAttempt;
    }

    public Advertisement setOwnerChatId(Long ownerChatId) {
        this.ownerChatId = ownerChatId;
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

    public Advertisement setUpdateAttempt(Integer updateAttempt) {
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
