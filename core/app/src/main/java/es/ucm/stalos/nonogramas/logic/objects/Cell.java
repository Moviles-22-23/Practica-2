package es.ucm.stalos.nonogramas.logic.objects;

import java.util.HashMap;
import java.util.Map;

import es.ucm.stalos.androidengine.Graphics;
import es.ucm.stalos.androidengine.TouchEvent;
import es.ucm.stalos.nonogramas.logic.Assets;
import es.ucm.stalos.nonogramas.logic.data.GameDataSystem;
import es.ucm.stalos.nonogramas.logic.enums.CellType;

public class Cell {
    Cell(int row, int col, int[] pos, float cellSize) {
        // INDEX
        this.row = row;
        this.col = col;
        // POSITION
        this.x = pos[0];
        this.y = pos[1];
        // FILLED SQUARE POSITION
        this.fx = (int) (pos[0] + whiteMargin);
        this.fy = (int) (pos[1] + whiteMargin);
        // CELL SIZE
        this.size = cellSize;
        // FILLED SQUARE SIZE
        this.fsize = cellSize - whiteMargin * 2;
        // TYPE
        this.cellType = CellType.EMPTY;

        // COLORS - MAP
        _colors = new HashMap<>();
        _colors.put(CellType.FILL, 0x0000FFFF);
        _colors.put(CellType.EMPTY, 0xBBBBBBFF);
        _colors.put(CellType.NOFILL, 0xFFFFFFFF);
        _colors.put(CellType.RED, 0xFF0000FF);
    }

    public void render(Graphics graphics) {
        // Filled Square
        int[] fillPos = new int[]{fx, fy};
        graphics.setColor(_colors.get(cellType));

        if (cellType == CellType.FILL) graphics.setColor(Assets.colorSets.get(Assets.currPalette).getFirst());
//        if (cellType == CellType.EMPTY) graphics.setColor(Assets.secundaryColor);

        graphics.fillSquare(fillPos, fsize);

        int[] fillPos2 = new int[]{fx + (int) fsize, fy + (int) fsize};
        if (cellType == CellType.NOFILL) {
            graphics.setColor(0x000000FF);
            graphics.drawRect(fillPos, fsize);
            graphics.drawLine(fillPos, fillPos2);
        }
    }

    public void update(float deltaTime)
    {

    }

    public void handleInput(int[] clickPos, TouchEvent touch) {
        switch (touch) {
            case touchDown:
                cellType = CellType.FILL;
                break;
            case longTouch:
                cellType = CellType.NOFILL;
                break;
        }
    }

    /**
     * Cell or color type
     */
    public CellType cellType;
    /**
     * Entire logic cell position X
     */
    public int x;
    /**
     * Entire logic cell position Y
     */
    public int y;
    /**
     * Logic filled square position X
     */
    public int fx;
    /**
     * Logic filled square position Y
     */
    public int fy;
    /**
     * Row index of the cell
     * in the board
     */
    public int row;
    /**
     * Column index of the cell
     * in the board
     */
    public int col;
    /**
     * Entire cell size
     */
    public float size;
    /**
     * Filled square size
     */
    public float fsize;
    /**
     * Offset to draw the filled square
     */
    public float whiteMargin = 2;
    /**
     * Different colors of the cel
     */
    private Map<CellType, Integer> _colors;
    /**
     *
     */
    public TouchEvent _lastTouchEvent;
}
