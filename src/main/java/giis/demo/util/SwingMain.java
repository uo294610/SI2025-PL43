	package giis.demo.util;
	
	import java.awt.EventQueue;
	import javax.swing.JFrame;
	import javax.swing.BoxLayout;
	import javax.swing.JButton;
	import java.awt.event.ActionListener;
	import java.awt.event.ActionEvent;
	
	// Imports de tus paquetes
	
	import diego_asignarReporteros_33602.*;
	import diego_ReportajesEvento_33607.*;
	import alex_ModificarDecision_33611.*;
	public class SwingMain {
	
		private JFrame frame;
	
		public static void main(String[] args) {
			EventQueue.invokeLater(new Runnable() {
				public void run() {
					try {
						SwingMain window = new SwingMain();
						window.frame.setVisible(true);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		}
	
		public SwingMain() {
			initialize();
		}
	
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