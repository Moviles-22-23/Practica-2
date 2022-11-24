package es.ucm.stalos.nonogramas.logic.states;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.ucm.stalos.androidengine.State;
import es.ucm.stalos.androidengine.Engine;
import es.ucm.stalos.androidengine.Font;
import es.ucm.stalos.androidengine.Image;
import es.ucm.stalos.androidengine.TouchEvent;
import es.ucm.stalos.nonogramas.logic.Assets;
import es.ucm.stalos.nonogramas.logic.enums.GridType;
import es.ucm.stalos.nonogramas.logic.interfaces.ButtonCallback;
import es.ucm.stalos.nonogramas.logic.objects.SelectPackageButton;

public class SelectRandomLevel extends State {
    public SelectRandomLevel(Engine engine) {
        super(engine);
    }

//-----------------------------------------OVERRIDE-----------------------------------------------//

    @Override
    public boolean init() {
        try {
            // Texts
            _textsFont = _graphics.newFont("JosefinSans-Bold.ttf", 25, true);

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
        // Texts
        _graphics.setColor(_greyColor);
        _graphics.drawCenteredString(_modeText, _modePos, _modeSize, _textsFont);
        _graphics.drawCenteredString(_commentText, _commentPos, _commentSize, _textsFont);

        // Back Button
        _graphics.setColor(_blackColor);
        _graphics.drawImage(_backImage, _backImagePos, _backImageSize);
        _graphics.drawCenteredString(_backText, _backTextPos, _backTextSize, _textsFont);

        // SelectLevel buttons
        for (SelectPackageButton button : _selectButtons) {
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
                    for (SelectPackageButton button : _selectButtons) {
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
     * Initialize the buttons to select the levels
     *
     * @throws Exception in case of font creation fails
     */
    private void initSelectLevelButtons() throws Exception {
        _selectButtons = new ArrayList<>();

        float min = Math.min((_graphics.getLogWidth() * 0.2f), (_graphics.getLogHeight() * 0.2f));
        float[] size = new float[]{min, min};

        Font font = _graphics.newFont("Molle-Regular.ttf", 20, true);

        int[] pos = new int[2];

        initGridTypesMap();

        int j = 0;
        for (int i = 0; i < GridType.MAX.getValue(); i++) {
            pos[0] = (int) (_graphics.getLogWidth() * 0.1f) * (1 + (3 * j));
            pos[1] = (int) (_graphics.getLogHeight() * 0.143f) * (3 + (i / 3) * 2);

            final SelectPackageButton _level = new SelectPackageButton(pos, size, _gridTypes.get(i), font, true);
            _level.setCallback(new ButtonCallback() {
                @Override
                public void doSomething() {
                    int r = _level.getRows();
                    int c = _level.getCols();
                    State gameState = new GameStateRandom(_engine, r, c, true);
                    _engine.reqNewState(gameState);
                    _audio.playSound(Assets.clickSound, 0);
                    _audio.stopMusic();
                }
            });
            _selectButtons.add(_level);
            j++;
            if (j == 3) j = 0;
        }
    }

    /**
     * Initializes rows and cols types of the selectButton.
     */
    private void initGridTypesMap() {
        _gridTypes = new HashMap<>();
        _gridTypes.put(0, GridType._4x4);
        _gridTypes.put(1, GridType._5x5);
        _gridTypes.put(2, GridType._5x10);
        _gridTypes.put(3, GridType._8x8);
        _gridTypes.put(4, GridType._10x10);
        _gridTypes.put(5, GridType._10x15);
    }

    //----------------------------------------ATTRIBUTES----------------------------------------------//
    // Texts
    private Font _textsFont;

    // Mode Text
    private String _modeText = "JUEGO ALEATORIO";
    private int[] _modePos = new int[2];
    private float[] _modeSize = new float[2];

    // Comment Text
    private final String _commentText = "Selecciona el tamaño del puzzle";
    private int[] _commentPos = new int[2];
    private float[] _commentSize = new float[2];

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
    List<SelectPackageButton> _selectButtons;
    /**
     * Dictionary of information about
     * different grid level types
     */
    Map<Integer, GridType> _gridTypes;

    // Colors
    private final int _greyColor = 0x313131FF;
    private final int _blackColor = 0x000000FF;
}
