package baraholkateam.bot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public final class BaraholkaBotProperties {
    public static final String NAME;
    public static final String USERNAME;
    public static final String ID;
    public static final String TOKEN;
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
        NAME = properties.getProperty("bot.name");
        USERNAME = properties.getProperty("bot.username");
        ID = properties.getProperty("bot.id");
        TOKEN = properties.getProperty("bot.token");
        DB_URL = properties.getProperty("db.url");
        DB_USER = properties.getProperty("db.user");
        DB_PASS = properties.getProperty("db.password");
    }
}
