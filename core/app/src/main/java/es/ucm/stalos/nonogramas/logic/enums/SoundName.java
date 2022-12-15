package es.ucm.stalos.nonogramas.logic.enums;

/**
 * Enum which contains the information of the name of
 * every Sound Asset of the game. It is used as for
 * creating new sounds from audio as for playing
 */
public enum SoundName {
    MainTheme("MainTheme"),
    MenuTheme("MenuTheme"),
    ClickSound("ClickSound"),
    FailSound("FailSound"),
    GoodSound("GoodSound"),
    WindSound("WindSound");

    SoundName(String name) {
        this._name = name;
    }

    public String getName() {
        return _name;
    }

    private String _name;
}
