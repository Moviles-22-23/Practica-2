package es.ucm.stalos.nonogramas.logic.states;

import es.ucm.stalos.androidengine.AbstractState;
import es.ucm.stalos.androidengine.AndroidAudio;
import es.ucm.stalos.androidengine.AndroidEngine;
import es.ucm.stalos.androidengine.AndroidGraphics;
import es.ucm.stalos.nonogramas.logic.Assets;

/**
 * This state is created to initialize all the assets of the game before it starts
 */
public class LoadState extends AbstractState {
    public LoadState(AndroidEngine engine) {
        super(engine);
    }

    @Override
    public boolean init() {
        try {
            AndroidGraphics graphics = _engine.getGraphics();
            AndroidAudio audio = _engine.getAudio();

            //Sprites
            Assets.backArrow = graphics.newImage("backArrow.png");
            Assets.lens = graphics.newImage("lents.png");

            // Audio
            Assets.menuTheme = audio.newSound("menuTheme.wav");
            Assets.mainTheme = audio.newSound("mainTheme.wav");
            Assets.clickSound = audio.newSound("clickSound.wav");
            Assets.winSound = audio.newSound("winSound.wav");

            // Start MainMenu
            AbstractState mainMenu = new MainMenuState(_engine);
            _engine.reqNewState(mainMenu);

        } catch (Exception e) {
            System.err.println(e);
            return false;
        }

        return true;
    }
}
