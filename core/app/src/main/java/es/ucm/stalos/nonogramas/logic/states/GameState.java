package es.ucm.stalos.nonogramas.logic.states;

import java.util.List;

import es.ucm.stalos.androidengine.Engine;
import es.ucm.stalos.androidengine.State;
import es.ucm.stalos.androidengine.TouchEvent;
import es.ucm.stalos.nonogramas.logic.Assets;
import es.ucm.stalos.nonogramas.logic.data.GameData;
import es.ucm.stalos.nonogramas.logic.data.GameDataSystem;
import es.ucm.stalos.nonogramas.logic.enums.FontName;
import es.ucm.stalos.nonogramas.logic.enums.GridType;
import es.ucm.stalos.nonogramas.logic.enums.ImageName;
import es.ucm.stalos.nonogramas.logic.enums.MyColor;
import es.ucm.stalos.nonogramas.logic.enums.PlayingState;
import es.ucm.stalos.nonogramas.logic.enums.SoundName;
import es.ucm.stalos.nonogramas.logic.interfaces.ButtonCallback;
import es.ucm.stalos.nonogramas.logic.objects.Board;
import es.ucm.stalos.nonogramas.logic.objects.ColorPalette;
import es.ucm.stalos.nonogramas.logic.objects.RewardManager;

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
        _rewardManager = new RewardManager(engine, this);
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
        _rewardManager = new RewardManager(engine, this);
    }

//-----------------------------------------OVERRIDE-----------------------------------------------//

    @Override
    public boolean init() {
        try {
            _engine.swapBannerAdVisibility(false);
            initBoard();
            initButtons();
            initTexts();
            initPalette();

            _audio.playMusic(SoundName.MainTheme.getName());

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
        // Background Color
        _graphics.clear(Assets.colorSets.get(Assets.currPalette).getSecond());

        if (_playState != PlayingState.GameOver) {
            _board.render(_graphics);
        }

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
                    _audio.playSound(SoundName.ClickSound.getName(), 0);
                }

                // BACK BUTTON WIN
                else if (_playState == PlayingState.Win &&
                        clickInsideSquare(clickPos, _backImagePos, _backButtonSize)) {
                    _backCallback.doSomething();
                }

                // ADS
                else if (_lives != MAX_LIVES && (_playState == PlayingState.GameOver || _playState == PlayingState.Gaming) &&
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
        _data._currPalette = Assets.currPalette;
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

//-------------------------------------------INIT-------------------------------------------------//

    /**
     * Initializes the board
     */
    private void initBoard() throws Exception {
        // Create the board
        _posBoard[0] = 20;
        _posBoard[1] = 100;
        _sizeBoard[0] = 360.0f;
        _sizeBoard[1] = 360.0f;

        if (_data._inGame)
            _board = new Board(this, _data, _posBoard, _sizeBoard);
        else
            _board = new Board(this, _gridType, _posBoard, _sizeBoard,
                    _isRandom, _lives, _currentLevel);

        if (!_board.init(_data, _graphics.getLogWidth(),
                _graphics.getLogHeight(), _engine.getAssetManager()))
            throw new Exception("Error al crear el board");
    }

    /**
     * Initializes every button of the state
     */
    private void initButtons() {
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

        _backImagePos[0] = (int) ((_graphics.getLogWidth() - (_backTextSize[0] + _backImageSize[0])) * 0.25f);
        _backImagePos[1] = (int) (_graphics.getLogHeight() * 0.8f);
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
                _audio.playSound(SoundName.ClickSound.getName(), 0);
            }
        };

        // Life
        _lifeImageSize[0] = _graphics.getLogWidth() * 0.1207f;
        _lifeImageSize[1] = _graphics.getLogHeight() * 0.075f;
        _lifeImagePos[0] = (int) ((_graphics.getLogWidth() * 0.7f) - _lifeImageSize[0]);
        _lifeImagePos[1] = (int) (_giveupImagePos[1] - _giveupImageSize[1] / 2);

        // ADS - GameOver
        _adsImageSize[0] = _graphics.getLogWidth() * 0.13f;
        _adsImageSize[1] = _graphics.getLogHeight() * 0.07f;

        _adsLifeImageSize[0] = _graphics.getLogWidth() * 0.104f;
        _adsLifeImageSize[1] = _graphics.getLogHeight() * 0.056f;

        _adsTextSize[0] = _graphics.getLogWidth() * 0.5f;
        _adsTextSize[1] = _adsImageSize[1];

        _adsButtonSize[0] = _adsImageSize[0] + _adsTextSize[0];
        _adsButtonSize[1] = _adsImageSize[1];
        _adsCallback = new ButtonCallback() {
            @Override
            public void doSomething() {
                // TODO: Callback para los anuncios y recuperar vidas
                _rewardManager.showAd();
            }
        };

        _adsImagePos[0] = (int) (_graphics.getLogWidth() * 0.5f - _adsButtonSize[0] * 0.5f);
        _adsImagePos[1] = (int) (_graphics.getLogHeight() * 0.77f);

        _adsLifeImagePos[0] = (int) (_adsImagePos[0] + _adsImageSize[0] * 1.1f);
        _adsLifeImagePos[1] = _adsImagePos[1];

        _adsTextPos[0] = (int) (_adsImagePos[0] + _adsImageSize[0]);
        _adsTextPos[1] = _adsImagePos[1];

        // SHARE
        _shareImageSize[0] = _graphics.getLogWidth() * 0.13f;
        _shareImageSize[1] = _graphics.getLogHeight() * 0.07f;

        _shareImagePos[0] = (int) ((_graphics.getLogWidth() - _shareImageSize[0]) * 0.75f);
        _shareImagePos[1] = (int) (_graphics.getLogHeight() * 0.8f);
    }

    /**
     * Initializes every text of the state
     */
    private void initTexts() {
        // WIN TEXT
        _winSize1[0] = _graphics.getLogWidth();
        _winSize1[1] = _graphics.getLogHeight() * 0.08f;
        _winPos1[0] = 0;
        _winPos1[1] = (int) (_graphics.getLogHeight() * 0.0f);

        // NAME TEXT
        _levelNameSize[0] = _graphics.getLogWidth();
        _levelNameSize[1] = _graphics.getLogHeight() * 0.08f;
        _levelNamePos[0] = 0;
        _levelNamePos[1] = (int) (_graphics.getLogHeight() * 0.1f);

        // GameOver Text
        _gameOverImageSize[0] = _graphics.getLogWidth() * 0.9f;
        _gameOverImageSize[1] = _graphics.getLogHeight() * 0.5f;
        _gameOverImagePos[0] = (int) (_gameOverImageSize[0] * 0.05f);
        _gameOverImagePos[1] = (int) (_gameOverImageSize[1] * 0.5f);
    }

    /**
     * Initializes the palette's selector
     *
     * @throws Exception if the initialization fails
     */
    private void initPalette() throws Exception {
        // TODO leer data de la palette
//        if(_data._inGame)
//            _board = new Board(this, _data, _posBoard, _sizeBoard);
//        else
//            _board = new Board(this, _gridType, _posBoard, _sizeBoard,
//                    _isRandom, _lives, _currentLevel);
        _posColorPalette[0] = 0;
        _posColorPalette[1] = 520;
        _sizeColorPalette[0] = 400.0f;
        _sizeColorPalette[1] = 100.0f;

        _colorPalette = new ColorPalette(_posColorPalette, _sizeColorPalette);

        if (!_colorPalette.init(_data, _graphics.getLogWidth(), _graphics.getLogHeight()))
            throw new Exception("Error al iniciar palette");
    }

