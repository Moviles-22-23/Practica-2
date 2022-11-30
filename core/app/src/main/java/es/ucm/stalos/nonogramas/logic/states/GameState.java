package es.ucm.stalos.nonogramas.logic.states;

import android.provider.ContactsContract;

import java.util.List;

import es.ucm.stalos.androidengine.Engine;
import es.ucm.stalos.androidengine.Font;
import es.ucm.stalos.androidengine.Image;
import es.ucm.stalos.androidengine.State;
import es.ucm.stalos.androidengine.TouchEvent;
import es.ucm.stalos.nonogramas.logic.Assets;
import es.ucm.stalos.nonogramas.logic.data.DataSystem;
import es.ucm.stalos.nonogramas.logic.enums.GridType;
import es.ucm.stalos.nonogramas.logic.enums.PlayingState;
import es.ucm.stalos.nonogramas.logic.interfaces.ButtonCallback;
import es.ucm.stalos.nonogramas.logic.objects.Board;

// PRACTICA 2: Refactorización de los GameState
public class GameState extends State {
    public GameState(Engine engine, GridType gridType, boolean isRandom, int index) {
        super(engine);
        this._gridType = gridType;
        this._isRandom = isRandom;
        this._index = index;
        System.out.println("MODO: " + _isRandom + " ,INDEX: " + _index);
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

        renderButtons();
        renderText();
    }

    @Override
    public void handleInput() {
        List<TouchEvent> events = _engine.getInput().getTouchEvents();
        for (int i = 0; i < events.size(); i++) {
            TouchEvent currEvent = events.get(i);
            if (currEvent == TouchEvent.touchDown) {
                int[] clickPos = {currEvent.getX(), currEvent.getY()};

                // GIVE-UP BUTTON
                if (_playState != PlayingState.Win &&
                        clickInsideSquare(clickPos, _giveupImagePos, _giveupButtonSize)) {
                    _returnCallback.doSomething();
                }
                // BOARD TOUCH-DOWN
                else if (_playState == PlayingState.Gaming &&
                        clickInsideSquare(clickPos, _posBoard, _sizeBoard)) {
                    _board.handleInput(clickPos, TouchEvent.touchDown);
                    _audio.playSound(Assets.clickSound, 0);
                }
                // BACK BUTTON WIN
                else if (_playState == PlayingState.Win &&
                        clickInsideSquare(clickPos, _backImagePos, _backButtonSize)) {
                    _returnCallback.doSomething();
                }
                // ADS
                else if (_playState == PlayingState.GameOver &&
                        clickInsideSquare(clickPos, _adsImagePos, _adsButtonSize))
                    _adsCallback.doSomething();

            } else if (currEvent == TouchEvent.longTouch) {
                int[] clickPos = {currEvent.getX(), currEvent.getY()};
                // BOARD LONG TOUCH
                if (_playState == PlayingState.Gaming &&
                        clickInsideSquare(clickPos, _posBoard, _sizeBoard)) {
                    _board.handleInput(clickPos, TouchEvent.longTouch);
                    //TODO: buscar un sonido para holdClick
                    //_audio.playSound(Assets.clickSound, 0);
                }
            }

            if(_board.getWin()) _playState = PlayingState.Win;
        }
    }


//-------------------------------------------MISC-------------------------------------------------//

    /**
     * Renders every button of the state
     */
    public void renderButtons() {
        _graphics.setColor(_blackColor);
        switch (_playState) {
            case Gaming:
                System.out.println("GAMING");
                // GiveUp Button
                _graphics.drawRect(_giveupImagePos, _giveupButtonSize);
                _graphics.drawImage(_giveupImage, _giveupImagePos, _giveupImageSize);
                _graphics.drawCenteredString(_giveupText, _giveupTextPos, _giveupTextSize, _fontButtons);
                // Life Image
                _graphics.drawImage(_lifeImage, _lifeImagePos, _lifeImageSize);
                _graphics.drawCenteredString(_lifeText, _lifeTextPos, _lifeTextSize, _fontButtons);
                break;
            case Win: {
                System.out.println("WIN");

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

                // TODO: debug borrar
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
                _graphics.drawCenteredString(_winText2, _winPos2, _winSize2, _fontText);
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

        _board = new Board(this, _gridType, _posBoard, _sizeBoard, _isRandom, _lives, _index);
        if (!_board.init(_engine)) throw new Exception("Error al crear el board");
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
        _giveupImageSize[1] = _graphics.getLogHeight() * 0.04f;
        _giveupImagePos[0] = 10;
        _giveupImagePos[1] = 31;

        _giveupTextSize[0] = _graphics.getLogWidth() * 0.2f;
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

        _returnCallback = new ButtonCallback() {
            @Override
            public void doSomething() {
                State previusState;

                if (_isRandom) previusState = new SelectBoard(_engine, true);
                else previusState = new SelectLevelState(_engine, _gridType);

                _engine.reqNewState(previusState);
                _audio.stopMusic();
                _audio.playSound(Assets.clickSound, 0);
            }
        };

        // Life
        _lifeImageSize[0] = _giveupImageSize[0] * 1.5f;
        _lifeImageSize[1] = _giveupImageSize[1] * 1.5f;
        _lifeTextSize[0] = _graphics.getLogWidth() * 0.3f;
        _lifeTextSize[1] = _giveupImageSize[1];

        _lifeImagePos[0] = (int) ((_graphics.getLogWidth() * 0.5f) - _lifeImageSize[0]);
        _lifeImagePos[1] = _giveupImagePos[1];
        _lifeTextPos[0] = (_lifeImagePos[0]);
        _lifeTextPos[1] = _giveupTextPos[1];

        // ADS - GameOver
        _adsImageSize[0] = _graphics.getLogWidth() * 0.17f;
        _adsImageSize[1] = _graphics.getLogHeight() * 0.1f;

        _adsTextSize[0] = _graphics.getLogWidth() * 0.4f;
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

        _winSize2[0] = _graphics.getLogWidth();
        _winSize2[1] = _graphics.getLogHeight() * 0.08f;
        _winPos2[0] = 0;
        _winPos2[1] = (int) (_graphics.getLogHeight() * 0.18f);

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


//----------------------------------------ATTRIBUTES----------------------------------------------//

    // Game Mode
    protected PlayingState _playState = PlayingState.Gaming;
    protected boolean _isRandom;
    private GridType _gridType;

    // Board
    protected Board _board;
    protected int[] _posBoard = new int[2];
    protected float[] _sizeBoard = new float[2];

    // Texts
    protected Font _fontText;

    // Win
    protected final String _winText1 = "ENHORABUENA!";
    protected int[] _winPos1 = new int[2];
    protected float[] _winSize1 = new float[2];

    protected String _winText2 = "Solución original";
    protected int[] _winPos2 = new int[2];
    protected float[] _winSize2 = new float[2];

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
    protected ButtonCallback _returnCallback;

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
//    protected final String _gameOverText = "¡HAS PERDIDO!";
//    protected int[] _gameOverTextPos = new int[2];
//    protected float[] _gameOverTextSize = new float[2];

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

    int _index;
}
