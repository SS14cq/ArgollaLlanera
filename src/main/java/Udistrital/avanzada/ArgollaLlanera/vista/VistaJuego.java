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
 * @author sofia modificado 06-10-2025
 * @version 1.3
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

    private void initHeader() {
        lblTurno = new JLabel("Turno actual", SwingConstants.CENTER);
        lblTurno.setFont(new Font("Segoe UI Semibold", Font.BOLD, 20));
        lblTurno.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(lblTurno, BorderLayout.NORTH);
    }

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

    private JPanel crearPanelJugadores(Equipo equipo) {
    JPanel panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 2), equipo.getNombre()));

    for (Jugador jugador : equipo.getJugadores()) {
        JPanel jugadorPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        jugadorPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        // --- SOLO MOSTRAR NOMBRE Y APODO, SIN FOTO ---
        JLabel nombreLabel = new JLabel(jugador.getNombre() + " (" + jugador.getApodo() + ")");
        nombreLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        jugadorPanel.add(nombreLabel);
        panel.add(jugadorPanel);

        panelesJugadores.put(jugador, jugadorPanel);
    }
    return panel;
}


    public void actualizarDetalleLanzamiento(String texto) {
        areaDetalleLanzamiento.setText(texto);
    }

    public void actualizarVista(Map<Equipo, Integer> puntajes) {
        areaResultados.setText("");
        for (Map.Entry<Equipo, Integer> entry : puntajes.entrySet()) {
            areaResultados.append(entry.getKey().getNombre() + ": " + entry.getValue() + " puntos\n");
            for (Jugador j : entry.getKey().getJugadores()) {
                areaResultados.append(" - " + j.getNombre() + ": " + j.getResultado() + " (" + j.getPuntos() + " pts)\n");
            }
        }
    }

    public void actualizarPuntajes(int puntosA, int puntosB) {
        lblPuntosEquipoA.setText("Puntos " + equipoA.getNombre() + ": " + puntosA);
        lblPuntosEquipoB.setText("Puntos " + equipoB.getNombre() + ": " + puntosB);
    }

    public void mostrarMensajeEnVista(String mensaje) {
        areaResultados.append(mensaje + "\n");
        areaResultados.setCaretPosition(areaResultados.getDocument().getLength());
    }

    public void mostrarGanador(Equipo equipo) {
        JOptionPane.showMessageDialog(this,
                "¡Ganó el equipo " + equipo.getNombre() + "!",
                "Resultado Final",
                JOptionPane.INFORMATION_MESSAGE);
    }

    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje);
    }

    public JButton getBtnLanzar() { return btnLanzar; }
    public JButton getBtnSalir() { return btnSalir; }

    public void setTurno(String nombreEquipo) {
        lblTurno.setText("Turno de: " + nombreEquipo);
        lblTurno.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTurno.setForeground(new Color(30, 144, 255));
    }

    public void resaltarEquipo(Equipo equipoEnTurno) {
        if (equipoA != null) {
            panelEquipoA.setBorder(equipoA.equals(equipoEnTurno) ? bordeResaltado : bordeNormal);
        }
        if (equipoB != null) {
            panelEquipoB.setBorder(equipoB.equals(equipoEnTurno) ? bordeResaltado : bordeNormal);
        }
        repaint();
    }

    public void resaltarJugador(Jugador jugadorTurno) {
        for (Map.Entry<Jugador, JPanel> entry : panelesJugadores.entrySet()) {
            entry.getValue().setBackground(entry.getKey().equals(jugadorTurno) ? colorFondoResaltado : colorFondoNormal);
        }
        repaint();
    }
}
