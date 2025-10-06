package Udistrital.avanzada.ArgollaLlanera.control;

import Udistrital.avanzada.ArgollaLlanera.modelo.Equipo;
import Udistrital.avanzada.ArgollaLlanera.modelo.Juego;
import Udistrital.avanzada.ArgollaLlanera.modelo.Jugador;

import java.io.File;
import java.io.IOException;
import java.util.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Controlador principal del juego Argolla Llanera.
 * Gestiona la lógica principal de turnos, rondas, persistencia de resultados,
 * la muerte súbita en caso de empate, y comunicación con la vista (GUI).
 *
 * <p>Permite jugar un máximo de dos rondas, registra resultados en archivo
 * de acceso aleatorio, y al salir muestra en consola toda la información guardada.</p>
 *
 * @author juanr
 * @author sofia (modificado 05-10-2025)
 * @version 1.2
 */
public class ControlJuego {

    private ControlVista controlVista;
    private Juego juego;
    private ControlPersistencia persistencia;
    private ControlEquipo controlEquipo;
    private List<Equipo> equipos;
    private int rondasJugadas;
    private int equipoTurnoIndex;
    private int jugadorActualIndex;
    private Equipo equipoActual;
    private Map<Equipo, Boolean> manosCompletadas;

    /**
     * Constructor. Inicializa el controlador, estructuras y eventos.
     * @param controlVista El controlador de la vista asociada
     */
    public ControlJuego(ControlVista controlVista) {
        this.controlVista = controlVista;
        this.persistencia = new ControlPersistencia();
        this.controlEquipo = new ControlEquipo();
        this.equipos = new ArrayList<>();
        this.rondasJugadas = 0;
        this.manosCompletadas = new HashMap<>();
        configurarEventos();
    }

    /**
     * Configura los listeners de los botones principales.
     */
    private void configurarEventos() {
        controlVista.getBtnLanzar().addActionListener(e -> lanzarTurno());
        controlVista.getBtnSalir().addActionListener(e -> salir());
    }

    /**
     * Carga equipos desde archivo .properties.
     * Inicializa el juego, valida cantidades y actualiza la interfaz.
     * @param archivoPropiedades Archivo de props con los equipos/jugadores
     */
    public void cargarEquipos(File archivoPropiedades) {
        try {
            controlEquipo.cargarEquiposDesdeArchivo(archivoPropiedades.getAbsolutePath());
            equipos = new ArrayList<>(controlEquipo.listarEquipos());
            if (equipos.size() < 2) {
                controlVista.mostrarMensaje("Debe cargar al menos dos equipos.");
                return;
            }
            juego = new Juego(equipos);
            equipoTurnoIndex = new Random().nextInt(equipos.size());
            jugadorActualIndex = 0;
            equipoActual = equipos.get(equipoTurnoIndex);
            manosCompletadas.put(equipos.get(0), false);
            manosCompletadas.put(equipos.get(1), false);
            controlVista.mostrarEquipos(equipos);
            controlVista.setTurno(equipoActual.getNombre());
            controlVista.actualizarPuntajes(0, 0);
            controlVista.resaltarEquipo(equipoActual);
            if (!equipoActual.getJugadores().isEmpty()) {
                controlVista.resaltarJugador(equipoActual.getJugadores().get(0));
            }
        } catch (IOException e) {
            controlVista.mostrarMensaje("Error al cargar equipos: " + e.getMessage());
        }
    }

