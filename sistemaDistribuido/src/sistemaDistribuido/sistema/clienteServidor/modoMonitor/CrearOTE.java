/*
 * Laura Teresa García López    212354614
 * Sección: D05
 * Practica Java #2
 */
package sistemaDistribuido.sistema.clienteServidor.modoMonitor;

/**
 *
 * @author USUARIO
 */
public class CrearOTE implements ParMaquinaProceso{
    private String ip;
    private int origen;
    CrearOTE(String ip, int origen) {
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
