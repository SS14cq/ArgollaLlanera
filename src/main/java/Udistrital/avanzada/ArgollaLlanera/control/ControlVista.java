package Udistrital.avanzada.ArgollaLlanera.control;

import Udistrital.avanzada.ArgollaLlanera.modelo.Equipo;
import Udistrital.avanzada.ArgollaLlanera.modelo.Jugador;
import Udistrital.avanzada.ArgollaLlanera.vista.VistaJuego;

import javax.swing.JButton;
import java.util.List;
import java.util.Map;

/**
 * ControlVista
 *
 * Controlador de la vista para la aplicación Argolla Llanera bajo el patrón MVC.
 * Se encarga de conectar la lógica de interacción de usuario con las acciones del juego,
 * escuchando eventos de la interfaz gráfica (VistaJuego) y delegando las operaciones importantes al controlador de la lógica (ControlJuego).
 *
 * Sus funciones principales incluyen:
 * - Configuración de escuchas a botones y notificación de eventos de usuario.
 * - Intermediación entre la vista y el controlador principal del juego para actualizar el estado visual.
 * - Facilitar la comunicación de mensajes, resultados y cambios de turno en la GUI.
 *
 * No guarda lógica de modelo, solo se encarga de la actualización visual y delega la gestión del juego.
 *
 * @author juanr
 * @author Sofia modificado 06-10-2025
 * @version 1.0
 */
public class ControlVista {

    private VistaJuego vista;            // Referencia a la vista principal (JFrame)
    private ControlJuego controlJuego;   // Referencia al controlador principal del juego

    /**
     * Crea el controlador de vista y registra los eventos de acción relacionados con la interfaz gráfica.
     *
     * @param vista instancia de VistaJuego asociada
     */
    public ControlVista(VistaJuego vista) {
        this.vista = vista;
        configurarEventos();
    }

    /**
     * Asocia el controlador del juego para delegar las acciones previstas de la GUI.
     * Este método permite la vinculación circular necesaria en MVC.
     *
     * @param controlJuego controlador de la lógica del juego
     */
    public void setControlJuego(ControlJuego controlJuego) {
        this.controlJuego = controlJuego;
    }

    /**
     * Configura los escuchadores de eventos sobre los botones principales de la vista
     * para ejecutar la lógica relevante en el controlador del juego.
     */
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

    /**
     * Expone el botón de lanzar argolla de la vista.
     * @return JButton asociado al lanzamiento
     */
    public JButton getBtnLanzar() {
        return vista.getBtnLanzar();
    }

    /**
     * Expone el botón de salir de la vista.
     * @return JButton asociado a salida
     */
    public JButton getBtnSalir() {
        return vista.getBtnSalir();
    }

    /**
     * Actualiza el indicador visual del turno de equipo actual en la vista.
     * @param nombreEquipo nombre del equipo en turno
     */
    public void setTurno(String nombreEquipo) {
        vista.setTurno(nombreEquipo);
    }

    /**
     * Solicita a la vista mostrar la estructura y paneles de los equipos participantes.
     * @param equipos lista de equipos a presentar
     */
    public void mostrarEquipos(List<Equipo> equipos) {
        vista.mostrarEquipos(equipos);
    }

    /**
     * Actualiza los puntajes y resultados en la vista con los valores actuales.
     * @param puntajes mapa de equipos a puntaje
     */
    public void actualizarVista(Map<Equipo, Integer> puntajes) {
        vista.actualizarVista(puntajes);
    }

    /**
     * Actualiza los labels de puntajes individuales para ambos equipos.
     * @param puntosA puntaje del equipo A
     * @param puntosB puntaje del equipo B
     */
    public void actualizarPuntajes(int puntosA, int puntosB) {
        vista.actualizarPuntajes(puntosA, puntosB);
    }

    /**
     * Actualiza el área de detalle de lanzamiento con información específica del último turno.
     * @param detalle texto detallado del lanzamiento
     */
    public void actualizarDetalle(String detalle) {
        vista.actualizarDetalleLanzamiento(detalle);
    }

    /**
     * Resalta visualmente el equipo que está actualmente en turno.
     * @param equipo equipo a resaltar
     */
    public void resaltarEquipo(Equipo equipo) {
        vista.resaltarEquipo(equipo);
    }

    /**
     * Resalta visualmente el jugador que está actualmente en turno.
     * @param jugador jugador a resaltar
     */
    public void resaltarJugador(Jugador jugador) {
        vista.resaltarJugador(jugador);
    }

    /**
     * Muestra mensajes o resultados en el área de la vista dedicada a información del juego.
     * @param mensaje texto a mostrar
     */
    public void mostrarMensajeEnVista(String mensaje) {
        vista.mostrarMensajeEnVista(mensaje);
    }

    /**
     * Muestra un mensaje modal genérico al usuario usando JOptionPane.
     * @param mensaje texto del mensaje emergente
     */
    public void mostrarMensaje(String mensaje) {
        vista.mostrarMensaje(mensaje);
    }

    /**
     * Notifica visualmente la condición final de victoria para un equipo.
     * @param equipo equipo ganador
     */
    public void mostrarGanador(Equipo equipo) {
        vista.mostrarGanador(equipo);
    }

    /**
     * Permite acceso directo a la instancia de la vista principal.
     * Útil para operaciones avanzadas o test.
     * @return referencia a VistaJuego
     */
    public VistaJuego getVista() {
        return vista;
    }

    /**
     * Habilita o deshabilita el botón de lanzar argolla en la vista,
     * según el estado del turno o reglas especiales.
     *
     * @param habilitar true para habilitar el botón, false para deshabilitarlo
     */
    public void habilitarBotonLanzar(boolean habilitar) {
        vista.getBtnLanzar().setEnabled(habilitar);
    }
}
