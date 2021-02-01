package me.franciscofl12.arkanoid;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Esta clase representa las propiedades y acciones de una bola del videojuego SpaceInvaders
 */
public class Bola extends Actor {
	
	// Propiedades privadas de la bola
	int velocidadX = -5;
	int velocidadY = -5;
	public static String IMAGEN_BOLA;
	boolean eastereggbola = false;
	Color[] colores = {Color.WHITE,Color.YELLOW,Color.PINK,Color.CYAN,Color.GREEN,Color.ORANGE};
	int color = 0;
	
	/**
	 * Constructor sin argumentos de entrada
	 */
	public Bola() {
		super();
	}

	/**
	 * Constructor más completo, con todas las propiedades del objeto
	 * @param x
	 * @param y
	 * @param img
	 */
	public Bola(int x, int y, String img) {
		super(x, y, img);
	}
	
	// Acciones de la bola
	
	/**
	 * Metodo que devuelve un String con todos los valores de este objeto.
	 */
	public String toString() {
		return "bola [getX()=" + getX()
				+ ", getY()=" + getY() + ", getImg()=" + getImg() + "]";
	} 
	
	/**
	 * Utilizado para pintar una bola, según sus coordenadas de x e y
	 */
	@Override
	public void paint(Graphics g) {
		g.setColor(colores[color]);
		g.fillOval(this.x, this.y, 13, 13);
		easterEggBola(g);
	}
	

	@Override
	public void actua() {
		// La bola se mueve de manera horizontal, en cada FPS
		this.x += this.velocidadX;
		// Si la bola abandona la escena por la izquierda o la derecha, rebota
		if (this.x < 0 || this.x > Arkanoid.getInstance().getCanvas().getWidth()-13) {
			this.velocidadX = -this.velocidadX;
		}
		
		// Copiamos el esquema anterior para el movimiento vertical
		this.y += this.velocidadY;
		// Si la bola abandona la escena por la izquierda o la derecha, rebota
		if (this.y < 0 || this.y > Arkanoid.getInstance().getCanvas().getHeight()-13) {
			this.velocidadY = -this.velocidadY;
		}
	}
	
	public void easterEggBola(Graphics g) {
			int i = 1;
			i++;
			g.setColor(colores[i]);
			g.fillOval(this.x, this.y, 13, 13);
	}

}
