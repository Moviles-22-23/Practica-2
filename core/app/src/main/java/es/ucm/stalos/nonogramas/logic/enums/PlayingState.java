package es.ucm.stalos.nonogramas.logic.enums;

import java.io.Serializable;

/**
 * Information about different status on GameState
 */
public enum PlayingState implements Serializable {
    Gaming(0),
    Win(1),
    GameOver(2),
    NONE(3);

    PlayingState(int i) { this.value = i; }

    public int getValue () { return value; }

    private int value;
}
