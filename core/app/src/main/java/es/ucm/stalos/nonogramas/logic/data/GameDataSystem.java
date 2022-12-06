package es.ucm.stalos.nonogramas.logic.data;

import android.content.Context;
import android.content.res.AssetManager;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
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

            System.out.println("--Data loaded correctly--");
        } catch (Exception e) {
            System.out.println("Error al cargar los datos. Generando unos nuevos");
            System.err.println(e);
            _data = new GameData();
        }

        return true;
    }

    /**
     * Generate wrong data in order to test the hash system.
     * Only use it for debugging
     */
    public void loadWrongData() {
        try {
            // 1. If the file doesn't exist, it is created
            File f = _context.getFileStreamPath(_fileName);
            if (!f.exists())
                loadData();
            // 2. Creating fake data
            FileOutputStream file = _context.openFileOutput(_fileName, Context.MODE_PRIVATE);
            ObjectOutputStream out = new ObjectOutputStream(file);
            _data = new GameData();
            _data._lastUnlockedPack = 1000;
            out.writeObject(_data);
            // 3. Close the streams
            out.close();
            file.close();
            // 4. Comparing the hashes
            if (!isTheSameHash()) {
                throw new Exception("Los hashes no coinciden");
            }
        } catch (Exception e) {
            System.out.println("Error al cargar los datos. Generando unos nuevos");
            System.err.println(e);
            _data = new GameData();
        }
    }
    //-----------------------------------HASH-MANAGE--------------------------------------//

    /**
     * Read the store hash and compare it with the
     * hash generated from the store data
     *
     * @return true if the hashes are equal
     */
    private boolean isTheSameHash() {
        try {
            FileInputStream file = _context.openFileInput(_hashFileName);
            ObjectInputStream in = new ObjectInputStream(file);
            // 1. Store hash
            String savedHash = (String) in.readObject();
            char[] arr = savedHash.toCharArray();

            // 2. Store salt
            _salt = "";
            // 2.1 The first char of the hash is the number of
            // digits of the salt length
            _saltLengthDig = Integer.parseInt(String.valueOf(arr[0]));
            String length = "";
            for (int i = 1; i <= _saltLengthDig; i++) {
                length = String.format("%s%s", length, arr[i]);
            }
            // 2.2 Taking the salt result
            _saltLength = Integer.parseInt(length);
            for (int i = _saltLengthDig + 1; i <= _saltLength + _saltLengthDig; i++) {
                _salt = String.format("%s%s", _salt, arr[i]);
            }

            // 3. Closing streams
            file.close();
            in.close();

            // 4. Generating the hash from the store data
            createSecurePassword_SHA256();
            String dataHash = String.format("%s%s%s%s", _saltLengthDig,
                    _saltLength, _salt, getDataHash());

            // 5. Comparing the generated hashes
            return dataHash.equals(savedHash);
        } catch (Exception e) {
            System.out.println("Error al comparar los hashes");
            System.err.println(e);
        }

        return false;
    }

    /**
     * Generate a hash from the saved data and save the information in a hashFile
     */
    private void createHash() throws IOException, NoSuchAlgorithmException {
        createSalt();
        createSecurePassword_SHA256();
        String hash = String.format("%s%s%s%s", _saltLengthDig, _saltLength, _salt, getDataHash());

        // Writing/Creating hashFile
        FileOutputStream hashFile = _context.openFileOutput(_hashFileName, Context.MODE_PRIVATE);
        ObjectOutputStream out = new ObjectOutputStream(hashFile);
        out.writeObject(hash);
        out.close();
        hashFile.close();
    }

    /**
     * Generate the salt
     */
    private void createSalt() throws NoSuchAlgorithmException {
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[_saltBytes];
        sr.nextBytes(salt);

        // * As we're using _saltBytes = 16 bytes and the parameter radix = 16,
        //   we will have a salt length of 32 characters in hexadecimal, which means
        //   that we will have 64 bytes to save the salt-string content.
        // * Anyway, it's better to store the length of the salt result in order to
        //   know how many characters we will have to read during the hashes comparison
        _salt = new BigInteger(1, salt).toString(16);
        _saltLength = _salt.length();
        _saltLengthDig = Integer.toString(_saltLength).length();
    }

    /**
     * Generate a secure password with the original password
     * and the salt using the SHA256 system.
     */
    private void createSecurePassword_SHA256() {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(_salt.getBytes());
            byte[] bytes = md.digest(_originalPass.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte aByte : bytes) {
                sb.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
            }
            _securePass = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    /**
     * Read the saved data and generate a hash
     *
     * @return generated hash
     */
    private String getDataHash() throws
            NoSuchAlgorithmException,
            IOException {
        // 1. Hash method
        MessageDigest shaDigest = MessageDigest.getInstance("SHA-256");

        //2. Get the saved data file
        FileInputStream dataFile = _context.openFileInput(_fileName);

        //3. Create byte array to read data in chunks
        byte[] byteArray = new byte[2048];
        int bytesCount = dataFile.read(byteArray);

        //5. Get the hash's bytes
        shaDigest.update(byteArray);
        shaDigest.update(_securePass.getBytes());
        byte[] bytes = shaDigest.digest();

        //6. Convert it to hexadecimal format
        StringBuilder sb = new StringBuilder();
        for (byte aByte : bytes) {
            sb.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
        }

        return sb.toString();
    }

    /**
     * Data to be saved/loaded
     */
    public GameData _data;
    /**
     * Salt attributes
     */
    private String _salt;
    private int _saltBytes = 16;
    private int _saltLength;
    private int _saltLengthDig;
    /**
     * Secure password
     */
    private String _securePass;
    /**
     * File name of the GameDAta
     */
    private final String _fileName = "data.json";
    /**
     * File name of the generated hash
     */
    private final String _hashFileName = ".meta";
    /**
     * Original secret password
     */
    private final String _originalPass = "stalos_ArFerSer_moviles_22-23";

    /**
     * Reference to the AssetManager
     */
    private final AssetManager _assets;
    /**
     * Reference to the Context
     */
    private final AppCompatActivity _context;
}
