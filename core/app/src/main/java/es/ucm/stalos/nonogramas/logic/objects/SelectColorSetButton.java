package es.ucm.stalos.nonogramas.logic.objects;

import es.ucm.stalos.androidengine.Graphics;
import es.ucm.stalos.nonogramas.logic.Assets;
import es.ucm.stalos.nonogramas.logic.enums.ImageName;
import es.ucm.stalos.nonogramas.logic.enums.MyColor;
import es.ucm.stalos.nonogramas.logic.interfaces.ButtonCallback;

public class SelectColorSetButton {
    public SelectColorSetButton(int[] pos, float[] size, ColorSet colorSet, boolean _isUnlocked, int index) {
        this._buttonPos = pos;
        this._buttonSize = size;
        this._colorSet = colorSet;

        int margin = 10;

        this._firstColorPos[0] = (int) (_buttonPos[0] + _buttonSize[0] * 0.5f);
        this._firstColorPos[1] = (int) (_buttonPos[1] + _buttonSize[1] * 0.25f);

        this._secondColorPos[0] = (int) (_buttonPos[0] + _buttonSize[0] * 0.5f);
        this._secondColorPos[1] = (int) (_buttonPos[1] + _buttonSize[1] * 0.75f);

        this._colorRadius = (_buttonSize[0] / 2) - margin;

        this._isUnlocked = _isUnlocked;
        this._index = index;
    }

    public void render(Graphics graphics) {
        // Fondo negro
        if (Assets.currPalette == _index) graphics.setColor(_colorSet.getSecond());
        else graphics.setColor(MyColor.BLACK.getValue());
        graphics.fillSquare(_buttonPos, _buttonSize);
        // Color Primario
        graphics.setColor(_colorSet.getFirst());
        graphics.fillCircle(_firstColorPos, _colorRadius);
        // Marco color primario (Si es negro pone borde blanco)
        if (_index == 0) graphics.setColor(MyColor.WHITE.getValue());
        else graphics.setColor(MyColor.BLACK.getValue());
        graphics.drawCircle(_firstColorPos, _colorRadius);
        // Color Secundario
        graphics.setColor(_colorSet.getSecond());
        graphics.fillCircle(_secondColorPos, _colorRadius);
        graphics.setColor(MyColor.BLACK.getValue());
        graphics.drawCircle(_secondColorPos, _colorRadius);
        // Marco
//        gr.setColor(MyColor.WHITE.getValue());
//        gr.setColor(MyColor.WHITE.getValue());

        if (!_isUnlocked) graphics.drawImage(ImageName.Lock.getName(),
                _buttonPos, _buttonSize);
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
     * Font of the text
     */
    private final ColorSet _colorSet;

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
