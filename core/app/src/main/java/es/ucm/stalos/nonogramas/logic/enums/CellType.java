package es.ucm.stalos.nonogramas.logic.enums;

import java.io.Serializable;

/**
 * Information about different cell types of the game
 */
public enum CellType implements Serializable {
    GREY(0),
    BLUE(1),
    WHITE(2),
    RED(3),
    MAX(4);

    CellType(int i) { this.value = i; }

    public int getValue () { return value; }

    private int value;
}
