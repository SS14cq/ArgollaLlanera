package Udistrital.avanzada.ArgollaLlanera.control;

import Udistrital.avanzada.ArgollaLlanera.modelo.Jugador;
import java.util.ArrayList;

/**
 * ControladorJugadores
 *
 * Clase encargada de la gestión simple de objetos Jugador.
 * Provee métodos para crear jugadores y modificar sus puntos.
 * Se utiliza como auxiliar para administrar la lista de jugadores
 * en el contexto del juego Argolla Llanera.
 * 
 * No implementa la lógica de negocio completa, solo tareas específicas a jugadores.
 * 
 * @author juan valbuena
 * @version 1.0
 */
public class ControladorJugadores {

    // Lista interna que almacena los jugadores creados
    private ArrayList<Jugador> jugadores;

    /**
     * Constructor que inicializa la lista interna de jugadores vacía.
     */
    public ControladorJugadores() {
        jugadores = new ArrayList<>();
    }

    /**
     * Crea un nuevo jugador con nombre, apodo y foto, y lo agrega 
     * a la lista interna de jugadores.
     * 
     * @param nombre nombre completo o identificador del jugador
     * @param apodo sobrenombre o alias del jugador
     * @param foto ruta o nombre de la imagen que representa al jugador
     */
    public void crearJugador(String nombre, String apodo, String foto) {
        Jugador jugador = new Jugador(nombre, apodo, foto);
        jugadores.add(jugador);
    }

    /**
     * Resetea los puntos acumulados del jugador a cero.
     * 
     * @param jugador instancia del jugador a resetear
     */
    public void resetearPuntosJugador(Jugador jugador) {
        jugador.setPuntos(0);
    }

    /**
     * Suma una cantidad de puntos al total actual del jugador.
     * 
     * @param jugador instancia del jugador a modificar
     * @param puntos cantidad de puntos a añadir (positiva o negativa)
     */
    public void añadirPuntosJugador(Jugador jugador, int puntos) {
        jugador.agregarPuntosJugador(puntos);
    }
}
