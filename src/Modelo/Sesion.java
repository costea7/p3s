package Modelo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;

import java.util.Scanner;

/**
 * Clase Sesión
 *
 * @author Alex Costea y Aitor Mari
 */
public class Sesion implements Serializable {

  private int id, numSala;
  private String pelicula;
  private String horaInicio, horaFin, fecha;
  private Platea platea;

  /**
   * Crea una sesión
   *
   * @param id
   * @param ficheroSesion
   * @param mapaAsientos
   */
  public Sesion(int id, String ficheroSesion, String mapaAsientos) 
          throws FileNotFoundException {

    Scanner scanner = new Scanner(new File(ficheroSesion));

    this.id = id;
    this.numSala = scanner.nextInt();

    scanner.nextLine();

    this.pelicula = scanner.nextLine();
    this.horaInicio = scanner.nextLine();
    this.horaFin = scanner.nextLine();
    this.fecha = scanner.nextLine();
    platea = new Platea(mapaAsientos);
  }

  /**
   * Genera el mapa de ocupación para una platea
   *
   */
  public void generarMapaOcupacion() throws FileNotFoundException {
    platea.generarMapaOcupacion();
  }

  public int devuelveId() {
    return id;
  }

  public String devuelvePelicula() {
    return pelicula;
  }

  public String devuelveHoraInicio() {
    return horaInicio;
  }

  public String devuelveHoraFin() {
    return horaFin;
  }

  public String devuelveFecha() {
    return fecha;
  }

  public int devuelveNumSala() {
    return numSala;
  }

  public Platea devuelvePlatea() {
    return platea;
  }

  /**
   * String devuelve entrada
   * @param fila
   * @param numero
   * @return
   */
  public String comprarEntrada(int fila, int numero) {

    String s = "";
    s = s + devuelvePelicula() + "\n" + "Sesion "
            + devuelveHoraInicio() + " - "
            + devuelveHoraFin() + " - "
            + devuelveFecha() + "\n"
            + platea.comprarEntrada(fila, numero);

    return s;
  }
}