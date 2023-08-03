package baraholkateam.command;

import baraholkateam.rest.service.ChosenTagsService;
import baraholkateam.rest.service.PreviousStateService;
import baraholkateam.util.State;
import baraholkateam.util.Tag;
import baraholkateam.util.TagType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class SearchObyavleniyesAddObyavleniyeTypes extends Command {
    private static final String CHOOSE_OBYAVLENIYE_TYPE = """
            Выберите тип объявления.
            Вы можете выбрать несколько хэштегов, нажав на них, либо не выбрать ни один.
            Для подтверждения выбора, нажмите на кнопку '%s'.""";

    @Autowired
    private ChosenTagsService chosenTagsService;

    @Autowired
    private PreviousStateService previousStateService;

    public SearchObyavleniyesAddObyavleniyeTypes() {
        super(State.SearchObyavleniyes_AddObyavleniyeTypes.getIdentifier(),
                State.SearchObyavleniyes_AddObyavleniyeTypes.getDescription());
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
        List<Tag> tags = chosenTagsService.get(chat.getId());

        if (previousStateService.get(chat.getId()) == State.SearchObyavleniyes) {
            String hashtags = NO_HASHTAGS;
            if (tags != null && !tags.isEmpty()) {
                hashtags = tags.stream()
                        .map(Tag::getName)
                        .collect(Collectors.joining(" "));
            }
            sendAnswer(absSender, chat.getId(), this.getCommandIdentifier(), user.getUserName(),
                    String.format(CHOSEN_HASHTAGS, hashtags),
                    showNextButton());
            sendAnswer(absSender, chat.getId(), this.getCommandIdentifier(), user.getUserName(),
                    String.format(CHOOSE_OBYAVLENIYE_TYPE, NEXT_BUTTON_TEXT),
                    getTags(TagType.ObyavleniyeType, true));
        } else {
            sendAnswer(absSender, chat.getId(), this.getCommandIdentifier(), user.getUserName(),
                    String.format(INCORRECT_PREVIOUS_STATE, State.MainMenu.getIdentifier()), null);
        }
    }
}
