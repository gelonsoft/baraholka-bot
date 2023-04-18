package baraholkateam.command;

import baraholkateam.util.Advertisement;
import baraholkateam.util.State;
import baraholkateam.util.TagType;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.Map;

public class NewAdvertisement_AddAdvertisementTypes extends Command {
    private static final String CHOSEN_CITY = """
            Текущий выбранный город: %s""";
    private static final String ADD_TYPE_TEXT = """
            Выберите категории, наиболее подходящие для описания вашего товара:""";

    private final Map<Long, Advertisement> advertisement;

    public NewAdvertisement_AddAdvertisementTypes(Map<Long, Message> lastSentMessage,
                                                  Map<Long, Advertisement> advertisement) {
        super(State.NewAdvertisement_AddAdvertisementTypes.getIdentifier(),
                State.NewAdvertisement_AddAdvertisementTypes.getDescription(), lastSentMessage);
        this.advertisement = advertisement;
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        sendAnswer(absSender, chat.getId(), this.getCommandIdentifier(), user.getUserName(),
                String.format(CHOSEN_CITY, advertisement.get(chat.getId()).getTagsOfType(TagType.City)),
                showNextButton());
        sendAnswer(absSender, chat.getId(), this.getCommandIdentifier(), user.getUserName(),
                ADD_TYPE_TEXT, getTags(TagType.AdvertisementType, true));
    }
}
