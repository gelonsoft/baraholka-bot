package baraholkateam.secure_constants;

public final class SecureConstants {

    public static final String CREATE_TABLES = """
        CREATE TABLE IF NOT EXISTS "Advertisement"
        (
            chat_id bigint NOT NULL,
            message_id bigint NOT NULL,
            tags character(200),
            all_text character(2000),
            creation_time bigint,
            next_update_time bigint,
            update_attempt int,
            PRIMARY KEY (chat_id, message_id)
        );""";

    public static final String INSERT_NEW_ADVERTISEMENT = """
            INSERT INTO "Advertisement" VALUES (?, ?, ?, ?, ?, ?, ?);""";

    public static final String USER_ADS = """
            SELECT * FROM "Advertisement"
            WHERE chat_id = ?;
            """;

    public static final String GET_AD_TEXT = """
            SELECT all_text FROM "Advertisement"
            WHERE message_id = ?;
            """;

    public static final String DELETE_AD = """
            DELETE FROM "Advertisement"
            WHERE message_id = ?;
            """;

    public static final String TAGS_SEARCH = """
        SELECT message_id
        FROM "Advertisement"
        WHERE ? <@ regexp_split_to_array(tags, ' ')
        ORDER BY creation_time DESC
        LIMIT ?;""";

    public static final String ASK_ACTUAL_ADVERTISEMENTS = """
        SELECT chat_id, message_id, update_attempt
        FROM "Advertisement"
        WHERE next_update_time <= ?;""";

    public static final String REMOVE_ADVERTISEMENT = """
        DELETE FROM "Advertisement"
        WHERE chat_id = ? AND message_id = ?;""";

    public static final String UPDATE_ATTEMPT_NUMBER = """
        UPDATE "Advertisement"
        SET update_attempt = ?
        WHERE chat_id = ? AND message_id = ?;""";

    public static final String UPDATE_NEXT_UPDATE_TIME = """
        UPDATE "Advertisement"
        SET next_update_time = ?
        WHERE chat_id = ? AND message_id = ?;""";

    public static final String SWEAR_WORD_DETECTOR = "(?iu)\\b((у|[нз]а|(хитро|не)?вз?[ыьъ]|с[ьъ]|(и|ра)[зс]ъ?|(о[тб]|под)[ьъ]?|(.\\B)+?[оаеи])?-?([её]б(?!о[рй])|и[пб][ае][тц]).*?|(н[иеа]|([дп]|верт)о|ра[зс]|з?а|с(ме)?|о(т|дно)?|апч)?-?ху([яйиеёю]|ли(?!ган)).*?|(в[зы]|(три|два|четыре)жды|(н|сук)а)?-?бл(я(?!(х|ш[кн]|мб)[ауеыио]).*?|[еэ][дт]ь?)|(ра[сз]|[зн]а|[со]|вы?|п(ере|р[оие]|од)|и[зс]ъ?|[ао]т)?п[иеё]зд.*?|(за)?п[ие]д[аое]?р(ну.*?|[оа]м|(ас)?(и(ли)?[нщктл]ь?)?|(о(ч[еи])?|ас)?к(ой)|юг)[ауеы]?|манд([ауеыи](л(и[сзщ])?[ауеиы])?|ой|[ао]вошь?(е?к[ауе])?|юк(ов|[ауи])?)|муд([яаио].*?|е?н([ьюия]|ей))|мля([тд]ь)?|лять|([нз]а|по)х|м[ао]л[ао]фь([яию]|[еёо]й))\\b";
}
