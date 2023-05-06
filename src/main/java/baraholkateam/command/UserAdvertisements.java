package baraholkateam.command;

import baraholkateam.bot.BaraholkaBotProperties;
import baraholkateam.rest.model.ActualAdvertisement;
import baraholkateam.rest.repository.ActualAdvertisementRepository;
import baraholkateam.rest.service.LastSentMessageService;
import baraholkateam.telegram_api_requests.TelegramAPIRequests;
import baraholkateam.util.State;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.List;

@Component
public class UserAdvertisements extends Command {
    private static final String NO_ADVERTISEMENTS = "Пользователь не опубликовал ни одного объявления.";
    private static final String USER_ADVERTISEMENTS = "Опубликованные актуальные объявления пользователя:";

    @Autowired
    private TelegramAPIRequests telegramAPIRequests;

    @Autowired
    private ActualAdvertisementRepository actualAdvertisementRepository;

    @Autowired
    private LastSentMessageService lastSentMessageService;

    public UserAdvertisements() {
        super(State.UserAdvertisements.getIdentifier(), State.UserAdvertisements.getDescription());
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
        List<ActualAdvertisement> actualAdvertisements = actualAdvertisementRepository.findAllByOwnerChatId(chat.getId());

        if (actualAdvertisements != null && !actualAdvertisements.isEmpty()) {
            sendAnswer(absSender, chat.getId(), this.getCommandIdentifier(), user.getUserName(),
                    USER_ADVERTISEMENTS, null);
            for (ActualAdvertisement actualAdvertisement : actualAdvertisements) {
                telegramAPIRequests.forwardMessage(
                        BaraholkaBotProperties.CHANNEL_CHAT_ID,
                        String.valueOf(actualAdvertisement.getOwnerChatId()),
                        actualAdvertisement.getMessageId()
                );
            }
        } else {
            sendAnswer(absSender, chat.getId(), this.getCommandIdentifier(), user.getUserName(),
                    NO_ADVERTISEMENTS, null);
        }
    }
}
