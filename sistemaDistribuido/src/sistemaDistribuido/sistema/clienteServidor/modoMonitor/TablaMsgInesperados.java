package sistemaDistribuido.sistema.clienteServidor.modoMonitor;

import java.util.Hashtable;

/**
 *  Hector Fernando Gonzalez Trujillo
 * D05
 * 212354517
 * Practica 5
 */
public class TablaMsgInesperados
{
    private Hashtable<Integer,byte[]> tabla = new Hashtable();

    public void agregar(int addr, byte[] msg)
    {

        tabla.put(addr,msg);
    }

    public boolean existe(int dest)
    {
        return tabla.containsKey(dest);

    }

    public byte[] obtenerDatos(int addr)
    {
        return tabla.get(addr);
    }

    public void quitar(int addr)
    {
        tabla.remove(addr);
    }

}
