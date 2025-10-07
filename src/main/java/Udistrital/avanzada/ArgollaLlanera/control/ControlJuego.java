package Udistrital.avanzada.ArgollaLlanera.control;

import Udistrital.avanzada.ArgollaLlanera.modelo.Equipo;
import Udistrital.avanzada.ArgollaLlanera.modelo.Juego;
import Udistrital.avanzada.ArgollaLlanera.modelo.Jugador;
import java.io.File;
import java.io.IOException;
import java.util.*;
import javax.swing.*;

/**
 * ControlJuego
 *
 * Controlador central que gestiona la lógica de partidas, turnos y reglas del juego Argolla Llanera.
 * Orquesta los flujos de la competencia entre equipos, maneja el avance de rondas, lanzamientos
 * de jugadores, cálculo y registro de puntajes, así como manejo de persistencia de resultados.
 * Cumple con la separación MVC, concentrando la lógica sin alterar la vista ni el modelo.
 *
 * Flujos principales:
 * - Carga de equipos para la competencia.
 * - Control de turnos y turnos extra.
 * - Aplicación de reglas para los 21 puntos y muerte súbita.
 * - Manejo de puntajes y decisión de ganador.
 * - Persistencia de resultados en archivo.
 *
 * @author juanr
 * @author Sofia modificado 05-10-2025
 * @author Sofia modificado 06-10-2025
 * @version 1.3
 */
public class ControlJuego {

    private ControlVista controlVista;           // Referencia al controlador de la vista
    private Juego juego;                         // Instancia que representa la partida actual
    private ControlPersistencia persistencia;    // Persistencia de resultados
    private ControlEquipo controlEquipo;         // Auxiliar para gestión y carga de equipos

    private List<Equipo> equiposCargados;        // Todos los equipos disponibles en el archivo
    private List<Equipo> equiposJuego;           // Equipos seleccionados para la partida

    private int rondasJugadas;                   // Contador de rondas completadas
    private int jugadorActualIndex;              // Índice del jugador en turno dentro del equipo actual
    private Equipo equipoActual;                 // Equipo actualmente en turno

    private Map<Equipo, Boolean> manosCompletadas;       // Marcas de equipos que completaron sus manos

    private boolean turnoExtraActivado;                  // Marcas para manejo de rondas extra
    private int lanzamientosExtraRestantes;
    private Equipo equipoPrimerAlcance21;                // Equipo que primero alcanzó 21 puntos

    private static final int LIMITE_RONDAS = 2;          // Número máximo de rondas

    /**
     * Constructor principal. Inicializa estructuras internas y conecta con la vista.
     *
     * @param controlVista referencia al controlador de la vista asociada
     * @throws IOException en caso de error de inicialización de persistencia
     */
    public ControlJuego(ControlVista controlVista) throws IOException {
        this.controlVista = controlVista;
        this.persistencia = new ControlPersistencia();
        this.controlEquipo = new ControlEquipo();

        this.equiposCargados = new ArrayList<>();
        this.equiposJuego = new ArrayList<>();
        this.manosCompletadas = new HashMap<>();
        this.rondasJugadas = 0;

        this.jugadorActualIndex = 0;
        this.turnoExtraActivado = false;
        this.lanzamientosExtraRestantes = 0;
        this.equipoPrimerAlcance21 = null;
    }

    /**
     * Carga los equipos desde el archivo de propiedades.
     * Verifica que hay al menos dos equipos válidos, inicializa los que jugarán e inicia la partida.
     *
     * @param archivoPropiedades archivo .properties seleccionado por el usuario
     */
    public void cargarEquipos(File archivoPropiedades) {
        try {
            controlEquipo.cargarEquiposDesdeArchivo(archivoPropiedades.getAbsolutePath());
            equiposCargados = new ArrayList<>(controlEquipo.listarEquipos());

            if (equiposCargados.size() < 2) {
                controlVista.mostrarMensaje("Debe cargar al menos dos equipos.");
                return;
            }

            equiposJuego = Arrays.asList(equiposCargados.get(0), equiposCargados.get(1));
            juego = new Juego(equiposJuego);

            iniciarPrimeraMano();

        } catch (IOException e) {
            controlVista.mostrarMensaje("Error al cargar equipos: " + e.getMessage());
        }
    }

