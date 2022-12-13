package es.ucm.stalos.nonogramas.logic.states;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.ucm.stalos.androidengine.State;
import es.ucm.stalos.androidengine.Engine;
import es.ucm.stalos.androidengine.TouchEvent;
import es.ucm.stalos.nonogramas.logic.data.GameDataSystem;
import es.ucm.stalos.nonogramas.logic.enums.FontName;
import es.ucm.stalos.nonogramas.logic.enums.GridType;
import es.ucm.stalos.nonogramas.logic.enums.ImageName;
import es.ucm.stalos.nonogramas.logic.enums.MyColor;
import es.ucm.stalos.nonogramas.logic.enums.SoundName;
import es.ucm.stalos.nonogramas.logic.interfaces.ButtonCallback;
import es.ucm.stalos.nonogramas.logic.objects.ColorPalette;
import es.ucm.stalos.nonogramas.logic.objects.SelectButton;

public class SelectBoardState extends State {
    public SelectBoardState(Engine engine, boolean isRandom) {
        super(engine);
        this._isRandom = isRandom;
        ((GameDataSystem) _serSystem)._data._inGame = false;
    }

//-----------------------------------------OVERRIDE-----------------------------------------------//

    @Override
    public boolean init() {
        try {
            _engine.swapBannerAdVisibility(false);

            if (_isRandom) {
                _modeText = "JUEGO ALEATORIO";
                _commentText = "Selecciona el tamaño del puzzle";
            } else {
                _modeText = "MODO HISTORIA";
                _commentText = "Selecciona el paquete";
            }

            // MODE TEXT
            _modeSize[0] = _graphics.getLogWidth();
            _modeSize[1] = _graphics.getLogHeight() * 0.1f;
            _modePos[0] = (int) (_graphics.getLogWidth() - _modeSize[0]);
            _modePos[1] = (int) ((_graphics.getLogHeight() - _modeSize[1]) * 0.18f);

            // Comment Text
            _commentSize[0] = _graphics.getLogWidth();
            _commentSize[1] = _graphics.getLogHeight() * 0.1f;
            _commentPos[0] = (int) (_graphics.getLogWidth() - _commentSize[0]);
            _commentPos[1] = (int) ((_graphics.getLogHeight() - _commentSize[1]) * 0.28f);

            // Back Button
            // Image
            _backImageSize[0] = _graphics.getLogWidth() * 0.072f;
            _backImageSize[1] = _graphics.getLogHeight() * 0.04f;
            _backImagePos[0] = 10;
            _backImagePos[1] = 31;

            // Text
            _backTextSize[0] = _graphics.getLogWidth() * 0.2f;
            _backTextSize[1] = _backImageSize[1];
            _backTextPos[0] = (int) (_backImagePos[0] + _backImageSize[0]);
            _backTextPos[1] = _backImagePos[1];

            // Back Button
            _backButtonSize[0] = _backImageSize[0] + _backTextSize[0];
            _backButtonSize[1] = _backImageSize[1];
            _backCallback = new ButtonCallback() {
                @Override
                public void doSomething() {
                    State mainMenuState = new MainMenuState(_engine);
                    _engine.reqNewState(mainMenuState);
                    _audio.playSound(SoundName.ClickSound.getName(), 0);
                }
            };

            // Buttons
            initSelectButtons();

            // AUDIO
            _audio.playMusic(SoundName.MenuTheme.getName());

        } catch (Exception e) {
            System.out.println("Error init Select Level");
            System.out.println(e);
            return false;
        }

        return true;
    }

    @Override
    public void render() {
        // BackgroundColor
        _graphics.clear(ColorPalette._colorSets.get(ColorPalette._currPalette).y);

        // Texts
        _graphics.setColor(MyColor.LIGHT_GREY.getValue());
        _graphics.drawCenteredString(_modeText, FontName.DefaultFont.getName(),
                _modePos, _modeSize);
        _graphics.drawCenteredString(_commentText, FontName.DefaultFont.getName(),
                _commentPos, _commentSize);

        // Back Button
        _graphics.setColor(MyColor.BLACK.getValue());
        _graphics.drawImage(ImageName.BackArrow.getName(), _backImagePos, _backImageSize);
        _graphics.drawCenteredString(_backText, FontName.DefaultFont.getName(),
                _backTextPos, _backTextSize);

        // SelectLevel buttons
        for (SelectButton button : _selectButtons) {
            button.render(_graphics);
        }
    }

