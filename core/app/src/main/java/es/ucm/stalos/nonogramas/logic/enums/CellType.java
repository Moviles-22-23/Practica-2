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

    CellType(int i) { this.value = i; }

    public int getValue () { return value; }

    private int value;
}
