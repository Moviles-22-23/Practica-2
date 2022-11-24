package es.ucm.stalos.nonogramas.logic.states;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import es.ucm.stalos.androidengine.TouchEvent;
import es.ucm.stalos.androidengine.State;
import es.ucm.stalos.androidengine.Engine;
import es.ucm.stalos.androidengine.Font;
import es.ucm.stalos.androidengine.Image;
import es.ucm.stalos.nonogramas.DataSystem;
import es.ucm.stalos.nonogramas.logic.Assets;
import es.ucm.stalos.nonogramas.logic.enums.PlayingState;
import es.ucm.stalos.nonogramas.logic.interfaces.ButtonCallback;
import es.ucm.stalos.nonogramas.logic.objects.Board;


// TODO: REWARDED VIDEO, SISTEMA DE VIDAS, PALETA DE COLORES

// REWARDED VIDEO: Los vídeos recompensados serán opcionales para el jugador, dándole ventajas sobre el
//juego si deciden verlos. Para más información sobre cómo recompensamos al jugador leer
//la subsección siguiente, Retención y recompensas


// SISTEMA DE VIDAS: Implementar la lógica del sistema de vidas para que no pueda colocar nuevas
// casillas hasta tener vidas de nuevo. Añadir apartado gráfico para las vidas (corazones o lo que sea)

// PALETA DE COLORES: crearemos un conjunto de características que les permite customizar la vista
// del juego y la paleta de colores del tablero.

// ENUNCIADO: Por ello crearemos un sistema de recompensas que permite conseguir las paletas de
//colores y a su vez que nos de la opción a ganar también vidas (?)

public class GameStateRandom extends State {

    public GameStateRandom(Engine engine, int rows, int columns, boolean isRandom) {
        super(engine);
        this._rows = rows;
        this._cols = columns;
        this._isRandom = isRandom;
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
        _lifeText = "x" + _lives;
    }

    @Override
    public void render() {
        _board.render(_graphics);
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

                /// GIVE-UP BUTTON
                if (_playState == PlayingState.Gaming && clickInsideSquare(clickPos, _giveupImagePos, _giveupButtonSize))
                    _giveupCallback.doSomething();
                    // CHECK BUTTON
                else if (_playState == PlayingState.Gaming && clickInsideSquare(clickPos, _checkImagePos, _checkButtonSize))
                    _checkCallback.doSomething();
                    // BOARD INPUT
                else if (_playState != PlayingState.Win && clickInsideSquare(clickPos, _posBoard, _sizeBoard)) {
                    if (_playState == PlayingState.Checking && _timer != null && _timerTask != null) {
                        _timerTask.run();
                        _timer.cancel();
                        _timerTask = null;
                        _timer = null;
                    }

                    _board.handleInput(clickPos, TouchEvent.touchDown);
                    _audio.playSound(Assets.clickSound, 0);
                }
                // BACK BUTTON WIN
                else if (_playState == PlayingState.Win && clickInsideSquare(clickPos, _backImagePos, _backButtonSize))
                    _backCallback.doSomething();
            } else if (currEvent == TouchEvent.longTouch) {
                int[] clickPos = {currEvent.getX(), currEvent.getY()};
                if (_playState != PlayingState.Win && clickInsideSquare(clickPos, _posBoard, _sizeBoard)) {
                    if (_playState == PlayingState.Checking && _timer != null && _timerTask != null) {
                        _timerTask.run();
                        _timer.cancel();
                        _timerTask = null;
                        _timer = null;
                    }

                    _board.handleInput(clickPos, TouchEvent.longTouch);
                    //TODO: buscar un sonido para holdClick
                    //_audio.playSound(Assets.clickSound, 0);
                }
            }
        }
    }

