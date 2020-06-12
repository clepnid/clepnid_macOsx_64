package red.broadcast;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import red.compartirContenido.Cliente;
import red.compartirContenido.Servidor;
import teclado.EventoTeclasGlobal;
import ventana.Ventana;

/**
 * Clase con metodos para obtener las ips de los equipos conectados en la misma
 * red y dar acceso a un único servidor.
 * 
 * @author: Pavon
 * @version: 17/05/2020
 * @since 1.0
 */

public class BroadcastingIpControl {

	public static final int PUERTO_MENSAJES = 55557;
	public CopyOnWriteArrayList<BroadcastingIp> lista_ips;
	public CopyOnWriteArrayList<String> lista_ip_propia;
	public CopyOnWriteArrayList<String> lista_ip_broadcast_propia;
	public CopyOnWriteArrayList<BroadcastingIp> lista_ip_servidor;
	public boolean soyServidor;
	public boolean hayServidorEnLista;
	public boolean seguir = true;
	public boolean serServidor = false;
	public boolean noSerServidor = false;
	public DatagramSocket socket;
	public RefrescarInformacion refrescar;
	public Enviar enviar;
	public Recibir recibir;
	public red.compartirContenido.Cliente clienteCompartirContenido;
	public red.compartirContenido.Servidor servidorCompartirContenido;
	public red.compartirFicheros.Servidor servidorCompartirFicheros;
	public Ventana ventana;

	/**
	 * Constructor inicia las variables e inicia los hilos
	 * {@link RefrescarInformacion}, {@link Enviar} y {@link Recibir}
	 */

