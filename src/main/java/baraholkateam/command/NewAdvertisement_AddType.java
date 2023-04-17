package baraholkateam.command;

import baraholkateam.util.State;
import baraholkateam.util.TagType;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.Map;

public class NewAdvertisement_AddType extends Command {
    private static final String ADD_TYPE_TEXT = """
            Теперь выберите категории, наиболее подходящие для описания вашего товара.""";

    private final Map<Long, String> chosenTags;
    private final Map<Long, State> previousState;

    public NewAdvertisement_AddType(String commandIdentifier, String description,
                                          Map<Long, Message> lastSentMessage, Map<Long, String> chosenTags,
                                          Map<Long, State> previousState) {
        super(commandIdentifier, description, lastSentMessage);
        this.chosenTags = chosenTags;
        this.previousState = previousState;
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        String chosenTagsString = chosenTags.get(chat.getId());
        sendAnswer(absSender, chat.getId(), this.getCommandIdentifier(), user.getUserName(),
                String.format(CHOSEN_HASHTAGS, chosenTagsString == null ? NO_HASHTAGS : chosenTagsString),
                showNextButton());
        sendAnswer(absSender, chat.getId(), this.getCommandIdentifier(), user.getUserName(),
                ADD_TYPE_TEXT,
                getTags(TagType.AdvertisementType, true));
    }
}
