package sistemaDistribuido.visual.proyecto;

import java.awt.*;
import java.awt.event.*;


public class ProyectoFramePanel extends Panel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Button btnClienteFrank, btnServerFrank, btnClienteLau, btnServidorLau, btnClienteGiss, btnServidorGiss, btnClienteFer, btnServidorFer;
        private final Button botonServidorNombres;        
        Panel p=new Panel();
        
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
            btnClienteFer = new Button("Cliente (Fer)");
            btnServidorFer = new Button("Servidor (Fer)");
            p.setLayout(new GridLayout(4,2));
            Panel pnlFrank = new Panel();
            Panel pnlLau   = new Panel();
            Panel pnlGiss  = new Panel();
            Panel pnlFer   = new Panel();
            pnlFrank.add(btnClienteFrank);
            pnlFrank.add(btnServerFrank);
            pnlLau.add(btnClienteLau);
            pnlLau.add(btnServidorLau);
            pnlGiss.add(btnClienteGiss);
            pnlGiss.add(btnServidorGiss);
            pnlFer.add(btnClienteFer);
            pnlFer.add(btnServidorFer);
            p.add(pnlFrank);
            p.add(pnlLau);
            p.add(pnlGiss);
            p.add(pnlFer);
            add(p);
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
            btnServidorFer.addActionListener(listener);
            btnClienteFer.addActionListener(listener);
        }
}
