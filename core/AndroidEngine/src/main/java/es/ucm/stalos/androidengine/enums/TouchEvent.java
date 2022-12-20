package es.ucm.stalos.androidengine.enums;

/**
 * Enum to classify every TouchEvent that can happens
 */
public enum TouchEvent {
    touchDown(0),
    touchUp(1),
    longTouch(2),
    touchDrag(3);

    private int _x, _y;
    private final int _touchEvent;

    TouchEvent(int touchEvent) {
        this._touchEvent = touchEvent;
    }

    public void setX(int x) {
        _x = x;
    }

    public void setY(int y) {
        _y = y;
    }

    public int getTouchEvent() {
        return _touchEvent;
    }

    public int getX() {
        return _x;
    }

    public int getY() {
        return _y;
    }


}
