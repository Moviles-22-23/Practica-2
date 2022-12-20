package es.ucm.stalos.nonogramas.logic.enums;

/**
 * Contains information about the different platforms to which to send a msg
 */
public enum ShareType {
    TWITTER(0),
    WHATSAPP(1);

    ShareType(int type) { this._value = type; }

    public int getValue () { return _value; }

    private int _value;
}
