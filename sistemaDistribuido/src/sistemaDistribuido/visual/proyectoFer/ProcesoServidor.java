package sistemaDistribuido.visual.proyectoFer;

import sistemaDistribuido.sistema.clienteServidor.modoUsuario.*;

import sistemaDistribuido.sistema.clienteServidor.modoMonitor.Nucleo;
import sistemaDistribuido.sistema.clienteServidor.modoUsuario.Proceso;
import sistemaDistribuido.util.Escribano;
import sistemaDistribuido.util.Pausador;
import sistemaDistribuido.sistema.clienteServidor.modoUsuario.ServidorNombres;

/**
 *  Hector Fernando Gonzalez Trujillo
 * D05
 * Practica 5
 */
public class ProcesoServidor extends Proceso{

	/**
	 * 
	 */
    
	public ProcesoServidor(Escribano esc){
		super(esc);
		start();
	}

	/**
	 * 
	 */

	private String nombreArchivo;
	private int origen;
        public static final String ServerName = "FileServerFer";
	public byte[] convToByte(String cad)
	{

		byte[] arreglo = new byte[cad.length()+1];
		arreglo[0] = (byte)cad.length();
		byte[] aux = cad.getBytes();
		System.arraycopy(aux,0,arreglo,1,cad.length());

		return arreglo;
	}

	public void desempaqueta(byte[] solServidor)
	{

		nombreArchivo = new String(solServidor,11,solServidor[10]);
		origen = solServidor[0];
	}
	public void run(){
		imprimeln("Proceso servidor Fer en ejecucion.");
		byte[] solServidor=new byte[1024];
		byte[] respServidor =new byte[1024];
                int id = ServidorNombres.getInstance().registrarServidor(ServerName, this.dameMaquinaProceso());
		byte opc;
		String resp = "";
		while(continuar()){
			Nucleo.receive(dameID(),solServidor);
			imprimeln("Recibiendo mensaje por la red");
			opc = solServidor[8];
			switch (opc)
			{
				case 0:
					desempaqueta(solServidor);
					resp="Archivo creado con el nombre: "+ nombreArchivo;
					imprimeln("Se recibio peticion de creacion");
					break;
				case 1:
					desempaqueta(solServidor);
					resp="Archivo con el nombre: "+nombreArchivo +" fue eliminado";
					imprimeln("Se recibio peticion de eliminacion");
					break;
				case 2:
					desempaqueta(solServidor);
					resp="Archivo con el nombre: "+nombreArchivo+" ha sido leido";
					imprimeln("Se recibio peticion de lectura");
					break;
				case 3:
					desempaqueta(solServidor);
					resp="Se ha escrito en archivo con nombre: "+nombreArchivo;
					imprimeln("Se recibio peticion de escritura");
					break;
			}
			imprimeln("Creando respuesta");
			System.arraycopy(convToByte(resp),0,respServidor,8,resp.length()+1);
			respServidor[4]=(byte)origen;
			respServidor[0]=solServidor[4];

			Pausador.pausa(5000);  //sin esta l�nea es posible que Servidor solicite send antes que Cliente solicite receive
			imprimeln("enviando respuesta a "+origen);
			Nucleo.send(origen,respServidor);
			imprimeln("Fin de peticion");
		}
            ServidorNombres.getInstance().deregistrarServidor(id);
	}
}
