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
	protected int unidadDeTiempo = 0;
	protected List<BufferedImage> sprites = new ArrayList<BufferedImage>(); // Lista de archivos de imagen utilizado para representarse en pantalla
	protected BufferedImage spriteActual = null; // Sprite que representa actualmente a este actor
	protected int velocidadDeCambioDeSprite = 0;  // Esta propiedad indica cada cuantas "unidades de tiempo" debemos mostrar el siguiente sprite del actor
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
	 * Constructor usado cuando el actor solo tiene un unico sprite
	 * @param spriteName
	 */
	public Actor (String spriteName) {
		this.velocidadDeCambioDeSprite = 1;
		cargarImagenesDesdeSpriteNames(new String[] {spriteName});
	}
	
	/**
	 * Constructor ampliamente utilizado, indicando los nombres de los sprites a utilizar para mostrar este actor
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
	
	public List<BufferedImage> getSprites() {
		return sprites;
	}

	public void setSprites(List<BufferedImage> sprites) {
		this.sprites = sprites;
	}

	public BufferedImage getSpriteActual() {
		return spriteActual;
	}

	public void setSpriteActual(BufferedImage spriteActual) {
		this.spriteActual = spriteActual;
	}

	public int getVelocidadDeCambioDeSprite() {
		return velocidadDeCambioDeSprite;
	}

	public void setVelocidadDeCambioDeSprite(int velocidadDeCambioDeSprite) {
		this.velocidadDeCambioDeSprite = velocidadDeCambioDeSprite;
	}

	public double getVelocidadX() {
		return velocidadX;
	}

	public void setVelocidadX(int velocidadX) {
		this.velocidadX = velocidadX;
	}

	public static int getVelNave() {
		return VEL_NAVE;
	}

	public void paintImagen(Graphics2D g){
		g.drawImage(this.spriteActual, this.x, this.y, null);
	}
	
	/**
	 * Método que permite que cada actor realice las acciones que necesite en la
	 * creación de cada Frame
	 */
	public void actua() {
		// En el caso de que exista un array de sprites el actor actual se tratara de una animacion, para eso llevaremos a cabo los siguientes pasos
		if (this.sprites != null && this.sprites.size() > 1) {
			// cada vez que llaman a "actua()" se incrementara esta unidad, siempre que existan sprites
			unidadDeTiempo++;
			// Si la unidad de tiemplo coincide o es multiplo de la velocidad de cambio de sprite, entramos al if
			if (unidadDeTiempo % velocidadDeCambioDeSprite == 0){
				// Reiniciamos la unidad de tiempo
				unidadDeTiempo = 0;
				// Obtengo el indice del spriteActual, dentro de la lista de indices
				int indiceSpriteActual = sprites.indexOf(this.spriteActual);
				// Obtengo el siguiente indice de sprite, teniendo en cuenta que los sprites cambian de uno a otro en ciclo
				int indiceSiguienteSprite = (indiceSpriteActual + 1) % sprites.size();
				// Se selecciona el nuevo spriteActual
				this.spriteActual = sprites.get(indiceSiguienteSprite);
			}
		}
		
	}

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
	 * Cuando una tecla se libera se desactiva la bandera booleana que se habia activado al pulsarla
	 */
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void actua(int x) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * A partir de un array de String, cargamos en memoria la lista de imagenes que constituyen los sprites del actor
	 * @param spriteNames
	 */
	private void cargarImagenesDesdeSpriteNames(String spriteNames[]) {
		// Obtengo las imagenes de este actor, a partir del patron de diseno Singleton con el que se encuentra
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
