package ud.prog3.pr02;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class JLabelEstrella extends JLabel {
	private static final long serialVersionUID = 1L;
	public static final int RADIO_ESFERA_ESTRELLA = 17;  // Radio en píxels del bounding circle de la estrella(para choques)
	private static final boolean DIBUJAR_ESFERA_ESTRELLA = true;  // Dibujado (para depuración) del bounding circle de choque de la estrella
	public static final int TAMANYO_ESTRELLA = 40;  //
	private Long creacion;
	public JLabelEstrella(long milisegundo){
		try {
			setIcon( new ImageIcon( JLabelCoche.class.getResource( "img/estrella.png" ).toURI().toURL() ) );
			creacion=milisegundo;
		} catch (Exception e) {
			System.err.println( "Error en carga de recurso: coche.png no encontrado" );
			e.printStackTrace();
		}
		setBounds( 0, 0, TAMANYO_ESTRELLA, TAMANYO_ESTRELLA );
	}
	public long getTime(){
		return creacion;
	}
	private double miGiro=90;
	public void setGiro(double grados){
		miGiro=miGiro+grados;
	}
	// Redefinición del paintComponent para que se escale y se rote el gráfico
	@Override
	protected void paintComponent(Graphics g) {
//		super.paintComponent(g);   // En este caso no nos sirve el pintado normal de un JLabel
		Image img = ((ImageIcon)getIcon()).getImage();
		Graphics2D g2 = (Graphics2D) g;  // El Graphics realmente es Graphics2D
		// Escalado más fino con estos 3 parámetros:
		g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g2.setRenderingHint(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_QUALITY);
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);	
		// Prepara rotación (siguientes operaciones se rotarán)
        g2.rotate( Math.toRadians(miGiro), TAMANYO_ESTRELLA/2, TAMANYO_ESTRELLA/2 ); // getIcon().getIconWidth()/2, getIcon().getIconHeight()/2 );
        // Dibujado de la imagen
        g2.drawImage( img, 0, 0, TAMANYO_ESTRELLA, TAMANYO_ESTRELLA, null );
        if (DIBUJAR_ESFERA_ESTRELLA) g2.drawOval( TAMANYO_ESTRELLA/2-RADIO_ESFERA_ESTRELLA, TAMANYO_ESTRELLA/2-RADIO_ESFERA_ESTRELLA,
        		RADIO_ESFERA_ESTRELLA*2, RADIO_ESFERA_ESTRELLA*2 );
	}

}
