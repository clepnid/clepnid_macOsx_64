package red.broadcast;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Clase que extiende de {@link Thread}, hace uso de la funcion
 * {@link BroadcastingIpControl} refrescarInterfaces() para refrescar las listas
 * propias ips y ips broadcasts por si ha habido un cambio de red.
 * 
 * @author: Pavon
 * @version: 17/05/2020
 * @since 1.0
 */

public class RefrescarInformacion extends Thread {

	CopyOnWriteArrayList<String> lista_ips_propia_AUX;
	CopyOnWriteArrayList<String> lista_ip_broadcast_propia_AUX;
	BroadcastingIpControl controlBroadcasting;

	/**
	 * Constructor que da acceso a {@link BroadcastingIpControl} e inicia las
	 * variables de las listas
	 * 
	 * @param controlBroadcasting {@link BroadcastingIpControl}
	 */

	public RefrescarInformacion(BroadcastingIpControl controlBroadcasting) {
		this.controlBroadcasting = controlBroadcasting;
		lista_ips_propia_AUX = new CopyOnWriteArrayList<String>();
		lista_ip_broadcast_propia_AUX = new CopyOnWriteArrayList<String>();
	}

	/**
	 * Inicia el hilo para por segundo refrescar las listas de direcciones ips.
	 */

	public void run() {
		while (controlBroadcasting.seguir) {
			controlBroadcasting.refrescarInterfaces(lista_ips_propia_AUX, lista_ip_broadcast_propia_AUX);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
