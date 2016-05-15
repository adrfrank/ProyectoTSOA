/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sistemaDistribuido.sistema.clienteServidor.modoMonitor;

/**
 *
 * @author USUARIO
 */
class Remotos {
    private int servicio;
    private int id;
    private String ip;
    
    public Remotos(int servicio, int id, String ip){
        this.servicio = servicio;
        this.id = id;
        this.ip = ip;
    }
    public String dameIP(){
        return ip;
    }
    
    public int dameServicio(){
        return servicio;
    }
    
    public  int dameID(){
        return id;
    }
}
