package baraholkateam.rest.service;

import baraholkateam.rest.model.PreviousState;
import baraholkateam.rest.repository.PreviousStateRepository;
import baraholkateam.util.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Сервис взаимодействия с сущностью "PreviousState".
 */
@Service
public class PreviousStateService {
    private static final Logger LOGGER = LoggerFactory.getLogger(PreviousStateService.class);

    @Autowired
    private PreviousStateRepository previousStateRepository;

    public State get(Long chatId) {
        Optional<PreviousState> previousStateRepositoryOptional = previousStateRepository.findById(chatId);
        if (previousStateRepositoryOptional.isPresent()) {
            return previousStateRepositoryOptional.get().getState();
        } else {
            LOGGER.error(String.format("Previous state for chat %d not found!", chatId));
            return null;
        }
    }

    public void put(Long chatId, State state) {
        previousStateRepository.save(new PreviousState(chatId, state));
    }
}
