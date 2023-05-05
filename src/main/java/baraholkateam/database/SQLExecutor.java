package baraholkateam.database;

import baraholkateam.bot.BaraholkaBotProperties;
import baraholkateam.rest.model.Advertisement;
import baraholkateam.util.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static baraholkateam.bot.BaraholkaBot.SEARCH_ADVERTISEMENTS_LIMIT;
import static baraholkateam.secure_constants.SecureConstants.ASK_ACTUAL_ADVERTISEMENTS;
import static baraholkateam.secure_constants.SecureConstants.CREATE_TABLES;
import static baraholkateam.secure_constants.SecureConstants.GET_AD_TEXT;
import static baraholkateam.secure_constants.SecureConstants.INSERT_NEW_ADVERTISEMENT;
import static baraholkateam.secure_constants.SecureConstants.REMOVE_ADVERTISEMENT;
import static baraholkateam.secure_constants.SecureConstants.TAGS_SEARCH;
import static baraholkateam.secure_constants.SecureConstants.UPDATE_ATTEMPT_NUMBER;
import static baraholkateam.secure_constants.SecureConstants.UPDATE_NEXT_UPDATE_TIME;
import static baraholkateam.secure_constants.SecureConstants.USER_ADS;

public class SQLExecutor {
    private final Connection connection;
    private final Logger logger = LoggerFactory.getLogger(SQLExecutor.class);

    public SQLExecutor() {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            logger.error(String.format("Cannot find JDBC driver: %s", e.getMessage()));
            throw new RuntimeException("Failed to load JDBC driver", e);
        }

