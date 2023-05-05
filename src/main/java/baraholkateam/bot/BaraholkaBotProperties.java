package baraholkateam.bot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public final class BaraholkaBotProperties {
    public static final String BOT_NAME;
    public static final String BOT_USERNAME;
    public static final String BOT_ID;
    public static final String BOT_TOKEN;
    public static final String CHANNEL_USERNAME;
    public static final String CHANNEL_CHAT_ID;
    public static final String BOT_CHANNEL_ID;
    public static final String DB_URL;
    public static final String DB_USER;
    public static final String DB_PASS;
    private static final Properties properties = new Properties();
    private static final Logger logger = LoggerFactory.getLogger(BaraholkaBotProperties.class);

    static {
        try {
            properties.load(new FileReader(String.format("%s\\src\\main\\resources\\application.properties",
                    System.getProperty("user.dir"))));
        } catch (IOException e) {
            logger.error("Cannot load file application.properties: " + e.getMessage());
            throw new IllegalStateException("Failed to read api keys from application.properties.", e);
        }
        BOT_NAME = properties.getProperty("bot.name");
        BOT_USERNAME = properties.getProperty("bot.username");
        BOT_ID = properties.getProperty("bot.id");
        BOT_TOKEN = properties.getProperty("bot.token");
        CHANNEL_USERNAME = properties.getProperty("channel.username");
        CHANNEL_CHAT_ID = properties.getProperty("channel.chat_id");
        BOT_CHANNEL_ID = properties.getProperty("bot.channelId");
        DB_URL = properties.getProperty("spring.datasource.url");
        DB_USER = properties.getProperty("spring.datasource.username");
        DB_PASS = properties.getProperty("spring.datasource.password");
    }
}
