package Udistrital.avanzada.ArgollaLlanera.control;

import Udistrital.avanzada.ArgollaLlanera.vista.VistaJuego;

import javax.swing.*;
import java.io.File;
import java.io.IOException;

/**
 * ControlAplicativo
 *
 * Clase responsable de inicializar y conectar las capas de la aplicación
 * (Vista, ControlVista y ControlJuego) siguiendo el patrón MVC.
 *
 * Cumple con los principios SOLID:
 *  - SRP: Su única responsabilidad es inicializar la aplicación.
 *  - OCP: Puede extenderse (por ejemplo, con otra vista) sin modificarse.
 *  - DIP: Depende de abstracciones (controladores) y no de implementaciones de bajo nivel.
 *
 * Además, mantiene el flujo MVC al coordinar Vista, ControlVista y ControlJuego.
 *
 * @author juan valbuena
 * @author sofia modificado 06-05-2025
 * @version 1.1
 * 
 */
public class ControlAplicativo {

    private ControlJuego controlJuego;
    private ControlVista controlVista;

    /**
     * Inicia la aplicación creando y conectando las capas MVC.
     *
     * @throws IOException si ocurre un error al cargar los datos iniciales.
     */
    public void iniciarAplicacion() throws IOException {
    VistaJuego vista = new VistaJuego();
    controlVista = new ControlVista(vista);
    controlJuego = new ControlJuego(controlVista);
    controlVista.setControlJuego(controlJuego);

    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setDialogTitle("Selecciona el archivo Equipos.properties");
    int result = fileChooser.showOpenDialog(null);

    if (result == JFileChooser.APPROVE_OPTION) {
        File archivo = fileChooser.getSelectedFile();
        System.out.println("Archivo seleccionado: " + archivo.getAbsolutePath()); // Log de selección

        // Intentar cargar equipos
        try {
            controlJuego.cargarEquipos(archivo);
            System.out.println("Equipos cargados correctamente."); // Log posterior a carga exitosa

            // Mostrar la vista en el hilo de Swing
            SwingUtilities.invokeLater(() -> vista.setVisible(true));
        } catch (Exception e) {
            System.err.println("Error cargando equipos: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al cargar equipos: " + e.getMessage());
            System.exit(1);
        }

    } else {
        System.out.println("No se seleccionó archivo."); // Log si se cancela selección
        JOptionPane.showMessageDialog(null, "No se seleccionó ningún archivo. El juego no puede iniciar.");
        System.exit(0);
    }
}
}
