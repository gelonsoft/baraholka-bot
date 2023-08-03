package baraholkateam.command;

import baraholkateam.rest.model.ActualObyavleniye;
import baraholkateam.rest.service.ActualObyavleniyeService;
import baraholkateam.rest.service.ChosenTagsService;
import baraholkateam.rest.service.PreviousStateService;
import baraholkateam.telegram_api_requests.TelegramAPIRequests;
import baraholkateam.util.State;
import baraholkateam.util.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static baraholkateam.bot.BaraholkaBot.SEARCH_OBYAVLENIYES_LIMIT;

@Component
public class SearchObyavleniyesShowFoundObyavleniyes extends Command {
    private static final String CANNOT_FIND_OBYAVLENIYES = """
            По Вашему запросу ничего не нашлось.
            Вы можете вернуться в главное меню /%s или найти объявления по другим хэштегам /%s.""";
    private static final String FOUND_OBYAVLENIYES = """
            По Вашему запросу нашлось объявлений: %d.
            Показывается не более %s самых актуальных объявлений.
            Вы можете вернуться в главное меню /%s или найти объявления по другим хэштегам /%s.""";

    @Autowired
    private ChosenTagsService chosenTagsService;

    @Autowired
    private TelegramAPIRequests telegramAPIRequests;

    @Autowired
    private ActualObyavleniyeService actualObyavleniyeService;

    @Autowired
    private PreviousStateService previousStateService;

    @Value("${channel.username}")
    private String channelUsername;

    public SearchObyavleniyesShowFoundObyavleniyes() {
        super(State.SearchObyavleniyes_ShowFoundObyavleniyes.getIdentifier(),
                State.SearchObyavleniyes_ShowFoundObyavleniyes.getDescription());
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
        List<Tag> tags = chosenTagsService.get(chat.getId());

        if (previousStateService.get(chat.getId()) == State.SearchObyavleniyes_AddProductCategories) {
            String hashtags = NO_HASHTAGS;
            if (tags != null && !tags.isEmpty()) {
                hashtags = tags.stream()
                        .map(Tag::getName)
                        .collect(Collectors.joining(" "));
            }
            sendAnswer(absSender, chat.getId(), this.getCommandIdentifier(), user.getUserName(),
                    String.format(CHOSEN_HASHTAGS, hashtags), null);

            int count = forwardMessages(chat.getId());

            if (count == 0) {
                sendAnswer(absSender, chat.getId(), this.getCommandIdentifier(), user.getUserName(),
                        String.format(CANNOT_FIND_OBYAVLENIYES,
                                State.MainMenu.getIdentifier(),
                                State.SearchObyavleniyes.getIdentifier()),
                        showCommandButtons(List.of(State.MainMenu.getDescription(),
                                State.SearchObyavleniyes.getDescription())));
            } else {
                sendAnswer(absSender, chat.getId(), this.getCommandIdentifier(), user.getUserName(),
                        String.format(FOUND_OBYAVLENIYES,
                                count,
                                SEARCH_OBYAVLENIYES_LIMIT,
                                State.MainMenu.getIdentifier(),
                                State.SearchObyavleniyes.getIdentifier()),
                        showCommandButtons(List.of(State.MainMenu.getDescription(),
                                State.SearchObyavleniyes.getDescription())));
            }
        } else {
            sendAnswer(absSender, chat.getId(), this.getCommandIdentifier(), user.getUserName(),
                    String.format(INCORRECT_PREVIOUS_STATE, State.MainMenu.getIdentifier()), null);
        }
    }

    private int forwardMessages(Long chatId) {
        List<Tag> tags = chosenTagsService.get(chatId);
        if (tags == null || tags.isEmpty()) {
            return 0;
        }
        List<ActualObyavleniye> sortedAds = actualObyavleniyeService.tagsSearch(tags.stream()
                .map(Tag::getName)
                .toArray(String[]::new));
        int count = 0;
        for (ActualObyavleniye sortedAd : sortedAds) {
            telegramAPIRequests.forwardMessage(channelUsername, String.valueOf(chatId),
                    sortedAd.getMessageId());
            count++;
        }
        return count;
    }

    private ReplyKeyboardMarkup showCommandButtons(List<String> commands) {
        ReplyKeyboardMarkup rkm = new ReplyKeyboardMarkup();
        rkm.setSelective(true);
        rkm.setResizeKeyboard(true);
        rkm.setOneTimeKeyboard(true);
        List<KeyboardRow> commandButtons = new ArrayList<>(commands.size());
        for (String command : commands) {
            KeyboardRow commandButton = new KeyboardRow();
            commandButton.add(new KeyboardButton(command));
            commandButtons.add(commandButton);
        }
        rkm.setKeyboard(commandButtons);
        return rkm;
    }
}
