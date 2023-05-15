package baraholkateam.command;

import baraholkateam.rest.service.CurrentAdvertisementService;
import baraholkateam.rest.service.LastSentMessageService;
import baraholkateam.util.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

@Component
public class NewAdvertisementConfirmPhoto extends Command {
    public static final String DELETE_ALL_PHOTOS = "Удалить все фотографии";
    private static final String CONFIRM_PHOTOS_TEXT = """
            Общее количество добавленных фотографий: %s.
            Вы можете добавить еще или перейти к добавлению описания.""";

    private static final String NO_MORE_CONFIRM_PHOTOS_TEXT = """
            Общее количество добавленных фотографий: %s.
            Пожалуйста, перейдите к описанию.""";
    private static final Logger LOGGER = LoggerFactory.getLogger(NewAdvertisementConfirmPhoto.class);

    @Autowired
    private LastSentMessageService lastSentMessageService;

    @Autowired
    private CurrentAdvertisementService currentAdvertisementService;

    public NewAdvertisementConfirmPhoto() {
        super(State.NewAdvertisement_ConfirmPhoto.getIdentifier(),
                State.NewAdvertisement_ConfirmPhoto.getDescription());
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        int addedPhotosCount = currentAdvertisementService.getPhotoIds(chat.getId()).size();

        Message lastSentMessage = lastSentMessageService.get(chat.getId());
        if (lastSentMessage.getText().substring(0, 20).equals(CONFIRM_PHOTOS_TEXT.substring(0, 20))) {
            DeleteMessage deleteMessage = new DeleteMessage();
            deleteMessage.setChatId(chat.getId());
            deleteMessage.setMessageId(lastSentMessage.getMessageId());
            try {
                absSender.execute(deleteMessage);
            } catch (TelegramApiException e) {
                LOGGER.error("Cannot delete message", e);
            }
        }

        if (addedPhotosCount < 10) {
            sendAnswer(absSender, chat.getId(), this.getCommandIdentifier(), user.getUserName(),
                    String.format(CONFIRM_PHOTOS_TEXT, addedPhotosCount), getAddReplyKeyboard(true));
        } else {
            sendAnswer(absSender, chat.getId(), this.getCommandIdentifier(), user.getUserName(),
                    String.format(NO_MORE_CONFIRM_PHOTOS_TEXT, addedPhotosCount), getAddReplyKeyboard(false));
        }
    }

    private ReplyKeyboardMarkup getAddReplyKeyboard(boolean ifAddPhotos) {
        if (ifAddPhotos) {
            return getReplyKeyboard(List.of(
                    State.NewAdvertisement_AddPhotos.getDescription(),
                    State.NewAdvertisement_AddDescription.getDescription(),
                    DELETE_ALL_PHOTOS
            ));
        }

        return getReplyKeyboard(List.of(
                State.NewAdvertisement_AddDescription.getDescription(),
                DELETE_ALL_PHOTOS
        ));
    }
}
