package baraholkateam;

import baraholkateam.bot.BaraholkaBot;
import baraholkateam.bot.BaraholkaBotProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class BaraholkaBotMain {
    private static final Logger LOGGER = LoggerFactory.getLogger(BaraholkaBotMain.class);

    public static void main(String[] args) {
        SpringApplication.run(BaraholkaBotMain.class, args);
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(new BaraholkaBot(BaraholkaBotProperties.BOT_NAME, BaraholkaBotProperties.BOT_TOKEN));
        } catch (TelegramApiException e) {
            LOGGER.error(String.format("Cannot register new bot: %s", e.getMessage()));
            throw new RuntimeException("Failed to start application.", e);
        }
    }
}
