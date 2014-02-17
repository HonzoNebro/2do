package com.ejemplo.hilos;

import com.estudioskelon.hilos.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {
	class Hilo extends Thread {

		private int n, res;

		public Hilo(int n) {
			this.n = n;
		}

		@Override
		public void run() {
			res = factorial(n);
			salida.append(res + "\n");
		}
	}

	class Trabajo extends AsyncTask<Integer, Integer, Integer> {

		private ProgressDialog progreso;

		@Override
		protected void onPreExecute() {
			progreso = new ProgressDialog(MainActivity.this);
			progreso.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			progreso.setMessage("Calculando...");

			progreso.setMax(100);
			progreso.setProgress(0);
			progreso.show();
			progreso.setCancelable(true);
			progreso.setOnCancelListener(new OnCancelListener() {

				@Override
				public void onCancel(DialogInterface dialog) {
					Trabajo.this.cancel(true);
				}
			});
		}

		@Override
		protected Integer doInBackground(Integer... n) {
			int res = 1;
			for (int i = 1; i <= n[0] && !isCancelled(); i++) {
				res *= i;
				SystemClock.sleep(1000);
				publishProgress(i * 100 / n[0]);
			}
			return res;
		}

		@Override
		protected void onProgressUpdate(Integer... porc) {
			progreso.setProgress(porc[0]);
		}

		@Override
		protected void onPostExecute(Integer res) {
			progreso.dismiss();
			salida.append(res + "\n");
		}

		@Override
		protected void onCancelled() {
			salida.append("cancelado\n");

		}
	}

	private EditText entrada;
	private TextView salida;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		entrada = (EditText) findViewById(R.id.entrada);
		salida = (TextView) findViewById(R.id.salida);
	}

	public void calcularOperacion(View view) {
		int n = Integer.parseInt(entrada.getText().toString());
		salida.append(n + "! = ");
		Trabajo tarea = new Trabajo();
		tarea.execute(n);
	}

	public int factorial(int n) {
		int res = 1;
		for (int i = 1; i <= n; i++) {
			res *= i;
			SystemClock.sleep(1000);
		}

		return res;
	}
}