package es.ucm.stalos.androidengine;

import android.content.res.AssetManager;
import android.view.SurfaceView;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.Group;

import com.google.android.gms.ads.AdView;

public class Engine implements Runnable {
    public Engine() {

    }

    public boolean init(State initState, int w, int h, AppCompatActivity activity, SurfaceView view, Group adView) {
        _context = activity;
        _assetsMan = activity.getApplicationContext().getAssets();

        //STATE
        _currState = initState;

        //GRAPHICS
        _graphics = new Graphics(w, h, _assetsMan);

        // INPUT
        _input = new Input(this);

        //SCREEN
        _view = view;
        _adView = adView;

        _view.setOnTouchListener(_input);
        _view.setOnLongClickListener(_input);

        // AUDIO
        _audio = new Audio(_assetsMan, 10);

        return (_graphics).init(_input) && _currState.init();
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
            _graphics.prepareFrame(_view);
            _graphics.clear(0xFFFFFFFF);
            _currState.render();
            _graphics.restore(_view);

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

            _audio.resumeMusic();
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

            _audio.pauseBackMusic();
            _currState.saveData();
        }
    }

    //---------------------------------------ABSTRACT-ENGINE-VIEJO--------------------------------//

    /**
     * Request a change state. It is used to not change immediately, but
     * to do later.
     *
     * @param newState New state to change
     */
    public void reqNewState(State newState) {
        _changeState = true;
        _newState = newState;
    }

    /**
     * @return Instance of graphics engine
     */
    public Graphics getGraphics() {
        return _graphics;
    }

    /**
     * @return Instance of input manager
     */
    public Input getInput() {
        return _input;
    }

    /**
     * @return Instace of audio manager
     */
    public Audio getAudio() {
        return _audio;
    }

    /**
     * Update deltaTime value
     */
    protected void updateDeltaTime() {
        _currentTime = System.nanoTime();
        long nanoElapsedTime = _currentTime - _lastFrameTime;
        _lastFrameTime = _currentTime;
        _deltaTime = (double) nanoElapsedTime / 1.0E9;
    }

    public AssetManager getAssetManager() {
        return _assetsMan;
    }

    public AppCompatActivity getContext() {
        return _context;
    }

    /**
     * Set the visibility of the Ad Banner depending on the current state
     *
     * @param mainMenu if the current state is main menu
     */
    public void swapBannerAdVisibility(boolean mainMenu) {
        // este metodo de activity ejecuta el codigo en el hilo de la interfaz
        _context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (_adView.getVisibility() == View.VISIBLE && !mainMenu)
                    _adView.setVisibility(View.GONE);
                else if (mainMenu)
                    _adView.setVisibility(View.VISIBLE);
            }
        });
    }

    //--------------------------------------------------------------------------------------------//

    private Thread _renderThread;
    private boolean _running;

    private boolean _changeState = false;
    private State _newState;
    private State _currState;
    private Graphics _graphics;
    private Input _input;
    private Audio _audio;
    private AssetManager _assetsMan;
    private AppCompatActivity _context;

    private SurfaceView _view;
    private Group _adView;

    // DELTA TIME
    private long _lastFrameTime = 0;
    private long _currentTime = 0;
    private double _deltaTime = 0;
}
