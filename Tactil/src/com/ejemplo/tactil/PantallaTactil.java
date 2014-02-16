package com.ejemplo.tactil;

import com.estudioskelon.t5_pantallatactil.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.TextView;

public class PantallaTactil extends Activity implements OnTouchListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pantalla_tactil);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.pantalla_tactil, menu);
		TextView texto = (TextView) findViewById(R.id.TextViewEntrada);
		texto.setOnTouchListener(this);
		return true;

	}

	@Override
	public boolean onTouch(View vista, MotionEvent evento) {
		TextView salida = (TextView) findViewById(R.id.TextViewSalida);
		String acciones[] = { "ACTION_DOWN", "ACTION_UP", "ACTION_MOVE",
				"ACTION_CANCEL", "ACTION_OUTSIDE", "ACTION_POINTER_DOWN",
				"ACTION_POINTER_UP" };
		int accion = evento.getAction();
		int codigoAccion = accion & MotionEvent.ACTION_MASK;
		salida.append(acciones[codigoAccion]);
		for (int i = 0; i < evento.getPointerCount(); i++) {
			salida.append(" PUNTERO: " + evento.getPointerId(i) + "\n x:"
					+ evento.getX(i) + " y:" + evento.getY(i));
		}
		salida.append("\n");
		return true;
	}

}
