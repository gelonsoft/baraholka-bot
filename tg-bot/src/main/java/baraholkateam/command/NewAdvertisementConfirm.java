package baraholkateam.command;

import baraholkateam.rest.model.CurrentAdvertisement;
import baraholkateam.rest.service.CurrentAdvertisementService;
import baraholkateam.telegram_api_requests.TelegramAPIRequests;
import baraholkateam.util.Converter;
import baraholkateam.util.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMediaGroup;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.media.InputMedia;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaPhoto;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Component
public class NewAdvertisementConfirm extends Command {
    private static final String CONTACTS_LIST_TEXT = """
            Добавлены ссылки на следующие социальные сети:
            %s""";
    private static final String FORMED_ADVERTISEMENT = """
            Сформировано следующее объявление:""";
    private static final String CONFIRM_AD_TEXT = """
            Желаете опубликовать ваше объявление в канале?""";
    private static final Logger LOGGER = LoggerFactory.getLogger(NewAdvertisementConfirm.class);

    @Autowired
    private CurrentAdvertisementService currentAdvertisementService;

    @Autowired
    private TelegramAPIRequests telegramAPIRequests;

    @Autowired
    public NewAdvertisementConfirm() {
        super(State.NewAdvertisement_Confirm.getIdentifier(), State.NewAdvertisement_Confirm.getDescription());
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        CurrentAdvertisement ad = currentAdvertisementService.get(chat.getId());

        String text = ad.getAdvertisementText();

        List<String> photos = ad.getPhotos();

        if (ad.getContacts() != null && !ad.getContacts().isEmpty()) {
            sendAnswer(absSender, chat.getId(), this.getCommandIdentifier(), user.getUserName(),
                    String.format(CONTACTS_LIST_TEXT, String.join("\n", ad.getContacts())), null);
        }

        sendAnswer(absSender, chat.getId(), this.getCommandIdentifier(), user.getUserName(),
                FORMED_ADVERTISEMENT, getReplyKeyboard(Collections.emptyList(), true));

        if (photos.size() == 1) {
            sendPhotoMessage(absSender, chat.getId(), Converter.convertBase64StringToPhoto(photos.get(0)), text);
        } else if (photos.size() > 1) {
            List<File> photoFiles = new ArrayList<>();
            for (String photo : photos) {
                photoFiles.add(Objects.requireNonNull(Converter.convertBase64StringToPhoto(photo)));
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
            LOGGER.error("Can't send photo message to chatId="+chatId, e);
            return null;
        }
    }

    public Message sendPhotoMessage(AbsSender absSender, String chatId, File photoFile, String text) {
        SendPhoto sendPhoto = SendPhoto.builder()
                .chatId(chatId)
                .photo(new InputFile(Objects.requireNonNull(photoFile)))
                .caption(text)
                .parseMode(ParseMode.HTML)
                .build();

        try {
            return absSender.execute(sendPhoto);
        } catch (TelegramApiException e) {
            LOGGER.error("Can't send photo message to chatId="+chatId, e);
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
            LOGGER.error("Can't send photos with media group", e);
            return null;
        }
    }

    private InlineKeyboardMarkup getConfirmAdvertisement(List<String> photos) {
        InlineKeyboardButton yesButton = new InlineKeyboardButton();
        yesButton.setText("Да");
        String yesCallbackData = String.format("%s %s %d", CONFIRM_AD_CALLBACK_DATA, "yes",
                photos.size() == 1 ? 0 : 1);
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
