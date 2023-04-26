package baraholkateam.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public enum State {
    Start("start", "Старт"),
    Help("help", "Справочная информация по боту"),
    MainMenu("menu", "Главное меню"),
    UserAdvertisements("my_advertisements", "Созданные пользователем актуальные объявления"),
    NewAdvertisement("new_advertisement", "Создание нового объявления"),
    DeleteAdvertisement("delete_advertisement", "Удаление созданного объявления"),
    NewAdvertisement_AddPhotos("add_photos", "Добавить фотографии"),
    NewAdvertisement_ConfirmPhoto("confirm_photo", "Подтвердить фотографии"),
    NewAdvertisement_AddDescription("add_description", "Добавить описание"),
    NewAdvertisement_AddCity("add_city", "Добавить город"),
    NewAdvertisement_AddAdvertisementTypes("add_advertisement_types", "Выбрать типы объявления"),
    NewAdvertisement_AddCategories("add_categories", "Выбрать категории"),
    NewAdvertisement_AddPrice("add_price", "Добавить стоимость"),
    NewAdvertisement_ConfirmPrice("confirm_price", "Подтвердить стоимость"),
    NewAdvertisement_AddContacts("add_contacts", "Добавить контакты"),
    NewAdvertisement_AddPhone("add_phone", "Добавить номер телефона"),
    NewAdvertisement_ConfirmPhone("confirm_phone", "Подтвердить номер телефона"),
    NewAdvertisement_AddSocial("add_social", "Добавить ссылку"),
    NewAdvertisement_Confirm("confirm_ad", "Подтвердить"),
    SearchAdvertisements("search_advertisement", "Поиск объявлений по хэштегам"),
    SearchAdvertisements_AddAdvertisementTypes("search_advertisement_types",
            "Выбор типов объявления для поиска"),
    SearchAdvertisements_AddProductCategories("search_product_categories",
            "Выбор категорий товаров для поиска"),
    SearchAdvertisements_ShowFoundAdvertisements("show_found_advertisements",
            "Вывод найденных объявлений");

    private final String identifier;
    private final String description;
    private static final Map<State, State> NEXT_STATE = getNextStates();

    State(String identifier, String description) {
        this.identifier = identifier;
        this.description = description;
    }

    public static Map<State, State> getNextStates() {
        Map<State, State> nextStates = new HashMap<>();
        nextStates.put(SearchAdvertisements, SearchAdvertisements_AddAdvertisementTypes);
        nextStates.put(SearchAdvertisements_AddAdvertisementTypes, SearchAdvertisements_AddProductCategories);
        nextStates.put(SearchAdvertisements_AddProductCategories, SearchAdvertisements_ShowFoundAdvertisements);
        nextStates.put(NewAdvertisement, NewAdvertisement_AddPhotos);
        nextStates.put(NewAdvertisement_AddPhotos, NewAdvertisement_ConfirmPhoto);
        nextStates.put(NewAdvertisement_ConfirmPhoto, NewAdvertisement_AddDescription);
        nextStates.put(NewAdvertisement_AddDescription, NewAdvertisement_AddCity);
        nextStates.put(NewAdvertisement_AddCity, NewAdvertisement_AddAdvertisementTypes);
        nextStates.put(NewAdvertisement_AddAdvertisementTypes, NewAdvertisement_AddCategories);
        nextStates.put(NewAdvertisement_AddCategories, NewAdvertisement_AddPrice);
        nextStates.put(NewAdvertisement_AddPrice, NewAdvertisement_ConfirmPrice);
        nextStates.put(NewAdvertisement_ConfirmPrice, NewAdvertisement_AddContacts);
        nextStates.put(NewAdvertisement_AddContacts, NewAdvertisement_AddPhone);
        nextStates.put(NewAdvertisement_AddPhone, NewAdvertisement_ConfirmPhone);
        nextStates.put(NewAdvertisement_ConfirmPhone, NewAdvertisement_AddSocial);
        nextStates.put(NewAdvertisement_AddSocial, NewAdvertisement_Confirm);
        return nextStates;
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
