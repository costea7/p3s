package Modelo;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Clase Platea
 *
 * @author Alex Costea y Aitor Mari
 */
public class Platea {

  private int asientosTotales = 0;
  private int asientosLibres = 0;
  private int asientosOcupados = 0;
  
  private final String mapa;
  private final Asiento[][] asientos;
  private Casilla[][] casillas;
  
  private int filas = 1;
  private int columnas = 0;

  static final String LIBRE = "- ";
  static final String OCUPADO = "X ";
  
  
  static final int FILAS = 10;
  static final int COLUMNAS = 21;

  /**
   * Construye una platea
   *
   * @param mapa
   * @throws java.io.FileNotFoundException
   */
  public Platea(String mapa) throws FileNotFoundException {
    this.asientosLibres = 0;
    this.mapa = mapa;
    asientos = new Asiento[FILAS][COLUMNAS];
    casillas = new Casilla[FILAS][COLUMNAS];
    crearAsientos();
  }

  /**
   * Lee el archivo de texto para crear los asientos
   *
   */
  public void crearAsientos() throws FileNotFoundException {
    Scanner scanner = new Scanner(new File(mapa));
    int asientoTemp;
    filas = 0;
    columnas = -1;

    int filaActual = 1;

    while (scanner.hasNextInt()) {
      int fila;
      int numero;
      
      //almacena enteros(4 dígitos) del txt en una variable temporal
      asientoTemp = scanner.nextInt();

      //obtiene fila y numero del asiento
      //los primeros 2 dígitos indican la fila y las ultimas 2 el numero
      fila = asientoTemp / 100;
      numero = asientoTemp % 100;

      if (filaActual != fila) {
        filas++;
        filaActual++;
        columnas = 0;
      } else {
        columnas++;
      }

      if (numero != 0) {
        Asiento asiento = new Asiento(fila, numero);
        asientos[filas][columnas] = asiento;
        asientosTotales++;
      } else {
        asientos[filas][columnas] = null;
      }
    }
  }

  /**
   * Diseña el mapa de ocupación de los asientos creados para una platea.
   * Inicialmente todos estarán libres.
   *
   */
  public void generarMapaOcupacion() throws FileNotFoundException {

    Scanner scanner = new Scanner(new File(mapa));

    int asientoTemp; //almacena el valor del siguiente entero del archivo
    
    filas = 0;
    columnas = -1;
    int filaActual = 1;

    while (scanner.hasNextInt()) {
      int fila;
      int numero;

      asientoTemp = scanner.nextInt();

      fila = asientoTemp / 100;
      numero = asientoTemp % 100;

      if (filaActual != fila) {
        filas++;
        filaActual++;
        columnas = 0;
      } else {
        columnas++;
      }

      if (numero != 0) {
        if (!asientos[filas][columnas].estaOcupado()) {
          casillas[filas][columnas] = Casilla.LIBRE;
        } else {
          casillas[filas][columnas] = Casilla.OCUPADA;
          asientosOcupados++;
        }
      } else {
        casillas[filas][columnas] = Casilla.VACIA;
      }
    }

  }

 
  /**
   * Dadas una fila y un número, busca el asiento correspondiente en la platea
   *
   * @param fila
   * @param numero
   * @return
   */
  public Asiento buscarAsiento(int fila, int numero) {
    for (int i = 0; i < FILAS; i++) {
      for (int j = 0; i < COLUMNAS; i++) {
        if ((asientos[i][j].devuelveFila() == fila)
                && (asientos[i][j].devuelveNumero() == numero)) {
          return asientos[i][j];
        }
      }
    }
    return null;
  }

  /**
   * Devuelve el número de asientos totales de una sesión
   * 
   * @return 
   */
   public int devuelveAsientosTotales() {
    return asientosTotales;
  }
  
  /**
   * Devuelve el número de asientos libres de una sesión
   * 
   * @return 
   */
  public int devuelveAsientosLibres() {
    asientosLibres = asientosTotales - asientosOcupados;
    asientosOcupados = 0;
    return asientosLibres;
  }

  public Casilla devuelveEstadoCasilla(Asiento asiento) {
    return casillas[asiento.devuelveFila()][asiento.devuelveNumero()];
  }

  public int devuelveNumFilas() {
    return FILAS;
  }

  public int devuelveNumColumnas() {
    return COLUMNAS;
  }

  /**
   * String comprar entrada
   *
   * @param fila
   * @param numero
   * @return
   */
  public String comprarEntrada(int fila, int numero) {

    Asiento asiento = asientos[fila][numero];
    asiento.ocupar();
    casillas[fila][numero] = Casilla.OCUPADA;

    return  "Fila: " + fila + " Número: " + numero;
  }
}
