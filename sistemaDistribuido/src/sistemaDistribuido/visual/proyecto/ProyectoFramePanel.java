package sistemaDistribuido.visual.proyecto;

import java.awt.*;
import java.awt.event.*;


public class ProyectoFramePanel extends Panel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Button btnClienteFrank, btnServerFrank, btnClienteLau, btnServidorLau, btnClienteGiss, btnServidorGiss;
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
            btnClienteLau = new Button("Cliente (Lau)");
            btnServidorLau = new Button("Servidor (Lau)");
            btnClienteGiss = new Button("Cliente (Giss)");
            btnServidorGiss = new Button("Servidor (Giss)");
            Panel pnlFrank = new Panel();
            Panel pnlLau   = new Panel();
            Panel pnlGiss  = new Panel();
            pnlFrank.add(btnClienteFrank);
            pnlFrank.add(btnServerFrank);
            pnlLau.add(btnClienteLau);
            pnlLau.add(btnServidorLau);
            pnlGiss.add(btnClienteGiss);
            pnlGiss.add(btnServidorGiss);
            add(pnlFrank);
            add(pnlLau);
            add(pnlGiss);
        }
        
        public void addListener(ActionListener listener){
            botonServidorNombres.addActionListener(listener);
            
            //repertir esto por cada boton que se agregue al proyecto
            btnClienteFrank.addActionListener(listener);
            btnServerFrank.addActionListener(listener);
            btnClienteLau.addActionListener(listener);
            btnServidorLau.addActionListener(listener);
            btnServidorGiss.addActionListener(listener);
            btnClienteGiss.addActionListener(listener);
            
        }
}
