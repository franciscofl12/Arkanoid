package me.franciscofl12.arkanoid;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * Clase principal, que crea los Ladrillos
 *
 */
public class Arkanoid {

	private static int FPS = 60;
	private static int espacioEntreLadrillosX = 5;
	private static int espacioEntreLadrillosY = 5;
	private static JFrame ventana = null;
	private static List<Actor> actores = new ArrayList<Actor>();
	private static ArkanoidCanvas canvas = null;
	/**
	 * Main
	 * @param args
	 */
	public static void main(String[] args) {
		ventana = new JFrame("Arkanoid by franciscofl12");
		try {
			ventana.setIconImage(ImageIO.read(new File("resources.images/icono.png")));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		ventana.setBounds(0, 0, 360, 600);
		
		// Para colocar objetos sobre la ventana debo asignarle un "layout" (plantilla) al panel principal de la ventana
		ventana.getContentPane().setLayout(new BorderLayout());
		
		// Creo una lista de actores que intervendrá en el juego.
		actores = creaActores();
		
		// Creo y agrego un canvas, es un objeto que permitirá dibujar sobre él
		canvas = new ArkanoidCanvas(actores);
		ventana.getContentPane().add(canvas, BorderLayout.CENTER);
		// Consigo que la ventana no se redibuje por los eventos de Windows
		ventana.setIgnoreRepaint(true);
		// Hago que la ventana sea visible
		ventana.setVisible(true);
		//Hago que la ventana no se pueda reescalar, por lo que no me movera los objetos que yo cree
		ventana.setResizable(false);
		
		// Control del evento de cierre de ventana
		ventana.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		ventana.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				cerrarAplicacion();
			}
		});
		
		// Comienzo un bucle, que consistirá en el juego completo.
		juego();
	}
	
	
	
	
	/**
	 * Al cerrar la aplicación preguntaremos al usuario si está seguro de que desea salir.
	 */
	private static void cerrarAplicacion() {
		String [] opciones ={"Aceptar","Cancelar"};
		int eleccion = JOptionPane.showOptionDialog(ventana,"¿Desea cerrar la aplicación?","Salir de la aplicación",
		JOptionPane.YES_NO_OPTION,
		JOptionPane.QUESTION_MESSAGE, null, opciones, "Aceptar");
		if (eleccion == JOptionPane.YES_OPTION) {
			System.exit(0);
		}
	}
	
	
	

	
	/**
	 * Bucle del juego principal
	 */
	public static void juego () {
		int millisPorCadaFrame = 1000 / FPS;
		do {
			// Redibujo la escena tantas veces por segundo como indique la variable FPS
			// Tomo los millis actuales
			long millisAntesDeProcesarEscena = new Date().getTime();
			
			// Redibujo la escena
			canvas.repaint();
			
			// Recorro todos los actores, consiguiendo que cada uno de ellos actúe
			for (Actor a : actores) {
				a.actua();
			}
			
			// Calculo los millis que debemos parar el proceso, generando 60 FPS.
			long millisDespuesDeProcesarEscena = new Date().getTime();
			int millisDeProcesamientoDeEscena = (int) (millisDespuesDeProcesarEscena - millisAntesDeProcesarEscena);
			int millisPausa = millisPorCadaFrame - millisDeProcesamientoDeEscena;
			millisPausa = (millisPausa < 0)? 0 : millisPausa;
			// "Duermo" el proceso principal durante los milllis calculados.
			try {
				Thread.sleep(millisPausa);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} while (true);
	}
	
	
	/**
	 * 
	 * @return
	 */
	private static List<Actor> creaActores () {
		List<Actor> actores = new ArrayList<Actor>();
		
		//Construyo un player para este juego y lo agrego a la lista
		Player jugador = new Player(155, 400, Player.IMAGEN_PLAYER);
		actores.add(jugador);
		
		//Construyo la bola para el juego y la agrego a la lista
		Bola bola = new Bola(155,300,Bola.IMAGEN_BOLA);
		actores.add(bola);
		
		// Creo los Ladrillos del juego
		for (int j=0; j< 6; j++) {
			espacioEntreLadrillosX = 5;
			for (int i = 0; i < 12; i++) {
				Ladrillo l = new Ladrillo(0, 0, Ladrillo.IMAGEN_LADRILLO_0, "l", j);
				l.x = i*l.getAncho()+espacioEntreLadrillosX;
				l.y = j*l.getAlto()+espacioEntreLadrillosY;
				actores.add(l);
				espacioEntreLadrillosX++;
				espacioEntreLadrillosX++;
				espacioEntreLadrillosX++;
			}
			espacioEntreLadrillosY ++;
			espacioEntreLadrillosY ++;
			espacioEntreLadrillosY ++;
		}
		// Devuelvo la lista con todos los actores del juego
		return actores;
	}

	/**
	 * Obtención de un número aleatorio en unos límites
	 * @param minimo
	 * @param maximo
	 * @return
	 */
	private static int numAleatorio (int minimo, int maximo) {
		return (int) Math.round(Math.random() * (maximo - minimo) + minimo);
	}
}
