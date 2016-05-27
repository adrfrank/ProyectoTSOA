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
public final class MicroNucleo extends MicroNucleoBase {

    private static MicroNucleo nucleo = new MicroNucleo();
    Hashtable<Integer, ParMaquinaProceso> TablaEmision = new Hashtable<Integer, ParMaquinaProceso>();
    private Hashtable<Integer, byte[]> TablaRecepcion = new Hashtable<Integer, byte[]>();
    LinkedList<Remotos> ProcesosRemotos = new LinkedList<Remotos>();
    LinkedList<Locales> ProcesosLocales = new LinkedList<Locales>();
        //****************************************************************************************
    //Agregado para almacenamiento
    Hashtable<Integer, LinkedList<byte[]>> TB = new Hashtable<Integer, LinkedList<byte[]>>();
    private TablaMsgInesperados tablaMsgInesperados = new TablaMsgInesperados();

    /**
     *
     */
    public MicroNucleo() {
    }

    /**
     *
     */
    public final static MicroNucleo obtenerMicroNucleo() {
        return nucleo;
    }

    /*---Metodos para probar el paso de mensajes entre los procesos cliente y servidor en ausencia de datagramas.
     Esta es una forma incorrecta de programacion "por uso de variables globales" (en este caso atributos de clase)
     ya que, para empezar, no se usan ambos parametros en los metodos y fallaria si dos procesos invocaran
     simultaneamente a receiveFalso() al reescriir el atributo mensaje---*/
    byte[] mensaje;

    public void sendFalso(int dest, byte[] message) {
        System.arraycopy(message, 0, mensaje, 0, message.length);
        notificarHilos();  //Reanuda la ejecucion del proceso que haya invocado a receiveFalso()
    }

    public void receiveFalso(int addr, byte[] message) {
        mensaje = message;
        suspenderProceso();
    }
    /*---------------------------------------------------------*/

    /**
     *
     */
    protected boolean iniciarModulos() {
        return true;
    }

