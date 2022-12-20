package es.ucm.stalos.nonogramas.logic.enums;

/**
 * Enumerated to facilitate the translation between the name of a color and the value it represents
 */
public enum MyColor {
    // USEFULL COLORS
    BLACK(0x000000FF),
    RED(0xFF0000FF),
    GREEN(0x00FF00FF),
    BLUE(0x0000FFFF),
    LIGHT_GREY(0x313131FF),
    DARK_GREY(0x9B9B9BFF),
    WHITE(0xFFFFFFFF),

    // PALETTE COLORS
    ORANGE(0xF48C06FF),
    LIGHT_ORANGE(0xFFBA08FF),
    DARK_GREEN(0x007200FF),
    LIGHT_GREEN(0x70E000FF),
    SOFT_BLUE(0x00B4D8FF),
    LIGHT_BLUE(0x90E0EFFF),
    DARK_RED(0xBA181BFF),
    LIGHT_RED(0xE5383BFF),
    PURPLE(0x7B2CBFFF),
    LIGHT_PURPLE(0xC77DFFFF),
    DARK_PURPLE(0x6F2DBDFF),
    SKY_BLUE(0x00A6FBFF);

    MyColor(int color) { this._color = color; }

    public int get_color() { return _color; }

    private int _color;
}
