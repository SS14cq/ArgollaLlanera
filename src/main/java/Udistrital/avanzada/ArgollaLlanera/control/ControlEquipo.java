package Udistrital.avanzada.ArgollaLlanera.control;

import Udistrital.avanzada.ArgollaLlanera.modelo.Equipo;
import Udistrital.avanzada.ArgollaLlanera.modelo.Jugador;
import Udistrital.avanzada.ArgollaLlanera.modelo.ConexionPropiedades;

import java.util.*;

/**
 * Controlador encargado de manejar la lógica de negocio relacionada con los equipos.
 * <p>
 * Este controlador permite cargar equipos desde un archivo de propiedades,
 * agregar jugadores a equipos existentes, listar equipos y calcular o asignar
 * puntos a los jugadores.
 * </p>
 * 
 * @author Sofia
 * @version 1.0
 */
public class ControlEquipo {

    /** Almacena los equipos cargados, indexados por su clave única. */
    private final Map<String, Equipo> equipos;

    /**
     * Constructor que inicializa el controlador y su almacenamiento interno.
     */
    public ControlEquipo() {
        equipos = new HashMap<>();
    }

    /**
     * Carga los equipos desde un archivo de propiedades usando la clase ConexionPropiedades.
     * <p>
     * El archivo debe cumplir con el formato esperado por
     * {@link ConexionPropiedades#cargarEquiposDesdeProperties(String)}.
     * </p>
     *
     * @param rutaArchivo ruta al archivo .properties que contiene la información de equipos y jugadores
     * @throws Exception si ocurre un error durante la carga
     */
    public void cargarEquiposDesdeArchivo(String rutaArchivo) throws Exception {
        List<Equipo> equiposCargados = ConexionPropiedades.cargarEquiposDesdeProperties(rutaArchivo);
        equipos.clear();
        for (Equipo e : equiposCargados) {
            equipos.put(e.getClave(), e);
        }
    }

    /**
     * Obtiene un equipo dado su clave.
     * 
     * @param clave clave del equipo a buscar
     * @return objeto Equipo o {@code null} si no se encuentra
     */
    public Equipo getEquipo(String clave) {
        return equipos.get(clave);
    }

    /**
     * Lista todos los equipos cargados.
     * 
     * @return colección inmutable de equipos
     */
    public Collection<Equipo> listarEquipos() {
        return Collections.unmodifiableCollection(equipos.values());
    }

    /**
     * Agrega un jugador a un equipo basado en la clave de equipo indicada.
     * 
     * @param clave clave del equipo
     * @param jugador jugador a agregar
     * @return {@code true} si se agregó correctamente; {@code false} si el equipo no existe o el jugador es {@code null}
     */
    public boolean agregarJugadorAEquipo(String clave, Jugador jugador) {
        Equipo e = equipos.get(clave);
        return e != null && e.agregarJugador(jugador);
    }

    /**
     * Calcula la suma de los puntos de todos los jugadores en un equipo.
     * 
     * @param clave clave del equipo
     * @return suma de puntos de los jugadores o -1 si no existe el equipo
     */
    public int calcularPuntosEquipo(String clave) {
        Equipo e = equipos.get(clave);
        if (e == null) return -1;
        return e.getJugadores().stream().mapToInt(Jugador::getPuntos).sum();
    }

    /**
     * Asigna una cantidad de puntos a todos los jugadores del equipo.
     * 
     * @param clave clave del equipo
     * @param puntos puntos a asignar a cada jugador
     * @return {@code true} si el equipo existe y se asignaron los puntos, {@code false} en caso contrario
     */
    public boolean asignarPuntosEquipo(String clave, int puntos) {
        Equipo e = equipos.get(clave);
        if (e == null) return false;
        e.getJugadores().forEach(j -> j.agregarPuntosJugador(puntos));
        return true;
    }
}