//----------------------------------------MAIN-LOOP-----------------------------------------------//

    /**
     * Renders every button of the state
     */
    private void renderButtons() {
        _graphics.setColor(MyColor.BLACK.getValue());
        switch (_playState) {
            case Gaming: {
                // GiveUp Button
                _graphics.setColor(Assets.colorSets.get(Assets.currPalette).getFirst());
                float[] aux = {_giveupImageSize[0] + _giveupTextSize[0], _giveupImageSize[1]};

                _graphics.setColor(0xFF0);
                _graphics.drawImage(ImageName.BackArrow.getName(), _giveupImagePos, _giveupImageSize);
                _graphics.drawCenteredString(_giveupText, FontName.GameStateButton.getName(),
                        _giveupTextPos, _giveupTextSize);

                // Life Image
                for (int i = 0; i < _lives; i++) {
                    int[] pos = new int[]{(int) (_lifeImagePos[0] + (_lifeImageSize[0] + _liveImageMargin) * i), _lifeImagePos[1]};
                    _graphics.drawImage(ImageName.Heart.getName(), pos, _lifeImageSize);
                }

                // Ad button
                _graphics.drawImage(ImageName.Ads.getName(), _adsImagePos, _adsImageSize);
                if (_lives == MAX_LIVES)
                    _graphics.drawImage(ImageName.HeartDisable.getName(),
                            _adsLifeImagePos, _adsLifeImageSize);
                else
                    _graphics.drawImage(ImageName.Heart.getName(),
                            _adsLifeImagePos, _adsLifeImageSize);

                _colorPalette.render(_graphics);
                break;
            }
            case Win: {
                // Back Button
                _graphics.setColor(MyColor.BLACK.getValue());

                _graphics.drawImage(ImageName.BackArrow.getName(), _backImagePos, _backImageSize);
                _graphics.drawCenteredString(_backText, FontName.GameStateButton.getName(),
                        _backTextPos, _backTextSize);

                _graphics.drawImage(ImageName.Share.getName(), _shareImagePos, _shareImageSize);
                break;
            }
            case GameOver:
                // GiveUp Button
                _graphics.setColor(MyColor.BLACK.getValue());

                _graphics.drawImage(ImageName.BackArrow.getName(), _giveupImagePos, _giveupImageSize);
                _graphics.drawCenteredString(_giveupText, FontName.GameStateButton.getName(),
                        _giveupTextPos, _giveupTextSize);
                // Ad button
                _graphics.drawImage(ImageName.Ads.getName(), _adsImagePos, _adsImageSize);
                _graphics.drawCenteredString(_adsText, FontName.GameStateButton.getName(),
                        _adsTextPos, _adsTextSize);
                break;
            default:
                break;
        }
    }

    /**
     * Renders every text of the state
     */
    private void renderText() {
        switch (_playState) {
            case Win:
                // TEXT WIN
                _graphics.setColor(MyColor.BLACK.getValue());
                _graphics.drawCenteredString(_winText1, FontName.GameStateText.getName(),
                        _winPos1, _winSize1);

                // NAME TEXT
                _graphics.setColor(MyColor.BLACK.getValue());
                _graphics.drawCenteredString(_levelNameText, FontName.GameStateText.getName(),
                        _levelNamePos, _levelNameSize);
                break;
            case GameOver:
                _graphics.drawImage(ImageName.GameOver.getName(), _gameOverImagePos, _gameOverImageSize);
                break;
            default:
                break;
        }
    }

