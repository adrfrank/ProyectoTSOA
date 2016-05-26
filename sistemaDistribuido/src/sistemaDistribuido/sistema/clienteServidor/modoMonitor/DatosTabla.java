package sistemaDistribuido.sistema.clienteServidor.modoMonitor;

import sistemaDistribuido.sistema.clienteServidor.modoMonitor.ParMaquinaProceso;

class DatosTabla implements ParMaquinaProceso
{
    private String IP;
    private int ID;

    public DatosTabla(String ip, int id)
    {
        IP = ip;
        ID = id;
    }

    @Override
    public String dameIP() {
        return IP;
    }

    @Override
    public int dameID() {
        return ID;
    }
}