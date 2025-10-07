package Udistrital.avanzada.ArgollaLlanera.control;

import Udistrital.avanzada.ArgollaLlanera.modelo.Jugador;
import java.util.ArrayList;

/**
 * ControladorJugadores para gestión simple de jugadores.
 */
public class ControladorJugadores {

    private ArrayList<Jugador> jugadores;

    public ControladorJugadores() {
        jugadores = new ArrayList<>();
    }

    public void crearJugador(String nombre, String apodo, String foto) {
        Jugador jugador = new Jugador(nombre, apodo, foto);
        jugadores.add(jugador);
    }

    public void resetearPuntosJugador(Jugador jugador) {
        if (jugador != null) {
            jugador.setPuntos(0);
        }
    }

    public void añadirPuntosJugador(Jugador jugador, int puntos) {
        if (jugador != null) {
            jugador.setPuntos(jugador.getPuntos() + puntos);
        }
    }

    public ArrayList<Jugador> getJugadores() {
        return jugadores;
    }
}