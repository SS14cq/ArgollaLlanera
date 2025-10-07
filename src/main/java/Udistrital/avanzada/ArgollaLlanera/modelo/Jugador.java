package Udistrital.avanzada.ArgollaLlanera.modelo;

import java.io.Serializable;

/**
 * Clase Jugador que representa un jugador dentro de un equipo determinado.
 * Recibe nombre, apodo y foto (genérica).
 * @author juan-
 * @version 1.1
 */
public class Jugador implements Serializable {

    private String nombre;
    private String apodo;
    private String foto;

    // Campos transitorios que no se serializan
    private transient int puntos;
    private transient String resultado;

    /**
     * Constructor que recibe los atributos para crear un objeto Jugador.
     * @param nombre Nombre del jugador.
     * @param apodo Apodo del jugador.
     * @param foto Ruta o nombre de la imagen del jugador.
     */
    public Jugador(String nombre, String apodo, String foto) {
        this.nombre = nombre;
        this.apodo = apodo;
        this.foto = foto;
        this.puntos = 0;
        this.resultado = null;
    }

    public void agregarPuntosJugador(int puntosJugador) {
        this.puntos += puntosJugador;
    }

    public void reset() {
        this.puntos = 0;
        this.resultado = null;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApodo() {
        return apodo;
    }

    public void setApodo(String apodo) {
        this.apodo = apodo;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public int getPuntos() {
        return puntos;
    }

    public void setPuntos(int puntos) {
        this.puntos = puntos;
    }

    public String getResultado() {
        return resultado;
    }

    public void setResultado(String resultado) {
        this.resultado = resultado;
    }

    @Override
    public String toString() {
        return nombre + " (" + apodo + ") - " + resultado + ": " + puntos + " pts";
    }
}
