package es.ucm.stalos.nonogramas.logic.data;

import java.io.Serializable;

import es.ucm.stalos.nonogramas.logic.enums.CellType;
import es.ucm.stalos.nonogramas.logic.enums.GridType;
import es.ucm.stalos.nonogramas.logic.enums.PlayingState;
import es.ucm.stalos.nonogramas.logic.enums.StateType;

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
     * Primary Color
     */
    public int _currPalette;
    /**
     * Current State when the phone toggled portrait-landscape
     */
    public StateType _currStateType = StateType.MainMenuState;
    /**
     * Determine if the current GaeState is random
     */
    public boolean _isRandom = false;
    /**
     * Current status of GameState when the app was closed
     */
    public PlayingState _currentPlayingState;
    /**
     * Index of the package when the app was closed during GameState
     */
    public int _currentPackage = 0;
    /**
     * Index of the level when the app was closed during GameState
     */
    public int _currentLevel = 0;
    /**
     * Number of lives when the game was closed
     */
    public int _currentLives;
    /**
     * Figure name saved when the app was closed during GameState
     */
    public String _currentFigName;
    /**
     * GridType saved when the app was closed during GameState
     */
    public GridType _currGridType = GridType.NONE;
    /**
     * Current board state saved when the app was closed during GameState
     */
    public CellType[][] _currBoardState;
    /**
     * Random solution generated of the board when the app was closed during GameState
     */
    public boolean[][] _randomSol;
}
