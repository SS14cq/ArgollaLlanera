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
     * Constructor que crea un equipo con clave y nombre.
     * 
     * @param clave Identificador único del equipo
     * @param nombre Nombre legible del equipo
     */
    public Equipo(String clave, String nombre) {
        this.clave = clave;
        this.nombre = nombre;
        this.jugadores = new ArrayList<>();
    }

    /** @return La clave del equipo */
    public String getClave() { return clave; }

    /** @return El nombre del equipo */
    public String getNombre() { return nombre; }

    /**
     * Devuelve la lista de jugadores de forma inmutable (solo lectura).
     * 
     * @return lista inmutable de jugadores
     */
    public List<Jugador> getJugadores() {
        return Collections.unmodifiableList(jugadores);
    }

    /**
     * Agrega un jugador al equipo si no es nulo.
     * 
     * @param jugador el jugador a agregar
     * @return {@code true} si se agregó con éxito, {@code false} si el jugador es nulo
     */
    public boolean agregarJugador(Jugador jugador) {
        if (jugador != null) {
            return jugadores.add(jugador);
        }
        return false;
    }

    /** @return la cantidad actual de jugadores en el equipo */
    public int getCantidadJugadores() {
        return jugadores.size();
    }

    @Override
    public String toString() {
        return String.format("Equipo[clave=%s, nombre=%s, jugadores=%s]", clave, nombre, jugadores);
    }
}
