package examen.carretera;

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.Toast;

public class Rana {

	private View vista;
	private Drawable piel;
	private int ancho = 0;
	private int alto = 0;
	private int posX = 0;
	private int posY = 0;
	private int posYinicial;
	private int destino;
	private int velocidad = 30;

	private int arriba;
	private int derecha;

	public Rana(View vista, int posX, int posY, int foto) {
		this.vista = vista;
		setPiel(this.vista.getResources().getDrawable(foto));
		setAncho(piel.getIntrinsicWidth() / 6);
		setAlto(piel.getIntrinsicHeight() / 6);
		setPosX(posX / 2 - getAncho() / 2);
		setPosY(posY);
		setPosYinicial(getPosY());
		setDestino(getPosY());
	}

	public void dibujar(Canvas lienzo) {
		setArriba(getPosY() - getAlto());
		setDerecha(getPosX() + getAncho());
		piel.setBounds(getPosX(), getArriba(), getDerecha(), getPosY());
		// izquierda, arriba, derecha, abajo;
		lienzo.save();
		piel.draw(lienzo);
		lienzo.restore();
		vista.invalidate();
	}

	public void Comprobar() {
		if (getPosY() > getDestino()) {
			setPosY(getPosY() - getVelocidad());
			// hace saltar a la rana la cantidad de Velocidad, llamada en cada "onDraw"
		}
		if (getArriba() <= 0) {
			Toast.makeText(vista.getContext(), "Llegué", Toast.LENGTH_SHORT)
					.show();
		}
	}

	public void Saltar() {
		setDestino(getDestino() - getVelocidad());
		// establece un destino mas lejano
	}

	public void Reiniciar() {
		setPosY(getPosYinicial());
		setDestino(getPosYinicial());
		Toast.makeText(vista.getContext(), "¡Comienza una nueva vida!",
				Toast.LENGTH_SHORT).show();
	}

	int getAncho() {
		return ancho;
	}

	void setAncho(int ancho) {
		this.ancho = ancho;
	}

	int getAlto() {
		return alto;
	}

	void setAlto(int alto) {
		this.alto = alto;
	}

	int getPosX() {
		return posX;
	}

	void setPosX(int posX) {
		this.posX = posX;
	}

	int getPosY() {
		return posY;
	}

	void setPosY(int posY) {
		this.posY = posY;
	}

	int getPosYinicial() {
		return posYinicial;
	}

	void setPosYinicial(int posYinicial) {
		this.posYinicial = posYinicial;
	}

	int getVelocidad() {
		return velocidad;
	}

	void setVelocidad(int velocidad) {
		this.velocidad = velocidad;
	}

	Drawable getPiel() {
		return piel;
	}

	void setPiel(Drawable piel) {
		this.piel = piel;
	}

	int getArriba() {
		return arriba;
	}

	void setArriba(int arriba) {
		this.arriba = arriba;
	}

	int getDerecha() {
		return derecha;
	}

	void setDerecha(int derecha) {
		this.derecha = derecha;
	}

	int getDestino() {
		return destino;
	}

	void setDestino(int destino) {
		this.destino = destino;
	}

}
