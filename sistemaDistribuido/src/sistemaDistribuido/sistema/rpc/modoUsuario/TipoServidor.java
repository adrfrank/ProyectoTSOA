/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sistemaDistribuido.sistema.rpc.modoUsuario;

import sistemaDistribuido.sistema.clienteServidor.modoMonitor.ParMaquinaProceso;

/**
 *
 * @author USUARIO
 */
class TipoServidor {
    private String nombreServidor;
    private String version;
    private ParMaquinaProceso asa;

    TipoServidor(String nombreServidor, String version, ParMaquinaProceso asa) {
        this.asa = asa;
        this.nombreServidor  = nombreServidor;
        this.version = version;       
    }
    
    public String dameServidor(){
        return nombreServidor;
    }
    
    public String dameVersion(){
        return version;
    }
    
    public  ParMaquinaProceso dameAsa(){
        return asa;
    }
}
