package baraholkateam.command;

import baraholkateam.rest.service.CurrentAdvertisementService;
import baraholkateam.util.State;
import baraholkateam.util.TagType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Component
public class NewAdvertisementAddAdvertisementTypes extends Command {
    private static final String CHOSEN_CITY = """
            Текущий выбранный город: %s""";
    private static final String ADD_TYPE_TEXT = """
            Выберите категории, наиболее подходящие для описания вашего товара:""";

    @Autowired
    private CurrentAdvertisementService currentAdvertisementService;

    public NewAdvertisementAddAdvertisementTypes() {
        super(State.NewAdvertisement_AddAdvertisementTypes.getIdentifier(),
                State.NewAdvertisement_AddAdvertisementTypes.getDescription());
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        sendAnswer(absSender, chat.getId(), this.getCommandIdentifier(), user.getUserName(),
                String.format(CHOSEN_CITY, currentAdvertisementService.get(chat.getId()).getTagsOfType(TagType.City)),
                showNextButton());
        sendAnswer(absSender, chat.getId(), this.getCommandIdentifier(), user.getUserName(),
                ADD_TYPE_TEXT, getTags(TagType.AdvertisementType, true));
    }
}
