package me.franciscofl12.arkanoid;

import java.awt.Color;
import java.awt.Graphics;

/**
 * Esta clase representa las propiedades y acciones de una bola del videojuego SpaceInvaders
 */
public class Bola extends Actor {
	
	// Propiedades privadas de la bola
	int velocidadX = -5;
	int velocidadY = -5;
	public static String IMAGEN_BOLA;
	
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
		g.setColor(Color.WHITE);
		g.fillOval(this.x, this.y, 13, 13);
	}

	@Override
	public void actua() {
		// El monstruo se mueve de manera horizontal, en cada FPS
		this.x += this.velocidadX;
		// Si el monstruo abandona la escena por la izquierda o la derecha, rebota
		if (this.x < 0 || this.x > 335) {
			this.velocidadX = -this.velocidadX;
		}
		
		// Copiamos el esquema anterior para el movimiento vertical
		this.y += this.velocidadY;
		// Si el monstruo abandona la escena por la izquierda o la derecha, rebota
		if (this.y < 0 || this.y > 555) {
			this.velocidadY = -this.velocidadY;
		}
	}

}
