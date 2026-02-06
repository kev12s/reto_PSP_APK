package eus.reto.psp.APKWebClient.app;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.springframework.http.HttpStatusCode;

import eus.reto.psp.APKWebClient.client.ApkClient;
import eus.reto.psp.APKWebClient.model.Apk;

import javax.swing.JTextField;
import javax.swing.JLabel;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JTextArea;

public class VentanaMain extends JFrame {
	private final ApkClient cliente;
	private final Scanner scanner;

	private JPanel contentPane;
	private JTextField idtextField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					VentanaMain frame = new VentanaMain();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public VentanaMain() {
		this.cliente = new ApkClient("http://localhost:8080");
		this.scanner = new Scanner(System.in);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 574, 402);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);

		idtextField = new JTextField();
		idtextField.setBounds(10, 46, 86, 20);
		contentPane.add(idtextField);
		idtextField.setColumns(10);

		JLabel lblNewLabel = new JLabel("ID de aplicacion");
		lblNewLabel.setBounds(10, 21, 96, 14);
		contentPane.add(lblNewLabel);

		JLabel lblNewLabel_1 = new JLabel("Intorduce descripcion");
		lblNewLabel_1.setBounds(126, 21, 155, 14);
		contentPane.add(lblNewLabel_1);

		Label label = new Label("Descripcion");
		label.setBounds(10, 196, 86, 22);
		contentPane.add(label);

		Label label_1 = new Label("APKs");
		label_1.setBounds(10, 249, 62, 22);
		contentPane.add(label_1);

		JButton btnModificar = new JButton("MODIFICAR");
		btnModificar.setBounds(10, 220, 96, 23);
		contentPane.add(btnModificar);

		JButton btnAñadir = new JButton("AÑADIR");
		btnAñadir.setBounds(166, 220, 96, 23);
		contentPane.add(btnAñadir);

		JButton btnEliminar = new JButton("ELIMINAR");
		btnEliminar.setBounds(312, 220, 96, 23);
		contentPane.add(btnEliminar);

		JButton btnDescargar = new JButton("DESCARGAR");
		btnDescargar.setBounds(10, 277, 96, 23);
		contentPane.add(btnDescargar);

		JButton btnHash = new JButton("HASH");
		btnHash.setBounds(166, 277, 96, 23);
		contentPane.add(btnHash);

		JButton btnVer = new JButton("VER APKS");
		btnVer.setBounds(312, 277, 96, 23);
		contentPane.add(btnVer);

		JTextArea textArea = new JTextArea();
		textArea.setBounds(126, 44, 383, 145);
		contentPane.add(textArea);

		JLabel lblMensaje = new JLabel("");
		lblMensaje.setBounds(126, 338, 337, 14);
		contentPane.add(lblMensaje);
		
		JButton btnImagen = new JButton("IMAGEN");
		btnImagen.setBounds(10, 311, 96, 23);
		contentPane.add(btnImagen);


		//APLICACIONES
		//listar todas apks
		btnVer.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {    

				List<Apk> apks = cliente.obetenerApks();
				if(apks!=null) {
					textArea.setText(apks.toString());
				}else {
					lblMensaje.setText("NO SE HAN PODIDO OBTENER APKS");
				}
			}
		});

		//descargar apk
		btnDescargar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int id = Integer.parseInt(idtextField.getText());          

				byte[] bytes = cliente.descargarApk(id);
				if(bytes!=null) {
					lblMensaje.setText("DESCARGAR CORRECTA");
				}else {
					lblMensaje.setText("NO SE HA PODIDO DESCARGAR");
				}
			}
		});

		//descargar imagen
		btnImagen.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int id = Integer.parseInt(idtextField.getText());

				byte[] bytes = cliente.descargarImagen(id);
				if(bytes!=null) {
					lblMensaje.setText("DESCARGAR CORRECTA");
				}else {
					lblMensaje.setText("NO SE HA PODIDO DESCARGAR");
				}
			}
		});
		
		//descargar hash
		btnHash.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int id = Integer.parseInt(idtextField.getText());

				String hash = cliente.obtenerHash(id);
				
				if(hash!=null) {
					lblMensaje.setText(hash);
				}else {
					lblMensaje.setText(hash);
				}
			}
		});

		//DESCRIPCIN 
		//modificar descripcion
		btnModificar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int id = Integer.parseInt(idtextField.getText());
				String descripcion = textArea.getText();

				System.out.println("Modificando APK - ID: " + id + ", Descripción: " + descripcion);

				// Usar Web Client para modificar
				Apk apk = cliente.modificarDescripcion(id, descripcion);
				if(apk!=null) {
					textArea.setText("MODIFICACION CORRETA: " + apk.toString());
				}else {
					textArea.setText("MODIFICACION INCORRETA: ");
				}
			}
		});

		//añadir descripcion
		btnAñadir.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int id = Integer.parseInt(idtextField.getText());
				String descripcion = textArea.getText();

				System.out.println("Añadiendo APK - ID: " + id + ", Descripción: " + descripcion);

				Apk apk = cliente.añadirDescripcion(id, descripcion);
				if(apk!=null) {
					textArea.setText("MODIFICACION CORRETA: " + apk.toString());
				}else {
					textArea.setText("MODIFICACION CORRETA: ");
				}
			}
		});
		
		//eliminar descripcion
		btnAñadir.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int id = Integer.parseInt(idtextField.getText());
				String descripcion = textArea.getText();

				HttpStatusCode status = cliente.eliminarDescripcion(id);
				if(status.equals(200)) {
					textArea.setText("ELIMINACION CORRETA: " + status);
				}else {
					textArea.setText("ELIMINACION INCORRETA: " + status);
				}
			}
		});

	}


}
