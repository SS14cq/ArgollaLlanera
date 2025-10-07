package Udistrital.avanzada.ArgollaLlanera.vista;

import Udistrital.avanzada.ArgollaLlanera.modelo.Equipo;
import Udistrital.avanzada.ArgollaLlanera.modelo.Jugador;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.*;
import javax.swing.border.Border;

/**
 * Vista principal del juego Argolla Llanera.
 * Construye la interfaz gráfica, muestra jugadores, equipos, puntajes,
 * detalle de lanzamientos y controla visualización de estados activos.
 * 
 * <p>Gestiona la interacción visual en conjunto con ControlVista y ControlJuego.</p>
 * 
 * @author juanr
 * @author Sofia modificado 05-10-2025
 * @version 1.2
 */
public class VistaJuego extends JFrame {

    private JPanel panelEquipos;
    private JButton btnLanzar;
    private JButton btnSalir;
    private JLabel lblTurno;
    private JTextArea areaResultados;

    private JTextArea areaDetalleLanzamiento;
    private JLabel lblPuntosEquipoA;
    private JLabel lblPuntosEquipoB;

    private JPanel panelEquipoA;
    private JPanel panelEquipoB;

    private Equipo equipoA;
    private Equipo equipoB;

    private Map<Jugador, JPanel> panelesJugadores = new HashMap<>();

