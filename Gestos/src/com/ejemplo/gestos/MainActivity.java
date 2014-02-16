package com.ejemplo.gestos;

import java.util.ArrayList;

import com.ejemplo.t5_gestures.R;

import android.app.Activity;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.GestureOverlayView.OnGesturePerformedListener;
import android.gesture.Prediction;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends Activity implements OnGesturePerformedListener {
	private GestureLibrary biblioteca;
	private TextView texto;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		biblioteca = GestureLibraries.fromRawResource(this, R.raw.gestures);
		if (!biblioteca.load()) {
			finish();
		}
		GestureOverlayView gesturesView = (GestureOverlayView) findViewById(R.id.gestures);
		gesturesView.addOnGesturePerformedListener(this);
		texto = (TextView) findViewById(R.id.texto);
	}

	public void onGesturePerformed(GestureOverlayView ov, Gesture gesture) {
		ArrayList<Prediction> predictions = biblioteca.recognize(gesture);
		texto.setText("");
		for (Prediction prediction : predictions) {
			texto.append(prediction.name + " " + prediction.score + "\n");
		}
	}
}
