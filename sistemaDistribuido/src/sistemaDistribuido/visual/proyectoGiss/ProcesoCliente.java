package sistemaDistribuido.visual.proyectoGiss;
import sistemaDistribuido.sistema.clienteServidor.modoUsuario.*;
import java.util.Arrays;
import sistemaDistribuido.sistema.clienteServidor.modoMonitor.Nucleo;
import sistemaDistribuido.sistema.clienteServidor.modoUsuario.Proceso;
import sistemaDistribuido.util.Escribano;

/**
 *  Laura Gissel Barreto Siordia
 * D05
 * Practica 1
 */
public class ProcesoCliente extends Proceso{
    
        int opcion;
        String msj,respf;

	
	public ProcesoCliente(Escribano esc){
		super(esc);
		start();
	}

	public void run(){
                imprimeln("Inicio de proceso");
		imprimeln("Proceso cliente Giss en ejecucion.");
		imprimeln("Esperando datos para continuar.");
		Nucleo.suspenderProceso();
		imprimeln("Generando mensaje a ser enviado, llenando los campos necesarios");
		byte[] solCliente=new byte[1024];
                solCliente[9]=(byte) opcion;
                solCliente[10]= (byte) msj.length();
                byte[] arreglomsj = new byte[msj.length()];
                arreglomsj=msj.getBytes();
                System.arraycopy(arreglomsj,0,solCliente,11,msj.length());
		byte[] respCliente=new byte[1024];
		//byte dato;
		//solCliente[0]=(byte)10;
                imprimeln("Señalamiento al nucleo para envio de mensajes");
		//Nucleo.send(248,solCliente); esto enviaba el 248 y ahora tu direccionamiento sera servidor de nombres por lo que necesita una cadena
                Nucleo.send(ProcesoServidor.ServerName, solCliente);
                imprimeln("Invocando a receive()");
		Nucleo.receive(dameID(),respCliente);
                imprimeln("Procesando respuesta recibida del servidor");
            //dato=respCliente[0];
                respf = new String(respCliente,10,respCliente[9]);
		imprimeln("Resultado de la operacion: "+respf);
	}
        
        public void CODOP(int opc, String mensaje)
        {
            opcion=opc;
            msj=mensaje;
        }
        
}
