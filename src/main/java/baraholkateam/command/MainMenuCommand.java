package baraholkateam.command;

import baraholkateam.util.State;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

public class MainMenuCommand extends Command {
    // TODO добавить новые возможности
    private static final String MAIN_MENU = """
            Добро пожаловать в главное меню. Здесь Вы можете:
            1. Создать новое объявление: /%s.
            """;

    public MainMenuCommand(String commandIdentifier, String description) {
        super(commandIdentifier, description);
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        sendAnswer(absSender, chat.getId(), this.getCommandIdentifier(), user.getUserName(),
                String.format(MAIN_MENU, State.NewAdvertisement.getIdentifier()));
    }
}
