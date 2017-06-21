/**
 * OyenteVista.java
 * 03/2017
 *
 * @author ccatalan
 */
package Controlador;

/**
 * Interfaz de oyente para recibir eventos de la interfaz de usuario
 *
 */
public interface OyenteVista {

  public enum Evento {
    NUEVA, SALIR, COMPRAR_ENTRADA, COMPRAR_ENTRADA1, VER_MAPA
  }

  /**
   * Llamado para notificar un evento de la interfaz de usuario
   *
   * @param evento
   * @param obj
   */
  public void notificacion(Evento evento, Object obj);
}
