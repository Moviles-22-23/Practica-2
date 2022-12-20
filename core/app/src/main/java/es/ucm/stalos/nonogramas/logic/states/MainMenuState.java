package es.ucm.stalos.nonogramas.logic.states;

import java.util.List;

import es.ucm.stalos.androidengine.enums.Constraint;
import es.ucm.stalos.androidengine.State;
import es.ucm.stalos.androidengine.Engine;
import es.ucm.stalos.nonogramas.logic.enums.StateType;
import es.ucm.stalos.androidengine.enums.TouchEvent;
import es.ucm.stalos.nonogramas.logic.data.GameDataSystem;
import es.ucm.stalos.nonogramas.logic.enums.FontName;
import es.ucm.stalos.nonogramas.logic.enums.MyColor;
import es.ucm.stalos.nonogramas.logic.enums.SoundName;
import es.ucm.stalos.nonogramas.logic.interfaces.ButtonCallback;
import es.ucm.stalos.nonogramas.logic.objects.ColorPalette;

public class MainMenuState extends State {

    public MainMenuState(Engine engine) {
        super(engine);

        ((GameDataSystem) _serSystem)._data._currStateType = StateType.MainMenuState;
    }

    //-----------------------------------------OVERRIDE-----------------------------------------------//
    @Override
    public boolean init() {
        try {
            _engine.swapBannerAdVisibility(true);

            togglePortraitLandscape(_engine.isLandScape());

            // Callbacks
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
            // Audio
            _audio.playMusic(SoundName.MenuTheme.getName());
        } catch (Exception e) {
            System.out.println("Error init Main Menu State");
            System.out.println(e);
            return false;
        }
        return true;
    }

    @Override
    public void render() {
        // BackgroundColor
        _graphics.clear(ColorPalette._colorSets.get(ColorPalette._currPalette).y);

        // Title
        _graphics.setColor(MyColor.BLACK.get_color());
        _graphics.drawCenteredString(_titleText, FontName.TitleMainMenu.getName(), _titlePos, _titleSize);

        // Play Button
        _graphics.setColor(ColorPalette._colorSets.get(ColorPalette._currPalette).x);
        if (ColorPalette._currPalette == 0) _graphics.drawRect(_playButtonPos, _playButtonSize);
        else _graphics.fillSquare(_playButtonPos, _playButtonSize);
        _graphics.drawCenteredString(_playButtonText, FontName.ButtonMainMenu.getName(), _playButtonPos, _playButtonSize);

        // Play Random Button
        _graphics.setColor(ColorPalette._colorSets.get(ColorPalette._currPalette).x);
        if (ColorPalette._currPalette == 0) _graphics.drawRect(_playRandomButtonPos, _playRandomButtonSize);
        else _graphics.fillSquare(_playRandomButtonPos, _playRandomButtonSize);
        _graphics.drawCenteredString(_playRandomButtonText, FontName.ButtonMainMenu.getName(), _playRandomButtonPos, _playRandomButtonSize);
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

    @Override
    protected void togglePortraitLandscape(boolean isLandscape) {
        if(!isLandscape){
            // TITLE
            _titleSize[0] = _graphics.getLogWidth() * 0.6f;
            _titleSize[1] = _graphics.getLogHeight() * 0.1f;
            _titlePos = _graphics.constrainedToScreenPos(Constraint.TOP, _titleSize, new int[]{0, (int) (_graphics.getLogHeight() * 0.15f )});

            // PLAY BUTTON PORTRAIT
            _playButtonText = "Modo Historia";
            _playButtonSize[0] = _graphics.getLogWidth() * 0.7f;
            _playButtonSize[1] = _graphics.getLogHeight() * 0.1f;
            _playButtonPos = _graphics.constrainedToScreenPos(Constraint.MIDDLE, _playButtonSize, new int[]{0, 0});

            // PLAY RANDOM BUTTON PORTRAIT
            _playRandomButtonText = "Modo Aleatorio";
            _playRandomButtonSize = _playButtonSize;
            _playRandomButtonPos = _graphics.constrainedToObjectPos(Constraint.TOP,
                    _playButtonPos, _playButtonSize,
                    _playRandomButtonSize, new int[]{0, (int) (_graphics.getLogHeight() * 0.1)});

        }
        else {
            // TITLE
            _titleSize[0] = _graphics.getLogWidth() * 0.6f;
            _titleSize[1] = _graphics.getLogHeight() * 0.1f;
            _titlePos = _graphics.constrainedToScreenPos(Constraint.TOP, _titleSize, new int[]{0, (int) (_graphics.getLogHeight() * 0.1f )});

            // PLAY RANDOM BUTTON LANDSCAPE
            _playButtonText = "Modo\nHistoria";
            _playButtonSize[0] = _graphics.getLogWidth() * 0.3f;
            _playButtonSize[1] = _graphics.getLogHeight() * 0.3f;
            _playButtonPos = _graphics.constrainedToScreenPos(Constraint.LEFT, _playButtonSize, new int[]{ (int) (_graphics.getLogWidth() * 0.15f), (int) (_graphics.getLogWidth() * 0.05f)});


            // PLAY RANDOM BUTTON LANDSCAPE
            _playRandomButtonText = "Modo\nAleatorio";
            _playRandomButtonSize = _playButtonSize;
            _playRandomButtonPos = _graphics.constrainedToScreenPos(Constraint.RIGHT, _playRandomButtonSize, new int[]{(int)(_graphics.getLogWidth() * 0.15f), (int) (_graphics.getLogWidth() * 0.05f)});
        }
    }

    //----------------------------------------ATTRIBUTES----------------------------------------------//

    // Callbacks
    private ButtonCallback _randCallback;
    private ButtonCallback _storyCallback;

    // Title
    private final String _titleText = "Nonogramas";
    private int[] _titlePos = new int[2];
    private float[] _titleSize = new float[2];

    // Play Button
    private String _playButtonText = "Modo Historia";
    private int[] _playButtonPos = new int[2];
    private float[] _playButtonSize = new float[2];

    // Play Random Button
    private String _playRandomButtonText = "Modo Aleatorio";
    private int[] _playRandomButtonPos = new int[2];
    private float[] _playRandomButtonSize = new float[2];
}
