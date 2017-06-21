/**
 * AsientoVista.java
 *
 * @author Alex Costea y Aitor Mari
 *
 */
package Vista;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.SwingConstants;
import java.awt.Color;
import javax.swing.JButton;

import Modelo.Asiento;

/**
 * Vista de una asiento de la platea a partir de un JLabel
 *
 */
public class AsientoVista extends JButton {

  private Asiento asiento;

  /**
   * Construye una vista con el icono indicado de la casilla en una posición
   *
   */
  AsientoVista(Asiento asiento, Icon icono) {
    this.asiento = asiento;
    setIcon(icono);
    setBackground(Color.WHITE);

    setHorizontalAlignment(SwingConstants.CENTER);
    setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
  }

  /**
   * Construye una vista sin icono de la casilla en una posición
   *
   */
  AsientoVista(Asiento asiento) {
    this(asiento, null);
  }

  /**
   * Devuelve la posición del asiento
   *
   */
  public Asiento devuelveAsiento() {
    return asiento;
  }

  /**
   * Sobreescribe toString
   *
   */
  @Override
  public String toString() {
    return asiento.toString();
  }
}
