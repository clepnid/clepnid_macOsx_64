package red.compartirFicheros;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.Socket;
import java.util.zip.CRC32;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import ventana.Ventana;

/**
 * Clase que extiende de {@link Thread}, manda comprimido de ficheros a
 * {@link Cliente}
 * 
 * @author: Pavon
 * @version: 17/05/2020
 * @since 1.0
 */

class OperacionServidor extends Thread {

	private static int MAX_READ_SIZE = 1024;
	boolean conectado = false;
	Socket sc;
	DataInputStream in = null;

	Ventana ventana;

	/**
	 * Inicia las variables
	 * 
	 * @param sc
	 *            {@link Socket} conexion con {@link Cliente}
	 * @param ventana
	 *            {@link Ventana}
	 */

	public OperacionServidor(Socket sc, Ventana ventana) {
		this.sc = sc;
		conectado = true;
		this.ventana = ventana;
	}

	/**
	 * Metodo principal para controlar el envio segun el tipo de fichero.
	 * 
	 * @param zipOpStream
	 *            {@link ZipOutputStream} buffer para enviar fichero.
	 * @param outFile
	 *            {@link File} fichero a controlar.
	 */

	public void sendFileOutput(ZipOutputStream zipOpStream, File outFile)
			throws Exception {
		String relativePath = outFile.getAbsoluteFile().getParentFile()
				.getAbsolutePath();
		outFile = outFile.getAbsoluteFile();
		if (outFile.isDirectory()) {
			sendFolder(zipOpStream, outFile, relativePath);
		} else {
			sendFile(zipOpStream, outFile, relativePath);
		}
	}

	/**
	 * Envia los ficheros contenidos en la carpeta.
	 * 
	 * @param zipOpStream
	 *            {@link ZipOutputStream} buffer para enviar fichero.
	 * @param folder
	 *            {@link File} carpeta.
	 * @param relativePath
	 *            {@link File} ruta relativa.
	 */

	public void sendFolder(ZipOutputStream zipOpStream, File folder,
			String relativePath) throws Exception {
		File[] filesList = folder.listFiles();
		for (File file : filesList) {
			if (file.isDirectory()) {
				sendFolder(zipOpStream, file, relativePath);
			} else {
				sendFile(zipOpStream, file, relativePath);
			}
		}
	}

	/**
	 * Envia fichero
	 * 
	 * @param zipOpStream
	 *            {@link ZipOutputStream} buffer para enviar fichero.
	 * @param file
	 *            {@link File} carpeta.
	 * @param relativePath
	 *            {@link File} ruta relativa.
	 */

	public void sendFile(ZipOutputStream zipOpStream, File file,
			String relativePath) throws Exception {
		String absolutePath = file.getAbsolutePath();
		String zipEntryFileName = absolutePath;
		if (absolutePath.startsWith(relativePath)) {
			zipEntryFileName = absolutePath.substring(relativePath.length());
			if (zipEntryFileName.startsWith(File.separator)) {
				zipEntryFileName = zipEntryFileName.substring(1);
			}
		} else {
			throw new Exception("Invalid Absolute Path");
		}

		BufferedInputStream bis = new BufferedInputStream(new FileInputStream(
				file));
		byte[] fileByte = new byte[MAX_READ_SIZE];
		int readBytes = 0;
		CRC32 crc = new CRC32();
		while (0 != (readBytes = bis.read(fileByte))) {
			if (-1 == readBytes) {
				break;
			}
			crc.update(fileByte, 0, readBytes);
		}
		bis.close();
		ZipEntry zipEntry = new ZipEntry(zipEntryFileName);
		zipEntry.setMethod(ZipEntry.STORED);
		zipEntry.setCompressedSize(file.length());
		zipEntry.setSize(file.length());
		zipEntry.setCrc(crc.getValue());
		zipOpStream.putNextEntry(zipEntry);
		bis = new BufferedInputStream(new FileInputStream(file));
		while (0 != (readBytes = bis.read(fileByte))) {
			if (-1 == readBytes) {
				break;
			}

			zipOpStream.write(fileByte, 0, readBytes);
		}
		bis.close();

	}

	/**
	 * Inicia el hilo, envía fichero pedidos por {@link Cliente}
	 */

	public void run() {
		try {
			in = new DataInputStream(sc.getInputStream());
			if (in.readUTF().equals("si")) {
				int numero = in.readInt();
				ZipOutputStream zipOpStream = new ZipOutputStream(
						sc.getOutputStream());
				if (ventana.ficheros.ficheros.get(numero).exists()) {
					sendFileOutput(zipOpStream,
							ventana.ficheros.ficheros.get(numero));

				}
				zipOpStream.flush();
				sc.close();
				in.close();
				conectado = false;
			} else {
				ZipOutputStream zipOpStream = new ZipOutputStream(
						sc.getOutputStream());
				for (File readFile : ventana.ficheros.ficheros) {
					if (readFile.exists()) {
						sendFileOutput(zipOpStream, readFile);

					}
				}
				zipOpStream.flush();
				sc.close();
				in.close();
				conectado = false;
			}

		} catch (Exception e) {
			conectado = false;
		}
	}
}