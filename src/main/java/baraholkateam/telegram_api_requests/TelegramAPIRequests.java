package baraholkateam.telegram_api_requests;

import baraholkateam.bot.BaraholkaBotProperties;
import org.apache.http.client.utils.URIBuilder;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private final HttpClient client = HttpClient.newHttpClient();
    private final Logger logger = LoggerFactory.getLogger(TelegramAPIRequests.class);

    public Long forwardMessage(String fromChatId, String toChatId, Long messageId) {
        try {
            URI uri = new URIBuilder(String.format(FORWARD_MESSAGE, BaraholkaBotProperties.BOT_TOKEN))
                    .addParameter("chat_id", String.format("%s", toChatId))
                    .addParameter("from_chat_id", String.format("%s", fromChatId))
                    .addParameter("message_id", String.valueOf(messageId))
                    .build();

            HttpRequest request = HttpRequest
                    .newBuilder()
                    .GET()
                    .uri(uri)
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            String errorMessage;
            if (response.statusCode() != 200) {
                errorMessage = """
                                Not 200 code of the response.
                                Request URI: %s
                                Request headers: %s
                                Response: %s""";
                logError(request, response, errorMessage);
                return 0L;
            }

            JSONObject object = new JSONObject(response.body());

            if (!object.has("result")) {
                errorMessage = """
                                Response doesn't contain 'result' field.
                                Request URI: %s
                                Request headers: %s
                                Response: %s""";
                logError(request, response, errorMessage);
                return 0L;
            }

            JSONObject result = object.getJSONObject("result");

            if (!result.has("message_id")) {
                errorMessage = """
                                Response doesn't contain 'message_id' field.
                                Request URI: %s
                                Request headers: %s
                                Response: %s""";
                logError(request, response, errorMessage);
            }

            return result.getLong("message_id");
        } catch (URISyntaxException | IOException | InterruptedException e) {
            logger.error(String.format("Cannot create request: %s", e.getMessage()));
            return 0L;
        }
    }

    public String getFilePath(String fileId) {
        try {
            URI uri = new URIBuilder(String.format(GET_FILE, BaraholkaBotProperties.BOT_TOKEN))
                    .addParameter("file_id", fileId)
                    .build();
            HttpRequest request = HttpRequest.newBuilder(uri).GET().build();
            HttpResponse<String> response;

            response = client.send(request, HttpResponse.BodyHandlers.ofString());

            String errorMessage;
            if (response.statusCode() != 200) {
                errorMessage = """
                                Not 200 code of the response.
                                Request URI: %s
                                Request headers: %s
                                Response: %s""";
                logError(request, response, errorMessage);
                return null;
            }

            JSONObject object = new JSONObject(response.body());

            if (!object.has("result")) {
                errorMessage = """
                                Response doesn't contain 'result' field.
                                Request URI: %s
                                Request headers: %s
                                Response: %s""";
                logError(request, response, errorMessage);
                return null;
            }

            JSONObject result = object.getJSONObject("result");

            if (!result.has("file_path")) {
                errorMessage = """
                                Response doesn't contain 'file_path' field.
                                Request URI: %s
                                Request headers: %s
                                Response: %s""";
                logError(request, response, errorMessage);
            }

            return result.getString("file_path");
        } catch (URISyntaxException | IOException | InterruptedException e) {
            logger.error(String.format("Cannot create request: %s", e.getMessage()));
            return null;
        }
    }

    public String getUserRole(Long userId) {
        try {
            URI uri = new URIBuilder(String.format(GET_CHAT_MEMBER, BaraholkaBotProperties.BOT_TOKEN))
                    .addParameter("chat_id", String.format("%s", BaraholkaBotProperties.CHANNEL_USERNAME))
                    .addParameter("user_id", String.valueOf(userId))
                    .build();

            HttpRequest request = HttpRequest
                    .newBuilder()
                    .GET()
                    .uri(uri)
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            String errorMessage;
            if (response.statusCode() != 200) {
                errorMessage = """
                                Not 200 code of the response.
                                Request URI: %s
                                Request headers: %s
                                Response: %s""";
                logError(request, response, errorMessage);
                return null;
            }

            JSONObject object = new JSONObject(response.body());

            if (!object.has("result")) {
                errorMessage = """
                                Response doesn't contain 'result' field.
                                Request URI: %s
                                Request headers: %s
                                Response: %s""";
                logError(request, response, errorMessage);
                return null;

            }

            JSONObject result = object.getJSONObject("result");

            if (!result.has("status")) {
                errorMessage = """
                                Response doesn't contain 'title' field.
                                Request URI: %s
                                Request headers: %s
                                Response: %s""";
                logError(request, response, errorMessage);
                return null;
            }

            return result.getString("status");
        } catch (URISyntaxException | IOException | InterruptedException e) {
            logger.error(String.format("Cannot create request: %s", e.getMessage()));
            return null;
        }
    }

    private void logError(HttpRequest request, HttpResponse<String> response, String errorMessage) {
        logger.error(String.format(errorMessage, request.uri(), request.headers().toString(), response.body()));
    }
}