    /**
     * Inicializa el estado y vista de la primera mano de la ronda.
     * Selecciona de forma aleatoria el equipo que inicia.
     */
    private void iniciarPrimeraMano() {
        jugadorActualIndex = 0;
        Random rand = new Random();
        int indiceInicial = rand.nextInt(equiposJuego.size());
        equipoActual = equiposJuego.get(indiceInicial);

        manosCompletadas.clear();
        for (Equipo eq : equiposJuego) {
            manosCompletadas.put(eq, false);
        }
        controlVista.mostrarEquipos(equiposJuego);
        controlVista.setTurno(equipoActual.getNombre());
        controlVista.resaltarEquipo(equipoActual);

        if (!equipoActual.getJugadores().isEmpty()) {
            controlVista.resaltarJugador(equipoActual.getJugadores().get(jugadorActualIndex));
        }

        controlVista.actualizarPuntajes(
                juego.getPuntajes().getOrDefault(equiposJuego.get(0), 0),
                juego.getPuntajes().getOrDefault(equiposJuego.get(1), 0)
        );
        controlVista.actualizarDetalle("");
        controlVista.mostrarMensajeEnVista("");
        controlVista.habilitarBotonLanzar(true);
    }

    /**
     * Ejecuta el turno de lanzamiento para el jugador actual del equipo en turno.
     * Aplica las reglas del juego, actualiza la vista y controla el avance de rondas.
     */
    public void lanzarTurno() {
        if (rondasJugadas >= LIMITE_RONDAS) {
            controlVista.mostrarMensaje("Ya se jugaron las " + LIMITE_RONDAS + " rondas permitidas.");
            controlVista.habilitarBotonLanzar(false);
            return;
        }

        if (equipoActual == null || equipoActual.getJugadores().isEmpty()) {
            controlVista.mostrarMensaje("Error: equipo o lista de jugadores no inicializada.");
            return;
        }

        Jugador jugadorActual = equipoActual.getJugadores().get(jugadorActualIndex);

        String resultado = juego.generarResultadoAleatorio();
        JOptionPane.showMessageDialog(null, "El jugador " + jugadorActual.getNombre() + " lanzó: " + resultado);

        int puntos = juego.calcularPuntos(resultado);
        jugadorActual.setResultado(resultado);
        jugadorActual.setPuntos(puntos);

        int puntosPrevios = juego.getPuntajes().getOrDefault(equipoActual, 0);
        juego.getPuntajes().put(equipoActual, puntosPrevios + puntos);

        controlVista.mostrarMensajeEnVista("Equipo: " + equipoActual.getNombre()
                + " | Jugador: " + jugadorActual.getNombre() + " (" + jugadorActual.getApodo() + ")"
                + " | Resultado: " + resultado + " | Puntos obtenidos: " + puntos);

        controlVista.actualizarDetalle("Jugador: " + jugadorActual.getNombre() + "\n"
                + "Jugada: " + resultado + "\n"
                + "Puntos obtenidos: " + puntos);
        controlVista.actualizarVista(juego.getPuntajes());
        controlVista.actualizarPuntajes(
                juego.getPuntajes().getOrDefault(equiposJuego.get(0), 0),
                juego.getPuntajes().getOrDefault(equiposJuego.get(1), 0)
        );

        controlVista.resaltarEquipo(equipoActual);
        jugadorActualIndex++;

        int puntosActuales = juego.getPuntajes().getOrDefault(equipoActual, 0);

        // Gestión de regla de los 21 puntos
        if (puntosActuales >= 21) {
           if (equipoPrimerAlcance21 == null) {
            equipoPrimerAlcance21 = equipoActual;
            controlVista.mostrarMensaje("El equipo " + equipoActual.getNombre()
                + " alcanzó 21 puntos. El otro equipo debe completar su turno para intentar igualar.");
            cambiarTurno();
            return;
            } else {
        // Si el segundo equipo termina su turno y no iguala, el primero gana
            int puntosPrimerEquipo = juego.getPuntajes().getOrDefault(equipoPrimerAlcance21, 0);
            int puntosSegundoEquipo = juego.getPuntajes().getOrDefault(equipoActual, 0);

            if (puntosSegundoEquipo < 21 && puntosPrimerEquipo >= 21) {
            controlVista.mostrarGanador(equipoPrimerAlcance21);
            finalizarJuegoConGanador(equipoPrimerAlcance21);
            return;
            }

        // Si ambos tienen 21 o más, evaluar empate o muerte súbita
        evaluarGanador();
        return;
            }
        }

        // Gestionar avance de turno y cambio de jugador/equipo
        if (jugadorActualIndex >= equipoActual.getJugadores().size()) {
            cambiarTurno();
            return;
        }

        if (jugadorActualIndex < equipoActual.getJugadores().size()) {
            controlVista.resaltarJugador(equipoActual.getJugadores().get(jugadorActualIndex));
            controlVista.habilitarBotonLanzar(true);
        }
    }

