package Udistrital.avanzada.ArgollaLlanera.control;

import Udistrital.avanzada.ArgollaLlanera.modelo.Equipo;
import Udistrital.avanzada.ArgollaLlanera.modelo.Jugador;
import Udistrital.avanzada.ArgollaLlanera.vista.VistaJuego;

import javax.swing.JButton;
import java.util.List;
import java.util.Map;

public class ControlVista {

    private VistaJuego vista;
    private ControlJuego controlJuego;

    public ControlVista(VistaJuego vista) {
        this.vista = vista;
        configurarEventos();
    }

    public void setControlJuego(ControlJuego controlJuego) {
        this.controlJuego = controlJuego;
    }

    private void configurarEventos() {
        vista.getBtnLanzar().addActionListener(e -> {
            if (controlJuego != null) {
                controlJuego.lanzarTurno();
            }
        });
        vista.getBtnSalir().addActionListener(e -> {
            if (controlJuego != null) {
                controlJuego.salir();
            }
        });
    }

    public JButton getBtnLanzar() {
        return vista.getBtnLanzar();
    }

    public JButton getBtnSalir() {
        return vista.getBtnSalir();
    }

    public void setTurno(String nombreEquipo) {
        vista.setTurno(nombreEquipo);
    }

    public void mostrarEquipos(List<Equipo> equipos) {
        vista.mostrarEquipos(equipos);
    }

    public void actualizarVista(Map<Equipo, Integer> puntajes) {
        vista.actualizarVista(puntajes);
    }

    public void actualizarPuntajes(int puntosA, int puntosB) {
        vista.actualizarPuntajes(puntosA, puntosB);
    }

    public void actualizarDetalle(String detalle) {
        vista.actualizarDetalleLanzamiento(detalle);
    }

    public void resaltarEquipo(Equipo equipo) {
        vista.resaltarEquipo(equipo);
    }

    public void resaltarJugador(Jugador jugador) {
        vista.resaltarJugador(jugador);
    }

    public void mostrarMensajeEnVista(String mensaje) {
        vista.mostrarMensajeEnVista(mensaje);
    }

    public void mostrarMensaje(String mensaje) {
        vista.mostrarMensaje(mensaje);
    }

    public void mostrarGanador(Equipo equipo) {
        vista.mostrarGanador(equipo);
    }

    public VistaJuego getVista() {
        return vista;
    }
}
