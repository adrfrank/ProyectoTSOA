package sistemaDistribuido.visual.proyecto;

import java.awt.*;
import java.awt.event.*;


public class ProyectoFramePanel extends Panel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Button btnClienteFrank, btnServerFrank;
        private final Button botonServidorNombres;
        
        
        public ProyectoFramePanel(){
            //Servidor de nombres
            botonServidorNombres = new Button("Servidor de Nombres");
            Panel pnlSN = new Panel();
            pnlSN.add(botonServidorNombres);
            add(pnlSN);
            
            
            //Repetir esto por cada uno en el equipo
            btnClienteFrank = new Button("Cliente (Frank)");
            btnServerFrank = new Button("Servidor (Frank)");
            Panel pnlFrank = new Panel();
            pnlFrank.add(btnClienteFrank);
            pnlFrank.add(btnServerFrank);
            add(pnlFrank);
        }
        
        public void addListener(ActionListener listener){
            botonServidorNombres.addActionListener(listener);
            
            //repertir esto por cada boton que se agregue al proyecto
            btnClienteFrank.addActionListener(listener);
            btnServerFrank.addActionListener(listener);
            
        }
}
