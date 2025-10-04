/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Udistrital.avanzada.ArgollaLlanera.control;

import Udistrital.avanzada.ArgollaLlanera.modelo.Equipo;
import Udistrital.avanzada.ArgollaLlanera.modelo.Juego;
import Udistrital.avanzada.ArgollaLlanera.modelo.Jugador;
import Vista.VistaJuego;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 *
 * @author juanr
 */


public class ControlJuego {

    private Juego juego;
    private VistaJuego vista;
    private ControlPersistencia persistencia;
    private List<Equipo> equipos;
    private int rondasJugadas;
    private final String fotoGenerica = "img/jugador.png"; // Ruta relativa o nombre de archivo

    public ControlJuego(VistaJuego vista) {
        this.vista = vista;
        this.persistencia = new ControlPersistencia();
        this.equipos = new ArrayList<>();
        this.rondasJugadas = 0;
    }

    /**
     * Carga los equipos y jugadores desde archivo de propiedades.
     */
    public void cargarEquipos(File archivoPropiedades) {
        try (FileInputStream fis = new FileInputStream(archivoPropiedades)) {
            Properties props = new Properties();
            props.load(fis);

            int numEquipos = Integer.parseInt(props.getProperty("equipos"));
            for (int i = 1; i <= numEquipos; i++) {
                String nombreEquipo = props.getProperty("equipo" + i);
                List<Jugador> jugadores = new ArrayList<>();
                for (int j = 1; j <= 4; j++) {
                    String nombre = props.getProperty("equipo" + i + ".jugador" + j + ".nombre");
                    String apodo = props.getProperty("equipo" + i + ".jugador" + j + ".apodo");
                    jugadores.add(new Jugador(nombre, apodo, fotoGenerica));
                }
                equipos.add(new Equipo(nombreEquipo, jugadores));
            }

            juego = new Juego(equipos);
            vista.mostrarEquipos(equipos);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ... (resto de métodos: jugarRonda, guardarResultado, salir, etc.)
}