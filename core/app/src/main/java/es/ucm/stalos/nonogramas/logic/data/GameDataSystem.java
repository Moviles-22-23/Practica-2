package es.ucm.stalos.nonogramas.logic.data;

import android.app.Application;
import android.content.res.AssetManager;
import android.os.Environment;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import es.ucm.stalos.androidengine.SerializableSystem;
import es.ucm.stalos.nonogramas.logic.enums.GridType;

public class GameDataSystem implements SerializableSystem {

    public GameDataSystem(AppCompatActivity context, AssetManager assets) {
        _context = context;
        _assets = assets;
    }

    @Override
    public boolean saveData() {
        try {
            // 1. If there is no data, it is created
            if(_data == null)
                _data = new GameData();
            // 2. Writing/Creating the file data
            FileOutputStream file = _context.openFileOutput("data.json", _context.MODE_PRIVATE);
            ObjectOutputStream out = new ObjectOutputStream(file);
            out.writeObject(_data);
            // 3. Close the streams
            out.close();
            file.close();
        } catch (Exception e) {
            System.out.println("Error guardar los datos");
            System.err.println(e);
            return false;
        }

        return true;
    }

    @Override
    public boolean loadData() {
        try
        {
            // 1. If the file doesn't exist, it is created
            File f = _context.getFileStreamPath("data.json");
            if(!f.exists())
                saveData();
            // 2. Reading the object from a file
            FileInputStream file = _context.openFileInput("data.json");
            System.out.println(file.getFD());
            ObjectInputStream in = new ObjectInputStream(file);
            // 3. Method for deserialization of object
            _data = (GameData) in.readObject();
            // 4. Close the streams
            in.close();
            file.close();

            System.out.println("Object has been deserialized ");
        } catch (Exception e) {
            System.out.println("Error al cargar los datos");
            System.err.println(e);
            return false;
        }
        //_data = new GameData();
        return true;
    }

    public GameData _data;
    private String _fileName = "/data.json";
    private AssetManager _assets;
    private AppCompatActivity _context;
}
