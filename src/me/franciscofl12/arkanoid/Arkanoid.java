package me.franciscofl12.arkanoid;

import java.awt.BorderLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
	static Player player = null;
	
	private static Arkanoid instance = null;
	
	// Creo un doble buffer que lo utilizare para que no muestre lagazos a la hora de procesar los ladrillos
	public BufferStrategy strategy;
	
	public static Arkanoid getInstance () {
		if (instance == null) { // Si no está inicializada, se inicializa
			instance = new Arkanoid();
		}
		return instance;
	}
	
	public Arkanoid() {
		ventana = new JFrame("Arkanoid by franciscofl12");
//		try {
//			ventana.setIconImage(ImageIO.read(new File("resources.images/icono.png")));
//		} catch (IOException e1) {
//			e1.printStackTrace();
//		}
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
//		createBufferStrategy(2);
//		strategy = getBufferStrategy();
		canvas.addMouseMotionListener(new MouseAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				super.mouseMoved(e);
				player.mover(e.getX());
			}			
		});
		canvas.addKeyListener(new KeyAdapter() {
			
			/****
			 * El codigo de la tecla de direccion derecha es 39, con esto sabremos y podremos
			 * hacer un simple formato en el cual cuando sea pulsado, la coordenada x se mueva
			 * para la derecha, y a la izquierda de igual manera.
			 */
			
			public void keyPressed(KeyEvent e) {
				super.keyPressed(e);
				int xTecla = player.getX();
				if (e.getKeyCode() == 39) {
					xTecla++;xTecla++;xTecla++;xTecla++;
					player.mover(xTecla);
				}
				else {
					if (player.getX() > 0) {
						if (e.getKeyCode() == 37) {
							xTecla--;xTecla--;xTecla--;xTecla--;
							player.mover(xTecla);
						}
					}
				}
			}
		});
		
		// Control del evento de cierre de ventana
		ventana.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		ventana.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				cerrarAplicacion();
			}
		});
	}

	/**
	 * Main
	 * @param args
	 */
	public static void main(String[] args) {
		// Comienzo un bucle, que consistirá en el juego completo.
		Arkanoid.getInstance().juego();
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
	public void juego () {
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
		player = new Player(155, 500, Player.IMAGEN_PLAYER);
		actores.add(player);
		
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
				espacioEntreLadrillosX++;espacioEntreLadrillosX++;espacioEntreLadrillosX++;
			}
			espacioEntreLadrillosY ++;espacioEntreLadrillosY ++;espacioEntreLadrillosY ++;
		}
		// Devuelvo la lista con todos los actores del juego
		return actores;
	}
	
	public ArkanoidCanvas getCanvas() {
		return canvas;
	}
	
}
