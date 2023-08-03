package baraholkateam.rest.controller;

import baraholkateam.bot.BaraholkaBot;
import baraholkateam.command.NewObyavleniyeConfirm;
import baraholkateam.rest.model.ActualObyavleniye;
import baraholkateam.rest.model.CurrentObyavleniye;
import baraholkateam.rest.service.ActualObyavleniyeService;
import baraholkateam.rest.service.CurrentObyavleniyeService;
import baraholkateam.telegram_api_requests.TelegramAPIRequests;
import baraholkateam.util.Tag;
import baraholkateam.util.TelegramUserInfo;
import com.fasterxml.jackson.databind.JsonNode;
import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageCaption;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import static baraholkateam.command.DeleteObyavleniye.NOT_ACTUAL_TEXT;
import static baraholkateam.notification.NotificationExecutor.FIRST_REPEAT_NOTIFICATION_PERIOD;
import static baraholkateam.notification.NotificationExecutor.FIRST_REPEAT_NOTIFICATION_TIME_UNIT;

@Component
public class BaraholkaBotRestControllerHelper {

    @Autowired
    private TelegramAPIRequests telegramAPIRequests;

    @Autowired
    private CurrentObyavleniyeService currentObyavleniyeService;

    @Autowired
    private ActualObyavleniyeService actualObyavleniyeService;

    @Autowired
    private NewObyavleniyeConfirm newObyavleniyeConfirm;

    @Autowired
    private BaraholkaBot baraholkaBot;

    @Value("${bot.token}")
    private String botToken;

    @Value("${channel.chat_id}")
    private String channelChatId;

    @Value("${channel.username}")
    private String channelUsername;

    private static final Logger LOGGER = LoggerFactory.getLogger(BaraholkaBotRestControllerHelper.class);

    @Autowired
    public BaraholkaBotRestControllerHelper() {

    }

