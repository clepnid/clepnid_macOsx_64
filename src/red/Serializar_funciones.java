package red;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.imageio.ImageIO;

/**
 * Contiene funciones para serializar y viceversa objetos.
 * 
 * @author: Pavon
 * @version: 09/05/2020
 * @since 1.0
 */

public class Serializar_funciones {

	/**
	 * Convertir objeto a bytes[], el objeto a serializar tiene que tener
	 * implementado Serializable.
	 * 
	 * @param unObjetoSerializable {@link Object} objeto a serializar
	 * @return Array de bytes.
	 */

	public static byte[] convertirAByteArray(Object unObjetoSerializable) {
		ByteArrayOutputStream bs = new ByteArrayOutputStream();
		try {
			ObjectOutputStream os = new ObjectOutputStream(bs);
			os.writeObject(unObjetoSerializable);
			os.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		byte[] bytes = bs.toByteArray();
		try {
			bs.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bytes;
	}

	/**
	 * Pasa de {@link Image} a {@link BufferedImage}
	 * 
	 * @param im {@link Image} image para obtener BufferedImage
	 * @return {@link BufferedImage}
	 */
	
	 public static BufferedImage imageToBufferedImage(Image im) {
	     BufferedImage bi = new BufferedImage
	        (im.getWidth(null),im.getHeight(null),BufferedImage.TYPE_INT_RGB);
	     Graphics bg = bi.getGraphics();
	     bg.drawImage(im, 0, 0, null);
	     bg.dispose();
	     return bi;
	  }

	/**
	 * Convertir byte[] al objeto original, el objeto a deserializar tiene que tener
	 * implementado Serializable.
	 * 
	 * @param bytes array de bytes a convertir en el objeto original.
	 * @return Objeto deserializado.
	 */

	public static Object convertirAObjeto(byte[] bytes) {
		Object unObjetoSerializable = new Object();
		ByteArrayInputStream bs = new ByteArrayInputStream(bytes);
		ObjectInputStream is;
		try {
			is = new ObjectInputStream(bs);
			unObjetoSerializable = (Object) is.readObject();
			is.close();
			bs.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return unObjetoSerializable;
	}

	/**
	 * Convertir una imagen a byte[]
	 * 
	 * @param imagen {@link Image} imagen a serializar
	 * @return byte[].
	 */

	public static byte[] imageToByte(Image imagen) {
		byte[] imageInByte = null;
		try {
			BufferedImage buffered = imageToBufferedImage(imagen);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			// de bufferedImage pasa a buffer de bytes
			ImageIO.write(buffered, "jpg", baos);
			baos.flush();
			imageInByte = baos.toByteArray();
			baos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return imageInByte;
	}

	/**
	 * Convertir byte[] a imagen original.
	 * 
	 * @param imagen {@link BufferedImage} imagen a serializar
	 * @return {@link BufferedImage} original.
	 */

	public static BufferedImage ImageFromBytes(byte[] imagen) {
		ByteArrayInputStream bais = new ByteArrayInputStream(imagen);
		try {
			return ImageIO.read(bais);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
