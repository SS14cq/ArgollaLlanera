package Udistrital.avanzada.ArgollaLlanera.modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Clase que representa un equipo con una clave única, nombre y una lista de jugadores.
 * Permite agregar jugadores y consultar la cantidad actual.
 * Implementa Serializable para soporte de persistencia del objeto.
 * @author Sofia
 * @version 1.0
 */
public class Equipo implements Serializable {
   

    private String clave;
    private String nombre;
    private List<Jugador> jugadores;

    /**
     * Constructor que inicializa el equipo con su clave y nombre.
     */
    public Equipo(String clave, String nombre) {
        this.clave = clave;
        this.nombre = nombre;
        this.jugadores = new ArrayList<>();
    }

    /**
     * Agrega un jugador al equipo.
     */
    public boolean agregarJugador(Jugador jugador) {
        return jugadores.add(jugador);
    }

    /**
     * Devuelve la clave única del equipo.
     */
    public String getClave() {
        return clave;
    }

    /**
     * Devuelve el nombre del equipo.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Devuelve la lista de jugadores del equipo.
     */
    public List<Jugador> getJugadores() {
        return jugadores;
    }

    /**
     * Devuelve la cantidad de jugadores en el equipo.
     */
    public int getCantidadJugadores() {
        return jugadores.size();
    }

    /**
     * Calcula los puntos totales del equipo sumando los puntos de cada jugador.
     */
    public int getPuntosTotales() {
        return jugadores.stream().mapToInt(Jugador::getPuntos).sum();
    }

    @Override
    public String toString() {
        return nombre + " (" + clave + ")";
    }
}