package es.ucm.stalos.androidengine;

import android.content.res.AssetManager;
import android.view.SurfaceView;

import androidx.appcompat.app.AppCompatActivity;

public class AndroidEngine implements Runnable {
    public AndroidEngine() {

    }

    public boolean init(AbstractState initState, int w, int h, AppCompatActivity activity) {
        AssetManager assetsMan = activity.getApplicationContext().getAssets();

        //STATE
        _currState = initState;

        //GRAPHICS
        _graphics = new AndroidGraphics(w, h, activity.getWindowManager(), activity.getWindow());

        // INPUT
        _input = new AndroidInput(this);

        // AUDIO
        _audio = new AndroidAudio(assetsMan);

        // FILE READER
        _fReader = new AndroidFileReader(assetsMan);

        return ((AndroidGraphics) _graphics).init((AndroidInput) _input, activity) && _currState.init();
    }

    public void run() {
        if (_renderThread != Thread.currentThread()) {
            // Evita que cualquiera que no sea esta clase llame a este Runnable en un Thread
            // Programación defensiva
            throw new RuntimeException("run() should not be called directly");
        }
        //TODO: Bucle principal en android
        _lastFrameTime = System.nanoTime();
        _running = true;

        while (_running) {
            // Refresco del deltaTime
            updateDeltaTime();

            // Refresco del estado actual
            _currState.handleInput();
            _currState.update(_deltaTime);

            // Pintado del estado actual
            _graphics.prepareFrame();
            _graphics.clear(0xFFFFFFFF);
            _currState.render();
            _graphics.restore();

            // Inicializacion del siguiente estado en diferido
            if (_changeState) {
                _changeState = false;
                _currState = _newState;
                _currState.init();
            }
        }

    }

    public void resume() {
        if (!this._running) {
            this._running = true;

            this._renderThread = new Thread(this);
            this._renderThread.start();

            ((AndroidAudio) _audio).resume();
        }
    }

    public void pause() {
        if (this._running) {
            this._running = false;

            while (true) {
                try {
                    this._renderThread.join();
                    this._renderThread = null;
                    break;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            ((AndroidAudio) _audio).pause();
        }
    }

    //---------------------------------------ABSTRACT-ENGINE-VIEJO--------------------------------//

    protected void updateDeltaTime() {
        _currentTime = System.nanoTime();
        long nanoElapsedTime = _currentTime - _lastFrameTime;
        _lastFrameTime = _currentTime;
        _deltaTime = (double) nanoElapsedTime / 1.0E9;
    }

    public void reqNewState(AbstractState newState){
        _changeState = true;
        _newState = newState;
    }

    public AndroidGraphics getGraphics() {
        return _graphics;
    }

    public AndroidInput getInput() {
        return _input;
    }

    public AndroidAudio getAudio(){
        return _audio;
    }

    public AndroidFileReader getFileReader(){
        return _fReader;
    }
    //--------------------------------------------------------------------------------------------//

    private Thread _renderThread;
    private boolean _running;

    protected boolean _changeState = false;
    protected AbstractState _newState;
    protected AbstractState _currState;
    protected AndroidGraphics _graphics;
    protected AndroidInput _input;
    protected AndroidAudio _audio;
    protected AndroidFileReader _fReader;

    // DELTA TIME
    protected long _lastFrameTime = 0;
    protected long _currentTime = 0;
    protected double _deltaTime = 0;
}
