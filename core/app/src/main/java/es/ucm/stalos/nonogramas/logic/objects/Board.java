package es.ucm.stalos.nonogramas.logic.objects;

// Example
//         1   1
//         1 2 1   1
//         1 2 1 2 1
//        __________
//     5 | X X X X X
//   1 1 | - X - X -
// 1 1 1 | X - X - X
//     1 | - X - - -
//     3 | X X X - -

import android.content.res.AssetManager;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Random;

import es.ucm.stalos.androidengine.Font;
import es.ucm.stalos.androidengine.Graphics;
import es.ucm.stalos.nonogramas.logic.enums.StateType;
import es.ucm.stalos.androidengine.enums.TouchEvent;
import es.ucm.stalos.nonogramas.logic.data.GameData;
import es.ucm.stalos.nonogramas.logic.enums.CellType;
import es.ucm.stalos.nonogramas.logic.enums.FontName;
import es.ucm.stalos.nonogramas.logic.enums.GridType;
import es.ucm.stalos.nonogramas.logic.enums.SoundName;
import es.ucm.stalos.nonogramas.logic.states.GameState;

public class Board {
    /**
     * Constructor of the board
     *
     * @param gridType Number of rows
     * @param pos      Up-Left position
     * @param size     Board size (hints includes)
     */
    public Board(GameState state, GridType gridType, int[] pos, float[] size,
                 boolean isRandom, int levelIndex) {
        // GameState
        this._state = state;
        // Grid Type
        this._gridType = gridType;
        this._rows = gridType.getRows();
        this._cols = gridType.getCols();
        this._sol = new boolean[_rows][_cols];
        this._boardState = new Cell[_rows][_cols];
        this._hintRows = new int[_rows][(int) Math.ceil(_cols / 2.0f)];
        this._hintCols = new int[(int) Math.ceil(_rows / 2.0f)][_cols];
        // Pos & Size
        this._pos = pos;
        this._size = size;
        // IsRandom
        this._isRandom = isRandom;
        // Level Index
        this._levelIndex = levelIndex;

        // Cell Size must be square so we have to use the min between rows and cols
        float maxRowsSize = size[1] * 2 / (_rows * 2 + (int) Math.ceil(_rows / 2.0f));
        float maxColsSize = size[0] * 2 / (_cols * 2 + (int) Math.ceil(_cols / 2.0f));
        _cellSize = Math.min(maxRowsSize, maxColsSize);
        _hintSize = _cellSize / 2;

        // Solo tiene offset si el tablero tiene mas filas que columnas
        if (_rows > _cols)
            _offset = (int) (_size[0] - (_cellSize * _cols + _hintSize * (int) Math.ceil(_rows / 2.0f))) / 2;
        else _offset = 0;
    }

    /**
     * Board constructor to initialize the board from loaded data
     *
     * @param state Reference to the GameState
     * @param pos   Up-Left position
     * @param size  Board size (hints includes)
     * @param data  Data to be loaded
     */
    public Board(GameState state, GameData data, int[] pos, float[] size) {
        // GameState
        this._state = state;
        // Grid Type
        this._gridType = data._currGridType;
        this._rows = _gridType.getRows();
        this._cols = _gridType.getCols();
        this._sol = data._randomSol;
        this._boardState = new Cell[_rows][_cols];
        this._hintRows = new int[_rows][(int) Math.ceil(_cols / 2.0f)];
        this._hintCols = new int[(int) Math.ceil(_rows / 2.0f)][_cols];
        // Pos & Size
        this._pos = pos;
        this._size = size;
        // IsRandom
        this._isRandom = data._isRandom;
        // Level Index
        this._levelIndex = data._currentLevel;

        // Cell Size must be square so we have to use the min between rows and cols
        float maxRowsSize = size[1] * 2 / (_rows * 2 + (int) Math.ceil(_rows / 2.0f));
        float maxColsSize = size[0] * 2 / (_cols * 2 + (int) Math.ceil(_cols / 2.0f));
        _cellSize = Math.min(maxRowsSize, maxColsSize);
        _hintSize = _cellSize / 2;

        // Solo tiene offset si el tablero tiene mas filas que columnas
        if (_rows > _cols)
            _offset = (int) (_size[0] - (_cellSize * _cols + _hintSize * (int) Math.ceil(_rows / 2.0f))) / 2;
        else _offset = 0;
    }

