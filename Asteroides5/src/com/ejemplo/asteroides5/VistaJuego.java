package com.ejemplo.asteroides5;

import java.util.List;
import java.util.Vector;

import com.estudioskelon.t5_asteroides.R;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.PathShape;
import android.graphics.drawable.shapes.RectShape;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

public class VistaJuego extends View implements SensorEventListener {
	private String tipoControl;

	// 0 = Pantalla
	// 1 = Teclado
	// 2 = Aceler�metro

	// MANEJO CON EL TECLADO
	@Override
	public boolean onKeyDown(int codigoTecla, KeyEvent evento) {
		super.onKeyDown(codigoTecla, evento);
		boolean procesada = true;
		if (tipoControl.equals("1")) {
			switch (codigoTecla) {
			case KeyEvent.KEYCODE_DPAD_UP:
				aceleracionNave = +PASO_ACELERACION_NAVE;
				break;
			case KeyEvent.KEYCODE_DPAD_LEFT:
				giroNave = -PASO_GIRO_NAVE;
				break;
			case KeyEvent.KEYCODE_DPAD_RIGHT:
				giroNave = +PASO_GIRO_NAVE;
				break;
			case KeyEvent.KEYCODE_DPAD_CENTER:
			case KeyEvent.KEYCODE_ENTER:
				ActivaMisil();
				break;
			default:
				// Si estamos aqu�, no hay pulsaci�n que nos interese
				procesada = false;
				break;
			}
		}
		return procesada;
	}

	@Override
	public boolean onKeyUp(int codigoTecla, KeyEvent evento) {
		super.onKeyUp(codigoTecla, evento);
		// Suponemos que vamos a procesar la pulsaci�n
		boolean procesada = true;
		if (tipoControl.equals("1")) {
			switch (codigoTecla) {
			case KeyEvent.KEYCODE_DPAD_UP:
				aceleracionNave = 0;
				break;
			case KeyEvent.KEYCODE_DPAD_LEFT:
			case KeyEvent.KEYCODE_DPAD_RIGHT:
				giroNave = 0;
				break;
			default:
				// Si estamos aqu�, no hay pulsaci�n que nos interese
				procesada = false;
				break;
			}
		}
		return procesada;
	}

