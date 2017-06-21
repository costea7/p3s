/**
 * Clase Casilla
 *
 * @author Alex Costea y Aitor Mari
 */
package Modelo;

/**
 * Casilla que representa una butaca de la platea
 *
 */
public enum Casilla {
  VACIA, LIBRE, OCUPADA;

  /**
   * Sobreescribe toString
   *
   */
  @Override
  public String toString() {
    switch (this) {
      case VACIA:
        return " ";

      case LIBRE:
        return ".";

      case OCUPADA:
        return "x";
    }
    return " ";
  }
}
