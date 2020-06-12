package red.broadcast;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.UnknownHostException;

import red.Serializar_funciones;

public class Enviar extends Thread {

	BroadcastingIpControl controlBroadcasting;

	public Enviar(BroadcastingIpControl controlBroadcasting) {
		this.controlBroadcasting = controlBroadcasting;
	}

	public void run() {

		while (controlBroadcasting.seguir) {

			try {

				if (controlBroadcasting.serServidor) {
					// pasar de cliente a servidor.
					while (!controlBroadcasting.soyServidor) {
						for (BroadcastingIp broadcastingIp : controlBroadcasting.lista_ips) {
							if (controlBroadcasting.esMiIp(broadcastingIp.getIp())) {
								broadcastingIp.setServidor(true);
							} else {
								broadcastingIp.setServidor(false);
							}
						}
						controlBroadcasting.controlListaServidor();
						controlBroadcasting.soyServidor = controlBroadcasting.soyServidor();
						controlBroadcasting.hayServidorEnLista = controlBroadcasting.hayServidoresEnLista();
					}

				} else if (controlBroadcasting.noSerServidor) {
					// pasar de servidor a cliente.
					while (controlBroadcasting.soyServidor) {
						for (BroadcastingIp broadcastingIp : controlBroadcasting.lista_ips) {
							if (controlBroadcasting.esMiIp(broadcastingIp.getIp())) {
								broadcastingIp.setServidor(false);
							}
						}
						controlBroadcasting.controlListaServidor();
						controlBroadcasting.soyServidor = controlBroadcasting.soyServidor();
						controlBroadcasting.hayServidorEnLista = controlBroadcasting.hayServidoresEnLista();
					}

				}

				// obtiene los bytes del objeto serializandolo
				byte[] dato = Serializar_funciones.convertirAByteArray(controlBroadcasting.lista_ips);

				// manda la lista de ips serializadas.
				for (String broadcast : controlBroadcasting.lista_ip_broadcast_propia) {

					DatagramPacket dgp;
					dgp = new DatagramPacket(dato, dato.length, InetAddress.getByName(broadcast),
							BroadcastingIpControl.PUERTO_MENSAJES);
					controlBroadcasting.socket.send(dgp);

				}

				Thread.sleep(100);

			} catch (UnknownHostException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				;
			}

		}
	}

}
