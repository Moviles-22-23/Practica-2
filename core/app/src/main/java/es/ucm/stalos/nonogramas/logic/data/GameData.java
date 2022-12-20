package es.ucm.stalos.nonogramas.logic.data;

import java.io.IOException;
import java.io.Serializable;

import es.ucm.stalos.nonogramas.logic.enums.CellType;
import es.ucm.stalos.nonogramas.logic.enums.GridType;
import es.ucm.stalos.nonogramas.logic.enums.PlayingState;
import es.ucm.stalos.nonogramas.logic.enums.StateType;

/**
 * Son los datos que se van a guardar
 */
public class GameData implements Serializable {

    /**
     * Determines if a de-serialized file is compatible with this class.
     *
     * Maintainers must change this value if and only if the new version
     * of this class is not compatible with old versions. See Sun docs
     * for <a href=http://java.sun.com/products/jdk/1.1/docs/guide
     * /serialization/spec/version.doc.html> details. </a>
     *
     * Not necessary to include in first version of the class, but
     * included here as a reminder of its importance.
     */
    private static final long serialVersionUID =  1234567L;

    public GameData() {
    }

    private void readObject(java.io.ObjectInputStream stream) throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
        // Initialize non primitive data when they are non saved
        // if(_data == null) _data = ...;
    }

    private void writeObject(java.io.ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
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
    public int _currPalette = 0;
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
