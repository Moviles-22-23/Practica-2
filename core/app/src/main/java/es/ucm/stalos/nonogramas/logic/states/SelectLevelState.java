package es.ucm.stalos.nonogramas.logic.states;


//TODO: Hacer toda la lógica de los paquetes

// LÓGICA CON DATOS GUARDADOS: Consiste en un conjunto de categorías y niveles en los que
// únicamente podremos avanzar al siguiente si hemos completado el nivel actual. Los niveles y
// categorías en este modo son fijos. Para poder tener este modo tenemos que guardar cuantos niveles
// ha desbloqueado el jugador hasta el momento

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.ucm.stalos.androidengine.Engine;
import es.ucm.stalos.androidengine.Font;
import es.ucm.stalos.androidengine.Image;
import es.ucm.stalos.androidengine.State;
import es.ucm.stalos.androidengine.TouchEvent;
import es.ucm.stalos.nonogramas.logic.Assets;
import es.ucm.stalos.nonogramas.logic.data.GameDataSystem;
import es.ucm.stalos.nonogramas.logic.enums.GridType;
import es.ucm.stalos.nonogramas.logic.enums.MyColor;
import es.ucm.stalos.nonogramas.logic.interfaces.ButtonCallback;
import es.ucm.stalos.nonogramas.logic.objects.SelectButton;

public class SelectLevelState extends State {

    protected SelectLevelState(Engine engine, GridType gridType) {
        super(engine);
        _gridType = gridType;
        ((GameDataSystem) _serSystem)._data._inGame = false;
    }

    @Override
    public boolean init() {
        try {
            _engine.swapBannerAdVisibility(false);
            // Texts
            _modeText = "Paquete " + _gridType.getText();
            _textsFont = _graphics.newFont("JosefinSans-Bold.ttf", 25, true);

            // MODE TEXT
            _modeSize[0] = _graphics.getLogWidth();
            _modeSize[1] = _graphics.getLogHeight() * 0.1f;
            _modePos[0] = (int) (_graphics.getLogWidth() - _modeSize[0]);
            _modePos[1] = (int) ((_graphics.getLogHeight() - _modeSize[1]) * 0.09f);

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
                    State selectPackage = new SelectBoardState(_engine, false);
                    _engine.reqNewState(selectPackage);
                    _audio.playSound(Assets.clickSound, 0);
                }
            };

            // BUTTONS
            initSelectLevelButtons();

            // AUDIO
            _audio.playMusic(Assets.menuTheme);

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
        _graphics.clear(Assets.colorSets.get(Assets.currPalette).getSecond());

        // Texts
        _graphics.setColor(MyColor.GREY_SOFT.getValue());
        _graphics.drawCenteredString(_modeText, _modePos, _modeSize, _textsFont);

        // Back Button
        _graphics.setColor(MyColor.BLACK.getValue());
        _graphics.drawImage(_backImage, _backImagePos, _backImageSize);
        _graphics.drawCenteredString(_backText, _backTextPos, _backTextSize, _textsFont);

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
                System.out.println("f");
            }
        }
    }

//-------------------------------------------MISC-------------------------------------------------//

    /**
     * Initialize the buttons to select the levels
     *
     * @throws Exception in case of font creation fails
     */
    private void initSelectLevelButtons() throws Exception {
        Font font = _graphics.newFont("Molle-Regular.ttf", 20, true);
        _selectButtons = new ArrayList<>();

        float minSize = Math.min((_graphics.getLogWidth() * 0.15f), (_graphics.getLogHeight() * 0.15f));
        float[] size = new float[]{minSize, minSize};

        float separation = Math.min((_graphics.getLogWidth() * 0.2f), (_graphics.getLogHeight() * 0.2f));

        int[] pos = new int[2];
        int[] initialPos = new int[2];
        initialPos[0] = (int) (_graphics.getLogWidth() * 0.125f);
        initialPos[1] = (int) (_graphics.getLogHeight() * 0.2f);

        initGridTypesMap();

        for (int i = 0; i < _numLevels; i++) {
            pos[0] = initialPos[0] + (int) ((i % 4) * separation);
            pos[1] = initialPos[1] + (int) ((i / 4) * separation);

            boolean unlocked = _gridType.getValue() < ((GameDataSystem) _serSystem)._data._lastUnlockedPack
                    || i <= ((GameDataSystem) _serSystem)._data._lastUnlockedLevel;

            String text = "" + (i + 1);
            final SelectButton _level = new SelectButton(pos, size, text, font, unlocked);
            // Seleccion de los datos del nivel escogido
            final int aux_i = i;

            _level.setCallback(new ButtonCallback() {
                @Override
                public void doSomething() {
                    int r = _level.getRows();
                    int c = _level.getCols();
                    State gameState = new GameState(_engine, _gridType, _isRandom, aux_i);
                    _engine.reqNewState(gameState);
                    _audio.playSound(Assets.clickSound, 0);
                    _audio.stopMusic();
                }
            });
            _selectButtons.add(_level);
        }
    }

    /**
     * Initializes rows and cols types of the selectButton.
     */
    private void initGridTypesMap() {
        _gridTypes = new HashMap<>();
        _gridTypes.put(0, GridType._4x4);
        _gridTypes.put(1, GridType._5x5);
        _gridTypes.put(2, GridType._10x5);
        _gridTypes.put(3, GridType._8x8);
        _gridTypes.put(4, GridType._10x10);
        _gridTypes.put(5, GridType._15x10);
    }

    //----------------------------------------ATTRIBUTES----------------------------------------------//
    private GridType _gridType;
    private final int _numLevels = 20;

    // Mode
    private boolean _isRandom = false; // Siempre que vayamos a elegir un nivel va a ser false

    // Texts
    private Font _textsFont;

    // Mode Text
    private String _modeText = "Paquete NxM";
    private int[] _modePos = new int[2];
    private float[] _modeSize = new float[2];

    // Back Button
    private final String _backText = "Volver";
    private int[] _backTextPos = new int[2];
    private float[] _backTextSize = new float[2];

    private final Image _backImage = Assets.backArrow;
    private int[] _backImagePos = new int[2];
    private float[] _backImageSize = new float[2];

    private float[] _backButtonSize = new float[2];
    private ButtonCallback _backCallback;

    // SELECT LEVEL BUTTONS
    /**
     * List of all select level buttons
     */
    List<SelectButton> _selectButtons;
    /**
     * Dictionary of information about
     * different grid level types
     */
    Map<Integer, GridType> _gridTypes;
}
