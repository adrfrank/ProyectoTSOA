package sistemaDistribuido.visual.util;

import java.awt.BorderLayout;
import java.awt.Label;
import java.awt.List;
import java.awt.Panel;
import java.util.Enumeration;


import sistemaDistribuido.sistema.clienteServidor.modoUsuario.RegistroServidor;
import sistemaDistribuido.sistema.clienteServidor.modoUsuario.ServidorNombres;
import sistemaDistribuido.sistema.clienteServidor.modoUsuario.ServidorNombresEvent;
import sistemaDistribuido.sistema.clienteServidor.modoUsuario.ServidorNombresListener;
import sistemaDistribuido.util.Escribano;

public class PanelServidores extends Panel implements ServidorNombresListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	List lstServidores;
	Escribano escribano;
	Label titulo;
	public PanelServidores(Escribano esc){
		escribano = esc;
		ServidorNombres.getInstance().addServidorNombresListener(this);
		lstServidores = new List();
		lstServidores.setEnabled(false);
		titulo = new Label("Servidores: ");
		//lstServidores.setSize(200,100);
		updateList();
		this.setLayout(new BorderLayout());
		add(lstServidores,"Center");
		add(titulo,"North");
	}
	
	void updateList(){		
		Enumeration<RegistroServidor> keys = ServidorNombres
				.getInstance()
				.getServidores()
				.elements();
		String txt = "";
		lstServidores.removeAll();
		while(keys.hasMoreElements()){
			txt = "";
			RegistroServidor rs = keys.nextElement();
			txt += rs;
			lstServidores.add(txt);
		}
		
	}
	
	@Override
	public void listUpdated(ServidorNombresEvent evt) {
		// TODO Auto-generated method stub
		if(evt.getTipo().equals(ServidorNombresEvent.REGISTRO)){
			escribano.imprimeln("Servidor registrado: "+ evt.getServidor());
		}else if(evt.getTipo().equals(ServidorNombresEvent.DEREGISTRO)){
			escribano.imprimeln("Servidor deregistrado: "+evt.getServidor());
		}
		updateList();
	}

	
}
