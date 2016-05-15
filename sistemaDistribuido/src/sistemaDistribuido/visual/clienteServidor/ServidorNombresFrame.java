package sistemaDistribuido.visual.clienteServidor;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import microKernelBasedSystem.util.Writer;
import microKernelBasedSystem.util.WriterManager;
import sistemaDistribuido.sistema.clienteServidor.modoUsuario.ServidorNombres;
import sistemaDistribuido.util.Escribano;
import sistemaDistribuido.visual.util.PanelInformador;
import sistemaDistribuido.visual.util.PanelServidores;

public class ServidorNombresFrame extends Frame implements WindowListener,Escribano {
	PanelInformador informador ;
	PanelServidores servidores;

	/**
	 * Servidor de nombres
	 */
	private static final long serialVersionUID = 1L;

	public ServidorNombresFrame(){
		setSize(500, 500);
		setTitle("Servidor de nombres");
		setLayout(new BorderLayout());
		addWindowListener(this);

		informador = new PanelInformador();
		servidores = new PanelServidores(this);
		add(informador,"North");
		add(servidores,"Center");
		ServidorNombres.getInstance().setEscribano(this);
	}


	@Override
	public void print(String s) {
		imprime(s);
	}

	@Override
	public void println(String s) {
		imprimeln(s);
	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub
		finalizar();
	}

	@Override
	public WriterManager getWriterManager() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void appendWriter(Writer w) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void imprime(String s) {
		informador.imprime(s);
	}

	@Override
	public void imprimeln(String s) {
		informador.imprimeln(s);
	}

	@Override
	public void finalizar() {
		
	}

	@Override
	public void windowActivated(WindowEvent e) {
		imprimeln("Ventana de servidor de nombres iniciada");
	}

	@Override
	public void windowClosed(WindowEvent e) {
		
	}

	@Override
	public void windowClosing(WindowEvent e) {
		imprimeln("Cerrando ventana de servidor de nombres");
		this.setVisible(false);
		
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		
	}

	@Override
	public void windowIconified(WindowEvent e) {
		
	}

	@Override
	public void windowOpened(WindowEvent e) {
		imprimeln("Abriendo ventana de servidor de nombres");
	}
}
