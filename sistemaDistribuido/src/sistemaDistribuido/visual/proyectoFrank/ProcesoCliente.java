package sistemaDistribuido.visual.proyectoFrank;

import sistemaDistribuido.sistema.clienteServidor.modoMonitor.Nucleo;
import sistemaDistribuido.sistema.clienteServidor.modoUsuario.Proceso;
import sistemaDistribuido.util.ConvertidorPaquetes;
import sistemaDistribuido.util.Escribano;

/**
 * 
 */
public class ProcesoCliente extends Proceso{
	String msg;
	short code;
	/**
	 * 
	 */
	public ProcesoCliente(Escribano esc){
		super(esc);
		start();
	}

	public void sendMessage(Short code, String msg){
		this.msg = msg;
		this.code = code;
	}
	/**
	 * 
	 */
	public void run(){
		imprimeln("Inicio de proceso");
		imprimeln("Proceso cliente Frank en ejecucion.");
		imprimeln("Esperando datos para continuar.");
		while(continuar()){
			Nucleo.suspenderProceso();
			//imprimeln("Hola =)");
			byte[] solCliente=new byte[ConvertidorPaquetes.SOL_LENGTH];
			byte[] respCliente=new byte[ConvertidorPaquetes.SOL_LENGTH];
			ConvertidorPaquetes sol = new ConvertidorPaquetes(solCliente);
			
			imprimeln("Generando mensaje a ser enviado, llenando los campos necesarios");
			sol.setOptCode((short)code);
			sol.setData(msg==null?"":msg);
			imprimeln("Señalamiento al núcleo para envío de mensaje");
			//Nucleo.send(248,solCliente);
			Nucleo.send(ProcesoServidor.ServerName, solCliente);
			imprimeln("Invocando a receive()");
			Nucleo.receive(dameID(),respCliente);
			imprimeln("Procesando respuesta recibida del servidor");
			ConvertidorPaquetes resp = new ConvertidorPaquetes(respCliente);
			println("Codigo: "+resp.getOptCode());
			println("Origen: "+resp.getEmisor());
			println("Destino: "+resp.getReceptor());
			if(resp.getOptCode() == -1){
				println("Servidor envioa TA");
			}if(resp.getOptCode() == -2){
				println("Servidor envioa AU");
				
			}else{
				String dato = resp.getStringData();
				imprimeln("Proceso de la operaci�n: "+dato);
				
			}
			
		}
		
	}
}
