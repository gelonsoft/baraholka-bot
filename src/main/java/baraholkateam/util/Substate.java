package baraholkateam.util;

import java.util.Map;

public enum Substate implements IState {
    // TODO добавить подсостояния для процесса создания объявления и удаления объявления
    AddPhotos(State.NewAdvertisement.getIdentifier(), "Добавление фотографий"),
    AddCity(State.NewAdvertisement.getIdentifier(), "Добавление города"),
    AddDescription(State.NewAdvertisement.getIdentifier(), "Добавление описания");

    private final String identifier;
    private final String description;
    // TODO указать переходы между состояниями согласно схеме создания и удаления объявления
    private static final Map<IState, IState> NEXT_SUBSTATE = Map.of(
            State.NewAdvertisement, AddPhotos, // обязательно переходим со команды (стейта) в первый сабстейт
            AddPhotos, AddCity,
            AddCity, AddDescription,
            AddDescription, State.MainMenu // обязательно после завершения последнего сабстейта команды переходим в главное меню
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
}
