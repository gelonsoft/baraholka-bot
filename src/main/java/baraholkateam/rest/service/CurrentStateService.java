package baraholkateam.rest.service;

import baraholkateam.rest.model.CurrentState;
import baraholkateam.rest.repository.CurrentStateRepository;
import baraholkateam.util.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Сервис взаимодействия с сущностью "CurrentState".
 */
@Service
public class CurrentStateService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CurrentStateService.class);

    @Autowired
    private CurrentStateRepository currentStateRepository;

    public State get(Long chatId) {
        Optional<CurrentState> currentStateRepositoryOptional = currentStateRepository.findById(chatId);
        if (currentStateRepositoryOptional.isPresent()) {
            return currentStateRepositoryOptional.get().getState();
        } else {
            LOGGER.error(String.format("Current state for chat %d not found!", chatId));
            return null;
        }
    }

    public void put(Long chatId, State state) {
        currentStateRepository.save(new CurrentState(chatId, state));
    }
}
