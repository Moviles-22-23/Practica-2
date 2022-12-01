package es.ucm.stalos.nonogramas.logic.data;

import java.io.Serializable;

import es.ucm.stalos.nonogramas.logic.enums.GridType;

/**
 * Son los datos que se van a guardar
 */
public class GameData implements Serializable {

    public GameData() {
    }

    /**
     * Index of the last unlocked package
     */
    public int _lastUnlockedPack = 0;
    /**
     * Index of the last unlocked level
     */
    public int _lastUnlockedLevel = 0;
    /**
     * Determine if the game has been quited from GameState
     */
    public boolean _inGame = false;
    /**
     * Index of the package when the app was closed
     */
    public int _currentPackage = 0;
    /**
     * Index of the level when the app was closed.
     */
    public int _currentLevel = 0;
    /**
     * Board type to be saved
     */
    public GridType _gridType = GridType.NONE;
}
