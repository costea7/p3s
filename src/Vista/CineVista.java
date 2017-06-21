/**
 * CineVista.java
 *
 * @author Alex Costea y Aitor Mari
 */
package Vista;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.*;
import java.util.Observer;
import java.util.Observable;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.io.File;
import java.io.FileNotFoundException;
import javax.swing.filechooser.FileNameExtensionFilter;

import Modelo.Platea;
import Modelo.Sesion;
import Controlador.OyenteVista;
import Controlador.Cine;

public class CineVista extends JFrame implements ActionListener, Observer {

  private static CineVista instancia = null;  // es singleton
  private Cine cine;
  private OyenteVista oyenteVista;
  private Platea platea;
  private PlateaVista plateaVista;
  private SesionesVista sesionesVista;
  private SesionesVista sesionTab;

  private JFrame ventanaPrincipal;
  private JPanel panelCentral;

  private ImageIcon iconoAplicacion, iconoAsientoLibre, iconoAsientoOcupado;
  private JButton botonAbrir, botonComprar;
  private AsientoVista asientos[][];

  public static final int ABRIR_FICHERO = 0;
  public static final int OPCION_SI = JOptionPane.YES_OPTION;

  /**
   * Identificadores de textos dependientes del idioma
   */
  private static final String TITULO = "Cine Rialto";
  private static final String AUTOR = "Alex Costea y Aitor Mari";
  private static final String VERSION = "06/2017";

  private static final String VENDER_ENTRADA = "Ocupar butaca";
  public static final String MENSAJE_CONFIRMACION_ENTRADAS="Entrada generada!";
  public static final String MENSAJE_ASIENTO_INEXISTENTE="¡Error!\nNo válido!";

  private static final String MENU_FICHERO = "Fichero";
  private static final String MENU_ITEM_NUEVA = "Nueva sesion";
  private static final char ATAJO_MENU_ITEM_NUEVA = 'N';

  private static final String MENU_ITEM_SALIR = "Salir";
  private static final char ATAJO_MENU_ITEM_SALIR = 'S';

  private static final String MENU_AYUDA = "Ayuda";
  private static final String MENU_ITEM_ACERCA_DE = "Acerca de...";
  private static final char ATAJO_ITEM_ACERCA_DE = 'A';

  public static final String FICHERO_NO_ENCONTRADO = "Fichero no encontrado";

  private static final String RUTA_RECURSOS = "/Vista/recursos/";
  private static final String ICONO_APLICACION = "iconoCine.png";
  private static final String ICONO_ASIENTO_LIBRE = "asientoLibre.png";
  private static final String ICONO_ASIENTO_OCUPADO = "asientoOcupado.png";

  private static final String ICONO_NUEVA = "nueva.png";

  /**
   * Constantes para redimensionamiento
   */
  private static final int MARGEN_HORIZONTAL = 50;
  private static final int MARGEN_VERTICAL = 75;

  public static final String EXT_FICHERO_PARTIDA = ".txt";
  public static final String FILTRO_SESIONES = "Sesiones";

  String datosEntrada;
  int asientosOcupados = 0;
  int filas, columnas;

  /** variable que comprueba si se ha pinchado sobre un asiento o
   * sobre un espacio en blanco 
   */
  boolean esAsiento = false;

