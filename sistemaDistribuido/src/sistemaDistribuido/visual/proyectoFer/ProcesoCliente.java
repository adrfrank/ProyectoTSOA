package sistemaDistribuido.visual.proyectoFer;
import sistemaDistribuido.sistema.clienteServidor.modoUsuario.*;
import sistemaDistribuido.sistema.clienteServidor.modoMonitor.Nucleo;
import sistemaDistribuido.sistema.clienteServidor.modoUsuario.Proceso;
import sistemaDistribuido.util.Escribano;

/**
 *  Hector Fernando Gonzalez Trujillo
 * D05
 * Practica 5
 */
public class ProcesoCliente extends Proceso{

	private String codop, mensaje;

	public void recibeDatos(String cod, String msg)
	{
		codop = cod;
		mensaje = msg;
	}



	public ProcesoCliente(Escribano esc){
		super(esc);
		start();
	}


	/**
	 * 
	 */
	public byte[] convToByte(String cad)
	{

		byte[] arreglo = new byte[cad.length()+1];
		arreglo[0] = (byte)cad.length();
		byte[] aux = cad.getBytes();
		System.arraycopy(aux,0,arreglo,1,cad.length());

		return arreglo;
	}

	public void run(){
		imprimeln("Proceso cliente Fer en ejecucion.");
		imprimeln("Esperando datos para continuar.");
		Nucleo.suspenderProceso();
		byte[] solCliente=new byte[1024];
		byte[] respCliente=new byte[1024];

		//Poner el codop en la posicion 8
		if(codop.equals("Crear"))
			solCliente[8]=(byte)0;
		else if(codop.equals("Eliminar"))
			solCliente[8]=(byte)1;
		else if(codop.equals("Leer"))
			solCliente[8]=(byte)2;
		else if(codop.equals("Escribir"))
			solCliente[8]=(byte)3;
		imprimeln("Creando mensaje con los datos");
		System.arraycopy(convToByte(mensaje),0,solCliente,10,mensaje.length()+1);
		imprimeln("Enviando petici�n al nucleo");
		//Nucleo.send(248,solCliente);
                Nucleo.send(ProcesoServidor.ServerName, solCliente);
		imprimeln("Invocando receive");
		Nucleo.receive(dameID(),respCliente);
		imprimeln("Recibiendo mensaje por la red");

		String aux = new String(respCliente,9,respCliente[8]);
		if (aux.length()==0)
			imprimeln("Address Unknown");
		else
			imprimeln("el servidor me envi� un "+aux);

	}
}
