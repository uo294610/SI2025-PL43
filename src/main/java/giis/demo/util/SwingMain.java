
package giis.demo.util;

import java.awt.EventQueue;

import javax.swing.JFrame;

import nico_EntregarReportEvento.*;
import diego_asignarReporteros_33602.*;
import diego_ReportajesEvento_33607.*;
import adrian_ofrecerReportajes_33604.*;
import alex_ModificarDecision_33611.*;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * Punto de entrada principal que incluye botones para la ejecucion de las pantallas 
 * de las aplicaciones de ejemplo
 * y acciones de inicializacion de la base de datos.
 * No sigue MVC pues es solamente temporal para que durante el desarrollo se tenga posibilidad
 * de realizar acciones de inicializacion
 */
public class SwingMain {

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() { //NOSONAR codigo autogenerado
			public void run() {
				try {
					SwingMain window = new SwingMain();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace(); //NOSONAR codigo autogenerado
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public SwingMain() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */

		private void initialize() {
			frame = new JFrame();
			frame.setTitle("Main Menu - Gestión de Ofrecimientos");
			frame.setBounds(0, 0, 350, 300); // Un poco más alto para los nuevos botones
			frame.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
			frame.getContentPane().setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
		
			// --- BOTONES DE BASE DE DATOS ---
			JButton btnInicializarBaseDeDatos = new JButton("Inicializar Base de Datos en Blanco");
			btnInicializarBaseDeDatos.addActionListener(e -> {
				Database db = new Database();
				db.createDatabase(false);
			});
			frame.getContentPane().add(btnInicializarBaseDeDatos);
				
			JButton btnCargarDatosIniciales = new JButton("Cargar Datos Iniciales para Pruebas");
			btnCargarDatosIniciales.addActionListener(e -> {
				Database db = new Database();
				db.createDatabase(false);
				db.loadDatabase();
			});
			frame.getContentPane().add(btnCargarDatosIniciales);
			
			// --- BOTONES DE FUNCIONALIDADES ---
			
			// Asignar Reporteros
			JButton btnAsignarReporteros = new JButton("Asignar Reporteros");
			btnAsignarReporteros.addActionListener(e -> {
				ReporteroController controller = new ReporteroController(new ReporteroModel(), new ReporteroView());
				controller.initController();
			});
			frame.getContentPane().add(btnAsignarReporteros);
			
			// Ver reportajes
			JButton btnLeerReportajes = new JButton("Leer Reportajes (Empresas)");
			btnLeerReportajes.addActionListener(e -> {
				EmpresaController controller = new EmpresaController(new EmpresaModel(), new EmpresaView());
				controller.initController();
			});
			frame.getContentPane().add(btnLeerReportajes);
			// Ofrecer Reportajes 
			JButton btnOfrecerReportajes_33604 = new JButton("Ofrecer Reportajes");
			btnOfrecerReportajes_33604.addActionListener(new ActionListener() { 
				public void actionPerformed(ActionEvent e) {
					OfrecimientoModel model = new OfrecimientoModel();
					OfrecimientoView view = new OfrecimientoView();
					OfrecimientoController controller = new OfrecimientoController(model, view);
	        
	        controller.initController();
	        controller.initView();
				}
			});
			frame.getContentPane().add(btnOfrecerReportajes_33604);
	
			// Entregar Reportaje (Historia #33603)
			JButton btnEntregarReportaje = new JButton("Entregar Reportaje");
			btnEntregarReportaje.addActionListener(new ActionListener() {
			    public void actionPerformed(ActionEvent e) {
			        // Instanciamos los componentes de tu paquete nico_entregaEvento_33603
			        EntregaReportajeModel model = new EntregaReportajeModel();
			        EntregaReportajeView view = new EntregaReportajeView();
			        
			        // Creamos el controlador pasando el modelo, la vista y el ID del reportero logueado (ej: 1)
			        EntregaReportajeController controller = new EntregaReportajeController(model, view, 1);
			        
			        // Inicializamos la lógica del controlador
			        controller.initController();
			    }
			});
			frame.getContentPane().add(btnEntregarReportaje);
			
			// // Modificar Entrega de Reportaje (Historia #33610)
			JButton btnModificarEntrega = new JButton("Modificar Entrega");
			btnModificarEntrega.addActionListener(new ActionListener() {
			    public void actionPerformed(ActionEvent e) {
			        // Instanciamos los componentes de tu NUEVO paquete nico_ModificaEntrega_33610
			        nico_ModificaEntrega_33610.ModificaEntregaModel model = new nico_ModificaEntrega_33610.ModificaEntregaModel();
			        nico_ModificaEntrega_33610.ModificaEntregaView view = new nico_ModificaEntrega_33610.ModificaEntregaView();
			        
			        // Creamos el controlador (ya no hace falta pasar el ID porque se elige en el ComboBox)
			        nico_ModificaEntrega_33610.ModificaEntregaController controller = new nico_ModificaEntrega_33610.ModificaEntregaController(model, view);
			        
			        // Inicializamos la lógica y mostramos la ventana
			        controller.initController();
			    }
			});
			frame.getContentPane().add(btnModificarEntrega);
	
			// Gestionar Ofrecimientos Actualizado
			JButton btnGestionarOfrecimientosMod = new JButton("Gestionar Ofrecimientos");
			btnGestionarOfrecimientosMod.addActionListener(e -> {
				OfrecimientosController controller = new OfrecimientosController(new OfrecimientosModel(), new OfrecimientosView());
				
				controller.initController();
				controller.initView();
			});
			frame.getContentPane().add(btnGestionarOfrecimientosMod);
		
		}

	public JFrame getFrame() { return this.frame; }
	}
