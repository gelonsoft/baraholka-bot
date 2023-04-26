package baraholkateam.command;

import baraholkateam.bot.BaraholkaBotProperties;
import baraholkateam.database.SQLExecutor;
import baraholkateam.telegram_api_requests.TelegramAPIRequests;
import baraholkateam.util.State;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.List;
import java.util.Map;

public class UserAdvertisements extends Command {
    private static final String NO_ADVERTISEMENTS = "Пользователь не опубликовал ни одного объявления.";
    private static final String USER_ADVERTISEMENTS = "Опубликованные актуальные объявления пользователя:";
    private final SQLExecutor sqlExecutor;
    private final TelegramAPIRequests telegramAPIRequests;

    public UserAdvertisements(Map<Long, Message> lastSentMessage, SQLExecutor sqlExecutor,
                              TelegramAPIRequests telegramAPIRequests) {
        super(State.UserAdvertisements.getIdentifier(), State.UserAdvertisements.getDescription(), lastSentMessage);
        this.sqlExecutor = sqlExecutor;
        this.telegramAPIRequests = telegramAPIRequests;
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
        List<SQLExecutor.AdvertisementScheme> advertisements = sqlExecutor.userAds(chat.getId());

        if (advertisements != null && !advertisements.isEmpty()) {
            sendAnswer(absSender, chat.getId(), this.getCommandIdentifier(), user.getUserName(),
                    USER_ADVERTISEMENTS, null);
            for (SQLExecutor.AdvertisementScheme advertisementScheme : advertisements) {
                telegramAPIRequests.forwardMessage(
                        BaraholkaBotProperties.CHANNEL_CHAT_ID,
                        String.valueOf(advertisementScheme.getChatId()),
                        advertisementScheme.getMessageId()
                );
            }
        } else {
            sendAnswer(absSender, chat.getId(), this.getCommandIdentifier(), user.getUserName(),
                    NO_ADVERTISEMENTS, null);
        }
    }
}
