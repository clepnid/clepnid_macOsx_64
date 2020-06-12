package ventana;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.SWTResourceManager;

import portapapeles.Contenido;
import portapapeles.Ficheros;
import red.broadcast.BroadcastingIpControl;
import red.compartirContenido.Cliente;
import red.compartirContenido.Servidor;
import teclado.GlobalKeys;

import java.util.ArrayList;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;

/**
 * Ventana de la aplicacion para mostrar por pantalla el contenido recogido en
 * el portapapeles del sistema {@link portapapeles.Clip} usando {@link GlobalKeys} del propio
 * equipo o de otro equipo conectado en la misma red {@link Cliente} /
 * {@link Servidor}.
 * 
 * @author: Pavon
 * @version: 20/05/2020
 * @since 1.0
 */

public class Ventana {

	public Display display;
	public Shell shlSwt;
	protected ProgressBar BarraProgreso;
	protected Label lblPorcentajeBarraProgreso;
	public BarraProgreso panBarraProgreso;
	public PanelTexto bloque1;
	public PanelImagen bloque2;
	public PanelFichero bloque3;
	public Composite panCuerpo;
	public Listener listener;
	public ScrolledComposite c2;
	public ArrayList<PanelContenido> listaContenido = new ArrayList<PanelContenido>();
	public BroadcastingIpControl controlBroadcast;
	public Ficheros ficheros;
	public Contenido contenido = null;
	public GlobalKeys teclas;
	public Label lblBotonServidor;
	public Image lblBotonServidorImage;
	private Composite composite;
	public Label lblHayServidor;

	/**
	 * Crea y añade un objeto {@link PanelContenido} mostrando el tipo de contenido
	 * disponible para recoger por {@link GlobalKeys}.
	 * 
	 * @param contenido {@link Contenido} recogido por {@link Cliente}.
	 */

	public void mostrarContenidoPorPantalla(Contenido contenido) {
		if (!controlBroadcast.hayServidorEnLista && !controlBroadcast.serServidor || controlBroadcast.noSerServidor) {
			vaciarLista();
		} else {
			if (this.contenido == null || !(contenido == null) && !contenido.equals(this.contenido)) {
				switch (contenido.tipo) {
				case Texto:
					vaciarLista();
					anyadirPanelTexto(panCuerpo, this, contenido.texto);
					this.contenido = contenido;
					break;
				case Imagen:
					vaciarLista();
					anyadirPanelImagen(panCuerpo, this, contenido.imagen_ancho, contenido.imagen_alto);
					this.contenido = contenido;
					break;
				case Ficheros:
					vaciarLista();
					for (int i = 0; i < contenido.listaFicheros.size(); i++) {
						anyadirPanelFichero(panCuerpo, this, contenido.listaFicheros.get(i)[0],
								contenido.listaFicheros.get(i)[1], contenido.listaFicheros.get(i)[2],
								contenido.listaFicheros.get(i)[3], i);
					}
					this.contenido = contenido;
					break;

				default:
					break;
				}

			}
		}
	}

	public static void main(String[] args) {
		Ventana ventana = new Ventana();
		BroadcastingIpControl controlBroadcast = new BroadcastingIpControl();
		controlBroadcast.compartirLista(ventana);
		GlobalKeys teclas = new GlobalKeys(ventana);
		ventana.teclas = teclas;
		ventana.controlBroadcast = controlBroadcast;
		ventana.open();
	}

	/**
	 * Constructor de la Clase a la que hay que añadir objetos:
	 * {@link BroadcastingIpControl}, {@link GlobalKeys}.
	 */

	public Ventana() {

	}

