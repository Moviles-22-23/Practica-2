package es.ucm.stalos.androidengine;

import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

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

        _events.add(currEvent);
    }


    protected void onLongTouchEvent(int x, int y) {
        Graphics g = _engine.getGraphics();

        TouchEvent currEvent = TouchEvent.longTouch;
        int[] eventPos = g.logPos(x, y);
        currEvent.setX(eventPos[0]);
        currEvent.setY(eventPos[1]);

        _events.add(currEvent);
    }

    public boolean onTouch(View v, MotionEvent e) {
        if (e.getAction() == MotionEvent.ACTION_DOWN) {
            lastTouchX = e.getX();
            lastTouchY = e.getY();
        }
        if (e.getAction() == MotionEvent.ACTION_UP) {
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
