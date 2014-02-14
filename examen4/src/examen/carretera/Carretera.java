package examen.carretera;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Toast;

import examen.graficos3.R;

public class Carretera extends View {

	ArrayList<Vehiculo> vehiculosAL = new ArrayList<Vehiculo>();
	Vehiculo coche, coche2, coche3, coche4, coche5, coche6;
	int marcado;
	Canvas lienzo;
	Rana rana;
	Boolean ranaviva = false;
	Toast t = Toast.makeText(getContext(), "", Toast.LENGTH_SHORT);
	

	public Carretera(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onSizeChanged(int ancho, int alto, int ancho_anterior,
			int alto_anterior) {

	}

	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		lienzo = canvas;
		if(vehiculosAL.isEmpty()){
			agregarVehiculos(lienzo.getWidth(), lienzo.getHeight());
		}
		actualizarVehiculos();
		dibujarVehiculos();
		if (ranaviva == false) {
			agregarRana(lienzo.getWidth(), lienzo.getHeight());
		}
		rana.Comprobar();
		rana.dibujar(lienzo);
		comprobarColision();
	}

	public void agregarVehiculos(int der, int abajo) {
		if (vehiculosAL.size() < 6) {
			vehiculosAL.add(new Vehiculo(this, 0, abajo-60,
					R.drawable.vehiculo1d, true)); // camion rojo

			vehiculosAL.add(new Vehiculo(this, der+1750, 70, R.drawable.vehiculo2i,
					false)); // coche gris con remolque

			vehiculosAL.add(new Vehiculo(this, 0, 240,
					R.drawable.vehiculo3d, true)); // tractor azul

			vehiculosAL.add(new Vehiculo(this, der+1000, 320,
					R.drawable.vehiculo5i, false)); // coche azul

			vehiculosAL.add(new Vehiculo(this, der, 320,
					R.drawable.vehiculo6i, false)); // camion blanco
		}
	}

	public void agregarRana(int posX, int posY) {
		rana = new Rana(this, posX, posY, R.drawable.rana);
		ranaviva = true;
	}

	public void actualizarVehiculos() {
		for (Vehiculo vehiculoAL : vehiculosAL) {
			vehiculoAL.actualizar(lienzo.getHeight(), lienzo.getWidth());
		}
	}

	public void dibujarVehiculos() {
		for (Vehiculo vehiculoAL : vehiculosAL) {
			vehiculoAL.dibuja(lienzo);
		}
	}

	public void comprobarColision() {
		for (Vehiculo vehiculoAL : vehiculosAL) {
			//izquierda, arriba, derecha, abajo;
			Rect r = new Rect(rana.getPosX(), rana.getArriba(), rana.getDerecha(), rana.getPosY());
			if(vehiculoAL.piel.getBounds().contains(r)){
				t.setText("¡squash!");
				t.show();
				rana.Reiniciar();
			}
		}
	}
}