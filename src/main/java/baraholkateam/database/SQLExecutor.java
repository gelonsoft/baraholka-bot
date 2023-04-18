package baraholkateam.database;

import baraholkateam.bot.BaraholkaBotProperties;
import baraholkateam.util.Advertisement;
import baraholkateam.util.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

import static baraholkateam.bot.BaraholkaBot.SEARCH_ADVERTISEMENTS_LIMIT;
import static baraholkateam.secure_constants.SecureConstants.ASK_ACTUAL_ADVERTISEMENTS;
import static baraholkateam.secure_constants.SecureConstants.CREATE_TABLES;
import static baraholkateam.secure_constants.SecureConstants.INSERT_NEW_ADVERTISEMENT;
import static baraholkateam.secure_constants.SecureConstants.REMOVE_ADVERTISEMENT;
import static baraholkateam.secure_constants.SecureConstants.REMOVE_ALL_DATA;
import static baraholkateam.secure_constants.SecureConstants.TAGS_SEARCH;
import static baraholkateam.secure_constants.SecureConstants.UPDATE_ATTEMPT_NUMBER;
import static baraholkateam.secure_constants.SecureConstants.UPDATE_NEXT_UPDATE_TIME;

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

    public int insertNewAdvertisement(Advertisement advertisement) {
        try {
            PreparedStatement insertNewAdvertisement = connection.prepareStatement(INSERT_NEW_ADVERTISEMENT);

            insertNewAdvertisement.setLong(1, advertisement.getChatId());
            insertNewAdvertisement.setLong(2, advertisement.getMessageId());
            insertNewAdvertisement.setString(3, advertisement.getTags().stream()
                    .map(Tag::getName)
                    .collect(Collectors.joining(" ")));
            insertNewAdvertisement.setString(4, advertisement.getAdvertisementText());
            insertNewAdvertisement.setLong(5, advertisement.getCreationTime());
            insertNewAdvertisement.setLong(6, advertisement.getNextUpdateTime());
            insertNewAdvertisement.setInt(7, advertisement.getUpdateAttempt());

            return insertNewAdvertisement.executeUpdate();
        } catch (SQLException e) {
            logger.error(String.format("Error while inserting new advertisement into database: %s", e.getMessage()));
            return -1;
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

    public boolean removeAdvertisement(long chatId, long messageId) {
        try {
            PreparedStatement removeAdvertisement = connection.prepareStatement(REMOVE_ADVERTISEMENT);

            removeAdvertisement.setLong(1, chatId);
            removeAdvertisement.setLong(2, messageId);

            return removeAdvertisement.execute();
        } catch (SQLException e) {
            logger.error(String.format("Error while deleting advertisements from database: %s", e.getMessage()));
            return false;
        }
    }

    public boolean updateAttemptNumber(long chatId, long messageId, int newAttemptNum) {
        try {
            PreparedStatement updateAttemptNumber = connection.prepareStatement(UPDATE_ATTEMPT_NUMBER);

            updateAttemptNumber.setInt(1, newAttemptNum);
            updateAttemptNumber.setLong(2, chatId);
            updateAttemptNumber.setLong(3, messageId);

            return updateAttemptNumber.execute();
        } catch (SQLException e) {
            logger.error(String.format("Error while deleting advertisements from database: %s", e.getMessage()));
            return false;
        }
    }

    public boolean updateNextUpdateTime(long chatId, long messageId, long nextUpdateTime) {
        try {
            PreparedStatement updateNextUpdateTime = connection.prepareStatement(UPDATE_NEXT_UPDATE_TIME);

            updateNextUpdateTime.setLong(1, nextUpdateTime);
            updateNextUpdateTime.setLong(2, chatId);
            updateNextUpdateTime.setLong(3, messageId);

            return updateNextUpdateTime.execute();
        } catch (SQLException e) {
            logger.error(String.format("Error while deleting advertisements from database: %s", e.getMessage()));
            return false;
        }
    }

    // только для тестирования!
    public int removeAllData() {
        try {
            PreparedStatement removeAllData = connection.prepareStatement(REMOVE_ALL_DATA);

            return removeAllData.executeUpdate();
        } catch (SQLException e) {
            logger.error(String.format("Error while searching advertisements by tags in database: %s", e.getMessage()));
            return -1;
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
}
