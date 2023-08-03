package baraholkateam.command;

import baraholkateam.rest.model.ActualObyavleniye;
import baraholkateam.rest.service.ActualObyavleniyeService;
import baraholkateam.util.State;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.ArrayList;
import java.util.List;

import static baraholkateam.rest.model.ActualObyavleniye.DESCRIPTION_TEXT;

@Component
public class DeleteObyavleniye extends Command {
    public static final String NOT_ACTUAL_TEXT = "<b>НЕАКТУАЛЬНО</b>";
    public static final String USER_ACTUAL_ADS_TEXT = """
            Здесь представлены краткие описания Ваших актуальных объявлений.
            Пожалуйста, выберите одно из них для удаления:""";
    public static final String DELETE_AD = """
            Удалить выбранное объявление?""";
    private static final String NO_ADS_TO_DELETE = """
            У вас нет актуальных объявлений.""";

    @Autowired
    private ActualObyavleniyeService actualObyavleniyeService;

    public DeleteObyavleniye() {
        super(State.DeleteObyavleniye.getIdentifier(), State.DeleteObyavleniye.getDescription());
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
        long chatId = chat.getId();
        List<ActualObyavleniye> ads = actualObyavleniyeService.getByChatId(chatId);

        if (ads == null || ads.isEmpty()) {
            sendAnswer(absSender, chat.getId(), this.getCommandIdentifier(), user.getUserName(), NO_ADS_TO_DELETE,
                    null);
        } else {
            sendAnswer(absSender, chat.getId(), this.getCommandIdentifier(), user.getUserName(), USER_ACTUAL_ADS_TEXT,
                    sendInlineKeyBoardMessage(ads));
        }
    }

    public InlineKeyboardMarkup sendInlineKeyBoardMessage(List<ActualObyavleniye> ads) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();

        ads.forEach(ad -> {
            InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
            String description = ad.getObyavleniyeText();
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