	public BroadcastingIpControl() {
		this.lista_ip_propia = new CopyOnWriteArrayList<String>();
		this.lista_ip_broadcast_propia = new CopyOnWriteArrayList<String>();
		this.lista_ips = new CopyOnWriteArrayList<BroadcastingIp>();
		this.lista_ip_servidor = new CopyOnWriteArrayList<BroadcastingIp>();
		this.soyServidor = false;
		this.hayServidorEnLista = false;
		try {
			socket = new DatagramSocket(PUERTO_MENSAJES);
			socket.setSoTimeout(200);
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		iniciarInterfaces();
		refrescar = new RefrescarInformacion(this);
		recibir = new Recibir(this);
		enviar = new Enviar(this);
		refrescar.start();
		recibir.start();
		enviar.start();

	}

	/**
	 * Da acceso a {@link Ventana} e inicia los hilos {@link Cliente},
	 * {@link Servidor} y {@link red.compartirFicheros.Servidor}
	 * 
	 * @param ventana {@link Ventana}
	 */

	public void compartirLista(Ventana ventana) {
		this.ventana = ventana;
		clienteCompartirContenido = new red.compartirContenido.Cliente(ventana, this);
		clienteCompartirContenido.start();
		servidorCompartirContenido = new red.compartirContenido.Servidor(ventana, this);
		servidorCompartirContenido.start();
		servidorCompartirFicheros = new red.compartirFicheros.Servidor(ventana, this);
		servidorCompartirFicheros.start();
	}

	/**
	 * Deja de ser servidor y apaga los hilos del paquete {@link red}
	 */

	public void close() {
		apagarServidor();
		seguir = false;
		socket.close();
		if (servidorCompartirFicheros.servidor != null) {
			try {
				servidorCompartirFicheros.servidor.close();
			} catch (IOException e) {
				;
			}
		}
		if (servidorCompartirContenido.servidor != null) {
			try {
				servidorCompartirContenido.servidor.close();
			} catch (IOException e) {
				;
			}
		}
	}

	/**
	 * Iniciar hilo para convertirse en servidor.
	 */

	public void serServidor() {

		if (!soyServidor) {
			Thread hiloSerServidor = new Thread(new Runnable() {
				public void run() {
					serServidor = true;
					// durante el tiempo en el que este hilo duerme, el hilo Enviar manda esta
					// direccion ip como servidor y el hilo Recibir queda deshabilitado durante este
					// tiempo para no recoger ningun otro servidor y quitarle a esta ip el puesto de
					// servidor.
					try {
						Thread.sleep(300);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					serServidor = false;
				}
			});
			hiloSerServidor.start();
		}

	}

	/**
	 * Iniciar hilo para convertirse en servidor.
	 * 
	 * @param eventoTeclasGlobal {@link EventoTeclasGlobal} espera a que el nuevo
	 *                           hilo finalice para seguir ejecutandose.
	 */

	public void serServidor(EventoTeclasGlobal eventoTeclasGlobal) {
		if (!soyServidor) {
			Thread hiloSerServidor = new Thread(new Runnable() {
				public void run() {
					serServidor = true;
					synchronized (eventoTeclasGlobal) {
						eventoTeclasGlobal.notify();
					}
					// durante el tiempo en el que este hilo duerme, el hilo Enviar manda esta
					// direccion ip como servidor y el hilo Recibir queda deshabilitado durante este
					// tiempo para no recoger ningun otro servidor y quitarle a esta ip el puesto de
					// servidor.
					try {
						Thread.sleep(300);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					serServidor = false;
				}
			});
			hiloSerServidor.start();
		} else {
			synchronized (eventoTeclasGlobal) {
				eventoTeclasGlobal.notify();
			}
		}

	}

	/**
	 * Iniciar hilo para dejar de ser servidor.
	 */

	public void dejarDeSerServidor() {

		Thread hiloDejarSerServidor = new Thread(new Runnable() {
			public void run() {
				if (soyServidor) {
					noSerServidor = true;
					// durante el tiempo en el que este hilo duerme, el hilo Enviar manda esta
					// direccion ip como cliente y el hilo Recibir queda deshabilitado durante este
					// tiempo para no recoger esta direccion como servidor.
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					noSerServidor = false;
				}
			}
		});
		hiloDejarSerServidor.start();
	}

	/**
	 * Dejar de ser servidor.
	 */

	public void apagarServidor() {
		if (soyServidor) {
			noSerServidor = true;
			// durante el tiempo en el que este hilo duerme, el hilo Enviar manda esta
			// direccion ip como cliente y el hilo Recibir queda deshabilitado durante este
			// tiempo para no recoger esta direccion como servidor.
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			noSerServidor = false;
		}
	}

	/**
	 * Metodo principal, controla la introduccion de una ip en la lista y el cambio
	 * de esta al introducirla.
	 * 
	 * @param ip_boolean <code>true</code> si la ip es servidor, <code>false</code> si la ip no
	 *                          es servidor.
	 */

	public void control_Introduccion_ips(BroadcastingIp ip_boolean) {
		boolean esMiIp = esMiIp(ip_boolean.getIp());
		controlRepeticiones(ip_boolean);
		definirServidorEnLista(esMiIp, ip_boolean);
		controlListaServidor();
		soyServidor = soyServidor();
		hayServidorEnLista = hayServidoresEnLista();
	}

	/**
	 * Controla los cambio de ips que son servidor a no serlo y viceversa en la
	 * lista.
	 */

	private void definirServidorEnLista(boolean esMiIp, BroadcastingIp ip_boolean) {
		if (ip_boolean.isServidor()) {
			if (esMiIp) {
				for (BroadcastingIp ip : lista_ips) {
					if (esMiIp(ip.getIp())) {
						ip.setServidor(true);
					} else {
						ip.setServidor(false);
					}
				}
			} else {
				for (BroadcastingIp ip : lista_ips) {
					if (ip.getIp().equals(ip_boolean.getIp())) {
						ip.setServidor(true);
					}
				}
			}
		} else {
			if (esMiIp) {
				for (BroadcastingIp ip : lista_ips) {
					if (esMiIp(ip.getIp())) {
						ip.setServidor(false);
					}
				}
			} else {
				for (BroadcastingIp ip : lista_ips) {
					if (ip.getIp().equals(ip_boolean.getIp())) {
						ip.setServidor(false);
					}
				}
			}
		}
	}

	/**
	 * Vacia la lista servidor y la llena con ips que sean servidores de la lista de
	 * ips
	 */

	public void controlListaServidor() {
		lista_ip_servidor.clear();
		for (BroadcastingIp ip : lista_ips) {
			if (ip.isServidor()) {
				lista_ip_servidor.add(ip);
			}
		}
	}

	/**
	 * Comprueba si la cadena con la ip esta en la lista de ips propias del equipo
	 * 
	 * @param ip {@link String} con la ip de un equipo.
	 * @return <code>true</code> si la ip pasada por parametros es mi ip,
	 *         <code>false</code> si la ip pasada por parametros no es mi ip
	 */

	public boolean esMiIp(String ip) {
		for (String string : lista_ip_propia) {
			if (string.equals(ip)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Comprueba si la cadena con la direccion broadcast esta en la lista de ips
	 * broadcast propias del equipo
	 * 
	 * @param ip {@link String} con la ip de un equipo.
	 * @return <code>true</code> si la ip pasada por parametros es una de mis
	 *         direcciones de broadcast, <code>false</code> si la ip pasada por
	 *         parametros no es una de mis direcciones de broadcast
	 */

	private boolean esMiBroadcastIp(String ip) {
		for (String string : lista_ip_broadcast_propia) {
			if (string.equals(ip)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @return <code>true</code> si todas las ips del propio equipo corresponde con
	 *         las del servidor, <code>false</code> si las ips del servidor no son
	 *         todas las del propio equipo.
	 */

	//
	boolean soyServidor() {
		if (lista_ip_servidor.isEmpty()) {
			return false;
		}
		for (BroadcastingIp ip : lista_ip_servidor) {
			if (!esMiIp(ip.getIp())) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Controla las repeticiones en la lista de ips.
	 * 
	 * @param ip_boolean {@link String} direccion ip a introducir en lista si no lo
	 *                   está.
	 */

	private void controlRepeticiones(BroadcastingIp ip_boolean) {
		boolean ipRepetida = false;
		for (BroadcastingIp ip : lista_ips) {
			if (ip_boolean.getIp().equals(ip.getIp())) {
				ipRepetida = true;
			}
		}
		if (!ipRepetida) {
			lista_ips.add(ip_boolean);
		}
	}

	/**
	 * Indica si hay servidores en la lista servidores.
	 * 
	 * @return <code>true</code> si la lista servidores no esta vacía,
	 *         <code>false</code> si la lista servidores esta vacía.
	 */

	boolean hayServidoresEnLista() {
		if (lista_ip_servidor.isEmpty()) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * De las las direcciones ipv4 de las interfaces se obtienen las ips y las ips
	 * broadcast del equipo y se actualizan las listas de ips y ips de bradcast.
	 * 
	 * @param lista_ips_propia_AUX    {@link CopyOnWriteArrayList} de {@link String}
	 *                                lista ips
	 * @param lista_ips_broadcast_AUX {@link CopyOnWriteArrayList} de {@link String}
	 *                                lista ips de broadcast.
	 */

	void refrescarInterfaces(CopyOnWriteArrayList<String> lista_ips_propia_AUX,
			CopyOnWriteArrayList<String> lista_ips_broadcast_AUX) {
		try {
			// estas variables listas se usan para no sobrescribir directamente sobre las
			// variables de listas ips y de broadcast, dando posibles errores en el
			// contenido de estas en el caso de sobrescribirlas directamente.
			lista_ips_propia_AUX.clear();
			lista_ips_broadcast_AUX.clear();
			Enumeration<NetworkInterface> e = NetworkInterface.getNetworkInterfaces();

			while (e.hasMoreElements()) {
				NetworkInterface ni = e.nextElement();

				List<InterfaceAddress> e2 = ni.getInterfaceAddresses();

				for (int i = 0; i < e2.size(); i++) {
					InetAddress ip = e2.get(i).getAddress();
					if (ip instanceof Inet4Address && !ip.getHostAddress().toString().equals("127.0.0.1")) {
						lista_ips_propia_AUX.add(ip.getHostAddress());
						lista_ips_broadcast_AUX.add(e2.get(i).getBroadcast().getHostAddress());
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		// si en la auxiliar hay una ip que no aparece en la principal se anyade a la
		// principal
		for (String string : lista_ips_broadcast_AUX) {
			if (!esMiBroadcastIp(string)) {
				lista_ip_broadcast_propia.add(string);
			}
		}

		// si en la auxiliar hay una ip que no aparece en la principal se anyade a la
		// principal
		for (String string : lista_ips_propia_AUX) {
			if (!esMiBroadcastIp(string)) {
				lista_ip_propia.add(string);
			}
		}

		// si la lista principla contiene una ip que no aparece en la auxiliar se
		// borrará de la principal
		for (String string : lista_ip_broadcast_propia) {
			boolean esta = false;
			for (String string2 : lista_ips_broadcast_AUX) {
				if (string.equals(string2)) {
					esta = true;
					break;
				}
			}
			if (!esta) {
				lista_ip_broadcast_propia.remove(string);
			}
		}

		// si la lista principal contiene una ip que no aparece en la auxiliar se
		// borrará de la principal y se pondra en false en la lista_ips
		for (String string : lista_ip_propia) {
			boolean esta = false;
			for (String string2 : lista_ips_propia_AUX) {
				if (string.equals(string2)) {
					esta = true;
					break;
				}
			}
			if (!esta) {
				lista_ip_propia.remove(string);
				for (BroadcastingIp broadcastingIp : lista_ips) {
					if (broadcastingIp.equals(new BroadcastingIp(string, soyServidor))) {
						broadcastingIp.setServidor(false);
					}
				}
			}
		}

		// actualizar lista_ips con las ips_propias

		for (String ip : lista_ip_propia) {
			boolean estaEnLista = false;
			for (BroadcastingIp objeto : lista_ips) {
				if (objeto.getIp().equals(ip)) {
					estaEnLista = true;
					break;
				}
			}

			if (!estaEnLista) {
				lista_ips.add(new BroadcastingIp(ip, soyServidor));
			}
		}

	}

	/**
	 * De las las direcciones ipv4 de las interfaces se obtienen las ips y las ips
	 * broadcast del equipo y se actualizan las listas de ips y ips de broadcast.
	 */
	private void iniciarInterfaces() {
		try {
			lista_ip_propia.clear();
			lista_ip_broadcast_propia.clear();
			Enumeration<NetworkInterface> e = NetworkInterface.getNetworkInterfaces();

			while (e.hasMoreElements()) {
				NetworkInterface ni = e.nextElement();

				List<InterfaceAddress> e2 = ni.getInterfaceAddresses();

				for (int i = 0; i < e2.size(); i++) {
					// de cada interfaz de red del equipo se recoge la ip y la direccion broadcast
					InetAddress ip = e2.get(i).getAddress();
					if (ip instanceof Inet4Address && !ip.getHostAddress().toString().equals("127.0.0.1")) {
						lista_ip_propia.add(ip.getHostAddress());
						lista_ip_broadcast_propia.add(e2.get(i).getBroadcast().getHostAddress());
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		// se renuevan las listas de direcciones propias del equipo
		for (String ip : lista_ip_propia) {
			boolean estaEnLista = false;
			for (BroadcastingIp objeto : lista_ips) {
				if (objeto.getIp().equals(ip)) {
					estaEnLista = true;
					break;
				}
			}

			if (!estaEnLista) {
				lista_ips.add(new BroadcastingIp(ip, soyServidor));
			}
		}
	}
}
