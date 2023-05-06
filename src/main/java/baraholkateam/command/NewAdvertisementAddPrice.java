package baraholkateam.command;

import baraholkateam.rest.service.CurrentAdvertisementService;
import baraholkateam.util.State;
import baraholkateam.util.TagType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Component
public class NewAdvertisementAddPrice extends Command {
    private static final String CHOSEN_CATEGORIES = """
            Выбраны категории товаров: %s""";
    private static final String ADD_PRICE_TEXT = """
            Укажите стоимость товара в рублях.
            Цена должна состоять только из цифр и ее длина не должна превышать 18 цифр.""";

    @Autowired
    private CurrentAdvertisementService currentAdvertisementService;

    public NewAdvertisementAddPrice() {
        super(State.NewAdvertisement_AddPrice.getIdentifier(), State.NewAdvertisement_AddPrice.getDescription());
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        sendAnswer(
                absSender,
                chat.getId(),
                this.getCommandIdentifier(),
                user.getUserName(),
                String.format(
                        CHOSEN_CATEGORIES,
                        currentAdvertisementService.get(chat.getId()).getTagsOfType(TagType.ProductCategories)
                ),
                null
        );
        sendAnswer(absSender, chat.getId(), this.getCommandIdentifier(), user.getUserName(),
                ADD_PRICE_TEXT, null);
    }
}
