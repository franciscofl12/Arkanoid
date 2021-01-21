package me.franciscofl12.arkanoid;

import java.awt.Color;
import java.awt.Graphics;

/**
 * Clase que representa un player en el juego
 *
 */
public class Player extends Actor {

	public static String IMAGEN_PLAYER;

	/**
	 * Constructor por defecto "default constructor"
	 */
	public Player() {
		super();
	}

	/**
	 * Constructor que inicializa las propiedades del objeto
	 * @param x
	 * @param y
	 * @param img
	 */
	public Player(int x, int y, String img) {
		super(x, y, img);
	}

	/**
	 * Obtención de un String con todos los datos de un objeto Player
	 */
	public String toString() {
		return "Player [getX()=" + getX() + ", getY()=" + getY() + ", getImg()=" + getImg() + "]";
	}

	/**
	 * Utilizado para pintar un player, según sus coordenadas de x e y
	 */
	@Override
	public void paint(Graphics g) {
		//Aqui tendremos al rectangulo que despues sera reemplazado por una barra
		g.setColor(Color.WHITE);
		g.fillRect(this.x, this.y, this.ancho, this.alto);
	}

	@Override
	public void actua() {
	
	}


	// Getters y Setters
	
}
