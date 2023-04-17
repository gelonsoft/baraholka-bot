package baraholkateam.command;

import baraholkateam.util.State;
import baraholkateam.util.TagType;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.Map;

public class SearchAdvertisements extends Command {
    private static final String SEARCH_ADVERTISEMENTS = """
            Команда /%s позволяет искать объявления по одному или нескольким хэштегам.""";
    private static final String CHOOSE_CITY = """
            Выберите город.
            Вы можете выбрать либо один город, нажав на него, либо не выбрать ни один.
            Если не хотите выбирать ни одного города, то нажмите на кнопку '%s'.""";
    private final Map<Long, String> chosenTags;

    public SearchAdvertisements(Map<Long, Message> lastSentMessage, Map<Long, String> chosenTags) {
        super(State.SearchAdvertisements.getIdentifier(),
                State.SearchAdvertisements.getDescription(), lastSentMessage);
        this.chosenTags = chosenTags;
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
        chosenTags.remove(chat.getId());
        sendAnswer(absSender, chat.getId(), this.getCommandIdentifier(), user.getUserName(),
                String.format(SEARCH_ADVERTISEMENTS, this.getCommandIdentifier()), showNextButton());
        sendAnswer(absSender, chat.getId(), this.getCommandIdentifier(), user.getUserName(),
                String.format(CHOOSE_CITY, NEXT_BUTTON_TEXT), getTags(TagType.City, false));
    }
}
