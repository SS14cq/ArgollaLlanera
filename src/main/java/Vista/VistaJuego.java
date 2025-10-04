/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Vista;

import Udistrital.avanzada.ArgollaLlanera.modelo.Equipo;
import Udistrital.avanzada.ArgollaLlanera.modelo.Jugador;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.util.List;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

/**
 *
 * @author juanr
 */


/**
 * Vista principal del juego de la argolla.
 * Muestra los equipos, jugadores, resultados y botones de acción.
 */
public class VistaJuego extends JFrame {

    private JPanel panelEquipos;
    private JButton btnLanzar;
    private JButton btnSalir;
    private JLabel lblTurno;
    private JTextArea areaResultados;

    public VistaJuego() {
        setTitle("Juego de la Argolla Llanera");
        setSize(1000, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initComponents();
    }

    private void initComponents() {
        panelEquipos = new JPanel(new GridLayout(1, 2));
        btnLanzar = new JButton("Lanzar Argolla");
        btnSalir = new JButton("Salir");
        lblTurno = new JLabel("Turno actual", SwingConstants.CENTER);
        areaResultados = new JTextArea();
        areaResultados.setEditable(false);

        JPanel panelBotones = new JPanel();
        panelBotones.add(btnLanzar);
        panelBotones.add(btnSalir);

        setLayout(new BorderLayout());
        add(lblTurno, BorderLayout.NORTH);
        add(panelEquipos, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);
        add(new JScrollPane(areaResultados), BorderLayout.EAST);
    }

    /**
     * Muestra los equipos y jugadores con foto en la interfaz.
     */
    public void mostrarEquipos(List<Equipo> equipos) {
        panelEquipos.removeAll();
        for (Equipo equipo : equipos) {
            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            panel.setBorder(BorderFactory.createTitledBorder(equipo.getNombre()));

            for (Jugador jugador : equipo.getJugadores()) {
                JPanel jugadorPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
                ImageIcon icon = new ImageIcon(jugador.getFoto());
                Image scaled = icon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
                JLabel fotoLabel = new JLabel(new ImageIcon(scaled));
                JLabel nombreLabel = new JLabel(jugador.getNombre() + " (" + jugador.getApodo() + ")");
                jugadorPanel.add(fotoLabel);
                jugadorPanel.add(nombreLabel);
                panel.add(jugadorPanel);
            }

            panelEquipos.add(panel);
        }
        revalidate();
        repaint();
    }

    /**
     * Actualiza los puntajes y resultados en el área lateral.
     */
    public void actualizarVista(Map<Equipo, Integer> puntajes) {
        areaResultados.setText("");
        for (Map.Entry<Equipo, Integer> entry : puntajes.entrySet()) {
            areaResultados.append(entry.getKey().getNombre() + ": " + entry.getValue() + " puntos\n");
            for (Jugador j : entry.getKey().getJugadores()) {
                areaResultados.append("  - " + j.getNombre() + ": " + j.getResultado() + " (" + j.getPuntos() + " pts)\n");
            }
        }
    }

    /**
     * Muestra el equipo ganador en una ventana emergente.
     */
    public void mostrarGanador(Equipo equipo) {
        JOptionPane.showMessageDialog(this,
                "¡Ganó el equipo " + equipo.getNombre() + "!",
                "Resultado Final",
                JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Muestra un mensaje genérico.
     */
    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje);
    }

    public JButton getBtnLanzar() {
        return btnLanzar;
    }

    public JButton getBtnSalir() {
        return btnSalir;
    }

    public void setTurno(String nombreEquipo) {
        lblTurno.setText("Turno de: " + nombreEquipo);
    }
}