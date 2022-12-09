package es.ucm.stalos.nonogramas.logic.objects;

import es.ucm.stalos.androidengine.Font;
import es.ucm.stalos.androidengine.Graphics;
import es.ucm.stalos.androidengine.Image;
import es.ucm.stalos.nonogramas.logic.Assets;
import es.ucm.stalos.nonogramas.logic.enums.GridType;
import es.ucm.stalos.nonogramas.logic.enums.MyColor;
import es.ucm.stalos.nonogramas.logic.interfaces.ButtonCallback;

public class SelectButton {
    public SelectButton(int[] pos, float[] size, String text, Font font, boolean unlocked) {
        _buttonPos[0] = pos[0];
        _buttonPos[1] = pos[1];

        _buttonSize[0] = size[0];
        _buttonSize[1] = size[1];

        this._text = text;

//        initType(gridType);

        _font = font;

        _isUnlocked = unlocked;
    }

    /**
     * Initialize the buttonType
     */
//    private void initType(GridType gridType) {
//        _rows = gridType.getRows();
//        _cols = gridType.getCols();
//        _text = gridType.getText();
//        System.out.println("Creado boton con texto " + _text);
//    }

    public void render(Graphics gr) {
        gr.setColor(Assets.colorSets.get(Assets.currPalette).getFirst());

        // Si el color es negro solo pinta el borde
        if(Assets.currPalette == 0) gr.drawRect(_buttonPos, _buttonSize);
        // Si no rellena el boton entero
        else gr.fillSquare(_buttonPos, _buttonSize);

        if(!_isUnlocked)
        {
            gr.setColor(MyColor.GREY_HARD.getValue());
            gr.fillSquare(_buttonPos, _buttonSize);
            gr.drawImage(_lockImage, _buttonPos, _buttonSize);
        }
        else {
            gr.setColor(MyColor.BLACK.getValue());
            gr.drawCenteredString(_text, _buttonPos, _buttonSize, _font);
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

    public int getRows() {
        return _rows;
    }

    public int getCols() {
        return _cols;
    }

    /**
     * Logic position
     */
    private final int[] _buttonPos = new int[2];
    /**
     * Button size
     */
    private final float[] _buttonSize = new float[2];
    /**
     * Font of the text
     */
    private final Font _font;
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
     * Callback of the button
     */
    private ButtonCallback _cb;
}