        try {
            connection = DriverManager.getConnection(
                    BaraholkaBotProperties.DB_URL,
                    BaraholkaBotProperties.DB_USER,
                    BaraholkaBotProperties.DB_PASS
            );

            createTables();
        } catch (SQLException e) {
            logger.error(String.format("Cannot connect to the database: %s", e.getMessage()));
            throw new RuntimeException("Failed to connect to the database.", e);
        }
    }

    private void createTables() {
        try {
            PreparedStatement createTables = connection.prepareStatement(CREATE_TABLES);

            createTables.execute();
        } catch (SQLException e) {
            logger.error(String.format("Error while creating tables: %s", e.getMessage()));
        }
    }

    public void insertNewAdvertisement(Advertisement advertisement) {
        try {
            PreparedStatement insertNewAdvertisement = connection.prepareStatement(INSERT_NEW_ADVERTISEMENT);

            insertNewAdvertisement.setLong(1, advertisement.getOwnerChatId());
            insertNewAdvertisement.setLong(2, advertisement.getMessageId());
            insertNewAdvertisement.setString(3, advertisement.getTags().stream()
                    .map(Tag::getName)
                    .collect(Collectors.joining(" ")));
            insertNewAdvertisement.setString(4, advertisement.getAdvertisementText());
            insertNewAdvertisement.setLong(5, advertisement.getCreationTime());
            insertNewAdvertisement.setLong(6, advertisement.getNextUpdateTime());
            insertNewAdvertisement.setInt(7, advertisement.getUpdateAttempt());

            insertNewAdvertisement.executeUpdate();
        } catch (SQLException e) {
            logger.error(String.format("Error while inserting new advertisement into database: %s", e.getMessage()));
        }
    }

    public ResultSet tagsSearch(List<Tag> tags) {
        try {
            PreparedStatement tagSearch = connection.prepareStatement(TAGS_SEARCH);

            tagSearch.setArray(1, connection.createArrayOf("TEXT",
                    tags.stream()
                            .map(Tag::getName)
                            .toArray(String[]::new)));
            tagSearch.setInt(2, SEARCH_ADVERTISEMENTS_LIMIT);

            return tagSearch.executeQuery();
        } catch (SQLException e) {
            logger.error(String.format("Error while searching advertisements by tags in database: %s", e.getMessage()));
            return null;
        }
    }

    public ResultSet askActualAdvertisements(Long currentTime) {
        try {
            PreparedStatement askActualAdvertisements = connection.prepareStatement(ASK_ACTUAL_ADVERTISEMENTS);

            askActualAdvertisements.setLong(1, currentTime);

            return askActualAdvertisements.executeQuery();
        } catch (SQLException e) {
            logger.error(String.format("Error while taking actual advertisements from database: %s", e.getMessage()));
            return null;
        }
    }

    public void removeAdvertisement(long chatId, long messageId) {
        try {
            PreparedStatement removeAdvertisement = connection.prepareStatement(REMOVE_ADVERTISEMENT);

            removeAdvertisement.setLong(1, chatId);
            removeAdvertisement.setLong(2, messageId);

            removeAdvertisement.execute();
        } catch (SQLException e) {
            logger.error(String.format("Error while deleting advertisements from database: %s", e.getMessage()));
        }
    }

    public void updateAttemptNumber(long chatId, long messageId, int newAttemptNum) {
        try {
            PreparedStatement updateAttemptNumber = connection.prepareStatement(UPDATE_ATTEMPT_NUMBER);

            updateAttemptNumber.setInt(1, newAttemptNum);
            updateAttemptNumber.setLong(2, chatId);
            updateAttemptNumber.setLong(3, messageId);

            updateAttemptNumber.execute();
        } catch (SQLException e) {
            logger.error(String.format("Error while deleting advertisements from database: %s", e.getMessage()));
        }
    }

    public void updateNextUpdateTime(long chatId, long messageId, long nextUpdateTime) {
        try {
            PreparedStatement updateNextUpdateTime = connection.prepareStatement(UPDATE_NEXT_UPDATE_TIME);

            updateNextUpdateTime.setLong(1, nextUpdateTime);
            updateNextUpdateTime.setLong(2, chatId);
            updateNextUpdateTime.setLong(3, messageId);

            updateNextUpdateTime.execute();
        } catch (SQLException e) {
            logger.error(String.format("Error while deleting advertisements from database: %s", e.getMessage()));
        }
    }

    public List<AdvertisementScheme> userAds(Long chatId) {
        try {
            PreparedStatement userAds = connection.prepareStatement(USER_ADS);
            userAds.setLong(1, chatId);
            ResultSet result = userAds.executeQuery();
            List<AdvertisementScheme> ads = new ArrayList<>();
            while (result.next()) {
                Long messageId = result.getLong("message_id");
                String tags = result.getString("tags");
                String allText = result.getString("all_text");
                Long creationTime = result.getLong("creation_time");
                Long nextUpdateTime = result.getLong("next_update_time");
                Integer updateAttempt = result.getInt("update_attempt");
                ads.add(new AdvertisementScheme(chatId, messageId, tags, allText, creationTime, nextUpdateTime,
                        updateAttempt));
            }
            return ads;
        } catch (SQLException e) {
            logger.error(String.format("Error while selecting user's ads in database: %s", e.getMessage()));
            return null;
        }
    }

    public String adText(Long messageId) {
        try {
            String text = null;
            PreparedStatement adText = connection.prepareStatement(GET_AD_TEXT);
            adText.setLong(1, messageId);
            ResultSet rs = adText.executeQuery();
            if (rs.next()) {
                text = rs.getString("all_text");
            }
            return text;
        } catch (SQLException e) {
            logger.error(String.format("Error while getting ad text from database: %s", e.getMessage()));
            return null;
        }
    }

    public void closeConnection() {
        try {
            connection.close();
        } catch (SQLException e) {
            logger.error(String.format("Cannot close connection due to: %s", e.getMessage()));
            throw new RuntimeException("Failed to close connection", e);
        }
    }

    public static class AdvertisementScheme {
        private final Long chatId;
        private final Long messageId;
        private final String tags;
        private final String allText;
        private final Long creationTime;
        private final Long nextUpdateTime;
        private final Integer updateAttempt;

        public AdvertisementScheme(Long chatId, Long messageId, String tags, String allText, Long creationTime,
                                   Long nextUpdateTime, Integer updateAttempt) {
            this.chatId = chatId;
            this.messageId = messageId;
            this.tags = tags;
            this.allText = allText;
            this.creationTime = creationTime;
            this.nextUpdateTime = nextUpdateTime;
            this.updateAttempt = updateAttempt;
        }

        public Long getChatId() {
            return chatId;
        }

        public Long getMessageId() {
            return messageId;
        }

        public String getTags() {
            return tags;
        }

        public String getAllText() {
            return allText;
        }

        public Long getCreationTime() {
            return creationTime;
        }

        public Long getNextUpdateTime() {
            return nextUpdateTime;
        }

        public Integer getUpdateAttempt() {
            return updateAttempt;
        }
    }
}
