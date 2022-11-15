package es.ucm.stalos.androidengine;

import android.content.res.AssetManager;

public class AndroidFileReader {
    public AndroidFileReader(AssetManager aMan)
    {
        _assetsMan = aMan;
    }

    public AndroidFile newFile(String _fileName) throws Exception {
        AndroidFile file = new AndroidFile(_fileName, _assetsMan);
        if (!file.init()) throw new Exception();

        return file;
    }

    AssetManager _assetsMan;
}
