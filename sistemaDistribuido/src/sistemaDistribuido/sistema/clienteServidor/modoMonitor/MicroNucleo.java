/*
 * Laura Teresa García López    212354614
 * Sección: D05
 * Practica Java #2
 */
package sistemaDistribuido.sistema.clienteServidor.modoMonitor;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import sistemaDistribuido.sistema.clienteServidor.modoMonitor.MicroNucleoBase;
import sistemaDistribuido.sistema.clienteServidor.modoUsuario.Proceso;
import sistemaDistribuido.sistema.clienteServidor.modoUsuario.ServidorNombres;
import sistemaDistribuido.util.ConvertidorPaquetes;
import sistemaDistribuido.util.Pausador;

/**
 * 
 */
public final class MicroNucleo extends MicroNucleoBase{
	private static MicroNucleo nucleo=new MicroNucleo();
        Hashtable<Integer,ParMaquinaProceso> TablaEmision=new Hashtable<Integer,ParMaquinaProceso>();
        private Hashtable<Integer,byte []> TablaRecepcion=new Hashtable<Integer,byte []>();
        LinkedList <Remotos> ProcesosRemotos = new LinkedList<Remotos>();
        LinkedList <Locales> ProcesosLocales = new LinkedList<Locales>();
        
	/**
	 * 
	 */
	public MicroNucleo(){
	}

	/**
	 * 
	 */
	public final static MicroNucleo obtenerMicroNucleo(){
		return nucleo;
	}

	/*---Metodos para probar el paso de mensajes entre los procesos cliente y servidor en ausencia de datagramas.
    Esta es una forma incorrecta de programacion "por uso de variables globales" (en este caso atributos de clase)
    ya que, para empezar, no se usan ambos parametros en los metodos y fallaria si dos procesos invocaran
    simultaneamente a receiveFalso() al reescriir el atributo mensaje---*/
	byte[] mensaje;

	public void sendFalso(int dest,byte[] message){
		System.arraycopy(message,0,mensaje,0,message.length);
		notificarHilos();  //Reanuda la ejecucion del proceso que haya invocado a receiveFalso()
	}

	public void receiveFalso(int addr,byte[] message){
		mensaje=message;
		suspenderProceso();
	}
	/*---------------------------------------------------------*/

	/**
	 * 
	 */
	protected boolean iniciarModulos(){
		return true;
	}

