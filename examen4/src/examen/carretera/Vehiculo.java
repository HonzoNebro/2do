package examen.carretera;

import java.util.Random;

import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.view.View;

public class Vehiculo {

	Canvas lienzo;
	View vista;
	Drawable piel;
	private int ancho;
	private int alto;
	private int cenX = 0;
	private int cenY = 0;
	private int velocidad = 1;
	private boolean sentido;
	Random r = new Random();
	private int posXinicial = 0;
	private int arriba;
	private int derecha;

	public Vehiculo(View vista, int posX, int posY, int foto, boolean sentido) {
		this.vista = vista;
		piel = vista.getResources().getDrawable(foto);
		setAncho(piel.getIntrinsicWidth() / 2);
		setAlto(piel.getIntrinsicHeight() / 2);
		setVelocidad(r.nextInt(10) + 3);
		if (sentido) {
			setCenX(0 - getAncho());
		} else {
			setCenX(posX + getAncho());
		}
		setPosXinicial(posX);
		setCenY(posY);
		this.setSentido(sentido);
	}

	public void dibuja(Canvas canvas) {
		lienzo = canvas;
		setArriba(getCenY() - getAlto());
		setDerecha(getCenX() + getAncho());
		piel.setBounds(getCenX(), getArriba(), getDerecha(), getCenY()); //izquierda, arriba, derecha, abajo;
		canvas.save();
		piel.draw(canvas);
		canvas.restore();
		vista.invalidate();
	}

	public void actualizar(int maxY, int maxX) {
		if (isSentido()) {
			if (getCenX() >= maxX) {
				//setRandSentido();
				setCenX(0 - getAncho());
				setVelocidad();
			}
			derecha();
		} else {
			if (getCenX() + getAncho() <= 0) {
				//setRandSentido();
				setCenX(getPosXinicial());
				setVelocidad();
			}
			izquierda();
		}
	}
	
	public void setVelocidad(){
		setVelocidad(r.nextInt(10) + 3);
	}
	
	public void setRandSentido(){
		setSentido(r.nextBoolean());
	}
	
	public void asignarCarril(){
		int[] carriles={0, lienzo.getHeight()-60, 320, 360, 240, 200, 70, 50};
		//sale por true == derecha, false == izquierda;
		if(isSentido()){
			int rand = r.nextInt(4)+1;
			switch (rand){
			case 1:
				setCenY(carriles[1]);
				break;
			case 2:
				setCenY(carriles[3]);
				break;
			case 3:
				setCenY(carriles[5]);
				break;
			case 4:
				setCenY(carriles[7]);
				break;
			}
		} else{
			
		}
	}
	
	public void izquierda() {
		setCenX(getCenX() - getVelocidad());
	}

	public void derecha() {
		setCenX(getCenX() + getVelocidad());
	}

	int getAncho() {
		return ancho;
	}

	void setAncho(int ancho) {
		this.ancho = ancho;
	}

	int getCenX() {
		return cenX;
	}

	void setCenX(int cenX) {
		this.cenX = cenX;
	}

	int getCenY() {
		return cenY;
	}

	void setCenY(int cenY) {
		this.cenY = cenY;
	}

	int getVelocidad() {
		return velocidad;
	}

	void setVelocidad(int velocidad) {
		this.velocidad = velocidad;
	}

	int getAlto() {
		return alto;
	}

	void setAlto(int alto) {
		this.alto = alto;
	}

	int getPosXinicial() {
		return posXinicial;
	}

	void setPosXinicial(int posXinicial) {
		this.posXinicial = posXinicial;
	}

	boolean isSentido() {
		return sentido; // true == derecha, false == izquierda;
	}

	private void setSentido(boolean sentido) {
		this.sentido = sentido;
	}

	int getDerecha() {
		return derecha;
	}

	void setDerecha(int derecha) {
		this.derecha = derecha;
	}

	int getArriba() {
		return arriba;
	}

	void setArriba(int arriba) {
		this.arriba = arriba;
	}
}
