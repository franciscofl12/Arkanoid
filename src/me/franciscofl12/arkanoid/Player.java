package me.franciscofl12.arkanoid;

import java.awt.Graphics;
import java.awt.event.KeyEvent;

/**
 * Clase que representa un player en el juego
 *
 */
public class Player extends Actor {

	public static String IMAGEN_PLAYER;
	@SuppressWarnings("unused")
	private boolean left,right; // Booleanas que determinan si la nave se esta moviendo actualmente

	/**
	 * Constructor por defecto "default constructor"
	 */
	public Player() {
		super("nave.png");
		//super();
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
//		g.setColor(Color.WHITE);
//		g.fillRect(this.x, this.y, 30, 10);
	}

	@Override
	public void actua() {
		// Controlo los casos en los que el jugador pueda salir del Canvas
		ArkanoidCanvas canvas = Arkanoid.getInstance().getCanvas(); // Referencia al objeto Canvas usado

		// Movimiento en el eje horizontal
		this.x += this.velocidadX; // En cada iteración del bucle principal, el monstruo cambiará su posición en el eje X, sumándole a esta el valor de vx
		// si la nave intenta salir por la derecha no se lo permitimos
		if (this.x <  0) {
			this.x = 0;
		}
		// si la nave intenta salir por la izquierda no se lo permitimos
		if (this.x >  (canvas.getWidth() - this.ancho)) {
			this.x = (canvas.getWidth() - this.ancho);
		}
		
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
	
	/****
	 * El codigo de la tecla de direccion derecha es 39, con esto sabremos y podremos
	 * hacer un simple formato en el cual cuando sea pulsado, la coordenada x se mueva
	 * para la derecha, y a la izquierda de igual manera.
	 */
	
	public void keyPressed(KeyEvent e) {
	  	switch (e.getKeyCode()) {
			case KeyEvent.VK_LEFT : left = true; break;
			case KeyEvent.VK_RIGHT : right = true; break;
	  	}
	  	updateSpeed();
	}

	/**
	 * Cuando una tecla se libera se desactiva la bandera booleana que se habia activado al pulsarla
	 */
	@Override
	public void keyReleased(KeyEvent e) {
		switch (e.getKeyCode()) {
  			case KeyEvent.VK_LEFT : left = false; break; 
  			case KeyEvent.VK_RIGHT : right = false;break;
		}
		updateSpeed();
	}
	
	//Utilizaremos este metodo para hacer el movimiento de la nave	
	protected void updateSpeed() {
		this.velocidadX = 0;
		if (left && (this.x > 0)) this.velocidadX = -VEL_NAVE;
		if (right) this.velocidadX = VEL_NAVE;
	}


}