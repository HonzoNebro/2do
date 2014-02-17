package com.ejemplo.asteroides5;

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.view.View;

public class Grafico {

	private Drawable drawable;

	private double posX, posY;

	private double incX, incY;

	private int angulo, rotacion;

	private int ancho, alto;

	private int radioColision;


	private View view;


	public static final int MAX_VELOCIDAD = 20;

	public Grafico(View view, Drawable drawable) {

		this.view = view;

		this.setDrawable(drawable);

		setAncho(drawable.getIntrinsicWidth());

		setAlto(drawable.getIntrinsicHeight());

		setRadioColision((getAlto() + getAncho()) / 4);

	}

	public void dibujaGrafico(Canvas canvas) {

		canvas.save();

		int x = (int) (getPosX() + getAncho() / 2);

		int y = (int) (getPosY() + getAlto() / 2);

		canvas.rotate((float) getAngulo(), (float) x, (float) y);

		getDrawable().setBounds((int) getPosX(), (int) getPosY(), (int) getPosX() + getAncho(),
				(int) getPosY() + getAlto());

		getDrawable().draw(canvas);

		canvas.restore();

		int rInval = (int) Math.hypot(getAncho(), getAlto()) / 2 + MAX_VELOCIDAD;

		view.invalidate(x - rInval, y - rInval, x + rInval, y + rInval);

	}

	public void incrementaPos(double factor) {

		setPosX(getPosX() + getIncX() * factor);

		// Si salimos de la pantalla, corregimos posición

		if (getPosX() < -getAncho() / 2) {
			setPosX(view.getWidth() - getAncho() / 2);
		}

		if (getPosX() > view.getWidth() - getAncho() / 2) {
			setPosX(-getAncho() / 2);
		}

		setPosY(getPosY() + getIncY() * factor);

		if (getPosY() < -getAlto() / 2) {
			setPosY(view.getHeight() - getAlto() / 2);
		}

		if (getPosY() > view.getHeight() - getAlto() / 2) {
			setPosY(-getAlto() / 2);
		}

		setAngulo((int) (getAngulo() + getRotacion() * factor));

	}

	public double distancia(Grafico g) {

		return Math.hypot(getPosX() - g.getPosX(), getPosY() - g.getPosY());

	}

	public boolean verificaColision(Grafico g) {

		return (distancia(g) < (getRadioColision() + g.getRadioColision()));

	}

	public Drawable getDrawable() {
		return drawable;
	}

	public void setDrawable(Drawable drawable) {
		this.drawable = drawable;
	}

	public double getPosX() {
		return posX;
	}

	public void setPosX(double posX) {
		this.posX = posX;
	}

	public double getPosY() {
		return posY;
	}

	public void setPosY(double posY) {
		this.posY = posY;
	}

	public double getIncX() {
		return incX;
	}

	public void setIncX(double incX) {
		this.incX = incX;
	}

	public double getIncY() {
		return incY;
	}

	public void setIncY(double incY) {
		this.incY = incY;
	}

	public int getAngulo() {
		return angulo;
	}

	public void setAngulo(int angulo) {
		this.angulo = angulo;
	}

	public int getRotacion() {
		return rotacion;
	}

	public void setRotacion(int rotacion) {
		this.rotacion = rotacion;
	}

	public int getAncho() {
		return ancho;
	}

	public void setAncho(int ancho) {
		this.ancho = ancho;
	}

	public int getAlto() {
		return alto;
	}

	public void setAlto(int alto) {
		this.alto = alto;
	}

	public int getRadioColision() {
		return radioColision;
	}

	public void setRadioColision(int radioColision) {
		this.radioColision = radioColision;
	}

	public View getView() {
		return view;
	}

	public void setView(View view) {
		this.view = view;
	}

	public static int getMaxVelocidad() {
		return MAX_VELOCIDAD;
	}
	
	

}