package teclado;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.wb.swt.SWTResourceManager;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import portapapeles.Clip;
import portapapeles.Contenido;
import portapapeles.DatoSeleccion;
import portapapeles.Ficheros;
import portapapeles.Contenido.Tipo;
import red.Serializar_funciones;
import red.broadcast.BroadcastingIpControl;
import red.compartirFicheros.Cliente;
import red.compartirFicheros.Servidor;
import ventana.Ventana;

/**
 * Clase que implementa un controlador de teclas del sistema.
 * {@link NativeKeyListener}
 * 
 * @author: Pavon
 * @version: 10/05/2020
 * @since 1.0
 */

public class EventoTeclasGlobal implements NativeKeyListener {

	private Set<Integer> pressed = new HashSet<Integer>();
	private static boolean copiar = false, pegar = false;
	private Clip clip;
	Transferable backupClip;
	public String rutaEscritorio = "";
	private Ventana ventana;

	EventoTeclasGlobal(Ventana ventana) {
		clip = new Clip();
		this.ventana = ventana;
	}

	/**
	 * Define el evento de pulsar una tecla del sistema, controlando si la
	 * combinación de estas es la indicada (Command + Shift + 1) o (Command + Shift + 2)
	 * para realizar las acciones de copiar y pegar.
	 * 
	 * @param arg0 {@link NativeKeyEvent} evento al pulsar una tecla.
	 */

	@Override
	public void nativeKeyPressed(NativeKeyEvent arg0) {
		// TODO Auto-generated method stub
		pressed.add(arg0.getKeyCode());
		if (pressed.size() > 2) {
			if (pressed.contains(NativeKeyEvent.VC_META) && pressed.contains(NativeKeyEvent.VC_SHIFT)
					&& pressed.contains(NativeKeyEvent.VC_1)) {
				copiar = true;
			} else if (pressed.contains(NativeKeyEvent.VC_META) && pressed.contains(NativeKeyEvent.VC_SHIFT)
					&& pressed.contains(NativeKeyEvent.VC_2)) {
				pegar = true;
			}
		}
	}

	/**
	 * Define el evento de dejar de pulsar una tecla del sistema, controlando si la
	 * combinación de estas es la indicada (Command + Shift + 1) o (Command + Shift + 2)
	 * para realizar las acciones de copiar y pegar.
	 * 
	 * @param arg0 {@link NativeKeyEvent} evento al dejar de pulsar una tecla.
	 */

	@Override
	public void nativeKeyReleased(NativeKeyEvent arg0) {
		// TODO Auto-generated method stub
		pressed.remove(arg0.getKeyCode());
		if (copiar && (!pressed.contains(NativeKeyEvent.VC_META) && !pressed.contains(NativeKeyEvent.VC_SHIFT)
				&& !pressed.contains(NativeKeyEvent.VC_1))) {
			copiar = false;
			boolean copiado = false;
			while (!copiado) {
				try {
					copiar();
					copiado = true;
				} catch (InterruptedException e) {
					;
				} catch (IllegalStateException e) {
					;
				}
			}
		}
		if (pegar && (!pressed.contains(NativeKeyEvent.VC_META) && !pressed.contains(NativeKeyEvent.VC_SHIFT)
				&& !pressed.contains(NativeKeyEvent.VC_2))) {
			pegar = false;
			pegarContenido();
		}
	}

	@Override
	public void nativeKeyTyped(NativeKeyEvent arg0) {
		// TODO Auto-generated method stub

	}

	/**
	 * Pulsa las teclas Ctrl + c para copiar {@link PulsarTeclas}, recoge los datos
	 * de texto, imagen o rutas de ficheros del portapapeles del sistema
	 * {@link Clip}, los muestra por {@link Ventana} y restaura el portapapeles al
	 * estado de antes de pulsar Ctrl + c. Con este metodo nos convertimos en
	 * servidor {@link BroadcastingIpControl}.
	 * 
	 * @throws InterruptedException si la conexion es detenida.
	 */

