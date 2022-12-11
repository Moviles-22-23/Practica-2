package es.ucm.stalos.nonogramas.logic.enums;

/**
 * Enum which contains the information of the name of
 * every Font Asset of the game. It is used as for
 * creating new fonts from graphics as for drawing
 */
public enum FontName {
    DefaultFont(0),
    TitleMainMenu(1),
    ButtonMainMenu(2),
    RowColNumber(3),
    LevelNumber(4),
    GameStateButton(5),
    GameStateText(6),
    HintFont(7),
    FigureName(8)
    ;

    FontName(int i) {
        this._value = i;
        switch (i) {
            case 0:
                _name = "defaultFont";
                _fileName = "JosefinSans-Bold.ttf";
                break;
            case 1:
                _name = "titleMainMenu";
                _fileName = "Molle-Regular.ttf";
                break;
            case 2:
                _name = "buttonMainMenu";
                _fileName = "JosefinSans-Bold.ttf";
                break;
            case 3:
                _name = "rowColNumber";
                _fileName = "Molle-Regular.ttf";
                break;
            case 4:
                _name = "levelNumber";
                _fileName = "Molle-Regular.ttf";
                break;
            case 5:
                _name = "gameStateButton";
                _fileName = "JosefinSans-Bold.ttf";
                break;
            case 6:
                _name = "gameStateText";
                _fileName = "JosefinSans-Bold.ttf";
                break;
            case 7:
                _name = "hintFont";
                _fileName = "JosefinSans-Bold.ttf";
                break;
            case 8:
                _name = "figureName";
                _fileName = "JosefinSans-Bold.ttf";
                break;
        }
    }

    public int getValue() {
        return _value;
    }

    public String getName() {
        return _name;
    }

    public String getFileName() {
        return _fileName;
    }

    private int _value;
    private String _name;
    private String _fileName;
}
