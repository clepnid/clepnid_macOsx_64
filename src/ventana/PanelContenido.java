package ventana;

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

/**
 * Clase que extiende de {@link Composite}.
 * 
 * @author: Pavon
 * @version: 10/04/2020
 * @since 1.0
 */

public class PanelContenido extends Composite {

	public GridData gd_panObjeto;
	public Ventana ventana;

	/**
	 * Constructor del panel con la barra de progreso.
	 * 
	 * @param parent  {@link Composite} panel padre para {@link PanelContenido}.
	 * @param style   numero de referencia de la apariencia que va a tener el
	 *                componente.
	 * @param ventana {@link Ventana} para mostrar por pantalla el componente.
	 */

	public PanelContenido(Composite parent, int style, Ventana ventana) {
		super(parent, style);
		this.ventana = ventana;
	}

	/**
	 * Procesa una imagen.
	 * 
	 * @param display {@link Display} controlador entre la ventana y el sistema
	 *                operativo.
	 * @param ruta    {@link String} ruta de la imagen a validar.
	 */

	Image getCheckedImage(Display display, String ruta) {
		Image image = new Image(display, ruta);
		GC gc = new GC(image);
		gc.drawImage(image, 0, 0);
		gc.dispose();
		return image;
	}

}
