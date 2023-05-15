package baraholkateam.command;

import baraholkateam.rest.service.CurrentAdvertisementService;
import baraholkateam.util.State;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.ArrayList;
import java.util.List;

@Component
public class NewAdvertisementConfirmPrice extends Command {
    private static final String CONFIRM_PRICE_TEXT = """
            Цена вашего товара: %s руб.
            Теперь необходимо добавить контакты для связи с вами.""";

    @Autowired
    private CurrentAdvertisementService currentAdvertisementService;

    public NewAdvertisementConfirmPrice() {
        super(State.NewAdvertisement_ConfirmPrice.getIdentifier(),
                State.NewAdvertisement_ConfirmPrice.getDescription());
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        sendAnswer(absSender, chat.getId(), this.getCommandIdentifier(), user.getUserName(),
                String.format(CONFIRM_PRICE_TEXT, currentAdvertisementService.get(chat.getId()).getPrice()),
                getAddReplyKeyboard());
    }

    private ReplyKeyboardMarkup getAddReplyKeyboard() {
        return getReplyKeyboard(List.of(State.NewAdvertisement_AddContacts.getDescription()));
    }
}
