package portapapeles;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;

import red.Serializar_funciones;
import ventana.Ventana;

/**
 * Clase que contiene los datos para mostrar por {@link Ventana}, implementa la
 * clase {@link Serializable}, se envia haciendo uso de la clase {@link Socket}
 * por la clase {@link red.compartirFicheros.Servidor} y se recibe por la clase
 * {@link red.compartirFicheros.Cliente}
 * 
 * @author: Pavon
 * @version: 10/05/2020
 * @since 1.0
 */

public class Contenido implements Serializable {

	private static final long serialVersionUID = 1L;

	public enum Tipo {
		Texto, Ficheros, Imagen
	}

	public Tipo tipo;
	public String texto = null;
	public byte[] imagen_bytes = null;
	public int imagen_ancho = 0;
	public int imagen_alto = 0;
	public ArrayList<String[]> listaFicheros = null;
	public int id = 0;

	/**
	 * Constructor para definir las variables:
	 * <ul>
	 * <li>tipo: categoriza el contenido de esta clase</li>
	 * <li>id: int que hace uso del metodo random() de {@link Math}</li>
	 * </ul>
	 */

	Contenido(Tipo tipo) {
		this.tipo = tipo;
		this.id = (int) ((99999999 * Math.random() + 1));
	}

	/**
	 * Constructor para definir las variables:
	 * <ul>
	 * <li>tipo: categoriza el contenido de esta clase</li>
	 * <li>texto: {@link String} texto contenido</li>
	 * <li>id: int que hace uso del metodo random() de {@link Math}</li>
	 * </ul>
	 * 
	 * @param texto {@link String} a contener.
	 */

	public Contenido(String texto) {
		this.tipo = Tipo.Texto;
		this.texto = texto;
		this.id = (int) ((99999999 * Math.random() + 1));
	}

	/**
	 * Constructor para definir las variables:
	 * <ul>
	 * <li>tipo: categoriza el contenido de esta clase</li>
	 * <li>imagen_bytes: {@link BufferedImage} bytes de la imagen pasada por
	 * parametros</li>
	 * <li>imagen_ancho: numero del ancho de la imagen pasada por parametros</li>
	 * <li>imagen_alto: numero del alto de la imagen pasada por parametros</li>
	 * <li>id: int que hace uso del metodo random() de {@link Math}</li>
	 * </ul>
	 * 
	 * @param imagen {@link BufferedImage} a contener.
	 */

	public Contenido(Image imagen) {
		this.tipo = Tipo.Imagen;
		this.imagen_bytes = Serializar_funciones.imageToByte(imagen);
		imagen_ancho = imagen.getWidth(null);
		imagen_alto = imagen.getHeight(null);
		this.id = (int) ((99999999 * Math.random() + 1));
	}

	/**
	 * Constructor para definir las variables:
	 * <ul>
	 * <li>tipo: categoriza el contenido de esta clase</li>
	 * <li>listaFicheros: {@link ArrayList} de arrays de {@link String}
	 * ficheros</li>
	 * <li>id: int que hace uso del metodo random() de {@link Math}</li>
	 * </ul>
	 * 
	 * @param ficheros {@link portapapeles.Ficheros} a obtener lista de ficheros.
	 */

	public Contenido(Ficheros ficheros) {
		this.tipo = Tipo.Ficheros;
		this.listaFicheros = ficheros.ficherosToContenido();
		this.id = (int) ((99999999 * Math.random() + 1));
	}

	/**
	 * Compara el id propio y el de la clase pasada por parametros
	 * 
	 * @param contenido {@link portapapeles.Contenido} a comparar.
	 * @return <code>true</code> tienen el mismo id. <code>false</code> no tienen el
	 *                          mismo id.
	 */

	public boolean equals(Contenido contenido) {
		if (contenido == null) {
			return false;
		}
		if (this.id == contenido.id) {
			return true;
		}
		return false;
	}

}
