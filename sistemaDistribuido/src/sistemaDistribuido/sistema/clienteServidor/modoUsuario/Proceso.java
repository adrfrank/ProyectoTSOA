package sistemaDistribuido.sistema.clienteServidor.modoUsuario;


import java.net.InetAddress;
import java.net.UnknownHostException;
import microKernelBasedSystem.system.clientServer.userMode.threadPackage.SystemProcess;
import sistemaDistribuido.sistema.clienteServidor.modoMonitor.MaquinaProceso;
import sistemaDistribuido.sistema.clienteServidor.modoMonitor.Nucleo;
import sistemaDistribuido.sistema.clienteServidor.modoMonitor.MicroNucleo;
import sistemaDistribuido.sistema.clienteServidor.modoMonitor.ParMaquinaProceso;
import sistemaDistribuido.util.Escribano;

/**
 * 
 */
public abstract class Proceso extends SystemProcess{
	protected MicroNucleo nucleo;

	/**
	 * 
	 */
	public Proceso(Escribano esc){
		super(Nucleo.nucleo,esc);
		this.nucleo=Nucleo.nucleo;
	}

	/**
	 * Solo para compatibilidad con versiones 2007B y anteriores, no necesario de 2008A en adelante
	 */
	public Proceso(MicroNucleo nucleo,Escribano esc){
		this(esc);
		start();
	}
	/**
	 * 
	 */
	protected void imprime(String s){
		super.print(s);
	}

	/**
	 * 
	 */
	protected void imprimeln(String s){
		super.println(s);
	}

	/**
	 * 
	 */
	public final int dameID(){
		return (int)super.getID();
	}

	/**
	 * 
	 */
	public final boolean continuar(){
		return super.continueExecuting();
	}

	/**
	 * Solo para compatibilidad con versiones 2007B y anteriores, no necesario de 2008A en adelante
	 */
	public void terminar(){
	}

	/**
	 * Actividad normal del proceso mientras est� activo
	 */
	public void run(){
	}

	/**
	 * Actividades a realizar tras recibir la se�al de terminaci�n del proceso
	 */
	protected void shutdown(){
		terminar();
	}
        
        public ParMaquinaProceso dameMaquinaProceso(){
		MaquinaProceso mp=null;
		try {
			int id = this.dameID();
			String ip = InetAddress.getLocalHost().getHostAddress();
			mp = new MaquinaProceso(ip,id);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return mp;
		
		
	}
}
