import java.awt.EventQueue;

import java.awt.Font;

import javax.swing.JFrame;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.JPasswordField;

import javax.swing.JTabbedPane;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import javax.swing.JScrollPane;

import javax.swing.ImageIcon;

import javax.swing.JButton;

import javax.swing.JTable;

import javax.swing.table.DefaultTableModel;

import java.awt.event.ActionListener;

import java.awt.event.ActionEvent;

import java.awt.Toolkit;

import java.awt.Color;
import javax.swing.JSpinner;
import javax.swing.SpinnerListModel;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;

public class Vista extends JFrame {

	private DefaultTableModel model;

	/**
	 * 
	 * Launch the application.
	 * 
	 */

	private JFrame frame;

	private JTextField txtDisco;

	private JButton btnStrt;

	private Controlador control;

	private Disco tempDisc;

	private JButton btnRegistro;
	private JTable tablaDatos;
	private JTextField txtArtista;
	private JTextField txtFecha;
	private JTextField txtGenero;
	private JScrollPane scrollPane;
	private JButton btnCargarBbdd;
	private JButton btnCargarFichero;
	private JButton btnCargarHibernate;
	private String source;
	private JButton btnBorrarRegistro;
	private JButton btnGuardar;
	private JButton btnEditar;
	private JButton btnCargarMongodb;
	private JButton btnAHibernate;
	private JButton btnAFichero;
	private JButton btnABbdd;
	private JButton btnAMongodb;
	private JButton btnCargarServidor;
	private JButton btnAServidor;
	private JButton btnCargarNode;
	private JButton btnANode;

	public void setControlador(Controlador control) {

		this.control = control;
		esBBDD();
		control.cargaSQL();

	}

	public void alertVacio() {
		JOptionPane.showMessageDialog(null, "No puede haber campos vacios");
	}

	public void alertNadaSel() {
		JOptionPane.showMessageDialog(null, "Debes seleccionar una fila para editar");
	}

	public void alertBBDD() {
		JOptionPane.showMessageDialog(null,
				"Error al acceder a la base de datos, comprueba el archivo de configuracion");
	}

