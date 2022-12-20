package es.ucm.stalos.nonogramas.logic.objects;

import android.graphics.Point;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import es.ucm.stalos.androidengine.Graphics;
import es.ucm.stalos.androidengine.enums.TouchEvent;
import es.ucm.stalos.nonogramas.logic.enums.MyColor;
import es.ucm.stalos.nonogramas.logic.interfaces.ButtonCallback;
import es.ucm.stalos.nonogramas.logic.states.GameState;

/**
 * Manage a list of every selectColorSetButton
 */
public class ColorPalette {
    public ColorPalette(int[] pos, float[] size, GameState state) {
        this._pos = pos;
        this._size = size;
        this._state = state;
    }

    public boolean init(int lastUnlockedPack, boolean isLandscape) {
        try {
            initSelectColorSetButtons(lastUnlockedPack, isLandscape);

            // We increase the size to fix empty pixels
            _size[0] += 10;
            _size[1] += 10;
        } catch (Exception e) {
            System.out.println("Error iniciando color palette");
            return false;
        }
        return true;
    }

    public void render(Graphics graphics) {
        // Black Background a little bigger to
        graphics.setColor(MyColor.BLACK.get_color());

        graphics.fillSquare(_pos, _size);

        // Buttons
        for (SelectPaletteButton button : _selectPaletteButton) {
            button.render(graphics);
        }
    }

    private void initSelectColorSetButtons(int lastUnlockedPack, boolean isLandscape) {
        float[] bSize;// = new float[]{_size[0] / 7, _size[1]};
        if(!isLandscape){
            bSize = new float[]{_size[0] / 7, _size[1]};
        }
        else{
            bSize = new float[]{_size[0], _size[1] / 7};
        }

        for (int i = 0; i < 7; i++) {
            final int auxI = i;

            int[] bPos;
            if(!isLandscape){
                bPos = new int[]{(int)(_pos[0] + i % 7 * bSize[0]), _pos[1]};
            }else{
                bPos = new int[]{_pos[0], (int)(_pos[1] + i % 7 * bSize[1])};
            }

            boolean unlocked = lastUnlockedPack >= i;

            // Crea el boton
            final SelectPaletteButton b = new SelectPaletteButton(bPos, bSize, _colorSets.get(i), unlocked, auxI, isLandscape);

            // Cambia el callBack
            b.setCallback(new ButtonCallback() {
                @Override
                public void doSomething() {
                    _currPalette = auxI;
                    _state.updateColorPalette();
                }
            });

            _selectPaletteButton.add(b);
        }
    }

    public void handleInput(int[] clickPos, TouchEvent touch) {
        for (SelectPaletteButton button : _selectPaletteButton) {
            if (button.clickInside(clickPos)) {
                button.doSomething();
            }
        }

        System.out.println("Seleccionada la palette: " + _currPalette);
    }

    public static Map<Integer, Point> _colorSets;
    public static int _currPalette;

    private int[] _pos;
    private float[] _size;

    // Cositas
    GameState _state;

    /**
     * List of all select level buttons
     */
    protected List<SelectPaletteButton> _selectPaletteButton = new ArrayList<>();
}
