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

import static baraholkateam.secure_constants.SecureConstants.CREATE_TABLES;
import static baraholkateam.secure_constants.SecureConstants.INSERT_NEW_ADVERTISEMENT;
import static baraholkateam.secure_constants.SecureConstants.REMOVE_ALL_DATA;
import static baraholkateam.secure_constants.SecureConstants.TAGS_SEARCH;

public class SQLExecutor {
    private static final Integer SEARCH_LIMIT = 10;
    private final Connection connection;
    private final Logger logger = LoggerFactory.getLogger(SQLExecutor.class);

    public SQLExecutor() {
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
            insertNewAdvertisement.setLong(4, advertisement.getPrice());
            insertNewAdvertisement.setString(5, advertisement.getDescription());
            insertNewAdvertisement.setString(6, String.join("; ", advertisement.getContacts()));
            insertNewAdvertisement.setLong(7, advertisement.getCreationTime());
            insertNewAdvertisement.setLong(8, advertisement.getNextUpdateTime());

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
            tagSearch.setInt(2, SEARCH_LIMIT);

            return tagSearch.executeQuery();
        } catch (SQLException e) {
            logger.error(String.format("Error while searching advertisements by tags in database: %s", e.getMessage()));
            return null;
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
