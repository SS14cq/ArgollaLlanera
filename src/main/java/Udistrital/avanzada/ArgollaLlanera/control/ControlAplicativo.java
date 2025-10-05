package Udistrital.avanzada.ArgollaLlanera;

import Udistrital.avanzada.ArgollaLlanera.control.ControlJuego;
import Vista.VistaJuego;

import javax.swing.*;
import java.io.File;

/**
 * Clase que centraliza la inicialización de la aplicación.
 * Lanza la interfaz gráfica y permite al usuario seleccionar el archivo de configuración.
 */
public class ControlAplicativo {

    public ControlAplicativo() {
        SwingUtilities.invokeLater(this::iniciarAplicacion);
    }

    /**
     * Método que inicia la aplicación gráfica y carga los equipos desde archivo.
     */
    private void iniciarAplicacion() {
        VistaJuego vista = new VistaJuego();
        ControlJuego controlador = new ControlJuego(vista);

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Selecciona el archivo Equipos.properties");
        int result = fileChooser.showOpenDialog(null);

        if (result == JFileChooser.APPROVE_OPTION) {
            File archivo = fileChooser.getSelectedFile();
            controlador.cargarEquipos(archivo);
            vista.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "No se seleccionó ningún archivo. El juego no puede iniciar.");
            System.exit(0);
        }
    }
}