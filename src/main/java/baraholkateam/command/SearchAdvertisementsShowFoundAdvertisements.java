package baraholkateam.command;

import baraholkateam.bot.BaraholkaBotProperties;
import baraholkateam.rest.model.ActualAdvertisement;
import baraholkateam.rest.service.ActualAdvertisementService;
import baraholkateam.rest.service.ChosenTagsService;
import baraholkateam.rest.service.PreviousStateService;
import baraholkateam.telegram_api_requests.TelegramAPIRequests;
import baraholkateam.util.State;
import baraholkateam.util.Tag;
import org.springframework.beans.factory.annotation.Autowired;
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

import static baraholkateam.bot.BaraholkaBot.SEARCH_ADVERTISEMENTS_LIMIT;

@Component
public class SearchAdvertisementsShowFoundAdvertisements extends Command {
    private static final String CANNOT_FIND_ADVERTISEMENTS = """
            По Вашему запросу ничего не нашлось.
            Вы можете вернуться в главное меню /%s или найти объявления по другим хэштегам /%s.""";
    private static final String FOUND_ADVERTISEMENTS = """
            По Вашему запросу нашлось объявлений: %d.
            Показывается не более %s самых актуальных объявлений.
            Вы можете вернуться в главное меню /%s или найти объявления по другим хэштегам /%s.""";

    @Autowired
    private ChosenTagsService chosenTagsService;

    @Autowired
    private TelegramAPIRequests telegramAPIRequests;

    @Autowired
    private ActualAdvertisementService actualAdvertisementService;

    @Autowired
    private PreviousStateService previousStateService;

    public SearchAdvertisementsShowFoundAdvertisements() {
        super(State.SearchAdvertisements_ShowFoundAdvertisements.getIdentifier(),
                State.SearchAdvertisements_ShowFoundAdvertisements.getDescription());
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
        List<Tag> tags = chosenTagsService.get(chat.getId());

        if (previousStateService.get(chat.getId()) == State.SearchAdvertisements_AddProductCategories) {
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
                        String.format(CANNOT_FIND_ADVERTISEMENTS,
                                State.MainMenu.getIdentifier(),
                                State.SearchAdvertisements.getIdentifier()),
                        showCommandButtons(List.of(State.MainMenu.getDescription(),
                                State.SearchAdvertisements.getDescription())));
            } else {
                sendAnswer(absSender, chat.getId(), this.getCommandIdentifier(), user.getUserName(),
                        String.format(FOUND_ADVERTISEMENTS,
                                count,
                                SEARCH_ADVERTISEMENTS_LIMIT,
                                State.MainMenu.getIdentifier(),
                                State.SearchAdvertisements.getIdentifier()),
                        showCommandButtons(List.of(State.MainMenu.getDescription(),
                                State.SearchAdvertisements.getDescription())));
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
        List<ActualAdvertisement> sortedAds = actualAdvertisementService.tagsSearch(tags.stream()
                .map(Tag::getName)
                .toArray(String[]::new));
        int count = 0;
        for (ActualAdvertisement sortedAd : sortedAds) {
            telegramAPIRequests.forwardMessage(BaraholkaBotProperties.CHANNEL_USERNAME, String.valueOf(chatId),
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
