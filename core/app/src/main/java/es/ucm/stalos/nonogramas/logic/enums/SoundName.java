package es.ucm.stalos.nonogramas.logic.enums;

/**
 * Enum which contains the information of the name of
 * every Sound Asset of the game. It is used as for
 * creating new sounds from audio as for playing
 */
public enum SoundName {
    MainTheme(0),
    MenuTheme(1),
    ClickSound(2),
    FailSound(3),
    GoodSound(4),
    WindSound(5);

    SoundName(int i) {
        this._value = i;
        switch (i) {
            case 0:
                _name = "mainTheme";
                _fileName = "mainTheme.wav";
                break;
            case 1:
                _name = "menuTheme";
                _fileName = "menuTheme.wav";
                break;
            case 2:
                _name = "clickSound";
                _fileName = "clickSound.wav";
                break;
            case 3:
                _name = "failSound";
                _fileName = "failSound.wav";
                break;
            case 4:
                _name = "goodSound";
                _fileName = "goodSound.wav";
                break;
            case 5:
                _name = "windSound";
                _fileName = "winSound.wav";
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
