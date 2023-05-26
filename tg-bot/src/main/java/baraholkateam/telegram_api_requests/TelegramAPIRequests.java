package baraholkateam.telegram_api_requests;

import org.apache.http.client.utils.URIBuilder;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Component
public class TelegramAPIRequests {
    private static final String FORWARD_MESSAGE = "https://api.telegram.org/bot%s/forwardMessage";
    private static final String GET_FILE = "https://api.telegram.org/bot%s/getFile";
    private static final String GET_CHAT_MEMBER = "https://api.telegram.org/bot%s/getChatMember";
    private static final String CHAT_ID_PARAMETER = "chat_id";
    private static final String FROM_CHAT_ID_PARAMETER = "from_chat_id";
    private static final String MESSAGE_ID_PARAMETER = "message_id";
    private static final String FILE_ID_PARAMETER = "file_id";
    private static final String USER_ID_PARAMETER = "user_id";
    private static final String NOT_SUCCESS_TEXT = """
                                Not 200 code of the response.
                                Request URI: %s
                                Request headers: %s
                                Response: %s""";
    private static final String NO_FIELD_TEXT = """
                                Response doesn't contain '%s' field.
                                Request URI: %s
                                Request headers: %s
                                Response: %s""";
    private static final String RESULT_FIELD = "result";
    private static final String MESSAGE_ID_FIELD = "message_id";
    private static final String FILE_PATH_FIELD = "file_path";
    private static final String STATUS_FIELD = "status";
    private static final String USER_FIELD = "user";
    private static final String ID_FIELD = "id";
    private static final String FIRST_NAME_FIELD = "first_name";
    private static final String LAST_NAME_FIELD = "last_name";
    private static final String USERNAME_FIELD = "username";
    private static final Logger LOGGER = LoggerFactory.getLogger(TelegramAPIRequests.class);
    private final HttpClient client = HttpClient.newHttpClient();

    @Value("${bot.token}")
    private String botToken;

    @Value("${channel.username}")
    private String channelUsername;

