package es.ucm.stalos.nonogramas.logic.data;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import es.ucm.stalos.androidengine.SerializableSystem;

public class GameDataSystem implements SerializableSystem {

    public GameDataSystem()
    {

    }

    @Override
    public boolean saveData() {
        try {
            FileOutputStream file = new FileOutputStream(_fileName);
            ObjectOutputStream out = new ObjectOutputStream(file);

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
        //try
        //{
        //    // Reading the object from a file
        //    FileInputStream file = new FileInputStream(_fileName);
        //    ObjectInputStream in = new ObjectInputStream(file);
        //    // Method for deserialization of object
        //    _data = (GameData) in.readObject();
        //    in.close();
        //    file.close();
        //    System.out.println("Object has been deserialized ");
        //} catch (Exception e) {
        //    System.out.println("Error al cargar los datos");
        //    System.err.println(e);
        //    return false;
        //}

        _data._lastUnlockedPack = 0;
        _data._lastUnlockedLevel = 0;
        _data._inGame = false;
        return true;
    }

    public static GameData _data;
    private String _fileName = "data.json";
}
