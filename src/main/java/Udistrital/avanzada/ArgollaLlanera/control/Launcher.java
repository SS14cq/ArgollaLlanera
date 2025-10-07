package Udistrital.avanzada.ArgollaLlanera.control;

/**
 * Launcher
 * 
 * Punto de entrada principal del programa.
 * 
 * Esta clase cumple con el Principio de Responsabilidad Única (SRP)
 * porque su única función es iniciar la aplicación, delegando toda
 * la configuración y lógica al ControlAplicativo.
 * 
 * También respeta el patrón MVC al no tener lógica de vista ni de modelo.
 * 
 * @author Sara
 */
public class Launcher {

    public static void main(String[] args) {
        try {
            // Crear instancia del controlador principal de la aplicación
            ControlAplicativo controlAplicativo = new ControlAplicativo();

            // Iniciar la aplicación
            controlAplicativo.iniciarAplicacion();
        } catch (Exception e) {
            System.err.println("❌ Error al iniciar la aplicación: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
