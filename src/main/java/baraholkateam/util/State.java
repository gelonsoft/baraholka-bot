package baraholkateam.util;

import com.google.common.collect.ImmutableMap;

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
    private static final Map<State, State> PREVIOUS_STATE = getPreviousState();

    State(String identifier, String description) {
        this.identifier = identifier;
        this.description = description;
    }

    private static Map<State, State> getNextStates() {
        return ImmutableMap.<State, State>builder()
                .put(SearchAdvertisements, SearchAdvertisements_AddAdvertisementTypes)
                .put(SearchAdvertisements_AddAdvertisementTypes, SearchAdvertisements_AddProductCategories)
                .put(SearchAdvertisements_AddProductCategories, SearchAdvertisements_ShowFoundAdvertisements)
                .put(NewAdvertisement, NewAdvertisement_AddPhotos)
                .put(NewAdvertisement_AddPhotos, NewAdvertisement_ConfirmPhoto)
                .put(NewAdvertisement_ConfirmPhoto, NewAdvertisement_AddDescription)
                .put(NewAdvertisement_AddDescription, NewAdvertisement_AddCity)
                .put(NewAdvertisement_AddCity, NewAdvertisement_AddAdvertisementTypes)
                .put(NewAdvertisement_AddAdvertisementTypes, NewAdvertisement_AddCategories)
                .put(NewAdvertisement_AddCategories, NewAdvertisement_AddPrice)
                .put(NewAdvertisement_AddPrice, NewAdvertisement_ConfirmPrice)
                .put(NewAdvertisement_ConfirmPrice, NewAdvertisement_AddContacts)
                .put(NewAdvertisement_AddContacts, NewAdvertisement_AddPhone)
                .put(NewAdvertisement_AddPhone, NewAdvertisement_ConfirmPhone)
                .put(NewAdvertisement_ConfirmPhone, NewAdvertisement_AddSocial)
                .put(NewAdvertisement_AddSocial, NewAdvertisement_Confirm)
                .build();
    }

    private static Map<State, State> getPreviousState() {
        return ImmutableMap.<State, State>builder()
                .put(Start, Start)
                .put(Help, MainMenu)
                .put(MainMenu, MainMenu)
                .put(UserAdvertisements, MainMenu)
                .put(NewAdvertisement, MainMenu)
                .put(DeleteAdvertisement, MainMenu)
                .put(NewAdvertisement_AddPhotos, NewAdvertisement)
                .put(NewAdvertisement_ConfirmPhoto, NewAdvertisement_AddPhotos)
                .put(NewAdvertisement_AddDescription, NewAdvertisement_AddPhotos)
                .put(NewAdvertisement_AddCity, NewAdvertisement_AddDescription)
                .put(NewAdvertisement_AddAdvertisementTypes, NewAdvertisement_AddCity)
                .put(NewAdvertisement_AddCategories, NewAdvertisement_AddAdvertisementTypes)
                .put(NewAdvertisement_AddPrice, NewAdvertisement_AddCity)
                .put(NewAdvertisement_ConfirmPrice, NewAdvertisement_AddPrice)
                .put(NewAdvertisement_AddContacts, NewAdvertisement_AddPrice)
                .put(NewAdvertisement_AddPhone, NewAdvertisement_AddContacts)
                .put(NewAdvertisement_ConfirmPhone, NewAdvertisement_AddPhone)
                .put(NewAdvertisement_AddSocial, NewAdvertisement_AddContacts)
                .put(NewAdvertisement_Confirm, NewAdvertisement_ConfirmPhone)
                .put(SearchAdvertisements, MainMenu)
                .put(SearchAdvertisements_AddAdvertisementTypes, SearchAdvertisements)
                .put(SearchAdvertisements_AddProductCategories, SearchAdvertisements_AddAdvertisementTypes)
                .put(SearchAdvertisements_ShowFoundAdvertisements, SearchAdvertisements_AddProductCategories)
                .build();
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

    public static State previousState(State currentState) {
        if (currentState == null) {
            return null;
        }
        State previousState = PREVIOUS_STATE.get(currentState);
        return previousState == null ? currentState : previousState;
    }
}
