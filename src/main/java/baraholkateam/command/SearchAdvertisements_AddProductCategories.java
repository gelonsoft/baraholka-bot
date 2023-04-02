package baraholkateam.command;

import baraholkateam.util.TagType;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.Map;

public class SearchAdvertisements_AddProductCategories extends Command {
    private static final String CHOOSE_PRODUCT_CATEGORY = """
            Выберите категории товаров.
            Вы можете выбрать несколько хэштегов, нажав на них, либо не выбрать ни один.
            Для подтверждения выбора, нажмите на кнопку '%s'.""";
    private final Map<Long, String> chosenTags;

    public SearchAdvertisements_AddProductCategories(String commandIdentifier, String description,
                                                     Map<Long, Message> lastSentMessage, Map<Long, String> chosenTags) {
        super(commandIdentifier, description, lastSentMessage);
        this.chosenTags = chosenTags;
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
        String chosenTagsString = chosenTags.get(chat.getId());

        sendAnswer(absSender, chat.getId(), this.getCommandIdentifier(), user.getUserName(),
                String.format(CHOSEN_HASHTAGS, chosenTagsString == null ? NO_HASHTAGS : chosenTagsString),
                showNextButton());
        sendAnswer(absSender, chat.getId(), this.getCommandIdentifier(), user.getUserName(),
                String.format(CHOOSE_PRODUCT_CATEGORY, NEXT_BUTTON_TEXT),
                getTags(TagType.ProductCategories, true));
    }
}