//-------------------------------------------MISC-------------------------------------------------//

    private void initButtons() throws Exception {
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
        _giveupCallback = new ButtonCallback() {
            @Override
            public void doSomething() {
                State selectLevel;
                // Story mode
                // TODO: En lugar de volver a StoryPackage hay que volver a PackageLevel
                //  para ello habría que guardar el paquete que se está jugando actualmente
                //  porque el SelectPackageLevel pedirá como parámetro el paquete a cargar
                if (!_isRandom)
                    selectLevel = new SelectStoryPackage(_engine);
                    // Random mode
                else
                    selectLevel = new SelectRandomLevel(_engine);
                _engine.reqNewState(selectLevel);
                _audio.stopMusic();
                _audio.playSound(Assets.clickSound, 0);
            }
        };

        // Check
        _checkTextSize[0] = _graphics.getLogWidth() * 0.3f;
        _checkTextSize[1] = _giveupImageSize[1];
        _checkTextPos[0] = (int) (_graphics.getLogWidth() - _checkTextSize[0]);
        _checkTextPos[1] = _giveupImagePos[1];

        _checkImageSize[0] = _giveupImageSize[0];
        _checkImageSize[1] = _giveupImageSize[1];
        _checkImagePos[0] = (int) (_graphics.getLogWidth() - _checkTextSize[0] - _checkImageSize[0]);
        _checkImagePos[1] = _giveupImagePos[1];

        _checkButtonSize[0] = _checkImageSize[0] + _checkTextSize[0];
        _checkButtonSize[1] = _checkImageSize[1];
        _checkCallback = new ButtonCallback() {
            @Override
            public void doSomething() {
                // At first it checks the original solution
                if (_board.checkOriginalSolution()) {
                    _playState = PlayingState.Win;
                    _board.setPos(new int[]{_posBoard[0], _posBoard[1] - 50});
                    _board.setWin(true);
                    _audio.playSound(Assets.winSound, 0);
                }
                // Then check for another one
                else if (_board.checkAnotherSolution()) {
                    _playState = PlayingState.Win;
                    _winText2 = "Otra solución";
                    _board.setPos(new int[]{_posBoard[0], _posBoard[1] - 50});
                    _board.setWin(true);
                    _audio.playSound(Assets.winSound, 0);
                } else {
                    _playState = PlayingState.Checking;
                    showText();
                }
                _audio.playSound(Assets.clickSound, 0);
            }
        };

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
                State selectLevel;
                // Story mode
                // TODO: En lugar de volver a StoryPackage hay que volver a PackageLevel
                //  para ello habría que guardar el paquete que se está jugando actualmente
                //  porque el SelectPackageLevel pedirá como parámetro el paquete a cargar
                if (!_isRandom)
                    selectLevel = new SelectStoryPackage(_engine);
                    // Random mode
                else
                    selectLevel = new SelectRandomLevel(_engine);

                _engine.reqNewState(selectLevel);
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
        _lifeTextPos[0] = (int) (_lifeImagePos[0]);
        _lifeTextPos[1] = _giveupTextPos[1];

    }

    private void initTexts() throws Exception {
        _fontText = _graphics.newFont("JosefinSans-Bold.ttf", 30, true);

        // TEXT HINTS
        _hintSize1[0] = _graphics.getLogWidth();
        _hintSize1[1] = _graphics.getLogHeight() * 0.08f;
        _hintPos1[0] = 0;
        _hintPos1[1] = (int) (_graphics.getLogHeight() * 0.1f);

        _hintSize2[0] = _graphics.getLogWidth();
        _hintSize2[1] = _graphics.getLogHeight() * 0.08f;
        _hintPos2[0] = 0;
        _hintPos2[1] = (int) (_graphics.getLogHeight() * 0.18f);

        // WIN TEXT
        _winSize1[0] = _graphics.getLogWidth();
        _winSize1[1] = _graphics.getLogHeight() * 0.08f;
        _winPos1[0] = 0;
        _winPos1[1] = (int) (_graphics.getLogHeight() * 0.1f);

        _winSize2[0] = _graphics.getLogWidth();
        _winSize2[1] = _graphics.getLogHeight() * 0.08f;
        _winPos2[0] = 0;
        _winPos2[1] = (int) (_graphics.getLogHeight() * 0.18f);

        _lifeText = "x" + _lives;
    }

    public void renderButtons() {
        _graphics.setColor(_blackColor);
        switch (_playState) {
            case Gaming:
                // GiveUp Button
                _graphics.drawImage(_giveupImage, _giveupImagePos, _giveupImageSize);
                _graphics.drawCenteredString(_giveupText, _giveupTextPos, _giveupTextSize, _fontButtons);
                // Check Button
                _graphics.drawImage(_checkImage, _checkImagePos, _checkImageSize);
                _graphics.drawCenteredString(_checkText, _checkTextPos, _checkTextSize, _fontButtons);
                // Life Image
                _graphics.drawImage(_lifeImage, _lifeImagePos, _lifeImageSize);
                _graphics.drawCenteredString(_lifeText, _lifeTextPos, _lifeTextSize, _fontButtons);
                break;
            case Win: {
                // Back Button
                _graphics.drawImage(_backImage, _backImagePos, _backImageSize);
                _graphics.drawCenteredString(_backText, _backTextPos, _backTextSize, _fontButtons);
                break;
            }
        }
    }

    public void renderText() {
        switch (_playState) {
            case Checking:
                // HINTS TEXT
                _graphics.setColor(_redColor);
                _graphics.drawCenteredString(_hintsText1, _hintPos1, _hintSize1, _fontText);
                _graphics.setColor(_redColor);
                _graphics.drawCenteredString(_hintsText2, _hintPos2, _hintSize2, _fontText);
                break;
            case Win:
                // TEXT WIN
                _graphics.setColor(_blackColor);
                _graphics.drawCenteredString(_winText1, _winPos1, _winSize1, _fontText);
                _graphics.drawCenteredString(_winText2, _winPos2, _winSize2, _fontText);
                break;
            default:
                break;
        }
    }

    public void initBoard() throws Exception {
        // Create the board
        _posBoard[0] = 20;
        _posBoard[1] = 200;
        _sizeBoard[0] = 360.0f;
        _sizeBoard[1] = 360.0f;

        _board = new Board(_rows, _cols, _posBoard, _sizeBoard, _isRandom, _lives);
        if (!_board.init(_engine)) throw new Exception("Error al crear el board");
    }

    private void showText() {
        _playState = PlayingState.Checking;
        // ATTRIBUTES
        int[] mistakes = _board.countMistakes();

        if (mistakes[0] == 0) _hintsText1 = "No te faltan casillas";
        else if (mistakes[0] == 1) _hintsText1 = "Te falta " + mistakes[0] + " casilla";
        else _hintsText1 = "Te faltan " + mistakes[0] + " casillas";

        if (mistakes[1] == 0) _hintsText2 = "No tienes casillas mal";
        else if (mistakes[1] == 1) _hintsText2 = "Tienes mal " + mistakes[1] + " casilla";
        else _hintsText2 = "Tienes mal " + mistakes[1] + " casillas";

        // TIMER
        _timer = new Timer();
        _timeDelay = 3000;
        _timerTask = new TimerTask() {
            @Override
            public void run() {
                _board.resetWrongCells();
                _playState = PlayingState.Gaming;
            }
        };
        _timer.schedule(_timerTask, _timeDelay);
    }

