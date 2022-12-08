package es.ucm.stalos.nonogramas.logic.states;

import java.util.List;

import es.ucm.stalos.androidengine.Engine;
import es.ucm.stalos.androidengine.Font;
import es.ucm.stalos.androidengine.Image;
import es.ucm.stalos.androidengine.State;
import es.ucm.stalos.androidengine.TouchEvent;
import es.ucm.stalos.nonogramas.logic.Assets;
import es.ucm.stalos.nonogramas.logic.data.GameData;
import es.ucm.stalos.nonogramas.logic.data.GameDataSystem;
import es.ucm.stalos.nonogramas.logic.enums.GridType;
import es.ucm.stalos.nonogramas.logic.enums.PlayingState;
import es.ucm.stalos.nonogramas.logic.interfaces.ButtonCallback;
import es.ucm.stalos.nonogramas.logic.objects.Board;
import es.ucm.stalos.nonogramas.logic.objects.ColorPalette;

// PRACTICA 2: Refactorización de los GameState
public class GameState extends State {
    public GameState(Engine engine, GridType gridType, boolean isRandom, int levelIndex) {
        super(engine);
        // INIT DATA OF THE LEVEL
        _data = ((GameDataSystem) _serSystem)._data;
        _data._currGridType = gridType;
        _data._currentPackage = gridType.getValue();
        _data._currentLevel = levelIndex;
        // INIT ATTRIBUTES
        this._gridType = gridType;
        this._isRandom = isRandom;
        this._currentLevel = levelIndex;
    }

    /**
     * GameState constructor to initialize GameSate from LoadState directly
     *
     * @param engine Reference to the Engine
     */
    public GameState(Engine engine) {
        super(engine);
        _data = ((GameDataSystem) _serSystem)._data;
        this._gridType = _data._currGridType;
        this._isRandom = _data._isRandom;
        this._currentLevel = _data._currentLevel;
        this._lives = _data._currentLives;
    }

//-----------------------------------------OVERRIDE-----------------------------------------------//

    @Override
    public boolean init() {
        try {
            // Board
            initBoard();

            // Buttons
            initButtons();

            // Texts
            initTexts();

            // Color Palette
            initPalette();

            _audio.playMusic(Assets.mainTheme);

        } catch (Exception e) {
            System.out.println("Error init Game State");
            System.out.println(e);
            return false;
        }
        return true;
    }

    @Override
    public void update(double deltaTime) {
    }

    @Override
    public void render() {
        if (_playState != PlayingState.GameOver) {
            _board.render(_graphics);
        }

        _colorPalette.render(_graphics);

        // TODO quitar test

        _graphics.setColor(_colorPalette.getPrimaryColor());
        _graphics.fillSquare(new int[]{50,550}, new float[]{50,50});
        _graphics.setColor(_colorPalette.getSecundaryColor());
        _graphics.fillSquare(new int[]{100,550}, new float[]{50,50});


        renderButtons();
        renderText();
    }

    @Override
    public void handleInput() {
        List<TouchEvent> events = _engine.getInput().getTouchEvents();
        for (int i = 0; i < events.size(); i++) {
            TouchEvent currEvent = events.get(i);
            int[] clickPos = {currEvent.getX(), currEvent.getY()};

            // touchDown
            if (currEvent == TouchEvent.touchDown) {
                // GIVE-UP BUTTON
                if (_playState != PlayingState.Win &&
                        clickInsideSquare(clickPos, _giveupImagePos, _giveupButtonSize)) {
                    _backCallback.doSomething();
                }

                // BOARD TOUCH-DOWN
                else if (_playState == PlayingState.Gaming &&
                        clickInsideSquare(clickPos, _posBoard, _sizeBoard)) {
                    _board.handleInput(clickPos, currEvent);
                    _audio.playSound(Assets.clickSound, 0);
                }

                // BACK BUTTON WIN
                else if (_playState == PlayingState.Win &&
                        clickInsideSquare(clickPos, _backImagePos, _backButtonSize)) {
                    _backCallback.doSomething();
                }

                // ADS
                else if (_playState == PlayingState.GameOver &&
                        clickInsideSquare(clickPos, _adsImagePos, _adsButtonSize))
                    _adsCallback.doSomething();

                // COLOR PALETTE
                else if (clickInsideSquare(clickPos, _posColorPalette, _sizeColorPalette)) {
                    System.out.println("Click en Palette");
                    _colorPalette.handleInput(clickPos, currEvent);
                }
            }

            // longTouch
            else if (currEvent == TouchEvent.longTouch) {
                // BOARD LONG TOUCH
                if (_playState == PlayingState.Gaming &&
                        clickInsideSquare(clickPos, _posBoard, _sizeBoard)) {
                    _board.handleInput(clickPos, currEvent);
                    //TODO: buscar un sonido para holdClick
                    //_audio.playSound(Assets.clickSound, 0);
                }
            }
        }
    }

