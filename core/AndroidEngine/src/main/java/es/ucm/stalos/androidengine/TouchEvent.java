package es.ucm.stalos.androidengine;

/**
 * Enum to classify every TouchEvent that can happens
 */
public enum TouchEvent {
    touchDown(0),
    touchUp(1),
    longTouch(2),
    touchDrag(3),
    MAX(4);

    private int _x, _y;
    private final int _value;

    public void setX(int x) {
        _x = x;
    }

    public void setY(int y) {
        _y = y;
    }

    public int getValue() {
        return _value;
    }

    public int getX() {
        return _x;
    }

    public int getY() {
        return _y;
    }

    TouchEvent(int i) {
        this._value = i;
    }

}
