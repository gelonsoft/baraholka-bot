package baraholkateam.rest.model;

import baraholkateam.util.Tag;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.List;

/**
 * Выбранные пользователем теги во время поиска объявлений по тегам
 */
@Entity
@Table(name = "chosen_tags")
public class ChosenTags {

    @Id
    @Column(name = "chat_id")
    private Long chatId;

    @Column(name = "chosen_tags")
    private List<Tag> chosenTags;

    public ChosenTags() {

    }

    public ChosenTags(Long chatId, List<Tag> tags) {
        this.chatId = chatId;
        this.chosenTags = tags;
    }

    public Long getChatId() {
        return chatId;
    }

    public List<Tag> getChosenTags() {
        return chosenTags;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public void setChosenTags(List<Tag> chosenTags) {
        this.chosenTags = chosenTags;
    }
}
