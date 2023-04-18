package baraholkateam.command;

import baraholkateam.bot.BaraholkaBot;
import baraholkateam.telegram_api_requests.TelegramAPIRequests;
import baraholkateam.util.Advertisement;
import baraholkateam.util.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMediaGroup;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.media.InputMedia;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaPhoto;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class NewAdvertisement_Confirm extends Command {
    private static final String CONFIRM_AD_TEXT = """
            Желаете опубликовать ваше объявление в канале?""";
    private final Map<Long, Advertisement> advertisement;
    private final TelegramAPIRequests telegramAPIRequests;
    private final BaraholkaBot bot;
    private static final Logger logger = LoggerFactory.getLogger(NewAdvertisement_Confirm.class);

    public NewAdvertisement_Confirm(Map<Long, Message> lastSentMessage, Map<Long, Advertisement> advertisement,
                                    TelegramAPIRequests telegramAPIRequests, BaraholkaBot bot) {
        super(State.NewAdvertisement_Confirm.getIdentifier(),
                State.NewAdvertisement_Confirm.getDescription(), lastSentMessage);
        this.advertisement = advertisement;
        this.telegramAPIRequests = telegramAPIRequests;
        this.bot = bot;
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        Advertisement ad = advertisement.get(chat.getId());

        String text = ad.getAdvertisementText();

        List<PhotoSize> photos = ad.getPhotos();

        if (photos.size() == 1) {
            sendPhotoMessage(absSender, chat.getId(), downloadPhoto(bot, photos.get(0)), text);
        } else if (photos.size() > 1) {
            List<File> photoFiles = new ArrayList<>();
            for (PhotoSize photoSize : photos) {
                photoFiles.add(Objects.requireNonNull(downloadPhoto(bot, photoSize)));
            }
            sendPhotoMediaGroup(absSender, chat.getId(), photoFiles, text);
        }

        sendAnswer(absSender, chat.getId(), this.getCommandIdentifier(), user.getUserName(),
                CONFIRM_AD_TEXT, getConfirmAdvertisement(photos));
    }

    public Message sendPhotoMessage(AbsSender absSender, long chatId, File photoFile, String text) {
        SendPhoto sendPhoto = SendPhoto.builder()
                .chatId(chatId)
                .photo(new InputFile(Objects.requireNonNull(photoFile)))
                .caption(text)
                .parseMode(ParseMode.HTML)
                .build();

        try {
            return absSender.execute(sendPhoto);
        } catch (TelegramApiException e) {
            logger.error("Can't send photo message", e);
            return null;
        }
    }

    public List<Message> sendPhotoMediaGroup(AbsSender absSender, long chatId, List<File> photoFiles, String text) {
        AtomicInteger count = new AtomicInteger();
        List<InputMedia> medias = photoFiles.stream()
                .map(photoFile -> {
                    String mediaName = UUID.randomUUID().toString();

                    InputMediaPhoto.InputMediaPhotoBuilder inputMediaPhotoBuilder = InputMediaPhoto.builder()
                                .media("attach://" + mediaName)
                                .mediaName(mediaName)
                                .isNewMedia(true)
                                .newMediaFile(Objects.requireNonNull(photoFile))
                                .parseMode(ParseMode.HTML);

                    if (count.getAndIncrement() == 0) {
                        inputMediaPhotoBuilder.caption(text);
                    }

                    return inputMediaPhotoBuilder.build();
                }).collect(Collectors.toList());

        SendMediaGroup sendMediaGroup = SendMediaGroup.builder()
                .chatId(chatId)
                .medias(medias)
                .build();

        try {
            return absSender.execute(sendMediaGroup);
        } catch (TelegramApiException e) {
            logger.error("Can't send photos with media group", e);
            return null;
        }
    }

    public File downloadPhoto(BaraholkaBot bot, PhotoSize photo) {
        String filePath = telegramAPIRequests.getFilePath(photo.getFileId());

        try {
            return bot.downloadFile(filePath);
        } catch (TelegramApiException e) {
            logger.error("Cannot download photo", e);
            return null;
        }
    }

    private InlineKeyboardMarkup getConfirmAdvertisement(List<PhotoSize> photos) {
        InlineKeyboardButton yesButton = new InlineKeyboardButton();
        yesButton.setText("Да");
        String yesCallbackData = String.format("%s %s %d", CONFIRM_AD_CALLBACK_DATA, "yes", photos.size() == 1 ? 0 : 1);
        yesButton.setCallbackData(yesCallbackData);

        InlineKeyboardButton noButton = new InlineKeyboardButton();
        noButton.setText("Нет");
        String noCallbackData = String.format("%s %s", CONFIRM_AD_CALLBACK_DATA, "no");
        noButton.setCallbackData(noCallbackData);

        List<InlineKeyboardButton> keyboardFirstRow = new ArrayList<>();
        keyboardFirstRow.add(yesButton);
        keyboardFirstRow.add(noButton);

        List<List<InlineKeyboardButton>> keyboardRows = new ArrayList<>();
        keyboardRows.add(keyboardFirstRow);

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(keyboardRows);

        return inlineKeyboardMarkup;
    }
}
