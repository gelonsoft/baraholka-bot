package baraholkateam.command;

import baraholkateam.database.SQLExecutor;
import baraholkateam.util.Advertisement;
import baraholkateam.util.IState;
import baraholkateam.util.State;
import baraholkateam.util.Substate;
import baraholkateam.util.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class NonCommand {
    private static final String NO_CURRENT_STATE = """
            Ошибка в текущем состоянии бота.
            Пожалуйста, вернитесь в /%s и следуйте инструкциям.""";
    private static final String COMMAND_ERROR_MESSAGE = """
            Команда /%s не предполагает ввод сообщений.
            Пожалуйста, вернитесь в /%s и следуйте инструкциям.""";
    private static final String UNKNOWN_COMMAND = """
            Введеная команда не понятна боту.
            Пожалуйста, вернитесь в /%s и следуйте инструкциям.""";
    private final SQLExecutor sqlExecutor;
    private final Logger logger = LoggerFactory.getLogger(NonCommand.class);
    // TODO убрать заглушку
    private final List<Tag> chosenTags = List.of(
            Tag.Exchange, Tag.Sale, Tag.Belgorod, Tag.Hobby
    );

    public NonCommand(SQLExecutor sqlExecutor) {
        this.sqlExecutor = sqlExecutor;
    }

    public AnswerPair nonCommandExecute(String text, Long chatId, IState currentState) {
        if (currentState == null) {
            return new AnswerPair(String.format(NO_CURRENT_STATE, State.MainMenu.getIdentifier()), true);
        }

        if (currentState.equals(State.Start) || currentState.equals(State.Help) || currentState.equals(State.MainMenu)
                || currentState.equals(State.NewAdvertisement)) {
            return new AnswerPair(String.format(COMMAND_ERROR_MESSAGE, currentState.getIdentifier(),
                    State.MainMenu.getIdentifier()), true);
        } else if (currentState.equals(Substate.AddPhotos)) {
            // TODO сделать обработку добавленных фотографий согласно ТЗ. Может быть также нарушения правил добавления
            //  фотографий, тогда нужна соответствующая обработка
            return new AnswerPair("Фотографии успешно отправлены.", false);
        } else if (currentState.equals(Substate.AddCity)) {
            // TODO аналогично предыдущему
            return new AnswerPair("Город успешно добавлен.", false);
        } else if (currentState.equals(Substate.AddDescription)) {
            // TODO аналогично предыдущему
            return new AnswerPair("Описание успешно добавлено.", false);
        } else if (currentState.equals(Substate.ChooseTags)) {
            // TODO убрать метод-заглушку
            createAds();
            return new AnswerPair(String.format("Выбраны следующие теги: %s",
                    chosenTags.stream().map(Tag::getName).collect(Collectors.joining(", "))), false);
        } else if (currentState.equals(Substate.ChooseAdvertisement)) {
            StringBuilder sb = new StringBuilder();
            ResultSet messageIds = sqlExecutor.tagsSearch(chosenTags);
            while (true) {
                try {
                    if (!messageIds.next()) break;
                    sb.append(messageIds.getLong("message_id")).append("\n");
                } catch (SQLException e) {
                    logger.error(String.format("Cannot handle sql: %s", e.getMessage()));
                    throw new RuntimeException("Failed to handle sql", e);
                }
            }
            sqlExecutor.removeAllData();
            return new AnswerPair(sb.toString(), false);
        }
        // TODO добавить обработку всех возможных состояний и подсостояний
        return new AnswerPair(String.format(UNKNOWN_COMMAND, State.MainMenu.getIdentifier()), true);
    }

    /**
     * Хранит ответ бота с индикатором ранее присланного ошибочного сообщения от пользователя.
     */
    public static class AnswerPair {
        private final String answer;
        private final Boolean isError;

        public AnswerPair(String answer, boolean isError) {
            this.answer = answer;
            this.isError = isError;
        }

        public String getAnswer() {
            return answer;
        }

        public Boolean getError() {
            return isError;
        }
    }

    // TODO убрать метод-заглушку
    private void createAds() {
        Advertisement ad1 = new Advertisement(new ArrayList<>(1), "описание1", List.of(Tag.Exchange, Tag.Sale, Tag.Belgorod, Tag.Hobby), 1000, List.of("контакт 1", "контакт 2"))
                .setChatId(1L)
                .setMessageId(2L)
                .setCreationTime(123L)
                .setNextUpdateTime(456L);
        Advertisement ad2 = new Advertisement(new ArrayList<>(1), "описание2", List.of(Tag.Exchange, Tag.Sale, Tag.Belgorod), 2000, List.of("контакт 3"))
                .setChatId(3L)
                .setMessageId(4L)
                .setCreationTime(789L)
                .setNextUpdateTime(101112L);
        Advertisement ad3 = new Advertisement(new ArrayList<>(1), "описание3", List.of(Tag.Sale, Tag.Belgorod, Tag.Hobby), 3000, List.of("контакт 4", "контакт 5", "контакт 6"))
                .setChatId(5L)
                .setMessageId(6L)
                .setCreationTime(131415L)
                .setNextUpdateTime(161718L);
        Advertisement ad4 = new Advertisement(new ArrayList<>(1), "описание4", List.of(Tag.Exchange, Tag.Sale, Tag.Belgorod, Tag.Hobby, Tag.Books), 4000, new ArrayList<>(1))
                .setChatId(7L)
                .setMessageId(8L)
                .setCreationTime(192021L)
                .setNextUpdateTime(222324L);
        Advertisement ad5 = new Advertisement(new ArrayList<>(1), "описание5", List.of(Tag.Exchange, Tag.Sale, Tag.Belgorod, Tag.Hobby, Tag.Ekaterinburg), 5000, List.of("контакт 7"))
                .setChatId(9L)
                .setMessageId(10L)
                .setCreationTime(252627L)
                .setNextUpdateTime(282930L);

        sqlExecutor.insertNewAdvertisement(ad1);
        sqlExecutor.insertNewAdvertisement(ad2);
        sqlExecutor.insertNewAdvertisement(ad3);
        sqlExecutor.insertNewAdvertisement(ad4);
        sqlExecutor.insertNewAdvertisement(ad5);
    }
}
