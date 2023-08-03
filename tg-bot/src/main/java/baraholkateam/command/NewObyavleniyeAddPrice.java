package baraholkateam.command;

import baraholkateam.rest.service.CurrentObyavleniyeService;
import baraholkateam.util.State;
import baraholkateam.util.TagType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.Collections;
import java.util.Objects;

@Component
public class NewObyavleniyeAddPrice extends Command {
    private static final String CHOSEN_CATEGORIES = """
            Выбраны категории товаров: %s""";
    private static final String ADD_PRICE_TEXT = """
            Укажите стоимость товара в рублях.
            Цена должна состоять только из цифр и ее длина не должна превышать 18 цифр.""";

    @Autowired
    private CurrentObyavleniyeService currentObyavleniyeService;

    public NewObyavleniyeAddPrice() {
        super(State.NewObyavleniye_AddPrice.getIdentifier(), State.NewObyavleniye_AddPrice.getDescription());
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        String chosenCategoriesTags = currentObyavleniyeService.get(chat.getId())
                .getTagsOfType(TagType.ProductCategories);
        if (Objects.equals(chosenCategoriesTags, "")) {
            chosenCategoriesTags = NO_HASHTAGS;
        }
        sendAnswer(
                absSender,
                chat.getId(),
                this.getCommandIdentifier(),
                user.getUserName(),
                String.format(
                        CHOSEN_CATEGORIES,
                        chosenCategoriesTags
                ),
                null
        );
        sendAnswer(absSender, chat.getId(), this.getCommandIdentifier(), user.getUserName(),
                ADD_PRICE_TEXT, getReplyKeyboard(Collections.emptyList(), true));
    }
}
