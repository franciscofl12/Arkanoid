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
		g.fillRect(this.x, this.y, 30, 10);
	}

	@Override
	public void actua() {
	
	}

	public void mover(int x) {
		this.x = x;
		// Controlo los casos en los que el jugador pueda salir del Canvas
		ArkanoidCanvas canvas = Arkanoid.getInstance().getCanvas(); // Referencia al objeto Canvas usado

		// Compruebo si el ratón sale por la derecha
		if (this.x > (canvas.getWidth() - 30)) {
			this.x = canvas.getWidth() - 30;
		}
	}
	
}