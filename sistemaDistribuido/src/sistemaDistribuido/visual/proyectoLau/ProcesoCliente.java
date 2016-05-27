/*
 * Laura Teresa García López    212354614
 * Sección: D05
 * Practica Java #1
 */
package sistemaDistribuido.visual.proyectoLau;

import sistemaDistribuido.sistema.clienteServidor.modoUsuario.*;
import java.util.Arrays;
import sistemaDistribuido.sistema.clienteServidor.modoMonitor.Nucleo;
import sistemaDistribuido.sistema.clienteServidor.modoUsuario.Proceso;
import sistemaDistribuido.util.Escribano;

/**
 * 
 */
public class ProcesoCliente extends Proceso{

	/**
	 * 
	 */
    private int num;
    private String mens;
    private byte[] solCliente=new byte[1024];
    private byte[] respCliente=new byte[1024];
	public ProcesoCliente(Escribano esc){
		super(esc);
		start();
	}

	/**
	 * 
	 */
	public void run(){
		imprimeln("Proceso cliente Lau en ejecucion.");
		imprimeln("Esperando datos para continuar.");
		Nucleo.suspenderProceso();
                imprimeln("Generando mensaje a ser enviado, llenando los campos necesarios");
                Empaquetar();
		Nucleo.send(248,solCliente);
                imprimeln("Invocando a receive.");
		Nucleo.receive(dameID(),respCliente);
                imprimeln("Procesando respuesta recibida del servidor.");
                Desempaquetar();
	}
        
    public void Empaquetar(){
        int size = mens.length();
        solCliente[8]=(byte)num;
        solCliente[10]= (byte)size;
        byte[] codop = mens.getBytes();
        System.arraycopy(codop, 0, solCliente, 11, size);
        //System.out.println(Arrays.toString(solCliente));
    }

    public void pasameDatos(String com, String text) {
       if(com.equals("Crear"))
           num=1;
       if(com.equals("Eliminar"))
           num=2;
       if(com.equals("Leer"))
           num=3;
       if(com.equals("Escribir"))
           num=4;
       mens= text;
    }

    public void Desempaquetar() {
        String cad;
        cad = new String(respCliente,9,respCliente[8]);  
        imprimeln(cad);
    }
}