    private final Border bordeNormal = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 1));
    private final Border bordeResaltado = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(30, 144, 255), 3));
    private final Color colorFondoResaltado = new Color(173, 216, 230);
    private final Color colorFondoNormal = Color.WHITE;

    /**
     * Constructor que inicializa la ventana con layouts, fuentes y colores.
     */
    public VistaJuego() {
        setTitle("Juego de la Argolla Llanera");
        setSize(1100, 650);
        setMinimumSize(new Dimension(900, 600));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        initHeader();
        initMainPanel();
        initFooter();

        getContentPane().setBackground(Color.WHITE);
    }

    /**
     * Inicializa la cabecera con etiqueta para mostrar el turno actual.
     */
    private void initHeader() {
        lblTurno = new JLabel("Turno actual", SwingConstants.CENTER);
        lblTurno.setFont(new Font("Segoe UI Semibold", Font.BOLD, 20));
        lblTurno.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(lblTurno, BorderLayout.NORTH);
    }

    /**
     * Construye los paneles principales para equipos y resultados.
     */
    private void initMainPanel() {
        panelEquipos = new JPanel(new GridLayout(1, 2, 20, 0));
        panelEquipos.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(panelEquipos, BorderLayout.CENTER);

        panelEquipoA = new JPanel(new BorderLayout());
        panelEquipoB = new JPanel(new BorderLayout());
        panelEquipos.add(panelEquipoA);
        panelEquipos.add(panelEquipoB);

        areaResultados = new JTextArea();
        areaResultados.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));
        areaResultados.setEditable(false);
        areaResultados.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY), "Resultados"));
        areaResultados.setBackground(new Color(245, 245, 245));

        JScrollPane scrollResultados = new JScrollPane(areaResultados);
        scrollResultados.setPreferredSize(new Dimension(350, 0));
        add(scrollResultados, BorderLayout.EAST);

        areaDetalleLanzamiento = new JTextArea(4, 30);
        areaDetalleLanzamiento.setLineWrap(true);
        areaDetalleLanzamiento.setWrapStyleWord(true);
        areaDetalleLanzamiento.setEditable(false);
        areaDetalleLanzamiento.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        areaDetalleLanzamiento.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY), "Detalle del Lanzamiento"));
        areaDetalleLanzamiento.setBackground(new Color(250, 250, 250));

        lblPuntosEquipoA = new JLabel("Puntos Equipo A: 0");
        lblPuntosEquipoA.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblPuntosEquipoA.setHorizontalAlignment(SwingConstants.CENTER);

        lblPuntosEquipoB = new JLabel("Puntos Equipo B: 0");
        lblPuntosEquipoB.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblPuntosEquipoB.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel abajoA = new JPanel(new BorderLayout(5, 5));
        abajoA.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        abajoA.add(lblPuntosEquipoA, BorderLayout.NORTH);
        abajoA.add(areaDetalleLanzamiento, BorderLayout.CENTER);

        JPanel abajoB = new JPanel(new BorderLayout());
        abajoB.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        abajoB.add(lblPuntosEquipoB, BorderLayout.NORTH);

        panelEquipoA.add(abajoA, BorderLayout.SOUTH);
        panelEquipoB.add(abajoB, BorderLayout.SOUTH);
    }

    /**
     * Inicializa el pie con botones para lanzar y salir.
     */
    private void initFooter() {
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        btnLanzar = new JButton("Lanzar Argolla");
        btnLanzar.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnLanzar.setBackground(new Color(30, 144, 255));
        btnLanzar.setForeground(Color.WHITE);
        btnLanzar.setFocusPainted(false);

        btnSalir = new JButton("Salir");
        btnSalir.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        btnSalir.setBackground(new Color(220, 53, 69));
        btnSalir.setForeground(Color.WHITE);
        btnSalir.setFocusPainted(false);

        panelBotones.add(btnLanzar);
        panelBotones.add(btnSalir);
        add(panelBotones, BorderLayout.SOUTH);
    }

    /**
     * Muestra los paneles de jugadores para cada equipo.
     * @param equipos lista con dos equipos para mostrar
     */
    public void mostrarEquipos(List<Equipo> equipos) {
        if (equipos.size() < 2) return;
        equipoA = equipos.get(0);
        equipoB = equipos.get(1);

        panelEquipoA.removeAll();
        panelEquipoB.removeAll();

        panelesJugadores.clear();

        JPanel jugadoresA = crearPanelJugadores(equipoA);
        JPanel jugadoresB = crearPanelJugadores(equipoB);

        panelEquipoA.setLayout(new BorderLayout());
        panelEquipoB.setLayout(new BorderLayout());

        panelEquipoA.add(jugadoresA, BorderLayout.CENTER);
        panelEquipoB.add(jugadoresB, BorderLayout.CENTER);

        panelEquipoA.revalidate();
        panelEquipoA.repaint();
        panelEquipoB.revalidate();
        panelEquipoB.repaint();

        resaltarEquipo(null);
        resaltarJugador(null);
    }

    /**
     * Crea un panel vertical con los jugadores del equipo visualmente organizados.
     * @param equipo equipo cuyos jugadores se mostrarán
     * @return JPanel con la lista visual de jugadores
     */
    private JPanel crearPanelJugadores(Equipo equipo) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 2), equipo.getNombre()));

        for (Jugador jugador : equipo.getJugadores()) {
            JPanel jugadorPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            jugadorPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            ImageIcon icon = new ImageIcon(jugador.getFoto());
            Image scaled = icon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
            JLabel fotoLabel = new JLabel(new ImageIcon(scaled));
            fotoLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));

            JLabel nombreLabel = new JLabel(jugador.getNombre() + " (" + jugador.getApodo() + ")");
            nombreLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));

            jugadorPanel.add(fotoLabel);
            jugadorPanel.add(Box.createRigidArea(new Dimension(10,0)));
            jugadorPanel.add(nombreLabel);
            panel.add(jugadorPanel);

            panelesJugadores.put(jugador, jugadorPanel);
        }
        return panel;
    }

    /**
     * Actualiza el área de texto que muestra detalles del último lanzamiento efectuado.
     * @param texto texto descriptivo del lanzamiento
     */
    public void actualizarDetalleLanzamiento(String texto) {
        areaDetalleLanzamiento.setText(texto);
    }

    /**
     * Actualiza la lista de resultados y puntajes mostrados en la ventana principal.
     * @param puntajes mapa con equipos y puntajes actuales
     */
    public void actualizarVista(Map<Equipo, Integer> puntajes) {
        areaResultados.setText("");
        for (Map.Entry<Equipo, Integer> entry : puntajes.entrySet()) {
            areaResultados.append(entry.getKey().getNombre() + ": " + entry.getValue() + " puntos\n");
            for (Jugador j : entry.getKey().getJugadores()) {
                areaResultados.append(" - " + j.getNombre() + ": " + j.getResultado() + " (" + j.getPuntos() + " pts)\n");
            }
        }
    }

    /**
     * Actualiza las etiquetas de la interfaz con los puntos de cada equipo.
     * @param puntosA puntos del equipo A
     * @param puntosB puntos del equipo B
     */
    public void actualizarPuntajes(int puntosA, int puntosB) {
        lblPuntosEquipoA.setText("Puntos " + equipoA.getNombre() + ": " + puntosA);
        lblPuntosEquipoB.setText("Puntos " + equipoB.getNombre() + ": " + puntosB);
    }

    /**
     * Añade un mensaje de texto visible en la ventana de resultados.
     * @param mensaje cadena para mostrar
     */
    public void mostrarMensajeEnVista(String mensaje) {
        areaResultados.append(mensaje + "\n");
        areaResultados.setCaretPosition(areaResultados.getDocument().getLength());
    }

    /**
     * Muestra cuadro de diálogo indicando que ganó el equipo especificado.
     * @param equipo equipo ganador a mostrar
     */
    public void mostrarGanador(Equipo equipo) {
        JOptionPane.showMessageDialog(this,
                "¡Ganó el equipo " + equipo.getNombre() + "!",
                "Resultado Final",
                JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Muestra un cuadro de diálogo general con el mensaje dado.
     * @param mensaje mensaje a mostrar
     */
    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje);
    }

    /**
     * Devuelve el botón para lanzar, para ser utilizado en ControlVista.
     * @return botón lanzar argolla
     */
    public JButton getBtnLanzar() {
        return btnLanzar;
    }

    /**
     * Devuelve el botón salir, para ser utilizado en ControlVista.
     * @return botón salir
     */
    public JButton getBtnSalir() {
        return btnSalir;
    }

    /**
     * Actualiza la etiqueta que indica el equipo al turno
     * @param nombreEquipo nombre del equipo activo
     */
    public void setTurno(String nombreEquipo) {
        lblTurno.setText("Turno de: " + nombreEquipo);
        lblTurno.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTurno.setForeground(new Color(30, 144, 255));
    }

    /**
     * Aplica resaltado visual al panel entero del equipo que está en turno,
     * removiendo resaltado de quien no está.
     * @param equipoEnTurno equipo al cual se le aplica el resaltado
     */
    public void resaltarEquipo(Equipo equipoEnTurno) {
        if (equipoA != null) {
            if (equipoA.equals(equipoEnTurno)) {
                panelEquipoA.setBorder(bordeResaltado);
            } else {
                panelEquipoA.setBorder(bordeNormal);
            }
        }
        if (equipoB != null) {
            if (equipoB.equals(equipoEnTurno)) {
                panelEquipoB.setBorder(bordeResaltado);
            } else {
                panelEquipoB.setBorder(bordeNormal);
            }
        }
        repaint();
    }

    /**
     * Aplica resaltado visual al panel correspondiente al jugador actual,
     * removiendo el resaltado previo de los otros jugadores.
     * @param jugadorTurno jugador que debe resaltarse
     */
    public void resaltarJugador(Jugador jugadorTurno) {
        for (Map.Entry<Jugador, JPanel> entry : panelesJugadores.entrySet()) {
            if (entry.getKey().equals(jugadorTurno)) {
                entry.getValue().setBackground(colorFondoResaltado);
            } else {
                entry.getValue().setBackground(colorFondoNormal);
            }
        }
        repaint();
    }
}
