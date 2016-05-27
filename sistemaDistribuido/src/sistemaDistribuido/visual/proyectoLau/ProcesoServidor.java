/*
 * Laura Teresa García López    212354614
 * Sección: D05
 * Practica Java #1
 */
package sistemaDistribuido.visual.proyectoLau;

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
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import sistemaDistribuido.sistema.clienteServidor.modoMonitor.Nucleo;
import sistemaDistribuido.sistema.clienteServidor.modoUsuario.Proceso;
import sistemaDistribuido.util.Escribano;
import sistemaDistribuido.util.Pausador;
import sistemaDistribuido.sistema.clienteServidor.modoMonitor.Locales;
import sistemaDistribuido.sistema.clienteServidor.modoUsuario.ServidorNombres;

public class ProcesoServidor extends Proceso {

    public static final String ServerName = "FileServerLau";

    /**
     *
     */
    private byte[] solServidor = new byte[1024];
    private byte[] respServidor = new byte[1024];

    public ProcesoServidor(Escribano esc) {
        super(esc);
        start();
    }

    /**
     *
     */
    public void run() {
        imprimeln("Proceso servidor Lau en ejecucion.");
        int id = ServidorNombres.getInstance().registrarServidor(ServerName, this.dameMaquinaProceso());
        Locales serLocal = null;
        try {
            serLocal = new Locales(248, dameID(), InetAddress.getLocalHost().getHostAddress());
        } catch (UnknownHostException ex) {
            Logger.getLogger(ProcesoServidor.class.getName()).log(Level.SEVERE, null, ex);
        }
        Nucleo.InsertarLocales(serLocal);
        //Agregado para almacenamiento
        LinkedList linkedlist = new LinkedList();
        Nucleo.AgregarBuzon(dameID(), linkedlist);
        while (continuar()) {
            imprimeln("Invocando a receive.");
            Nucleo.receive(dameID(), solServidor);
            imprimeln("Procesando petición recibida del cliente");
            Desempaquetar();
            imprimeln("Generando mensaje a ser enviado, llenando los campos necesarios");
            Pausador.pausa(5000);  //sin esta l�nea es posible que Servidor solicite send antes que Cliente solicite receive
            imprimeln("Enviando Respuesta ");
            imprimeln("DESTINO" + solServidor[0]);
            if (continuar()) {
                Nucleo.send(solServidor[0], respServidor);
            }
            
        }
        Nucleo.EliminarLocales(serLocal);
        ServidorNombres.getInstance().deregistrarServidor(id);
    }

    public void Desempaquetar() {
        int codop;
        String mensaje = null, cad;
        codop = solServidor[8];
        byte[] respu = null;
        cad = new String(solServidor, 11, solServidor[10]);
        switch (codop) {
            case 1:
                //Para crear el archivo se tiene que poner en el campo de texto SIN .txt
                imprimeln("Operacion solicitada: CREAR");
                imprimeln("Datos para realizar operacion: " + cad);
                File archivo = new File(cad + ".txt");
                try {
                    if (archivo.createNewFile()) {
                        mensaje = "El archivo de nombre: " + cad + " fue creado";
                    } else {
                        mensaje = "El archivo de nombre: " + cad + " no pudo ser creado";
                    }

                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
                respu = mensaje.getBytes();
                respServidor[8] = (byte) mensaje.length();
                System.arraycopy(respu, 0, respServidor, 9, respu.length);
                break;
            case 2:
                //Para eliminar el archivo se tiene que poner en el campo de texto SIN .txt
                imprimeln("Operacion solicitada: ELIMINAR");
                imprimeln("Datos para realizar operacion: " + cad);
                File fichero = new File(cad + ".txt");
                if (fichero.delete()) {
                    mensaje = "El archivo de nombre: " + cad + " fue eliminado";
                } else {
                    mensaje = "El archivo " + cad + " no pudo ser eliminado";
                }
                respu = mensaje.getBytes();
                respServidor[8] = (byte) mensaje.length();
                System.arraycopy(respu, 0, respServidor, 9, respu.length);
                break;
            case 3:
                //Para leer del archivo se tiene que poner en el campo de texto solo el nombre del archivo SIN .txt solo eso
                String leer = null;
                imprimeln("Operacion solicitada: LEER");
                imprimeln("Datos para realizar operacion: " + cad);
                try {
                    FileInputStream fstream = new FileInputStream(cad + ".txt");

                    DataInputStream entrada = new DataInputStream(fstream);

                    BufferedReader buffer = new BufferedReader(new InputStreamReader(entrada));

                    while ((leer = buffer.readLine()) != null) {
                        mensaje = "Del archivo: " + cad + " se leyo: " + leer;
                    }
                    entrada.close();
                } catch (Exception e) {
                    mensaje = ("No se pudo escribir en el archivo " + cad);
                }

                respu = mensaje.getBytes();
                respServidor[8] = (byte) mensaje.length();
                System.arraycopy(respu, 0, respServidor, 9, respu.length);
                break;
            case 4:
                //Para escribir en el archivo se tiene que poner en el campo de texto SIN .txt seguido de un # que es el limitador y despues lo que quieres escribir
                String nombre,
                 escribir;
                String[] datos = cad.split("#");
                nombre = datos[0];
                escribir = datos[1];
                imprimeln("Operacion solicitada: ESCRIBIR");
                imprimeln("Datos para realizar operacion: ");
                imprimeln("Nombre del archivo: " + nombre + ".txt");
                imprimeln("Datos a escribir: " + escribir);
                File f;
                f = new File(nombre + ".txt");
                try {
                    FileWriter w = new FileWriter(f);
                    BufferedWriter bw = new BufferedWriter(w);
                    PrintWriter wr = new PrintWriter(bw);
                    wr.write(escribir);
                    wr.close();
                    bw.close();
                    mensaje = "El mensaje fue escrito en el archivo: " + nombre + ".txt ";
                } catch (IOException e) {
                    mensaje = "El mensaje en el archivoo: " + nombre + ".txt  no pudo ser escrito";
                }

                respu = mensaje.getBytes();
                respServidor[8] = (byte) mensaje.length();
                System.arraycopy(respu, 0, respServidor, 9, respu.length);
                break;

        }
    }
}