    /**
     * Cambia el turno al siguiente equipo y reinicia índice de jugador.
     * Actualiza visualmente el equipo y jugador en turno.
     */
    private void cambiarTurno() {
        int indexActual = equiposJuego.indexOf(equipoActual);
        int indexSiguiente = (indexActual + 1) % equiposJuego.size();
        equipoActual = equiposJuego.get(indexSiguiente);
        jugadorActualIndex = 0;

        controlVista.setTurno(equipoActual.getNombre());
        controlVista.resaltarEquipo(equipoActual);
        if (!equipoActual.getJugadores().isEmpty()) {
            controlVista.resaltarJugador(equipoActual.getJugadores().get(jugadorActualIndex));
        }

        controlVista.habilitarBotonLanzar(true);
    }

    /**
     * Evalúa, al cerrar ronda, qué equipo ganó, empató o si se necesita muerte súbita.
     * Persiste los resultados y controla la transición final.
     */
    private void evaluarGanador() {
        int puntosA = juego.getPuntajes().getOrDefault(equiposJuego.get(0), 0);
        int puntosB = juego.getPuntajes().getOrDefault(equiposJuego.get(1), 0);

        // Guardar resultados en archivo
        for (Equipo eq : equiposJuego) {
            String res = (juego.getPuntajes().getOrDefault(eq, 0) >= 21) ? "Ganó" : "Perdió";
            guardarResultado(eq, res);
        }

        // Decisión de ganador, empate o avance de ronda extra
        if (puntosA >= 21 && puntosB < 21) {
            controlVista.mostrarGanador(equiposJuego.get(0));
            finalizarJuegoConGanador(equiposJuego.get(0));
            return;
        } else if (puntosB >= 21 && puntosA < 21) {
            controlVista.mostrarGanador(equiposJuego.get(1));
            finalizarJuegoConGanador(equiposJuego.get(1));
            return;
        }

        // Empate y muerte súbita
        if (puntosA >= 21 && puntosB >= 21) {
            controlVista.mostrarMensaje("¡Empate! Ambos equipos alcanzaron 21 o más puntos.\nSe jugará muerte súbita.");
            jugarMuerteSubita();
            return;
        }

        // Ningún equipo llegó a 21
        controlVista.mostrarMensajeEnVista("Ningún equipo alcanzó 21. Ronda finalizada sin ganador.");
        rondasJugadas++;
        if (rondasJugadas < LIMITE_RONDAS) {
            reiniciarParaNuevaRonda();
        } else {
            salir();
        }
    }

    /**
     * Ejecuta el ciclo de muerte súbita entre ambas parejas de jugadores.
     * Decide ganador por victorias individuales en cada pareja.
     */
    private void jugarMuerteSubita() {
        Equipo equipoA = equiposJuego.get(0);
        Equipo equipoB = equiposJuego.get(1);

        int victoriasA = 0;
        int victoriasB = 0;
        int totalJugadores = Math.min(equipoA.getJugadores().size(), equipoB.getJugadores().size());
        StringBuilder logMuerteSubita = new StringBuilder("🏹 Inicia Muerte Súbita 🏹\n\n");

        for (int i = 0; i < totalJugadores; i++) {
            Jugador jugadorA = equipoA.getJugadores().get(i);
            Jugador jugadorB = equipoB.getJugadores().get(i);

            String resultadoA = juego.generarResultadoAleatorio();
            int puntosA = juego.calcularPuntos(resultadoA);

            String resultadoB = juego.generarResultadoAleatorio();
            int puntosB = juego.calcularPuntos(resultadoB);

            logMuerteSubita.append("Pareja ").append(i + 1).append(":\n")
                    .append(jugadorA.getNombre()).append(" lanza: ").append(resultadoA)
                    .append(" (").append(puntosA).append(" pts)\n")
                    .append(jugadorB.getNombre()).append(" lanza: ").append(resultadoB)
                    .append(" (").append(puntosB).append(" pts)\n");

            if (puntosA > puntosB) {
                victoriasA++;
                logMuerteSubita.append("Ganador de la pareja: ")
                        .append(jugadorA.getNombre()).append(" (")
                        .append(equipoA.getNombre()).append(")\n\n");
            } else if (puntosB > puntosA) {
                victoriasB++;
                logMuerteSubita.append("Ganador de la pareja: ")
                        .append(jugadorB.getNombre()).append(" (")
                        .append(equipoB.getNombre()).append(")\n\n");
            } else {
                logMuerteSubita.append("Empate en esta pareja.\n\n");
            }
        }

        if (victoriasA > victoriasB) {
            logMuerteSubita.append("🏆 ¡Gana el equipo ").append(equipoA.getNombre()).append(" en muerte súbita!\n");
            controlVista.mostrarMensajeEnVista(logMuerteSubita.toString());
            finalizarJuegoConGanador(equipoA);
        } else if (victoriasB > victoriasA) {
            logMuerteSubita.append("🏆 ¡Gana el equipo ").append(equipoB.getNombre()).append(" en muerte súbita!\n");
            controlVista.mostrarMensajeEnVista(logMuerteSubita.toString());
            finalizarJuegoConGanador(equipoB);
        } else {
            logMuerteSubita.append("🔁 ¡Empate total en muerte súbita, se repite la ronda!\n");
            controlVista.mostrarMensajeEnVista(logMuerteSubita.toString());
            jugarMuerteSubita();
        }
    }

