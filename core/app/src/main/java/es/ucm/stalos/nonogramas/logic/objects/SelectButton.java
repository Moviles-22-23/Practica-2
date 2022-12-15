package es.ucm.stalos.nonogramas.logic.objects;

import es.ucm.stalos.androidengine.Graphics;
import es.ucm.stalos.nonogramas.logic.enums.ImageName;
import es.ucm.stalos.nonogramas.logic.enums.MyColor;
import es.ucm.stalos.nonogramas.logic.interfaces.ButtonCallback;

public class SelectButton {
    public SelectButton(int[] pos, float[] size, String text, String fontName, boolean unlocked) {
        _buttonPos[0] = pos[0];
        _buttonPos[1] = pos[1];

        _buttonSize[0] = size[0];
        _buttonSize[1] = size[1];

        _text = text;
        _fontName = fontName;
        _isUnlocked = unlocked;
    }

    public void render(Graphics gr) {
        gr.setColor(ColorPalette._colorSets.get(ColorPalette._currPalette).x);

        // Si el color es negro solo pinta el borde
        if (ColorPalette._currPalette == 0) gr.drawRect(_buttonPos, _buttonSize);
        else gr.fillSquare(_buttonPos, _buttonSize);

        if (!_isUnlocked) {
            gr.setColor(MyColor.DARK_GREY.get_color());
            gr.fillSquare(_buttonPos, _buttonSize);
            gr.drawImage(ImageName.Lock.getName(), _buttonPos, _buttonSize);
        } else {
            gr.setColor(MyColor.BLACK.get_color());
            gr.drawCenteredString(_text, _fontName, _buttonPos, _buttonSize);
        }
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

    public int getRows() {
        return _rows;
    }

    public int getCols() {
        return _cols;
    }
//--------------------------------------------ATTRIBUTES------------------------------------------//
    /**
     * Logic position
     */
    private final int[] _buttonPos = new int[2];
    /**
     * Button size
     */
    private final float[] _buttonSize = new float[2];
    /**
     * Determines if the level
     * is unlocked or not
     */
    private boolean _isUnlocked;
    /**
     * Grid's row number to
     * create with the button
     */
    private int _rows;
    /**
     * Grid's column number
     * to create with the button
     */
    private int _cols;
    /**
     * Text to show indise the button
     */
    private String _text;
    /**
     * Name-Key of the font to be used
     */
    private String _fontName;
    /**
     * Callback of the button
     */
    private ButtonCallback _cb;
}