    //-------------------------------------------INIT-------------------------------------------------//
    public boolean init(GameData data, int logW, int logH, AssetManager assetManager) {
        try {
            _fontSize = (int) (_hintSize * 0.9f);
            _state.createHintFont(_fontSize);

            if (data._currStateType != StateType.GameState) {
                if (_isRandom)
                    createRandomSolution();
                else {
                    readSolution(assetManager);
                }
            }

            _figNameSize[0] = logW * 0.7f;
            _figNameSize[1] = logH * 0.1f;
            _figNamePos[0] = (int) ((logW - _figNameSize[0]) * 0.5f);
            _figNamePos[1] = (int) ((logH - _figNameSize[1]) * 0.05f);

            loadLevel(data);
        } catch (Exception e) {
            System.out.println("Error leyendo el mapa");
            return false;
        }
        return true;
    }

    /**
     * Read a levelPack from the assets to
     * make a new gridLevel
     */
    private void readSolution(AssetManager assetManager) {
        try {
            // LevelPack Name
            String name = "levels/levelPack" + _rows + "x" + _cols + ".txt";
            // IFile from the current platform
            InputStream is = assetManager.open(name);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            //
            String line;
            line = br.readLine();
            int numLevels = Integer.parseInt(line);
            Random rn = new Random();
            int levelChoosen = Math.abs(rn.nextInt() % numLevels);

            if (_levelIndex <= numLevels) levelChoosen = _levelIndex;

            // Skip lines to be on the correct level
            for (int i = 0; i < levelChoosen; i++) {
                br.readLine();
                for (int j = 0; j < _rows; j++) {
                    br.readLine();
                }
            }

            // Read lines
            for (int i = 0; i < _rows; i++) {
                line = br.readLine();
                // Process every character as a boolean
                for (int j = 0; j < _cols; j++) {
                    _sol[i][j] = line.charAt(j) != '0';
                }
            }

            // Lee la ultima linea para saber le nombre de la figura
            line = br.readLine();
            if (line != null) {
                _state.setFigureName(line);
            }

        } catch (Exception e) {
            System.out.println("ERROR ANDROID: " + e.getMessage());
            System.out.println("Error reading file");
        }
    }

    /**
     * Generate a random grid
     */
    private void createRandomSolution() {
        // 1. Initialize all solutions to false
        for (int i = 0; i < _rows; i++) {
            for (int j = 0; j < _cols; j++) {
                _sol[i][j] = false;
            }
        }

        Random rn = new Random();
        float percent = 0.70f;
        float total = 0;

        // 2. Randomly set cells to true
        //    until get a 70% filled grid
        while (total / (_rows * _cols) < percent) {
            int i = Math.abs(rn.nextInt() % _rows);
            int j = Math.abs(rn.nextInt() % _cols);
            if (!_sol[i][j]) {
                _sol[i][j] = true;
                total++;
            }
        }
    }

    /**
     * Auxiliar function to load different level stuff
     */
    private void loadLevel(GameData data) {
        loadHintsRows();
        loadHintsCols();
        loadBoardState(data);
    }

    /**
     * Load the hints' row in function of the filled cells in the row
     * <p>
     * Is important to read right to left the row to fill hintsRows
     * - 2 1 | 1 1 0 1 0
     */
    private void loadHintsRows() {
        int cont, auxJ;
        // Each hint's row (Up to Down)
        for (int i = 0; i < _sol.length; i++) {
            // Reset cont and auxJ
            cont = 0;
            auxJ = _hintRows[0].length - 1;

            // Columns (Right to Left)
            for (int j = _sol[0].length - 1; j >= 0; j--) {
                // Plus one to counter
                if (_sol[i][j]) {
                    cont++;
                }
                // Save the counter number as a hint
                else if (cont > 0) {
                    _hintRows[i][auxJ] = cont;
                    cont = 0;
                    auxJ--;
                }
            }
            if (cont > 0) _hintRows[i][auxJ] = cont;
        }
    }

    /**
     * Load the hints' column in function of the filled cells in the column
     */
    private void loadHintsCols() {
        int cont, auxI;

        // Cada columna de las pistas (Left to Right)
        for (int j = 0; j < _sol[0].length; j++) {
            // Reset cont and auxJ
            cont = 0;
            auxI = _hintCols.length - 1;

            // Filas (Down to Up)
            for (int i = _sol.length - 1; i >= 0; i--) {
                // Plus one to counter
                if (_sol[i][j]) {
                    cont++;
                }
                // Save the counter number as a hint
                else if (cont > 0) {
                    _hintCols[auxI][j] = cont;
                    cont = 0;
                    auxI--;
                }
            }
            if (cont > 0) _hintCols[auxI][j] = cont;
        }
    }

