package baraholkateam.command;

import baraholkateam.util.State;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

import java.util.List;

@Component
public class NonCommand {
    private static final String NO_CURRENT_STATE = """
            Ошибка в текущем состоянии бота.
            Пожалуйста, вернитесь в /%s и следуйте инструкциям.""";
    private static final String COMMAND_ERROR_MESSAGE = """
            Команда /%s не предполагает ввод сообщений.
            Пожалуйста, вернитесь в /%s и следуйте инструкциям.""";
    private static final String UNKNOWN_COMMAND = "Введеное сообщение не понятно боту.";
    private static final String CHOOSE_CITY = "Пожалуйста, выберите город для поиска.";
    private static final String CHOOSE_OBYAVLENIYE_TYPES = "Пожалуйста, выберите типы объявления.";
    private static final String CHOOSE_PRODUCT_CATEGORIES = "Пожалуйста, выберите категории объявлений.";
    private static final String EMPTY_DESCRIPTION = """
            Длина описания превышает допустимый предел в 800 символов или был произведен ввод в некорректной форме.
            Пожалуйста, введите описание еще раз.""";
    private static final String INVALID_PHONE_NUMBER = """
            Номер телефона имеет неверный формат.
            Допустимый формат телефона: +7-xxx-xxx-xx-xx
            Пожалуйста, введите номер телефона еще раз.""";
    private static final String INVALID_SOCIAL = """
            Ссылка на социальную сеть имеет неверный формат.
            Пожалуйста, введите ссылку еще раз.""";
    private static final String INVALID_PRICE = """
            Введенная цена имеет неверный формат.
            Цена должна состоять только из цифр и ее длина не должна превышать 18 символов.
            Пожалуйста, введите цену еще раз.""";

    public List<AnswerPair> nonCommandExecute(Message msg, State currentState) {
        if (currentState == null) {
            return List.of(new AnswerPair(String.format(NO_CURRENT_STATE, State.MainMenu.getIdentifier()), true, null));
        }

        if (currentState.equals(State.Start) || currentState.equals(State.Help) || currentState.equals(State.MainMenu)
                || currentState.equals(State.NewObyavleniye)) {
            return List.of(new AnswerPair(String.format(COMMAND_ERROR_MESSAGE, currentState.getIdentifier(),
                    State.MainMenu.getIdentifier()), true, null));
        } else if (currentState.equals(State.NewObyavleniye_AddDescription)) {
            return List.of(new AnswerPair(EMPTY_DESCRIPTION, true, null));
        } else if (currentState.equals(State.NewObyavleniye_AddPhone)) {
           return List.of(new AnswerPair(INVALID_PHONE_NUMBER, true, null));
        } else if (currentState.equals(State.NewObyavleniye_AddSocial)) {
            return List.of(new AnswerPair(INVALID_SOCIAL, true, null));
        } else if (currentState.equals(State.NewObyavleniye_AddPrice)) {
            return List.of(new AnswerPair(INVALID_PRICE, true, null));
        } else if (currentState.equals(State.SearchObyavleniyes)) {
            return List.of(new AnswerPair(CHOOSE_CITY, true, null));
        } else if (currentState.equals(State.SearchObyavleniyes_AddObyavleniyeTypes)) {
            return List.of(new AnswerPair(CHOOSE_OBYAVLENIYE_TYPES, true, null));
        } else if (currentState.equals(State.SearchObyavleniyes_AddProductCategories)) {
            return List.of(new AnswerPair(CHOOSE_PRODUCT_CATEGORIES, true, null));
        } else if (currentState.equals(State.SearchObyavleniyes_ShowFoundObyavleniyes)) {
            return List.of(new AnswerPair(UNKNOWN_COMMAND, true, null));
        }
        return List.of(new AnswerPair(UNKNOWN_COMMAND, true, null));
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
