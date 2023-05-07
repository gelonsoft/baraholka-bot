package baraholkateam.rest.service;

import baraholkateam.rest.model.LastSentMessage;
import baraholkateam.rest.repository.LastSentMessageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.Optional;

/**
 * Сервис взаимодействия с сущностью "LastSentMessage".
 */
@Service
public class LastSentMessageService {

    private static final Logger LOGGER = LoggerFactory.getLogger(LastSentMessageService.class);

    @Autowired
    private LastSentMessageRepository lastSentMessageRepository;

    public Message get(Long chatId) {
        Optional<LastSentMessage> lastSentMessageOptional = lastSentMessageRepository.findById(chatId);
        if (lastSentMessageOptional.isPresent()) {
            return lastSentMessageOptional.get().getMessage();
        } else {
            LOGGER.error(String.format("Last sent message for chat %d not found!", chatId));
            return null;
        }
    }

    public void put(Long chatId, Message message) {
        lastSentMessageRepository.save(new LastSentMessage(chatId, message));
    }

}
