package es.ucm.stalos.nonogramas.logic.objects;

import java.util.ArrayList;
import java.util.List;

import es.ucm.stalos.androidengine.Engine;
import es.ucm.stalos.androidengine.Font;
import es.ucm.stalos.androidengine.Graphics;
import es.ucm.stalos.androidengine.State;
import es.ucm.stalos.androidengine.TouchEvent;
import es.ucm.stalos.nonogramas.logic.Assets;
import es.ucm.stalos.nonogramas.logic.data.GameData;
import es.ucm.stalos.nonogramas.logic.interfaces.ButtonCallback;

public class ColorPalette {
    public ColorPalette(int [] pos, float [] size) {
        this._pos = pos;
        this._size = size;
    }

    public boolean init(Engine engine, GameData data) {
        try {
            _engine = engine;

            // Text
            _font = engine.getGraphics().newFont("JosefinSans-Bold.ttf", _fontSize, true);
            _textSize[0] = engine.getGraphics().getLogWidth() * 0.7f;
            _textSize[1] = engine.getGraphics().getLogHeight() * 0.1f;
            _textPos[0] = (int) ((engine.getGraphics().getLogWidth() - _textSize[0]) * 0.5f);
            _textPos[1] = (int) ((engine.getGraphics().getLogHeight() - _textSize[1]) * 0.2f);

//            initColorSets();
            initSelectColorSetButtons();

        } catch (Exception e) {
            System.out.println("Error iniciando color palette");
            return false;
        }
        return true;
    }

    public void render(Graphics graphics) {

        // Black Background
        graphics.setColor(0x000000FF);
        graphics.fillSquare(_pos, _size);

        // Buttons
        for (SelectColorSetButton button : _selectColorSetButton) {
            button.render(graphics);
        }
    }

    private void initSelectColorSetButtons() throws Exception {
        Graphics graphics = _engine.getGraphics();

        int[] pos = new int[2];
        float[] size = new float[2];

        for(int i = 0; i < 7; i++){
            int[] bPos = new int[]{ _pos[0] + graphics.getLogWidth() * i / 7, _pos[1] };
            float[] bSize = new float[]{ _size[0] / 7, _size[1] };

            // Crea el boton
            final SelectColorSetButton b = new SelectColorSetButton(bPos, bSize, Assets.colorSets.get(i));

            final int auxI = i;

            // Cambia el callBack
            b.setCallback(new ButtonCallback() {
                @Override
                public void doSomething() {
                    Assets.currPalette = auxI;
                    _paletteSelected = auxI;
                    System.out.println("Pulsado el boton: " + auxI);
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

        System.out.println("Seleccionada la palette: " + _paletteSelected);
    }

    private int _paletteSelected = 0;

    private int[] _pos;
    private float[] _size;

    // Cositas
    State _state;
    Engine _engine;

    // Texto
    private Font _font;
    private int _fontSize = 10;

    private String _text = "COLOR PALETTE";
    private int[] _textPos = new int[2];
    private float[] _textSize = new float[2];

    /**
     * List of all select level buttons
     */
    protected List<SelectColorSetButton> _selectColorSetButton = new ArrayList<>();
}
