/*
 * Laura Teresa García López    212354614
 * Sección: D05
 * Practica Java #3
 */

package sistemaDistribuido.sistema.rpc.modoUsuario;

import sistemaDistribuido.sistema.rpc.modoMonitor.RPC;  //para pr�ctica 4
import java.util.Arrays;
import sistemaDistribuido.sistema.clienteServidor.modoMonitor.Nucleo;
import sistemaDistribuido.sistema.rpc.modoUsuario.Libreria;
import sistemaDistribuido.util.Escribano;

public class LibreriaCliente extends Libreria{
        final int CUADRADO=0, RAIZ=3, ORDENA=1, PROMEDIO=2;
        private byte[] solCliente = new byte[1024];
        private byte[] respCliente = new byte[1024];
	/**
	 * 
	 */
	public LibreriaCliente(Escribano esc){
		super(esc);
	}

	/**
	 * Ejemplo de resguardo del cliente suma
	 */
	protected void suma(){
		int asaDest=0;
		//...

		asaDest=RPC.importarInterfaz("FileServer", "3.1");  //para pr�ctica 4
		Nucleo.send(asaDest,null);
		//...
	}

    @Override
    protected void cuadrado() {
        int asaDest = RPC.importarInterfaz("FileServer", "3.1");
        int parametro = (Integer) pila.pop();        
        solCliente[8]=CUADRADO;
        Empaquetar(parametro,10); 
        Nucleo.send(asaDest, solCliente);
        Nucleo.receive(Nucleo.dameIdProceso(), respCliente);
        int resultado = Desempaqueta();
        pila.push(resultado);
    }

    private void Empaquetar(int parametro, int i) {        
        solCliente[i] = (byte)parametro;
        solCliente[i+1] = (byte)(parametro>>>8);
        solCliente[i+2] = (byte)(parametro>>>16);
        solCliente[1+3] = (byte)(parametro>>>24);    
    }

    private int Desempaqueta() {
       int numero= respCliente[13];
       numero= (numero<<24);
       numero =(numero|(respCliente[12]&0x00FF)<<16);
       numero =(numero|(respCliente[11]&0x00FF)<<8);
       numero =(numero|(respCliente[10]&0x00FF));
       return numero;
    }

    @Override
    protected void raiz() {
        int asaDest = RPC.importarInterfaz("FileServer", "3.1");
        int parametro = (Integer) pila.pop();        
        solCliente[8]=RAIZ;
        Empaquetar(parametro,10);    
        //System.out.println("solCLiente"+ Arrays.toString(solCliente));
        Nucleo.send(asaDest, solCliente);
        Nucleo.receive(Nucleo.dameIdProceso(), respCliente);
        //System.out.println("respCLiente"+ Arrays.toString(respCliente));
        int resultado = Desempaqueta();
        pila.push(resultado);
    }

    @Override
    protected void ordena() {
        int asaDest = RPC.importarInterfaz("FileServer", "3.1");
        int cuantos;
        int [] parametros = (int []) pila.pop();
        cuantos = parametros.length;
        solCliente[8]=ORDENA;
        Empaquetar(parametros,cuantos);
        Nucleo.send(asaDest, solCliente);
        Nucleo.receive(Nucleo.dameIdProceso(), respCliente);
        int [] resultado = DesempaquetaP();
        pila.push(resultado);
    
    }

    private void Empaquetar(int[] parametros, int cuantos) {        
        int inicio = 10;
        solCliente[9]=(byte) cuantos;
        for(int i =0; i<cuantos;i++){
            
            solCliente[inicio] = (byte)parametros[i];
            inicio++;
            solCliente[inicio] = (byte)(parametros[i]>>>8);
            inicio++;
            solCliente[inicio] = (byte)(parametros[i]>>>16);
            inicio++;
            solCliente[inicio] = (byte)(parametros[i]>>>24);
            inicio++;
        }        
    }

    private int[] DesempaquetaP() {
    int cuantos = solCliente[9];
    int num=(cuantos*4)+9;  
    int [] parametros= new int [cuantos];
        for(int i =0; i<cuantos;i++){
            
            int numero= respCliente[num];
            numero= (numero<<24);
            num--;
            numero =(numero|(respCliente[num]&0x00FF)<<16);
            num--;
            numero =(numero|(respCliente[num]&0x00FF)<<8);
            num--;
            numero =(numero|(respCliente[num]&0x00FF));
            num--;
            parametros[i]= numero;
        }
        //System.out.println("PARA: "+ Arrays.toString(parametros));
        return parametros;
    }

    @Override
    protected void promedio() {
        int asaDest = RPC.importarInterfaz("FileServer", "3.1");
        int cuantos;
        int [] parametros = (int []) pila.pop();
        cuantos = parametros.length;
        solCliente[8]=PROMEDIO;
        Empaquetar(parametros,cuantos);
        Nucleo.send(asaDest, solCliente);
        Nucleo.receive(Nucleo.dameIdProceso(), respCliente);
        int  resultado = Desempaqueta();
        pila.push(resultado);
    
    }




}