package baraholkateam.util;

import java.util.Map;
import java.util.Objects;

public enum State {
    // TODO добавить новые состояния (команды)
    Start("start", "Старт"),
    Help("help", "Справочная информация по боту"),
    MainMenu("menu", "Главное меню"),
    NewAdvertisement("new_advertisement", "Создание нового объявления"),
    SearchAdvertisements("search_advertisement", "Поиск объявлений по хэштегам"),
    SearchAdvertisements_AddAdvertisementType("add_advertisement_type",
            "Выбор типов объявления для поиска"),
    SearchAdvertisements_AddProductCategories("add_product_categories",
            "Выбор категорий товаров для поиска"),
    SearchAdvertisements_ShowFoundAdvertisements("show_found_advertisements",
            "Вывод найденных объявлений");

    private final String identifier;
    private final String description;
    private static final Map<State, State> NEXT_STATE = Map.of(
            SearchAdvertisements, SearchAdvertisements_AddAdvertisementType,
            SearchAdvertisements_AddAdvertisementType, SearchAdvertisements_AddProductCategories,
            SearchAdvertisements_AddProductCategories, SearchAdvertisements_ShowFoundAdvertisements
    );

    State(String identifier, String description) {
        this.identifier = identifier;
        this.description = description;
    }

    public String getIdentifier() {
        return identifier;
    }

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

    public static State findStateByDescription(String text) {
        for (State state : State.values()) {
            if (Objects.equals(state.getDescription(), text)) {
                return state;
            }
        }
        return null;
    }

    public static State nextState(State currentState) {
        if (currentState == null) {
            return null;
        }
        State nextState = NEXT_STATE.get(currentState);
        return nextState == null ? currentState : nextState;
    }
}
