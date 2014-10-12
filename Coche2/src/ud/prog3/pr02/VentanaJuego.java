package ud.prog3.pr02;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;

/** Clase principal de minijuego de coche para Práctica 02 - Prog III
 * Ventana del minijuego.
 * @author Andoni Eguíluz
 * Facultad de Ingeniería - Universidad de Deusto (2014)
 */
public class VentanaJuego extends JFrame {
	private static final long serialVersionUID = 1L;  // Para serialización
	JPanel pPrincipal;         // Panel del juego (layout nulo)
	MundoJuego miMundo;        // Mundo del juego
	CocheJuego miCoche;        // Coche del juego
	JLabel puntuacion=new JLabel("Puntuacion=0");
    static VentanaJuego miVentana;
	MiRunnable miHilo = null;  // Hilo del bucle principal de juego	
	Boolean [] tecla=new Boolean[4]; 

	/** Constructor de la ventana de juego. Crea y devuelve la ventana inicializada
	 * sin coches dentro
	 */
	public VentanaJuego() {
		for (int i=0;i<4;i++){
			tecla[i]=false;
		}
		// Liberación de la ventana por defecto al cerrar
		setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
		// Creación contenedores y componentes
		pPrincipal = new JPanel();
		Font estilo= new Font("Puntuacion 0",Font.BOLD, 20);
		puntuacion.setHorizontalAlignment(SwingConstants.CENTER);
		puntuacion.setFont(estilo);
		// Formato y layouts
		pPrincipal.setLayout( null );
		pPrincipal.setBackground( Color.white );
		// Añadido de componentes a contenedores
		add( pPrincipal, BorderLayout.CENTER );
		add( puntuacion, BorderLayout.SOUTH );
		// Formato de ventana
		setSize( 750, 500 );
		setResizable( false );
		// Escuchadores de botones
	
		// Añadido para que también se gestione por teclado con el KeyListener
		pPrincipal.addKeyListener( new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				switch (e.getKeyCode()) {
					case KeyEvent.VK_UP: {
						//miMundo.aplicarFuerza(miCoche.fuerzaAceleracionAdelante(),miCoche);
						tecla[0]=true;
						break;
					}
					case KeyEvent.VK_DOWN: {
						//miMundo.aplicarFuerza(miCoche.fuerzaAceleracionAtras(),miCoche);
						tecla[1]=true;
						break;
					}
					case KeyEvent.VK_LEFT: {
						//miCoche.gira( +10 );
						tecla[2]=true;
						break;
					}
					case KeyEvent.VK_RIGHT: {
						//miCoche.gira( -10 );
						tecla[3]=true;
						break;
					}
				}
			}
			public void keyReleased(KeyEvent e) {
				switch (e.getKeyCode()) {
				case KeyEvent.VK_UP: {
					tecla[0]=false;
					break;
				}
				case KeyEvent.VK_DOWN: {
					tecla[1]=false;
					break;
				}
				case KeyEvent.VK_LEFT: {
					tecla[2]=false;
					break;
				}
				case KeyEvent.VK_RIGHT: {
					tecla[3]=false;
					break;
				}
			}
			}
		});
		pPrincipal.setFocusable(true);
		pPrincipal.requestFocus();
		pPrincipal.addFocusListener( new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				pPrincipal.requestFocus();
			}
		});
		// Cierre del hilo al cierre de la ventana
		addWindowListener( new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				if (miHilo!=null) miHilo.acaba();
			}
		});
	}

	
	/** Programa principal de la ventana de juego
	 * @param args
	 */
	public static void main(String[] args) {
		// Crea y visibiliza la ventana con el coche
		try {
			miVentana = new VentanaJuego();
			SwingUtilities.invokeAndWait( new Runnable() {
				@Override
				public void run() {
					miVentana.setVisible( true );
				}
			});
			
			miVentana.miMundo = new MundoJuego( miVentana.pPrincipal );
			miVentana.miMundo.creaCoche( 150, 100 );
			miVentana.miCoche = miVentana.miMundo.getCoche();
			miVentana.miCoche.setPiloto( "Fernando Alonso" );
			// Crea el hilo de movimiento del coche y lo lanza
			miVentana.miHilo = miVentana.new MiRunnable();  // Sintaxis de new para clase interna
			Thread nuevoHilo = new Thread( miVentana.miHilo );
			nuevoHilo.start();
		} catch (Exception e) {
			System.exit(1);  // Error anormal
		}
	}
	
	/** Clase interna para implementación de bucle principal del juego como un hilo
	 * @author Andoni Eguíluz
	 * Facultad de Ingeniería - Universidad de Deusto (2014)
	 */
	class MiRunnable implements Runnable {
		boolean sigo = true;
		@Override
		public void run() {
			// Bucle principal forever hasta que se pare el juego...
			while (sigo) {
				// Mover coche
				miCoche.mueve( 0.040 );
				miMundo.comprobar();
				int fin=miMundo.quitaYRotaEstrellas(6000); 
				if(fin==10){
					JOptionPane.showMessageDialog(pPrincipal, "game over" );
					miVentana.dispose();
					acaba();
				}
				int punt=miMundo.choquesConEstrellas();
				puntuacion.setText("Puntuacion="+" "+punt);
				// Chequear choques
				// (se comprueba tanto X como Y porque podría a la vez chocar en las dos direcciones (esquinas)
				if (miMundo.hayChoqueHorizontal(miCoche)) // Espejo horizontal si choca en X
					miMundo.rebotaHorizontal(miCoche);
				if (miMundo.hayChoqueVertical(miCoche)) // Espejo vertical si choca en Y
					miMundo.rebotaVertical(miCoche);
				if(tecla[0]){
					miMundo.aplicarFuerza(miCoche.fuerzaAceleracionAdelante(), miCoche);
				}
				if (tecla[0]==false){
					miMundo.aplicarFuerza(0, miCoche);
				}
				if(tecla[1]){
					miMundo.aplicarFuerza(miCoche.fuerzaAceleracionAtras(), miCoche);
				}
				
				if(tecla[2]){
					miCoche.gira( +10 );
				}
				if(tecla[3]){
					miCoche.gira( -10 );
				}
				// Dormir el hilo 40 milisegundos
				try {
					Thread.sleep( 40 );
				} catch (Exception e) {
				}
			}
		}
		/** Ordena al hilo detenerse en cuanto sea posible
		 */
		public void acaba() {
			sigo = false;
		}
	};
	
}
