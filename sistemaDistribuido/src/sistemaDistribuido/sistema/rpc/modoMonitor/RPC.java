/*
 * Laura Teresa García López    212354614
 * Sección: D05
 * Practica Java #4
 */

package sistemaDistribuido.sistema.rpc.modoMonitor;

import sistemaDistribuido.sistema.clienteServidor.modoMonitor.MicroNucleo;
import sistemaDistribuido.sistema.clienteServidor.modoMonitor.Nucleo;   //para pr�ctica 4
import sistemaDistribuido.sistema.clienteServidor.modoMonitor.ParMaquinaProceso;
import sistemaDistribuido.sistema.rpc.modoUsuario.ProgramaConector;

public class RPC{
	private static ProgramaConector conector;

	/**
	 * 
	 */
	public static void asignarConector(ProgramaConector con){
		conector=con;
		conector.inicializar();
	}

	/**
	 * Efectua la llamada de busqueda en el conector.
	 * Regresa un dest para la llamada a send(dest,message).
	 */
	public static int importarInterfaz(String nombreServidor,String version){
		ParMaquinaProceso asa= conector.busqueda(nombreServidor, version);
                if(asa == null){
                    return -1;
                }
                else{                    
                    Nucleo.InsertarTE(asa.dameID(),asa);
                    return asa.dameID();
                }
	}

	/**
	 * Efectua la llamada a registro en el conector.
	 * Regresa una identificacionUnica para el deregistro.
	 */
	public static int exportarInterfaz(String nombreServidor,String version,ParMaquinaProceso asa){
		
            return conector.registro(nombreServidor,version,asa);
	}

	/**
	 * Efectua la llamada a deregistro en el conector.
	 * Regresa el status del deregistro, true significa llevado a cabo.
	 */
	public static boolean deregistrarInterfaz(String nombreServidor,String version,int identificacionUnica){
            
		return conector.deregistro(nombreServidor,version,identificacionUnica);
	}
}