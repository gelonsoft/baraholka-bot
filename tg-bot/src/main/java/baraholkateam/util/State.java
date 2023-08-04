package baraholkateam.util;

import com.google.common.collect.ImmutableMap;

import java.util.Map;
import java.util.Objects;

public enum State {
    Start("start", "Старт"),
    Help("help", "Справочная информация по боту"),
    MainMenu("menu", "Главное меню"),
    UserObyavleniyes("my_ads", "Созданные пользователем актуальные объявления"),
    NewObyavleniye("new_ads", "Создание нового объявления"),
    DeleteObyavleniye("delete_ad", "Удаление созданного объявления"),
    NewObyavleniye_AddPhotos("add_photos", "Добавить фотографии"),
    NewObyavleniye_ConfirmPhoto("confirm_photo", "Подтвердить фотографии"),
    NewObyavleniye_AddDescription("add_description", "Добавить описание"),
    NewObyavleniye_AddCity("add_city", "Добавить город"),
    NewObyavleniye_AddObyavleniyeTypes("add_ad_types", "Выбрать типы объявления"),
    NewObyavleniye_AddCategories("add_categories", "Выбрать категории"),
    NewObyavleniye_AddPrice("add_price", "Добавить стоимость"),
    NewObyavleniye_ConfirmPrice("confirm_price", "Подтвердить стоимость"),
    NewObyavleniye_AddContacts("add_contacts", "Добавить контакты"),
    NewObyavleniye_AddPhone("add_phone", "Добавить номер телефона"),
    NewObyavleniye_ConfirmPhone("confirm_phone", "Подтвердить номер телефона"),
    NewObyavleniye_AddSocial("add_social", "Добавить ссылку"),
    NewObyavleniye_Confirm("confirm_ad", "Подтвердить"),
    SearchObyavleniyes("search_ads", "Поиск объявлений по хэштегам"),
    SearchObyavleniyes_AddObyavleniyeTypes("search_ads_types",
            "Выбор типов объявления для поиска"),
    SearchObyavleniyes_AddProductCategories("search_product_categories",
            "Выбор категорий товаров для поиска"),
    SearchObyavleniyes_ShowFoundObyavleniyes("show_found_ads",
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
                .put(SearchObyavleniyes, SearchObyavleniyes_AddObyavleniyeTypes)
                .put(SearchObyavleniyes_AddObyavleniyeTypes, SearchObyavleniyes_AddProductCategories)
                .put(SearchObyavleniyes_AddProductCategories, SearchObyavleniyes_ShowFoundObyavleniyes)
                .put(NewObyavleniye, NewObyavleniye_AddPhotos)
                .put(NewObyavleniye_AddPhotos, NewObyavleniye_ConfirmPhoto)
                .put(NewObyavleniye_ConfirmPhoto, NewObyavleniye_AddDescription)
                .put(NewObyavleniye_AddDescription, NewObyavleniye_AddCity)
                .put(NewObyavleniye_AddCity, NewObyavleniye_AddObyavleniyeTypes)
                .put(NewObyavleniye_AddObyavleniyeTypes, NewObyavleniye_AddCategories)
                .put(NewObyavleniye_AddCategories, NewObyavleniye_AddPrice)
                .put(NewObyavleniye_AddPrice, NewObyavleniye_ConfirmPrice)
                .put(NewObyavleniye_ConfirmPrice, NewObyavleniye_AddContacts)
                .put(NewObyavleniye_AddContacts, NewObyavleniye_AddPhone)
                .put(NewObyavleniye_AddPhone, NewObyavleniye_ConfirmPhone)
                .put(NewObyavleniye_ConfirmPhone, NewObyavleniye_AddSocial)
                .put(NewObyavleniye_AddSocial, NewObyavleniye_Confirm)
                .build();
    }

    private static Map<State, State> getPreviousState() {
        return ImmutableMap.<State, State>builder()
                .put(Start, Start)
                .put(Help, MainMenu)
                .put(MainMenu, MainMenu)
                .put(UserObyavleniyes, MainMenu)
                .put(NewObyavleniye, MainMenu)
                .put(DeleteObyavleniye, MainMenu)
                .put(NewObyavleniye_AddPhotos, NewObyavleniye)
                .put(NewObyavleniye_ConfirmPhoto, NewObyavleniye_AddPhotos)
                .put(NewObyavleniye_AddDescription, NewObyavleniye_AddPhotos)
                .put(NewObyavleniye_AddCity, NewObyavleniye_AddDescription)
                .put(NewObyavleniye_AddObyavleniyeTypes, NewObyavleniye_AddCity)
                .put(NewObyavleniye_AddCategories, NewObyavleniye_AddCity)
                .put(NewObyavleniye_AddPrice, NewObyavleniye_AddCity)
                .put(NewObyavleniye_ConfirmPrice, NewObyavleniye_AddPrice)
                .put(NewObyavleniye_AddContacts, NewObyavleniye_AddPrice)
                .put(NewObyavleniye_AddPhone, NewObyavleniye_AddContacts)
                .put(NewObyavleniye_ConfirmPhone, NewObyavleniye_AddContacts)
                .put(NewObyavleniye_AddSocial, NewObyavleniye_AddContacts)
                .put(NewObyavleniye_Confirm, NewObyavleniye_AddContacts)
                .put(SearchObyavleniyes, MainMenu)
                .put(SearchObyavleniyes_AddObyavleniyeTypes, SearchObyavleniyes)
                .put(SearchObyavleniyes_AddProductCategories, SearchObyavleniyes_AddObyavleniyeTypes)
                .put(SearchObyavleniyes_ShowFoundObyavleniyes, SearchObyavleniyes_AddProductCategories)
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
