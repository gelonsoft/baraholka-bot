package baraholkateam.command;

import baraholkateam.util.State;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.Map;

public class MainMenuCommand extends Command {
    private static final String MAIN_MENU = """
            Добро пожаловать в главное меню. Здесь Вы можете:
            1. Посмотреть список созданных актуальных объявлений: /%s;
            2. Создать новое объявление: /%s;
            3. Удалить созданное объявление: /%s;
            4. Найти нужные объявления по хэштегам: /%s;
            5. Получить справку по командам бота: /%s.""";

    public MainMenuCommand(Map<Long, Message> lastSentMessage) {
        super(State.MainMenu.getIdentifier(), State.MainMenu.getDescription(), lastSentMessage);
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        sendAnswer(absSender, chat.getId(), this.getCommandIdentifier(), user.getUserName(),
                String.format(MAIN_MENU,
                        State.UserAdvertisements.getIdentifier(),
                        State.NewAdvertisement.getIdentifier(),
                        State.DeleteAdvertisement.getIdentifier(),
                        State.SearchAdvertisements.getIdentifier(),
                        State.Help.getIdentifier()),
                null);
    }
}
