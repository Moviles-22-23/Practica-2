package es.ucm.stalos.nonogramas.logic.states;

import android.graphics.Point;

import java.util.HashMap;

import es.ucm.stalos.androidengine.State;
import es.ucm.stalos.androidengine.Engine;
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

            //Sprites
            _graphics.newImage(ImageName.BackArrow.getName(),
                    ImageName.BackArrow.getFileName());
            _graphics.newImage(ImageName.Lens.getName(),
                    ImageName.Lens.getFileName());
            _graphics.newImage(ImageName.Heart.getName(),
                    ImageName.Heart.getFileName());
            _graphics.newImage(ImageName.Lock.getName(),
                    ImageName.Lock.getFileName());
            _graphics.newImage(ImageName.GameOver.getName(),
                    ImageName.GameOver.getFileName());
            _graphics.newImage(ImageName.Ads.getName(),
                    ImageName.Ads.getFileName());
            _graphics.newImage(ImageName.HeartDisable.getName(),
                    ImageName.HeartDisable.getFileName());
            _graphics.newImage(ImageName.Share.getName(),
                    ImageName.Share.getFileName());
            _graphics.newImage(ImageName.HeartAdd.getName(),
                    ImageName.HeartAdd.getFileName());

            // Fonts
            _graphics.newFont(FontName.TitleMainMenu.getName(),
                    FontName.TitleMainMenu.getFileName(), 50, true);
            _graphics.newFont(FontName.ButtonMainMenu.getName(),
                    FontName.ButtonMainMenu.getFileName(), 35, true);
            _graphics.newFont(FontName.DefaultFont.getName(),
                    FontName.DefaultFont.getFileName(), 25, true);
            _graphics.newFont(FontName.RowColNumber.getName(),
                    FontName.RowColNumber.getFileName(), 20, true);
            _graphics.newFont(FontName.LevelNumber.getName(),
                    FontName.LevelNumber.getFileName(), 20, true);
            _graphics.newFont(FontName.GameStateButton.getName(),
                    FontName.GameStateButton.getFileName(), 20, true);
            _graphics.newFont(FontName.GameStateButton.getName(),
                    FontName.GameStateButton.getFileName(), 30, true);
            _graphics.newFont(FontName.FigureName.getName(),
                    FontName.FigureName.getFileName(), 50, true);

            // Audio
            _audio = _engine.getAudio();
            _audio.newSound(SoundName.MainTheme.getName(), SoundName.MainTheme.getFileName());
            _audio.newSound(SoundName.MenuTheme.getName(), SoundName.MenuTheme.getFileName());
            _audio.newSound(SoundName.ClickSound.getName(), SoundName.ClickSound.getFileName());
            _audio.newSound(SoundName.FailSound.getName(), SoundName.FailSound.getFileName());
            _audio.newSound(SoundName.GoodSound.getName(), SoundName.GoodSound.getFileName());
            _audio.newSound(SoundName.WindSound.getName(), SoundName.WindSound.getFileName());

            // Color Sets
            ColorPalette._colorSets = new HashMap<>();
            ColorPalette._colorSets.put(0, new Point(MyColor.BLACK.getValue(),
                    MyColor.WHITE.getValue()));
            ColorPalette._colorSets.put(1, new Point(MyColor.ORANGE.getValue(),
                    MyColor.LIGHT_ORANGE.getValue()));
            ColorPalette._colorSets.put(2, new Point(MyColor.DARK_GREEN.getValue(),
                    MyColor.LIGHT_GREEN.getValue()));
            ColorPalette._colorSets.put(3, new Point(MyColor.SOFT_BLUE.getValue(),
                    MyColor.LIGHT_BLUE.getValue()));
            ColorPalette._colorSets.put(4, new Point(MyColor.DARK_RED.getValue(),
                    MyColor.LIGHT_RED.getValue()));
            ColorPalette._colorSets.put(5, new Point(MyColor.PURPLE.getValue(),
                    MyColor.LIGHT_PURPLE.getValue()));
            ColorPalette._colorSets.put(6, new Point(MyColor.DARK_PURPLE.getValue(),
                    MyColor.SKY_BLUE.getValue()));

            // LOAD DATA
            _serSystem = new GameDataSystem(_engine.getContext(), _engine.getAssetManager());
            _serSystem.loadData();
            // * loadWrongData will generate fake data in order to
            // test the Hash System.
            //((GameDataSystem)_serSystem).loadWrongData();

            GameData _data = ((GameDataSystem) _serSystem)._data;
            // Was the last game being played?
            if (_data._inGame) {
                // 1. Last GameState played
                State gameState = new GameState(_engine);
                _engine.reqNewState(gameState);
            } else {
                // 2. Init normal way: MainMenu
                State mainMenu = new MainMenuState(_engine);
                _engine.reqNewState(mainMenu);
            }
            ColorPalette._currPalette = _data._currPalette;

        } catch (Exception e) {
            System.out.println("Error en init de LoadState");
            System.err.println(e);
            return false;
        }

        return true;
    }
}
