package Controlador;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import Modelo.Sala;
import Modelo.Sesion;
import Vista.CineVista;

/**
 * Cine
 *
 * @author Alex Costea y Aitor Mari
 */
public class Cine implements OyenteVista {

  private final String nombre = "Rialto";
  Sesion sesion;
  ArrayList<Sala> salas = new ArrayList<Sala>();
  Iterator<Sala> nueva = salas.iterator();
  
  static final String msgError = "Error! Sala no existe: ";
  static final String RUTAMAPA = "mapaOcupacion.txt";
  static final String MAPA = "mapaAsientos.txt";
  
  private final String FORMATO_ARCHIVO = ".txt";
  static final String SEPARADOR = "-";
 
  private final int NUM_FILAS = 10;
  private final int NUM_COLUMNAS = 21;

  private String ficheroSesion;
  private final CineVista vista;

  private int numeroEntrada = 1; //variable para nombrar los archivos de texto	
  private int idSesion = 1; //id de sesion, que se irá autoincrementando
  private int fila, numero;
  private boolean eventoRaton = true;

  /**
   * Crea un cine
   */
  public Cine() throws FileNotFoundException {
   /**
    *Crea 3 salas con sus respectivos id y un mapa con la disposición de 
    *los asientos
    */
    nuevaSala(1, MAPA);
    nuevaSala(2, MAPA);
    nuevaSala(3, MAPA);

    vista = CineVista.instancia(this, NUM_FILAS, NUM_COLUMNAS);
  }

  /**
   * Crea una sala para un cine
   */
  boolean nuevaSala(int numero, String mapaAsientos) {
    salas.add(new Sala(numero, mapaAsientos));
    return true;
  }

  /**
   * Dado un entero, busca una platea que lo tenga como número de sala
   * @param numero
   */
  public Sala buscarSala(int numero) {
    for (int i = 0; i < salas.size(); i++) {
      Sala sala = salas.get(i);
      if (sala.devuelveNumero() == numero) {
        return sala;
      }
    }
    return null;
  }

  /**
   * Diseña el mapa de ocupación de los asientos creados de una platea de una
   * sesión de una sala
   *
   * @param infoSesion
   */
  public void generarMapaOcupacion(Object infoSesion) 
          throws FileNotFoundException {

    //Obtenemos la información de la sesión en un objeto que parsearemos
    String string = (String) infoSesion;
    String[] partes = string.split(SEPARADOR);
    String part1 = partes[0];
    String part2 = partes[1];

    int numSala = Integer.parseInt(part1);
    int id_sesion = Integer.parseInt(part2);

    Sala sala = buscarSala(numSala);
    sala.generarMapaOcupacion(id_sesion);

    vista.iniciarTableroVista(numSala,id_sesion, sala.devuelvePlatea(id_sesion),
            eventoRaton);
  }

  public String devuelveNombre() {
    return nombre;
  }

  /**
   * String devuelve entrada
   *
   * @param cadena
   * @return
   */
  public String comprarEntrada(Object cadena) throws FileNotFoundException {
    //Obtenemos la informacion en forma de Object para realizar una venta
    String string = (String) cadena;
    String[] partes = string.split(SEPARADOR);

    String part1 = partes[0];
    String part2 = partes[1];
    String part3 = partes[2];
    String part4 = partes[3];

    int fila = Integer.parseInt(part3);
    int numero = Integer.parseInt(part4);
    int numSala = Integer.parseInt(part1);
    int id_sesion = Integer.parseInt(part2);
    
    String s = "";
    try {
      Sala sala = buscarSala(numSala);
      sala.buscarSesion(id_sesion);

      s = devuelveNombre() + "\n";
      s = s + sala.comprarEntrada(id_sesion, fila, numero);

      FileWriter fichero = null;
      try {
        String rutaEntrada = "entradas/entrada" 
                + numeroEntrada 
                + FORMATO_ARCHIVO;
        numeroEntrada++;

        fichero = new FileWriter(rutaEntrada, true);
        PrintWriter pw1 = new PrintWriter(fichero);
        pw1.println(s);

        fichero.close();
      } catch (IOException ex) {
        System.out.println("Fallo con los ficheros" + ex.getMessage());
      }
    } catch (NullPointerException e) {

    }

    generarMapaOcupacion(numSala + "-" + id_sesion);
    return s;
  }
  
  /**
   *  Añade una nueva sesión
   *
   */
  private void añadirSesion() throws FileNotFoundException {
    ficheroSesion = vista.seleccionarFichero(vista.ABRIR_FICHERO);
    if (ficheroSesion != null) {
      Scanner scanner = new Scanner(new File(ficheroSesion));
      int numSala = scanner.nextInt();
      Sala sala = buscarSala(numSala);

      try {
        sesion = new Sesion(idSesion, ficheroSesion, 
                sala.devuelveMapaAsientos());
        idSesion++;
        
        sala.nuevaSesion(sesion);
        vista.añadirSesion(sesion);

      } catch (FileNotFoundException e1) {
        vista.mensajeDialogo(vista.FICHERO_NO_ENCONTRADO);
      }
    }
  }

  public void setNumero(int numero) {
    this.numero = numero;
  }

  public void setFila(int fila) {
    this.fila = fila;
  }

  public int getFila() {
    return fila;
  }

  public int getNumero() {
    return numero;
  }

  public void salir() {
    System.exit(0);
  }

  @Override
  public void notificacion(Evento evento, Object obj) {
    switch (evento) {
      case NUEVA: {
        try {
          añadirSesion();
        } catch (FileNotFoundException ex) {
          Logger.getLogger(Cine.class.getName()).log(Level.SEVERE, null, ex);
        }
      }
      break;

      case SALIR:
        salir();
        break;

      case VER_MAPA: {
        try {
          generarMapaOcupacion(obj);
        } catch (FileNotFoundException ex) {
          Logger.getLogger(Cine.class.getName()).log(Level.SEVERE, null, ex);
        }
      }
      break;

      case COMPRAR_ENTRADA: {
        try {
          comprarEntrada(obj);
        } catch (FileNotFoundException ex) {
          Logger.getLogger(Cine.class.getName()).log(Level.SEVERE, null, ex);
        }
      }
      break;
    }
  }
  
  public String toString() {
        return "Cine " + nombre;
    }

  public static void main(String[] args) throws FileNotFoundException {
    new Cine();
  }
}
