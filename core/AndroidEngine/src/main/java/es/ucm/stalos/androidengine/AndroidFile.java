package es.ucm.stalos.androidengine;

import android.content.res.AssetManager;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class AndroidFile {
    public AndroidFile(String fileName, AssetManager assets)
    {
        _fileName = fileName;
        _assets = assets;
    }

    public boolean init()
    {
        try {
            InputStream is = _assets.open(_fileName);
            _br = new BufferedReader(new InputStreamReader(is));
        } catch (Exception e) {
            System.err.println("Error: --Failed loading the image " + e);
            return false;
        }

        return true;
    }

    public BufferedReader getBufferReader() {
        return _br;
    }

    String _fileName;
    BufferedReader _br;
    AssetManager _assets;
}
