/*
 * Laura Teresa García López    212354614
 * Sección: D05
 * Practica Java #4
 */

package sistemaDistribuido.sistema.rpc.modoUsuario;

import java.util.Enumeration;
import sistemaDistribuido.visual.rpc.DespleganteConexiones;
import java.util.Hashtable;
import java.util.Set;
import java.util.Iterator;
import sistemaDistribuido.sistema.clienteServidor.modoMonitor.ParMaquinaProceso;

public class ProgramaConector{
	private DespleganteConexiones desplegante;
	private Hashtable<Integer,TipoServidor> conexiones;   //las llaves que provee DespleganteConexiones

	/**
	 * 
	 */
	public ProgramaConector(DespleganteConexiones desplegante){
		this.desplegante=desplegante;
	}

	/**
	 * Inicializar tablas en programa conector
	 */
	public void inicializar(){
		conexiones=new Hashtable<Integer,TipoServidor>();
	}

	/**
	 * Remueve tuplas visualizadas en la interfaz gr�fica registradas en tabla conexiones
	 */
	private void removerConexiones(){
		Set<Integer> s=conexiones.keySet();
		Iterator<Integer> i=s.iterator();
		while(i.hasNext()){
			desplegante.removerServidor(((Integer)i.next()).intValue());
			i.remove();
		}
	}

	/**
	 * Al solicitar que se termine el proceso, por si se implementa como tal
	 */
	public void terminar() {
		removerConexiones();
		desplegante.finalizar();
	}

    public int registro(String nombreServidor, String version, ParMaquinaProceso asa) {
        int idunico= desplegante.agregarServidor(nombreServidor, version, asa.dameIP(), String.valueOf(asa.dameID()));
        TipoServidor ts = new TipoServidor(nombreServidor,version,asa);
        conexiones.put(idunico, ts);
        return idunico;
    }

    public boolean deregistro(String nombreServidor, String version, int identificacionUnica) {
        TipoServidor ts = conexiones.get(identificacionUnica);
        if(ts!=null){
            if(ts.dameServidor().equalsIgnoreCase(nombreServidor)){
                if(ts.dameVersion().equalsIgnoreCase(version)){
                    desplegante.removerServidor(identificacionUnica);
                    conexiones.remove(identificacionUnica);
                    return true;
                }
                return false;
            }
            return false;
        }
        return false;
    }

    public ParMaquinaProceso busqueda(String nombreServidor, String version) {
        
        Enumeration elementos = conexiones.elements();
        Hashtable<Integer,TipoServidor> puntosextras = new Hashtable<Integer,TipoServidor>();
        int contServidor=0;
        while(elementos.hasMoreElements()){
           TipoServidor conexion = (TipoServidor) elementos.nextElement();
        if(nombreServidor.equalsIgnoreCase(conexion.dameServidor())){
            if(version.equalsIgnoreCase(conexion.dameVersion())){
                contServidor++;
                puntosextras.put(contServidor,conexion);                
            }
         }  
        }
        if(contServidor > 0){
            int llaverandom = (int)(Math.random() * (contServidor));
            if (llaverandom < 1){
                llaverandom =1;
            }else if (llaverandom > contServidor){
                llaverandom = contServidor;
            }
            return puntosextras.get(llaverandom).dameAsa();
        }else{
            return null;
        }   
    }
}