	// MANEJO CON LA PANTALLA
	private float mX = 0, mY = 0;
	private boolean disparo = false;

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		super.onTouchEvent(event);
		float x = event.getX();
		float y = event.getY();
		if (tipoControl.equals("0")) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				disparo = true;
				break;
			case MotionEvent.ACTION_MOVE:
				float dx = Math.abs(x - mX);
				float dy = Math.abs(y - mY);
				if (dy < 6 && dx > 6) {
					giroNave = Math.round((x - mX) / 2);
					disparo = false;
				} else if (dx < 6 && dy > 6) {
					aceleracionNave = Math.round((mY - y) / 25);
					disparo = false;
				}
				break;
			case MotionEvent.ACTION_UP:
				giroNave = 0;
				aceleracionNave = 0;
				if (disparo) {
					ActivaMisil();
				}
				break;
			}
			mX = x;
			mY = y;
		}
		return true;
	}

	// MANEJO CON LOS SENSORES
	@Override
	public void onSensorChanged(SensorEvent event) {
		float valor = event.values[1];
		if (tipoControl.equals("2")) {
			if (!hayValorInicial) {
				valorInicial = valor;
				hayValorInicial = true;
			}
			giroNave = (int) (valor - valorInicial) / 3;
		}
	}

	// //// MISIL //////
	private Drawable drawableNave, drawableAsteroide, drawableMisil;
	private Vector<Grafico> Misiles;
	private static int PASO_VELOCIDAD_MISIL = 12;
	// private boolean misilActivo = false;
	// private int tiempoMisil;
	private Vector<Integer> tiempoMisiles;
	// //// NAVE //////
	private Grafico nave;// Gr�fico de la nave
	private int giroNave; // Incremento de direcci�n
	private float aceleracionNave; // aumento de velocidad
	// Incremento est�ndar de giro y aceleraci�n
	private static final int PASO_GIRO_NAVE = 5;
	private static final float PASO_ACELERACION_NAVE = 0.5f;
	// //// ASTEROIDES //////
	private Vector<Grafico> Asteroides; // Vector con los Asteroides
	private int numAsteroides = 5; // N�mero inicial de asteroides
	// //// THREAD Y TIEMPO //////
	// Thread encargado de procesar el juego
	private ThreadJuego thread = new ThreadJuego();
	// Cada cuanto queremos procesar cambios (ms)
	private static int PERIODO_PROCESO = 50;
	// Cuando se realiz� el �ltimo proceso
	private long ultimoProceso = 0;

	public VistaJuego(Context context, AttributeSet attrs) {
		super(context, attrs);
		// RECONOCIMIENTO DE SENSORES
		SensorManager mSensorManager = (SensorManager) context
				.getSystemService(Context.SENSOR_SERVICE);
		List<Sensor> listSensors = mSensorManager
				.getSensorList(Sensor.TYPE_ACCELEROMETER);
		if (!listSensors.isEmpty()) {
			Sensor orientationSensor = listSensors.get(0);
			mSensorManager.registerListener(this, orientationSensor,
					SensorManager.SENSOR_DELAY_GAME);
		}
		// /
		SharedPreferences pref = context.getSharedPreferences(
				"com.estudioskelon.t5_asteroides_preferences",
				Context.MODE_PRIVATE);
		tipoControl = pref.getString("entrada", "0");
		if (pref.getString("graficos", "1").equals("0")) {

			// Nave Vectorial
			Path pathNave = new Path();
			pathNave.moveTo(0, 0);
			pathNave.lineTo(1, (float) 0.5);
			pathNave.lineTo(0, 1);
			pathNave.lineTo(0, 0);
			ShapeDrawable dNave = new ShapeDrawable(new PathShape(pathNave, 1,
					1));
			dNave.getPaint().setColor(Color.WHITE);
			dNave.getPaint().setStyle(Style.STROKE);
			dNave.setIntrinsicWidth(20);
			dNave.setIntrinsicHeight(15);
			drawableNave = dNave;

			// Misil vectorial
			ShapeDrawable dMisil = new ShapeDrawable(new RectShape());
			dMisil.getPaint().setColor(Color.WHITE);
			dMisil.getPaint().setStyle(Style.STROKE);
			dMisil.setIntrinsicWidth(15);
			dMisil.setIntrinsicHeight(3);
			drawableMisil = dMisil;

			// ASTEROIDE VECTORIAL
			Path pathAsteroide = new Path();
			pathAsteroide.moveTo((float) 0.3, (float) 0.0);
			pathAsteroide.lineTo((float) 0.6, (float) 0.0);
			pathAsteroide.lineTo((float) 0.6, (float) 0.3);
			pathAsteroide.lineTo((float) 0.8, (float) 0.2);
			pathAsteroide.lineTo((float) 1.0, (float) 0.4);
			pathAsteroide.lineTo((float) 0.8, (float) 0.6);
			pathAsteroide.lineTo((float) 0.9, (float) 0.9);
			pathAsteroide.lineTo((float) 0.8, (float) 1.0);
			pathAsteroide.lineTo((float) 0.4, (float) 1.0);
			pathAsteroide.lineTo((float) 0.0, (float) 0.6);
			pathAsteroide.lineTo((float) 0.0, (float) 0.2);
			pathAsteroide.lineTo((float) 0.3, (float) 0.0);
			ShapeDrawable dAsteroide = new ShapeDrawable(new PathShape(
					pathAsteroide, 1, 1));
			dAsteroide.getPaint().setColor(Color.WHITE);
			dAsteroide.getPaint().setStyle(Style.STROKE);
			dAsteroide.setIntrinsicWidth(50);
			dAsteroide.setIntrinsicHeight(50);
			drawableAsteroide = dAsteroide;
			setBackgroundColor(Color.BLACK);
		} else {
			drawableAsteroide = context.getResources().getDrawable(
					R.drawable.asteroide1);
			drawableNave = context.getResources().getDrawable(R.drawable.nave);
			drawableMisil = context.getResources().getDrawable(
					R.drawable.misil1);
		}
		nave = new Grafico(this, drawableNave);
		new Grafico(this, drawableMisil);
		Misiles = new Vector<Grafico>();
		tiempoMisiles = new Vector<Integer>();
		Asteroides = new Vector<Grafico>();
		for (int i = 0; i < numAsteroides; i++) {
			Grafico asteroide = new Grafico(this, drawableAsteroide);
			asteroide.setIncY(Math.random() * 4 - 2);
			asteroide.setIncX(Math.random() * 4 - 2);
			asteroide.setAngulo((int) (Math.random() * 360));
			asteroide.setRotacion((int) (Math.random() * 8 - 4));
			Asteroides.add(asteroide);
		}
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
	}

	private boolean hayValorInicial = false;
	private float valorInicial;

	@Override
	protected void onSizeChanged(int ancho, int alto, int ancho_anter,
			int alto_anter) {
		super.onSizeChanged(ancho, alto, ancho_anter, alto_anter);
		for (Grafico asteroide : Asteroides) {
			do {
				asteroide.setPosX(Math.random()
						* (ancho - asteroide.getAncho()));
				asteroide.setPosY(Math.random() * (alto - asteroide.getAlto()));
			} while (asteroide.distancia(nave) < (ancho + alto) / 5);
		}
		nave.setPosX(ancho / 2);
		nave.setPosY(alto / 2);
		ultimoProceso = System.currentTimeMillis();
		thread.start();

	}

	class ThreadJuego extends Thread {
		@Override
		public void run() {
			while (true) {
				actualizaFisica();
			}
		}
	}

	protected synchronized void actualizaFisica() {
		long ahora = System.currentTimeMillis();
		if (ultimoProceso + PERIODO_PROCESO > ahora) {
			return;
		}
		double retardo = (ahora - ultimoProceso) / PERIODO_PROCESO;
		ultimoProceso = ahora;
		nave.setAngulo((int) (nave.getAngulo() + giroNave * retardo));
		double nIncX = nave.getIncX() + aceleracionNave
				* Math.cos(Math.toRadians(nave.getAngulo())) * retardo;
		double nIncY = nave.getIncY() + aceleracionNave
				* Math.sin(Math.toRadians(nave.getAngulo())) * retardo;
		double MAX_VELOCIDAD_NAVE = 10;
		// Actualizamos si el m�dulo de la velocidad no excede el m�ximo
		if (Math.hypot(nIncX, nIncY) <= MAX_VELOCIDAD_NAVE) {
			nave.setIncX(nIncX);
			nave.setIncY(nIncY);
		}
		nave.incrementaPos(retardo);
		for (Grafico asteroide : Asteroides) {
			asteroide.incrementaPos(retardo);
		}
		for (int i = 0; i < Misiles.size(); i++) {
			if (tiempoMisiles.get(i) > 0) {
				Misiles.get(i).incrementaPos(retardo);
				tiempoMisiles.setElementAt(
						(int) (tiempoMisiles.get(i) - retardo), i);
				if (tiempoMisiles.get(i) < 0) {
					Misiles.remove(i);
					tiempoMisiles.remove(i);
				} else {
					for (int j = 0; j < Asteroides.size(); j++)
						if (Misiles.get(i).verificaColision(
								Asteroides.elementAt(j))) {
							destruyeAsteroide(j);
							Misiles.remove(i);
							tiempoMisiles.remove(i);
							break;
						}
				}
			} else {
				Misiles.remove(i);
				tiempoMisiles.remove(i);
			}

		}
	}

	private void destruyeAsteroide(int i) {
		Asteroides.remove(i);
	}

	private void ActivaMisil() {
		Misiles.add(new Grafico(this, drawableMisil));
		Misiles.lastElement().setPosX(nave.getPosX());
		Misiles.lastElement().setPosY(nave.getPosY() + 6);
		Misiles.lastElement().setAngulo(nave.getAngulo());
		Misiles.lastElement().setIncX(
				Math.cos(Math.toRadians(Misiles.lastElement().getAngulo()))
						* PASO_VELOCIDAD_MISIL);
		Misiles.lastElement().setIncY(
				Math.sin(Math.toRadians(Misiles.lastElement().getAngulo()))
						* PASO_VELOCIDAD_MISIL);
		tiempoMisiles
				.add((int) Math.min(
						this.getWidth()
								/ Math.abs(Misiles.lastElement().getIncX()),
						this.getHeight()
								/ Math.abs(Misiles.lastElement().getIncY())) - 2);
	}

	@Override
	protected synchronized void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		for (Grafico asteroide : Asteroides) {
			asteroide.dibujaGrafico(canvas);
		}
		// Dibujar la nave en el Canvas
		nave.dibujaGrafico(canvas);
		for (int i = 0; i < Misiles.size(); i++) {
			Misiles.get(i).dibujaGrafico(canvas);
		}
	}
}