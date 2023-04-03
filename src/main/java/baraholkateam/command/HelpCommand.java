package baraholkateam.command;

import baraholkateam.util.State;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class HelpCommand extends Command {
    private static final String HELP_INFO = """
            Полный список команд:
            %s""";
    private final Collection<IBotCommand> commands;
    private static final List<String> HIDDEN_COMMANDS = List.of(
            State.Start.getIdentifier(),
            State.Help.getIdentifier(),
            State.SearchAdvertisements_AddAdvertisementType.getIdentifier(),
            State.SearchAdvertisements_AddProductCategories.getIdentifier(),
            State.SearchAdvertisements_ShowFoundAdvertisements.getIdentifier()
    );

    public HelpCommand(String commandIdentifier, String description, Map<Long, Message> lastSentMessage,
                       Collection<IBotCommand> commands) {
        super(commandIdentifier, description, lastSentMessage);
        this.commands = commands;
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        sendAnswer(absSender, chat.getId(), this.getCommandIdentifier(), user.getUserName(),
                String.format(HELP_INFO, allCommands()), null);
    }

    private String allCommands() {
        StringBuilder result = new StringBuilder();
        for (IBotCommand command : commands) {
            if (!HIDDEN_COMMANDS.contains(command.getCommandIdentifier())) {
                result
                        .append("/")
                        .append(command.getCommandIdentifier())
                        .append(" - ")
                        .append(command.getDescription())
                        .append("\n");
            }
        }
        return result.toString();
    }
}
