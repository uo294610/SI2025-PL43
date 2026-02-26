
package giis.demo.util;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import nico_EntregarReportEvento.*;
import diego_asignarReporteros_33602.*;
import diego_ReportajesEvento_33607.*;
import adrian_ofrecerReportajes_33604.*;
import adrian_modificarOfrecimiento_33609.*;
import alex_InformeEvento_33613.*;
import alex_ModificarDecision_33611.*;
import adrian_distribuirReportajes_33606.*;
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
			frame.setTitle("Main Menu");
			frame.setBounds(0, 0, 350, 300);
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
	
			// // Restaurar Versión de Reportaje (Historia #33612)
			JButton btnRestaurarVersion = new JButton("Restaurar Versión de un Reportaje");
			btnRestaurarVersion.addActionListener(new ActionListener() {
			    public void actionPerformed(ActionEvent e) {
			        
			        // 1. Pedimos el ID con el nuevo texto orientativo
			        String input = JOptionPane.showInputDialog(frame, "Introduce el ID del reportaje que quieres probar (ej. 300, 301...):");
			        
			        if (input != null && !input.trim().isEmpty()) {
			            try {
			                int idReportajePrueba = Integer.parseInt(input.trim());
			                
			                // 2. Instanciamos SOLO el modelo primero para consultar a la base de datos
			                nico_RestaurarVersionReport_33612.RestaurarVersionModel model = new nico_RestaurarVersionReport_33612.RestaurarVersionModel();
			                
			                // 3. VERIFICACIÓN PREVIA: ¿Existe este reportaje?
			                java.util.List<Object[]> datos = model.getDatosCabeceraReportaje(idReportajePrueba);
			                
			                if (datos == null || datos.isEmpty()) {
			                    // Si no existe, lanzamos el error y NO abrimos la pantalla (el return corta la ejecución)
			                    JOptionPane.showMessageDialog(frame, 
			                        "Aviso: No existe ningún reportaje con el ID " + idReportajePrueba + " en la base de datos.", 
			                        "Reportaje no encontrado", 
			                        JOptionPane.WARNING_MESSAGE);
			                    return; 
			                }
			                
			                // 4. Si el código llega hasta aquí, significa que SÍ existe. Instanciamos la vista y el controlador.
			                nico_RestaurarVersionReport_33612.RestaurarVersionView view = new nico_RestaurarVersionReport_33612.RestaurarVersionView();
			                nico_RestaurarVersionReport_33612.RestaurarVersionController controller = new nico_RestaurarVersionReport_33612.RestaurarVersionController(model, view, idReportajePrueba);
			                
			                // 5. Arrancamos la pantalla de la historia
			                controller.initController();
			                
			            } catch (NumberFormatException ex) {
			                JOptionPane.showMessageDialog(frame, "Por favor, introduce solo números.");
			            }
			        }
			    }
			});
			frame.getContentPane().add(btnRestaurarVersion);
			
			// Gestionar Ofrecimientos Actualizado
			JButton btnGestionarOfrecimientosMod = new JButton("Gestionar Ofrecimientos");
			btnGestionarOfrecimientosMod.addActionListener(e -> {
				OfrecimientosController controller = new OfrecimientosController(new OfrecimientosModel(), new OfrecimientosView());
				
				controller.initController();
				controller.initView();
			});
			frame.getContentPane().add(btnGestionarOfrecimientosMod);

	// Distribuir Reportajes 
	JButton btnDistribuir = new JButton("Distribuir Reportajes");
	btnDistribuir.addActionListener(new ActionListener() { 
	    public void actionPerformed(ActionEvent e) {
	        DistribucionModel m = new DistribucionModel();
	        DistribucionView v = new DistribucionView();
	        DistribucionController c = new DistribucionController(m, v);
	        c.initController();
	        c.initView();
	    }
	});
	frame.getContentPane().add(btnDistribuir);
	

	// Modificar Ofrecimientos 
	JButton btnModificar = new JButton("Modificar Ofrecimientos");
	btnModificar.addActionListener(new ActionListener() { 
	    public void actionPerformed(ActionEvent e) {
	        adrian_modificarOfrecimiento_33609.OfrecimientoModModel m = new adrian_modificarOfrecimiento_33609.OfrecimientoModModel();
	        adrian_modificarOfrecimiento_33609.OfrecimientoModView v = new adrian_modificarOfrecimiento_33609.OfrecimientoModView();
	        adrian_modificarOfrecimiento_33609.OfrecimientoModController c = new adrian_modificarOfrecimiento_33609.OfrecimientoModController(m, v);
	        c.initController();
	        c.initView();
	    }
	});
	frame.getContentPane().add(btnModificar);
	

	
	// Generar informe .csv
	JButton btnInformeEvento = new JButton("Generar Informe Eventos");
	btnInformeEvento.addActionListener(e -> {
		InformeEventosController controller = new InformeEventosController(new InformeEventosModel(), new InformeEventosView());
			    controller.initController();
			    controller.initView();
			});
			frame.getContentPane().add(btnInformeEvento);
}

		


	public JFrame getFrame() { return this.frame; }
	}
