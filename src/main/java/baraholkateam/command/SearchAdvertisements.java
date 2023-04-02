package baraholkateam.command;

import baraholkateam.util.TagType;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.Map;

import static baraholkateam.command.NonCommand.getTags;

public class SearchAdvertisements extends Command {
    // TODO  дополнить описание
    private static final String SEARCH_ADVERTISEMENTS = """
            Команда /%s позволяет искать объявления по одному или нескольким хэштегам.""";
    private static final String CHOOSE_CITY = """
            Выберите город:""";
    private final Map<Long, String> chosenTags;

    public SearchAdvertisements(String commandIdentifier, String description, Map<Long, Message> lastSentMessage,
                                Map<Long, String> chosenTags) {
        super(commandIdentifier, description, lastSentMessage);
        this.chosenTags = chosenTags;
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
        chosenTags.remove(chat.getId());
        sendAnswer(absSender, chat.getId(), this.getCommandIdentifier(), user.getUserName(),
                String.format(SEARCH_ADVERTISEMENTS, this.getCommandIdentifier()), null);
        sendAnswer(absSender, chat.getId(), this.getCommandIdentifier(), user.getUserName(),
                CHOOSE_CITY, getTags(TagType.City, false));
    }
}
