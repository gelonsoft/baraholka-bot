package baraholkateam.command;

import baraholkateam.rest.service.CurrentObyavleniyeService;
import baraholkateam.util.State;
import baraholkateam.util.TagType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.Objects;

@Component
public class NewObyavleniyeAddCategories extends Command {
    private static final String CHOSEN_OBYAVLENIYE_TYPES = """
            Выбраны категории объявлений: %s""";
    private static final String ADD_CATEGORIES_TEXT = """
            Теперь выберите категории, наиболее подходящие для описания вашего товара.""";

    @Autowired
    private CurrentObyavleniyeService currentObyavleniyeService;

    public NewObyavleniyeAddCategories() {
        super(State.NewObyavleniye_AddCategories.getIdentifier(),
                State.NewObyavleniye_AddCategories.getDescription());
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        String chosenObyavleniyeTypeTags = currentObyavleniyeService.get(chat.getId())
                .getTagsOfType(TagType.ObyavleniyeType);
        if (Objects.equals(chosenObyavleniyeTypeTags, "")) {
            chosenObyavleniyeTypeTags = NO_HASHTAGS;
        }
        sendAnswer(
                absSender,
                chat.getId(),
                this.getCommandIdentifier(),
                user.getUserName(),
                String.format(
                        CHOSEN_OBYAVLENIYE_TYPES,
                        chosenObyavleniyeTypeTags
                ),
                showNextButton()
        );
        sendAnswer(absSender, chat.getId(), this.getCommandIdentifier(), user.getUserName(),
                ADD_CATEGORIES_TEXT, getTags(TagType.ProductCategories, true));
    }
}
