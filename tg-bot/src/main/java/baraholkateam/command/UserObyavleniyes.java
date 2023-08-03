package baraholkateam.command;

import baraholkateam.rest.model.ActualObyavleniye;
import baraholkateam.rest.repository.ActualObyavleniyeRepository;
import baraholkateam.rest.service.LastSentMessageService;
import baraholkateam.telegram_api_requests.TelegramAPIRequests;
import baraholkateam.util.State;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.List;

@Component
public class UserObyavleniyes extends Command {
    private static final String NO_OBYAVLENIYES = "Пользователь не опубликовал ни одного объявления.";
    private static final String USER_OBYAVLENIYES = "Опубликованные актуальные объявления пользователя:";

    @Autowired
    private TelegramAPIRequests telegramAPIRequests;

    @Autowired
    private ActualObyavleniyeRepository actualObyavleniyeRepository;

    @Autowired
    private LastSentMessageService lastSentMessageService;

    @Value("${channel.chat_id}")
    private String channelChatId;

    public UserObyavleniyes() {
        super(State.UserObyavleniyes.getIdentifier(), State.UserObyavleniyes.getDescription());
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
        List<ActualObyavleniye> actualObyavleniyes = actualObyavleniyeRepository
                .findAllByOwnerChatId(chat.getId());

        if (actualObyavleniyes != null && !actualObyavleniyes.isEmpty()) {
            sendAnswer(absSender, chat.getId(), this.getCommandIdentifier(), user.getUserName(),
                    USER_OBYAVLENIYES, null);
            for (ActualObyavleniye actualObyavleniye : actualObyavleniyes) {
                telegramAPIRequests.forwardMessage(
                        channelChatId,
                        String.valueOf(actualObyavleniye.getOwnerChatId()),
                        actualObyavleniye.getMessageId()
                );
            }
        } else {
            sendAnswer(absSender, chat.getId(), this.getCommandIdentifier(), user.getUserName(),
                    NO_OBYAVLENIYES, null);
        }
    }
}
