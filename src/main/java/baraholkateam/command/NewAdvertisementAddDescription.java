package baraholkateam.command;

import baraholkateam.util.State;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Component
public class NewAdvertisementAddDescription extends Command {
    private static final String ADD_DESCRIPTION_TEXT = """
            Введите краткое описание товара (не более 1024 символов):""";

    public NewAdvertisementAddDescription() {
        super(State.NewAdvertisement_AddDescription.getIdentifier(),
                State.NewAdvertisement_AddDescription.getDescription());
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        sendAnswer(absSender, chat.getId(), this.getCommandIdentifier(), user.getUserName(),
                ADD_DESCRIPTION_TEXT, null);
    }
}
