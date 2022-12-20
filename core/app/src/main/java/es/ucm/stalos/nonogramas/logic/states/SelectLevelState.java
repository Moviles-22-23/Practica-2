package es.ucm.stalos.nonogramas.logic.states;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.ucm.stalos.androidengine.Constrain;
import es.ucm.stalos.androidengine.Engine;
import es.ucm.stalos.androidengine.State;
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

            togglePortraitLandscape(_engine.isLandScape());

            // Callback
            _backCallback = new ButtonCallback() {
                @Override
                public void doSomething() {
                    State selectPackage = new SelectBoardState(_engine, false);
                    _engine.reqNewState(selectPackage);
                    _audio.playSound(SoundName.ClickSound.getName(), 0);
                }
            };

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
        _graphics.setColor(MyColor.LIGHT_GREY.get_color());
        _graphics.drawCenteredString(_modeText, FontName.SelectStateTitle.getName(),
                _modePos, _modeSize);

        // Back Button
        _graphics.setColor(ColorPalette._colorSets.get(ColorPalette._currPalette).x);
        if (ColorPalette._currPalette == 0) _graphics.drawRect(_backImagePos, _backButtonSize);
        else _graphics.fillSquare(_backImagePos, _backButtonSize);
        _graphics.setColor(MyColor.BLACK.get_color());
        _graphics.drawImage(ImageName.BackArrow.getName(), _backImagePos, _backImageSize);
        _graphics.drawCenteredString(_backText, FontName.SelectStateButton.getName(), _backTextPos, _backTextSize);

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

    @Override
    protected void togglePortraitLandscape(boolean isLandscape) {

        if(!isLandscape){
            // Mode Text
            _modeSize[0] = _graphics.getLogWidth();
            _modeSize[1] = _graphics.getLogHeight() * 0.1f;
            _modePos = _graphics.constrainedToScreenPos(Constrain.TOP, _modeSize, new int[]{ 0, (int) (_graphics.getLogHeight() * 0.1f) });

            // Back Button
            // Image
            _backImageSize[0] = _graphics.getLogWidth() * 0.108f;
            _backImageSize[1] = _graphics.getLogHeight() * 0.06f;
            _backImagePos = _graphics.constrainedToScreenPos(Constrain.TOP_LEFT, _backImageSize, new int[] { 0, 0 });

            // Text
            _backTextSize[0] = _graphics.getLogWidth() * 0.3f;
            _backTextSize[1] = _backImageSize[1];
            _backTextPos = _graphics.constrainedToObjectPos(Constrain.LEFT, _backImagePos, _backImageSize, _backTextSize, new int[] { 0, 0 });

            // Back Button
            _backButtonSize[0] = _backImageSize[0] + _backTextSize[0];
            _backButtonSize[1] = _backImageSize[1];
        }
        else{
            // Mode Text
            _modeSize[0] = _graphics.getLogWidth();
            _modeSize[1] = _graphics.getLogHeight() * 0.1f;
            _modePos = _graphics.constrainedToScreenPos(Constrain.TOP, _modeSize, new int[]{ 0, (int) (_graphics.getLogHeight() * 0.1f) });

            // Back Button
            // Image
            _backImageSize[0] = _graphics.getLogWidth() * 0.06f;
            _backImageSize[1] = _graphics.getLogHeight() * 0.108f;
            _backImagePos = _graphics.constrainedToScreenPos(Constrain.TOP_LEFT, _backImageSize, new int[] { 0, 0 });

            // Text
            _backTextSize[0] = _graphics.getLogWidth() * 0.15f;
            _backTextSize[1] = _backImageSize[1];
            _backTextPos = _graphics.constrainedToObjectPos(Constrain.LEFT, _backImagePos, _backImageSize, _backTextSize, new int[] { 0, 0 });

            // Back Button
            _backButtonSize[0] = _backImageSize[0] + _backTextSize[0];
            _backButtonSize[1] = _backImageSize[1];
        }

        initSelectLevelButtons(isLandscape);
    }

    //-------------------------------------------MISC-------------------------------------------------//

    /**
     * Initialize the buttons to select the levels
     */
    private void initSelectLevelButtons(boolean isLandscape) {
        int numButtonsAxisX, numButtonsAxisY;
        if(!isLandscape){
            numButtonsAxisX = 4;
            numButtonsAxisY = 5;
        }
        else{
            numButtonsAxisX = 5;
            numButtonsAxisY = 4;
        }
        float paddingX = _graphics.getLogWidth() * 0.05f;
        float paddingY = _graphics.getLogHeight() * 0.05f;
        // Calculate buttons dimensions
        float buttonSide = Math.min((_graphics.getLogWidth() * 0.15f), (_graphics.getLogHeight() * 0.15f));
        float[] buttonSize = new float[] { buttonSide, buttonSide};

        float[] fullSize = new float[]{buttonSide * numButtonsAxisX + paddingX * (numButtonsAxisX - 1),
                buttonSide * numButtonsAxisY + paddingY * (numButtonsAxisY - 1)};
        int[] fullPosition = _graphics.constrainedToScreenPos(Constrain.MIDDLE, fullSize, new int[]{ 0, (int) (_graphics.getLogHeight() * 0.1f) });

        initGridTypesMap();

        for (int i = 0; i < _numLevels; i++) {
            int[] buttonPos = new int[2];
            buttonPos[0] = (int)(fullPosition[0] + (i % numButtonsAxisX) * (paddingX + buttonSide));
            buttonPos[1] = (int)(fullPosition[1] + (i / numButtonsAxisX) * (paddingY + buttonSide));

            boolean unlocked = _gridType.getValue() < ((GameDataSystem) _serSystem)._data._lastUnlockedPack
                    || i <= ((GameDataSystem) _serSystem)._data._lastUnlockedLevel;

            String text = "" + (i + 1);
            final SelectButton _level = new SelectButton(buttonPos, buttonSize, text,
                    FontName.LevelNumber.getName(), unlocked);

            // Seleccion de los datos del nivel escogido
            final int aux_i = i;

            _level.setCallback(new ButtonCallback() {
                @Override
                public void doSomething() {
                    int r = _level.getRows();
                    int c = _level.getCols();
                    State gameState = new GameState(_engine, _gridType, _isRandom, aux_i);
                    _engine.reqNewState(gameState);
                    _audio.playSound(SoundName.ClickSound.getName(), 0);
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
    private boolean _isRandom = false;

    // Mode Text
    private String _modeText = "Paquete NxM";
    private int[] _modePos = new int[2];
    private float[] _modeSize = new float[2];

    // Back Button
    private final String _backText = "Volver";
    private int[] _backTextPos = new int[2];
    private float[] _backTextSize = new float[2];

    private int[] _backImagePos = new int[2];
    private float[] _backImageSize = new float[2];

    private float[] _backButtonSize = new float[2];
    private ButtonCallback _backCallback;

    // SELECT LEVEL BUTTONS
    /**
     * List of all select level buttons
     */
    List<SelectButton> _selectButtons = new ArrayList<>();
    /**
     * Dictionary of information about
     * different grid level types
     */
    Map<Integer, GridType> _gridTypes = new HashMap<>();
}
