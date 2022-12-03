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
            FileOutputStream file = _context.openFileOutput("data.json", _context.MODE_PRIVATE);
            ObjectOutputStream out = new ObjectOutputStream(file);
            //_data = new GameData();
            out.writeObject(_data);

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
            // Reading the object from a file
            FileInputStream file = _context.openFileInput("data.json");
            ObjectInputStream in = new ObjectInputStream(file);
            // Method for deserialization of object
            _data = (GameData) in.readObject();
            in.close();
            file.close();
            System.out.println("Object has been deserialized ");
        } catch (Exception e) {
            System.out.println("Error al cargar los datos");
            System.err.println(e);
            return false;
        }

        return true;
    }

    public GameData _data;
    private String _fileName = "/data.json";
    private AssetManager _assets;
    private AppCompatActivity _context;
}
