package Modelo;

import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Clase Sala
 *
 * @author Alex Costea y Aitor Mari
 */
public class Sala implements Serializable {

  ArrayList<Sesion> sesiones = new ArrayList<>();
  Iterator<Sesion> nueva = sesiones.iterator();

  private final String mapaAsientos;
  private final int numero;

  /**
   * Crea una nueva sala
   *
   * @param numero
   * @param mapaAsientos
   */
  public Sala(int numero, String mapaAsientos) {
    this.numero = numero;
    this.mapaAsientos = mapaAsientos;
  }

  /**
   * Crea una sesión para una sala
   *
   * @param sesion
   * @return
   */
  public boolean nuevaSesion(Sesion sesion) throws FileNotFoundException {
    sesiones.add(sesion);
    return true;
  }

  /**
   * Dado un entero, busca una platea que lo tenga como id
   *
   * @param idSesion
   * @return
   */
  public Sesion buscarSesion(int idSesion) {
    for (int i = 0; i < sesiones.size(); i++) {
      Sesion sesion = sesiones.get(i);
      if (sesion.devuelveId() == idSesion) {
        return sesion;
      }
    }
    return null;
  }

  /**
   * Devuelve la platea cuyo id es que se le pasa como parámetro
   *
   * @param idSesion
   * @return
   */
  public Platea devuelvePlatea(int idSesion) {
    for (int i = 0; i < sesiones.size(); i++) {
      Sesion sesion = sesiones.get(i);
      if (sesion.devuelveId() == idSesion) {
        return sesion.devuelvePlatea();
      }
    }
    return null;
  }

  /**
   * Diseña el mapa de ocupación de los asientos creados de una platea de una
   * sesión
   *
   * @param idSesion
   */
  public void generarMapaOcupacion(int idSesion) throws FileNotFoundException {
    Sesion sesion = buscarSesion(idSesion);
    sesion.generarMapaOcupacion();
  }

  /**
   * String devuelve entrada
   *
   * @param idSesion
   * @param fila
   * @param numero
   * @return
   */
  public String comprarEntrada(int idSesion, int fila, int numero) {

    String s = "";

    Sesion sesion = buscarSesion(idSesion);
    s = s + "Sala: " + devuelveNumero() + "\n";
    s = s + sesion.comprarEntrada(fila, numero);

    return s;
  }

  public int devuelveNumero() {
    return numero;
  }

  public String devuelveMapaAsientos() {
    return mapaAsientos;
  }
}
