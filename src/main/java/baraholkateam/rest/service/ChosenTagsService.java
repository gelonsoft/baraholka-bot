package baraholkateam.rest.service;

import baraholkateam.rest.model.ChosenTags;
import baraholkateam.rest.repository.ChosenTagsRepository;
import baraholkateam.util.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Сервис взаимодействия с сущностью "ChosenTags"
 */
@Service
public class ChosenTagsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChosenTagsService.class);

    @Autowired
    private ChosenTagsRepository chosenTagsRepository;

    public List<Tag> get(Long chatId) {
        Optional<ChosenTags> chosenTagsOptional = chosenTagsRepository.findById(chatId);
        return chosenTagsOptional.map(ChosenTags::getChosenTags).orElse(null);
    }

    public void put(Long chatId, List<Tag> tags) {
        chosenTagsRepository.save(new ChosenTags(chatId, tags));
    }

    public void delete(Long chatId) {
        chosenTagsRepository.deleteById(chatId);
    }
}
