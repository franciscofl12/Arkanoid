package me.franciscofl12.arkanoid;

import java.awt.Color;
import java.awt.Graphics;

/**
 * Esta clase representa las propiedades y acciones de un ladrillo del videojuego SpaceInvaders
 */
public class Ladrillo extends Actor {
	public static String IMAGEN_LADRILLO_0;
	
	// Propiedades privadas de cada ladrillo
	private String nombre; // Nombre que recibe el ladrillo

	private int color;
	
	/**
	 * Constructor sin argumentos de entrada
	 */
	public Ladrillo() {
		super();
	}

	/**
	 * Constructor más completo, con todas las propiedades del objeto
	 * @param x
	 * @param y
	 * @param img
	 * @param nombre
	 */
	public Ladrillo(int x, int y, String img, String nombre, int color) {
		super(x, y, img);
		this.nombre = nombre;
		this.color = color;
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
		if (color == 0) {
			g.setColor(Color.RED);
		}
		else {
			if (color == 1) {
				g.setColor(Color.YELLOW);
			}
			else {
				if (color == 2) {
					g.setColor(Color.PINK);
				}
				else {
					if (color == 3) {
						g.setColor(Color.CYAN);
					}
					else {
						if (color == 4) {
							g.setColor(Color.GREEN);
						}
						else {
							if (color == 5) {
								g.setColor(Color.ORANGE);
							}
						}
					}
				}
			}
		}
		g.fillRect(this.x, this.y, this.ancho, this.alto);
	}

	@Override
	public void actua() {
	}

}
