package sistemaDistribuido.visual.proyectoGiss;

import sistemaDistribuido.sistema.clienteServidor.modoUsuario.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import sistemaDistribuido.sistema.clienteServidor.modoMonitor.Nucleo;
import sistemaDistribuido.sistema.clienteServidor.modoUsuario.Proceso;
import sistemaDistribuido.util.ConvertidorPaquetes;
import sistemaDistribuido.util.Escribano;
import sistemaDistribuido.util.ManejadorArchivos;
import sistemaDistribuido.util.Pausador;
import sistemaDistribuido.sistema.clienteServidor.modoUsuario.ServidorNombres;
import java.util.LinkedList;
/*Laura Gissel Barreto Siordia
 * D05
 * Practica 1
*/
public class ProcesoServidor extends Proceso{
    public static final String ServerName = "FileServerGiss";
	String msje,res;
	public ProcesoServidor(Escribano esc){
		super(esc);
		start();
	}

	public void run(){
                imprimeln("Inicio de proceso");
		imprimeln("Proceso servidor Giss en ejecucion.");
		byte[] solServidor=new byte[1024];
		byte[] respServidor = new byte[1024];
                int id = ServidorNombres.getInstance().registrarServidor(ServerName, this.dameMaquinaProceso());
		
                byte dato;
                
                LinkedList linkedlist= new LinkedList();
                Nucleo.AgregarBuzon(dameID(), linkedlist);
		while(continuar())
                {
                        imprimeln("Invocando a receive()");
			Nucleo.receive(dameID(),solServidor);
                        imprimeln("Procesando peticion recibida del cliente");
			imprimeln("Generando mensaje a ser enviado, llenando los campos necesarios"); 
                        if(solServidor[9]==0)
                        {
                            
                            msje=new String(solServidor,11,solServidor[10]);
                            imprimeln("Solicitud: Crear");
                            imprimeln("Datos"+msje);
                            msje+=".txt";
                            File archivo = new File (msje);
                            try {
                            if (archivo.createNewFile())
                              res = "El archivo de nombre: "+msje+" fue creado";
                            else
                              res = "El archivo de nombre: "+msje+" no pudo ser creado";

                          } catch (IOException ioe) {
                            ioe.printStackTrace();
                          }
                            //res="El archivo "+msje+" ya se creó";
                            byte[] resp = res.getBytes();
                            respServidor[9]=(byte) res.length();
                            System.arraycopy(resp,0,respServidor,10,resp.length);
                        }else if(solServidor[9]==1)
                        {
                            msje=new String(solServidor,11,solServidor[10]);
                            imprimeln("Solicitud: Eliminar");
                            imprimeln("Datos"+msje);
                            msje+=".txt";
                            File archivo = new File (msje);
                            if (archivo.delete())
                                res = "El archivo de nombre: "+msje+" fue eliminado";
                            else
                                res = "El archivo de nombre: "+msje+" no pudo ser eliminado";
                            //res="El archivo "+msje+" ya se elimino";
                            byte[] resp = res.getBytes();
                            respServidor[9]=(byte) res.length();
                            System.arraycopy(resp,0,respServidor,10,resp.length);
                        }else if(solServidor[9]==2)
                        {
                            msje=new String(solServidor,11,solServidor[10]);
                            imprimeln("Solicitud: Leer");
                            imprimeln("Datos"+msje);
                            msje+=".txt";
                            try{
                                FileInputStream fstream = new FileInputStream(msje);
                                DataInputStream archivoentrada = new DataInputStream(fstream);
                                BufferedReader buffer = new BufferedReader(new InputStreamReader(archivoentrada));
                                String linea;

                                while ((linea = buffer.readLine()) != null)   {
                                   res+= "El archivo: "+msje+" dice: " + linea+"\n";               
                                }
                                archivoentrada.close();
                            }catch (Exception e){
                                res=("No se pudo leer el archivo "+ msje);
                            }
                            
                            //res="El archivo "+msje+" ya se leyo";
                            byte[] resp = res.getBytes();
                            respServidor[9]=(byte) res.length();
                            System.arraycopy(resp,0,respServidor,10,resp.length);
                        }
                        else if(solServidor[9]==3)
                        {
                            String ar, cosas;
                            msje=new String(solServidor,11,solServidor[10]);
                            imprimeln("Solicitud: Escribir");
                            //imprimeln("Datos"+msje);
                            String [] info = msje.split(",");
                            ar=info[0];
                            ar+=".txt";
                            cosas=info[1];
                            imprimeln("Datos: "+ar+" "+cosas);
                            File archivo;
                            archivo = new File(ar);
                            try{
                                FileWriter escribe = new FileWriter(archivo);
                                BufferedWriter bufferescribe = new BufferedWriter(escribe);
                                PrintWriter escr = new PrintWriter(bufferescribe);	
                                escr.write(cosas);
                                escr.close();
                                bufferescribe.close();
                                res = "Se escribio en el archivo: "+ar;
                            }catch(IOException e){
                                res = "No se escribio en el archivo: "+ar;
                            }

                            //res="El archivo "+msje+" ya se escribio";
                            byte[] resp = res.getBytes();
                            respServidor[9]=(byte) res.length();
                            System.arraycopy(resp,0,respServidor,10,resp.length);
                        }
			//Pausador.pausa(20000);  //sin esta l�nea es posible que Servidor solicite send antes que Cliente solicite receive
			
                        imprimeln("Señalamiento al nucleo para envio de mensajes");
			Nucleo.send(solServidor[0],respServidor);                        
		}
                ServidorNombres.getInstance().deregistrarServidor(id);
	}
}
