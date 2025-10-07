/*
 Prueba de la clase ControladorJugadores que busca verificar que esta clase modifique la lista
 de los jugadores con los metodos crearJugador(), resetearPuntosJugador y añadirPuntosJugador().
 */
package Udistrital.avanzada.ArgollaLlanera.control;

import Udistrital.avanzada.ArgollaLlanera.modelo.Jugador;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author juan-
 */
public class ControladorJugadoresTest {

    public ControladorJugadoresTest() {
    }

    @BeforeAll
    public static void setUpClass() {
    }

    @AfterAll
    public static void tearDownClass() {
    }

    @BeforeEach
    public void setUp() {
    }

    @AfterEach
    public void tearDown() {
    }

    /**
     * Prueba al metodo crearJugador de la clase ControladorJugador
        esta prueba verifica que el jugador se cree correctamente y 
        se agregue a la lista interna de jugadores
     */
    @org.junit.jupiter.api.Test
    public void testCrearJugador() {
        //Crea el jugador de ejemplo
        System.out.println("crearJugador");
        String nombre = "Mario";
        String apodo = "Babilla coja";
        String foto = "foto.jpg";
        ControladorJugadores instance = new ControladorJugadores();
        //se instancia al jugador con los parametros nombre apodo y edad
        instance.crearJugador(nombre, apodo, foto);
        List<Jugador> jugadores = instance.getJugadores();
        //se verifica que el jugador de ejemplo se agregue a la lista interna de jugadores
        assertEquals(1, jugadores.size(), "Debe haber un jugador en la lista");
        Jugador jugador = jugadores.get(0);
        assertEquals(nombre, jugador.getNombre());
        assertEquals(apodo, jugador.getApodo());
        assertEquals(foto, jugador.getFoto());
    }

    /**
     * Pureba del metodo resetearPuntosJugador() de la clase  ControlJugadores
     * Con esta prueba se verifica que luego de iniciar la segunda ronda los puntos de 
     * los jugadores se reseteen.
     */
    @org.junit.jupiter.api.Test
    public void testResetearPuntosJugador() {
        System.out.println("resetearPuntosJugador");
        //Se escoge al jugador para la prueba
        Jugador jugador = new Jugador("Marcos", "Alacran", "foto.jpg");
        //Se inicializa los puntos del jugador en 100
        jugador.setPuntos(100);
        ControladorJugadores instance = new ControladorJugadores();
        instance.resetearPuntosJugador(jugador);
        // Verificar que los puntos se hayan reseteado
        assertEquals(0, jugador.getPuntos(), "Los puntos del jugador deben ser 0 después de resetear");
    }

    /**
     * Prueba del metodo añadirPuntosJugador() de la clase ControlJugadores
     * Con esta prueba se verifica que el metodo añada los puntos a cada jugador 
     */
    @org.junit.jupiter.api.Test
    public void testAñadirPuntosJugador() {
        System.out.println("a\u00f1adirPuntosJugador");
        //Se elige al jugador de prueba
        Jugador jugador = new Jugador("Mercedes", "Florentina", "foto.jpg");
        //se inicializa los puntos del jugador en 50
        jugador.setPuntos(50);
        //se suman 21 puntos al jugador
        int puntos = 21;
        ControladorJugadores instance = new ControladorJugadores();
        instance.añadirPuntosJugador(jugador, puntos);
        //imprime el puntaje total del jugador instanciado
        assertEquals(71, jugador.getPuntos(), "Los puntos del jugador deben ser 71 después de añadir 20");
    }

}