//-------------------------------------------MISC-------------------------------------------------//

    /**
     * Creates the hint's font in order to have different
     * sizes of this font every time a board is created.
     * Not all boards have the same hint's font size.
     *
     * @param size Size of the font
     */
    public void createHintFont(int size) throws Exception {
        _graphics.newFont(FontName.HintFont.getName(),
                FontName.HintFont.getFileName(), size, true);
    }

    /**
     * Updates the current lives and the lives text
     *
     * @param lvs updated number of lives
     */
    public void updateLives(int lvs) {
        _lives = lvs;
        if (_lives <= 0)
            _playState = PlayingState.GameOver;
    }

    /**
     * Adds a life to the current level
     */
    public void addLife() {
        if (_lives < MAX_LIVES) {
            _lives++;
            _board.addLife();
        }
    }

    /**
     * Restores de lives value to MAX_LIVES value
     */
    public void restoreLives() {
        switch (_playState) {
            case Gaming:
                addLife();
                break;
            case GameOver:
                _lives = MAX_LIVES;
                _board.restoreLives(_lives);
                _playState = PlayingState.Gaming;
                //TODO: Resetear el tablero
                break;
        }
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
            saveData();
        }
    }

    public void playSound(SoundName sound) {
        _engine.getAudio().playSound(sound.getName(), 0);
    }
//------------------------------------------GET-SET-----------------------------------------------//

    public ColorPalette getColorPalette() {
        return _colorPalette;
    }

    public PlayingState getPlayingState() {
        return _playState;
    }

    public void setPlayingState(PlayingState newPlayingState) {
        _playState = newPlayingState;
    }

    public void setNameText(String n) {
        _levelNameText = n;
    }

//----------------------------------------ATTRIBUTES----------------------------------------------//

    private PlayingState _playState = PlayingState.Gaming;
    private boolean _isRandom;
    private GridType _gridType;
    private int _currentLevel;
    private GameData _data;

    // Board
    private Board _board;
    private int[] _posBoard = new int[2];
    private float[] _sizeBoard = new float[2];

    // Color Palette
    private ColorPalette _colorPalette;
    private int[] _posColorPalette = new int[2];
    private float[] _sizeColorPalette = new float[2];

    // Win
    private final String _winText1 = "ENHORABUENA!";
    private int[] _winPos1 = new int[2];
    private float[] _winSize1 = new float[2];

    // Name
    private String _levelNameText = "RANDOM";
    private int[] _levelNamePos = new int[2];
    private float[] _levelNameSize = new float[2];

    // Give Up Button
    private final String _giveupText = "Rendirse";
    private int[] _giveupTextPos = new int[2];
    private float[] _giveupTextSize = new float[2];

    private int[] _giveupImagePos = new int[2];
    private float[] _giveupImageSize = new float[2];

    private float[] _giveupButtonSize = new float[2];

    // Back Button
    private final String _backText = "Volver";
    private int[] _backTextPos = new int[2];
    private float[] _backTextSize = new float[2];

    private int[] _backImagePos = new int[2];
    private float[] _backImageSize = new float[2];

    private float[] _backButtonSize = new float[2];
    private ButtonCallback _backCallback;

    // PRACTICA 2
    // Life management
    private final int MAX_LIVES = 3;
    private int _lives = 3;
    private final int _liveImageMargin = 3;

    private int[] _lifeImagePos = new int[2];
    private float[] _lifeImageSize = new float[2];

    // GameOver stuff
    private int[] _gameOverImagePos = new int[2];
    private float[] _gameOverImageSize = new float[2];

    // Ads
    private int[] _adsImagePos = new int[2];
    private float[] _adsImageSize = new float[2];

    private int[] _adsLifeImagePos = new int[2];
    private float[] _adsLifeImageSize = new float[2];

    private final String _adsText = "Recuperar vida";
    private int[] _adsTextPos = new int[2];
    private float[] _adsTextSize = new float[2];

    private float[] _adsButtonSize = new float[2];
    private ButtonCallback _adsCallback;
    private RewardManager _rewardManager;

    // Share Button
    private int[] _shareImagePos = new int[2];
    private float[] _shareImageSize = new float[2];
}
