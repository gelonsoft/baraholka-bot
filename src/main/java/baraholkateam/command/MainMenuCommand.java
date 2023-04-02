package baraholkateam.command;

import baraholkateam.util.State;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.Map;

public class MainMenuCommand extends Command {
    // TODO добавить новые возможности
    private static final String MAIN_MENU = """
            Добро пожаловать в главное меню. Здесь Вы можете:
            1. Создать новое объявление: /%s;
            2. Удалить созданное объявление: /!!!добавить команду!!!;
            3. Найти нужные объявления по хэштегам: /%s.""";

    public MainMenuCommand(String commandIdentifier, String description, Map<Long, Message> lastSentMessage) {
        super(commandIdentifier, description, lastSentMessage);
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        sendAnswer(absSender, chat.getId(), this.getCommandIdentifier(), user.getUserName(),
                String.format(MAIN_MENU, State.NewAdvertisement.getIdentifier(),
                        State.SearchAdvertisements.getIdentifier()), null);
    }
}
