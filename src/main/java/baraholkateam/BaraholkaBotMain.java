package baraholkateam;

import baraholkateam.bot.BaraholkaBot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@ComponentScan("{ baraholkateam.bot, baraholkateam.command, baraholkateam.notification, "
        + "baraholkateam.rest.service, baraholkateam.telegram_api_requests }")
@EnableJpaRepositories("baraholkateam.rest.repository")
@EntityScan("baraholkateam.rest.model")
@EnableScheduling
@SpringBootApplication
public class BaraholkaBotMain {
    private static final Logger LOGGER = LoggerFactory.getLogger(BaraholkaBotMain.class);

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(BaraholkaBotMain.class, args);
        BaraholkaBot bot = context.getBean(BaraholkaBot.class);
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(bot);
        } catch (TelegramApiException e) {
            LOGGER.error(String.format("Cannot register new bot: %s", e.getMessage()));
            throw new RuntimeException("Failed to start application.", e);
        }
    }
}
