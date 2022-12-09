package es.ucm.stalos.androidengine;

import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

// PRACTICA 2: Cambios ahora la clase Input hererda de OnLongClickListener para dar soporte a LongClick
public class Input implements View.OnTouchListener, View.OnLongClickListener {
    public Input(Engine e) {
        _engine = e;
        _events = new ArrayList<>();
    }

    /**
     * @return return the list of all events
     */
    public List<TouchEvent> getTouchEvents() {
        if (!_events.isEmpty()) {
            List<TouchEvent> touchEvents = new ArrayList<>(_events);
            _events.clear();
            return touchEvents;
        } else return _events;
    }

    /**
     * Process input's coordinates to transform them into
     * x and y logical positions.
     * Also adds the event to the list
     *
     * @param x Window X position
     * @param y Window Y position
     */
    protected void onTouchDownEvent(int x, int y) {
        Graphics g = _engine.getGraphics();

        TouchEvent currEvent = TouchEvent.touchDown;
        int[] eventPos = g.logPos(x, y);
        currEvent.setX(eventPos[0]);
        currEvent.setY(eventPos[1]);

        System.out.println("Touch UP pos: X: " + eventPos[0] + " Y: " + eventPos[1]);

        _events.add(currEvent);
    }

    // PRACTICA 2: Cambios metodo para añadir un evento de longTouch a la cola de eventos

    /**
     * Process input's coordinates to transform them into
     * x and y logical positions.
     * Also adds the event to the list
     *
     * @param x Window X position
     * @param y Window Y position
     */
    protected void onLongTouchEvent(int x, int y) {
        Graphics g = _engine.getGraphics();

        TouchEvent currEvent = TouchEvent.longTouch;
        int[] eventPos = g.logPos(x, y);
        currEvent.setX(eventPos[0]);
        currEvent.setY(eventPos[1]);

        _events.add(currEvent);
    }

    // PRACTICA 2: Cambios diferenciacion entre touch normal y longTouch
    public boolean onTouch(View v, MotionEvent e) {
        if (e.getAction() == MotionEvent.ACTION_DOWN) {
            lastTouchX = e.getX();
            lastTouchY = e.getY();
        }
        if (e.getAction() == MotionEvent.ACTION_UP) {
            v.performClick();
            if (!_longTouching) {
                System.out.println("Touch UP pos: X: " + e.getX() + " Y: " + e.getY());
                onTouchDownEvent((int) e.getX(), (int) e.getY());
            } else if (e.getX() == lastTouchX && e.getY() == lastTouchY) {
                System.out.println("Long Touch UP pos: X: " + e.getX() + " Y: " + e.getY());
                onLongTouchEvent((int) e.getX(), (int) e.getY());
                _longTouching = false;
            } else _longTouching = false;
            System.out.println("INPUT: --Fin del touch--");
        }
        return false;
    }

    // PRACTICA 2: Sobreescritura del metodo OnLongClick
    @Override
    public boolean onLongClick(View v) {
        _longTouching = true;
        return false;
    }

    private Engine _engine;
    private boolean _longTouching = false;
    private List<TouchEvent> _events;
    private float lastTouchX = 0, lastTouchY = 0;
}
