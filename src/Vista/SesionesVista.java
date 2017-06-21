/**
 * SesionesVista.java
 * @author Alex Costea y Aitor Mari
 */
package Vista;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import Controlador.OyenteVista;
import Modelo.Sesion;

public class SesionesVista extends JPanel implements ActionListener {

  private CineVista cineVista;
  
  public static final boolean RECIBIR_EVENTOS_RATON = true;
  public static final String VER_PLATEA = "Ver platea";
  public JButton botonVer;

  DefaultListModel modelSesiones, modelLogs;
  JList listSesiones;
  JList listLogs;
  JScrollPane paneSesiones, paneLogs;

  String infoSesion = "";

  SesionesVista(final CineVista vista) throws FileNotFoundException {

    this.cineVista = vista;

    setLayout(new BorderLayout());

    JPanel panelCentral = new JPanel();
    panelCentral.setLayout(new FlowLayout());

    botonVer = new JButton(VER_PLATEA);
    botonVer.setPreferredSize(new Dimension(180, 40));
    botonVer.addActionListener(this);
    botonVer.setEnabled(false);

    JPanel panelNorte = new JPanel();
    panelNorte.setLayout(new GridLayout(1, 1));

    modelSesiones = new DefaultListModel();
    listSesiones = new JList(modelSesiones);
    paneSesiones = new JScrollPane(listSesiones);
    paneSesiones.setBorder(BorderFactory.createTitledBorder("Sesiones"));
    paneSesiones.setPreferredSize(new Dimension(120, 230));

    panelCentral.add(botonVer);
    panelNorte.add(paneSesiones);

    add(paneSesiones, BorderLayout.NORTH);
    add(panelCentral, BorderLayout.CENTER);
    
    this.setPreferredSize(new Dimension(250, 250));
  }

  /**
   * añade una sesion
   *
   */
  public void añadirSesion(final Sesion sesion) throws FileNotFoundException {

    String datosSesion = sesion.devuelvePelicula() + " - " + 
            sesion.devuelveFecha() + " - " + 
            sesion.devuelveHoraInicio() + " - " + 
            sesion.devuelveNumSala();
    modelSesiones.addElement(datosSesion);
    this.setPreferredSize(new Dimension(250, 250));

  }

  @Override
  public void actionPerformed(ActionEvent e) {
    if (e.getActionCommand().equals(VER_PLATEA)) {
      int indiceLista = listSesiones.getSelectedIndex();
      if (indiceLista >= 0) {
        String s = (String) listSesiones.getSelectedValue();
        
        String[] parts = s.split(" - ");
        String numSala = parts[3];
        
        int indice = indiceLista + 1;
        
        //guardamos en infoSesion el numero de sala y el id de la sesion
        infoSesion = numSala + "-" + indice;

        this.setPreferredSize(new Dimension(250, 250));
        cineVista.notificacion(OyenteVista.Evento.VER_MAPA, infoSesion);
      }
    }
  }
}