    /**
     * Ejecuta la lógica principal de un turno de juego.
     * Actualiza puntajes, detalles, resalta jugador y controla avance de ronda.
     */
    public void lanzarTurno() {
        if (rondasJugadas >= 2) {
            controlVista.mostrarMensaje("Ya se jugaron las dos rondas permitidas.");
            return;
        }

        Jugador jugadorActual = equipoActual.getJugadores().get(jugadorActualIndex);
        String resultado = juego.generarResultadoAleatorio();
        int puntos = juego.calcularPuntos(resultado);
        jugadorActual.setResultado(resultado);
        jugadorActual.setPuntos(puntos);

        int puntosPrevios = juego.getPuntajes().getOrDefault(equipoActual, 0);
        juego.getPuntajes().put(equipoActual, puntosPrevios + puntos);

        String mensajeTurno = "Equipo: " + equipoActual.getNombre()
            + " | Jugador: " + jugadorActual.getNombre()
            + " (" + jugadorActual.getApodo() + ")"
            + " | Resultado: " + resultado
            + " | Puntos obtenidos: " + puntos;
        controlVista.mostrarMensajeEnVista(mensajeTurno);

        String detalle = "Jugador: " + jugadorActual.getNombre() + "\n"
            + "Jugada: " + resultado + "\n"
            + "Puntos obtenidos: " + puntos;
        controlVista.actualizarDetalle(detalle);
        controlVista.actualizarVista(juego.getPuntajes());
        controlVista.actualizarPuntajes(
            juego.getPuntajes().get(equipos.get(0)),
            juego.getPuntajes().get(equipos.get(1))
        );
        controlVista.resaltarEquipo(equipoActual);
        controlVista.resaltarJugador(jugadorActual);

        jugadorActualIndex++;

        if (jugadorActualIndex >= equipoActual.getJugadores().size()) {
            manosCompletadas.put(equipoActual, true);

            if (manosCompletadas.values().stream().allMatch(v -> v)) {
                // Guarda resultados de la ronda para cada equipo
                for (Equipo eq : equipos) {
                    String res = (juego.getPuntajes().getOrDefault(eq, 0) >= 21) ? "Ganó" : "Perdió";
                    guardarResultado(eq, res);
                }

                manosCompletadas.put(equipos.get(0), false);
                manosCompletadas.put(equipos.get(1), false);

                Equipo equipoA = equipos.get(0);
                Equipo equipoB = equipos.get(1);
                int puntosA = juego.getPuntajes().get(equipoA);
                int puntosB = juego.getPuntajes().get(equipoB);
                boolean ambosSuperan21 = puntosA > 21 && puntosB > 21;
                boolean ambosIguales21 = puntosA == 21 && puntosB == 21;
                if (ambosSuperan21 || ambosIguales21) {
                    controlVista.mostrarMensajeEnVista("Se jugará muerte súbita por empate o ambos superan 21 puntos.");
                    jugarMuerteSubita();
                } else if (puntosA >= 21 && puntosB < 21) {
                    finalizarJuegoConGanador(equipoA);
                } else if (puntosB >= 21 && puntosA < 21) {
                    finalizarJuegoConGanador(equipoB);
                }
            } else {
                cambiarTurno();
            }
            jugadorActualIndex = 0;
        } else {
            Jugador siguienteJugador = equipoActual.getJugadores().get(jugadorActualIndex);
            controlVista.resaltarJugador(siguienteJugador);
        }
    }

