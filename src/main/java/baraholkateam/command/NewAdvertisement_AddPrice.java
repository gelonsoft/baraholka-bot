package baraholkateam.command;

import baraholkateam.rest.model.Advertisement;
import baraholkateam.util.State;
import baraholkateam.util.TagType;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.Map;

public class NewAdvertisement_AddPrice extends Command {
    private static final String CHOSEN_CATEGORIES = """
            Выбраны категории товаров: %s""";
    private static final String ADD_PRICE_TEXT = """
            Укажите стоимость товара в рублях.
            Цена должна состоять только из цифр и ее длина не должна превышать 18 цифр.""";
    private final Map<Long, Advertisement> advertisement;

    public NewAdvertisement_AddPrice(Map<Long, Message> lastSentMessage, Map<Long, Advertisement> advertisement) {
        super(State.NewAdvertisement_AddPrice.getIdentifier(),
                State.NewAdvertisement_AddPrice.getDescription());
        this.advertisement = advertisement;
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        sendAnswer(absSender, chat.getId(), this.getCommandIdentifier(), user.getUserName(),
                String.format(CHOSEN_CATEGORIES,
                        advertisement.get(chat.getId()).getTagsOfType(TagType.ProductCategories)), null);
        sendAnswer(absSender, chat.getId(), this.getCommandIdentifier(), user.getUserName(),
                ADD_PRICE_TEXT, null);
    }
}