	public void copiar() throws InterruptedException {
		backupClip = (Transferable) clip.getContenidoClipboard();
		Object backupClipAux = clip.getContenidoEspecifico();
		PulsarTeclas.copiar();
		int cont = 0;
		Object contenidoSeleccion = clip.getContenidoEspecifico();
		while (cont < 10 && backupClip.equals(contenidoSeleccion)) {
			cont++;
			contenidoSeleccion = clip.getContenidoEspecifico();
		}
		if (contenidoSeleccion == null || backupClip.equals(clip.getContenidoClipboard())) {
			clip.tipoContenido = "";
			clip = new Clip();
		} else {
			try {
				String texto = clip.getString();
				Image imagen = clip.getImagen();
				List<?> rutas_ficheros = clip.getListaFicheros();
				if (!(texto == null) && !texto.equals(backupClipAux)) {
					ventana.controlBroadcast.serServidor(this);
					synchronized (this) {
						if (!ventana.controlBroadcast.soyServidor) {
							this.wait();
						}
					}

					ventana.display.asyncExec(new Runnable() {
						public void run() {
							ventana.lblBotonServidor.setImage(ventana.lblBotonServidorImage);
							ventana.lblHayServidor
									.setImage(SWTResourceManager.getImage(Ventana.class, "/imagenes/btn_verde.gif"));
						}
					});
					ventana.mostrarContenidoPorPantalla(new Contenido(texto));
					clip.setContenidoClipboard(backupClip);
				} else if (!(imagen == null) && !imagen.equals(backupClipAux)) {
					ventana.controlBroadcast.serServidor(this);
					synchronized (this) {
						if (!ventana.controlBroadcast.soyServidor) {
							this.wait();
						}
					}

					ventana.display.asyncExec(new Runnable() {
						public void run() {
							ventana.lblBotonServidor.setImage(ventana.lblBotonServidorImage);
							ventana.lblHayServidor
									.setImage(SWTResourceManager.getImage(Ventana.class, "/imagenes/btn_verde.gif"));
						}
					});
					ventana.mostrarContenidoPorPantalla(new Contenido(imagen));
					clip.setContenidoClipboard(backupClip);
				} else if (!(rutas_ficheros == null) && !rutas_ficheros.equals(backupClipAux)) {
					Ficheros ficheros = new Ficheros(rutas_ficheros);
					ventana.ficheros = new Ficheros(rutas_ficheros);
					ventana.controlBroadcast.serServidor(this);
					synchronized (this) {
						if (!ventana.controlBroadcast.soyServidor) {
							this.wait();
						}
					}

					ventana.display.asyncExec(new Runnable() {
						public void run() {
							ventana.lblBotonServidor.setImage(ventana.lblBotonServidorImage);
							ventana.lblHayServidor
									.setImage(SWTResourceManager.getImage(Ventana.class, "/imagenes/btn_verde.gif"));
						}
					});
					ventana.mostrarContenidoPorPantalla(new Contenido(ficheros));
					clip.setContenidoClipboard(backupClip);
				}
			} catch (UnsupportedFlavorException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * vuelca los datos de texto, imagen o ficheros recogidos por {@link Servidor}
	 * mostrados por {@link Ventana} en el clipboard del sistema {@link Clip}, pulsa
	 * las teclas Ctrl + v para pegar {@link PulsarTeclas} si los datos son de texto
	 * o imagen, o abre una ventana de clase {@link DirectoryDialog} si son ficheros
	 * y restaura el portapapeles al estado de antes de pulsar Ctrl + v.
	 */

	public void pegarContenido() {
		if (ventana.contenido != null) {
			final Transferable backupClip = (Transferable) clip.getContenidoClipboard();
			if (ventana.contenido.tipo != Tipo.Ficheros) {
				if (ventana.contenido.tipo == Tipo.Texto) {
					ventana.display.asyncExec(new Runnable() {
						public void run() {
							String texto = ventana.contenido.texto;
							DatoSeleccion seleccion = new DatoSeleccion(texto);
							clip.setContenidoClipboard(seleccion);
							PulsarTeclas.pegar();
							clip.setContenidoClipboard(backupClip);
						}
					});
				}
				if (ventana.contenido.tipo == Tipo.Imagen) {
					ventana.display.asyncExec(new Runnable() {
						public void run() {
							Image imagen = Serializar_funciones.ImageFromBytes(ventana.contenido.imagen_bytes);
							DatoSeleccion seleccion = new DatoSeleccion(imagen);
							Toolkit.getDefaultToolkit().getSystemClipboard().setContents(seleccion, seleccion);
							PulsarTeclas.pegar();
							clip.setContenidoClipboard(backupClip);
						}
					});
				}
			} else {
				ventana.display.asyncExec(new Runnable() {
					public void run() {
						DirectoryDialog dialog = new DirectoryDialog(ventana.shlSwt);
						String ruta = dialog.open();
						if (ruta != null) {
							red.compartirFicheros.Cliente cliente = new Cliente(ventana, ventana.controlBroadcast);
							cliente.ruta = ruta;
							cliente.start();
						}
					}
				});

			}

		}

	}

	/**
	 * vuelca en el portapapeles del sistema {@link Clip} los datos de texto/imagen
	 * dependiendo del {@link Contenido} mostrado por {@link Ventana}
	 */

	public void copiarContenidoBtn() {
		if (ventana.contenido != null) {

			if (ventana.contenido.tipo != Tipo.Ficheros) {
				if (ventana.contenido.tipo == Tipo.Texto) {
					ventana.display.asyncExec(new Runnable() {
						public void run() {
							String texto = ventana.contenido.texto;
							DatoSeleccion seleccion = new DatoSeleccion(texto);
							clip.setContenidoClipboard(seleccion);
						}
					});
				}
				if (ventana.contenido.tipo == Tipo.Imagen) {
					ventana.display.asyncExec(new Runnable() {
						public void run() {
							Image imagen = Serializar_funciones.ImageFromBytes(ventana.contenido.imagen_bytes);
							DatoSeleccion seleccion = new DatoSeleccion(imagen);
							Toolkit.getDefaultToolkit().getSystemClipboard().setContents(seleccion, seleccion);
						}
					});
				}
			}

		}

	}

	/**
	 * {@link Cliente} realiza una peticion al servidor {@link Servidor} y lo recibe
	 * en la ruta introducida en {@link DirectoryDialog}
	 * 
	 * @param numero entero con la posicion del {@link portapapeles.Contenido} a pedir.
	 */

	public void copiarContenidoFicheroBtn(final int numero) {
		if (ventana.contenido != null) {

			if (ventana.contenido.tipo == Tipo.Ficheros) {
				ventana.display.asyncExec(new Runnable() {
					public void run() {
						DirectoryDialog dialog = new DirectoryDialog(ventana.shlSwt);
						String ruta = dialog.open();
						if (ruta != null) {
							red.compartirFicheros.Cliente cliente = new Cliente(ventana, ventana.controlBroadcast,
									numero);
							cliente.ruta = ruta;
							cliente.start();
						}
					}
				});
			}

		}

	}

}