    public Long forwardMessage(String fromChatId, String toChatId, Long messageId) {
        try {
            URI uri = new URIBuilder(String.format(FORWARD_MESSAGE, botToken))
                    .addParameter(CHAT_ID_PARAMETER, String.format("%s", toChatId))
                    .addParameter(FROM_CHAT_ID_PARAMETER, String.format("%s", fromChatId))
                    .addParameter(MESSAGE_ID_PARAMETER, String.valueOf(messageId))
                    .build();

            HttpRequest request = HttpRequest
                    .newBuilder()
                    .GET()
                    .uri(uri)
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                logErrorNotSuccessCode(request, response);
                return null;
            }

            JSONObject object = new JSONObject(response.body());

            if (!object.has(RESULT_FIELD)) {
                logErrorNoField(request, response, RESULT_FIELD);
                return null;
            }

            JSONObject result = object.getJSONObject(RESULT_FIELD);

            if (!result.has(MESSAGE_ID_FIELD)) {
                logErrorNoField(request, response, MESSAGE_ID_FIELD);
                return null;
            }

            return result.getLong(MESSAGE_ID_FIELD);
        } catch (URISyntaxException | IOException | InterruptedException e) {
            LOGGER.error(String.format("Cannot create request: %s", e.getMessage()));
            return null;
        }
    }

    public String getFilePath(String fileId) {
        try {
            URI uri = new URIBuilder(String.format(GET_FILE, botToken))
                    .addParameter(FILE_ID_PARAMETER, fileId)
                    .build();
            HttpRequest request = HttpRequest.newBuilder(uri).GET().build();
            HttpResponse<String> response;

            response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                logErrorNotSuccessCode(request, response);
                return null;
            }

            JSONObject object = new JSONObject(response.body());

            if (!object.has(RESULT_FIELD)) {
                logErrorNoField(request, response, RESULT_FIELD);
                return null;
            }

            JSONObject result = object.getJSONObject(RESULT_FIELD);

            if (!result.has(FILE_PATH_FIELD)) {
                logErrorNoField(request, response, FILE_PATH_FIELD);
            }

            return result.getString(FILE_PATH_FIELD);
        } catch (URISyntaxException | IOException | InterruptedException e) {
            LOGGER.error(String.format("Cannot create request: %s", e.getMessage()));
            return null;
        }
    }

    public String getUserRole(Long userId) {
        try {
            URI uri = new URIBuilder(String.format(GET_CHAT_MEMBER, botToken))
                    .addParameter(CHAT_ID_PARAMETER, String.format("%s", channelUsername))
                    .addParameter(USER_ID_PARAMETER, String.valueOf(userId))
                    .build();

            HttpRequest request = HttpRequest
                    .newBuilder()
                    .GET()
                    .uri(uri)
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                logErrorNotSuccessCode(request, response);
                return null;
            }

            JSONObject object = new JSONObject(response.body());

            if (!object.has(RESULT_FIELD)) {
                logErrorNoField(request, response, RESULT_FIELD);
                return null;

            }

            JSONObject result = object.getJSONObject(RESULT_FIELD);

            if (!result.has(STATUS_FIELD)) {
                logErrorNoField(request, response, STATUS_FIELD);
                return null;
            }

            return result.getString(STATUS_FIELD);
        } catch (URISyntaxException | IOException | InterruptedException e) {
            LOGGER.error(String.format("Cannot create request: %s", e.getMessage()));
            return null;
        }
    }

    public TelegramUser getUser(Long userId) {
        try {
            URI uri = new URIBuilder(String.format(GET_CHAT_MEMBER, botToken))
                    .addParameter(CHAT_ID_PARAMETER, String.format("%s", channelUsername))
                    .addParameter(USER_ID_PARAMETER, String.valueOf(userId))
                    .build();

            HttpRequest request = HttpRequest
                    .newBuilder()
                    .GET()
                    .uri(uri)
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                logErrorNotSuccessCode(request, response);
                return null;
            }

            JSONObject object = new JSONObject(response.body());

            if (!object.has(RESULT_FIELD)) {
                logErrorNoField(request, response, RESULT_FIELD);
                return null;

            }

            JSONObject result = object.getJSONObject(RESULT_FIELD);

            if (!result.has(USER_FIELD)) {
                logErrorNoField(request, response, USER_FIELD);
                return null;
            }

            JSONObject user = result.getJSONObject(USER_FIELD);

            if (!user.has(ID_FIELD)) {
                logErrorNoField(request, response, ID_FIELD);
                return null;
            }

            if (!user.has(FIRST_NAME_FIELD)) {
                logErrorNoField(request, response, FIRST_NAME_FIELD);
                return null;
            }

            String firstName = user.getString(FIRST_NAME_FIELD);

            if (!user.has(LAST_NAME_FIELD)) {
                logErrorNoField(request, response, LAST_NAME_FIELD);
                return null;
            }

            String lastName = user.getString(LAST_NAME_FIELD);

            if (!user.has(USERNAME_FIELD)) {
                logErrorNoField(request, response, USERNAME_FIELD);
                return null;
            }

            Integer uid = user.getInt(ID_FIELD);
            String username = user.getString(USERNAME_FIELD);

            return new TelegramUser(uid, firstName, lastName, username);
        } catch (URISyntaxException | IOException | InterruptedException e) {
            LOGGER.error(String.format("Cannot create request: %s", e.getMessage()));
            return null;
        }
    }

    private void logErrorNotSuccessCode(HttpRequest request, HttpResponse<String> response) {
        LOGGER.error(String.format(NOT_SUCCESS_TEXT, request.uri(), request.headers().toString(), response.body()));
    }

    private void logErrorNoField(HttpRequest request, HttpResponse<String> response, String field) {
        LOGGER.error(String.format(NO_FIELD_TEXT, field, request.uri(), request.headers().toString(), response.body()));
    }

    /**
     * Данные пользователя Телеграмма.
     * @param userId id пользователя
     * @param firstName имя
     * @param lastName фамилия
     * @param username  никнейм
     */
    public record TelegramUser(Integer userId, String firstName, String lastName, String username) {

    }
}