	/**
	 * 
	 */
	protected void sendVerdadero(int dest,byte[] message){
		//sendFalso(dest,message);
                int idOrigen = 0, idDestino = 0;
                String ip = null;
                DatagramSocket socketEmison = dameSocketEmision();
                DatagramPacket dp;
                imprimeln("El proceso invocante es el "+super.dameIdProceso());
		ParMaquinaProceso par = TablaEmision.get(dest);
                //ParMaquinaProceso pmp=dameDestinatarioDesdeInterfaz();
                if(par==null)
                {
                   /*Practica2
                   idOrigen = super.dameIdProceso();                   
                   idDestino = pmp.dameID();                  
                   ip = pmp.dameIP();*/    
                    //Practica5
                    Remotos buscar = obtenerProceso(dest);
                    if(buscar != null){
                        idOrigen = super.dameIdProceso();
                        idDestino = buscar.dameID();
                        ip = buscar.dameIP(); 
                        message[0]=(byte) idOrigen;
                        imprimeln("ID ORIGEN :   "+ idOrigen);
                        message[4]= (byte) idDestino;
                        imprimeln("ID DESTINO :   "+ idDestino);
                        try {                        
                            dp = new DatagramPacket(message,message.length,InetAddress.getByName(ip),damePuertoRecepcion());
                            socketEmison.send(dp);
                            //socketEmison.close();
                        } catch (UnknownHostException ex) {
                            Logger.getLogger(MicroNucleo.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (IOException ex) {
                            Logger.getLogger(MicroNucleo.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        //lo siguiente aplica para la pr�ctica #2
                        imprimeln("Enviando mensaje a IP=   "+ip+" ID=  "+idDestino);
                        //suspenderProceso();   //esta invocacion depende de si se requiere bloquear al hilo de control invocador
                    }else{
                        //MANDAR LSA POR LA RED     
                        imprimeln("Buscando Servidor con servicio: " + dest);
                        //Pausador.pausa(3000);
                        HiloEnviarLSA buscarServidor = new HiloEnviarLSA(super.dameIdProceso(), dest, message);
                        buscarServidor.start();
                    }
                }else{                    
                    TablaEmision.remove(dest);
                    idOrigen = super.dameIdProceso();
                    idDestino = par.dameID();
                    ip = par.dameIP();
                    message[0]=(byte) idOrigen;
                    imprimeln("ID ORIGEN :   "+ idOrigen);
                    message[4]= (byte) idDestino;
                    imprimeln("ID DESTINO :   "+ idDestino);
                    try {                        
                        dp = new DatagramPacket(message,message.length,InetAddress.getByName("localhost"),damePuertoRecepcion());
                        socketEmison.send(dp);
                        //socketEmison.close();
                    } catch (UnknownHostException ex) {
                        Logger.getLogger(MicroNucleo.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(MicroNucleo.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    //lo siguiente aplica para la pr�ctica #2
                    imprimeln("Enviando mensaje a IP=   "+ip+" ID=  "+idDestino);
                    //suspenderProceso();   //esta invocacion depende de si se requiere bloquear al hilo de control invocador
                }
	}

	/**
	 * 
	 */
	protected void receiveVerdadero(int addr,byte[] message){
		//receiveFalso(addr,message);
		//el siguiente aplica para la pr�ctica #2
                TablaRecepcion.put(addr, message);
		suspenderProceso();
	}

	/**
	 * Para el(la) encargad@ de direccionamiento por servidor de nombres en prï¿½ctica 5  
	 */
	protected void sendVerdadero(String dest,byte[] message){
		//sendFalso(dest,message);
		imprimeln("El proceso invocante es el "+super.dameIdProceso());
		println("Buscando en servidor de nombres el par (mï¿½quina, proceso) que corresponde al parï¿½metro dest de la llamada a send");
		ParMaquinaProceso pmp = ServidorNombres.getInstance().buscarServidor(dest);

		if(pmp == null){
			println("La solicitud del proceso "+super.dameIdProceso()+" no puede ser atendida");
			Proceso p = super.dameProcesoLocal(super.dameIdProceso());
			p.println("La solicitud no puede ser atendida");
			this.reanudarProceso(p);				
		}else{
			TablaEmision.put(pmp.dameID(), pmp);	


			imprimeln("Completando campos de encabezado del mensaje a ser enviado");
			ConvertidorPaquetes solicitud = new ConvertidorPaquetes(message);
			solicitud.setReceptor(pmp.dameID());
			solicitud.setEmisor(super.dameIdProceso());
			DatagramSocket socketEmision;
			DatagramPacket dp;
			println("Origen empaquetado: "+solicitud.getEmisor());
			println("Destino empaquetado: "+solicitud.getReceptor());
			try{
				socketEmision=dameSocketEmision();  
				println("Socket obtenido");
				dp=new DatagramPacket(message,message.length,InetAddress.getByName(pmp.dameIP()),damePuertoRecepcion());
				imprimeln("Enviando mensaje a IP="+pmp.dameIP()+" ID="+pmp.dameID());
				socketEmision.send(dp);			
				println("Enviado");
			}catch(SocketException e){
				println("Error iniciando socket: "+e.getMessage());
			}catch(UnknownHostException e){
				println("UnknownHostException: "+e.getMessage());
			}catch(IOException e){
				println("IOException: "+e.getMessage());
			}
			//no descomentar la sig. linea en la pÅ•actica 2
			//suspenderProceso();   //esta invocacion depende de si se requiere bloquear al hilo de control invocador
		}

	}

	/**
	 * Para el(la) encargad@ de primitivas sin bloqueo en pr�ctica 5
	 */
	protected void sendNBVerdadero(int dest,byte[] message){
	}

	/**
	 * Para el(la) encargad@ de primitivas sin bloqueo en pr�ctica 5
	 */
	protected void receiveNBVerdadero(int addr,byte[] message){
	}
	/**
	 * 
	 */
	public void run(){
            DatagramSocket socketReceptor = dameSocketRecepcion();
            DatagramPacket dp; 
            int origen, destino;
            String ip;
            byte [] buffer = new byte[1024];
            dp = new DatagramPacket(buffer, buffer.length);
		while(seguirEsperandoDatagramas()){			                  
			try{
                                socketReceptor.receive(dp);
                                origen = buffer[0];
                                imprimeln("Origen:  "+origen);
                                destino = buffer[4];
                                imprimeln("Destino:  "+destino);
                                ip = dp.getAddress().getHostAddress();
                                imprimeln("IP:  "+ip);
                                Proceso procesolocal = dameProcesoLocal(destino);
                                if(buffer[1023]==-1){
                                    //RECIBE UN AU 
                                        /*imprimeln("Proceso local no encontrado, AUSENCIA DE DESTINATARIO");
                                        reanudarProceso(procesolocal);*/
                                        buffer[1023]=(byte)0;                           
                                        // se envia el destino del buffer[4] y se guarda el servicio que daba para buscar uno nuevo con ese servicio
                                        int dest = EliminarServidorRemoto(destino); 
                                        Remotos buscar = obtenerProceso(dest);
                                        if(buscar!=null){
                                            buffer[0]= (byte) origen;
                                            buffer[4]= (byte) buscar.dameID();
                                            dp = new DatagramPacket(buffer,buffer.length,InetAddress.getByName(buscar.dameIP()),damePuertoRecepcion());
                                            socketReceptor.send(dp);
                                        }else{
                                            HiloEnviarLSA buscarServidor = new HiloEnviarLSA(super.dameIdProceso(), dest, buffer);
                                            buscarServidor.start();
                                        }
                                    }
                                else if(buffer[8]==-2){
                                        //LSA
                                        int servicio=(buffer[10]<<8&0x0000FF00)|(buffer[9]&0x000000FF);
                                        imprimeln("Mensaje LSA recibido para " + servicio);
                                        EnviarFSA buscarServidor = new EnviarFSA(servicio,ip);
                                        buscarServidor.start();
                                    }
                                else if(buffer[8]==-3){
                                        //FSA
                                        int servicio=(buffer[10]<<8&0x0000FF00)|(buffer[9]&0x000000FF);
                                        imprimeln("Mensjae FSA recibido para registrar a : "+servicio+" ID: "+buffer[11]+" IP: "+ip);
                                        Remotos nuevo = new Remotos(servicio,buffer[11],ip);
                                        ProcesosRemotos.add(nuevo);       
                                    }                                
                                else if(procesolocal != null){
                                    byte [] esperaRecibir = TablaRecepcion.get(destino);
                                    if(esperaRecibir != null){
                                    CrearOTE cote = new CrearOTE(ip,origen);
                                    TablaEmision.put(origen, cote); 
                                    System.arraycopy(buffer, 0, esperaRecibir, 0, buffer.length);
                                    TablaRecepcion.remove(destino);
                                    reanudarProceso(procesolocal);
                                    }else{ 
                                        // aqui va el try again                                       
                                    }
                                }else{
                                    System.out.println("SI ENTRE AQUI");
                                    //AU                                    
                                    DatagramSocket socketAU = dameSocketRecepcion();
                                    DatagramPacket dpAU; 
                                    /*byte [] bufferAU = new byte[1024];
                                    bufferAU[0]= (byte)0;
                                    bufferAU[4]= (byte)origen;*/
                                    buffer[1023]= (byte)-1; 
                                    try{
                                        dpAU = new DatagramPacket(buffer,buffer.length,InetAddress.getByName(ip),damePuertoRecepcion());
                                        socketAU.send(dpAU);
                                        //socketAU.close();
                                    }catch (UnknownHostException ex) {
                                        Logger.getLogger(MicroNucleo.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                    
                                }
			} catch (IOException ex) {
                    Logger.getLogger(MicroNucleo.class.getName()).log(Level.SEVERE, null, ex);
                }
		}
	}

    private Remotos obtenerProceso(int numServicio) {
    
        if (ProcesosRemotos.isEmpty())
            return null;
        else {
            int numeroServidores = ProcesosRemotos.size();
            for(int i=0; i < numeroServidores; i++){
                if(ProcesosRemotos.get(i).dameServicio() == numServicio){
                    return ProcesosRemotos.get(i);
                }            
            }
        }
       
            return null;
    }
    
    void registroServidor(Locales serLocal){                       
            ProcesosLocales.add(serLocal);                    
        }
    void eliminaServidorLocal(Locales serLocal) {
        Iterator<Locales> auxLocales = ProcesosLocales.iterator();
        Locales aux=null;
        int IDaux=0;
        while(auxLocales.hasNext())
        {
            aux=(Locales) auxLocales.next();
            if(aux.equals(serLocal)){
                aux=ProcesosLocales.get(IDaux);
                break;
            }
            else
                aux=null;
            IDaux++;
        }
        if(aux!=null){
            ProcesosLocales.remove(IDaux);
            imprimeln("Quitando a proceso: "+serLocal.dameID());
        }
        else{
            imprimeln("Error al eliminar proceso: "+serLocal.dameID());
        }
    }

    private int  EliminarServidorRemoto(int id) {
        int dest=0;
        Iterator<Remotos> auxRemotos = ProcesosRemotos.iterator();
        Remotos aux=null;
        int IDaux=0;
        while(auxRemotos.hasNext())
        {
            aux=(Remotos) auxRemotos.next();
            if(aux.dameID()== id){                
                aux=ProcesosRemotos.get(IDaux);
                break;
            }
            else
                aux=null;
            IDaux++;
        }
        if(aux!=null){
            dest = aux.dameServicio();
            ProcesosRemotos.remove(IDaux);
            imprimeln("Quitando a proceso: "+id);
            return dest;
        }
        else{
            imprimeln("Error al eliminar proceso: "+id);
            return 0;
        }
    }

    class EnviarFSA extends Thread{  
        private int servicio;
        private String ip;
       public EnviarFSA(int servicio, String ip){//Recibir num de servicio lsa[9] y e ip averiguada 
           this.servicio = servicio;
           this.ip = ip;           
       }     
    public void run(){
        int numeroServidores = ProcesosLocales.size();
        for(int i=0; i < numeroServidores; i++){
            if(ProcesosLocales.get(i).dameServicio()==servicio){
                DatagramSocket socketEmisionFSA = dameSocketRecepcion();
                byte [] FSA = new byte[1024];
                FSA[8]=-3;
                FSA[9]=(byte) ProcesosLocales.get(i).dameServicio();
                FSA[10]= (byte) (ProcesosLocales.get(i).dameServicio()>>>8);
                FSA[11]= (byte) ProcesosLocales.get(i).dameID();
                try {
                    DatagramPacket dpFSA = new DatagramPacket(FSA,FSA.length,InetAddress.getByName(ip),damePuertoRecepcion());
                    socketEmisionFSA.send(dpFSA);
                    imprimeln("Enviando mensaje FSA");
                } catch (UnknownHostException ex) {
                    Logger.getLogger(MicroNucleo.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(MicroNucleo.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }            
            }
        }
    }
    
    class HiloEnviarLSA extends Thread{
        private int dest;
        private int idOrigen;
        byte[] msg = new byte[1024];
        
        public HiloEnviarLSA(int idOrigen, int dest, byte[]msg){
        this.dest = dest;
        this.idOrigen=idOrigen;
        this.msg = msg;
        }
        public void run(){
            DatagramPacket dpLSA;          
            Remotos buscar; 
            buscar = obtenerProceso(dest);
            for(int i =0; i<3 && buscar == null;i++){
                DatagramSocket socketEmisonLSA = dameSocketEmision();
                byte [] LSA = new byte[1024];
                LSA[8]=(byte)-2;
                LSA[9]=(byte) dest;  
                LSA[10]=(byte) (dest>>8);
                try {
                    dpLSA = new DatagramPacket(LSA,LSA.length,InetAddress.getByName("127.0.0.1"),damePuertoRecepcion());
                                        
                    socketEmisonLSA.send(dpLSA);                    
                   
                    } catch (UnknownHostException ex) {
                        Logger.getLogger(MicroNucleo.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(MicroNucleo.class.getName()).log(Level.SEVERE, null, ex);
                    }
                Pausador.pausa(2000);
                imprimeln("Intento "+(i+1)+ " de paquete LSA");
                
                buscar = obtenerProceso(dest);               
                Pausador.pausa(3000);
            }
            if(buscar!=null){
                try {
                    DatagramSocket socketTemp= dameSocketEmision();
                    DatagramPacket dpEnviar; 
                    msg[0]=(byte) idOrigen;
                    msg[4]= (byte) buscar.dameID(); 
                    dpEnviar = new DatagramPacket(msg,msg.length,InetAddress.getByName(buscar.dameIP()),damePuertoRecepcion());
                    socketTemp.send(dpEnviar);
                } catch (IOException ex) {
                    Logger.getLogger(MicroNucleo.class.getName()).log(Level.SEVERE, null, ex);
                }
            }else {
                imprimeln("No se encontro servidor");
            }
            
            
        }
    }
}