    @Override
    public void handleInput() {
        List<TouchEvent> events = _engine.getInput().getTouchEvents();
        for (int i = 0; i < events.size(); i++) {
            TouchEvent currEvent = events.get(i);
            if (currEvent == TouchEvent.touchDown) {
                int[] clickPos = {currEvent.getX(), currEvent.getY()};

                if (clickInsideSquare(clickPos, _backImagePos, _backButtonSize))
                    _backCallback.doSomething();
                else {
                    for (SelectButton button : _selectButtons) {
                        int[] pos = button.getPos();
                        float[] size = button.getSize();
                        if (clickInsideSquare(clickPos, pos, size)) {
                            button.doSomething();
                        }
                    }
                }
            }
        }
    }

//-------------------------------------------MISC-------------------------------------------------//

    /**
     * Initializes rows and cols types of the selectButton.
     */
    protected void initGridTypesMap() {
        _gridTypes.put(0, GridType._4x4);
        _gridTypes.put(1, GridType._5x5);
        _gridTypes.put(2, GridType._10x5);
        _gridTypes.put(3, GridType._8x8);
        _gridTypes.put(4, GridType._10x10);
        _gridTypes.put(5, GridType._15x10);
    }

    /**
     * Initialize the buttons to select the levels
     */
    private void initSelectButtons() {
        // Calculate buttons dimensions
        float min = Math.min((_graphics.getLogWidth() * 0.2f), (_graphics.getLogHeight() * 0.2f));
        float[] size = new float[]{min, min};

        int[] pos = new int[2];

        initGridTypesMap();

        int j = 0;
        for (int i = 0; i < GridType.MAX.getValue(); i++) {
            pos[0] = (int) (_graphics.getLogWidth() * 0.1f) * (1 + (3 * j));
            pos[1] = (int) (_graphics.getLogHeight() * 0.143f) * (3 + (i / 3) * 2);

            boolean unlocked = _isRandom ||
                    ((GameDataSystem) _serSystem)._data._lastUnlockedPack >= i;

            final GridType _this_gridType = _gridTypes.get(i);
            String text = _gridTypes.get(i).getRows() + " x " + _gridTypes.get(i).getCols();
            final SelectButton _level = new SelectButton(pos, size, text,
                    FontName.RowColNumber.getName(), unlocked);

            _level.setCallback(new ButtonCallback() {
                @Override
                public void doSomething() {
                    int r = _level.getRows();
                    int c = _level.getCols();

                    State gameState;

                    // Dependiendo de si estamos en modo random o no harán cosas distintas
                    if (_isRandom) gameState = new GameState(_engine, _this_gridType,
                            true, 0);
                    else gameState = new SelectLevelState(_engine, _this_gridType);

                    _engine.reqNewState(gameState);
                    _audio.playSound(SoundName.ClickSound.getName(), 0);
                    _audio.stopMusic();
                }
            });
            _selectButtons.add(_level);
            j++;
            if (j == 3) j = 0;
        }
    }

    //----------------------------------------ATTRIBUTES----------------------------------------------//
    // GameMode
    private boolean _isRandom;

    // Mode Text
    protected String _modeText = "JUEGO ALEATORIO";
    protected int[] _modePos = new int[2];
    protected float[] _modeSize = new float[2];

    // Comment Text
    protected String _commentText = "Selecciona el tamaño del puzzle";
    protected int[] _commentPos = new int[2];
    protected float[] _commentSize = new float[2];

    // Back Button
    protected final String _backText = "Volver";
    protected int[] _backTextPos = new int[2];
    protected float[] _backTextSize = new float[2];

    protected int[] _backImagePos = new int[2];
    protected float[] _backImageSize = new float[2];

    protected float[] _backButtonSize = new float[2];
    protected ButtonCallback _backCallback;

    // SELECT LEVEL BUTTONS
    /**
     * List of all select level buttons
     */
    protected List<SelectButton> _selectButtons = new ArrayList<>();
    /**
     * Dictionary of information about
     * different grid level types
     */
    protected Map<Integer, GridType> _gridTypes = new HashMap<>();
}
