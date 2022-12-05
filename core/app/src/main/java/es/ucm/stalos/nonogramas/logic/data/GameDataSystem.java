package es.ucm.stalos.nonogramas.logic.data;

import android.content.Context;
import android.content.res.AssetManager;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import es.ucm.stalos.androidengine.SerializableSystem;

public class GameDataSystem implements SerializableSystem {

    public GameDataSystem(AppCompatActivity context, AssetManager assets) {
        _context = context;
        _assets = assets;
    }

    //-----------------------------------LOAD-SAVE---------------------------------------//
    @Override
    public boolean saveData() {
        try {
            // 1. If there is no data, it is created
            if (_data == null)
                _data = new GameData();
            // 2. Writing/Creating the file data
            FileOutputStream file = _context.openFileOutput(_fileName, Context.MODE_PRIVATE);
            ObjectOutputStream out = new ObjectOutputStream(file);
            out.writeObject(_data);
            //out.writeObject(_data);
            // 3. Close the streams
            out.close();
            file.close();
            // 4. Writing/Creating Hash
            createHash();
        } catch (Exception e) {
            System.out.println("Error guardar los datos");
            System.err.println(e);
            return false;
        }

        return true;
    }

    @Override
    public boolean loadData() {
        try {
            // 1. If the file doesn't exist, it is created
            File f = _context.getFileStreamPath(_fileName);
            if (!f.exists())
                saveData();
            // 2. Reading the object from a file
            FileInputStream file = _context.openFileInput(_fileName);
            ObjectInputStream in = new ObjectInputStream(file);
            // 3. Method for deserialization of object
            _data = (GameData) in.readObject();
            // 4. Close the streams
            in.close();
            file.close();
            // 5. Comparing the hashes
            if (!isTheSameHash()) {
                throw new Exception("Los hashes no coinciden");
            }

            String dataHash = getDataHash();

            System.out.println("Object has been deserialized ");
        } catch (Exception e) {
            System.out.println("Error al cargar los datos. Generando unos nuevos");
            System.err.println(e);
            _data = new GameData();
        }

        return true;
    }

    private boolean isTheSameHash() {
        try {
            String dataHash = getDataHash();
            FileInputStream file = _context.openFileInput(_hashFileName);
            ObjectInputStream in = new ObjectInputStream(file);
            String savedHash = (String) in.readObject();
            in.close();
            file.close();
            return dataHash == savedHash;
        } catch (Exception e) {
            System.out.println("Error al comparar los hashes");
            System.err.println(e);
        }

        return false;
    }
    //-----------------------------------HASH-MANAGE--------------------------------------//

    /**
     * Generate a hash from the saved data and save the information in a hashFile
     *
     * @throws IOException
     * @throws NoSuchAlgorithmException
     */
    private void createHash() throws IOException, NoSuchAlgorithmException {
        String hash = getDataHash();

        // Writing/Creating hashFile
        FileOutputStream hashFile = _context.openFileOutput(_hashFileName, Context.MODE_PRIVATE);
        ObjectOutputStream out = new ObjectOutputStream(hashFile);
        out.writeObject(hash);
        out.close();
        hashFile.close();

        System.out.println("HASH: " + hash);
    }

    /**
     * Generate the salt
     *
     * @return the salt
     * @throws NoSuchAlgorithmException
     */
    private static String getSalt() throws NoSuchAlgorithmException {
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[16];
        sr.nextBytes(salt);

        return salt.toString();
    }

    /**
     * Generate a secure password with the original password and the salt
     *
     * @param passwordToHash Original password
     * @param salt           Generated salt
     * @return the secure password
     */
    private static String getSHA256SecurePassword(String passwordToHash,
                                                  String salt) {
        String generatedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt.getBytes());
            byte[] bytes = md.digest(passwordToHash.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte aByte : bytes) {
                sb.append(Integer.toString((aByte & 0xff) + 0x100, 16)
                        .substring(1));
            }
            generatedPassword = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return generatedPassword;
    }

    /**
     * Read the saved data and generate a hash
     *
     * @return generated hash
     * @throws NoSuchAlgorithmException
     * @throws IOException
     */
    private String getDataHash() throws
            NoSuchAlgorithmException,
            IOException {
        // 1. Hash method, salt and pass
        MessageDigest shaDigest = MessageDigest.getInstance("SHA-256");
        String salt = getSalt();
        String securePassword = getSHA256SecurePassword(_pass, salt);

        //2. Get the saved data file
        FileInputStream dataFile = _context.openFileInput(_fileName);

        //3. Create byte array to read data in chunks
        //int totalBytes = bytesToSerialize();
        //byte[] byteArray = new byte[totalBytes];
        byte[] byteArray = new byte[2048];
        int bytesCount = dataFile.read(byteArray);

        //5. Get the hash's bytes
        shaDigest.update(byteArray);
        shaDigest.update(securePassword.getBytes());
        byte[] bytes = shaDigest.digest();

        //6. Convert it to hexadecimal format
        StringBuilder sb = new StringBuilder();
        for (byte aByte : bytes) {
            sb.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
        }

        return sb.toString();
    }

    public GameData _data;
    private String _fileName = "data.json";
    private String _hashFileName = ".meta";
    private AssetManager _assets;
    private AppCompatActivity _context;
    private String _pass = "stalos_ArFerSer_moviles_22-23";
}
