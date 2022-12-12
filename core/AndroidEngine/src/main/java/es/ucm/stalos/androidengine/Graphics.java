package es.ucm.stalos.androidengine;

import android.content.res.AssetManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.view.SurfaceView;

import java.util.HashMap;

public class Graphics {

    protected Graphics(int w, int h, AssetManager assetManager, SurfaceView view) {
        _logWidth = w;
        _logHeight = h;
        _logPosX = 0.0f;
        _logPosY = 0.0f;
        _assetManager = assetManager;
        _paint = new Paint();
        _images = new HashMap<>();
        _fonts = new HashMap<>();
        _surfaceView = view;
    }

    //---------------------------ABSTRACT-GRAPHICS-VIEJO------------------------------------------//

    /**
     * Calculate the scale to force a determined aspect ratio
     *
     * @return the scale factor
     */
    private float getScaleFactor() {
        float widthScale = getWidth() / _logWidth;
        float heightScale = getHeight() / _logHeight;

        // Nos interesa el tamaño más pequeño
        return Math.min(widthScale, heightScale);
    }

    /**
     * Given a position P(x, y), returns a new value into the
     * logic system of coordinates
     *
     * @param x X-axis position
     * @param y Y-axis position
     */
    public int[] logPos(int x, int y) {
        _scaleFactor = getScaleFactor();
        float offsetX = (_logWidth - (getWidth() / _scaleFactor)) / 2.0f;
        float offsetY = (_logHeight - (getHeight() / _scaleFactor)) / 2.0f;

        int newPosX = (int) ((x / _scaleFactor) + offsetX);
        int newPosY = (int) ((y / _scaleFactor) + offsetY);

        int[] newPos = new int[2];
        newPos[0] = newPosX;
        newPos[1] = newPosY;

        return newPos;
    }

    private int[] translateWindow() {
        float offsetX = (getWidth() - (_logWidth * _scaleFactor)) / 2.0f;
        float offsetY = (getHeight() - (_logHeight) * _scaleFactor) / 2.0f;

        int newPosX = (int) ((_logPosX * _scaleFactor) + offsetX);
        int newPosY = (int) ((_logPosY * _scaleFactor) + offsetY);

        int[] newPos = new int[2];
        newPos[0] = newPosX;
        newPos[1] = newPosY;

        return newPos;
    }

    public int getLogWidth() {
        return (int) _logWidth;
    }

    public int getLogHeight() {
        return (int) _logHeight;
    }

    //--------------------------------------------------------------------------------------------//

//-----------------------------------------------------------------//

    /**
     * Creates and stores a new image ready to be used
     *
     * @param name     Image's name-key to store
     * @param fileName File name of the image with extension
     * @throws Exception if the creation fails
     */
    public void newImage(String name, String fileName) throws Exception {
        Image img = new Image("images/" + fileName, _assetManager);
        if (!img.init())
            throw new Exception();

        _images.put(name, img);
    }

    /**
     * Creates and stores a new font ready to be used
     *
     * @param name     Font's name-key to store
     * @param fileName File name of the font with extension
     * @param size     Size of the font
     * @param isBold   Determines if the font will be bold
     * @throws Exception if the creation fails
     */
    public void newFont(String name, String fileName, int size, boolean isBold) throws Exception {
        Font font = new Font("fonts/" + fileName, size, isBold, _assetManager);
        if (!font.init())
            throw new Exception();

        _fonts.put(name, font);
    }

//-----------------------------------------------------------------//

    public void clear(int color) {
        setColor(color);
        fillCanvas(color);
    }

    /**
     * Recibe el color en formato RGBA
     *
     * @param color
     */
    public void setColor(int color) {
        int r = (color & 0xff000000) >> 24;
        int g = (color & 0x00ff0000) >> 16;
        int b = (color & 0x0000ff00) >> 8;
        int a = color & 0x000000ff;

        _paint.setColor(Color.argb(a, r, g, b));
    }

    public void fillCanvas(int color) {
        int r = (color & 0xff000000) >> 24;
        int g = (color & 0x00ff0000) >> 16;
        int b = (color & 0x0000ff00) >> 8;
        int a = color & 0x000000ff;

        _canvas.drawColor(Color.argb(a, r, g, b));
    }

//-----------------------------------------------------------------//

    public void drawImage(String imageName, int[] pos, float[] size) {
        if (!_images.containsKey(imageName)) {
            System.err.println("La imagen '" + imageName + "' no existe...");
            return;
        }

        Image im = _images.get(imageName);
        Rect source = new Rect(0, 0, im.getWidth(), im.getHeight());
        Rect destiny = new Rect(pos[0], pos[1], (int) (pos[0] + size[0]), (int) (pos[1] + size[1]));
        _canvas.drawBitmap(im.getBitmap(), source, destiny, null);
    }

    public void drawText(String text, String fontName, int[] pos) {
        if (!_fonts.containsKey(fontName)) {
            System.err.println("La fuente '" + fontName + "' no existe...");
            return;
        }

        Font fo = _fonts.get(fontName);
        Typeface currFont = fo.getAndroidFont();
        _paint.setTypeface(currFont);
        _paint.setTextSize(fo.getSize());
        _paint.setTextAlign(Paint.Align.LEFT);
        _canvas.drawText(text, pos[0], pos[1], _paint);

        _paint.reset();
    }

