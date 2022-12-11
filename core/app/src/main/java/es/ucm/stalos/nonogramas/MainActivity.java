package es.ucm.stalos.nonogramas;

import android.os.Build;
import android.os.Bundle;
import android.view.SurfaceView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.Group;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.util.Arrays;

import es.ucm.stalos.androidengine.Engine;
import es.ucm.stalos.nonogramas.logic.states.LoadState;

public class MainActivity extends AppCompatActivity {
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        SurfaceView gameView = findViewById(R.id.surfaceView);
        adGroup = findViewById(R.id.adGroup);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        AdView adView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        _engine = new Engine();
        // NOTIFICATIONS
        _pushNotification = new Notification(this, "unique_channel",
                0, "reminder");
        _pushNotification._msgs.addAll(Arrays.asList(msgs));

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

        // TODO: Crear un sistema de alarma para lanzar la notificacion cada cierto tiempo
        _pushNotification.showNotification();
    }

    protected Engine _engine;
    private Group adGroup;
    private Notification _pushNotification;
    private String[] msgs =
            {
                    "Te echamos de menos. Nuevos retos te esperan \uD83D\uDE0A",
                    "No pierdas más el tiempo. Enfréntate a la aventura \uD83D\uDE00",
                    "Parece que llevas un tiempo sin jugar. " +
                            "Es hora de ejercitar un poco el coco \uD83D\uDE00"
            };
}