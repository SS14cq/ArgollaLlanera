package Udistrital.avanzada.ArgollaLlanera.modelo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.Random;

/**
 * Clase Juego
 *
 * Representa la lógica principal del juego Argolla Llanera,
 * controlando el estado de los equipos, generación de resultados aleatorios,
 * cálculo de puntos, y evaluación de condiciones de victoria y empate.
 *
 * Esta clase actúa como el modelo central para puntajes y reglas del juego.
 * 
 * @author juan
 * @version 1.0
 */
public class Juego {

    /** Lista de equipos que participan en este juego */
    private List<Equipo> equipos;

    /** Mapa que almacena los puntajes actuales de cada equipo */
    private Map<Equipo, Integer> puntajes;

    /** Indica si actualmente hay empate según reglas del juego */
    private boolean empate;

    /**
     * Constructor que inicializa el juego con los equipos participantes.
     * Inicializa los puntajes a cero.
     *
     * @param equipos lista de equipos en la partida
     */
    public Juego(List<Equipo> equipos) {
        this.equipos = equipos;
        this.puntajes = new HashMap<>();
        for (Equipo equipo : equipos) {
            puntajes.put(equipo, 0);
        }
        this.empate = false;
    }

    /**
     * Genera el resultado completo del lanzamiento para un equipo
     * sumando puntos de todos sus jugadores.
     * No se usa por el controlador principal, que lanza jugador a jugador.
     *
     * @param equipo equipo a lanzar
     * @return puntaje total después del lanzamiento
     */
    public int lanzarEquipo(Equipo equipo) {
        int total = 0;
        for (Jugador jugador : equipo.getJugadores()) {
            String resultado = generarResultadoAleatorio();
            int puntos = calcularPuntos(resultado);
            jugador.setResultado(resultado);
            jugador.setPuntos(puntos);
            total += puntos;
        }
        puntajes.put(equipo, puntajes.get(equipo) + total);
        return puntajes.get(equipo);
    }

    /**
     * Genera un resultado aleatorio para un lanzamiento individual,
     * elegido de un conjunto de opciones válidas del juego.
     *
     * @return String resultado aleatorio
     */
    public String generarResultadoAleatorio() {
        String[] opciones = {"moñona", "engarzada", "hueco", "palmo", "timbre", "otro"};
        Random rand = new Random();
        return opciones[rand.nextInt(opciones.length)];
    }

    /**
     * Calcula los puntos obtenidos para un resultado dado,
     * según la tabla de puntajes definida en el juego.
     *
     * @param resultado texto que indica el resultado del lanzamiento
     * @return puntos asociados al resultado
     */
    public int calcularPuntos(String resultado) {
        switch(resultado) {
            case "moñona": return 8;
            case "engarzada": return 5;
            case "hueco": return 3;
            case "palmo": return 2;
            case "timbre": return 1;
            default: return 0;
        }
    }

    /**
     * Determina si un equipo ha ganado con base en los puntos acumulados,
     * considerando la regla de mínimo 21 puntos para ganar.
     *
     * @param equipo equipo a evaluar
     * @return true si el equipo tiene al menos 21 puntos, false en otro caso
     */
    public boolean equipoHaGanado(Equipo equipo) {
        return puntajes.get(equipo) >= 21;
    }

    /**
     * Verifica si existe empate, definido como que más de un equipo haya alcanzado o superado 21 puntos.
     *
     * @return true si hay empate, false en caso contrario
     */
    public boolean hayEmpate() {
        List<Integer> puntos = new ArrayList<>(puntajes.values());
        int conteoMayoresIguales21 = 0;
        for (Integer p : puntos) {
            if (p >= 21) conteoMayoresIguales21++;
        }
        return conteoMayoresIguales21 > 1;
    }

    /**
     * Obtiene el mapa completo de puntajes de todos los equipos en el juego.
     *
     * @return mapa con clave equipo y valor su puntaje actual
     */
    public Map<Equipo, Integer> getPuntajes() {
        return puntajes;
    }
}
