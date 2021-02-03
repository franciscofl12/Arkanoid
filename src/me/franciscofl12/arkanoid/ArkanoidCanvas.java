package me.franciscofl12.arkanoid;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.util.List;

import javax.swing.JButton;

public class ArkanoidCanvas extends Canvas {

	/**
	 * Clase que sobrescribe un objeto de tipo Canvas. Lo utilizaremos para poder pintar sobre él todo lo que queramos
	 * @author Usuario
	 *
	 */
	
	    
		List<Actor> actores = null;

		/**
		 * Constructor
		 * @param actores
		 */
		
		public ArkanoidCanvas (List<Actor> actores) {
			this.actores = actores;
		}
		/**
		 * Sobrescritura del méotod paint(), aquí tengo el control sobre aquello que se va a pintar en pantalla.
		 */
//		@Override
//		public void paint(Graphics g) {
//			// Pinto el fondo
//			this.setBackground(Color.BLACK);
//			
//		}
		
}
