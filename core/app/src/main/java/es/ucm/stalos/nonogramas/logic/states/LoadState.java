package es.ucm.stalos.nonogramas.logic.states;

import android.graphics.Point;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;

import es.ucm.stalos.androidengine.State;
import es.ucm.stalos.androidengine.Engine;
import es.ucm.stalos.nonogramas.R;
import es.ucm.stalos.nonogramas.logic.data.GameData;
import es.ucm.stalos.nonogramas.logic.data.GameDataSystem;
import es.ucm.stalos.nonogramas.logic.enums.FontName;
import es.ucm.stalos.nonogramas.logic.enums.SoundName;
import es.ucm.stalos.nonogramas.logic.enums.ImageName;
import es.ucm.stalos.nonogramas.logic.enums.MyColor;
import es.ucm.stalos.nonogramas.logic.objects.ColorPalette;

/**
 * This state is created to initialize all the assets of the game before it starts
 */
public class LoadState extends State {
    public LoadState(Engine engine) {
        super(engine);
    }

    @Override
    public boolean init() {
        try {
            _graphics = _engine.getGraphics();
            AppCompatActivity context = _engine.getContext();
            //Sprites
            _graphics.newImage(ImageName.BackArrow.getName(),
                    context.getResources().getString(R.string.BackArrow));
            _graphics.newImage(ImageName.Lock.getName(),
                    context.getResources().getString(R.string.Lock));
            _graphics.newImage(ImageName.GameOver.getName(),
                    context.getResources().getString(R.string.GameOver));
            _graphics.newImage(ImageName.Ads.getName(),
                    context.getResources().getString(R.string.Ads));
            _graphics.newImage(ImageName.Share.getName(),
                    context.getResources().getString(R.string.Share));
            _graphics.newImage(ImageName.Heart.getName(),
                    context.getResources().getString(R.string.Heart));
            _graphics.newImage(ImageName.HeartDisable.getName(),
                    context.getResources().getString(R.string.HeartDisable));
            _graphics.newImage(ImageName.HeartRecovery.getName(),
                    context.getResources().getString(R.string.HeartRecovery));
            _graphics.newImage(ImageName.Twitter.getName(),
                    context.getResources().getString(R.string.Twitter));
            _graphics.newImage(ImageName.WhatsApp.getName(),
                    context.getResources().getString(R.string.WhatsApp));

            // Fonts
            // Main Menu
            _graphics.newFont(FontName.TitleMainMenu.getName(), context.getResources().getString(R.string.Molle),
                    50, true);
            _graphics.newFont(FontName.ButtonMainMenu.getName(), context.getResources().getString(R.string.JoseFinSans),
                    35, true);

            // Select State
            _graphics.newFont(FontName.SelectStateTitle.getName(), context.getResources().getString(R.string.JoseFinSans),
                    36, true);
            _graphics.newFont(FontName.SelectStateDescription.getName(), context.getResources().getString(R.string.JoseFinSans),
                    26, true);
            _graphics.newFont(FontName.SelectStateButton.getName(), context.getResources().getString(R.string.JoseFinSans),
                    26, true);
            _graphics.newFont(FontName.LevelNumber.getName(), context.getResources().getString(R.string.Molle),
                    20, true);

            _graphics.newFont(FontName.DefaultFont.getName(), context.getResources().getString(R.string.JoseFinSans),
                    32, true);
            _graphics.newFont(FontName.RowColNumber.getName(),
                    context.getResources().getString(R.string.Molle),
                    20, true);
            _graphics.newFont(FontName.GameStateButton.getName(), context.getResources().getString(R.string.JoseFinSans),
                    20, true);
            _graphics.newFont(FontName.GameStateButton.getName(),
                    context.getResources().getString(R.string.JoseFinSans),
                    30, true);
            _graphics.newFont(FontName.FigureName.getName(),
                    context.getResources().getString(R.string.JoseFinSans),
                    50, true);
            _graphics.newFont(FontName.GameStateText.getName(),
                    context.getResources().getString(R.string.JoseFinSans),
                    30, true);

            // Audio
            _audio = _engine.getAudio();
            _audio.newSound(SoundName.MainTheme.getName(),
                    context.getResources().getString(R.string.MainTheme));
            _audio.newSound(SoundName.MenuTheme.getName(),
                    context.getResources().getString(R.string.MenuTheme));
            _audio.newSound(SoundName.ClickSound.getName(),
                    context.getResources().getString(R.string.ClickSound));
            _audio.newSound(SoundName.FailSound.getName(),
                    context.getResources().getString(R.string.FailSound));
            _audio.newSound(SoundName.GoodSound.getName(),
                    context.getResources().getString(R.string.GoodSound));
            _audio.newSound(SoundName.WindSound.getName(),
                    context.getResources().getString(R.string.WindSound));

            // Color Sets
            ColorPalette._colorSets = new HashMap<>();
            ColorPalette._colorSets.put(0, new Point(MyColor.BLACK.get_color(),
                    MyColor.WHITE.get_color()));
            ColorPalette._colorSets.put(1, new Point(MyColor.ORANGE.get_color(),
                    MyColor.LIGHT_ORANGE.get_color()));
            ColorPalette._colorSets.put(2, new Point(MyColor.DARK_GREEN.get_color(),
                    MyColor.LIGHT_GREEN.get_color()));
            ColorPalette._colorSets.put(3, new Point(MyColor.SOFT_BLUE.get_color(),
                    MyColor.LIGHT_BLUE.get_color()));
            ColorPalette._colorSets.put(4, new Point(MyColor.DARK_RED.get_color(),
                    MyColor.LIGHT_RED.get_color()));
            ColorPalette._colorSets.put(5, new Point(MyColor.PURPLE.get_color(),
                    MyColor.LIGHT_PURPLE.get_color()));
            ColorPalette._colorSets.put(6, new Point(MyColor.DARK_PURPLE.get_color(),
                    MyColor.SKY_BLUE.get_color()));

            // LOAD DATA
            _serSystem = new GameDataSystem(_engine.getContext(), _engine.getAssetManager());
            _serSystem.loadData();
            // * loadWrongData will generate fake data in order to
            // test the Hash System.
            //((GameDataSystem)_serSystem).loadWrongData();

            loadData();


        } catch (Exception e) {
            System.out.println("Error en init de LoadState");
            System.err.println(e);
            return false;
        }

        return true;
    }

    private void loadData() {
        GameData _data = ((GameDataSystem) _serSystem)._data;
        State currentState;
        switch (_data._currStateType) {
            default:
            case MainMenuState:
                currentState = new MainMenuState(_engine);
                break;
            case SelectBoardState:
                currentState = new SelectBoardState(_engine, _data._isRandom);
                break;
            case SelectLevelState:
                currentState = new SelectLevelState(_engine, _data._currGridType);
                break;
            case GameState:
                // 1. Last GameState played
                currentState = new GameState(_engine);
                break;
        }

        _engine.reqNewState(currentState);
        ColorPalette._currPalette = _data._currPalette;
    }
}
