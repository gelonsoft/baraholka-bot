package baraholkateam.secure_constants;

public final class SecureConstants {
    public static final String CREATE_TABLES = """
            CREATE TABLE IF NOT EXISTS "Advertisement"
            (
                chat_id bigint NOT NULL,
                message_id bigint NOT NULL,
                tags character(100),
                price bigint,
                description character(100),
                contacts character(100),
                creation_time bigint,
                next_update_time bigint,
                PRIMARY KEY (chat_id, message_id)
            );""";
    public static final String INSERT_NEW_ADVERTISEMENT = """
            INSERT INTO "Advertisement" VALUES (?, ?, ?, ?, ?, ?, ?, ?);""";
    public static final String TAGS_SEARCH = """
            SELECT message_id
            FROM "Advertisement"
            WHERE ? <@ regexp_split_to_array(tags, ' ')
            LIMIT ?;""";
    public static final String REMOVE_ALL_DATA = """
            DELETE * FROM "Advertisement";""";

    public static final String USER_ADS = """
            SELECT * FROM "Advertisement"
            WHERE chat_id = ?;
            """;
    public static final String DELETE_AD = """
            DELETE FROM "Advertisement"
            WHERE message_id = ?;
            """;
}
