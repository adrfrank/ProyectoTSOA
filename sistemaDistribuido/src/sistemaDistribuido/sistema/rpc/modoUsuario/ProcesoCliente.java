/*
 * Laura Teresa García López    212354614
 * Sección: D05
 * Practica Java #3
 */
package sistemaDistribuido.sistema.rpc.modoUsuario;

import java.util.Arrays;
import sistemaDistribuido.sistema.clienteServidor.modoMonitor.Nucleo;
import sistemaDistribuido.sistema.clienteServidor.modoUsuario.Proceso;
import sistemaDistribuido.util.Escribano;

/**
 * 
 */
public class ProcesoCliente extends Proceso{
	private Libreria lib;
        private int cuadrado,raizcuadrada;
        private int [] ordenar, promedio;
	/**
	 * 
	 */
	public ProcesoCliente(Escribano esc){
		super(esc);
		//lib=new LibreriaServidor(esc);  //primero debe funcionar con esta para subrutina servidor local
		lib=new LibreriaCliente(esc);  //luego con esta comentando la anterior, para subrutina servidor remota
		start();
	}

	/**
	 * Programa Cliente
	 */
	public void run(){		

		imprimeln("Proceso cliente en ejecucion.");
		imprimeln("Esperando datos para continuar.");
		Nucleo.suspenderProceso();
		imprimeln("Salio de suspenderProceso");
		int resultado;
                int [] resultadon;                
                resultadon = lib.ordena(ordenar);
                imprimeln("Los numeros ordenados son " + Arrays.toString(resultadon));
                resultado = lib.cuadrado(cuadrado);    
                imprimeln("El cuadrado de  "+cuadrado+" es "+resultado);  
                resultado = lib.promedio(promedio);
                imprimeln("El promedio de  "+Arrays.toString(promedio)+ " es "+resultado);
                resultadon = lib.ordena(ordenar);
                resultado = lib.raiz(raizcuadrada);
                imprimeln("La raiz cuadrada de "+raizcuadrada+" es "+resultado);
		imprimeln("Fin del cliente.");
	}

    public void pasameDatos(String text, String text0, String text1, String text2) {
        String ordena[] = text.split(" ");
         ordenar = new int[ordena.length];
         for(int i = 0;i < ordena.length;i++)
            {
               ordenar[i] = Integer.parseInt(ordena[i]);
            }
        cuadrado = Integer.parseInt(text0);
       
        String prom [] = text1.split(" ");
        promedio = new int[prom.length];
         for(int i = 0;i < prom.length;i++)
            {
               promedio[i] = Integer.parseInt(prom[i]);
            }
        
        raizcuadrada =  Integer.parseInt(text2);        
        
    }
}