  /**
   * Construye la vista del tablero de filas x columnas con el oyente para
   * eventos de la interfaz de usuario indicado
   *
   */
  private CineVista(final OyenteVista oyenteVista, int filas, int columnas) 
          throws FileNotFoundException {
    
    ventanaPrincipal = new JFrame(TITULO);

    this.oyenteVista = oyenteVista;
    this.filas = filas;
    this.columnas = columnas;

    ventanaPrincipal.addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosing(WindowEvent e) {
        oyenteVista.notificacion(OyenteVista.Evento.SALIR, null);
      }
    });

    ventanaPrincipal.setLayout(new BorderLayout());

    JPanel panelNorte = new JPanel();
    panelNorte.setLayout(new GridLayout(2, 1));

    // creamos elementos
    creaMenus(panelNorte);
    creaBarraHerramientas(panelNorte);

    //   muestraInfoSesiones(panelNorte);
    ventanaPrincipal.add(panelNorte, BorderLayout.NORTH);

    panelCentral = new JPanel();
    panelCentral.setLayout(new BorderLayout());

    // creaTablero(panelCentral, filas, columnas);
    ventanaPrincipal.add(panelCentral, BorderLayout.CENTER);

    //creacion panel este con la jlist de las sesiones 
    JPanel panelEste = new JPanel();
    panelEste.setLayout(new GridLayout());

    panelEste.setMaximumSize(new Dimension(20, 20));
    panelEste.setBorder(BorderFactory.createLineBorder(Color.gray));
    creaListaSesiones(panelEste);

    ventanaPrincipal.add(panelEste, BorderLayout.EAST);

    iconoAplicacion = new ImageIcon(
            this.getClass().getResource(RUTA_RECURSOS + ICONO_APLICACION));
    ventanaPrincipal.setIconImage(iconoAplicacion.getImage());

    iconoAsientoLibre = new ImageIcon(
            this.getClass().getResource(RUTA_RECURSOS + ICONO_ASIENTO_LIBRE));

    iconoAsientoOcupado = new ImageIcon(
            this.getClass().getResource(RUTA_RECURSOS + ICONO_ASIENTO_OCUPADO));

    // hacemos visible con el tamaño y la posición deseados     
    ventanaPrincipal.setResizable(false);
    ventanaPrincipal.setSize((int) (25.5 * +MARGEN_HORIZONTAL),
            (int) (9.5 * +MARGEN_VERTICAL));

    ventanaPrincipal.setVisible(true);
    ventanaPrincipal.setLocationRelativeTo(null);  // centra en la pantalla
  }

  /**
   * Devuelve la instancia de la vista del tablero
   *
   */
  public static synchronized CineVista instancia(
          OyenteVista oyenteIU, int numFilTab, int numColTab) 
          throws FileNotFoundException {
    
    if (instancia == null) {
      instancia = new CineVista(oyenteIU, numFilTab, numColTab);
    }
    return instancia;
  }

  private void creaMenus(JPanel panelNorte) {

    JMenu menuFichero = new JMenu(MENU_FICHERO);

    JMenuItem menuFicheroNueva
            = new JMenuItem(MENU_ITEM_NUEVA, ATAJO_MENU_ITEM_NUEVA);
    menuFicheroNueva.addActionListener(this);
    menuFicheroNueva.setActionCommand(MENU_ITEM_NUEVA);
    menuFichero.add(menuFicheroNueva);

    JMenuItem menuFicheroSalir
            = new JMenuItem(MENU_ITEM_SALIR, ATAJO_MENU_ITEM_SALIR);
    menuFicheroSalir.addActionListener(this);
    menuFicheroSalir.setActionCommand(MENU_ITEM_SALIR);
    menuFichero.add(menuFicheroSalir);

    JMenu menuAyuda = new JMenu(MENU_AYUDA);

    JMenuItem menuAyudaAcercaDe
            = new JMenuItem(MENU_ITEM_ACERCA_DE, ATAJO_ITEM_ACERCA_DE);
    menuAyudaAcercaDe.addActionListener(this);
    menuAyudaAcercaDe.setActionCommand(MENU_ITEM_ACERCA_DE);
    menuAyuda.add(menuAyudaAcercaDe);

    JMenuBar menuPrincipal = new JMenuBar();
    menuPrincipal.add(menuFichero);
    menuPrincipal.add(menuAyuda);

    panelNorte.add(menuPrincipal);

  }

  /**
   * Crea barra de herramientas
   *
   */
  private void creaBarraHerramientas(JPanel panelNorte) {

    JToolBar barra = new JToolBar();
    barra.setFloatable(false);

    botonAbrir = new JButton(new ImageIcon(
            this.getClass().getResource(RUTA_RECURSOS + ICONO_NUEVA)));
    botonAbrir.setToolTipText(MENU_ITEM_NUEVA);
    botonAbrir.addActionListener(this);
    botonAbrir.setActionCommand(MENU_ITEM_NUEVA);

    barra.add(botonAbrir);

    panelNorte.add(barra);
  }

  /**
   * Crea la vista del tablero
   *
   */
  private void creaTablero(JPanel panel, int numFilTab, int numColTab, 
          int numAsientosTotales, int numAsientosLibres) 
          throws FileNotFoundException {

    JPanel panel1 = new JPanel();
    panel1.setLayout(new BorderLayout());

    sesionTab = new SesionesVista(this);
    JPanel panelInfo = new JPanel();
    panelInfo.setLayout(new GridLayout(1, 1));

    JLabel asientosLibres = new JLabel();
    JLabel asientosTotales = new JLabel();

    Font font = new Font("Verdana", Font.BOLD, 14);

    asientosLibres.setText(" Asientos libres: " + numAsientosLibres);
    asientosTotales.setText("     Asientos totales: " + numAsientosTotales);

    asientosLibres.setFont(font);
    asientosTotales.setFont(font);

    panelInfo.add(asientosLibres);
    panelInfo.add(asientosTotales);

    plateaVista = new PlateaVista(this, numFilTab, numColTab, 
            PlateaVista.RECIBIR_EVENTOS_RATON);
    
    panel1.add(panelInfo, BorderLayout.NORTH);
    panel1.add(plateaVista, BorderLayout.CENTER);

    panel.add(panel1);

  }

  private void creaListaSesiones(JPanel panelEste) throws FileNotFoundException{
    sesionesVista = new SesionesVista(this);
    JPanel panelNorte = new JPanel();
    panelNorte.setLayout(new GridLayout(2, 1));

    panelNorte.add(sesionesVista, BorderLayout.NORTH);

    JPanel panelCentral = new JPanel();
    panelCentral.setLayout(new FlowLayout());

    botonComprar = new JButton(VENDER_ENTRADA);
    botonComprar.addActionListener(this);
    botonComprar.setEnabled(false);
    botonComprar.setActionCommand(VENDER_ENTRADA);
    botonComprar.setPreferredSize(new Dimension(180, 40));

    panelCentral.add(botonComprar);

    panelNorte.add(panelCentral, BorderLayout.SOUTH);

    panelEste.add(panelNorte, BorderLayout.EAST);
  }

  /**
   * Selecciona fichero de partida
   *
   */
  public String seleccionarFichero(int operacion) {
    String nombreFichero = null;

    JFileChooser dialogoSeleccionar = new JFileChooser(new File("."));
    FileNameExtensionFilter filtro
            = new FileNameExtensionFilter(FILTRO_SESIONES,
                    EXT_FICHERO_PARTIDA.substring(1));

    dialogoSeleccionar.setFileFilter(filtro);
    int resultado = 0;

    if (operacion == ABRIR_FICHERO) {
      resultado = dialogoSeleccionar.showOpenDialog(this);
    } else {
      resultado = dialogoSeleccionar.showSaveDialog(this);
    }
    if (resultado == JFileChooser.APPROVE_OPTION) {
      nombreFichero = dialogoSeleccionar.getSelectedFile().getName();

    }
    return nombreFichero;
  }

  /**
   * Sobreescribe actionPerformed
   *
   */
  @Override
  public void actionPerformed(ActionEvent e) {
    switch (e.getActionCommand()) {

      case MENU_ITEM_NUEVA:
        oyenteVista.notificacion(OyenteVista.Evento.NUEVA, null);
        break;

      case MENU_ITEM_SALIR:
        oyenteVista.notificacion(OyenteVista.Evento.SALIR, null);
        break;

      case MENU_ITEM_ACERCA_DE:
        JOptionPane.showMessageDialog(this,
                TITULO + "\n" + AUTOR + "\n" + VERSION,
                MENU_ITEM_ACERCA_DE, JOptionPane.INFORMATION_MESSAGE, 
                iconoAplicacion);
        break;

      case VENDER_ENTRADA:
        notificacion(OyenteVista.Evento.COMPRAR_ENTRADA, datosEntrada);
        botonComprar.setEnabled(false);
        if (esAsiento == true) {
          mensajeDialogo(MENSAJE_CONFIRMACION_ENTRADAS);
        }
        if (esAsiento == false) {
          mensajeDialogo(MENSAJE_ASIENTO_INEXISTENTE);
        }
        asientosOcupados++;
        break;
    }
  }

  public void añadirSesion(Sesion sesion) throws FileNotFoundException {
    sesionesVista.botonVer.setEnabled(true);
    sesionesVista.añadirSesion(sesion);

  }

  /**
   * Inicia Tablero Vista
   *
   */
  public void iniciarTableroVista(int numSala, int idSesion, Platea platea, 
          boolean recibe_eventos_raton) throws FileNotFoundException {

    panelCentral.removeAll();

    this.platea = platea;

    int asientosTotales = platea.devuelveAsientosTotales();
    int asientosLibres = platea.devuelveAsientosLibres();

    creaTablero(panelCentral, filas, columnas, asientosTotales, asientosLibres);

    plateaVista.iniciarTableroVista(numSala, idSesion, platea, recibe_eventos_raton);

    ventanaPrincipal.add(panelCentral, BorderLayout.CENTER);

    panelCentral.revalidate();

  }

  /**
   * Notificación de un evento de la interfaz de usuario
   *
   */
  public void notificacion(OyenteVista.Evento evento, Object obj) {
    oyenteVista.notificacion(evento, obj);
  }

  public void mensajeDialogo(String mensaje) {
    JOptionPane.showMessageDialog(ventanaPrincipal, mensaje, TITULO,
            JOptionPane.INFORMATION_MESSAGE, iconoAplicacion);
  }

  public void mandaInfoCine(String datosEntrada) {
    this.datosEntrada = datosEntrada;
    botonComprar.setEnabled(true);
  }

  @Override
  public void update(Observable o, Object arg) {
  }
}
