package es.ucm.stalos.androidengine;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Base class to define different states
 */
public abstract class State {

    protected State(Engine engine) {
        this._engine = engine;
        this._graphics = engine.getGraphics();
        this._audio = engine.getAudio();
    }

    /**
     * Initialize the state
     *
     * @return false if it fails
     */
    protected boolean init() {
        return true;
    }

    /**
     * Calculate if the position of the click is inside an square
     *
     * @param clickPos   CLick position to be checked
     * @param squarePos  Upper-left corner of the square
     * @param squareSize Size of the square
     * @return true if the click is inside the square
     */
    protected boolean clickInsideSquare(int[] clickPos, int[] squarePos, float[] squareSize) {
        return (clickPos[0] > squarePos[0] && clickPos[0] < (squarePos[0] + squareSize[0]) &&
                clickPos[1] > squarePos[1] && clickPos[1] < (squarePos[1] + squareSize[1]));
    }

    /**
     * Updates logic
     */
    protected void update(double deltaTime) {

    }

    /**
     * Updates graphics
     */
    protected void render() {

    }

    /**
     * Updates input
     */
    protected void handleInput() {

    }

    /**
     * Change positions and size between Portrait or Landscape
     */
    protected void togglePortraitLandscape(boolean isLandscape){

    }

    protected void saveData() {
        _serSystem.saveData();
    }

    protected void manageSensorEvent() {

    }

    protected Engine _engine;
    protected Graphics _graphics;
    protected Audio _audio;

    // Attributes to create a timer
    protected Timer _timer;
    protected TimerTask _timerTask;
    protected int _timeDelay;

    protected static SerializableSystem _serSystem;
}

