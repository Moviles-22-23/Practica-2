package es.ucm.stalos.nonogramas.logic.states;

import es.ucm.stalos.androidengine.State;
import es.ucm.stalos.androidengine.Audio;
import es.ucm.stalos.androidengine.Engine;
import es.ucm.stalos.androidengine.Graphics;
import es.ucm.stalos.nonogramas.logic.Assets;

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

            // Audio
            Assets.menuTheme = audio.newSound("menuTheme.wav");
            Assets.mainTheme = audio.newSound("mainTheme.wav");
            Assets.clickSound = audio.newSound("clickSound.wav");
            Assets.winSound = audio.newSound("winSound.wav");

            // Start MainMenu
            State mainMenu = new MainMenuState(_engine);
            _engine.reqNewState(mainMenu);

        } catch (Exception e) {
            System.err.println(e);
            return false;
        }

        return true;
    }
}
