package baraholkateam.command;

import baraholkateam.util.IState;
import baraholkateam.util.State;
import baraholkateam.util.Substate;

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
    public AnswerPair nonCommandExecute(String text, Long chatId, IState currentState) {
        if (currentState == null) {
            return new AnswerPair(String.format(NO_CURRENT_STATE, State.MainMenu.getIdentifier()), true);
        }

        if (currentState.equals(State.Start) || currentState.equals(State.Help) || currentState.equals(State.MainMenu)
                || currentState.equals(State.NewAdvertisement)) {
            return new AnswerPair(String.format(COMMAND_ERROR_MESSAGE, currentState.getIdentifier(),
                    State.MainMenu.getIdentifier()), true);
        } else if (currentState.equals(Substate.AddPhotos)) {
            // TODO сделать обработку добавленных фотографий согласно ТЗ. Может быть также нарушения правил добавления фотографий, тогда нужна соответствующая обработка
            return new AnswerPair("Фотографии успешно отправлены.", false);
        } else if (currentState.equals(Substate.AddCity)) {
            // TODO аналогично предыдущему
            return new AnswerPair("Город успешно добавлен.", false);
        } else if (currentState.equals(Substate.AddDescription)) {
            // TODO аналогично предыдущему
            return new AnswerPair("Описание успешно добавлено.", false);
        }
        // TODO добавить обработку всех возможных состояний и подсостояний
        return new AnswerPair(String.format(UNKNOWN_COMMAND, State.MainMenu.getIdentifier()), true);
    }

    /**
     * Хранит ответ бота с индикатором ранее присланного ошибочного сообщения от пользователя
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
}
