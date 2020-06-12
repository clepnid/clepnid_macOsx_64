package red.broadcast;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.UnknownHostException;
import java.util.concurrent.CopyOnWriteArrayList;

import red.Serializar_funciones;

public class Recibir extends Thread {

	BroadcastingIpControl controlBroadcasting;

	public Recibir(BroadcastingIpControl controlBroadcasting) {
		this.controlBroadcasting = controlBroadcasting;
	}

	public void run() {

		while (controlBroadcasting.seguir) {

			try {
				// Un array de bytes lo suficientemente grande para contener
				// cualquier dato que podamos recibir.
				byte[] dato = new byte[1024];

				DatagramPacket peticion = new DatagramPacket(dato, dato.length);
				// La llamada se queda bloqueada hasta que recibamos algún mensaje
				controlBroadcasting.socket.receive(peticion);

				@SuppressWarnings("unchecked")
				CopyOnWriteArrayList<BroadcastingIp> ipsRecibidas = (CopyOnWriteArrayList<BroadcastingIp>) Serializar_funciones
						.convertirAObjeto(peticion.getData());

				// controla la introduccion de ips
				for (BroadcastingIp ip : ipsRecibidas) {
					controlBroadcasting.control_Introduccion_ips(ip);
				}

			} catch (UnknownHostException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e) {

			}

		}

	}

}
