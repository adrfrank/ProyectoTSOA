package sistemaDistribuido.visual.clienteServidor;

import java.awt.Panel;
import java.awt.Button;
import java.awt.event.ActionListener;

public class PanelClienteServidor extends Panel{
  private static final long serialVersionUID=1;
  private Button botonCliente,botonServidor, botonServidorNombres;

  public PanelClienteServidor(){
     botonCliente=new Button("Cliente");
     botonServidor=new Button("Servidor");
     botonServidorNombres = new Button("Servidor de Nombres");
     add(botonCliente);
     add(botonServidor);
     add(botonServidorNombres);
  }
  
  public Button dameBotonCliente(){
    return botonCliente;
  }
  
  public Button dameBotonServidor(){
    return botonServidor;
  }
   public Button dameBotonServidorNmbres(){
	  return botonServidorNombres;
  }
  public void agregarActionListener(ActionListener al){
    botonCliente.addActionListener(al);
    botonServidor.addActionListener(al);
     botonServidorNombres.addActionListener(al);
  }
}