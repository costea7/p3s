/**
 * PlateaVista.java
 *
 * @author Alex Costea y Aitor Mari
 */
package Vista;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.Icon;
import javax.swing.JPanel;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.border.Border;

import Modelo.Asiento;
import Controlador.OyenteVista;
import Modelo.Casilla;
import Modelo.Platea;

/**
 * Vista Swing del tablero
 *
 */
public class PlateaVista extends JPanel {

  private static final int ALTURA_FILA = 90;
  private static final int ANCHURA_COLUMNA = 20;

  private AsientoVista asientos[][];
  private CineVista tableroVista;
  private SesionesVista sesion;
  private OyenteVista oyenteVista;
  private CineVista cineVista;
  private SesionesVista sesi;
  private Platea platea;
  private ImageIcon iconoAsientoLibre, iconoAsientoOcupado;

  public static final boolean RECIBIR_EVENTOS_RATON = true;
  public static final boolean NO_RECIBIR_EVENTOS_RATON = false;

  private static final String RUTA_RECURSOS = "/Vista/recursos/";
  private static final String ICONO_ASIENTO_LIBRE = "asientoLibre.png";
  private static final String ICONO_ASIENTO_OCUPADO = "asientoOcupado.png";

  /*Variables para capturas la informacion de los asientos y sesiones
  que se mandará al control*/
  String infoAsiento;
  String infoSesion;

  PlateaVista(final CineVista vista, int filas, int columnas,
          boolean recibe_eventos_raton) {

    iconoAsientoLibre = new ImageIcon(
            this.getClass().getResource(RUTA_RECURSOS + ICONO_ASIENTO_LIBRE));

    iconoAsientoOcupado = new ImageIcon(
            this.getClass().getResource(RUTA_RECURSOS + ICONO_ASIENTO_OCUPADO));

    this.tableroVista = vista;
    setLayout(new GridLayout(filas, columnas));

    Border raisedbevel = BorderFactory.createRaisedBevelBorder();
    Border loweredbevel = BorderFactory.createLoweredBevelBorder();
    setBorder(BorderFactory.createCompoundBorder(raisedbevel, loweredbevel));

    asientos = new AsientoVista[filas][columnas];
    for (int fil = 0; fil < filas; fil++) {
      for (int col = 0; col < columnas; col++) {
        asientos[fil][col] = new AsientoVista(new Asiento(fil, col));
        add(asientos[fil][col]); //adds button to grid
        if (recibe_eventos_raton) {
          asientos[fil][col].addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
              AsientoVista casilla = (AsientoVista) e.getSource();
              infoAsiento = infoSesion
                      + casilla.devuelveAsiento().devuelveFila() + "-"
                      + casilla.devuelveAsiento().devuelveNumero();
              vista.mandaInfoCine(infoAsiento);
              vista.esAsiento
                      = compruebaAsiento(casilla.devuelveAsiento());
            }
          });
        }
      }
    }
    this.setPreferredSize(new Dimension(filas * ALTURA_FILA + 100,
            columnas * ANCHURA_COLUMNA + 100));

  }

  public void iniciarTableroVista(final int numSala, final int idSesion,
          final Platea platea, boolean recibe_eventos_raton) {
    for (int fila = 0; fila < platea.devuelveNumFilas(); fila++) {
      for (int columna = 0; columna < platea.devuelveNumColumnas(); columna++) {

        Asiento asiento = new Asiento(fila, columna);
        ponerIcono(asiento, platea.devuelveEstadoCasilla(asiento));

        infoSesion = numSala + "-" + idSesion + "-";
      }
    }
  }

  
  public void notificacion(OyenteVista.Evento evento, Object obj) {
    oyenteVista.notificacion(evento, obj);
  }

  public Platea devuelvePlatea() {
    return platea;
  }

  /**
   * Devuelve el tamaño de la platea
   *
   */
  public Dimension dimensionCasilla() {
    return asientos[0][0].getSize();
  }

  /**
   * Pone un icono en la casilla de la posición indicada
   *
   */
  public void ponerIcono(Asiento asiento, Casilla butaca) {
    if (butaca != Casilla.VACIA) {
      if (butaca == Casilla.LIBRE) {
        ponerIconoCasilla(asiento, iconoAsientoLibre);
      } else if (butaca == Casilla.OCUPADA) {
        ponerIconoCasilla(asiento, iconoAsientoOcupado);
      }
    } else {
      ponerIconoCasilla(asiento, null);
    }
  }

  public void ponerIconoCasilla(Asiento asiento, Icon icono) {
    asientos[asiento.devuelveFila()][asiento.devuelveNumero()].setIcon(icono);
  }

  /**
   * Método que comprueba si una posición es asiento o no
   *
   */
  public final boolean compruebaAsiento(Asiento asiento) {
    if (asientos[asiento.devuelveFila()][asiento.devuelveNumero()].getIcon() != null) {
      return true;
    }
    return false;
  }
}
