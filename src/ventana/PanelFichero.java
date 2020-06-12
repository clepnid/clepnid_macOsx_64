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
 * Clase que extiende de {@link Composite} para mostrar los datos de un array de
 * ficheros por {@link Ventana}.
 * 
 * @author: Pavon
 * @version: 10/04/2020
 * @since 1.0
 */

public class PanelFichero extends PanelContenido {

	private Composite panImagen;
	private Composite panBoton;
	private Button btnGuardar;
	private Composite panAtributos;
	private Label lblImagen;
	private Label lblNombre;
	public Display display;
	public Shell shell;
	public int numero;

	/**
	 * Constructor que define el panel referente a un fichero a mostrar por
	 * {@link Ventana}
	 * 
	 * @param parent      {@link Composite} panel padre para {@link PanelContenido}.
	 * @param ventana     {@link Ventana} para mostrar por pantalla el componente.
	 * @param style       numero de referencia de la apariencia que va a tener el
	 *                    componente.
	 * @param nombre      {@link String} nombre del fichero a mostrar.
	 * @param pesoFichero {@link String} con el peso del fichero a mostrar.
	 * @param formato {@link String} con el formato del fichero a mostrar.
	 * @param ruta        {@link String} ruta del fichero a mostrar.
	 * @param numero      numeración del componente a mostrar por pantalla.
	 */

	public PanelFichero(Composite parent, final Ventana ventana, int style, String nombre, String pesoFichero, String formato,
			String ruta, final int numero) {
		super(parent, style, ventana);
		this.numero = numero;
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
		lblImagen.setImage(getCheckedImage(display, ruta.replace("\\", "/")));

		panAtributos = new Composite(this, SWT.NONE);
		panAtributos.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		panAtributos.setLayout(new GridLayout(1, false));
		panAtributos.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		lblNombre = new Label(panAtributos, SWT.NONE);
		lblNombre.setBackground(SWTResourceManager.getColor(SWT.COLOR_LIST_BACKGROUND));
		lblNombre.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1));
		lblNombre.setText(nombre);

		Label lblPeso = new Label(panAtributos, SWT.NONE);
		lblPeso.setBackground(SWTResourceManager.getColor(SWT.COLOR_LIST_BACKGROUND));
		lblPeso.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1));
		lblPeso.setText(pesoFichero);

		Label lblFormato = new Label(panAtributos, SWT.NONE);
		lblFormato.setBackground(SWTResourceManager.getColor(SWT.COLOR_LIST_BACKGROUND));
		lblFormato.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1));
		lblFormato.setText(formato);

		panBoton = new Composite(this, SWT.NONE);
		panBoton.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		panBoton.setLayout(new GridLayout(1, false));
		panBoton.setLayoutData(new GridData(SWT.RIGHT, SWT.FILL, false, false, 1, 1));

		btnGuardar = new Button(panBoton, SWT.NONE);
		btnGuardar.setToolTipText("Guardar fichero en carpeta");
		btnGuardar.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, true, 1, 1));
		btnGuardar.setText("Guardar");

		btnGuardar.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				switch (e.type) {
				case SWT.Selection:
					ventana.teclas.eventos.copiarContenidoFicheroBtn(numero);
					break;
				}
			}
		});

	}

}
