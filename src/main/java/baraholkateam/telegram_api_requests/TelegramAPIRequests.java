package baraholkateam.telegram_api_requests;

import baraholkateam.bot.BaraholkaBotProperties;
import org.apache.http.client.utils.URIBuilder;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class TelegramAPIRequests {
    private static final String FORWARD_MESSAGE = "https://api.telegram.org/bot%s/forwardMessage";
    private final HttpClient client = HttpClient.newHttpClient();
    private final Logger logger = LoggerFactory.getLogger(TelegramAPIRequests.class);

    public void forwardMessage(String from, String to, Long messageId) {
        try {
            URI uri = new URIBuilder(String.format(FORWARD_MESSAGE, BaraholkaBotProperties.BOT_TOKEN))
                    .addParameter("chat_id", String.format("%s", to))
                    .addParameter("from_chat_id", String.format("%s", from))
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
                return;
            }

            JSONObject object = new JSONObject(response.body());

            if (!object.has("result")) {
                errorMessage = """
                                Response doesn't contain 'result' field.
                                Request URI: %s
                                Request headers: %s
                                Response: %s""";
                logError(request, response, errorMessage);
                return;
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
        } catch (URISyntaxException | IOException | InterruptedException e) {
            logger.error(String.format("Cannot create request: %s", e.getMessage()));
        }
    }

    private void logError(HttpRequest request, HttpResponse<String> response, String errorMessage) {
        logger.error(String.format(errorMessage, request.uri(), request.headers().toString(), response.body()));
    }
}
