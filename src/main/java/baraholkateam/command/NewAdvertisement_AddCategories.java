package baraholkateam.command;

import baraholkateam.rest.model.Advertisement;
import baraholkateam.util.State;
import baraholkateam.util.TagType;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.Map;

public class NewAdvertisement_AddCategories extends Command {
    private static final String CHOSEN_ADVERTISEMENT_TYPES = """
            Выбраны категории объявлений: %s""";
    private static final String ADD_CATEGORIES_TEXT = """
            Теперь выберите категории, наиболее подходящие для описания вашего товара.""";
    private final Map<Long, Advertisement> advertisement;

    public NewAdvertisement_AddCategories(Map<Long, Message> lastSentMessage, Map<Long, Advertisement> advertisement) {
        super(State.NewAdvertisement_AddCategories.getIdentifier(),
                State.NewAdvertisement_AddCategories.getDescription());
        this.advertisement = advertisement;
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        sendAnswer(absSender, chat.getId(), this.getCommandIdentifier(), user.getUserName(),
                String.format(CHOSEN_ADVERTISEMENT_TYPES,
                        advertisement.get(chat.getId()).getTagsOfType(TagType.AdvertisementType)), showNextButton());
        sendAnswer(absSender, chat.getId(), this.getCommandIdentifier(), user.getUserName(),
                ADD_CATEGORIES_TEXT, getTags(TagType.ProductCategories, true));
    }
}
