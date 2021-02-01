package me.franciscofl12.arkanoid;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public abstract class Actor {

	// Propiedades protegidas (visibles en la propia clase y en los subtipos) de
	// cada actor
	protected int x , y; // Coordenadas x e y del actor
	protected int ancho = 25, alto = 20; // ancho y alto que ocupa el actor en pantalla
	protected String img; // Imagen del actor
	protected static final int VEL_NAVE = 5;
	protected List<BufferedImage> sprites = new ArrayList<BufferedImage>(); // Lista de archivos de imagen utilizado para representarse en pantalla
	protected BufferedImage spriteActual = null; // Sprite que representa actualmente a este actor
	protected int velocidadDeCambioDeSprite = 0;  // Esta propiedad indica cada cu�ntas "unidades de tiempo" debemos mostrar el siguiente sprite del actor
	protected int velocidadX = 0; // Velocidades en cada eje
	protected boolean markedForRemoval; // Bandera para saber si un objeto tiene que ser eliminado
	

	/**
	 * Constructor sin parámetros de entrada
	 */
	public Actor() {
	}

	/**
	 * Constructor con parámetros de entrada
	 * 
	 * @param x
	 * @param y
	 * @param img
	 */
	public Actor(int x, int y, String img) {
		this.x = x;
		this.y = y;
		this.img = img;
	}
	
	/**
	 * Constructor usado cuando el actor s�lo tiene un �nico sprite
	 * @param spriteName
	 */
	public Actor (String spriteName) {
		this.velocidadDeCambioDeSprite = 1;
		cargarImagenesDesdeSpriteNames(new String[] {spriteName});
	}
	
	/**
	 * Constructor ampl�amente utilizado, indicando los nombres de los sprites a utilizar para mostrar este actor
	 * @param spriteName
	 */
	public Actor (String spriteNames[]) {
		this.velocidadDeCambioDeSprite = 1;
		cargarImagenesDesdeSpriteNames(spriteNames);
	}
	
	/**
	 * 
	 * @param spriteNames
	 * @param velocidadDeCambioDeSprite
	 */
	public Actor (String spriteNames[], int velocidadDeCambioDeSprite) {
		this.velocidadDeCambioDeSprite = velocidadDeCambioDeSprite;
		cargarImagenesDesdeSpriteNames(spriteNames);
	}

	/**
	 * Este método abstracto indica que todos los subtipos están obligados a
	 * implementarlo. Lo usaremos para pintar cada personaje.
	 * 
	 * @param g
	 */
	public abstract void paint(Graphics g);
	
//	public void paint(Graphics2D g){
//		g.drawImage( this.spriteActual, this.x, this.y, null);
//	}
	
	/**
	 * Método que permite que cada actor realice las acciones que necesite en la
	 * creación de cada Frame
	 */
	public abstract void actua();

	/**
	 * @return the x
	 */
	public int getX() {
		return x;
	}

	// Getters y setters

	/**
	 * @param x the x to set
	 */
	public void setX(int x) {
		this.x = x;
	}

	/**
	 * @return the y
	 */
	public int getY() {
		return y;
	}

	/**
	 * @param y the y to set
	 */
	public void setY(int y) {
		this.y = y;
	}

	/**
	 * @return the img
	 */
	public String getImg() {
		return img;
	}

	/**
	 * @param img the img to set
	 */
	public void setImg(String img) {
		this.img = img;
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

	/**
	 * Cuando una tecla se libera se desactiva la bandera booleana que se hab�a activado al pulsarla
	 */
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void actua(int x) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * A partir de un array de String, cargamos en memoria la lista de im�genes que constituyen los sprites del actor
	 * @param spriteNames
	 */
	private void cargarImagenesDesdeSpriteNames(String spriteNames[]) {
		// Obtengo las imagenes de este actor, a partir del patron de dise�o Singleton con el que se encuentra
		// el ArkanoidSprite
		for (String sprite : spriteNames) {
			this.sprites.add(ArkanoidSprite.getInstance().getSprite(sprite));
		}
		// ajusto el primer sprite del actor
		if (this.sprites.size() > 0) {
			this.spriteActual = this.sprites.get(0);
		}
		adjustHeightAndWidth ();
	}
	
	
	/**
	 * Actualiza los valores de width y height del Actor, a partir una BufferedImage que lo representara
	 * en pantalla
	 */
	private void adjustHeightAndWidth () {
		// Una vez que tengo las imagenes que representa a este actor, obtengo el ancho y el alto maximos de las mismas y se
		// los traspaso a objeto actual.
		if (this.sprites.size() > 0) {
			this.alto = this.sprites.get(0).getHeight();
			this.ancho = this.sprites.get(0).getWidth();
		}
		for (BufferedImage sprite : this.sprites) {
			// Ajusto el maximo width como el width del actor
			if (sprite.getWidth() > this.ancho) {
				this.ancho = sprite.getWidth();
			}
			// Lo mismo que el anterior, pero con el maximo height
			if (sprite.getHeight() > this.alto) {
				this.alto = sprite.getHeight();
			}
		}
	}
	
	/****
	 * Marcar que un objeto tiene que ser eliminado (markedForRemoval = true)
	 */
	
//	public void remove() {
//		markedForRemoval = true;
//	}
	
	/****
	 * 
	 * @return markedForRemoval
	 */
	public boolean isMarkedForRemoval() {
		return markedForRemoval;
	}
	
	/****
	 * 
	 * @param actorCollisioned para saber que actor colisiono
	 */

	public void collisionWith(Actor actorCollisioned) {}

	public void setMarkedForRemoval(boolean markedForRemoval) { this.markedForRemoval = markedForRemoval; }
	
}
