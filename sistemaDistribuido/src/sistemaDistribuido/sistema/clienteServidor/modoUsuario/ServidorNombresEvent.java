package sistemaDistribuido.sistema.clienteServidor.modoUsuario;

import java.util.Hashtable;


public class ServidorNombresEvent {
	public static final String REGISTRO="registro",DEREGISTRO="deregistro";
	RegistroServidor servidor;	
	Hashtable<Integer,RegistroServidor> servidores;
	String tipo;
	
	public RegistroServidor getServidor() {
		return servidor;
	}
	
	public Hashtable<Integer, RegistroServidor> getServidores() {
		return servidores;
	}
	
	public String getTipo() {
		return tipo;
	}

	
	public ServidorNombresEvent(RegistroServidor servidor, Hashtable<Integer, RegistroServidor> servidores,
			String tipo) {
		super();
		this.servidor = servidor;
		this.servidores = servidores;
		this.tipo = tipo;
	}
	
}