    @Override
    protected void saveData() {
        _data._inGame = _playState != PlayingState.Win &&
                _playState != PlayingState.GameOver;

        // We only want to save the following data in case of we're still playing:
        // 1. isRandom
        // 2. Lives
        // 3. BoardState
        // 4. Board solution
        if (_data._inGame) {
            _data._isRandom = _isRandom;
            _data._currentLives = _lives;
            _data._currBoardState = _board.getBoardState();
            _data._randomSol = _board.getBoardSolution();
        }

        ((GameDataSystem) _serSystem)._data = _data;
        _serSystem.saveData();
    }

//-------------------------------------------MISC-------------------------------------------------//

    /**
     * Renders every button of the state
     */
    public void renderButtons() {
        _graphics.setColor(_blackColor);
        switch (_playState) {
            case Gaming:
                // GiveUp Button
                _graphics.drawImage(_giveupImage, _giveupImagePos, _giveupImageSize);
                _graphics.drawCenteredString(_giveupText, _giveupTextPos, _giveupTextSize, _fontButtons);

//                // TODO: debug boton de rendirse - borrar en version final
//                _graphics.setColor(_redColor);
//                _graphics.drawRect(_giveupTextPos, _giveupTextSize);
//                _graphics.setColor(_blackColor);
//                _graphics.drawRect(_giveupImagePos, _giveupImageSize);

                // Life Image
                _graphics.drawImage(_lifeImage, _lifeImagePos, _lifeImageSize);
                _graphics.drawCenteredString(_lifeText, _lifeTextPos, _lifeTextSize, _fontButtons);

//                // TODO: debug vidas - borrar en version final
//                _graphics.setColor(_redColor);
//                _graphics.drawRect(_lifeTextPos, _lifeTextSize);
//                _graphics.setColor(_blackColor);
//                _graphics.drawRect(_lifeImagePos, _lifeImageSize);

                break;
            case Win: {
                // Back Button
                _graphics.drawImage(_backImage, _backImagePos, _backImageSize);
                _graphics.drawCenteredString(_backText, _backTextPos, _backTextSize, _fontButtons);
                break;
            }
            case GameOver:
                _graphics.setColor(_blackColor);
                // GiveUp Button
                _graphics.drawImage(_giveupImage, _giveupImagePos, _giveupImageSize);
                _graphics.drawCenteredString(_giveupText, _giveupTextPos, _giveupTextSize, _fontButtons);

                // Ad button
                _graphics.drawImage(_adsImage, _adsImagePos, _adsImageSize);
                _graphics.drawCenteredString(_adsText, _adsTextPos, _adsTextSize, _fontButtons);

                // TODO: debug boton de ads - borrar en version final
//                _graphics.setColor(_redColor);
//                _graphics.drawRect(_adsTextPos, _adsTextSize);
//                _graphics.setColor(_blackColor);
//                _graphics.drawRect(_adsImagePos, _adsButtonSize);
                break;
            default:
                break;
        }
    }

    /**
     * Renders every text of the state
     */
    public void renderText() {
        switch (_playState) {
            case Win:
                // TEXT WIN
                _graphics.setColor(_blackColor);
                _graphics.drawCenteredString(_winText1, _winPos1, _winSize1, _fontText);
                break;
            case GameOver:
                _graphics.drawImage(_gameOverImage, _gameOverImagePos, _gameOverImageSize);
                break;
            default:
                break;
        }
    }

    /**
     * Initializes the board
     */
    public void initBoard() throws Exception {
        // Create the board
        _posBoard[0] = 20;
        _posBoard[1] = 200;
        _sizeBoard[0] = 360.0f;
        _sizeBoard[1] = 360.0f;

        if(_data._inGame)
            _board = new Board(this, _data, _posBoard, _sizeBoard);
        else
            _board = new Board(this, _gridType, _posBoard, _sizeBoard,
                    _isRandom, _lives, _currentLevel);

        if (!_board.init(_engine, _data))
            throw new Exception("Error al crear el board");
    }

