package baraholkateam.command;

import baraholkateam.rest.service.ChosenTagsService;
import baraholkateam.util.State;
import baraholkateam.util.TagType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Component
public class SearchObyavleniyes extends Command {
    private static final String SEARCH_OBYAVLENIYES = """
            Команда /%s позволяет искать объявления по одному или нескольким хэштегам.""";
    private static final String CHOOSE_CITY = """
            Выберите город.
            Вы можете выбрать либо один город, нажав на него, либо не выбрать ни один.
            Если не хотите выбирать ни одного города, то нажмите на кнопку '%s'.""";

    @Autowired
    private ChosenTagsService chosenTagsService;

    public SearchObyavleniyes() {
        super(State.SearchObyavleniyes.getIdentifier(), State.SearchObyavleniyes.getDescription());
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
        chosenTagsService.delete(chat.getId());
        sendAnswer(absSender, chat.getId(), this.getCommandIdentifier(), user.getUserName(),
                String.format(SEARCH_OBYAVLENIYES, this.getCommandIdentifier()), showNextButton());
        sendAnswer(absSender, chat.getId(), this.getCommandIdentifier(), user.getUserName(),
                String.format(CHOOSE_CITY, NEXT_BUTTON_TEXT), getTags(TagType.City, false));
    }
}
