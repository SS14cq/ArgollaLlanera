package Udistrital.avanzada.ArgollaLlanera.control;
import Udistrital.avanzada.ArgollaLlanera.modelo.Equipo;
import Udistrital.avanzada.ArgollaLlanera.modelo.Juego;
import Udistrital.avanzada.ArgollaLlanera.modelo.Jugador;
import Vista.VistaJuego;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class ControlJuego {

    private VistaJuego vista;
    private Juego juego;
    private ControlPersistencia persistencia;
    private ControlEquipo controlEquipo;
    private List<Equipo> equipos;
    private int rondasJugadas;

    public ControlJuego(VistaJuego vista) {
        this.vista = vista;
        this.persistencia = new ControlPersistencia();
        this.controlEquipo = new ControlEquipo();
        this.equipos = new ArrayList<>();
        this.rondasJugadas = 0;
        configurarEventos();
    }

    private void configurarEventos() {
        vista.getBtnLanzar().addActionListener(e -> jugarRonda());
        vista.getBtnSalir().addActionListener(e -> salir());
    }

    public void cargarEquipos(File archivoPropiedades) {
        try {
            controlEquipo.cargarEquiposDesdeArchivo(archivoPropiedades.getAbsolutePath());
            equipos = new ArrayList<>(controlEquipo.listarEquipos());
            juego = new Juego(equipos);
            vista.mostrarEquipos(equipos);
        } catch (IOException e) {
            vista.mostrarMensaje("Error al cargar equipos: " + e.getMessage());
        }
    }

    public void jugarRonda() {
        if (rondasJugadas >= 2) {
            vista.mostrarMensaje("Ya se jugaron las dos rondas permitidas.");
            return;
        }

        Equipo ganador = juego.jugarRonda();
        vista.actualizarVista(juego.getPuntajes());

        if (ganador != null) {
            guardarResultado(ganador, "Ganó");
            for (Equipo equipo : equipos) {
                if (!equipo.equals(ganador)) {
                    guardarResultado(equipo, "Perdió");
                }
            }
            vista.mostrarGanador(ganador);
        } else if (juego.hayEmpate()) {
            vista.mostrarMensaje("Empate. Se jugará muerte súbita.");
            jugarMuerteSubita();
        }

        rondasJugadas++;
    }

    private void jugarMuerteSubita() {
        Equipo equipoA = equipos.get(0);
        Equipo equipoB = equipos.get(1);

        int puntosA = juego.lanzarEquipo(equipoA);
        int puntosB = juego.lanzarEquipo(equipoB);

        Equipo ganador = puntosA > puntosB ? equipoA : equipoB;
        Equipo perdedor = puntosA > puntosB ? equipoB : equipoA;

        guardarResultado(ganador, "Ganó");
        guardarResultado(perdedor, "Perdió");

        vista.actualizarVista(juego.getPuntajes());
        vista.mostrarGanador(ganador);
    }

    private void guardarResultado(Equipo equipo, String resultado) {
        String[] nombres = equipo.getJugadores().stream()
                .map(Jugador::getNombre)
                .toArray(String[]::new);
        persistencia.escribirRegistro(equipo.getClave(), equipo.getNombre(), nombres, resultado);
    }

    public void salir() {
        vista.mostrarMensaje("Finalizando juego...");
        persistencia.leerRegistros();
        System.exit(0);
    }
}