    private void initPalette() throws Exception {
        // TODO leer data de la palette
//        if(_data._inGame)
//            _board = new Board(this, _data, _posBoard, _sizeBoard);
//        else
//            _board = new Board(this, _gridType, _posBoard, _sizeBoard,
//                    _isRandom, _lives, _currentLevel);
        _posColorPalette[0] = 0;
        _posColorPalette[1] = 80;
        _sizeColorPalette[0] = 400.0f;
        _sizeColorPalette[1] = 100.0f;

        _colorPalette = new ColorPalette(_posColorPalette, _sizeColorPalette);

        if (!_colorPalette.init(_engine, _data))
            throw new Exception("Error al iniciar palette");
    }

    /**
     * Initializes every button of the state
     *
     * @throws Exception if the initialization fails
     */
    protected void initButtons() throws Exception {
        _fontButtons = _graphics.newFont("JosefinSans-Bold.ttf", 20, true);

        // Give Up
        _giveupImageSize[0] = _graphics.getLogWidth() * 0.071f;
        _giveupImageSize[1] = _graphics.getLogHeight() * 0.05f;
        _giveupImagePos[0] = 10;
        _giveupImagePos[1] = 31;

        _giveupTextSize[0] = _graphics.getLogWidth() * 0.3f;
        _giveupTextSize[1] = _giveupImageSize[1];
        _giveupTextPos[0] = (int) (_giveupImagePos[0] + _giveupImageSize[0]);
        _giveupTextPos[1] = _giveupImagePos[1];

        _giveupButtonSize[0] = _giveupImageSize[0] + _giveupTextSize[0];
        _giveupButtonSize[1] = _giveupImageSize[1];

        // Back
        _backImageSize[0] = _giveupImageSize[1];
        _backImageSize[1] = _giveupImageSize[1];
        _backTextSize[0] = _graphics.getLogWidth() * 0.2f;
        _backTextSize[1] = _giveupImageSize[1];

        _backImagePos[0] = (int) ((_graphics.getLogWidth() - (_backTextSize[0] + _backImageSize[0])) * 0.5f);
        _backImagePos[1] = (int) (_graphics.getLogHeight() * 0.9f);
        _backTextPos[0] = (int) (_backImagePos[0] + _backImageSize[0]);
        _backTextPos[1] = _backImagePos[1];

        _backButtonSize[0] = _backImageSize[0] + _backTextSize[0];
        _backButtonSize[1] = _backImageSize[1];

        _backCallback = new ButtonCallback() {
            @Override
            public void doSomething() {
                State previusState;

                if (_isRandom) previusState = new SelectBoardState(_engine, true);
                else previusState = new SelectLevelState(_engine, _gridType);

                _engine.reqNewState(previusState);
                _audio.stopMusic();
                _audio.playSound(Assets.clickSound, 0);
            }
        };

        // Life
        _lifeImageSize[0] = _graphics.getLogWidth() * 0.1207f;
        _lifeImageSize[1] = _graphics.getLogHeight() * 0.075f;

        _lifeTextSize[0] = _graphics.getLogWidth() * 0.1f;
        _lifeTextSize[1] = _lifeImageSize[1];

        _lifeImagePos[0] = (int) ((_graphics.getLogWidth() * 0.75f) - _lifeImageSize[0]);
        _lifeImagePos[1] = (int) (_giveupImagePos[1] - _giveupImageSize[1] / 2);

        _lifeTextPos[0] = (int) (_lifeImagePos[0] + _lifeImageSize[0]);
        _lifeTextPos[1] = _lifeImagePos[1];

        // ADS - GameOver
        _adsImageSize[0] = _graphics.getLogWidth() * 0.17f;
        _adsImageSize[1] = _graphics.getLogHeight() * 0.1f;

        _adsTextSize[0] = _graphics.getLogWidth() * 0.5f;
        _adsTextSize[1] = _adsImageSize[1];


        _adsButtonSize[0] = _adsImageSize[0] + _adsTextSize[0];
        _adsButtonSize[1] = _adsImageSize[1];
        _adsCallback = new ButtonCallback() {
            @Override
            public void doSomething() {
                // TODO: Callback para los anuncios y recuperar vidas

            }
        };

        _adsImagePos[0] = (int) (_graphics.getLogWidth() * 0.5f - _adsButtonSize[0] * 0.5f);
        _adsImagePos[1] = (int) (_graphics.getLogHeight() * 0.8f);

        _adsTextPos[0] = (int) (_adsImagePos[0] + _adsImageSize[0]);
        _adsTextPos[1] = _adsImagePos[1];
    }

