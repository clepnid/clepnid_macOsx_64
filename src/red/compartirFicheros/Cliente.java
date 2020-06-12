package red.compartirFicheros;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import red.broadcast.BroadcastingIp;
import red.broadcast.BroadcastingIpControl;
import ventana.Ventana;

/**
 * Clase que extiende de {@link Thread}, realiza peticiones a {@link Servidor}
 * para obtener comprimido y crear ficheros en la ruta introducida.
 * 
 * @author: Pavon
 * @version: 17/05/2020
 * @since 1.0
 */

public class Cliente extends Thread {
	// Puerto del servidor
	public final int PUERTO = 5001;
	public static boolean conectado = false;
	public static Socket sc;
	public boolean copiarFicheros = false;
	public String ruta;
	public String host = "";
	Ventana ventana;
	DataOutputStream out = null;
	BroadcastingIpControl controlBroadcasting;
	public String one = "no";
	public int numero = 0;

	/**
	 * Inicia las variables
	 * 
	 * @param ventana             {@link Ventana} para manejar {@link ventana.BarraProgreso}
	 * @param controlBroadcasting {@link BroadcastingIpControl} del que se obtiene
	 *                            la direccion servidor.
	 */
	
	public Cliente(Ventana ventana,
			BroadcastingIpControl controlBroadcasting) {
		this.ventana = ventana;
		this.controlBroadcasting = controlBroadcasting;
	}
	

	/**
	 * Inicia las variables
	 * 
	 * @param ventana             {@link Ventana} para manejar {@link ventana.BarraProgreso}
	 * @param controlBroadcasting {@link BroadcastingIpControl} del que se obtiene
	 *                            la direccion servidor.
	 * @param numero              posicion del {@link portapapeles.Contenido} de tipo fichero a
	 *                            obtener unicamente.
	 */

	public Cliente(Ventana ventana,
			BroadcastingIpControl controlBroadcasting, int numero) {
		one = "si";
		this.numero = numero;
		this.ventana = ventana;
		this.controlBroadcasting = controlBroadcasting;
	}
	
	/**
	 * Lee comprimido pedido a {@link Servidor}
	 * 
	 * @param socketIs {@link InputStream} buffer del que se obtiene los datos del
	 *                 comprimido mandado por {@link Servidor}
	 * @throws IOException en caso de que falle la conexión.
	 */

	public void readZip(InputStream socketIs)
			throws IOException {
		ZipInputStream zips = new ZipInputStream(socketIs);
		ZipEntry zipEntry = null;

		out = new DataOutputStream(sc.getOutputStream());
		out.writeUTF(one);

		if (one.equals("si")) {
			out.writeInt(numero);
		}
		while (null != (zipEntry = zips.getNextEntry())) {
			String fileName = zipEntry.getName();
			File outFile = new File(ruta + "/"
					+ fileName.replace("\\", "/"));

			if (zipEntry.isDirectory()) {
				File zipEntryFolder = new File(zipEntry.getName());
				if (zipEntryFolder.exists() == false) {
					outFile.mkdirs();
				}

				continue;
			} else {
				File parentFolder = outFile.getParentFile();
				if (parentFolder.exists() == false) {
					parentFolder.mkdirs();
				}
			}

			ventana.panBarraProgreso.setNombre(outFile.getName());

			FileOutputStream fos = new FileOutputStream(outFile);
			int fileLength = (int) zipEntry.getSize();

			byte[] fileByte = new byte[fileLength];
			int written = 0;
			int hola = 0;
			double proceso = 0;
			int length;

			while ((length = zips.read(fileByte, 0, fileByte.length)) >= 0) {
				written += length;
				proceso = (((double) written / zipEntry.getCompressedSize()));
				hola = (int) (proceso * 100);
				ventana.panBarraProgreso.setPorcentaje(hola);
				fos.write(fileByte, 0, length);
			}
			ventana.panBarraProgreso.setPorcentajeCero();
			fos.close();
		}
	}

	/**
	 * Hilo que conecta con {@link Servidor} y recibe/crea ficheros mostrando el
	 * progreso en {@link Ventana} con {@link ventana.BarraProgreso}.
	 */
	
	@Override
	public void run() {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

		// Creo el socket para conectarme con el client
		boolean estaConectado = true;
		for (BroadcastingIp broadcastingIp : controlBroadcasting.lista_ip_servidor) {
			estaConectado = true;
			try {
				sc = new Socket(broadcastingIp.getIp(), PUERTO);
			} catch (Exception e) {
				estaConectado = false;
			}
			if (estaConectado) {
				host = broadcastingIp.getIp();
				conectado = true;
				break;
			}
		}
		if (conectado) {
			copiarFicheros = true;
			BufferedInputStream bis;
			try {
				bis = new BufferedInputStream(sc.getInputStream());
				ventana.panBarraProgreso.esconderPanelProgressBar(false);
				readZip(bis);
				ventana.panBarraProgreso.esconderPanelProgressBar(true);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		try {
			if (out != null) {
				out.close();
			}
			if (sc != null) {
				sc.close();
			}
		} catch (IOException ex) {
			Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null,
					ex);
		}
	}

}