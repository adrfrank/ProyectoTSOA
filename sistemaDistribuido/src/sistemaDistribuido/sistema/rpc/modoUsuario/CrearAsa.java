/*
 * Laura Teresa García López    212354614
 * Sección: D05
 * Practica Java #4
 */
package sistemaDistribuido.sistema.rpc.modoUsuario;

import sistemaDistribuido.sistema.clienteServidor.modoMonitor.ParMaquinaProceso;

/**
 *
 * @author USUARIO
 */
class CrearAsa implements ParMaquinaProceso{
    private String ip;
    private int origen;
    
    CrearAsa(String ip, int origen) {
        this.origen = origen;
        this.ip = ip;
    }    
    
    public String dameIP() {    
        return ip; 
    }

    public int dameID() {
        return origen;    
    }
    
}
