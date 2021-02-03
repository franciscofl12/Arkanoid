package me.franciscofl12.arkanoid;

import java.awt.BorderLayout;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JButton;
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
	private static int movimientoMenu;
	private static JFrame ventana = null;
	private static List<Actor> actores = new ArrayList<Actor>();
	private static List<Ladrillo> ladrillos = new ArrayList<Ladrillo>();
	private static ArkanoidCanvas canvas = null;
	static Player player = null;
	static Bola bola = null;
	private static int vidas = 5;
	private static int primerMovimientoBola = 0;
	private static Arkanoid instance = null;
	private static boolean isGameOver = false;
	private static boolean isGameStarted = false;
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
		ventana.setIconImage(ArkanoidSprite.getInstance().getSprite("icono.png"));
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
				if (isGameOver == false) {
					player.mover(e.getX());
				}
				if (isGameStarted == false) {
					movimientoMenu = e.getY();
					seleccionMenu(e.getY());
				}
			}	
			
		});
		
		
		//Añado un MouseListener para captar cuando el usuario hace click
		canvas.addMouseListener(new MouseAdapter() {
			@Override
		    public void mousePressed(MouseEvent e) {
				super.mousePressed(e);
				player.mousePressed(e);
				if (primerMovimientoBola == 0) {
					primerMovimientoBola();
				}
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
					if (primerMovimientoBola == 0) {
						primerMovimientoBola();
					}
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
		
		
		//Contador para calcular los segundos que pasan despues de empezar a cargar el juego
//		long contadorDeCuandoEmpiezaElJuego = System.currentTimeMillis();
//		long contadorDeCuandoEmpiezaElJuegoSegundo = System.currentTimeMillis();
//		while (contadorDeCuandoEmpiezaElJuegoSegundo <= (contadorDeCuandoEmpiezaElJuego + 5*1000)) {
//			contadorDeCuandoEmpiezaElJuegoSegundo = System.currentTimeMillis();
//			if(contadorDeCuandoEmpiezaElJuegoSegundo >= (contadorDeCuandoEmpiezaElJuego + 5*1000)) {
//				  if (primerMovimientoBola == 0) primerMovimientoBola();
//				  break;
//			}
//			
//		}
		
		
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
		Arkanoid.getInstance().pantallaPrincipal();
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
	
	/****
	 * Pantalla principal para empezar el juego
	 */
	public void pantallaPrincipal() {
		do {
			Graphics2D g = (Graphics2D) strategy.getDrawGraphics();
			
			g.drawImage(ArkanoidSprite.getInstance().getSprite("bg.jpg"),0,0,null); // Fondo
			g.drawImage(ArkanoidSprite.getInstance().getSprite("Arkanoid-logo.png"),20,20,null); //Arkanoid Logo
			g.drawImage(ArkanoidSprite.getInstance().getSprite("seleccion.png"),65,seleccionMenu(movimientoMenu),null); // seleccion imagen ">"
			g.drawImage(ArkanoidSprite.getInstance().getSprite("1player.png"),115,245,null); // 1 Jugador
			g.drawImage(ArkanoidSprite.getInstance().getSprite("2player.png"),115,270,null); // 2 Jugadores
			g.drawImage(ArkanoidSprite.getInstance().getSprite("exit.png"),111,295,null); // Salir
			g.drawImage(ArkanoidSprite.getInstance().getSprite("textodebajo.png"),90,500,null); // Copyright ©
		    strategy.show();
		} while (true);
		
	}

	
	/**
	 * Bucle del juego principal
	 */
	public void juego () {
		isGameStarted = true;
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
		
		// Llamo al metodo para que la velocidad de la bola vaya aumentando
		bola.velocidadAumenta(1.009);
		
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
	
		paintStatus(g);
		
		if (vidas <= 0) paintGameOver(g);
		strategy.show(); 
	}

	public void paintStatus(Graphics2D g) {
		  paintLadrillosRestantes(g);
		  //paintLevel(g);
		  paintVidas(g);
//		  paintGameOver(g);
		  
	}
	
	public void paintLadrillosRestantes(Graphics2D g) {
		// Pintamos el texto de Ladrillos Restantes
		g.setFont(new Font("Arial", Font.BOLD,12));
		g.setPaint(Color.white);
		g.drawString("Ladrillos Restantes: " + ladrillos.size(), 10, canvas.getHeight() - 15);
	}
	
	public void paintVidas(Graphics2D g) {
		g.setFont(new Font("Arial", Font.BOLD,12));
		if (vidas>=3) {
			g.setPaint(Color.white);
		}
		else {
			g.setPaint(Color.red);
		}
		g.drawString("Vidas: " + vidas, 150, canvas.getHeight() - 15);
	}
	
	public void paintGameOver(Graphics2D g) {
		Color miColor = new Color(0, 0, 0, 127);
		g.setColor(miColor);
		g.fillRect(0, 0,canvas.getWidth(),canvas.getHeight()); 
		g.setColor(Color.white);
		g.setFont(new Font("Arial",Font.BOLD,20));
		g.drawString("GAME OVER", canvas.getWidth() / 2 - 65 , canvas.getHeight() / 2);
		strategy.show();
	}
	
	/**
	 * 
	 * @return
	 */
	private static List<Actor> creaActores () {
		List<Actor> actores = new ArrayList<Actor>();
		ladrillos = new ArrayList<Ladrillo>(); //Lista de ladrillos que utilizare para saber cuantos ladrillos hay
		
		//Construyo un player para este juego y lo agrego a la lista
		player = new Player();
		player.setX(155);
		player.setY(500);
		actores.add(player);
		
		//Construyo la bola para el juego y la agrego a la lista
		bola = new Bola();
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
				ladrillos.add(l);
				
				espacioEntreLadrillosX++;espacioEntreLadrillosX++;espacioEntreLadrillosX++;
			}
			espacioEntreLadrillosY ++;espacioEntreLadrillosY ++;espacioEntreLadrillosY ++;
		}
		// Devuelvo la lista con todos los actores del juego
		return actores;
	}
	
	/****
	 * 
	 * @return canvas
	 */
	
	public ArkanoidCanvas getCanvas() {
		return canvas;
	}
	
	/****
	 * 
	 * @param newActor, Agregar los nuevos actores a la escena
	 */
	public void addNewActorToNextIteration (Actor newActor) {
		this.newActorsForNextIteration.add(newActor);
	}
	
	//Metodo para mover a la bola por primera vez
	public static void primerMovimientoBola() {
		primerMovimientoBola++; //Variable auxiliar para saber si se ha ejecutado este metodo
		bola.velocidadY = 5;
		bola.velocidadX = 5;
	}
	
	public void vidaPerdida(int dificultad) {
		if (!(vidas <= 0)) vidas -= dificultad;
		if (vidas <= 0) {
			setGameOver();
		}
	}
	
	public void setGameOver(){
		isGameOver = true;
		bola.velocidadY = 0.1;
		bola.velocidadX = 0.1;
	}
	
	//245 , 270 , 295
	public int seleccionMenu(int x) {
		if (x >= 245 && x<=269) {
			return 245;
		}
		else {
			if (x >= 270 && x<=294) {
				return 270;
			}
			else {
				if (x >= 295 && x<=320) {
					return 295;
				}
				else {
					if (x < 245) {
						return 245;
					}
					else {
						if (x > 320) {
							return 295;
						}
					}
				}
			}
		}
		return x;
	}
}
