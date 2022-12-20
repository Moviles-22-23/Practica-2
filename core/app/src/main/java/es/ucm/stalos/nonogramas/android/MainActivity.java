package es.ucm.stalos.nonogramas.android;

import android.content.Context;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.view.SurfaceView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.Group;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.util.concurrent.TimeUnit;

import es.ucm.stalos.androidengine.Engine;
import es.ucm.stalos.nonogramas.R;
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
            public void onInitializationComplete(@NonNull InitializationStatus initializationStatus) {
            }
        });

        AdView adView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        _engine = new Engine();

        // SENSOR
        _sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        _sensor = new AndroidSensor(_sensorManager, _engine);

        // Cancel ReminderNotification
        NotificationManager.CancelSomeWork(this,
                getResources().getString(R.string.reminder));

        getResources().getString(R.string.reminder);

        LoadState loadAssets = new LoadState(_engine);

        if (!_engine.init(loadAssets, 360, 640, this, gameView, adGroup)) {
            System.out.println("Error al inicializar el engine");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        _engine.resume();
        _sensor.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        _engine.pause();
        _sensor.pause();

        NotificationManager.SetUpReminderNotification(this,
                getResources().getString(R.string.reminder),
                TimeUnit.SECONDS, 3);
    }

    protected Engine _engine;
    private Group adGroup;
    private SensorManager _sensorManager;
    private AndroidSensor _sensor;
}