    /**
     * Initializes every text of the state
     *
     * @throws Exception if the initialization fails
     */
    protected void initTexts() throws Exception {
        _fontText = _graphics.newFont("JosefinSans-Bold.ttf", 30, true);
        _fontButtons = _graphics.newFont("JosefinSans-Bold.ttf", 30, true);

        // WIN TEXT
        _winSize1[0] = _graphics.getLogWidth();
        _winSize1[1] = _graphics.getLogHeight() * 0.08f;
        _winPos1[0] = 0;
        _winPos1[1] = (int) (_graphics.getLogHeight() * 0.1f);

        // GameOver Text
        _gameOverImageSize[0] = _graphics.getLogWidth() * 0.9f;
        _gameOverImageSize[1] = _graphics.getLogHeight() * 0.5f;
        _gameOverImagePos[0] = (int) (_gameOverImageSize[0] * 0.05f);
        _gameOverImagePos[1] = (int) (_gameOverImageSize[1] * 0.5f);

        // LIVES TEXT
        _lifeText = "x" + _lives;
    }

    /**
     * Updates the current lives and the lives text
     *
     * @param lvs updated number of lives
     */
    public void updateLives(int lvs) {
        _lives = lvs;
        _lifeText = "x" + _lives;
        if (_lives <= 0)
            _playState = PlayingState.GameOver;
    }

    /**
     * Update the data to be save
     */
    public void updateSaveData() {
        // 1. Check if the currentPackage is the same as the last unlocked package
        // 2. Check if the currentLevel is the same as the last unlocked level
        if (_data._currentPackage == _data._lastUnlockedPack &&
                _data._currentLevel == _data._lastUnlockedLevel) {
            _data._lastUnlockedLevel += 1;
            // Si cambiamos de paquete
            if (_data._lastUnlockedLevel == 20) {
                _data._lastUnlockedPack += 1;
                _data._lastUnlockedLevel = 0;
            }
        }
    }

    public PlayingState getPlayingState() {
        return _playState;
    }

    public void setPlayingState(PlayingState newPlayingState) {
        _playState = newPlayingState;
    }

    public ColorPalette getColorPalette(){
        return _colorPalette;
    }

//----------------------------------------ATTRIBUTES----------------------------------------------//

    // Game Mode
    protected PlayingState _playState = PlayingState.Gaming;
    protected boolean _isRandom;
    private GridType _gridType;

    // Board
    protected Board _board;
    protected int[] _posBoard = new int[2];
    protected float[] _sizeBoard = new float[2];

    // Color Palette
    protected ColorPalette _colorPalette;
    protected int[] _posColorPalette = new int[2];
    protected float[] _sizeColorPalette = new float[2];

    // Texts
    protected Font _fontText;

    // Win
    protected final String _winText1 = "ENHORABUENA!";
    protected int[] _winPos1 = new int[2];
    protected float[] _winSize1 = new float[2];

    // Buttons
    protected Font _fontButtons;

    // Give Up Button
    protected final String _giveupText = "Rendirse";
    protected int[] _giveupTextPos = new int[2];
    protected float[] _giveupTextSize = new float[2];

    protected final Image _giveupImage = Assets.backArrow;
    protected int[] _giveupImagePos = new int[2];
    protected float[] _giveupImageSize = new float[2];

    protected float[] _giveupButtonSize = new float[2];

    // Back Button
    protected final String _backText = "Volver";
    protected int[] _backTextPos = new int[2];
    protected float[] _backTextSize = new float[2];

    protected final Image _backImage = Assets.backArrow;
    protected int[] _backImagePos = new int[2];
    protected float[] _backImageSize = new float[2];

    protected float[] _backButtonSize = new float[2];
    protected ButtonCallback _backCallback;

    // Colors
    protected final int _blackColor = 0x000000FF;
    protected final int _redColor = 0xFF0000FF;

    // PRACTICA 2
    // Life management
    protected int _lives = 3;
    protected String _lifeText = "xX";
    protected int[] _lifeTextPos = new int[2];
    protected float[] _lifeTextSize = new float[2];

    protected final Image _lifeImage = Assets.heart;
    protected int[] _lifeImagePos = new int[2];
    protected float[] _lifeImageSize = new float[2];

    // GameOver stuff
    protected final Image _gameOverImage = Assets.gameOver;
    protected int[] _gameOverImagePos = new int[2];
    protected float[] _gameOverImageSize = new float[2];

    // Ads
    protected final Image _adsImage = Assets.ads;
    protected int[] _adsImagePos = new int[2];
    protected float[] _adsImageSize = new float[2];

    protected final String _adsText = "Recuperar vidas";
    protected int[] _adsTextPos = new int[2];
    protected float[] _adsTextSize = new float[2];

    protected float[] _adsButtonSize = new float[2];
    protected ButtonCallback _adsCallback;

    private int _currentLevel;

    private GameData _data;
}
