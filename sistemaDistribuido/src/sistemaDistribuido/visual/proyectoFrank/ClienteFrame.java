package sistemaDistribuido.visual.proyectoFrank;

import sistemaDistribuido.sistema.clienteServidor.modoMonitor.Nucleo;
import sistemaDistribuido.visual.clienteServidor.MicroNucleoFrame;
import sistemaDistribuido.visual.clienteServidor.ProcesoFrame;
import java.awt.Label;
import java.awt.TextField;
import java.awt.Choice;
import java.awt.Button;
import java.awt.Panel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ClienteFrame extends ProcesoFrame{
	private static final long serialVersionUID=1;
	private ProcesoCliente proc;
	private Choice codigosOperacion;
	private TextField campoMensaje;
	private Button botonSolicitud;
	private String codop1,codop2,codop3,codop4;

	public ClienteFrame(MicroNucleoFrame frameNucleo){
		super(frameNucleo,"Cliente de Archivos Test");
		add("South",construirPanelSolicitud());
		validate();
		proc=new ProcesoCliente(this);
		fijarProceso(proc);
	}

	public Panel construirPanelSolicitud(){
		Panel p=new Panel();
		codigosOperacion=new Choice();
		codop1="Crear";
		codop2="Eliminar";
		codop3="Leer";
		codop4="Escribir";
		codigosOperacion.add(codop1);
		codigosOperacion.add(codop2);
		codigosOperacion.add(codop3);
		codigosOperacion.add(codop4);
		campoMensaje=new TextField(10);
		botonSolicitud=new Button("Solicitar");
		botonSolicitud.addActionListener(new ManejadorSolicitud());
		p.add(new Label("Operacion:"));
		p.add(codigosOperacion);
		p.add(new Label("Datos:"));
		p.add(campoMensaje);
		p.add(botonSolicitud);
		return p;
	}

	class ManejadorSolicitud implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			String com=e.getActionCommand();
			if (com.equals("Solicitar")){
				botonSolicitud.setEnabled(false);
				com=codigosOperacion.getSelectedItem();
				short code = (short)codigosOperacion.getSelectedIndex();
				imprimeln("Solicitud a enviar: "+com);
				imprimeln("Mensaje a enviar: "+campoMensaje.getText());
				proc.sendMessage(code,campoMensaje.getText());
				Nucleo.reanudarProceso(proc);
				botonSolicitud.setEnabled(true);
			}
		}
	}
}
