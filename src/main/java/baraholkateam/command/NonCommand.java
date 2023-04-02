package baraholkateam.command;

import baraholkateam.util.State;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

import java.util.List;

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
    private static final String CHOOSE_CITY = "Пожалуйста, выберите город для поиска.";
    private static final String CHOOSE_ADVERTISEMENT_TYPES = "Пожалуйста, выберите типы объявления.";
    private static final String CHOOSE_PRODUCT_CATEGORIES = "Пожалуйста, выберите категории объявлений.";

    public NonCommand() {
    }

    public List<AnswerPair> nonCommandExecute(Message msg, State currentState) {
        if (currentState == null) {
            return List.of(new AnswerPair(String.format(NO_CURRENT_STATE, State.MainMenu.getIdentifier()), true, null));
        }

        if (currentState.equals(State.Start) || currentState.equals(State.Help) || currentState.equals(State.MainMenu)
                || currentState.equals(State.NewAdvertisement)) {
            return List.of(new AnswerPair(String.format(COMMAND_ERROR_MESSAGE, currentState.getIdentifier(),
                    State.MainMenu.getIdentifier()), true, null));
        } else if (currentState.equals(State.SearchAdvertisements)) {
            return List.of(new AnswerPair(CHOOSE_CITY, true, null));
        } else if (currentState.equals(State.SearchAdvertisements_AddAdvertisementType)) {
            return List.of(new AnswerPair(CHOOSE_ADVERTISEMENT_TYPES, true, null));
        } else if (currentState.equals(State.SearchAdvertisements_AddProductCategories)) {
            return List.of(new AnswerPair(CHOOSE_PRODUCT_CATEGORIES, true, null));
        } else if (currentState.equals(State.SearchAdvertisements_ShowFoundAdvertisements)) {
            return List.of(new AnswerPair(UNKNOWN_COMMAND, true, null));
        }
        // TODO добавить обработку всех возможных состояний и подсостояний
        return List.of(new AnswerPair(String.format(UNKNOWN_COMMAND, State.MainMenu.getIdentifier()), true, null));
    }

    /**
     * Хранит ответ бота с индикатором ранее присланного ошибочного сообщения от пользователя.
     */
    public static class AnswerPair {
        private final String answer;
        private final Boolean isError;
        private final ReplyKeyboard inlineKeyboardMarkup;

        public AnswerPair(String answer, boolean isError, ReplyKeyboard inlineKeyboardMarkup) {
            this.answer = answer;
            this.isError = isError;
            this.inlineKeyboardMarkup = inlineKeyboardMarkup;
        }

        public String getAnswer() {
            return answer;
        }

        public Boolean getError() {
            return isError;
        }

        public ReplyKeyboard getReplyKeyboard() {
            return inlineKeyboardMarkup;
        }
    }
}
