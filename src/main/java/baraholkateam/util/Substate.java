package baraholkateam.util;

import java.util.Map;

public enum Substate implements IState {
    // TODO добавить подсостояния для процесса создания объявления и удаления объявления
    AddPhotos(State.NewAdvertisement.getIdentifier(), "Добавление фотографий"),
    AddCity(State.NewAdvertisement.getIdentifier(), "Добавление города"),
    AddDescription(State.NewAdvertisement.getIdentifier(), "Добавление описания"),
    SearchAdvertisements_ChooseAdvertisementType(State.SearchAdvertisements.getIdentifier(),
            "Выбор типа объявления для поиска"),
    SearchAdvertisements_ChooseProductCategories(State.SearchAdvertisements.getIdentifier(),
            "Выбор категорий товаров для поиска"),
    SearchAdvertisements_ShowFoundAdvertisements(State.SearchAdvertisements.getIdentifier(),
            "Выбор интересующего объявления из результатов поиска");

    private final String identifier;
    private final String description;
    // TODO указать переходы между состояниями согласно схеме создания и удаления объявления
    private static final Map<IState, IState> NEXT_SUBSTATE = Map.of(
            State.NewAdvertisement, AddPhotos,
            AddPhotos, AddCity,
            AddCity, AddDescription,
            AddDescription, State.MainMenu,
            State.SearchAdvertisements, SearchAdvertisements_ChooseAdvertisementType,
            SearchAdvertisements_ChooseAdvertisementType, SearchAdvertisements_ChooseProductCategories,
            SearchAdvertisements_ChooseProductCategories, SearchAdvertisements_ShowFoundAdvertisements,
            SearchAdvertisements_ShowFoundAdvertisements, State.MainMenu
    );

    private static final Map<IState, IState> PREV_SUBSTATE = Map.of(
            SearchAdvertisements_ShowFoundAdvertisements, SearchAdvertisements_ChooseProductCategories,
            SearchAdvertisements_ChooseProductCategories, SearchAdvertisements_ChooseAdvertisementType,
            SearchAdvertisements_ChooseAdvertisementType, State.SearchAdvertisements
    );

    Substate(String identifier, String description) {
        this.identifier = identifier;
        this.description = description;
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getDescription() {
        return description;
    }

    public static IState nextSubstate(IState currentSubstate) {
        IState nextSubstate = NEXT_SUBSTATE.get(currentSubstate);
        return nextSubstate == null ? currentSubstate : nextSubstate;
    }

    public static IState prevSubstate(IState currentSubstate) {
        IState prevSubstate = PREV_SUBSTATE.get(currentSubstate);
        return prevSubstate == null ? currentSubstate : prevSubstate;
    }
}