    /**
     * Generate every cell of the board calculating the respective position.
     */
    private void loadBoardState(GameData data) {
        int[] cellsPos = new int[2];
        cellsPos[0] = _pos[0] + (int) (_hintSize * _hintRows[0].length);
        cellsPos[1] = _pos[1] + (int) (_hintSize * _hintCols.length);

        int[] pos = new int[2];
        for (int i = 0; i < _rows; i++) {
            pos[1] = cellsPos[1] + (int) (i * _cellSize);
            for (int j = 0; j < _cols; j++) {
                pos[0] = cellsPos[0] + (int) (j * _cellSize) + _offset;
                _boardState[i][j] = new Cell(i, j, pos, _cellSize);
                if (data._currStateType == StateType.GameState)
                    _boardState[i][j].cellType = data._currBoardState[i][j];
            }
        }
    }

    //----------------------------------------MAIN-LOOP-----------------------------------------------//
    public void render(Graphics graphics) {
        switch (_state.getPlayingState()) {
            case Gaming:
                renderGaming(graphics);
                break;
            case Win:
                renderWin(graphics);
                break;
            default:
                break;
        }
    }

    // PRACTICA 2: Cambios en el handleInput para realizar la gestion de las vidas
    public void handleInput(int[] clickPos, TouchEvent touch) {
        for (int i = 0; i < _rows; i++) {
            for (int j = 0; j < _cols; j++) {
                Cell c = _boardState[i][j];
                if (clickInside(clickPos, c.x, c.y, c.size)) {
                    if (touch == TouchEvent.touchDown && !_sol[i][j] ||
                            touch == TouchEvent.longTouch && _sol[i][j]) {
                        _state.playSound(SoundName.FailSound);
                        _state.updateLives(-1);
                        return;
                    } else _state.playSound(SoundName.GoodSound);

                    _boardState[i][j].handleInput(clickPos, touch);

                    // Comprueba si hay victoria
                    if (checkSolution()) {
                        _state.setWinState();
                    }

                    return;
                }
            }
        }
    }

    private void renderGaming(Graphics graphics) {
        // Variable auiliares para pintar
        int[] pos = new int[2];
        float[] size = new float[2];
        String numText;

        // Number Colors
        graphics.setColor(0x000000FF);

        // TOTAL ROWS
        for (int i = 0; i < (_boardState.length + _hintCols.length); i++) {
            // TOTAL COLS
            for (int j = 0; j < (_boardState[0].length + _hintRows[0].length); j++) {
                if (i >= _hintCols.length || j >= _hintRows[0].length) {
                    // Range of hints rows
                    if (i >= _hintCols.length && j < _hintRows[0].length) {
                        // Los 0 no hace falta ponerlos
                        if (_hintRows[i - _hintCols.length][j] != 0) {
                            pos[0] = _pos[0] + (int) (j * _hintSize) + _offset;
                            pos[1] = _pos[1] + (int) ((_hintCols.length * _hintSize)
                                    + ((i - _hintCols.length) * _cellSize));
                            size[0] = _hintSize;
                            size[1] = _cellSize;
                            numText = Integer.toString(_hintRows[i - _hintCols.length][j]);
                            graphics.drawCenteredString(numText, FontName.HintFont.getName(),
                                    pos, size);
                        }
                    }
                    // Range of hints cols
                    else if (i < _hintCols.length && j >= _hintRows[0].length) {
                        if (_hintCols[i][j - _hintRows[0].length] != 0) {
                            pos[0] = _pos[0] + (int) ((_hintRows[0].length * _hintSize) + ((j - _hintRows[0].length) * _cellSize)) + _offset;
                            pos[1] = _pos[1] + (int) (i * _hintSize);
                            size[0] = _cellSize;
                            size[1] = _hintSize;
                            numText = Integer.toString(_hintCols[i][j - _hintRows[0].length]);
                            graphics.drawCenteredString(numText, FontName.HintFont.getName(),
                                    pos, size);
                        }
                    }
                    // Range of board
                    else {
                        _boardState[i - _hintCols.length][j - _hintRows[0].length].render(graphics);
                    }
                }
            }
        }

        // HintsRows Rect
        pos[0] = _pos[0] + _offset;
        pos[1] = _pos[1] + (int) (_hintSize * _hintCols.length);
        size[0] = _hintRows[0].length * _hintSize;
        size[1] = _hintRows.length * _cellSize;
        graphics.drawRect(pos, size);

        // HintCols Rect
        pos[0] = _pos[0] + (int) (_hintSize * _hintRows[0].length) + _offset;
        pos[1] = _pos[1];
        size[0] = _hintCols[0].length * _cellSize;
        size[1] = _hintCols.length * _hintSize;
        graphics.drawRect(pos, size);
    }

