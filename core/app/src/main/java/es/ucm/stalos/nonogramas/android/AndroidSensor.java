package es.ucm.stalos.nonogramas.android;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import es.ucm.stalos.androidengine.Engine;
import es.ucm.stalos.nonogramas.logic.states.GameState;

public class AndroidSensor implements SensorEventListener {
    public AndroidSensor(SensorManager sensorManager, Engine engine) {
        _sensorManager = sensorManager;
        _sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, _sensor, SensorManager.SENSOR_DELAY_NORMAL);
        _engine = engine;
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        // Detectar gesto de agitar
        final int SHAKE_SENSITIVITY = 100;
        if (sensorEvent.sensor.getType() == _sensor.getType()) {
            float x = sensorEvent.values[0];
            float y = sensorEvent.values[1];
            float z = sensorEvent.values[2];

            long currentTime = System.currentTimeMillis();

            if (currentTime - lastUpdate > _refreshMiliseconds) {

                float speed = Math.abs(x + y + z - lastX - lastY - lastZ) / (currentTime - lastUpdate) * 1000;

                if (speed > SHAKE_SENSITIVITY) {
                    // Gesto detectado
                    System.out.println("AGITAR");
                    // TODO: Callback
                    _engine.manageSensorEvent(_sensor.getType());
                }

                lastUpdate = currentTime;
                lastX = x;
                lastY = y;
                lastZ = z;
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    public void resume() {
        _sensorManager.registerListener(this, _sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void pause() {
        _sensorManager.unregisterListener(this);
    }

    private SensorManager _sensorManager;
    private Sensor _sensor;
    private Engine _engine;
    private long lastUpdate = 0;
    private int _refreshMiliseconds = 100;
    private float lastX, lastY, lastZ;
}
