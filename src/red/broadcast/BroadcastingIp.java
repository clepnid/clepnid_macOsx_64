package red.broadcast;

import java.io.Serializable;

/**
 * Clase que define la ip de un equipo y si es servidor.
 * 
 * @author: Pavon
 * @version: 17/05/2020
 * @since 1.0
 */

public class BroadcastingIp implements Serializable {

	private static final long serialVersionUID = 1L;
	private String ip;
	private boolean servidor;

	/**
	 * Constructor que define las variables de la clase.
	 * 
	 * @param ip       {@link String} ip del equipo.
	 * @param servidor <code>true</code> la ip es la del servidor,
	 *                 <code>false</code> la ip no es la del servidor.
	 */

	public BroadcastingIp(String ip, boolean servidor) {
		this.ip = ip;
		this.servidor = servidor;
	}

	/**
	 * @return {@link String} ip del equipo.
	 */

	public String getIp() {
		return ip;
	}

	/**
	 * define la ip del equipo.
	 * 
	 * @param ip {@link String} ip del equipo.
	 */

	public void setIp(String ip) {
		this.ip = ip;
	}

	/**
	 * @return <code>true</code> la ip es la del servidor, <code>false</code> la ip
	 *         no es la del servidor.
	 */

	public boolean isServidor() {
		return servidor;
	}

	/**
	 * define si es servidor.
	 * 
	 * @param servidor <code>true</code> la ip es la del servidor,
	 *                 <code>false</code> la ip no es la del servidor.
	 */

	public void setServidor(boolean servidor) {
		this.servidor = servidor;
	}

	/**
	 * @return {@link String} ej.: "192.168.0.0, false"
	 */

	@Override
	public String toString() {
		return ip.toString() + "," + servidor;
	}

}