    /**
     * Ejecuta la muerte súbita con lanzamientos de parejas, usando javax.swing.Timer
     * para mostrar cada lanzamiento con pausa visual.
     */
    private void jugarMuerteSubita() {
        Equipo equipoA = equipos.get(0);
        Equipo equipoB = equipos.get(1);

        final int[] victoriasA = {0};
        final int[] victoriasB = {0};
        final int[] index = {0};
        final int total = equipoA.getJugadores().size();

        controlVista.mostrarMensajeEnVista("Inicia Muerte Súbita...\n");

        // Referencia completa para timer de Swing
        javax.swing.Timer timer = new javax.swing.Timer(1500, null);
        timer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (index[0] >= total) {
                    timer.stop();

                    Equipo ganador;
                    if (victoriasA[0] > victoriasB[0]) {
                        ganador = equipoA;
                    } else if (victoriasB[0] > victoriasA[0]) {
                        ganador = equipoB;
                    } else {
                        controlVista.mostrarMensajeEnVista("Empate total en muerte súbita, se repite la ronda.\n");
                        jugarMuerteSubita();
                        return;
                    }
                    finalizarJuegoConGanador(ganador);
                    return;
                }

                Jugador jugadorA = equipoA.getJugadores().get(index[0]);
                Jugador jugadorB = equipoB.getJugadores().get(index[0]);
                String resultadoA = juego.generarResultadoAleatorio();
                int puntosA = juego.calcularPuntos(resultadoA);
                String resultadoB = juego.generarResultadoAleatorio();
                int puntosB = juego.calcularPuntos(resultadoB);

                String msg = "Pareja " + (index[0]+1) + ":\n" +
                             jugadorA.getNombre() + " lanza: " + resultadoA + " (" + puntosA + " pts)\n" +
                             jugadorB.getNombre() + " lanza: " + resultadoB + " (" + puntosB + " pts)\n";
                controlVista.mostrarMensajeEnVista(msg);

                if (puntosA > puntosB) {
                    victoriasA[0]++;
                    controlVista.mostrarMensajeEnVista("Ganador de la pareja: " + jugadorA.getNombre() + " (" + equipoA.getNombre() + ")\n");
                } else if (puntosB > puntosA) {
                    victoriasB[0]++;
                    controlVista.mostrarMensajeEnVista("Ganador de la pareja: " + jugadorB.getNombre() + " (" + equipoB.getNombre() + ")\n");
                } else {
                    controlVista.mostrarMensajeEnVista("Empate en esta pareja.\n");
                }
                index[0]++;
            }
        });
        timer.setInitialDelay(0);
        timer.start();
    }

    /**
     * Pasa el turno al siguiente equipo y resetea al primer jugador.
     */
    private void cambiarTurno() {
        equipoTurnoIndex = (equipoTurnoIndex + 1) % equipos.size();
        equipoActual = equipos.get(equipoTurnoIndex);
        controlVista.setTurno(equipoActual.getNombre());
        controlVista.resaltarEquipo(equipoActual);
        jugadorActualIndex = 0;
        if (!equipoActual.getJugadores().isEmpty()) {
            controlVista.resaltarJugador(equipoActual.getJugadores().get(0));
        }
    }

    /**
     * Muestra ventana de ganador y pregunta jugar otra ronda o salir.
     * Guarda resultados de la ronda antes de terminar.
     * @param ganador equipo ganador de la partida/ronda.
     */
    private void finalizarJuegoConGanador(Equipo ganador) {
        rondasJugadas++;

        StringBuilder mensaje = new StringBuilder();
        mensaje.append("¡Ganó el equipo ").append(ganador.getNombre()).append("!\nJugadores:\n");
        for (Jugador j : ganador.getJugadores()) {
            mensaje.append("- ").append(j.getNombre()).append(" (").append(j.getApodo()).append(")\n");
        }

        int opcion = JOptionPane.showOptionDialog(null,
                mensaje.toString(),
                "Resultado final",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                new String[]{"Jugar otra ronda", "Salir"},
                "Jugar otra ronda");

        if (opcion == JOptionPane.YES_OPTION) {
            if (rondasJugadas >= 2) {
                controlVista.mostrarMensaje("Solo se permiten 2 rondas. El juego finaliza.");
                salir();
            } else {
                reiniciarParaNuevaRonda();
            }
        } else {
            salir();
        }
    }

    /**
     * Reinicia el juego para una nueva ronda con estado limpio.
     */
    private void reiniciarParaNuevaRonda() {
        juego = new Juego(equipos);
        jugadorActualIndex = 0;
        equipoTurnoIndex = new Random().nextInt(equipos.size());
        equipoActual = equipos.get(equipoTurnoIndex);
        manosCompletadas.put(equipos.get(0), false);
        manosCompletadas.put(equipos.get(1), false);
        controlVista.mostrarEquipos(equipos);
        controlVista.setTurno(equipoActual.getNombre());
        controlVista.actualizarPuntajes(0, 0);
        controlVista.actualizarDetalle("");
        controlVista.resaltarEquipo(equipoActual);
        if (!equipoActual.getJugadores().isEmpty()) {
            controlVista.resaltarJugador(equipoActual.getJugadores().get(0));
        }
    }

    /**
     * Guarda los resultados de cada equipo tras la ronda usando ControlPersistencia.
     * @param equipo Equipo a guardar
     * @param resultado "Ganó"/"Perdió"
     */
    private void guardarResultado(Equipo equipo, String resultado) {
        String[] nombres = equipo.getJugadores().stream()
                .map(Jugador::getNombre)
                .toArray(String[]::new);
        persistencia.escribirRegistro(equipo.getClave(), equipo.getNombre(), nombres, resultado);
    }

    /**
     * Termina la aplicación mostrando en consola los registros guardados,
     * permitiendo seleccionar el archivo mediante JFileChooser.
     */
    public void salir() {
        try {
            JOptionPane.showMessageDialog(null, "Finalizando juego...");
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Seleccione archivo de registros para mostrar");
            int option = fileChooser.showOpenDialog(null);
            if (option == JFileChooser.APPROVE_OPTION) {
                File archivo = fileChooser.getSelectedFile();
                ControlPersistencia persist = new ControlPersistencia(archivo);
                persist.leerRegistros();
                persist.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.exit(0);
    }
}
