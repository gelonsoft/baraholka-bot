package baraholkateam.rest.model;

import baraholkateam.util.State;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Текущее состояние бота.
 */
@Entity
@Table(name = "current_state")
public class CurrentState {
    @Id
    @Column(name = "chat_id")
    private Long chatId;

    @Column(name = "state", length = 64)
    private String state;

    public CurrentState() {

    }

    public CurrentState(Long chatId, State state) {
        this.chatId = chatId;
        this.state = state.getIdentifier();
    }

    public Long getChatId() {
        return chatId;
    }

    public State getState() {
        return State.findState(state);
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public void setState(State state) {
        this.state = state.getIdentifier();
    }

}
