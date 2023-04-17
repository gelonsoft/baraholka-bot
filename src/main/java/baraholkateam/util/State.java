package baraholkateam.util;

import baraholkateam.command.NewAdvertisement_AddContacts;
import baraholkateam.command.NewAdvertisement_ConfirmPhone;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public enum State {
    // TODO добавить новые состояния (команды)
    Start("start", "Старт"),
    Help("help", "Справочная информация по боту"),
    MainMenu("menu", "Главное меню"),
    NewAdvertisement("new_advertisement", "Создание нового объявления"),
    NewAdvertisement_AddPhotos("add_photos", "Добавить фотографии"),
    NewAdvertisement_ConfirmPhoto("confirm_photo", "Подтвердить фотографии"),
    NewAdvertisement_AddDescription("add_description", "Добавить описание"),
    NewAdvertisement_ConfirmDescription("confirm_description", "Подтвердить описание"),
    NewAdvertisement_AddTags("add_tags", "Добавить хэштеги"),
    NewAdvertisement_ConfirmCity("confirm_city", "Подтвердить город"),
    NewAdvertisement_AddType("add_type", "Выбрать тип объявления"),
    NewAdvertisement_ConfirmType("confirm_type", "Подтвердить тип объявления"),
    NewAdvertisement_AddCategories("add_categories", "Выбрать категории"),
    NewAdvertisement_ConfirmCategories("confirm_categories", "Подтвердить категории"),
    NewAdvertisement_AddPrice("add_price", "Добавить стоимость"),
    NewAdvertisement_ConfirmPrice("confirm_price", "Подтвердить стоимость"),
    NewAdvertisement_AddContacts("add_contacts", "Добавить контакты"),
    NewAdvertisement_AddPhone("add_phone", "Добавить номер телефона"),
    NewAdvertisement_ConfirmPhone("confirm_phone", "Подтвердить номер телефона"),
    NewAdvertisement_AddSocial("add_social", "Добавить ссылку"),
    NewAdvertisement_Confirm("confirm_ad", "Подтвердить"),
    SearchAdvertisements("search_advertisement", "Поиск объявлений по хэштегам"),
    SearchAdvertisements_AddAdvertisementType("add_advertisement_type",
            "Выбор типов объявления для поиска"),
    SearchAdvertisements_AddProductCategories("add_product_categories",
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
        nextStates.put(SearchAdvertisements, SearchAdvertisements_AddAdvertisementType);
        nextStates.put(SearchAdvertisements_AddAdvertisementType, SearchAdvertisements_AddProductCategories);
        nextStates.put(SearchAdvertisements_AddProductCategories, SearchAdvertisements_ShowFoundAdvertisements);
        nextStates.put(NewAdvertisement, NewAdvertisement_AddPhotos);
        nextStates.put(NewAdvertisement_AddPhotos, NewAdvertisement_ConfirmPhoto);
        nextStates.put(NewAdvertisement_ConfirmPhoto, NewAdvertisement_AddDescription);
        nextStates.put(NewAdvertisement_AddDescription, NewAdvertisement_ConfirmDescription);
        nextStates.put(NewAdvertisement_ConfirmDescription, NewAdvertisement_AddTags);
        nextStates.put(NewAdvertisement_AddTags, NewAdvertisement_ConfirmCity);
        nextStates.put(NewAdvertisement_ConfirmCity, NewAdvertisement_AddType);
        nextStates.put(NewAdvertisement_AddType, NewAdvertisement_ConfirmType);
        nextStates.put(NewAdvertisement_ConfirmType, NewAdvertisement_AddCategories);
        nextStates.put(NewAdvertisement_AddCategories, NewAdvertisement_ConfirmCategories);
        nextStates.put(NewAdvertisement_ConfirmCategories, NewAdvertisement_AddPrice);
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
