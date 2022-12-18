package es.ucm.stalos.nonogramas.logic.states;

import java.util.List;

import es.ucm.stalos.androidengine.Constrain;
import es.ucm.stalos.androidengine.State;
import es.ucm.stalos.androidengine.Engine;
import es.ucm.stalos.androidengine.TouchEvent;
import es.ucm.stalos.nonogramas.logic.enums.FontName;
import es.ucm.stalos.nonogramas.logic.enums.MyColor;
import es.ucm.stalos.nonogramas.logic.enums.SoundName;
import es.ucm.stalos.nonogramas.logic.interfaces.ButtonCallback;
import es.ucm.stalos.nonogramas.logic.objects.ColorPalette;

public class MainMenuState extends State {

    public MainMenuState(Engine engine) {
        super(engine);
    }

    //-----------------------------------------OVERRIDE-----------------------------------------------//
    @Override
    public boolean init() {
        try {
            _engine.swapBannerAdVisibility(true);
            // TITLE
            _titleSize[0] = _graphics.getLogWidth() * 0.7f;
            _titleSize[1] = _graphics.getLogHeight() * 0.1f;
            _titlePos[0] = (int) ((_graphics.getLogWidth() - _titleSize[0]) * 0.5f);
            _titlePos[1] = (int) ((_graphics.getLogHeight() - _titleSize[1]) * 0.1f);

            // BUTTONS
            _randCallback = new ButtonCallback() {
                @Override
                public void doSomething() {
                    State selectLevelState = new SelectBoardState(_engine, true);
                    _engine.reqNewState(selectLevelState);
                    _audio.playSound(SoundName.ClickSound.getName(), 0);
                }
            };

            _storyCallback = new ButtonCallback() {
                @Override
                public void doSomething() {
                    State selectLevelState = new SelectBoardState(_engine, false);
                    _engine.reqNewState(selectLevelState);
                    _audio.playSound(SoundName.ClickSound.getName(), 0);
                }
            };

            // PLAY BUTTON
            _playButtonSize[0] = _graphics.getLogWidth() * 0.7f;
            _playButtonSize[1] = _graphics.getLogHeight() * 0.1f;

            _playButtonSizeL[0] = _graphics.getLogWidth() * 0.2f;
            _playButtonSizeL[1] = _playButtonSizeL[0];
//            _playButtonPos[0] = (int) ((_graphics.getLogWidth() - _playButtonSize[0]) * 0.5f);
//            _playButtonPos[1] = (int) ((_graphics.getLogHeight() - _playButtonSize[1]) * 0.40f);
            _playButtonPos = _graphics.constrainedPos(Constrain.TOP_LEFT, _playButtonSize, new int[]{0, 0});
            _playButtonPosL = _graphics.constrainedPos(Constrain.MIDDLE, _playButtonSizeL, new int[]{0, 0});

            // PLAY RANDOM BUTTON
            _playRandomButtonSize[0] = _graphics.getLogWidth() * 0.7f;
            _playRandomButtonSize[1] = _graphics.getLogHeight() * 0.1f;
//            _playRandomButtonPos[0] = (int) ((_graphics.getLogWidth() - _playRandomButtonSize[0]) * 0.5f);
//            _playRandomButtonPos[1] = (int) ((_graphics.getLogHeight() - _playRandomButtonSize[1]) * 0.60f);

            _playRandomButtonPos = _graphics.constrainedPos(Constrain.BOTTOM_RIGHT, _playRandomButtonSize, new int[]{0, 0});
            System.out.println("RandomButtonPos--- X: " + _playRandomButtonPos[0] + ", Y: " + _playRandomButtonPos[1]);

            // Audio
            _audio.playMusic(SoundName.MenuTheme.getName());
        } catch (Exception e) {
            System.out.println("Error init Main Menu");
            System.out.println(e);
            return false;
        }
        return true;
    }

    @Override
    public void render() {
        // BackgroundColor
        _graphics.clear(ColorPalette._colorSets.get(ColorPalette._currPalette).y);

        // Texts Color
        _graphics.setColor(MyColor.BLACK.get_color());

        if (!isLandscape()) {
            // Title
            _graphics.drawCenteredString(_titleText, FontName.TitleMainMenu.getName(),
                    _titlePos, _titleSize);

            // Play Button
            _graphics.setColor(ColorPalette._colorSets.get(ColorPalette._currPalette).x);
            if (ColorPalette._currPalette == 0) _graphics.drawRect(_playButtonPos, _playButtonSize);
            else _graphics.fillSquare(_playButtonPos, _playButtonSize);
            _graphics.drawCenteredString(_playButtonText, FontName.ButtonMainMenu.getName(),
                    _playButtonPos, _playButtonSize);

            // Play Random Button
            _graphics.setColor(ColorPalette._colorSets.get(ColorPalette._currPalette).x);
            if (ColorPalette._currPalette == 0)
                _graphics.drawRect(_playRandomButtonPos, _playRandomButtonSize);
            else _graphics.fillSquare(_playRandomButtonPos, _playRandomButtonSize);
            _graphics.drawCenteredString(_playRandomButtonText, FontName.ButtonMainMenu.getName(),
                    _playRandomButtonPos, _playRandomButtonSize);
        } else {
            // Play Button
            _graphics.setColor(ColorPalette._colorSets.get(ColorPalette._currPalette).x);
            if (ColorPalette._currPalette == 0)
                _graphics.drawRect(_playButtonPosL, _playButtonSizeL);
            else _graphics.fillSquare(_playButtonPosL, _playButtonSizeL);
            _graphics.drawCenteredString(_playButtonText, FontName.ButtonMainMenu.getName(),
                    _playButtonPosL, _playButtonSizeL);
        }
    }

    @Override
    public void handleInput() {
        List<TouchEvent> events = _engine.getInput().getTouchEvents();
        for (int i = 0; i < events.size(); i++) {
            TouchEvent currEvent = events.get(i);
            if (currEvent == TouchEvent.touchDown) {
                int[] clickPos = {currEvent.getX(), currEvent.getY()};

                // Play Button
                if (clickInsideSquare(clickPos, _playButtonPos, _playButtonSize)) {
                    _storyCallback.doSomething();
                }
                // Play Random Button
                else if (clickInsideSquare(clickPos, _playRandomButtonPos, _playRandomButtonSize)) {
                    _randCallback.doSomething();
                }
            }
        }
    }

    //----------------------------------------ATTRIBUTES----------------------------------------------//
    // Title
    private final String _titleText = "Nonogramas";
    private int[] _titlePos = new int[2];
    private float[] _titleSize = new float[2];

    // Buttons
    private ButtonCallback _randCallback;
    private ButtonCallback _storyCallback;

    // Play Button
    private final String _playButtonText = "Modo Historia";
    // Portrait
    private int[] _playButtonPos = new int[2];
    private float[] _playButtonSize = new float[2];
    // Landscape
    private int[] _playButtonPosL = new int[2];
    private float[] _playButtonSizeL = new float[2];

    // Play Random Button
    private final String _playRandomButtonText = "Modo Aleatorio";
    private int[] _playRandomButtonPos = new int[2];
    private float[] _playRandomButtonSize = new float[2];
}
