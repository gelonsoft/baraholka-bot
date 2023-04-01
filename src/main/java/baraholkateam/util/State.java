package baraholkateam.util;

import java.util.Objects;

public enum State implements IState {
    // TODO добавить новые состояния (команды)
    Start("start", "Старт"),
    Help("help", "Справочная информация по боту"),
    MainMenu("menu", "Главное меню"),
    NewAdvertisement("new_advertisement", "Создание нового объявления"),
    SearchAdvertisement("search_advertisement", "Поиск объявлений по хэштегам");

    private final String identifier;
    private final String description;

    State(String identifier, String description) {
        this.identifier = identifier;
        this.description = description;
    }

    @Override
    public String getIdentifier() {
        return identifier;
    }

    @Override
    public String getDescription() {
        return description;
    }

    public static State findState(String text) {
        for (State state : State.values()) {
            if (Objects.equals(state.getIdentifier(), text)) {
                return state;
            }
        }
        return null;
    }
}
