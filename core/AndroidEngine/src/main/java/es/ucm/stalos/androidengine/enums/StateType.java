package es.ucm.stalos.androidengine.enums;

import java.io.Serializable;

/**
 * Information about different State's types
 */
public enum StateType implements Serializable  {
    MainMenuState(0),
    SelectBoardState(1),
    SelectLevelState(2),
    GameState(3);

    StateType(int stateType)
    {
        this._stateType = stateType;
    }

    int _stateType;
}
