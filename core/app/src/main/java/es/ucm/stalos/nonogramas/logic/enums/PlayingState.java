package es.ucm.stalos.nonogramas.logic.enums;

/**
 * Information about different game states on GameState
 */
public enum PlayingState {
    Gaming(0),
    Win(1),
    GameOver(2);

    PlayingState(int i) { this.value = i; }

    public int getValue () { return value; }

    private int value;
}
