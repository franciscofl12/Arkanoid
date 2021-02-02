package me.franciscofl12.arkanoid;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
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
	static Bola bola = null;
	private static Arkanoid instance = null;
	// Lista con actores que deben incorporarse en la siguiente iteracion del juego
	private List<Actor> newActorsForNextIteration = new ArrayList<Actor>();
	
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
		
		//Creo un mouse listener para detectar el movimiento del raton y mover la nave
		canvas.addMouseMotionListener(new MouseAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				super.mouseMoved(e);
				player.mover(e.getX());
			}	
			public void mouseClicked(MouseEvent e) {
				player.setSpace(true);
			}
		});
		
		/****
		 * El codigo de la tecla de direccion derecha es 39, con esto sabremos y podremos
		 * hacer un simple formato en el cual cuando sea pulsado, la coordenada x se mueva
		 * para la derecha, y a la izquierda de igual manera.
		 */
		canvas.addKeyListener(new KeyAdapter() {
			
			public void keyPressed(KeyEvent e) {
				super.keyPressed(e);
				player.keyPressed(e);
				if (player.isSpace()) {
					bola.velocidadY = 5;
					bola.velocidadX = 5;
				}
			}
			
			public void keyReleased(KeyEvent e) {
				super.keyReleased(e);
				player.keyReleased(e);
			}
		});
		
		// Control del evento de cierre de ventana
		canvas.requestFocus();
		ventana.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		ventana.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				cerrarAplicacion();
			}
		});
			
		canvas.createBufferStrategy(2);
		strategy = canvas.getBufferStrategy();
		
		//Pongo en bucle la musica de fondo
		ArkanoidSound.getInstance().loopSound(ArkanoidSound.MUSICA);
//		ArkanoidSound.getInstance().playSound(ArkanoidSound.getInstance().MUSICA);
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
			pintaMundo();
			// Actualizo el mundo
			actualizaMundo();
			
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
	
	public void actualizaMundo() {
		/****
		 * Creo una lista de los actores que han sido colisionados, si han sido colisionados
		 * seran anadidos a una lista la cual seran luego borrados de la lista por defecto
		 */
		List<Actor> actorsForRemoval = new ArrayList<Actor>();
		for (Actor actor : actores) {
			if (actor.isMarkedForRemoval()) {
				actorsForRemoval.add(actor);
			}
		}
		// Reviso si le han dado al espacio para estar pegado a la barra
		if (player.isSpace() == false) {
			bola.setX(player.getX()+12);
			bola.setY(player.getY()-20);
		}
		
		// Elimino los actores marcados para su eliminacion
		for (Actor actor : actorsForRemoval) {
			actores.remove(actor);
		}
		
		// Limpio la lista de actores para eliminar
		actorsForRemoval.clear();
		
		// Añado los nuevos actores(explosiones) para hacer una animacion cuando el ladrillo desaparezca
		Arkanoid.actores.addAll(newActorsForNextIteration);
		this.newActorsForNextIteration.clear();
		
		
		
		/****
		 * Para cada actor comprobaremos si tiene una colision con otro actor
		 * el unico caso posible que deberia de aparecer es que la pelota colisione
		 * con un rectangulo (ladrillo)
		 */
		for (Actor actor1 : actores) {
			// Creo un rectangulo para este actor.
			Rectangle rect1 = new Rectangle(actor1.getX(), actor1.getY(), actor1.getAncho(), actor1.getAlto());
			// Compruebo un actor con cualquier otro actor
			for (Actor actor2 : actores) {
				// Evito comparar un actor consigo mismo, ya que eso siempre provocaria una colision y no tiene sentido
				if (!actor1.equals(actor2)) {
					// Formo el rectangulo del actor 2
					Rectangle rect2 = new Rectangle(actor2.getX(), actor2.getY(), actor2.getAncho(), actor2.getAlto());
					// Si los dos rectangulos tienen alguna interseccion, notifico una colision en los dos actores
					if (rect1.intersects(rect2)) {
						actor1.collisionWith(actor2); // El actor 1 colisiona con el actor 2
						actor2.collisionWith(actor1); // El actor 2 colisiona con el actor 1
					}
				}
			}
		}
	}
	
	
	@SuppressWarnings("static-access")
	public void pintaMundo() {
		
		Graphics2D g = (Graphics2D) strategy.getDrawGraphics();
		
		g.setColor(Color.black);
		g.fillRect(0, 0,canvas.getWidth(),canvas.getHeight()); 
		// Pinto cada uno de los actores
		for (Actor a : this.actores) {
			a.paint(g);
			a.paintImagen(g);
		}
		
		strategy.show(); 
	}

	
	/**
	 * 
	 * @return
	 */
	private static List<Actor> creaActores () {
		List<Actor> actores = new ArrayList<Actor>();
		
		//Construyo un player para este juego y lo agrego a la lista
//		player = new Player(155, 500, Player.IMAGEN_PLAYER);
		player = new Player();
		player.setX(155);
		player.setY(500);
		actores.add(player);
		
		//Construyo la bola para el juego y la agrego a la lista
//		Bola bola = new Bola(155,300,Bola.IMAGEN_BOLA);
		bola = new Bola();
//		while (GameStart = false) {
//			bola.setX(155);
//			bola.setY(player.getAncho()/2);
//		}
		bola.setX(player.getX()+12);
		bola.setY(player.getY()-20);
		actores.add(bola);
		
		// Creo los Ladrillos del juego
		for (int j=0; j< 6; j++) {
			int aux = j; 
			espacioEntreLadrillosX = 5;
			for (int i = 0; i < 12; i++) {
				if (i == 11) {
					aux++;
					if (aux == 6) {
						aux--;
					}
				}
				Ladrillo l = new Ladrillo(aux);
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

	public void addNewActorToNextIteration (Actor newActor) {
		this.newActorsForNextIteration.add(newActor);
	}

	
}
