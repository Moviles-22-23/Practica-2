package es.ucm.stalos.nonogramas.logic.enums;

/**
 * Enum which contains the information of the name of
 * every Image Asset of the game. It is used as for
 * creating new images from graphics as for drawing
 */
public enum ImageName {
    BackArrow(0),
    Lens(1),
    Heart(2),
    Lock(3),
    GameOver(4),
    Ads(5);

    ImageName(int i) {
        this._value = i;
        switch (i) {
            case 0:
                _name = "backArrow";
                _fileName = "backArrow.png";
                break;
            case 1:
                _name = "lents";
                _fileName = "lents.png";
                break;
            case 2:
                _name = "heart";
                _fileName = "heart.png";
                break;
            case 3:
                _name = "lock";
                _fileName = "lock.png";
                break;
            case 4:
                _name = "gameOver";
                _fileName = "gameOver.png";
                break;
            case 5:
                _name = "ads";
                _fileName = "ads.png";
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
