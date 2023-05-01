package baraholkateam.command;

import baraholkateam.database.SQLExecutor;
import baraholkateam.util.State;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static baraholkateam.util.Advertisement.DESCRIPTION_TEXT;

public class DeleteAdvertisement extends Command {
    public static final String NOT_ACTUAL_TEXT = "<b>НЕАКТУАЛЬНО</b>";
    public static final String USER_ACTUAL_ADS_TEXT = """
            Здесь представлены краткие описания Ваших актуальных объявлений.
            Пожалуйста, выберите одно из них для удаления:""";
    public static final String DELETE_AD = """
            Удалить выбранное объявление?""";
    private static final String NO_ADS_TO_DELETE = """
            У вас нет актуальных объявлений.""";
    private final SQLExecutor sqlExecutor;

    public DeleteAdvertisement(Map<Long, Message> lastSentMessage, SQLExecutor sqlExecutor) {
        super(State.DeleteAdvertisement.getIdentifier(), State.DeleteAdvertisement.getDescription(), lastSentMessage);
        this.sqlExecutor = sqlExecutor;
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
        long chatId = chat.getId();
        List<SQLExecutor.AdvertisementScheme> ads = sqlExecutor.userAds(chatId);

        if (ads == null || ads.isEmpty()) {
            sendAnswer(absSender, chat.getId(), this.getCommandIdentifier(), user.getUserName(), NO_ADS_TO_DELETE,
                    null);
        } else {
            sendAnswer(absSender, chat.getId(), this.getCommandIdentifier(), user.getUserName(), USER_ACTUAL_ADS_TEXT,
                    sendInlineKeyBoardMessage(ads));
        }
    }

    public InlineKeyboardMarkup sendInlineKeyBoardMessage(List<SQLExecutor.AdvertisementScheme> ads) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();

        ads.forEach(ad -> {
            InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
            String description = ad.getAllText();
            int descIndex = description.indexOf(DESCRIPTION_TEXT);
            inlineKeyboardButton.setText(
                    description
                            .substring(descIndex + DESCRIPTION_TEXT.length(),
                                    descIndex + DESCRIPTION_TEXT.length() + 40)
                            .concat("...")
            );
            inlineKeyboardButton.setCallbackData(String.format("%s %d", DELETE_CALLBACK_TEXT, ad.getMessageId()));
            List<InlineKeyboardButton> keyboardButtonsRow = new ArrayList<>();
            keyboardButtonsRow.add(inlineKeyboardButton);
            rowList.add(keyboardButtonsRow);
        });

        inlineKeyboardMarkup.setKeyboard(rowList);

        return inlineKeyboardMarkup;
    }
}
