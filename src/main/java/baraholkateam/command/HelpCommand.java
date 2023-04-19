package baraholkateam.command;

import baraholkateam.util.State;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.List;
import java.util.Map;

public class HelpCommand extends Command {
    private static final String HELP_INFO = """
            Полный список команд:
            %s""";
    private static final List<State> AVAILABLE_STATES = List.of(
            State.MainMenu,
            State.NewAdvertisement,
            State.SearchAdvertisements,
            State.DeleteAdvertisement
    );

    public HelpCommand(Map<Long, Message> lastSentMessage) {
        super(State.Help.getIdentifier(), State.Help.getDescription(), lastSentMessage);
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        sendAnswer(absSender, chat.getId(), this.getCommandIdentifier(), user.getUserName(),
                String.format(HELP_INFO, availableCommands()), null);
    }

    private String availableCommands() {
        StringBuilder result = new StringBuilder();
        for (State state : AVAILABLE_STATES) {
            result
                    .append("/")
                    .append(state.getIdentifier())
                    .append(" - ")
                    .append(state.getDescription())
                    .append("\n");
        }
        return result.toString();
    }
}
