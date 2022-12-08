package es.ucm.stalos.nonogramas.logic.objects;

public class ColorSet {
    ColorSet(int first, int second){
        this._first = first;
        this._second = second;
    }

    public int getFirst() { return _first; }

    public int getSecond() { return _second; }


    private int _first;
    private int _second;
}
