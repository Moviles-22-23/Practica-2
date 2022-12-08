package es.ucm.stalos.nonogramas.logic;

import java.util.Map;

import es.ucm.stalos.androidengine.Image;
import es.ucm.stalos.androidengine.Sound;
import es.ucm.stalos.nonogramas.logic.objects.ColorSet;

public class Assets {
    // Images
    public static Image backArrow;
    public static Image lens;
    public static Image heart;
    public static Image lock;
    public static Image gameOver;
    public static Image ads;

    // Audio
    public static Sound mainTheme;
    public static Sound menuTheme;
    public static Sound clickSound;
    public static Sound failSound;
    public static Sound goodSound;
    public static Sound winSound;

    /**
     * Dictionary of information about
     * different grid level types
     */
    public static Map<Integer, ColorSet> colorSets;

    public static int currPalette;
}