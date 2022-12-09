package es.ucm.stalos.nonogramas;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.view.SurfaceView;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.Group;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.io.File;

import es.ucm.stalos.androidengine.Engine;
import es.ucm.stalos.nonogramas.logic.states.LoadState;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        SurfaceView gameView = findViewById(R.id.surfaceView);
        adGroup = findViewById(R.id.adGroup);

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
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        AdView adView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        _engine = new Engine();
        LoadState loadAssets = new LoadState(_engine);

        if (!_engine.init(loadAssets, 400, 600, this, gameView, adGroup)) {
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
    private Group adGroup;
}