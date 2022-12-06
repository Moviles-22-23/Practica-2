package es.ucm.stalos.nonogramas.logic.states;

import es.ucm.stalos.androidengine.State;
import es.ucm.stalos.androidengine.Audio;
import es.ucm.stalos.androidengine.Engine;
import es.ucm.stalos.androidengine.Graphics;
import es.ucm.stalos.nonogramas.logic.Assets;
import es.ucm.stalos.nonogramas.logic.data.GameData;
import es.ucm.stalos.nonogramas.logic.data.GameDataSystem;
import es.ucm.stalos.nonogramas.logic.enums.GridType;

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
        } catch (Exception e) {
            System.out.println("Error en init de LoadState");
            System.err.println(e);
            return false;
        }

        return true;
    }
}
