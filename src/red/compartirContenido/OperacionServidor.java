package red.compartirContenido;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import portapapeles.Contenido;
import red.Serializar_funciones;
import ventana.Ventana;

/**
 * Clase que extiende de {@link Thread}, utiliza {@link Socket} para mandar
 * {@link Contenido} del {@link Servidor} y mostrarlo por {@link Ventana} del
 * {@link Cliente}.
 * 
 * @author: Pavon
 * @version: 17/05/2020
 * @since 1.0
 */

public class OperacionServidor extends Thread {

	public boolean conectado = true;
	Socket sc = null;
	DataOutputStream out;
	Servidor padre;
	ServerSocket servidor = null;

	/**
	 * Inicia las variables {@link ServerSocket} del {@link Servidor} y
	 * {@link Servidor} para obtener {@link Ventana}.
	 * 
	 * @param servidor {@link ServerSocket} servidor que acepta conexiones de
	 *                 {@link Cliente}
	 * @param padre    {@link Servidor} para tener acceso a la variable
	 *                 {@link Ventana} y obtener {@link Contenido} para mandar a
	 *                 {@link Cliente}.
	 */

	public OperacionServidor(ServerSocket servidor, Servidor padre) {
		this.servidor = servidor;
		this.padre = padre;
	}

	/**
	 * Inicia el hilo para aceptar la conexión con {@link Cliente} y mandar
	 * {@link Contenido}.
	 */

	public void run() {
		try {
			// acepta la conexion con el cliente y le manda el contenido.
			sc = servidor.accept();
			byte[] message = Serializar_funciones.convertirAByteArray(padre.ventana.contenido);
			DataOutputStream dOut = new DataOutputStream(sc.getOutputStream());
			dOut.writeInt(message.length);
			dOut.write(message);
			dOut.close();
			sc.close();
			conectado = false;
		} catch (IOException e) {
			conectado = false;
		}

	}
}
