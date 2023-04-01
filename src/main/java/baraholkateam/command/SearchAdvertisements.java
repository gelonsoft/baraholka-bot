package baraholkateam.command;

import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

public class SearchAdvertisements extends Command {
    // TODO  дополнить описание
    private static final String SEARCH_ADVERTISEMENTS = """
            Команда /%s позволяет искать объявления по одному или нескольким хэштегам.
            Пожалуйста, выберите необходимые хэштеги:""";

    public SearchAdvertisements(String commandIdentifier, String description) {
        super(commandIdentifier, description);
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
        sendAnswer(absSender, chat.getId(), this.getCommandIdentifier(), user.getUserName(),
                String.format(SEARCH_ADVERTISEMENTS, this.getCommandIdentifier()));
    }
}
