package teclado;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;

import ventana.Ventana;

/**
 * Clase para manejar los eventos de teclas del sistema operativo.
 * 
 * @author: Pavon
 * @version: 08/05/2020
 * @since 1.0
 */

public class GlobalKeys {

	public EventoTeclasGlobal eventos;

	/**
	 * Constructor que define la ventana en la que se van a mostrar los elementos
	 * seleccionados por teclas del sistema operativo.
	 * 
	 * @param ventana {@link Ventana} en la que se van a mostrar los elementos
	 *                seleccionados por teclas del sistema operativo.
	 * 
	 */

	public GlobalKeys(Ventana ventana) {
		eventos = new EventoTeclasGlobal(ventana);
		try {
			// bloquear la impresion de registros de la clase GlobalScreen.
			Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
			logger.setLevel(Level.OFF);
			// hilo de registros del raton y teclado global.
			GlobalScreen.registerNativeHook();
		} catch (NativeHookException e) {
			e.printStackTrace();
		}
		GlobalScreen.addNativeKeyListener(eventos);
	}

	/**
	 * Finaliza el hilo de eventos de teclas del sistema operativo.
	 */

	// cierra el hilo de registros del raton y teclado global
	public void cerrar() {
		try {
			GlobalScreen.unregisterNativeHook();
		} catch (NativeHookException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
