package es.ucm.stalos.nonogramas.logic.objects;

import es.ucm.stalos.androidengine.Font;
import es.ucm.stalos.androidengine.Graphics;
import es.ucm.stalos.androidengine.Image;
import es.ucm.stalos.nonogramas.logic.Assets;
import es.ucm.stalos.nonogramas.logic.interfaces.ButtonCallback;

public class SelectColorSetButton {
    public SelectColorSetButton(int[] pos, float[] size, ColorSet colorSet) {
        this._buttonPos = pos;
        this._buttonSize = size;
        this._colorSet = colorSet;

        int margin = 5;

        this._firstColorPos[0] = _buttonPos[0] + margin;
        this._firstColorPos[1] = _buttonPos[1] + margin;
        this._firstColorSize[0] = _buttonSize[0] - 2 * margin;
        this._firstColorSize[1] = _buttonSize[0] / 2 - margin;

        this._secondColorPos[0] = _buttonPos[0] + margin;
        this._secondColorPos[1] = (int)(_buttonSize[0] / 2 + _buttonPos[1] + margin);
        this._secondColorSize[0] = _buttonSize[0] - 2 * margin;
        this._secondColorSize[1] = _buttonSize[0] / 2 - margin;

        this._isUnlocked = true;
    }

    public void render(Graphics gr) {
        gr.drawRect(_buttonPos, _buttonSize);
        if(!_isUnlocked)
        {
            gr.setColor(0x9B9B9BFF);
            gr.fillSquare(_buttonPos, _buttonSize);
            gr.drawImage(_lockImage, _buttonPos, _buttonSize);
        }
        else {
            // Fondo negro
            gr.setColor(0x000000FF);
            gr.fillSquare(_buttonPos, _buttonSize);
            // Color Primario
            gr.setColor(_colorSet.getFirst());
            gr.fillSquare(_firstColorPos, _firstColorSize);
            // Color Secundario
            gr.setColor(_colorSet.getSecond());
            gr.fillSquare(_secondColorPos, _secondColorSize);
            // Marco
            gr.setColor(0xFFFFFFFF);
            gr.drawRect(_firstColorPos, _firstColorSize);
            gr.setColor(0xFFFFFFFF);
            gr.drawRect(_secondColorPos, _secondColorSize);
        }
    }

    public void setCallback(ButtonCallback cb) {
        _cb = cb;
    }

    /**
     * Callback function
     */
    public void doSomething() {
        if(!_isUnlocked) return;
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
    private float[] _firstColorSize = new float[2];

    private int[] _secondColorPos = new int[2];
    private float[] _secondColorSize = new float[2];

    /**
     * Determines if the level
     * is unlocked or not
     */
    private boolean _isUnlocked;
    /**
     * Locked image
     */
    private Image _lockImage = Assets.lock;
    /**
     * Callback of the button
     */
    private ButtonCallback _cb;

}