package interfaz;

import javax.swing.JFrame;
import java.awt.GridLayout;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Label;

import javax.swing.BoxLayout;
import javax.swing.JTextArea;
import javax.swing.SpringLayout;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JScrollPane;

import camara.CamaraIntServerImpl;

public class CamInterf extends JFrame {
	JTextArea robdata;
	static CamInterf cam;
	static CamaraIntServerImpl camara;
	public CamInterf() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(new GridLayout(1, 0, 0, 0));
		
		JSplitPane splitPane = new JSplitPane();
		splitPane.setDividerSize(5);
		getContentPane().add(splitPane);
		
		JPanel panel = new JPanel();
		splitPane.setRightComponent(panel);
		SpringLayout sl_panel = new SpringLayout();
		panel.setLayout(sl_panel);
		
		JLabel lblUrlCamara = new JLabel("URL Camara");
		sl_panel.putConstraint(SpringLayout.EAST, lblUrlCamara, -76, SpringLayout.EAST, panel);
		panel.add(lblUrlCamara);
		
		JLabel lblEngendra = new JLabel("engendra");
		lblEngendra.setAutoscrolls(true);
		lblEngendra.setBackground(Color.WHITE);
		sl_panel.putConstraint(SpringLayout.SOUTH, lblUrlCamara, -6, SpringLayout.NORTH, lblEngendra);
		sl_panel.putConstraint(SpringLayout.NORTH, lblEngendra, 44, SpringLayout.NORTH, panel);
		lblEngendra.setOpaque(true);
		lblEngendra.setEnabled(false);
		sl_panel.putConstraint(SpringLayout.WEST, lblEngendra, 36, SpringLayout.WEST, panel);
		sl_panel.putConstraint(SpringLayout.EAST, lblEngendra, -26, SpringLayout.EAST, panel);
		lblEngendra.setHorizontalAlignment(SwingConstants.CENTER);
		panel.add(lblEngendra);
		
		JLabel lblNDeRobots = new JLabel("N\u00BA de Robots: ");
		sl_panel.putConstraint(SpringLayout.NORTH, lblNDeRobots, 57, SpringLayout.SOUTH, lblEngendra);
		sl_panel.putConstraint(SpringLayout.WEST, lblNDeRobots, 21, SpringLayout.WEST, panel);
		panel.add(lblNDeRobots);
		
		JLabel lblNDeConsolas = new JLabel("N\u00BA de Consolas: ");
		sl_panel.putConstraint(SpringLayout.NORTH, lblNDeConsolas, 16, SpringLayout.SOUTH, lblNDeRobots);
		sl_panel.putConstraint(SpringLayout.WEST, lblNDeConsolas, 0, SpringLayout.WEST, lblNDeRobots);
		panel.add(lblNDeConsolas);
		
		JLabel lbNoRobs = new JLabel("0");
		lbNoRobs.setOpaque(true);
		lbNoRobs.setBackground(Color.WHITE);
		lbNoRobs.setHorizontalAlignment(SwingConstants.CENTER);
		sl_panel.putConstraint(SpringLayout.NORTH, lbNoRobs, 57, SpringLayout.SOUTH, lblEngendra);
		sl_panel.putConstraint(SpringLayout.WEST, lbNoRobs, -56, SpringLayout.EAST, panel);
		sl_panel.putConstraint(SpringLayout.EAST, lbNoRobs, -26, SpringLayout.EAST, panel);
		panel.add(lbNoRobs);
		
		JLabel lbNoCons = new JLabel("0");
		lbNoCons.setBackground(Color.WHITE);
		lbNoCons.setOpaque(true);
		lbNoCons.setHorizontalAlignment(SwingConstants.CENTER);
		sl_panel.putConstraint(SpringLayout.NORTH, lbNoCons, 16, SpringLayout.SOUTH, lbNoRobs);
		sl_panel.putConstraint(SpringLayout.WEST, lbNoCons, 0, SpringLayout.WEST, lbNoRobs);
		sl_panel.putConstraint(SpringLayout.EAST, lbNoCons, 0, SpringLayout.EAST, lblEngendra);
		panel.add(lbNoCons);
		
		JLabel lblFicheroDeEscenario = new JLabel("Fichero de Escenario");
		sl_panel.putConstraint(SpringLayout.NORTH, lblFicheroDeEscenario, 43, SpringLayout.SOUTH, lblNDeConsolas);
		sl_panel.putConstraint(SpringLayout.EAST, lblFicheroDeEscenario, -48, SpringLayout.EAST, panel);
		panel.add(lblFicheroDeEscenario);
		
		JLabel lblEscenario = new JLabel("defecto");
		lblEscenario.setOpaque(true);
		lblEscenario.setHorizontalAlignment(SwingConstants.CENTER);
		lblEscenario.setAutoscrolls(true);
		lblEscenario.setBackground(Color.WHITE);
		sl_panel.putConstraint(SpringLayout.NORTH, lblEscenario, 16, SpringLayout.SOUTH, lblFicheroDeEscenario);
		sl_panel.putConstraint(SpringLayout.WEST, lblEscenario, 0, SpringLayout.WEST, lblEngendra);
		sl_panel.putConstraint(SpringLayout.EAST, lblEscenario, 0, SpringLayout.EAST, lblEngendra);
		panel.add(lblEscenario);
		
		JButton btnCambiarEscenario = new JButton("Cambiar Escenario");
		sl_panel.putConstraint(SpringLayout.NORTH, btnCambiarEscenario, 35, SpringLayout.SOUTH, lblEscenario);
		sl_panel.putConstraint(SpringLayout.WEST, btnCambiarEscenario, 0, SpringLayout.WEST, lblEngendra);
		sl_panel.putConstraint(SpringLayout.EAST, btnCambiarEscenario, 0, SpringLayout.EAST, lblEngendra);
		panel.add(btnCambiarEscenario);
		
		JButton btnSalir = new JButton("Salir");
		btnSalir.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		sl_panel.putConstraint(SpringLayout.NORTH, btnSalir, 6, SpringLayout.SOUTH, btnCambiarEscenario);
		sl_panel.putConstraint(SpringLayout.WEST, btnSalir, 0, SpringLayout.WEST, lblEngendra);
		sl_panel.putConstraint(SpringLayout.EAST, btnSalir, 0, SpringLayout.EAST, lblEngendra);
		panel.add(btnSalir);
		
		robdata = new JTextArea(5,20);
		robdata.setRequestFocusEnabled(false);
		robdata.setEditable(false);
		JScrollPane scrollPane = new JScrollPane(robdata);
		scrollPane.setMaximumSize(new Dimension(250, 23));
		scrollPane.setAutoscrolls(true);
		scrollPane.setMinimumSize(new Dimension(250, 23));
		splitPane.setLeftComponent(scrollPane);
	}
	
	public void AnadirRobs()
	{
		for(String r : camara.obtenerListaRobots())
			robdata.setText(r+"\n");
		cam.repaint();
	}
	
	public static void main(String[] args)
	{
		camara = new CamaraIntServerImpl();
		cam = new CamInterf();
		cam.setSize(500,400);
		cam.setResizable(false);
		cam.setVisible(true);
		cam.AnadirRobs();
	}
}
