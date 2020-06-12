package portapapeles;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

/**
 * Contiene metodos para manejar el portapapeles del sistema {@link Clipboard}
 * 
 * @author: Pavon
 * @version: 22/04/2020
 * @since 1.0
 * @see <a href =
 *      "https://www.developer.com/java/data/how-to-code-java-clipboard-functionality.html"
 *      > Funcionalidad del portapapeles de Java. </a>
 */

public class Clip {
	DataFlavor dataFlavorStringJava;
	DataFlavor dataFlavorBitmapJava;
	DataFlavor dataFlavorFileJava;
	Clipboard clipboard;
	public String tipoContenido;

	/**
	 * Constructor, define las variables:
	 * 
	 * <ul>
	 * <li>clipboard: {@link Clipboard} controlador del portapapeles del sistema
	 * </li>
	 * <li>tipoContenido: {@link String} <code>"texto"</code>,
	 * <code>"imagen"</code>, <code>"ficheros"</code></li>
	 * <li>dataFlavorStringJava: {@link DataFlavor} mimetype texto</li>
	 * <li>dataFlavorBitmapJava: {@link DataFlavor} mimetype imagen</li>
	 * <li>dataFlavorFileJava: {@link DataFlavor} mimetype lista ficheros</li>
	 * </ul>
	 */

	public Clip() {
		clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		tipoContenido = "";
		try {
			dataFlavorStringJava = new DataFlavor(
					"application/x-java-serialized-object; class=java.lang.String");
			dataFlavorBitmapJava = new DataFlavor(
					"image/x-java-image; class=java.awt.Image");
			dataFlavorFileJava = new DataFlavor(
					"application/x-java-file-list; class=java.util.List");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * Obtener un objeto texto: {@link String}, imagen: {@link BufferedImage} o
	 * lista de ficheros: {@link List} del portapapeles del sistema.
	 * 
	 * @return Un objeto del portapapeles texto, imagen, lista de ficheros o si
	 *         no contiene ninguno: <code>null</code>.
	 * 
	 */

	public Object getContenidoEspecifico() {
		Transferable t = clipboard.getContents(null);
		try {
			if (t.isDataFlavorSupported(dataFlavorFileJava)) {
				List<?> lista;
				lista = (List<?>) t.getTransferData(dataFlavorFileJava);
				return lista;

			} else if (t.isDataFlavorSupported(dataFlavorBitmapJava)) {
				return (Image) t.getTransferData(DataFlavor.imageFlavor);

			} else if (t.isDataFlavorSupported(dataFlavorStringJava)) {
				String texto;
				texto = (String) t.getTransferData(dataFlavorStringJava);
				return texto;

			} else {
				return null;
			}

		} catch (UnsupportedFlavorException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;

	}

	/**
	 * Introduce un objeto {@link Transferable} dentro del clipboard del
	 * sistema.
	 * 
	 * @param objeto
	 *            El objeto a introducir.
	 */

	public void setContenidoClipboard(Transferable objeto) {
		try {
			clipboard.setContents(objeto, null);
		} catch (Exception e) {
			;
		}
	}

	/**
	 * Introduce un objeto {@link DatoSeleccion} dentro del clipboard del
	 * sistema.
	 * 
	 * @param objeto
	 *            El objeto a introducir.
	 */

	public void setContenidoClipboard(DatoSeleccion objeto) {
		clipboard.setContents(objeto, objeto);
	}

	/**
	 * Obtiene los datos de todos los objetos contenidos en el portapapeles del
	 * sistema.
	 * 
	 * @return Contenido del clipboard sin filtrar, es decir se devolvera un
	 *         objeto con toda la información que contiene el portapapeles del
	 *         sistema.
	 */

	public Object getContenidoClipboard() {
		return clipboard.getContents(null);
	}

	/**
	 * Segun el portapapeles del sistema contenga texto: {@link String}, imagen:
	 * {@link BufferedImage} o lista de ficheros: {@link List}, la variable
	 * tipoContenido contendra diferente {@link String} <code>"texto"</code>,
	 * <code>"imagen"</code>, <code>"ficheros"</code>,
	 * 
	 */

	private void tipoDeContenidoPortapapeles() {
		Transferable t = clipboard.getContents(null);
		if (t.isDataFlavorSupported(dataFlavorFileJava)) {
			tipoContenido = "ficheros";

		} else if (t.isDataFlavorSupported(dataFlavorBitmapJava)) {
			tipoContenido = "imagen";

		} else if (t.isDataFlavorSupported(dataFlavorStringJava)) {
			tipoContenido = "texto";

		} else {
			tipoContenido = "";
		}
	}

	/**
	 * Obtener texto {@link String} del portapapeles del sistema.
	 * 
	 * @return texto del portapapeles o si no contiene texto: <code>null</code>.
	 * @throws UnsupportedFlavorException si no es un {@link DataFlavor} válido.
	 * @throws IOException si no se puede recoger contenido.
	 */

	public String getString() throws UnsupportedFlavorException, IOException {
		tipoDeContenidoPortapapeles();
		if (tipoContenido.equals("texto")) {
			Transferable t = clipboard.getContents(null);
			String texto = (String) t.getTransferData(dataFlavorStringJava);
			return texto;
		} else {
			return null;
		}
	}

	/**
	 * Obtener imagen {@link BufferedImage} del portapapeles del sistema.
	 * 
	 * @return imagen del portapapeles o si no contiene imagen:
	 *         <code>null</code>.
	 * @throws UnsupportedFlavorException si no es un {@link DataFlavor} válido.
	 * @throws IOException si no se puede recoger contenido.
	 */

	public Image getImagen() throws UnsupportedFlavorException,
			IOException {
		tipoDeContenidoPortapapeles();
		if (tipoContenido.equals("imagen")) {
			Transferable t = clipboard.getContents(null);
			Image imagen;
			imagen = (Image) t.getTransferData(dataFlavorBitmapJava);
			return imagen;
		} else {
			return null;
		}

	}

	/**
	 * Obtener lista de ficheros {@link List} del portapapeles del sistema.
	 * 
	 * @return lista de ficheros del portapapeles o si no contiene ninguna lista
	 *         de ficheros: <code>null</code>.
	 * @throws UnsupportedFlavorException si no es un {@link DataFlavor} válido.
	 * @throws IOException si no se puede recoger contenido.
	 */

	public List<?> getListaFicheros() throws UnsupportedFlavorException,
			IOException {
		tipoDeContenidoPortapapeles();
		if (tipoContenido.equals("ficheros")) {
			Transferable t = clipboard.getContents(null);
			List<?> lista;
			lista = (List<?>) t.getTransferData(dataFlavorFileJava);
			return lista;
		} else {
			return null;
		}
	}
}
