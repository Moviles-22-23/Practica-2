package es.ucm.stalos.nonogramas.logic.states;

import android.provider.ContactsContract;

import es.ucm.stalos.androidengine.State;
import es.ucm.stalos.androidengine.Audio;
import es.ucm.stalos.androidengine.Engine;
import es.ucm.stalos.androidengine.Graphics;
import es.ucm.stalos.nonogramas.logic.data.DataSystem;
import es.ucm.stalos.nonogramas.logic.Assets;
import es.ucm.stalos.nonogramas.logic.data.HistoryData;
import es.ucm.stalos.nonogramas.logic.data.LevelData;

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

//            for (int i = 0; i < 6; i++) {
//                DataSystem._packageDataList.add(new PackageData());
//            }

            // Inicializacion de los datos
            DataSystem._historyData = new HistoryData();
            DataSystem._historyData._currentPackage = 1;
            DataSystem._historyData._currentLevel = 10;

            // TODO
//            // Hacer algo así al arrancar
//            if(DataSystem._inGame){
//                // Lee los datos que llevaba el nivel y los carga
//                LevelData levelToContinue = DataSystem._levelData;
//                State gameState = new GameState(_engine, levelToContinue);
//
//                // TODO no se si sera mejor hacer otra constructora o hacer un setter con los datos
//                _engine.reqNewState(gameState);
//            }
//            else {
//              // Carga el menu principal
//                State mainMenu = new MainMenuState(_engine);
//                _engine.reqNewState(mainMenu);
//            }

            // Start MainMenu
            State mainMenu = new MainMenuState(_engine);
            _engine.reqNewState(mainMenu);

        } catch (Exception e) {
            System.out.println("Error en init de LoadState");
            System.err.println(e);
            return false;
        }

        return true;
    }
}
