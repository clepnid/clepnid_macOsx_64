package ventana;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TaskBar;
import org.eclipse.swt.widgets.TaskItem;
import org.eclipse.wb.swt.SWTResourceManager;

/**
 * Clase que extiende de {@link Composite} con la clase de {@link ProgressBar}.
 * 
 * @author: Pavon
 * @version: 09/04/2020
 * @since 1.0
 */

public class BarraProgreso extends Composite {

	public Display display;
	public Shell shell;
	public ProgressBar barraProgreso;
	public Label lblPorcentajeBarraProgreso;
	public GridData gd_BarraProgreso;
	public GridData gd_panProgreso;
	public Label lblCopiando;
	public Ventana ventana;
	public TaskItem taskItem;

	/**
	 * Constructor del panel con la barra de progreso.
	 * 
	 * @param ventana {@link Ventana} en la que el panel con la barra de progreso se
	 *                muestra por pantalla.
	 * @param parent  {@link Composite} panel padre para {@link BarraProgreso}.
	 * @param style   numero de referencia de la apariencia que va a tener el
	 *                componente.
	 * @param display {@link Display} controlador entre la ventana y el sistema
	 *                operativo.
	 * @param shell   {@link shell} controlador de la ventana..
	 * 
	 */

	public BarraProgreso(Ventana ventana, Composite parent, int style, Display display, Shell shell) {
		super(parent, style);
		this.ventana = ventana;
		this.display = display;
		this.shell = shell;
		setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		setLayout(new GridLayout(1, false));

		this.gd_panProgreso = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd_panProgreso.heightHint = 71;
		setLayoutData(gd_panProgreso);

		this.lblCopiando = new Label(this, SWT.NONE);
		lblCopiando.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		lblCopiando.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		lblCopiando.setText("Copiando ...");

		barraProgreso = new ProgressBar(this, SWT.NONE);
		barraProgreso.setMinimum(0);
		barraProgreso.setMaximum(100);
		barraProgreso.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		gd_BarraProgreso = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_BarraProgreso.heightHint = 12;
		barraProgreso.setLayoutData(gd_BarraProgreso);

		lblPorcentajeBarraProgreso = new Label(this, SWT.NONE);
		lblPorcentajeBarraProgreso.setAlignment(SWT.RIGHT);
		lblPorcentajeBarraProgreso.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		lblPorcentajeBarraProgreso.setText("0%");
		lblPorcentajeBarraProgreso.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));

	}

	/**
	 * Oculta el panel con la barra de progreso.
	 * 
	 * @param condicion <code>true</code> para esconder el panel con la barra de
	 *                  progreso, <code>false</code> si desea mostrar el panel con
	 *                  la barra de progreso.
	 */

	public void esconderPanelProgressBar(final boolean condicion) {
		display.asyncExec(new Runnable() {
			public void run() {
				if (!condicion) {
					gd_panProgreso.exclude = false;
					setLayoutData(gd_panProgreso);
					setVisible(true);
					ventana.panCuerpo.layout();
				} else {
					gd_panProgreso.exclude = true;
					setVisible(false);
					ventana.panCuerpo.layout();
				}
			}
		});

	}

	/**
	 * Obtiene el objeto {@link TaskItem} manejador del icono de la barra de
	 * progreso.
	 * 
	 * @return {@link TaskItem} manejador del icono de la barra de progreso.
	 */

	public TaskItem getTaskBarItem() {
		TaskBar bar = display.getSystemTaskBar();
		if (bar == null) {
			return null;
		}
		TaskItem item = bar.getItem(shell);
		if (item == null) {
			item = bar.getItem(null);
		}
		return item;
	}

	/**
	 * Define el numero con el porcentaje que se va a mostrar en el
	 * {@link Composite}.
	 * 
	 * @param numero a mostrar por pantalla referente al porcentaje de la copia.
	 */

	public void setPorcentaje(final int numero) {
		display.asyncExec(new Runnable() {
			public void run() {
				barraProgreso.setSelection(numero);
				lblPorcentajeBarraProgreso.setText(String.valueOf(numero) + "%");
				taskItem = getTaskBarItem();
				if (taskItem != null) {
					taskItem.setProgress(numero);
				}
			}
		});
	}

	/**
	 * Define el nombre que se va a mostrar en el {@link Composite}.
	 * 
	 * @param nombre {@link String} a mostrar por pantalla referente al nombre de la
	 *                copia.
	 */

	public void setNombre(final String nombre) {
		display.asyncExec(new Runnable() {
			public void run() {
				lblCopiando.setText("Copiando " + nombre + "...");
			}
		});
	}

	/**
	 * Vuelve al estado antes de copiar a la barra de progreso.
	 */

	public void setPorcentajeCero() {
		ventana.display.asyncExec(new Runnable() {
			public void run() {
				taskItem = ventana.panBarraProgreso.getTaskBarItem();
				if (taskItem != null) {
					taskItem.setProgress(0);
					taskItem.setProgressState(0);
				}
				ventana.panBarraProgreso.barraProgreso.setSelection(0);
				ventana.panBarraProgreso.lblPorcentajeBarraProgreso.setText("0%");
			}
		});
	}

}
