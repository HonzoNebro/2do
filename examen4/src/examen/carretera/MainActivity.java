package examen.carretera;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import examen.graficos3.R;

public class MainActivity extends Activity implements OnTouchListener {

	static Carretera carretera;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		carretera = (Carretera) findViewById(R.id.miVista);
		carretera.setOnTouchListener(this);
	}

	public boolean onTouch(View vista, MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			carretera.rana.Saltar();
		}
		return true;
	}

}
