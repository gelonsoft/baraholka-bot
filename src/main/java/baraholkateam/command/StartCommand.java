package baraholkateam.command;

import baraholkateam.util.State;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.Map;

public class StartCommand extends Command {
    private static final String START_ANSWER = """
            Добро пожаловать в бот по созданию и размещению объявлений!
            Вы можете начать создавать новое объявление при помощи команды /%s.
            Кроме того, Вы можете найти интересующие Вас объявления по хэштегам при помощи команды %s.
            Также Вы можете перейти в главное меню /%s и следовать дальнейшим инструкциям.
            Справочная информация по функциям бота представлена по команде /%s.""";

    public StartCommand(Map<Long, Message> lastSentMessage) {
        super(State.Start.getIdentifier(), State.Start.getDescription(), lastSentMessage);
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
        sendAnswer(absSender, chat.getId(), this.getCommandIdentifier(), user.getUserName(),
                String.format(START_ANSWER,
                        State.NewAdvertisement.getIdentifier(),
                        State.SearchAdvertisements.getIdentifier(),
                        State.MainMenu.getIdentifier(),
                        State.Help.getIdentifier()), null);
    }
}
