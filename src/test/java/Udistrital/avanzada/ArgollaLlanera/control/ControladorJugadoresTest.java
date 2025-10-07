/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
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
     * Test of crearJugador method, of class ControladorJugadores.
     */
    @org.junit.jupiter.api.Test
    public void testCrearJugador() {
        System.out.println("crearJugador");
        String nombre = "Mario";
        String apodo = "Babilla coja";
        String foto = "foto.jpg";
        ControladorJugadores instance = new ControladorJugadores();
        instance.crearJugador(nombre, apodo, foto);
        // TODO review the generated test code and remove the default call to fail.
        List<Jugador> jugadores = instance.getJugadores();
        assertEquals(1, jugadores.size(), "Debe haber un jugador en la lista");
        Jugador jugador = jugadores.get(0);
        assertEquals(nombre, jugador.getNombre());
        assertEquals(apodo, jugador.getApodo());
        assertEquals(foto, jugador.getFoto());
    }

    /**
     * Test of resetearPuntosJugador method, of class ControladorJugadores.
     */
    @org.junit.jupiter.api.Test
    public void testResetearPuntosJugador() {
        System.out.println("resetearPuntosJugador");
        Jugador jugador = new Jugador("Marcos", "Alacran", "foto.jpg");
        jugador.setPuntos(100);
        ControladorJugadores instance = new ControladorJugadores();
        instance.resetearPuntosJugador(jugador);
        // Verificar que los puntos se hayan reseteado
        assertEquals(0, jugador.getPuntos(), "Los puntos del jugador deben ser 0 después de resetear");
    }

    /**
     * Test of añadirPuntosJugador method, of class ControladorJugadores.
     */
    @org.junit.jupiter.api.Test
    public void testAñadirPuntosJugador() {
        System.out.println("a\u00f1adirPuntosJugador");
        Jugador jugador = new Jugador("Mercedes", "Florentina", "foto.jpg");
        jugador.setPuntos(50);
        int puntos = 21;
        ControladorJugadores instance = new ControladorJugadores();
        instance.añadirPuntosJugador(jugador, puntos);
        // TODO review the generated test code and remove the default call to fail.
        assertEquals(71, jugador.getPuntos(), "Los puntos del jugador deben ser 71 después de añadir 30");
    }

}
