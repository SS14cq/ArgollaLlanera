package Udistrital.avanzada.ArgollaLlanera.control;

import Udistrital.avanzada.ArgollaLlanera.modelo.*;
import java.io.IOException;
import java.util.*;

/**
 * Controlador encargado de manejar toda la lógica de negocio relacionada con los equipos.
 * 
 * <p>Incluye la carga de equipos desde archivos .properties, la gestión de jugadores,
 * y la asignación o cálculo de puntos. De esta forma concentra la lógica del dominio
 * sin depender de la vista ni alterar la capa del modelo.</p>
 * 
 * @author Sofia
 * @version 1.1
 */
public class ControlEquipo {

    /** Almacena los equipos cargados, indexados por su clave única. */
    private final Map<String, Equipo> equipos;

    /** Constructor que inicializa la estructura interna. */
    public ControlEquipo() {
        equipos = new HashMap<>();
    }

    /**
     * Carga los equipos desde un archivo .properties utilizando {@link ConexionPropiedades}.
     *
     * @param rutaArchivo ruta al archivo .properties que contiene la información de los equipos
     * @throws IOException si ocurre un error al leer el archivo
     */
    public void cargarEquiposDesdeArchivo(String rutaArchivo) throws IOException {
        ConexionPropiedades conexion = ConexionPropiedades.loadFromFile(rutaArchivo);
        Properties props = conexion.asProperties();
        List<Equipo> equiposCargados = construirEquiposDesdeProperties(props);
        equipos.clear();
        for (Equipo e : equiposCargados) {
            equipos.put(e.getClave(), e);
        }
    }

    /**
     * Construye los objetos Equipo y Jugador a partir de las propiedades cargadas.
     * 
     * <p>Este método se separa de la lectura del archivo para mantener el principio SRP,
     * ya que aquí solo se construyen los objetos del dominio sin manejar entradas o salidas.</p>
     */
    private List<Equipo> construirEquiposDesdeProperties(Properties props) {
        List<Equipo> equipos = new ArrayList<>();
        int count = Integer.parseInt(props.getProperty("numeroEquipos.count", "0"));

        for (int i = 1; i <= count; i++) {
            String clave = props.getProperty("equipo." + i + ".clave");
            String nombre = props.getProperty("equipo." + i + ".name");
            int numJugadores = Integer.parseInt(props.getProperty("equipo." + i + ".numeroJugadores", "0"));

            Equipo equipo = new Equipo(clave, nombre);

            for (int j = 1; j <= numJugadores; j++) {
                String jnombre = props.getProperty("equipo." + i + ".player." + j + ".name");
                if (jnombre == null) continue;
                String japodo = props.getProperty("equipo." + i + ".player." + j + ".nick", "");
                String jfoto = props.getProperty("equipo." + i + ".player." + j + ".photo", "");
                Jugador jugador = new Jugador(jnombre, japodo, jfoto);
                equipo.agregarJugador(jugador);
            }
            equipos.add(equipo);
        }
        return equipos;
    }

    /** Obtiene un equipo por su clave. */
    public Equipo getEquipo(String clave) {
        return equipos.get(clave);
    }

    /** Devuelve una colección inmutable de todos los equipos cargados. */
    public Collection<Equipo> listarEquipos() {
        return Collections.unmodifiableCollection(equipos.values());
    }

    /** Agrega un jugador a un equipo. */
    public boolean agregarJugadorAEquipo(String clave, Jugador jugador) {
        Equipo e = equipos.get(clave);
        return e != null && e.agregarJugador(jugador);
    }

    /** Calcula los puntos totales del equipo. */
    public int calcularPuntosEquipo(String clave) {
        Equipo e = equipos.get(clave);
        if (e == null) return -1;
        return e.getJugadores().stream().mapToInt(Jugador::getPuntos).sum();
    }

    /** Asigna una cantidad de puntos a todos los jugadores del equipo. */
    public boolean asignarPuntosEquipo(String clave, int puntos) {
        Equipo e = equipos.get(clave);
        if (e == null) return false;
        e.getJugadores().forEach(j -> j.agregarPuntosJugador(puntos));
        return true;
    }
}