    public void drawCenteredString(String text, String fontName, int[] pos, float[] size) {
        if (!_fonts.containsKey(fontName)) {
            System.err.println("La fuente '" + fontName + "' no existe...");
            return;
        }

        Font fo = _fonts.get(fontName);
        Typeface currFont = fo.getAndroidFont();
        _paint.setTypeface(currFont);
        _paint.setTextSize(fo.getSize());
        _paint.setTextAlign(Paint.Align.CENTER);

        int xPos = (int) (pos[0] + size[0] / 2);
        int yPos = (int) ((pos[1] + size[1] / 2) - ((_paint.descent() + _paint.ascent()) / 2));
        _canvas.drawText(text, xPos, yPos, _paint);

        _paint.reset();
    }

    // TODO: Borrar esto
    // EN PC LO HACIAMOS ASI
//    @Override
//    public void drawCenteredString(String text, int[] pos, float[] size, Font font) {
//        java.awt.Font javaFont = initFont(font);
//
//        // Calculates de logic pos and size
//        int[] logicPos = finalPosition(pos[0], pos[1]);
//        int[] logicSize = finalSize(size[0], size[1]);
//
//        // Get the FontMetrics
//        FontMetrics metrics = _graphics.getFontMetrics(javaFont);
//
//        // Determine the X coordinate for the text
//        int x = logicPos[0] + ((int) logicSize[0] - metrics.stringWidth(text)) / 2;
//        // Determine the Y coordinate for the text (note we add the ascent, as in java_2D 0 is top of the screen)
//        int y = logicPos[1] + (((int) logicSize[1] - metrics.getHeight()) / 2) + metrics.getAscent();
//
//        // Drawing
//        _graphics.drawString(text, x, y);
//        _graphics.setPaintMode();
//    }

//    public void drawCircle(int[] pos, float radious){
//        _paint.setStyle(Paint.Style.STROKE);
//        _canvas.drawCircle(pos[0], pos[1], radious, _paint);
//    }

    public void drawLine(int[] start, int[] end) {
        _canvas.drawLine(start[0], start[1], end[0], end[1], _paint);
    }

    public void drawRect(int[] pos, float side) {
        _paint.setStyle(Paint.Style.STROKE);
        float[] s = {side, side};
        paintRect(pos, s);
    }

    public void drawRect(int[] pos, float[] size) {
        _paint.setStyle(Paint.Style.STROKE);
        paintRect(pos, size);
    }

    public void fillSquare(int[] pos, float side) {
        _paint.setStyle(Paint.Style.FILL);
        float[] s = {side, side};
        paintRect(pos, s);
    }

    public void fillSquare(int[] pos, float[] size) {
        _paint.setStyle(Paint.Style.FILL);
        paintRect(pos, size);
    }

    public void drawCircle(int pos[], float radius) {
        _paint.setStyle(Paint.Style.STROKE);
        paintCircle(pos, radius);
    }

    public void fillCircle(int pos[], float radius) {
        _paint.setStyle(Paint.Style.FILL);
        paintCircle(pos, radius);
    }

    /**
     * Support function to drawRect(...)
     */
    private void paintRect(int[] pos, float[] size) {
        _paint.setStrokeWidth(_rectThick);
        _canvas.drawRect(pos[0], pos[1], pos[0] + size[0], pos[1] + size[1], _paint);
        _paint.reset();
    }

    /**
     * Support function to drawCircle(...)
     */
    private void paintCircle(int[] pos, float radius) {
        _paint.setStrokeWidth(_rectThick);
        _canvas.drawCircle(pos[0], pos[1], radius, _paint);
        _paint.reset();
    }
//----------------------------------------------------------------//

    public int getWidth() {
        return _surfaceView.getWidth();
    }

    public int getHeight() {
        return _surfaceView.getHeight();
    }

//----------------------------------------------------------------//

    public void prepareFrame() {
        while (!_surfaceView.getHolder().getSurface().isValid()) {
            System.out.println("PREPARE FRAME: NULL");
        }

        _canvas = _surfaceView.getHolder().lockCanvas();
        // SCALE & TRANSLATE
        _scaleFactor = getScaleFactor();
        int[] newPos = translateWindow();
        translate(newPos[0], newPos[1]);
        scale(_scaleFactor, _scaleFactor);
    }

    public void translate(int x, int y) {
        _canvas.translate(x, y);
    }

    public void scale(float x, float y) {
        _canvas.scale(x, y);
    }

    public void restore() {
        _surfaceView.getHolder().unlockCanvasAndPost(_canvas);
    }

    //----------------------------------------------------------------//
    // VARIABLES
    private final Paint _paint;
    private final AssetManager _assetManager;
    private SurfaceView _surfaceView;
    private Canvas _canvas;

    /**
     * Physic scale
     */
    private int _width, _height;
    /**
     * Thickness of the rect lines
     */
    private final float _rectThick = 2.5f;
    /**
     * Logic position
     */
    private float _logPosX, _logPosY;
    /**
     * Logic scale
     */
    private float _logWidth, _logHeight;
    /**
     * Scale factor
     */
    private float _scaleFactor;
    /**
     * Dictionary which contains the images
     */
    private HashMap<String, Image> _images;
    /**
     * Dictionary which contains the fonts
     */
    private HashMap<String, Font> _fonts;
}