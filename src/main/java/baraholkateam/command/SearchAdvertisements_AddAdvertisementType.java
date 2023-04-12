package baraholkateam.command;

import baraholkateam.database.SQLExecutor;
import baraholkateam.util.Advertisement;
import baraholkateam.util.State;
import baraholkateam.util.Tag;
import baraholkateam.util.TagType;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.Map;

public class SearchAdvertisements_AddAdvertisementType extends Command {
    private static final String CHOOSE_ADVERTISEMENT_TYPE = """
            Выберите тип объявления.
            Вы можете выбрать несколько хэштегов, нажав на них, либо не выбрать ни один.
            Для подтверждения выбора, нажмите на кнопку '%s'.""";
    private final Map<Long, String> chosenTags;
    private final Map<Long, State> previousState;

    public SearchAdvertisements_AddAdvertisementType(String commandIdentifier, String description,
                                                     Map<Long, Message> lastSentMessage,
                                                     Map<Long, String> chosenTags,
                                                     Map<Long, State> previousState) {
        super(commandIdentifier, description, lastSentMessage);
        this.chosenTags = chosenTags;
        this.previousState = previousState;
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
        String chosenTagsString = chosenTags.get(chat.getId());

        if (previousState.get(chat.getId()) == State.SearchAdvertisements) {
            sendAnswer(absSender, chat.getId(), this.getCommandIdentifier(), user.getUserName(),
                    String.format(CHOSEN_HASHTAGS, chosenTagsString == null ? NO_HASHTAGS : chosenTagsString),
                    showNextButton());
            sendAnswer(absSender, chat.getId(), this.getCommandIdentifier(), user.getUserName(),
                    String.format(CHOOSE_ADVERTISEMENT_TYPE, NEXT_BUTTON_TEXT),
                    getTags(TagType.AdvertisementType, true));
        } else {
            sendAnswer(absSender, chat.getId(), this.getCommandIdentifier(), user.getUserName(),
                    String.format(INCORRECT_PREVIOUS_STATE, State.MainMenu.getIdentifier()), null);
        }
    }
}