    /**
     *
     */
    protected void sendVerdadero(int dest, byte[] message) {
        //sendFalso(dest,message);
        int idOrigen = 0, idDestino = 0;
        String ip = null;
        DatagramSocket socketEmison = dameSocketEmision();
        DatagramPacket dp;
        imprimeln("El proceso invocante es el " + super.dameIdProceso());
        ParMaquinaProceso par = TablaEmision.get(dest);
        //ParMaquinaProceso pmp=dameDestinatarioDesdeInterfaz();
        if (par == null) {
            /*Practica2
             idOrigen = super.dameIdProceso();                   
             idDestino = pmp.dameID();                  
             ip = pmp.dameIP();*/
            //Practica5
            Remotos buscar = obtenerProceso(dest);
            if (buscar != null) {
                idOrigen = super.dameIdProceso();
                idDestino = buscar.dameID();
                ip = buscar.dameIP();
                message[0] = (byte) idOrigen;
                imprimeln("ID ORIGEN :   " + idOrigen);
                message[4] = (byte) idDestino;
                imprimeln("ID DESTINO :   " + idDestino);
                try {
                    dp = new DatagramPacket(message, message.length, InetAddress.getByName(ip), damePuertoRecepcion());
                    socketEmison.send(dp);
                    //socketEmison.close();
                } catch (UnknownHostException ex) {
                    Logger.getLogger(MicroNucleo.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(MicroNucleo.class.getName()).log(Level.SEVERE, null, ex);
                }
                //lo siguiente aplica para la pr�ctica #2
                imprimeln("Enviando mensaje a IP=   " + ip + " ID=  " + idDestino);
                //suspenderProceso();   //esta invocacion depende de si se requiere bloquear al hilo de control invocador
            } else {
                //MANDAR LSA POR LA RED     
                imprimeln("Buscando Servidor con servicio: " + dest);
                //Pausador.pausa(3000);
                HiloEnviarLSA buscarServidor = new HiloEnviarLSA(super.dameIdProceso(), dest, message);
                buscarServidor.start();
            }
        } else {
            TablaEmision.remove(dest);
            idOrigen = super.dameIdProceso();
            idDestino = par.dameID();
            ip = par.dameIP();
            message[0] = (byte) idOrigen;
            imprimeln("ID ORIGEN :   " + idOrigen);
            message[4] = (byte) idDestino;
            imprimeln("ID DESTINO :   " + idDestino);
            try {
                dp = new DatagramPacket(message, message.length, InetAddress.getByName("localhost"), damePuertoRecepcion());
                socketEmison.send(dp);
                //socketEmison.close();
            } catch (UnknownHostException ex) {
                Logger.getLogger(MicroNucleo.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(MicroNucleo.class.getName()).log(Level.SEVERE, null, ex);
            }
            //lo siguiente aplica para la pr�ctica #2
            imprimeln("Enviando mensaje a IP=   " + ip + " ID=  " + idDestino);
            //suspenderProceso();   //esta invocacion depende de si se requiere bloquear al hilo de control invocador
        }
    }

    /**
     *
     */
    protected void receiveVerdadero(int addr, byte[] message) {
		//receiveFalso(addr,message);
        //el siguiente aplica para la pr�ctica #2

        //****************************************************************************************
        //Agregado para almacenamiento
        LinkedList<byte[]> linked = TB.get(addr);
        if ((linked == null || linked.size() == 0) /*&& !tablaMsgInesperados.existe(addr)*/) {
            imprimeln("Buzon vacío y buffer vacío");
            TablaRecepcion.put(addr, message);
            suspenderProceso();

        } else {
            byte[] msj = new byte[1024];
            msj = (byte[]) linked.poll();
            
            ConvertidorPaquetes cp = new ConvertidorPaquetes(msj);
            System.out.println("Mensaje obtenido de buzon de:" + cp.getEmisor());
            //Mover al buzon si hay algo en el buffer y el buzon existe
            
            /*if(linked.size() < 3 && tablaMsgInesperados.existe(addr)){
                imprimeln("Moviendo mensaje de buffer a buzon");
                System.out.println("Moviendo mensaje de buffer a buzon");
                linked.offer(tablaMsgInesperados.obtenerDatos(addr));
                tablaMsgInesperados.quitar(addr);
                
            }*/
            System.arraycopy(msj, 0, message, 0, msj.length);
             cp = new ConvertidorPaquetes(message);
            System.out.println("Mensaje guardado en TablaRecepcion de:" + cp.getEmisor());
            //TablaRecepcion.put(addr, message);
        }
        
         //Mover al buzon si hay algo en el buffer y el buzon existe
        
        
    }

    /**
     * Para el(la) encargad@ de direccionamiento por servidor de nombres en
     * prï¿½ctica 5
     */
    protected void sendVerdadero(String dest, byte[] message) {
        //sendFalso(dest,message);
        imprimeln("El proceso invocante es el " + super.dameIdProceso());
        println("Buscando en servidor de nombres el par (mï¿½quina, proceso) que corresponde al parï¿½metro dest de la llamada a send");
        ParMaquinaProceso pmp = ServidorNombres.getInstance().buscarServidor(dest);

        if (pmp == null) {
            println("La solicitud del proceso " + super.dameIdProceso() + " no puede ser atendida");
            Proceso p = super.dameProcesoLocal(super.dameIdProceso());
            p.println("La solicitud no puede ser atendida");
            this.reanudarProceso(p);
        } else {
            TablaEmision.put(pmp.dameID(), pmp);

            imprimeln("Completando campos de encabezado del mensaje a ser enviado");
            ConvertidorPaquetes solicitud = new ConvertidorPaquetes(message);
            solicitud.setReceptor(pmp.dameID());
            solicitud.setEmisor(super.dameIdProceso());
            DatagramSocket socketEmision;
            DatagramPacket dp;
            println("Origen empaquetado: " + solicitud.getEmisor());
            println("Destino empaquetado: " + solicitud.getReceptor());
            try {
                socketEmision = dameSocketEmision();
                println("Socket obtenido");
                dp = new DatagramPacket(message, message.length, InetAddress.getByName(pmp.dameIP()), damePuertoRecepcion());
                imprimeln("Enviando mensaje a IP=" + pmp.dameIP() + " ID=" + pmp.dameID());
                socketEmision.send(dp);
                println("Enviado");
            } catch (SocketException e) {
                println("Error iniciando socket: " + e.getMessage());
            } catch (UnknownHostException e) {
                println("UnknownHostException: " + e.getMessage());
            } catch (IOException e) {
                println("IOException: " + e.getMessage());
            }
			//no descomentar la sig. linea en la pÅ•actica 2
            //suspenderProceso();   //esta invocacion depende de si se requiere bloquear al hilo de control invocador
        }

    }

    /**
     * Para el(la) encargad@ de primitivas sin bloqueo en pr�ctica 5
     */
    protected void sendNBVerdadero(int dest, byte[] message) {
    }

    /**
     * Para el(la) encargad@ de primitivas sin bloqueo en pr�ctica 5
     */
    protected void receiveNBVerdadero(int addr, byte[] message) {
    }

    class TryAgainThread extends Thread {

        private int destino;
        private byte[] buffer;
        private LinkedList<byte[]> linked;
        private String ip;
        
        public TryAgainThread(){
            this.buffer = new byte[1024];
        }

        @Override
        public void run() {
            boolean con = true;
            while (con) {

                try {
                    sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (tablaMsgInesperados.existe(getDestino())) {
                    tablaMsgInesperados.quitar(getDestino());
                    byte[] auxSol = new byte[1024];
                    System.arraycopy(getBuffer(), 0, auxSol, 0, getBuffer().length);

                    if (getLinked().size() < 3) {
                        getLinked().offer(auxSol);
                    } else {
                        //considerar combiar origen y dest
                        auxSol[1023] = -2;
                        DatagramPacket dpTA = null;
                        try {
                            dpTA = new DatagramPacket(auxSol, auxSol.length, InetAddress.getByName(getIp()), damePuertoRecepcion());
                        } catch (UnknownHostException e) {
                            e.printStackTrace();
                        }
                        try {
                            dameSocketEmision().send(dpTA);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        imprimeln("Mensaje Enviado Try Again");
                    }
                } else {
                    imprimeln("Mensaje atendido exitosamente");
                }
                con = false;
            }
        }

        /**
         * @return the destino
         */
        public int getDestino() {
            return destino;
        }

        /**
         * @param destino the destino to set
         */
        public void setDestino(int destino) {
            this.destino = destino;
        }

        /**
         * @return the buffer
         */
        public byte[] getBuffer() {
            return buffer;
        }

        /**
         * @param buffer the buffer to set
         */
        public void setBuffer(byte[] buffer) {
            System.arraycopy(buffer, 0, this.buffer, 0, buffer.length);
        }

        /**
         * @return the linked
         */
        public LinkedList<byte[]> getLinked() {
            return linked;
        }

        /**
         * @param linked the linked to set
         */
        public void setLinked(LinkedList<byte[]> linked) {
            this.linked = linked;
        }

        /**
         * @return the ip
         */
        public String getIp() {
            return ip;
        }

        /**
         * @param ip the ip to set
         */
        public void setIp(String ip) {
            this.ip = ip;
        }
    }

    class TryAgainThread2 extends Thread {

        private byte[] buffer;
        private String ip;
        private int origen;
        
        public TryAgainThread2(){
            this.buffer = new byte[1024];
        }

        @Override
        public void run() {
            boolean continua = true;
            while (continua) {
                try {
                    sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                    //considerar cambio origen destino
                byte[] auxSol = new byte[1024];
                System.arraycopy(getBuffer(), 0, auxSol, 0, getBuffer().length);
                auxSol[1023] = 0;
                DatagramPacket dpTA = null;
                try {
                    dpTA = new DatagramPacket(auxSol, auxSol.length, InetAddress.getByName(getIp()), damePuertoRecepcion());
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
                try {
                    dameSocketEmision().send(dpTA);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                imprimeln("INTENTANDO DE NUEVO: " + getOrigen());
                continua = false;
            }

        }

        /**
         * @return the buffer
         */
        public byte[] getBuffer() {
            return buffer;
        }

        /**
         * @param buffer the buffer to set
         */
        public void setBuffer(byte[] buffer) {
            
            System.arraycopy(buffer, 0, this.buffer, 0, buffer.length);
        }

        /**
         * @return the ip
         */
        public String getIp() {
            return ip;
        }

        /**
         * @param ip the ip to set
         */
        public void setIp(String ip) {
            this.ip = ip;
        }

        /**
         * @return the origen
         */
        public int getOrigen() {
            return origen;
        }

        /**
         * @param origen the origen to set
         */
        public void setOrigen(int origen) {
            this.origen = origen;
        }

    }

    /**
     *
     */
    public void run() {
        DatagramSocket socketReceptor = dameSocketRecepcion();
        DatagramPacket dp;
        byte[] buffer = new byte[1024];
        dp = new DatagramPacket(buffer, buffer.length);
        while (seguirEsperandoDatagramas()) {
            try {
                socketReceptor.receive(dp);
                int origen = buffer[0];
                imprimeln("Origen:  " + origen);
                int destino = buffer[4];
                imprimeln("Destino:  " + destino);
                String ip = dp.getAddress().getHostAddress();
                imprimeln("IP:  " + ip);
                Proceso procesolocal = dameProcesoLocal(destino);

                //****************************************************************************************
                //Agregado para almacenamiento
                if (buffer[1023] == -2)//TA
                {
                    TryAgainThread2 hiloTA2 = new TryAgainThread2();
                    hiloTA2.setBuffer(buffer);
                    hiloTA2.setIp(ip);
                    hiloTA2.setOrigen(origen);
                    hiloTA2.start();

                } //****************************************************************************************
                else if (buffer[1023] == -1) {
                    //RECIBE UN AU
                        /*imprimeln("Proceso local no encontrado, AUSENCIA DE DESTINATARIO");
                     reanudarProceso(procesolocal);*/
                    buffer[1023] = (byte) 0;
                    // se envia el destino del buffer[4] y se guarda el servicio que daba para buscar uno nuevo con ese servicio
                    int dest = EliminarServidorRemoto(destino);
                    Remotos buscar = obtenerProceso(dest);
                    if (buscar != null) {
                        buffer[0] = (byte) origen;
                        buffer[4] = (byte) buscar.dameID();
                        dp = new DatagramPacket(buffer, buffer.length, InetAddress.getByName(buscar.dameIP()), damePuertoRecepcion());
                        socketReceptor.send(dp);
                    } else {
                        HiloEnviarLSA buscarServidor = new HiloEnviarLSA(super.dameIdProceso(), dest, buffer);
                        buscarServidor.start();
                    }
                } else if (buffer[8] == -2) {
                    //LSA
                    int servicio = (buffer[10] << 8 & 0x0000FF00) | (buffer[9] & 0x000000FF);
                    imprimeln("Mensaje LSA recibido para " + servicio);
                    EnviarFSA buscarServidor = new EnviarFSA(servicio, ip);
                    buscarServidor.start();
                } else if (buffer[8] == -3) {
                    //FSA
                    int servicio = (buffer[10] << 8 & 0x0000FF00) | (buffer[9] & 0x000000FF);
                    imprimeln("Mensjae FSA recibido para registrar a : " + servicio + " ID: " + buffer[11] + " IP: " + ip);
                    Remotos nuevo = new Remotos(servicio, buffer[11], ip);
                    ProcesosRemotos.add(nuevo);
                } else if (procesolocal != null) {
                    byte[] esperaRecibir = TablaRecepcion.get(destino);

                    if (esperaRecibir != null) {
                        CrearOTE cote = new CrearOTE(ip, origen);
                        TablaEmision.put(origen, cote);
                        System.arraycopy(buffer, 0, esperaRecibir, 0, buffer.length);
                        ConvertidorPaquetes cp = new ConvertidorPaquetes(esperaRecibir);
                        System.out.println("Mensaje copiado a servidor de: "+cp.getEmisor());
                        TablaRecepcion.remove(destino);
                        reanudarProceso(procesolocal);
                    } /**
                     * Agregado para almacenamiento
                     */
                    else {
                        esperaRecibir = TablaRecepcion.get(destino);
                        if (esperaRecibir != null) {
                            DatosTabla dt = new DatosTabla(ip, origen);
                            TablaEmision.put(origen, dt);
                            byte[] auxSol = new byte[1024];
                            System.arraycopy(buffer, 0, auxSol, 0, buffer.length);
                            TablaRecepcion.remove(destino);
                            reanudarProceso(procesolocal);
                        } else {
                            System.out.println("DESTINO ANTES DEL NULL POINTER"+ destino);
                            LinkedList<byte[]> linked = TB.get(destino);
                            if (/*linked!=null &&*/ linked.size() < 3 ) {
                                DatosTabla dt = new DatosTabla(ip, origen);
                                TablaEmision.put(origen, dt);
                                byte[] arreglon = new byte[1024];
                                System.arraycopy(buffer, 0, arreglon, 0, buffer.length);
                                ConvertidorPaquetes cp2 = new ConvertidorPaquetes(arreglon);
                                System.out.println("Guardado en buzón de: "+cp2.getEmisor() );
                                linked.offer(arreglon);
                            } else {

                                byte[] auxSol = new byte[1024];
                                System.arraycopy(buffer, 0, auxSol, 0, buffer.length);
                                if (!tablaMsgInesperados.existe(destino)) {
                                    tablaMsgInesperados.agregar(destino, auxSol);
                                    imprimeln("Agregando a Tabla de Mensajes Inesperados " + destino);
                                    System.out.println("Agregando a Tabla de Mensajes Inesperados " + destino);
                                    DatosTabla dt = new DatosTabla(ip, origen);
                                    TablaEmision.put(origen, dt);
                                    TryAgainThread hiloTA = new TryAgainThread();
                                    hiloTA.setBuffer(buffer);
                                    hiloTA.setDestino(destino);
                                    hiloTA.setIp(ip);
                                    hiloTA.setLinked(linked);                                   
                                    hiloTA.start();
                                } else {
                                    //considerar cambio origen destino
                                    DatosTabla dt = new DatosTabla(ip, origen);
                                    TablaEmision.put(origen, dt);
                                    byte[] auxSol2 = new byte[1024];
                                    System.arraycopy(buffer, 0, auxSol2, 0, buffer.length);
                                    auxSol2[1023] = -2;
                                    DatagramPacket dpAU = null;
                                    try {
                                        dpAU = new DatagramPacket(auxSol2, auxSol2.length, InetAddress.getByName(ip), damePuertoRecepcion());
                                    } catch (UnknownHostException e) {
                                        e.printStackTrace();
                                    }
                                    try {
                                        dameSocketEmision().send(dpAU);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    imprimeln("Mensaje Enviado: Try Again");
                                }

                            }

                        }
                    }
                } else//AU hilo
                {
                    System.out.println("SI ENTRE AQUI");
                    //AU
                    DatagramSocket socketAU = dameSocketRecepcion();
                    DatagramPacket dpAU;
                    /*byte [] bufferAU = new byte[1024];
                     bufferAU[0]= (byte)0;
                     bufferAU[4]= (byte)origen;*/
                    buffer[1023] = (byte) -1;
                    try {
                        dpAU = new DatagramPacket(buffer, buffer.length, InetAddress.getByName(ip), damePuertoRecepcion());
                        socketAU.send(dpAU);
                        //socketAU.close();
                    } catch (UnknownHostException ex) {
                        Logger.getLogger(MicroNucleo.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }
            } catch (IOException ex) {
                Logger.getLogger(MicroNucleo.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private Remotos obtenerProceso(int numServicio) {

        if (ProcesosRemotos.isEmpty()) {
            return null;
        } else {
            int numeroServidores = ProcesosRemotos.size();
            for (int i = 0; i < numeroServidores; i++) {
                if (ProcesosRemotos.get(i).dameServicio() == numServicio) {
                    return ProcesosRemotos.get(i);
                }
            }
        }

        return null;
    }

    void registroServidor(Locales serLocal) {
        ProcesosLocales.add(serLocal);
    }

    void eliminaServidorLocal(Locales serLocal) {
        Iterator<Locales> auxLocales = ProcesosLocales.iterator();
        Locales aux = null;
        int IDaux = 0;
        while (auxLocales.hasNext()) {
            aux = (Locales) auxLocales.next();
            if (aux.equals(serLocal)) {
                aux = ProcesosLocales.get(IDaux);
                break;
            } else {
                aux = null;
            }
            IDaux++;
        }
        if (aux != null) {
            ProcesosLocales.remove(IDaux);
            imprimeln("Quitando a proceso: " + serLocal.dameID());
        } else {
            imprimeln("Error al eliminar proceso: " + serLocal.dameID());
        }
    }

    private int EliminarServidorRemoto(int id) {
        int dest = 0;
        Iterator<Remotos> auxRemotos = ProcesosRemotos.iterator();
        Remotos aux = null;
        int IDaux = 0;
        while (auxRemotos.hasNext()) {
            aux = (Remotos) auxRemotos.next();
            if (aux.dameID() == id) {
                aux = ProcesosRemotos.get(IDaux);
                break;
            } else {
                aux = null;
            }
            IDaux++;
        }
        if (aux != null) {
            dest = aux.dameServicio();
            ProcesosRemotos.remove(IDaux);
            imprimeln("Quitando a proceso: " + id);
            return dest;
        } else {
            imprimeln("Error al eliminar proceso: " + id);
            return 0;
        }
    }

    class EnviarFSA extends Thread {

        private int servicio;
        private String ip;

        public EnviarFSA(int servicio, String ip) {//Recibir num de servicio lsa[9] y e ip averiguada 
            this.servicio = servicio;
            this.ip = ip;
        }

        public void run() {
            int numeroServidores = ProcesosLocales.size();
            for (int i = 0; i < numeroServidores; i++) {
                if (ProcesosLocales.get(i).dameServicio() == servicio) {
                    DatagramSocket socketEmisionFSA = dameSocketRecepcion();
                    byte[] FSA = new byte[1024];
                    FSA[8] = -3;
                    FSA[9] = (byte) ProcesosLocales.get(i).dameServicio();
                    FSA[10] = (byte) (ProcesosLocales.get(i).dameServicio() >>> 8);
                    FSA[11] = (byte) ProcesosLocales.get(i).dameID();
                    try {
                        DatagramPacket dpFSA = new DatagramPacket(FSA, FSA.length, InetAddress.getByName(ip), damePuertoRecepcion());
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

    class HiloEnviarLSA extends Thread {

        private int dest;
        private int idOrigen;
        byte[] msg = new byte[1024];

        public HiloEnviarLSA(int idOrigen, int dest, byte[] msg) {
            this.dest = dest;
            this.idOrigen = idOrigen;
            this.msg = msg;
        }

        public void run() {
            DatagramPacket dpLSA;
            Remotos buscar;
            buscar = obtenerProceso(dest);
            for (int i = 0; i < 3 && buscar == null; i++) {
                DatagramSocket socketEmisonLSA = dameSocketEmision();
                byte[] LSA = new byte[1024];
                LSA[8] = (byte) -2;
                LSA[9] = (byte) dest;
                LSA[10] = (byte) (dest >> 8);
                try {
                    dpLSA = new DatagramPacket(LSA, LSA.length, InetAddress.getByName("127.0.0.1"), damePuertoRecepcion());

                    socketEmisonLSA.send(dpLSA);

                } catch (UnknownHostException ex) {
                    Logger.getLogger(MicroNucleo.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(MicroNucleo.class.getName()).log(Level.SEVERE, null, ex);
                }
                Pausador.pausa(2000);
                imprimeln("Intento " + (i + 1) + " de paquete LSA");

                buscar = obtenerProceso(dest);
                Pausador.pausa(3000);
            }
            if (buscar != null) {
                try {
                    DatagramSocket socketTemp = dameSocketEmision();
                    DatagramPacket dpEnviar;
                    msg[0] = (byte) idOrigen;
                    msg[4] = (byte) buscar.dameID();
                    dpEnviar = new DatagramPacket(msg, msg.length, InetAddress.getByName(buscar.dameIP()), damePuertoRecepcion());
                    socketTemp.send(dpEnviar);
                } catch (IOException ex) {
                    Logger.getLogger(MicroNucleo.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                imprimeln("No se encontro servidor");
            }

        }
    }
}
