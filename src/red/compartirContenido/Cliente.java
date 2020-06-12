package red.compartirContenido;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import portapapeles.Contenido;
import red.Serializar_funciones;
import red.broadcast.BroadcastingIp;
import red.broadcast.BroadcastingIpControl;
import ventana.Ventana;

/**
 * Clase que extiende de {@link Thread}, hace uso de metodos
 * {@link BroadcastingIpControl} para saber cual es la direccion a conectar
 * utilizando {@link Socket}, obtener {@link Contenido} del {@link Servidor} y
 * mostrarlo por {@link Ventana}.
 * 
 * @author: Pavon
 * @version: 17/05/2020
 * @since 1.0
 */

public class Cliente extends Thread {

	public static boolean conectado = false;
	public static Socket sc;
	Ventana ventana;
	BroadcastingIpControl controlBroadcasting;

	/**
	 * Constructor para tener acceso a {@link Ventana} y
	 * {@link BroadcastingIpControl}.
	 * 
	 * @param ventana             {@link Ventana} para mostrar {@link Contenido}
	 *                            obtenido del {@link Servidor}
	 * @param controlBroadcasting {@link BroadcastingIpControl} obtener direccion y
	 *                            conectar con {@link Servidor} por medio de
	 *                            {@link Socket}
	 */

	public Cliente(Ventana ventana, BroadcastingIpControl controlBroadcasting) {
		this.ventana = ventana;
		this.controlBroadcasting = controlBroadcasting;
	}

	/**
	 * Inicia hilo para conectar con {@link Servidor}, obtener {@link Contenido} del
	 * {@link Servidor} y mostrarlo por {@link Ventana}.
	 */

	@Override
	public void run() {
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		// Puerto del servidor
		final int PUERTO = 5000;
		while (controlBroadcasting.seguir) {
			System.out.print("");
			if (!controlBroadcasting.serServidor && !controlBroadcasting.noSerServidor
					&& !controlBroadcasting.hayServidorEnLista && !controlBroadcasting.soyServidor) {
				// cambia el indicador inferior derecho a gris ya que no hay servidor
				ventana.cambiarbtnHayServidor(false);
			}
			// si hay servidor en la red
			if (!controlBroadcasting.serServidor && !controlBroadcasting.noSerServidor
					&& controlBroadcasting.hayServidorEnLista && !controlBroadcasting.soyServidor) {
				// Creo el socket para conectarme con el cliente
				boolean estaConectado = true;
				for (BroadcastingIp broadcastingIp : controlBroadcasting.lista_ip_servidor) {
					estaConectado = true;
					try {
						conectado = false;
						sc = new Socket();
						InetSocketAddress direccion = new InetSocketAddress(broadcastingIp.getIp(), PUERTO);
						sc.connect(direccion, 1000);
						sc.setSoTimeout(1000);
					} catch (Exception e) {
						estaConectado = false;
					}
					if (estaConectado) {
						conectado = true;
						break;
					}
				}
				if (conectado) {
					ventana.cambiarbtnHayServidor(conectado);
					try {

						// envio mensaje a la lista de servidores

						// Recibo el mensaje del servidor
						DataInputStream dIn = new DataInputStream(sc.getInputStream());

						int length = dIn.readInt(); // read length of incoming message
						if (length > 0) {
							byte[] message = new byte[length];
							dIn.readFully(message, 0, message.length); // read the message
							Contenido contenido = (Contenido) Serializar_funciones.convertirAObjeto(message);
							ventana.mostrarContenidoPorPantalla(contenido);
						}
						dIn.close();

					} catch (IOException ex) {
						;
					}

				} else {
					// el servidor ha cerrado la aplicacion
					conectado = false;
					ventana.cambiarbtnHayServidor(conectado);
				}

			}
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		try {
			if (sc != null) {
				sc.close();
			}
		} catch (IOException ex) {
			Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

}