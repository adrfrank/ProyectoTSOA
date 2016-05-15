package sistemaDistribuido.sistema.clienteServidor.modoUsuario;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import sistemaDistribuido.sistema.clienteServidor.modoMonitor.ParMaquinaProceso;
import sistemaDistribuido.util.Escribano;

public class ServidorNombres {
	static ServidorNombres instance=null;
	public static ServidorNombres getInstance(){
		if(instance == null)
			instance = new ServidorNombres();
		return instance;
	}
	Hashtable<Integer,RegistroServidor> servidores;
	List<ServidorNombresListener> listeners;
	Escribano escribano;
	public Hashtable<Integer, RegistroServidor> getServidores() {
		return servidores;
	}

	int idsec=0;
	public ServidorNombres(){
		servidores = new Hashtable<Integer,RegistroServidor>();
		listeners =new ArrayList<ServidorNombresListener>();
	}

	public void setEscribano(Escribano esc){
		escribano = esc;
	}

	public int registrarServidor(String nombre, ParMaquinaProceso asa){
		RegistroServidor  r = new RegistroServidor();
		r.setNombre(nombre);
		r.setAsa(asa);
		idsec++;
		servidores.put(idsec,r);
		if(escribano != null)
			escribano.imprimeln("Registrando servidor: "+r);
		callListeners(r,ServidorNombresEvent.REGISTRO);
		return idsec;
	}
	public boolean deregistrarServidor(int id){
		RegistroServidor r = servidores.remove(id);
		if(escribano != null)
			escribano.imprimeln("Derregistrando servidor: "+r);
		callListeners(r,ServidorNombresEvent.DEREGISTRO);
		return r==null;		
	}
	public ParMaquinaProceso buscarServidor(String nombre){
		if(escribano != null)
			escribano.imprimeln("Buscando servidor: "+nombre);
		List<RegistroServidor> servs = new ArrayList<RegistroServidor>();
		Enumeration<RegistroServidor> keys = servidores.elements();
		while(keys.hasMoreElements()){
			RegistroServidor rs = keys.nextElement();
			if(rs.nombre.equalsIgnoreCase(nombre)){
				servs.add(rs);
			}
		}
		try {
			if(escribano != null && servs.size()>0)
				escribano.imprimeln(servs.size()+" servidores encontrados");
			ParMaquinaProceso p=servs.get((int)(Math.random()*servs.size())).getAsa();
			if(escribano != null )
				escribano.imprimeln("Enviando asa para servidor con ID de proceso "+p.dameID());		
			return p;
		} catch (Exception e) {
			return null;
		}
	}

	public void addServidorNombresListener(ServidorNombresListener listener){
		listeners.add(listener);
	}

	void callListeners(RegistroServidor servidor,String tipo){
		ServidorNombresEvent evt = new ServidorNombresEvent(servidor,servidores,tipo);
		for(ServidorNombresListener listener : listeners){
			listener.listUpdated(evt);
		}
	}
}

