package es.ucm.stalos.nonogramas.logic.states;

import java.util.HashMap;

import es.ucm.stalos.androidengine.State;
import es.ucm.stalos.androidengine.Audio;
import es.ucm.stalos.androidengine.Engine;
import es.ucm.stalos.androidengine.Graphics;
import es.ucm.stalos.nonogramas.logic.Assets;
import es.ucm.stalos.nonogramas.logic.data.GameData;
import es.ucm.stalos.nonogramas.logic.data.GameDataSystem;
import es.ucm.stalos.nonogramas.logic.enums.GridType;
import es.ucm.stalos.nonogramas.logic.enums.MyColor;
import es.ucm.stalos.nonogramas.logic.objects.ColorSet;

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
            Graphics graphics = _engine.getGraphics();
            Audio audio = _engine.getAudio();

            //Sprites
            Assets.backArrow = graphics.newImage("backArrow.png");
            Assets.lens = graphics.newImage("lents.png");
            Assets.heart = graphics.newImage("heart.png");
            Assets.lock = graphics.newImage("lock.png");
            Assets.gameOver = graphics.newImage("gameOver.png");
            Assets.ads = graphics.newImage("ads.png");

            // Audio
            Assets.menuTheme = audio.newSound("menuTheme.wav");
            Assets.mainTheme = audio.newSound("mainTheme.wav");
            Assets.clickSound = audio.newSound("clickSound.wav");
            Assets.failSound = audio.newSound("failSound.wav");
            Assets.goodSound = audio.newSound("goodSound.wav");
            Assets.winSound = audio.newSound("winSound.wav");

            // Color Sets
            Assets.colorSets = new HashMap<>();
            Assets.colorSets.put(0, new ColorSet(MyColor.ORANGE_SET_FIRST.getValue(), MyColor.ORANGE_SET_SECOND.getValue()));
            Assets.colorSets.put(1, new ColorSet(MyColor.RED.getValue(), MyColor.BLUE.getValue()));
            Assets.colorSets.put(2, new ColorSet(MyColor.GREEN_SET_FIRST.getValue(), MyColor.GREEN_SET_SECOND.getValue()));
            Assets.colorSets.put(3, new ColorSet(MyColor.BLUE_SET_FIRST.getValue(), MyColor.BLUE_SET_SECOND.getValue()));
            Assets.colorSets.put(4, new ColorSet(MyColor.RED_SET_FIRST.getValue(), MyColor.RED_SET_SECOND.getValue()));
            Assets.colorSets.put(5, new ColorSet(MyColor.PURPLE_SET_FIRST.getValue(), MyColor.PURPLE_SET_SECOND.getValue()));
            Assets.colorSets.put(6, new ColorSet(MyColor.PURBLUE_SET_FIRST.getValue(), MyColor.PURBLUE_SET_SECOND.getValue()));

            _serSystem = new GameDataSystem(_engine.getContext(), _engine.getAssetManager());

            // LOAD DATA
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
            Assets.currPalette = _data._currPalette;
        } catch (Exception e) {
            System.out.println("Error en init de LoadState");
            System.err.println(e);
            return false;
        }

        return true;
    }
}