//----------------------------------------ATTRIBUTES----------------------------------------------//

    // Game Mode
    private PlayingState _playState = PlayingState.Gaming;
    private boolean _isRandom;

    // Atributos del estado
    private int _rows;
    private int _cols;

    // Board
    private Board _board;
    private int[] _posBoard = new int[2];
    private float[] _sizeBoard = new float[2];

    // Texts
    private Font _fontText;

    private String _hintsText1 = "Te faltan x casillas";
    private int[] _hintPos1 = new int[2];
    private float[] _hintSize1 = new float[2];

    private String _hintsText2 = "Tienes mal x casillas";
    private int[] _hintPos2 = new int[2];
    private float[] _hintSize2 = new float[2];

    private final String _winText1 = "ENHORABUENA!";
    private int[] _winPos1 = new int[2];
    private float[] _winSize1 = new float[2];

    private String _winText2 = "Solución original";
    private int[] _winPos2 = new int[2];
    private float[] _winSize2 = new float[2];

    // Buttons
    private Font _fontButtons;

    // Give Up Button
    private final String _giveupText = "Rendirse";
    private int[] _giveupTextPos = new int[2];
    private float[] _giveupTextSize = new float[2];

    private final Image _giveupImage = Assets.backArrow;
    private int[] _giveupImagePos = new int[2];
    private float[] _giveupImageSize = new float[2];

    private float[] _giveupButtonSize = new float[2];
    private ButtonCallback _giveupCallback;

    // Check Button
    private final String _checkText = "Comprobar";
    private int[] _checkTextPos = new int[2];
    private float[] _checkTextSize = new float[2];

    private final Image _checkImage = Assets.lens;
    private int[] _checkImagePos = new int[2];
    private float[] _checkImageSize = new float[2];

    private float[] _checkButtonSize = new float[2];
    private ButtonCallback _checkCallback;

    // Back Button
    private final String _backText = "Volver";
    private int[] _backTextPos = new int[2];
    private float[] _backTextSize = new float[2];

    private final Image _backImage = Assets.backArrow;
    private int[] _backImagePos = new int[2];
    private float[] _backImageSize = new float[2];

    private float[] _backButtonSize = new float[2];
    private ButtonCallback _backCallback;

    // Life management
    private int _lives = 3;
    private String _lifeText = "xX";
    private int[] _lifeTextPos = new int[2];
    private float[] _lifeTextSize = new float[2];

    private final Image _lifeImage = Assets.heart;
    private int[] _lifeImagePos = new int[2];
    private float[] _lifeImageSize = new float[2];

    // Colors
    private final int _blackColor = 0x000000FF;
    private final int _redColor = 0xFF0000FF;
}
