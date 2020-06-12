package ventana;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.SWTResourceManager;

/**
 * Clase que extiende de {@link Composite} para mostrar los datos de una imagen
 * por {@link Ventana}.
 * 
 * @author: Pavon
 * @version: 10/04/2020
 * @since 1.0
 */

public class PanelImagen extends PanelContenido {

	private Composite panImagen;
	private Composite panBoton;
	private Button btnCopiar;
	private Composite panAtributos;
	private Label lblImagen;
	private Label lblNombre;
	public Display display;
	public Shell shell;

	/**
	 * Constructor que define el panel referente a una imagen a mostrar por
	 * {@link Ventana}
	 * 
	 * @param parent  {@link Composite} panel padre para {@link PanelContenido}.
	 * @param ventana {@link Ventana} para mostrar por pantalla el componente.
	 * @param style   numero de referencia de la apariencia que va a tener el
	 *                componente.
	 * @param ancho   numero con el ancho de pixeles de la imagen a mostrar.
	 * @param alto    numero con el alto de pixeles de la imagen a mostrar.
	 */

	public PanelImagen(Composite parent, final Ventana ventana, int style, int ancho, int alto) {
		super(parent, style, ventana);
		setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		setLayout(new GridLayout(3, false));
		gd_panObjeto = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_panObjeto.heightHint = 146;
		setLayoutData(gd_panObjeto);

		panImagen = new Composite(this, SWT.NONE);
		panImagen.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		panImagen.setLayout(new GridLayout(1, false));
		GridData gd_panImagen = new GridData(SWT.LEFT, SWT.FILL, false, true, 1, 1);
		gd_panImagen.heightHint = 182;
		gd_panImagen.widthHint = 133;
		panImagen.setLayoutData(gd_panImagen);

		lblImagen = new Label(panImagen, SWT.NONE);
		lblImagen.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		lblImagen.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false, 1, 1));
		lblImagen.setImage(getCheckedImage(display, "./src/imagenes/imagen.gif"));

		panAtributos = new Composite(this, SWT.NONE);
		panAtributos.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		panAtributos.setLayout(new GridLayout(1, false));
		panAtributos.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		lblNombre = new Label(panAtributos, SWT.NONE);
		lblNombre.setBackground(SWTResourceManager.getColor(SWT.COLOR_LIST_BACKGROUND));
		lblNombre.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1));
		lblNombre.setText("Dimensiones: ");

		Label lblPeso = new Label(panAtributos, SWT.NONE);
		lblPeso.setBackground(SWTResourceManager.getColor(SWT.COLOR_LIST_BACKGROUND));
		lblPeso.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1));
		lblPeso.setText("  Ancho: " + ancho + "px");

		Label lblFormato = new Label(panAtributos, SWT.NONE);
		lblFormato.setBackground(SWTResourceManager.getColor(SWT.COLOR_LIST_BACKGROUND));
		lblFormato.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1));
		lblFormato.setText("  Alto: " + alto + "px");

		panBoton = new Composite(this, SWT.NONE);
		panBoton.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		panBoton.setLayout(new GridLayout(1, false));
		panBoton.setLayoutData(new GridData(SWT.RIGHT, SWT.FILL, false, false, 1, 1));

		btnCopiar = new Button(panBoton, SWT.NONE);
		btnCopiar.setToolTipText("Copiar fichero al portapapeles");
		btnCopiar.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, true, 1, 1));
		btnCopiar.setText("Copiar");

		btnCopiar.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				switch (e.type) {
				case SWT.Selection:
					ventana.teclas.eventos.copiarContenidoBtn();
					break;
				}
			}
		});

	}

}