    /**
     * Finaliza la partida mostrando el ganador y solicitando al usuario si desea seguir jugando o salir.
     *
     * @param ganador equipo que ganó la ronda
     */
    private void finalizarJuegoConGanador(Equipo ganador) {
        rondasJugadas++;

        StringBuilder mensaje = new StringBuilder();
        mensaje.append("¡Ganó el equipo ").append(ganador.getNombre()).append("!\nJugadores:\n");
        for (Jugador jugador : ganador.getJugadores()) {
            mensaje.append("- ").append(jugador.getNombre()).append(" (").append(jugador.getApodo()).append(")\n");
        }

        controlVista.mostrarGanador(ganador);

        int opcion = JOptionPane.showOptionDialog(null,
                mensaje.toString(),
                "Resultado final",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                new String[]{"Jugar otra ronda", "Salir"},
                "Jugar otra ronda");

        if (opcion == JOptionPane.YES_OPTION) {
            if (rondasJugadas >= LIMITE_RONDAS) {
                controlVista.mostrarMensaje("Se alcanzó el límite de " + LIMITE_RONDAS + " rondas. El juego finaliza.");
                controlVista.habilitarBotonLanzar(false);
                salir();
            } else {
                reiniciarParaNuevaRonda();
            }
        } else {
            controlVista.habilitarBotonLanzar(false);
            salir();
        }
    }

    /**
     * Reinicia el estado interno para comenzar una nueva ronda entre los equipos actuales.
     * Se conservan los mismos equipos y jugadores.
     */
    private void reiniciarParaNuevaRonda() {
        juego = new Juego(equiposJuego);
        jugadorActualIndex = 0;
        turnoExtraActivado = false;
        lanzamientosExtraRestantes = 0;
        equipoPrimerAlcance21 = null;
        controlVista.mostrarEquipos(equiposJuego);
        iniciarPrimeraMano();
    }

    /**
     * Guarda el resultado de la ronda para el equipo en el archivo de persistencia.
     *
     * @param equipo equipo cuyos datos se registran
     * @param resultado texto (“Ganó” o “Perdió”)
     */
    private void guardarResultado(Equipo equipo, String resultado) {
        if (persistencia == null) {
            controlVista.mostrarMensaje("No se puede guardar resultado: persistencia no inicializada.");
            return;
        }
        String[] nombres = new String[4];
        List<Jugador> jugadores = equipo.getJugadores();
        for (int i = 0; i < 4; i++) {
            nombres[i] = i < jugadores.size() ? jugadores.get(i).getNombre() : "";
        }
        persistencia.escribirRegistro(equipo.getClave(), equipo.getNombre(), nombres, resultado);
    }

    /**
     * Finaliza el juego cerrando recursos de persistencia y mostrando los registros al usuario.
     * Cierra la aplicación.
     */
    public void salir() {
        try {
            if (persistencia != null) {
                persistencia.close();
            }
            JOptionPane.showMessageDialog(null, "Finalizando juego...");
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Seleccione archivo de registros para mostrar");
            int option = fileChooser.showOpenDialog(null);
            if (option == JFileChooser.APPROVE_OPTION) {
                File archivo = fileChooser.getSelectedFile();
                ControlPersistencia persist = new ControlPersistencia(archivo.getAbsolutePath());
                persist.leerRegistros();
                persist.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.exit(0);
    }
}

