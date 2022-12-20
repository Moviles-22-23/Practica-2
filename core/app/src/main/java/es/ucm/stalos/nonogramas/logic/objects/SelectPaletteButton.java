package es.ucm.stalos.nonogramas.logic.objects;

import android.graphics.Point;

import es.ucm.stalos.androidengine.Graphics;
import es.ucm.stalos.nonogramas.logic.enums.ImageName;
import es.ucm.stalos.nonogramas.logic.enums.MyColor;
import es.ucm.stalos.nonogramas.logic.interfaces.ButtonCallback;

/**
 * Button that changes the current Color Palette
 */
public class SelectPaletteButton {
    public SelectPaletteButton(int[] pos, float[] size, Point colorSet, boolean _isUnlocked, int index, boolean isLandscape) {
        this._buttonPos = pos;
        this._buttonSize = size;
        this._colorSet = colorSet;

        float lockMaxSide = Math.min(_buttonSize[0], _buttonSize[1]);
        this._lockSize = new float[] { lockMaxSide,lockMaxSide };


        if(!isLandscape) {
            int margin = 10;

            this._firstColorPos[0] = (int) (_buttonPos[0] + _buttonSize[0] * 0.5f);
            this._firstColorPos[1] = (int) (_buttonPos[1] + _buttonSize[1] * 0.25f);

            this._secondColorPos[0] = (int) (_buttonPos[0] + _buttonSize[0] * 0.5f);
            this._secondColorPos[1] = (int) (_buttonPos[1] + _buttonSize[1] * 0.75f);

            this._colorRadius = (_buttonSize[0] / 2) - margin;

            this._lockPos = new int[]{_buttonPos[0], (int) (_buttonPos[1] + (_buttonSize[1] - lockMaxSide) / 2)};
        }
        else{
            int margin = 10;

            this._firstColorPos[0] = (int) (_buttonPos[0] + _buttonSize[0] * 0.25f);
            this._firstColorPos[1] = (int) (_buttonPos[1] + _buttonSize[1] * 0.5f);

            this._secondColorPos[0] = (int) (_buttonPos[0] + _buttonSize[0] * 0.75f);
            this._secondColorPos[1] = (int) (_buttonPos[1] + _buttonSize[1] * 0.5f);

            this._colorRadius = (_buttonSize[1] / 2) - margin;

            this._lockPos = new int[]{(int) (_buttonPos[0] + (_buttonSize[0] - lockMaxSide) / 2), _buttonPos[1]};
        }

        this._isUnlocked = _isUnlocked;
        this._index = index;
    }

    public void render(Graphics graphics) {
        // Fondo negro
        if (ColorPalette._currPalette == _index) graphics.setColor(_colorSet.y);
        else graphics.setColor(MyColor.BLACK.get_color());
        graphics.fillSquare(_buttonPos, _buttonSize);
        // Color Primario
        graphics.setColor(_colorSet.x);
        graphics.fillCircle(_firstColorPos, _colorRadius);
        // Marco color primario (Si es negro pone borde blanco)
        if (_index == 0) graphics.setColor(MyColor.WHITE.get_color());
        else graphics.setColor(MyColor.BLACK.get_color());
        graphics.drawCircle(_firstColorPos, _colorRadius);
        // Color Secundario
        graphics.setColor(_colorSet.y);
        graphics.fillCircle(_secondColorPos, _colorRadius);
        graphics.setColor(MyColor.BLACK.get_color());
        graphics.drawCircle(_secondColorPos, _colorRadius);

        if (!_isUnlocked) graphics.drawImage(ImageName.Lock.getName(), _lockPos, _lockSize);
    }

    public void setCallback(ButtonCallback cb) {
        _cb = cb;
    }

    /**
     * Callback function
     */
    public void doSomething() {
        if (!_isUnlocked) return;
        _cb.doSomething();
    }

    public int[] getPos() {
        return _buttonPos;
    }

    public float[] getSize() {
        return _buttonSize;
    }

    public boolean clickInside(int[] clickPos) {
        return (clickPos[0] > _buttonPos[0] && clickPos[0] < (_buttonPos[0] + _buttonSize[0]) &&
                clickPos[1] > _buttonPos[1] && clickPos[1] < (_buttonPos[1] + _buttonSize[1]));
    }

    /**
     * Logic position
     */
    private final int[] _buttonPos;
    /**
     * Button size
     */
    private final float[] _buttonSize;

    /**
     * Pos to draw lock
     */
    private int[] _lockPos;
    /**
     * Size to draw lock
     */
    private float[] _lockSize;
    /**
     * Font of the text
     */
    private final Point _colorSet;

    private int[] _firstColorPos = new int[2];
    private int[] _secondColorPos = new int[2];
    private float _colorRadius;
    /**
     * Determines if the level
     * is unlocked or not
     */
    private boolean _isUnlocked;

    private final int _index;
    /**
     * Callback of the button
     */
    private ButtonCallback _cb;

}
