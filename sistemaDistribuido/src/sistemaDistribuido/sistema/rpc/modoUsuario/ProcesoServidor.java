/*
 * Laura Teresa García López    212354614
 * Sección: D05
 * Practica Java #3
 */
package sistemaDistribuido.sistema.rpc.modoUsuario;

import sistemaDistribuido.sistema.rpc.modoMonitor.RPC;   //para pr�ctica 4
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import sistemaDistribuido.sistema.clienteServidor.modoMonitor.Nucleo;
import sistemaDistribuido.sistema.clienteServidor.modoUsuario.Proceso;
import sistemaDistribuido.util.Escribano;

/**
 * 
 */
public class ProcesoServidor extends Proceso{
	private LibreriaServidor ls;   //para pr�ctica 3

	/**
	 * 
	 */
        private byte[] solServidor=new byte[1024];
	private byte[] respServidor = new byte[1024];
	public ProcesoServidor(Escribano esc){
		super(esc);
		ls=new LibreriaServidor(esc);   //para pr�ctica 3
		start();
	}

	/**
	 * Resguardo del servidor
	 */
	public void run(){
		imprimeln("Proceso servidor en ejecucion.");
                CrearAsa asa = null;
            try {
                asa = new CrearAsa(InetAddress.getLocalHost().getHostAddress(),dameID());
            } catch (UnknownHostException ex) {
                Logger.getLogger(ProcesoServidor.class.getName()).log(Level.SEVERE, null, ex);
            }
		int idUnico=RPC.exportarInterfaz("FileServer", "3.1", asa);  //para pr�ctica 4
		while(continuar()){
                        imprimeln("Invocando a receive.");
			Nucleo.receive(dameID(),solServidor);
                        imprimeln("Procesando petición recibida del cliente");                        
			Desempaquetar();
                        Nucleo.send(solServidor[0],respServidor);
		}

		RPC.deregistrarInterfaz("FileServer", "3.1", idUnico);  //para pr�ctica 4
	}

    private void Desempaquetar() {
       int codop,param,resul;
       int [] parametros, resultado;
       codop= solServidor[8];        
       switch(codop){
           case 0:
               param= ObtenerI(solServidor);
               resul=ls.cuadrado(param);
               Empaqueta(resul,10);
               break;          
           case 1:
               parametros = ObtenerP(solServidor);
               resultado = ls.ordena(parametros);
               Empaqueta(resultado);
               break;
           case 2:
               parametros = ObtenerP(solServidor);
               resul = ls.promedio(parametros);
               Empaqueta(resul,10);
               break;
           case 3:
               param= ObtenerI(solServidor);
               resul=ls.raiz(param);
               Empaqueta(resul,10);
               break;
       }
       
    }

    private int ObtenerI(byte[] a){
       int numero= a[13];
       numero= (numero<<24);
       numero =(numero|(a[12]&0x00FF)<<16);
       numero =(numero|(a[11]&0x00FF)<<8);
       numero =(numero|(a[10]&0x00FF));
      // System.out.println("el numeroooo: " +numero);
       return numero;
    }

    private void Empaqueta(int resul, int i) {    
        respServidor[i] = (byte)resul;
        respServidor[i+1] = (byte)(resul>>>8);
        respServidor[i+2] = (byte)(resul>>>16);
        respServidor[i+3] = (byte)(resul>>>24);
        //System.out.println(Arrays.toString(respServidor));
    }

    private int[] ObtenerP(byte[] solServidor) {
        int cuantos=solServidor[9];
        int num=(cuantos*4)+9;    
        //System.out.println("CUANTOS: "+ cuantos+"  num: "+num);
        int [] parametros= new int [cuantos];
            for(int i =0; i<cuantos;i++){

                int numero= solServidor[num];
                numero= (numero<<24);
                num--;
                numero =(numero|(solServidor[num]&0x00FF)<<16);
                num--;
                numero =(numero|(solServidor[num]&0x00FF)<<8);
                num--;
                numero =(numero|(solServidor[num]&0x00FF));
                num--;
                parametros[i]= numero;
            }
           //System.out.println("DEL SERVIDOR"+Arrays.toString(parametros));
            return parametros;
    
    }

    private void Empaqueta(int[] resultado) {
        int inicio = 10;
        int cuantos=solServidor[9];
        for(int i =0; i<cuantos;i++){
            
            respServidor[inicio] = (byte)resultado[i];
            inicio++;
            respServidor[inicio] = (byte)(resultado[i]>>>8);
            inicio++;
            respServidor[inicio] = (byte)(resultado[i]>>>16);
            inicio++;
            respServidor[inicio] = (byte)(resultado[i]>>>24);
            inicio++;
        }        
    
    }

   
}
