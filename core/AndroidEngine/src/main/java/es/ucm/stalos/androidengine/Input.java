package es.ucm.stalos.androidengine;

import android.view.MotionEvent;
import android.view.View;
import java.util.ArrayList;
import java.util.List;

public class Input implements View.OnTouchListener {
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

    public boolean onTouch(View v, MotionEvent e) {
        if (e.getAction() == MotionEvent.ACTION_DOWN) {
            onTouchDownEvent((int) e.getX(), (int) e.getY());
        }
        return true;
    }

    private Engine _engine;
    private List<TouchEvent> _events;
}
