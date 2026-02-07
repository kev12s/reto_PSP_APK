package eus.reto.psp.APKWebClient.app;

import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.client.WebClientResponseException;

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
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.swing.ImageIcon;
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
		setBounds(100, 100, 976, 455);
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
		textArea.setBounds(126, 44, 767, 160);
		contentPane.add(textArea);

		JLabel lblMensaje = new JLabel("");
		lblMensaje.setBounds(10, 338, 518, 14);
		contentPane.add(lblMensaje);

		JButton btnImagen = new JButton("IMAGEN");
		btnImagen.setBounds(10, 311, 96, 23);
		contentPane.add(btnImagen);

		JLabel lblImagen = new JLabel();
		lblImagen.setBounds(600, 215, 293, 200); 
		lblImagen.setHorizontalAlignment(JLabel.CENTER);
		lblImagen.setVerticalAlignment(JLabel.CENTER);
		lblImagen.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
		lblImagen.setText("Imagen aparecerá aquí");
		lblImagen.setFont(new Font("Tahoma", Font.ITALIC, 12));
		contentPane.add(lblImagen);

		JLabel lblTituloImagen = new JLabel("Vista previa de imagen");
		lblTituloImagen.setBounds(126, 190, 150, 14);
		contentPane.add(lblTituloImagen);


		//APLICACIONES
		//listar todas apks (ESTE VA BIEN)
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
				try {
					int id = Integer.parseInt(idtextField.getText());          

					byte[] bytes = cliente.descargarApk(id);
					
					if(bytes != null && bytes.length > 0) {
		                lblMensaje.setText("APK descargada (" + bytes.length + " bytes). Verificando hash...");
					}
					
					boolean hashValido = cliente.verificarHash(id, bytes);

					if(hashValido) {
						// Crear carpeta "descargas" si no existe
						File downloadsDir = new File("descargas");
						if (!downloadsDir.exists()) {
							downloadsDir.mkdir();
						}

						// Crear archivo
						File apkFile = new File(downloadsDir, "app_" + id + ".apk");

						// Guardar bytes
						try (FileOutputStream fos = new FileOutputStream(apkFile)) {
							fos.write(bytes);
							lblMensaje.setText("APK guardada en: " + apkFile.getAbsolutePath());
							System.out.println("Guardado: " + apkFile.getAbsolutePath());
						}
					} else {
						lblMensaje.setText("ERROR: No se recibieron datos del servidor");
					}

				} catch (NumberFormatException ex) {
					lblMensaje.setText("ID inválido");
				} catch (IOException ex) {
					lblMensaje.setText("Error al guardar archivo");
				} catch (Exception ex) {
					lblMensaje.setText("Error: " + ex.getMessage());
				}
			}
		});

		//descargar imagen
		btnImagen.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				try {
					int id = Integer.parseInt(idtextField.getText());

					byte[] bytes = cliente.descargarImagen(id);
					if(bytes != null && bytes.length > 0) {
						lblMensaje.setText("DESCARGAR CORRECTA - Tamaño imagen: " + bytes.length + " bytes");

						// Convertir bytes a ImageIcon y mostrar en el JLabel
						try {
							ImageIcon icon = new ImageIcon(bytes);

							// Escalar la imagen si es muy grande
							ImageIcon scaledIcon = scaleImageIcon(icon, 400, 200);

							lblImagen.setIcon(scaledIcon);
							lblImagen.setText(""); // Limpiar texto
						} catch (Exception ex) {
							lblImagen.setText("Error al cargar imagen");
							lblImagen.setIcon(null);
							lblMensaje.setText("Error al procesar imagen: " + ex.getMessage());
						}
					} else {
						lblMensaje.setText("NO SE HA PODIDO DESCARGAR LA IMAGEN");
						lblImagen.setText("Imagen no disponible");
						lblImagen.setIcon(null);
					}
				}catch (NumberFormatException ex) {
					lblMensaje.setText("ID inválido");
				}catch (Exception ex) {
					lblMensaje.setText("Error: " + ex.getMessage());
				}
			}
		});


		//descargar hash
		btnHash.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					int id = Integer.parseInt(idtextField.getText());

					String hash = cliente.obtenerHash(id);

					if(hash!=null) {
						lblMensaje.setText(hash);
					}else {
						lblMensaje.setText("Aplicacion no encontrada");
					}
				}catch (NumberFormatException ex) {
					lblMensaje.setText("ID inválido");
				}catch (Exception ex) {
					lblMensaje.setText("Error: " + ex.getMessage());
				}
			}
		});

		//DESCRIPCION 
		//modificar descripcion (ESTE VA BIEN)
		btnModificar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					int id = Integer.parseInt(idtextField.getText());
					String descripcion = textArea.getText();

					System.out.println("Modificando APK - ID: " + id + ", Descripción: " + descripcion);

					// Usar Web Client para modificar
					Apk apk = cliente.modificarDescripcion(id, descripcion);
					if(apk!=null) {
						textArea.setText("MODIFICACION CORRETA: " + apk.toString());
					}else {
						textArea.setText("ERROR: APK NO MODIFICADA");
					}
				}catch(NumberFormatException ex) {
					lblMensaje.setText("ID inválido");
				}catch (Exception ex) {
					lblMensaje.setText("Error: " + ex.getMessage());
				}

			}
		});

		//añadir descripcion
		btnAñadir.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					int id = Integer.parseInt(idtextField.getText());
					String descripcion = textArea.getText();

					System.out.println("Añadiendo APK - ID: " + id + ", Descripción: " + descripcion);

					Apk apk = cliente.añadirDescripcion(id, descripcion);
					if(apk!=null) {
						textArea.setText("SE HA AÑADIDO CORRECTAMENTE: " + apk.toString());
					}else {
						textArea.setText("ERROR: APK NO MODIFICADA");
					}
				}catch(NumberFormatException ex) {
					lblMensaje.setText("ID inválido");
				}catch(WebClientResponseException.NotFound  ex) {
					lblMensaje.setText("APK NO ENCONTRADA");
				}catch(WebClientResponseException.BadRequest  ex) {
						lblMensaje.setText(ex.getStatusText() + ": NO SE HA INTRODUCIDO DESCRIPCION");
				}catch (Exception ex) {
					lblMensaje.setText("Error: " + ex.getMessage());
				}
			}
		});

		//eliminar descripcion
		btnEliminar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					int id = Integer.parseInt(idtextField.getText());
					String descripcion = textArea.getText();

					boolean eliminado = cliente.eliminarDescripcion(id);
					if(eliminado) {
						textArea.setText("ELIMINACION CORRETA");
					}else {
						textArea.setText("ERROR: APK NO MODIFICADA" );
					}
				}catch(NumberFormatException ex) {
					lblMensaje.setText("ID inválido");
				}catch (Exception ex) {
					lblMensaje.setText("Error: " + ex.getMessage());
				}
			}
		});

	}

	// Método para escalar ImageIcon
	private ImageIcon scaleImageIcon(ImageIcon icon, int maxWidth, int maxHeight) {
		java.awt.Image img = icon.getImage();
		int originalWidth = icon.getIconWidth();
		int originalHeight = icon.getIconHeight();

		// Calcular nuevo tamaño manteniendo proporción
		double widthRatio = (double) maxWidth / originalWidth;
		double heightRatio = (double) maxHeight / originalHeight;
		double ratio = Math.min(widthRatio, heightRatio);

		int newWidth = (int) (originalWidth * ratio);
		int newHeight = (int) (originalHeight * ratio);

		java.awt.Image scaledImg = img.getScaledInstance(newWidth, newHeight, java.awt.Image.SCALE_SMOOTH);
		return new ImageIcon(scaledImg);
	}


}
