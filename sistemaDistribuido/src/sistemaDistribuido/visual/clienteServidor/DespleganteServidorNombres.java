package sistemaDistribuido.visual.clienteServidor;

import sistemaDistribuido.sistema.clienteServidor.modoMonitor.ParMaquinaProceso;

public interface DespleganteServidorNombres {

	public void registrarServidor(String nombre,ParMaquinaProceso asa);
	
	public void deregistrarServidor(String nombre, ParMaquinaProceso asa);
	
//	public ParMaquinaProceso buscarServidor(String nombre);
}
