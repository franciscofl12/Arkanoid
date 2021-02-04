package me.franciscofl12.arkanoid;

import java.awt.Color;

import java.awt.Graphics;

/**
 * Esta clase representa las propiedades y acciones de un ladrillo del videojuego SpaceInvaders
 */
public class Ladrillo extends Actor {
	
	public static String IMAGEN_LADRILLO = "ladrillorojo.png";
	
	// Propiedades privadas de cada ladrillo
	private String nombre; // Nombre que recibe el ladrillo
	String[] colores = {"ladrillorojo.png","ladrilloamarillo.png","ladrillorosa.png","ladrilloazul.png","ladrilloverde.png","ladrillonaranja.png"};
	private int color;
	private int resistencia;
	private Ladrillo lastLadrilloEliminated; // Ultimo ladrillo eliminado
	
	/**
	 * Constructor sin argumentos de entrada
	 * @param nivel 
	 */
	public Ladrillo(int color, int nivel) {
		super(IMAGEN_LADRILLO);	
		IMAGEN_LADRILLO = colores[color];
		this.resistencia = nivel;
	}

	/**
	 * Constructor más completo, con todas las propiedades del objeto
	 * @param x
	 * @param y
	 * @param img
	 * @param nombre
	 */
	public Ladrillo(int x, int y, String img, String nombre, int color,int nivel) {
		super(x, y, img);
		this.nombre = nombre;
		this.color = color;
		this.resistencia = nivel;
	}
	
	// Acciones de cada ladrillo
	
	/**
	 * Metodo que devuelve un String con todos los valores de este objeto.
	 */
	public String toString() {
		return "Ladrillo [nombre=" + nombre + ", getX()=" + getX()
				+ ", getY()=" + getY() + ", getImg()=" + getImg() + "]";
	}

	// Getters y Setters 
	
	/**
	 * @return the nombre
	 */
	public String getNombre() {
		return nombre;
	}

	/**
	 * @param nombre the nombre to set
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	/**
	 * Utilizado para pintar un ladrillo, según sus coordenadas de x e y
	 */
	@Override
	public void paint(Graphics g) {
		//Creo un array de colores para que cada fila tenga un distinto color
//		g.setColor(colores[color]);
//		g.fillRoundRect(this.x, this.y, this.ancho, this.alto, 3, 3);
	}

	@Override
	public void actua() {
	}
	
	/**
	 * Metodo que determina el comportamiento cuando este actor colisione con otro
	 */
	@Override
	public void collisionWith(Actor actorCollisioned) {
	super.collisionWith(actorCollisioned);
	this.resistencia--;
	Arkanoid.getInstance().bola.setY(Arkanoid.getInstance().bola.getY()+5);
		//Compruebo si el ladrillo colisiona con la bola
		if (actorCollisioned instanceof Bola) {
			if (this.resistencia <= 0) {
				// Si este actor colisiona, debo eliminar el ladrilo
				this.setMarkedForRemoval(true);
				//Crearemos una explosion , que ejecutara una secuencia de sprites
				createExplosion();
				// Lanzo el sonido que corresponde a una explosi�n
			   	ArkanoidSound.getInstance().playSound(ArkanoidSound.getInstance().LADRILLOELIMINADO);
			}
		}
	}
	
	private void createExplosion () {
		Explosion explosion = new Explosion();
		explosion.setX(this.x); 
		explosion.setY(this.y); 
        Arkanoid.getInstance().addNewActorToNextIteration(explosion);
	}

	public Ladrillo getLastLadrilloEliminated() {
		return lastLadrilloEliminated;
	}

	public void setLastLadrilloEliminated(Ladrillo lastLadrilloEliminated) {
		this.lastLadrilloEliminated = lastLadrilloEliminated;
	}
	
	
}
