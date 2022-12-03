package es.ucm.stalos.nonogramas;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.io.File;

import es.ucm.stalos.androidengine.Engine;
import es.ucm.stalos.nonogramas.logic.states.LoadState;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
//
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.MANAGE_EXTERNAL_STORAGE) ==
//                PackageManager.PERMISSION_GRANTED) {
//            // Si tenemos permisos, podemos usar la API solicitada.
//        } else {
//            // Pedimos permisos al usuario.
//            // Lo correcto es explicar al usuario
//            // antes el porqué son necesarios
//            requestPermissions(
//                    this,
//                    new String[]{Manifest.permission.MANAGE_EXTERNAL_STORAGE},
//                    REQUEST_CODE);
//        }
//
//        askForPermissions();
        _engine = new Engine();
        LoadState loadAssets = new LoadState(_engine);

        if (!_engine.init(loadAssets, 400, 600, this)) {
            System.out.println("Error al inicializar el engine");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        _engine.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        _engine.pause();
    }

    private void askForPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                startActivity(intent);
                return;
            }
            createDir();
        }
    }

    private void createDir() {
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    protected Engine _engine;
    public static File dir = new File(new File(Environment.getExternalStorageDirectory(), "bleh"),
            "bleh");
}