    private void renderWin(Graphics graphics) {
        // Cuando hemos ganado
        int oneRowSize = (int) (_size[0] / _rows);
        int oneColSize = (int) (_size[1] / _cols);
        int rowMargin = (int) (((_size[0] / _rows) - _cellSize) * 0.5f);
        int colMargin = (int) (((_size[1] / _cols) - _cellSize) * 0.5f);

        int size = Math.min(oneRowSize, oneColSize);
        int margin = Math.min(rowMargin, colMargin);

        // Aqui dibuja solo la solucion cuando hemos ganado
        for (int i = 0; i < _rows; i++) {
            for (int j = 0; j < _cols; j++) {
                graphics.setColor(ColorPalette._colorSets.get(ColorPalette._currPalette).x);

                int[] solPos = {_pos[0] + size * j + margin + _offset, _pos[1] + size * i + margin};

                if (_sol[i][j])
                    graphics.fillSquare(solPos, _cellSize);
            }
        }
    }

    /**
     * @param clickPos Mouse position
     * @return if the mouse has clicked inside the cell
     */
    private boolean clickInside(int[] clickPos, int x, int y, float size) {
        return (clickPos[0] > x && clickPos[0] < (x + size) &&
                clickPos[1] > y && clickPos[1] < (y + size));
    }

    /**
     * Check if the current board is the solution
     *
     * @return true if it is the original one
     */
    public boolean checkSolution() {
        for (int i = 0; i < _rows; i++) {
            for (int j = 0; j < _cols; j++) {
                if ((_boardState[i][j].cellType == CellType.FILL && !_sol[i][j]) ||
                        (_boardState[i][j].cellType != CellType.FILL && _sol[i][j])) {
                    return false;
                }
            }
        }

        return true;
    }

    //-------------------------------------------MISC-------------------------------------------------//
    public void setPos(int[] newPos) {
        _pos = newPos;
    }

    /**
     * Save the current Board State into
     * a bi-dimensional array of CellType.
     *
     * @return The current state of the board
     */
    public CellType[][] getBoardState() {
        CellType[][] currState = new CellType[_rows][_cols];
        for (int i = 0; i < _rows; i++) {
            for (int j = 0; j < _cols; j++) {
                currState[i][j] = _boardState[i][j].cellType;
            }
        }

        return currState;
    }

    public boolean[][] getBoardSolution() {
        return _sol;
    }

    //----------------------------------------ATTRIBUTES----------------------------------------------//
    /**
     * Figure's name position
     */
    private int[] _figNamePos = new int[2];
    /**
     * Size of the box of the name text
     */
    private float[] _figNameSize = new float[2];
    /**
     * Reference to the GameState
     */
    private GameState _state;
    /**
     * Current grid type
     */
    GridType _gridType;
    /**
     * Number of rows of the grid
     */
    private final int _rows;
    /**
     * Number of columns of the grid
     */
    private final int _cols;
    /**
     * Separation to center the game
     */
    private int _offset;
    /**
     * Array which contains the solution
     */
    private boolean[][] _sol;
    /**
     * Array which contains the info
     * of the rows' numbers
     */
    private int[][] _hintRows;
    /**
     * Array which contains the info
     * of the columns' numbers
     */
    private int[][] _hintCols;
    /**
     * Array of the cells
     */
    private Cell[][] _boardState;
    /**
     * Determines if the current board
     * is random or not
     */
    private boolean _isRandom;
    /**
     * Logic position of the entire grid
     */
    private int[] _pos;
    /**
     * Size of the entire grid
     */
    private float[] _size;
    /**
     * Cell size
     */
    private float _cellSize;
    /**
     * Hints' text size
     */
    private float _hintSize;
    /**
     * Hints' text font
     */
    private Font _hintFont;
    /**
     * Font size
     */
    private int _fontSize;
    /**
     * Index of current level
     */
    private int _levelIndex;
}
