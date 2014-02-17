package com.ejemplo.sensores;

import java.util.List;

import com.estudios_kelon.t5_sensores.R;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

public class SensoresActivity extends Activity implements SensorEventListener {

	private TextView salida;

	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sensores);
		salida = (TextView) findViewById(R.id.salida);
		SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		List<Sensor> listaSensores = sensorManager
				.getSensorList(Sensor.TYPE_ALL);
		for (Sensor sensor : listaSensores) {
			log(sensor.getName());
		}
		listaSensores = sensorManager.getSensorList(Sensor.TYPE_ORIENTATION);

		if (!listaSensores.isEmpty()) {
			Sensor orientationSensor = listaSensores.get(0);
			sensorManager.registerListener(this, orientationSensor,
					SensorManager.SENSOR_DELAY_UI);
		}
		listaSensores = sensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER);
		if (!listaSensores.isEmpty()) {
			Sensor acelerometerSensor = listaSensores.get(0);
			sensorManager.registerListener(this, acelerometerSensor,
					SensorManager.SENSOR_DELAY_UI);
		}
		listaSensores = sensorManager.getSensorList(Sensor.TYPE_MAGNETIC_FIELD);
		if (!listaSensores.isEmpty()) {
			Sensor magneticSensor = listaSensores.get(0);
			sensorManager.registerListener(this, magneticSensor,
					SensorManager.SENSOR_DELAY_UI);
		}
		listaSensores = sensorManager.getSensorList(Sensor.TYPE_TEMPERATURE);
		if (!listaSensores.isEmpty()) {
			Sensor temperatureSensor = listaSensores.get(0);
			sensorManager.registerListener(this, temperatureSensor,
					SensorManager.SENSOR_DELAY_UI);
		}
	}

	private void log(String string) {
		salida.append(string + "\n");
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int precision) {
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onSensorChanged(SensorEvent evento) {
		synchronized (this) {
			switch (evento.sensor.getType()) {
			case Sensor.TYPE_ORIENTATION:
				for (int i = 0; i < 3; i++) {
					log("Orientaci�n " + i + ": " + evento.values[i]);
				}
				break;
			case Sensor.TYPE_ACCELEROMETER:
				for (int i = 0; i < 3; i++) {
					log("Aceler�metro " + i + ": " + evento.values[i]);
				}
				break;
			case Sensor.TYPE_MAGNETIC_FIELD:
				for (int i = 0; i < 3; i++) {
					log("Magnetismo " + i + ": " + evento.values[i]);
				}
				break;
			default:
				for (int i = 0; i < evento.values.length; i++) {
					log("Temperatura " + i + ": " + evento.values[i]);
				}
			}
		}
	}
}
