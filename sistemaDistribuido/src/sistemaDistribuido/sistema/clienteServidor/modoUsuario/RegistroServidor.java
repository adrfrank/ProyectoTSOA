package sistemaDistribuido.sistema.clienteServidor.modoUsuario;

import sistemaDistribuido.sistema.clienteServidor.modoMonitor.ParMaquinaProceso;

public class RegistroServidor{
	String nombre;
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public ParMaquinaProceso getAsa() {
		return asa;
	}
	public void setAsa(ParMaquinaProceso asa) {
		this.asa = asa;
	}
	ParMaquinaProceso asa;
	
	public String toString(){
		return getAsa().dameID()+" | "
				+getAsa().dameIP() +" | "
				+getNombre();
	}
}