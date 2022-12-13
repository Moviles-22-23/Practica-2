package es.ucm.stalos.nonogramas.logic.objects;

import android.graphics.Point;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import es.ucm.stalos.androidengine.Graphics;
import es.ucm.stalos.androidengine.TouchEvent;
import es.ucm.stalos.nonogramas.logic.enums.MyColor;
import es.ucm.stalos.nonogramas.logic.interfaces.ButtonCallback;
import es.ucm.stalos.nonogramas.logic.states.GameState;

public class ColorPalette {
    public ColorPalette(int[] pos, float[] size, GameState state) {
        this._pos = pos;
        this._size = size;
        this._state = state;
    }

    public boolean init(int lastUnlockedPack, int logW, int logH) {
        try {
//            initColorSets();
            initSelectColorSetButtons(logW, lastUnlockedPack);

        } catch (Exception e) {
            System.out.println("Error iniciando color palette");
            return false;
        }
        return true;
    }

    public void render(Graphics graphics) {
        // Black Background
        graphics.setColor(MyColor.BLACK.getValue());
        graphics.fillSquare(_pos, _size);

        // Buttons
        for (SelectColorSetButton button : _selectColorSetButton) {
            button.render(graphics);
        }
    }

    private void initSelectColorSetButtons(int logW, int lastUnlockedPack) {

        int[] pos = new int[2];
        float[] size = new float[2];

        for (int i = 0; i < 7; i++) {
            final int auxI = i;

            int[] bPos = new int[]{_pos[0] + logW * i / 7, _pos[1]};
            float[] bSize = new float[]{_size[0] / 7, _size[1]};

            boolean unlocked = lastUnlockedPack >= i;

            // Crea el boton
            final SelectColorSetButton b = new SelectColorSetButton(bPos, bSize,
                    _colorSets.get(i), unlocked, auxI);


            // Cambia el callBack
            b.setCallback(new ButtonCallback() {
                @Override
                public void doSomething() {
                    _currPalette = auxI;
                    _state.updateColorPalette();
                }
            });

            _selectColorSetButton.add(b);
        }
    }

    public void handleInput(int[] clickPos, TouchEvent touch) {
        for (SelectColorSetButton button : _selectColorSetButton) {
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
    protected List<SelectColorSetButton> _selectColorSetButton = new ArrayList<>();
}