	/**
	 * Abre la ventana.
	 */
	public void open() {
		display = new Display();
		createContents();
		shlSwt.open();
		shlSwt.layout();
		center(shlSwt);

		// Evento que abre una ventana modal para validar o cancelar el cierre de
		// ventana.
		shlSwt.addListener(SWT.Close, new Listener() {
			public void handleEvent(Event event) {
				int style = SWT.APPLICATION_MODAL | SWT.YES | SWT.NO;
				MessageBox messageBox = new MessageBox(shlSwt, style);
				messageBox.setText("ClipRed");
				messageBox.setMessage("¿Quieres cerrar ClipRed?");
				if (messageBox.open() == SWT.YES) {
					// llamamos el metodo cerrar de la aplicacion para cerrar los demas hilos.
					cerrar();
					event.doit = true;
				} else {
					event.doit = false;
				}
			}
		});
		lblBotonServidorImage = SWTResourceManager.getImage(Ventana.class, "/imagenes/btn_on.gif");
		while (!shlSwt.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		if (!display.isDisposed()) {
			display.dispose();
			shlSwt.dispose();
		}
	}

	/**
	 * Cierra la {@link Shell} de {@link Ventana}, la conexion de esta con el
	 * sistema operativo {@link Display} y las Clases.
	 * {@link BroadcastingIpControl}, {@link GlobalKeys}
	 */

	public void cerrar() {
		teclas.cerrar();
		controlBroadcast.close();
		shlSwt.dispose();
	}

	/**
	 * Ventana se muestra centrada
	 * 
	 * @param shell ventana para centrar
	 */

	private static void center(Shell shell) {
		Monitor monitor = shell.getMonitor();
		Rectangle bounds = monitor.getBounds();
		Rectangle rect = shell.getBounds();
		int x = bounds.x + (bounds.width - rect.width) / 2;
		int y = bounds.y + (bounds.height - rect.height) / 2;
		shell.setLocation(x, y);
	}


	/**
	 * Crea los componentes dentro de {@link Ventana}.
	 */

	protected void createContents() {
		shlSwt = new Shell(display);
		shlSwt.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		shlSwt.setImage(getImageValida(display, "./src/imagenes/clipboard.gif"));
		shlSwt.setSize(600, 400);
		shlSwt.setMinimumSize(400, 0);
		shlSwt.setText("Netclip");
		shlSwt.setLayout(new GridLayout(1, false));

		c2 = new ScrolledComposite(shlSwt, SWT.NONE | SWT.H_SCROLL | SWT.V_SCROLL);
		c2.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		panCuerpo = new Composite(c2, SWT.PUSH);
		panCuerpo.setToolTipText("Contenido disponibles");
		panCuerpo.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		panCuerpo.setLayout(new GridLayout(1, false));
		panCuerpo.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		panBarraProgreso = new BarraProgreso(this, panCuerpo, SWT.NONE, display, shlSwt);
		GridData gd_panBarraProgreso = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_panBarraProgreso.heightHint = 79;
		panBarraProgreso.setLayoutData(gd_panBarraProgreso);
		panBarraProgreso.esconderPanelProgressBar(true);
		c2.setContent(panCuerpo);
		c2.setExpandHorizontal(true);
		c2.setExpandVertical(true);

		composite = new Composite(shlSwt, SWT.NONE);
		composite.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		composite.setLayout(new GridLayout(3, false));
		GridData gd_composite = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_composite.heightHint = 32;
		composite.setLayoutData(gd_composite);

		Label lblServidor = new Label(composite, SWT.NONE);
		lblServidor.setToolTipText("Equipo en modo Servidor");
		lblServidor.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		lblServidor.setText("Servidor");

		lblBotonServidor = new Label(composite, SWT.NONE);
		lblBotonServidor.setToolTipText("Apagado");
		lblBotonServidor.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));
		lblBotonServidor.setImage(SWTResourceManager.getImage(Ventana.class, "/imagenes/btn_off.gif"));
		lblBotonServidor.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));

		lblHayServidor = new Label(composite, SWT.NONE);
		lblHayServidor.setToolTipText("Estado");
		lblHayServidor.setImage(SWTResourceManager.getImage(Ventana.class, "/imagenes/btn_gris.gif"));
		lblHayServidor.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		lblHayServidor.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1));

		// Eventos al pulsar el botón servidor
		lblBotonServidor.addMouseListener(new MouseAdapter() {

			public void mouseUp(MouseEvent arg0) {
				if (lblBotonServidor.getImage()
						.equals(SWTResourceManager.getImage(Ventana.class, "/imagenes/btn_on_down.gif"))) {
					controlBroadcast.dejarDeSerServidor();
					display.asyncExec(new Runnable() {
						public void run() {
							lblBotonServidor
									.setImage(SWTResourceManager.getImage(Ventana.class, "/imagenes/btn_off.gif"));
							lblBotonServidor.setToolTipText("Apagado");
						}
					});
					lblHayServidor.setImage(SWTResourceManager.getImage(Ventana.class, "/imagenes/btn_gris.gif"));
					vaciarLista();
					contenido = null;
				}
			}

			public void mouseDown(MouseEvent arg0) {
				if (lblBotonServidor.getImage()
						.equals(SWTResourceManager.getImage(Ventana.class, "/imagenes/btn_on_up.gif"))) {
					lblBotonServidor.setImage(SWTResourceManager.getImage(Ventana.class, "/imagenes/btn_on_down.gif"));
				}
			}

		});

		// Eventos al posar raton encima del boton servidor
		lblBotonServidor.addListener(SWT.MouseEnter, new Listener() {
			public void handleEvent(Event e) {
				if (lblBotonServidorImage.equals(lblBotonServidor.getImage())) {
					lblBotonServidor.setImage(SWTResourceManager.getImage(Ventana.class, "/imagenes/btn_on_up.gif"));
					lblBotonServidor.setToolTipText("Encendido");
				}
			}
		});

		// Eventos al posar raton fuera del boton servidor
		lblBotonServidor.addListener(SWT.MouseExit, new Listener() {
			public void handleEvent(Event e) {
				if (lblBotonServidor.getImage()
						.equals(SWTResourceManager.getImage(Ventana.class, "/imagenes/btn_on_up.gif"))) {
					lblBotonServidor.setImage(SWTResourceManager.getImage(Ventana.class, "/imagenes/btn_on.gif"));
				}
			}
		});

	}

	/**
	 * Vacia la lista de {@link PanelContenido}.
	 */

	public void vaciarLista() {
		display.asyncExec(new Runnable() {
			public void run() {
				for (PanelContenido panelContenido : listaContenido) {
					panelContenido.gd_panObjeto.exclude = true;
					panelContenido.setVisible(false);
					panCuerpo.layout();
				}
			}
		});
	}

	/**
	 * Cambia el botón representando el estado del servidor
	 * {@link BroadcastingIpControl}.
	 * 
	 * @param verde <code>true</code> definir imagen de botón en verde,
	 *              <code>false</code> definir imagen de boton en gris.
	 */

	public void cambiarbtnHayServidor(boolean verde) {

		if (verde) {
			display.asyncExec(new Runnable() {
				public void run() {
					if (!lblHayServidor.getImage()
							.equals(SWTResourceManager.getImage(Ventana.class, "/imagenes/btn_verde.gif"))) {
						lblHayServidor.setImage(SWTResourceManager.getImage(Ventana.class, "/imagenes/btn_verde.gif"));
					}
					if (!lblBotonServidor.getImage()
							.equals(SWTResourceManager.getImage(Ventana.class, "/imagenes/btn_off.gif"))) {
						lblBotonServidor.setImage(SWTResourceManager.getImage(Ventana.class, "/imagenes/btn_off.gif"));
					}
				}
			});
		} else {
			display.asyncExec(new Runnable() {
				public void run() {
					if (lblHayServidor.getImage()
							.equals(SWTResourceManager.getImage(Ventana.class, "/imagenes/btn_verde.gif"))) {
						lblHayServidor.setImage(SWTResourceManager.getImage(Ventana.class, "/imagenes/btn_gris.gif"));
						vaciarLista();
						contenido = null;
					}
				}
			});
		}
	}

	/**
	 * Añade un componente {@link PanelTexto} a la lista contenido de esta Clase.
	 * 
	 * @param parent  {@link Composite} panel padre para {@link PanelTexto}.
	 * @param ventana {@link Ventana} para mostrar por pantalla el componente.
	 * @param texto   {@link String} a mostrar por {@link Ventana}.
	 */

	public void anyadirPanelTexto(final Composite parent, final Ventana ventana, final String texto) {
		display.asyncExec(new Runnable() {
			public void run() {
				listaContenido.add(new PanelTexto(parent, ventana, SWT.NONE, texto));
				c2.setMinSize(panCuerpo.computeSize(SWT.DEFAULT, SWT.DEFAULT));
				panCuerpo.layout();
			}
		});
	}

	/**
	 * Añade un componente {@link PanelImagen} a la lista contenido de esta Clase.
	 * 
	 * @param parent  {@link Composite} panel padre para {@link PanelTexto}.
	 * @param ventana {@link Ventana} para mostrar por pantalla el componente.
	 * @param ancho   numero con el ancho de la imagen a mostrar por
	 *                {@link Ventana}.
	 * @param alto    numero con el alto de la imagen a mostrar por {@link Ventana}.
	 */

	public void anyadirPanelImagen(final Composite parent, final Ventana ventana, final int ancho, final int alto) {
		display.asyncExec(new Runnable() {
			public void run() {
				listaContenido.add(new PanelImagen(parent, ventana, SWT.NONE, ancho, alto));
				c2.setMinSize(panCuerpo.computeSize(SWT.DEFAULT, SWT.DEFAULT));
				panCuerpo.layout();
			}
		});
	}

	/**
	 * Añade un componente {@link PanelFichero} a la lista contenido de esta Clase.
	 * 
	 * @param parent  {@link Composite} panel padre para {@link PanelTexto}.
	 * @param ventana {@link Ventana} para mostrar por pantalla el componente.
	 * @param nombre  {@link String} con el nombre del fichero
	 * @param peso    {@link String} con el peso del fichero
	 * @param formato {@link String} con el formato del fichero
	 * @param ruta    {@link String} con la ruta del fichero
	 * @param numero  numero posición de la lista contenido.
	 */

	public void anyadirPanelFichero(final Composite parent, final Ventana ventana, final String nombre, final String peso, final String formato,
			final String ruta, final int numero) {
		display.asyncExec(new Runnable() {
			public void run() {
				listaContenido.add(new PanelFichero(parent, ventana, SWT.NONE, nombre, peso, formato, ruta, numero));
				c2.setMinSize(panCuerpo.computeSize(SWT.DEFAULT, SWT.DEFAULT));
				panCuerpo.layout();
			}

		});
	}

	/**
	 * Procesa una imagen.
	 * 
	 * @param display {@link Display} controlador entre la ventana y el sistema
	 *                operativo.
	 * @param ruta    {@link String} ruta de la imagen a validar.
	 * @return {@link Image}
	 */

	public Image getImageValida(Display display, String ruta) {
		Image image = new Image(display, ruta);
		GC gc = new GC(image);
		gc.drawImage(image, 0, 0);
		gc.dispose();
		return image;
	}
}
