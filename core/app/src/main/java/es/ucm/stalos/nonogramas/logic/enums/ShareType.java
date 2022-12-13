package es.ucm.stalos.nonogramas.logic.enums;

public enum ShareType {
    TWITTER(0),
    WHATSAPP(1),
    TELEGRAM(2);

    ShareType(int type) { this._value = type; }

    public int getValue () { return _value; }

    private int _value;
}
