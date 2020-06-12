package teclado;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;

/**
 * Clase para manejar la introducción simulada de teclas dentro del sistema
 * operativo.
 * 
 * @author: Pavon
 * @version: 10/04/2020
 * @since 1.0
 */

public class PulsarTeclas {

	private static Robot robot;

	/**
	 * Simula Ctrl + c dentro del sistema operativo.
	 */

	public static void copiar() {
		try {
			robot = new Robot();
		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		robot.keyPress(KeyEvent.VK_META);
		robot.keyPress(KeyEvent.VK_C);
		robot.delay(10);
		robot.keyRelease(KeyEvent.VK_C);
		robot.keyRelease(KeyEvent.VK_META);
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Simula Ctrl + v dentro del sistema operativo.
	 */

	public static void pegar() {

		try {
			robot = new Robot();
		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		robot.keyPress(KeyEvent.VK_META);
		robot.keyPress(KeyEvent.VK_V);
		robot.delay(10);
		robot.keyRelease(KeyEvent.VK_V);
		robot.keyRelease(KeyEvent.VK_META);
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
