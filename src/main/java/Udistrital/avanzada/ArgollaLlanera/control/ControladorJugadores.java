
package Udistrital.avanzada.ArgollaLlanera.control;

import Udistrital.avanzada.ArgollaLlanera.modelo.Jugador;
import java.util.ArrayList;

/**
 *
 * @author juan-
 * @version 1.0
 */
public class ControladorJugadores {
    private Jugador jugador;
    private ArrayList jugadores;
    
    public ControladorJugadores(ControlAplicativo ca){
        jugadores = new ArrayList<Jugador>();
    }
    public void crearJugador(String nombre, String apodo, String foto){
        jugador = new Jugador(nombre, apodo, foto);
        jugadores.add(jugador);
    }
    
    public void resetearPuntosJugador(){
        jugador.setPuntos(0);
    }
    
    public void añadirPuntosJugador(int puntos){
        jugador.agregarPuntosJugador(puntos);
    }
}
