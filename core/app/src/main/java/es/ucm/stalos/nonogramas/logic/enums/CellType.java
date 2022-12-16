package es.ucm.stalos.nonogramas.logic.enums;

import java.io.Serializable;

/**
 * Information about different cell types of the game
 */
public enum CellType implements Serializable {
    EMPTY(0),
    FILL(1),
    NOFILL(2),
    RED(3),
    MAX(4);

    CellType(int type) { this._type = type; }

    public int getType() { return _type; }

    private int _type;
}
