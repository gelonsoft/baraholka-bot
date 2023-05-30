package baraholkateam.command;

import baraholkateam.rest.service.CurrentAdvertisementService;
import baraholkateam.util.State;
import baraholkateam.util.TagType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.Objects;

@Component
public class NewAdvertisementAddCategories extends Command {
    private static final String CHOSEN_ADVERTISEMENT_TYPES = """
            Выбраны категории объявлений: %s""";
    private static final String ADD_CATEGORIES_TEXT = """
            Теперь выберите категории, наиболее подходящие для описания вашего товара.""";

    @Autowired
    private CurrentAdvertisementService currentAdvertisementService;

    public NewAdvertisementAddCategories() {
        super(State.NewAdvertisement_AddCategories.getIdentifier(),
                State.NewAdvertisement_AddCategories.getDescription());
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        String chosenAdvertisementTypeTags = currentAdvertisementService.get(chat.getId())
                .getTagsOfType(TagType.AdvertisementType);
        if (Objects.equals(chosenAdvertisementTypeTags, "")) {
            chosenAdvertisementTypeTags = NO_HASHTAGS;
        }
        sendAnswer(
                absSender,
                chat.getId(),
                this.getCommandIdentifier(),
                user.getUserName(),
                String.format(
                        CHOSEN_ADVERTISEMENT_TYPES,
                        chosenAdvertisementTypeTags
                ),
                showNextButton()
        );
        sendAnswer(absSender, chat.getId(), this.getCommandIdentifier(), user.getUserName(),
                ADD_CATEGORIES_TEXT, getTags(TagType.ProductCategories, true));
    }
}
