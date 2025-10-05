package Udistrital.avanzada.ArgollaLlanera.control;

import Udistrital.avanzada.ArgollaLlanera.vista.VistaJuego;

import javax.swing.*;
import java.io.File;

/**
 * ControlAplicativo inicia la interfaz y controla la carga de archivos.
 */
public class ControlAplicativo {

    public ControlAplicativo() {
        SwingUtilities.invokeLater(this::iniciarAplicacion);
    }

    private void iniciarAplicacion() {
        VistaJuego vista = new VistaJuego();
        ControlVista controlVista = new ControlVista(vista);
        ControlJuego controlJuego = new ControlJuego(controlVista);
        controlVista.setControlJuego(controlJuego);

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Selecciona el archivo Equipos.properties");
        int result = fileChooser.showOpenDialog(null);

        if (result == JFileChooser.APPROVE_OPTION) {
            File archivo = fileChooser.getSelectedFile();
            controlJuego.cargarEquipos(archivo);
            vista.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "No se seleccionó ningún archivo. El juego no puede iniciar.");
            System.exit(0);
        }
    }

    public static void main(String[] args) {
        new ControlAplicativo();
    }
}