	public Vista() {

		getContentPane().setBackground(Color.WHITE);

		setBackground(Color.WHITE);

		// setIconImage(Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("/img/Empawake2.png")));

		setTitle("Login");

		setBounds(600, 200, 855, 430);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		getContentPane().setLayout(null);

		txtDisco = new JTextField();

		txtDisco.setBounds(85, 59, 129, 20);

		getContentPane().add(txtDisco);

		txtDisco.setColumns(10);

		scrollPane = new JScrollPane();
		scrollPane.setBounds(85, 165, 407, 202);
		getContentPane().add(scrollPane);

		tablaDatos = new JTable();
		scrollPane.setViewportView(tablaDatos);
		tablaDatos.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

			public void valueChanged(ListSelectionEvent event) {
				if (!tablaDatos.getSelectionModel().isSelectionEmpty()) {
					tempDisc = new Disco(
							Integer.parseInt(tablaDatos.getValueAt(tablaDatos.getSelectedRow(), 0).toString()),
							tablaDatos.getValueAt(tablaDatos.getSelectedRow(), 1).toString(),
							tablaDatos.getValueAt(tablaDatos.getSelectedRow(), 2).toString(),
							tablaDatos.getValueAt(tablaDatos.getSelectedRow(), 3).toString(),
							tablaDatos.getValueAt(tablaDatos.getSelectedRow(), 4).toString());
					setData(tempDisc.getNombre(),tempDisc.getArtista(),tempDisc.getYear(),tempDisc.getGenero());

				}

			}

			
		});
		JLabel lblDisco = new JLabel("Disco");
		lblDisco.setBounds(131, 34, 46, 14);
		getContentPane().add(lblDisco);

		JLabel lblArtista = new JLabel("Artista");
		lblArtista.setBounds(270, 34, 46, 14);
		getContentPane().add(lblArtista);

		txtArtista = new JTextField();
		txtArtista.setColumns(10);
		txtArtista.setBounds(224, 59, 129, 20);
		getContentPane().add(txtArtista);

		JLabel lblAo = new JLabel("A\u00F1o");
		lblAo.setBounds(409, 34, 46, 14);
		getContentPane().add(lblAo);

		txtFecha = new JTextField();
		txtFecha.setColumns(10);
		txtFecha.setBounds(363, 59, 129, 20);
		getContentPane().add(txtFecha);

		JLabel lblGenero = new JLabel("Genero");
		lblGenero.setBounds(270, 109, 46, 14);
		getContentPane().add(lblGenero);

		txtGenero = new JTextField();
		txtGenero.setColumns(10);
		txtGenero.setBounds(224, 134, 129, 20);
		getContentPane().add(txtGenero);

		btnCargarBbdd = new JButton("Cargar BBDD");
		btnCargarBbdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				control.cargaSQL();

				esBBDD();
				tablaDatos.clearSelection();
				tempDisc = null;
			}
		});
		btnCargarBbdd.setBounds(541, 59, 129, 23);
		getContentPane().add(btnCargarBbdd);

		btnCargarFichero = new JButton("Cargar Fichero");
		btnCargarFichero.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				control.cargaFichero();

				esFichero();
				tablaDatos.clearSelection();
				tempDisc = null;
			}
		});
		btnCargarFichero.setBounds(680, 59, 129, 23);
		getContentPane().add(btnCargarFichero);

		btnCargarHibernate = new JButton("Cargar Hibernate");
		btnCargarHibernate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				control.cargaHib();
				esHib();

				tablaDatos.clearSelection();
				tempDisc = null;

			}
		});
		btnCargarHibernate.setBounds(541, 87, 129, 23);
		getContentPane().add(btnCargarHibernate);

		btnGuardar = new JButton("Guardar");
		btnGuardar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!txtDisco.getText().isEmpty() && !txtArtista.getText().isEmpty() && !txtFecha.getText().isEmpty()
						&& !txtGenero.getText().isEmpty()) {
					Disco tmp = new Disco(txtDisco.getText(), txtArtista.getText(), txtFecha.getText(),
							txtGenero.getText());
					control.guarda(source,tmp );
					limpiaCuestionario();
				}else {
					alertVacio();
				}
					
			}
		});
		btnGuardar.setBounds(541, 121, 129, 23);
		getContentPane().add(btnGuardar);

		btnEditar = new JButton("Editar");
		btnEditar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(tablaDatos.getSelectionModel().isSelectionEmpty()) {
					alertNadaSel();
				}else if(txtDisco.getText().isEmpty() && txtArtista.getText().isEmpty() && txtFecha.getText().isEmpty()
						&& txtGenero.getText().isEmpty()){
					alertVacio();
					
				}else {
					tempDisc.setArtista(txtArtista.getText());
					tempDisc.setNombre(txtDisco.getText());
					tempDisc.setGenero(txtGenero.getText());
					tempDisc.setYear(txtFecha.getText());
					System.out.println(tempDisc.toString());
					control.edita(source,tempDisc);
					limpiaCuestionario();
				}
			}
		});
		btnEditar.setBounds(680, 121, 129, 23);
		getContentPane().add(btnEditar);

		btnBorrarRegistro = new JButton("Borrar Registro");
		btnBorrarRegistro.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(tablaDatos.getSelectionModel().isSelectionEmpty()) {
					alertNadaSel();
				}else {
					control.borra(source,tempDisc);
				}
				
			}
		});
		btnBorrarRegistro.setBounds(541, 151, 129, 23);
		getContentPane().add(btnBorrarRegistro);

		btnCargarMongodb = new JButton("Cargar MongoDB");
		btnCargarMongodb.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				control.cargaMongo();
				esMongo();
				tablaDatos.clearSelection();
				tempDisc = null;
			}

		});
		btnCargarMongodb.setBounds(680, 87, 129, 23);
		getContentPane().add(btnCargarMongodb);

		JButton btnBorrar = new JButton("Borrar");
		btnBorrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				control.borra(source);
			}
		});
		btnBorrar.setBounds(680, 151, 129, 23);
		getContentPane().add(btnBorrar);

		btnAHibernate = new JButton("A Hibernate");
		btnAHibernate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				control.aHib(source);
			}
		});
		btnAHibernate.setBounds(541, 219, 129, 23);
		getContentPane().add(btnAHibernate);

		btnABbdd = new JButton("A BBDD");
		btnABbdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				control.aBBDD(source);
			}
		});
		btnABbdd.setBounds(541, 185, 129, 23);
		getContentPane().add(btnABbdd);

		btnAFichero = new JButton("A Fichero");
		btnAFichero.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				control.aFichero(source);
			}
		});
		btnAFichero.setBounds(680, 185, 129, 23);
		getContentPane().add(btnAFichero);

		btnAMongodb = new JButton("A MongoDB");
		btnAMongodb.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				control.aMongo(source);
			}
		});
		btnAMongodb.setBounds(680, 219, 129, 23);
		getContentPane().add(btnAMongodb);
		
		btnCargarServidor = new JButton("Cargar Servidor");
		btnCargarServidor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				esServidor();
				tablaDatos.clearSelection();
				tempDisc = null;
				control.cargaServer();
				
			}
		});
		btnCargarServidor.setBounds(541, 30, 129, 23);
		getContentPane().add(btnCargarServidor);
		
		btnAServidor = new JButton("A Servidor");
		btnAServidor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				control.aServer(source);
				
			}
		});
		btnAServidor.setBounds(541, 253, 129, 23);
		getContentPane().add(btnAServidor);
		
		btnCargarNode = new JButton("Cargar Node");
		btnCargarNode.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				esNode();
				tablaDatos.clearSelection();
				tempDisc = null;
				control.cargarNode();
				
			}
		});
		btnCargarNode.setBounds(680, 29, 129, 25);
		getContentPane().add(btnCargarNode);
		
		btnANode = new JButton("A node");
		btnANode.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				control.aNode(source);
				
			}
		});
		btnANode.setBounds(680, 252, 129, 25);
		getContentPane().add(btnANode);
		this.esBBDD();

	}

	public void esServidor() {
		this.btnCargarFichero.setEnabled(true);
		this.btnCargarBbdd.setEnabled(true);
		this.btnCargarMongodb.setEnabled(true);
		this.btnCargarHibernate.setEnabled(true);
		this.btnAFichero.setEnabled(true);
		this.btnABbdd.setEnabled(true);
		this.btnAHibernate.setEnabled(true);
		this.btnAMongodb.setEnabled(true);
		this.btnCargarServidor.setEnabled(false);
		this.btnAServidor.setEnabled(false);
		this.btnANode.setEnabled(true);
		this.btnCargarNode.setEnabled(true);
		source = "server";
		
	}

	protected void limpiaCuestionario() {
		txtArtista.setText("");
		txtFecha.setText("");
		txtDisco.setText("");
		txtGenero.setText("");
	}

	public void setTabla(DefaultTableModel tabla) {
		this.tablaDatos.setModel(tabla);
	}

	public void esFichero() {
		this.btnCargarFichero.setEnabled(false);
		this.btnCargarBbdd.setEnabled(true);
		this.btnCargarMongodb.setEnabled(true);
		this.btnCargarHibernate.setEnabled(true);
		this.btnAFichero.setEnabled(false);
		this.btnABbdd.setEnabled(true);
		this.btnAHibernate.setEnabled(true);
		this.btnAMongodb.setEnabled(true);
		this.btnCargarServidor.setEnabled(true);
		this.btnAServidor.setEnabled(true);
		this.btnANode.setEnabled(true);
		this.btnCargarNode.setEnabled(true);

		source = "fichero";

		

	}

	public void esBBDD() {
		this.btnCargarFichero.setEnabled(true);
		this.btnCargarBbdd.setEnabled(false);
		this.btnCargarMongodb.setEnabled(true);
		this.btnCargarHibernate.setEnabled(true);
		this.btnAFichero.setEnabled(true);
		this.btnABbdd.setEnabled(false);
		this.btnAHibernate.setEnabled(true);
		this.btnAMongodb.setEnabled(true);
		this.btnCargarServidor.setEnabled(true);
		this.btnAServidor.setEnabled(true);
		this.btnANode.setEnabled(true);
		this.btnCargarNode.setEnabled(true);
		source = "mysql";
		
		

	}

	public void esHib() {
		this.btnCargarFichero.setEnabled(true);
		this.btnCargarBbdd.setEnabled(true);
		this.btnCargarMongodb.setEnabled(true);
		this.btnCargarHibernate.setEnabled(false);
		this.btnAFichero.setEnabled(true);
		this.btnABbdd.setEnabled(true);
		this.btnAHibernate.setEnabled(false);
		this.btnAMongodb.setEnabled(true);
		this.btnCargarServidor.setEnabled(true);
		this.btnAServidor.setEnabled(true);
		this.btnANode.setEnabled(true);
		this.btnCargarNode.setEnabled(true);
		source = "hib";
		
	}

	public void esMongo() {
		this.btnCargarFichero.setEnabled(true);
		this.btnCargarBbdd.setEnabled(true);
		this.btnCargarMongodb.setEnabled(false);
		this.btnCargarHibernate.setEnabled(true);
		this.btnAFichero.setEnabled(true);
		this.btnABbdd.setEnabled(true);
		this.btnAHibernate.setEnabled(true);
		this.btnAMongodb.setEnabled(false);
		this.btnCargarServidor.setEnabled(true);
		this.btnAServidor.setEnabled(true);
		this.btnANode.setEnabled(true);
		this.btnCargarNode.setEnabled(true);
		source = "mongo";
		
		

	}
	private void setData(String disco, String artista, String year, String genero) {
		txtDisco.setText(disco);
		txtArtista.setText(artista);
		txtFecha.setText(year);
		txtGenero.setText(genero);

	}
	

	public void alertDuplicado() {
		JOptionPane.showMessageDialog(null,
				"Registro duplicado, cancelando inserción");
		
		
	}

	public void alertErrorEscritura() {
		JOptionPane.showMessageDialog(null,
				"Error en la insercion, cancelando inserción");
		this.limpiaCuestionario();
		this.tempDisc = null;
		
	}

	public void ocultaHibernate() {
		btnAHibernate.setVisible(false);
		btnCargarHibernate.setVisible(false);
		
	}

	public void esNode() {
		this.btnCargarFichero.setEnabled(true);
		this.btnCargarBbdd.setEnabled(true);
		this.btnCargarMongodb.setEnabled(true);
		this.btnCargarHibernate.setEnabled(true);
		this.btnAFichero.setEnabled(true);
		this.btnABbdd.setEnabled(true);
		this.btnAHibernate.setEnabled(true);
		this.btnAMongodb.setEnabled(true);
		this.btnCargarServidor.setEnabled(true);
		this.btnAServidor.setEnabled(true);
		this.btnANode.setEnabled(false);
		this.btnCargarNode.setEnabled(false);
		source = "node";
		
	}
}
