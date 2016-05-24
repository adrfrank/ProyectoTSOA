package sistemaDistribuido.visual.proyecto;

import sistemaDistribuido.sistema.clienteServidor.modoMonitor.Nucleo;
import sistemaDistribuido.visual.clienteServidor.MicroNucleoFrame;
import java.awt.Panel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import sistemaDistribuido.visual.clienteServidor.ServidorNombresFrame;

public class ProyectoFrame extends MicroNucleoFrame{
	private static final long serialVersionUID=1;
        private final ServidorNombresFrame servidorNombres;


	public ProyectoFrame(){
            setTitle("ProyectoFinal");
            servidorNombres= new ServidorNombresFrame();
		
	}

	protected Panel construirPanelSur(){
            ProyectoFramePanel panel = new ProyectoFramePanel();
            panel.addListener(new ManejadorBotones());
            return panel;
	}

	class ManejadorBotones implements ActionListener{
            public void actionPerformed(ActionEvent e){
                String com=e.getActionCommand();
                if(com.equals("Cliente (Frank)")){
                    levantarProcesoFrame(new sistemaDistribuido.visual.proyectoFrank.ClienteFrame(ProyectoFrame.this));
                }else if(com.equals("Servidor (Frank)")){
                    levantarProcesoFrame(new sistemaDistribuido.visual.proyectoFrank.ServidorFrame(ProyectoFrame.this));
                }else if(com.equals("Servidor de Nombres")){
                    servidorNombres.setVisible(true);
                }else if(com.equals("Cliente (Lau)")){
                   levantarProcesoFrame(new sistemaDistribuido.visual.proyectoLau.ClienteFrame(ProyectoFrame.this));
                }else if(com.equals("Servidor (Lau)")){
                   levantarProcesoFrame(new sistemaDistribuido.visual.proyectoLau.ServidorFrame(ProyectoFrame.this));
                }
            }
	}



	public static void main(String args[]){
            ProyectoFrame rpcf=new ProyectoFrame();
            rpcf.setVisible(true);
            rpcf.imprimeln("Ventana del micronucleo iniciada.");
            Nucleo.iniciarSistema(rpcf,2001,2002,rpcf);
	}
}
