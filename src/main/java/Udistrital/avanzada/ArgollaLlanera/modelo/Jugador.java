package Udistrital.avanzada.ArgollaLlanera.modelo;

import java.io.Serializable;

/**
 *
 * @author juan-
 * @version 1.0
 *
 * Clase Jugador que representa 1 jugador dentro de un equipo determinado Recibe
 * nombre, apodo, foto (generica)
 */
public class Jugador implements Serializable {

    private String nombre;
    private String apodo;
    private String foto;
    private transient int puntos;

    /* Constructor que recibe los atributos para crear 1 objeto Jugador */
    public Jugador(String nombre, String apodo, String foto) {
        this.nombre = nombre;
        this.apodo = apodo;
        this.foto = foto;
        this.puntos = 0;
    }

    public void agregarPuntosJugador(int puntosJugador) {
        this.puntos += puntosJugador;
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

}
