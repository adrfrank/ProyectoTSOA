package sistemaDistribuido.sistema.clienteServidor.modoMonitor;

public class MaquinaProceso implements ParMaquinaProceso {

	private String ip;
	private int id;
	
	public MaquinaProceso(String ip, int id){
		this.ip= ip;
		this.id = id;
	}
	
	@Override
	public String dameIP() {
		return ip;
	}

	@Override
	public int dameID() {
		return id;
	}

}