    boolean checkUserRights(TelegramUserInfo userInfo) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] secretKeyBytes = md.digest(botToken.getBytes(StandardCharsets.UTF_8));
            Mac sha256HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKey = new SecretKeySpec(secretKeyBytes, "HmacSHA256");
            sha256HMAC.init(secretKey);

            String calculatedHash = Hex.encodeHexString(sha256HMAC.doFinal(
                    userInfo
                            .getCheckString()
                            .getBytes(StandardCharsets.UTF_8)
            ));

            return calculatedHash.equals(userInfo.getHash());
        } catch (NoSuchAlgorithmException e) {
            LOGGER.error("No such algorithm", e);
            return false;
        } catch (InvalidKeyException e) {
            LOGGER.error("Invalid key", e);
            return false;
        }
    }

    boolean checkIsUserChannelMember(Long userId) {
        String userRole = telegramAPIRequests.getUserRole(userId);
        return Objects.equals(userRole, "creator")
                || Objects.equals(userRole, "administrator")
                || Objects.equals(userRole, "member");
    }

    TelegramUserInfo getUserInfo(JsonNode json) {
        Long userId = Long.parseLong(json.get("id").asText());
        String firstName = json.get("first_name").asText();
        String lastName = json.get("last_name").asText();
        String username = json.get("username").asText();
        String photoUrl = json.get("photo_url").asText();
        Integer authDate = Integer.parseInt(json.get("auth_date").asText());
        String hash = json.get("hash").asText();

        return new TelegramUserInfo(userId, firstName, lastName, username, photoUrl,
                authDate, hash);
    }

    CurrentObyavleniye getCurrentObyavleniye(JsonNode json) {
        Iterator<JsonNode> tagNodeIterator = json.withArray("tags").elements();
        List<Tag> tags = new ArrayList<>();
        while (tagNodeIterator.hasNext()) {
            tags.add(Tag.getTagByName(tagNodeIterator.next().asText()));
        }
        String phone = json.get("phone").asText();
        if (Objects.equals(phone, "null")) {
            phone = null;
        }
        Iterator<JsonNode> contactsNodeIterator = json.withArray("contacts").elements();
        List<String> contacts = new ArrayList<>();
        while (contactsNodeIterator.hasNext()) {
            contacts.add(contactsNodeIterator.next().asText());
        }
        Long userId = Long.parseLong(json.get("id").asText());
        String description = json.get("description").asText();
        String priceString = json.get("price").asText();
        Long price = null;
        if (!Objects.equals(priceString, "null")) {
            price = Long.parseLong(priceString);
        }

        return new CurrentObyavleniye(userId, description, tags, price, phone, contacts);
    }

    List<String> getTagsList(JsonNode json) {
        List<String> tagsList = new ArrayList<>();
        Iterator<JsonNode> tagNodeIterator = json.withArray("tags").elements();
        while (tagNodeIterator.hasNext()) {
            tagsList.add(tagNodeIterator.next().asText());
        }
        return tagsList;
    }

    boolean addNewObyavleniye(CurrentObyavleniye currentObyavleniye, JsonNode json) {
        if (currentObyavleniye.getContacts().size() == 0 && currentObyavleniye.getPhone() == null) {
            currentObyavleniye.setSocials(List.of("@"
                    + telegramAPIRequests.getUser(currentObyavleniye.getChatId()).username()));
        }

        currentObyavleniyeService.put(currentObyavleniye);

        Iterator<JsonNode> photosNodeIterator = json.withArray("photos").elements();
        List<String> photos = new ArrayList<>();
        while (photosNodeIterator.hasNext()) {
            photos.add(photosNodeIterator.next().asText());
        }

        Message sentAd;
        if (photos.size() == 1) {
            try {
                File photoFile = File.createTempFile("photo", "temp");
                Files.write(Path.of(photoFile.getPath()), Base64.getDecoder().decode(photos.get(0)));
                sentAd = newObyavleniyeConfirm.sendPhotoMessage(
                        baraholkaBot,
                        channelUsername,
                        //Long.parseLong(channelChatId),
                        photoFile,
                        currentObyavleniyeService.getObyavleniyeText(currentObyavleniye.getChatId())
                );
            } catch (IOException e) {
                LOGGER.error("Cannot send photo", e);
                return false;
            }
        } else {
            List<File> photoFiles = new ArrayList<>();

            for (String fileString : photos) {
                try {
                    File file = File.createTempFile("photo", "temp");
                    Files.write(Path.of(file.getPath()), Base64.getDecoder().decode(fileString));
                    photoFiles.add(file);
                } catch (IOException e) {
                    LOGGER.error("Cannot send photos", e);
                    return false;
                }
            }

            List<Message> messages = newObyavleniyeConfirm.sendPhotoMediaGroup(baraholkaBot,
                    Long.parseLong(channelChatId),
                    photoFiles,
                    currentObyavleniyeService.getObyavleniyeText(currentObyavleniye.getChatId()));
            sentAd = messages.get(0);
        }

        if (sentAd != null) {
            currentObyavleniye
                    .setMessageId(Long.parseLong(String.valueOf(sentAd.getMessageId())))
                    .setPhotos(photos)
                    .setCreationTime(System.currentTimeMillis())
                    .setNextUpdateTime(
                            System.currentTimeMillis()
                                    + FIRST_REPEAT_NOTIFICATION_TIME_UNIT
                                    .toMillis(FIRST_REPEAT_NOTIFICATION_PERIOD)
                    )
                    .setUpdateAttempt(0);
            currentObyavleniyeService.put(currentObyavleniye);
            actualObyavleniyeService.insertNewObyavleniye(currentObyavleniye);

            return true;
        }
        LOGGER.error("Cannot send obyavleniye to channel.");
        return false;
    }

    void deleteMessage(Long messageId) throws TelegramApiException {
        EditMessageCaption editMessage = new EditMessageCaption();
        String adText = actualObyavleniyeService.adText(messageId)
                .substring(Tag.Actual.getName().length() + 1);
        String editedText = String.format("%s\n\n%s", NOT_ACTUAL_TEXT, adText);
        //editMessage.setChatId(channelChatId);
        editMessage.setChatId(channelUsername);
        editMessage.setMessageId(Integer.parseInt(String.valueOf(messageId)));
        editMessage.setParseMode(ParseMode.HTML);
        editMessage.setCaption(editedText);

        baraholkaBot.execute(editMessage);
    }

    boolean isUserMessageOwner(Long userId, Long messageId) {
        ActualObyavleniye obyavleniye = actualObyavleniyeService.get(messageId);
        return obyavleniye != null && Objects.equals(obyavleniye.getOwnerChatId(), userId);
    }
}
