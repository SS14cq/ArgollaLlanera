/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Udistrital.avanzada.ArgollaLlanera.control;

/**
 *
 * @author juanr
 */

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;

public class ControlPersistencia {

    private static final String FILE_PATH = "Specs/data/resultados.dat";
    private static final int RECORD_SIZE = 300; // Ajusta según longitud máxima esperada

    /**
     * Escribe un registro en el archivo de acceso aleatorio.
     * @param clave Identificador único de la partida.
     * @param nombreEquipo Nombre del equipo.
     * @param jugadores Array con los nombres de los 4 jugadores.
     * @param resultado Resultado de la partida ("Ganó" o "Perdió").
     */
    public void escribirRegistro(String clave, String nombreEquipo, String[] jugadores, String resultado) {
        try (RandomAccessFile file = new RandomAccessFile(FILE_PATH, "rw")) {
            file.seek(file.length()); // Ir al final del archivo
            StringBuilder registro = new StringBuilder();
            registro.append(pad(clave, 20));
            registro.append(pad(nombreEquipo, 30));
            for (String jugador : jugadores) {
                registro.append(pad(jugador, 30));
            }
            registro.append(pad(resultado, 20));

            byte[] data = registro.toString().getBytes(StandardCharsets.UTF_8);
            file.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Lee todos los registros del archivo y los imprime en consola.
     */
    public void leerRegistros() {
        try (RandomAccessFile file = new RandomAccessFile(FILE_PATH, "r")) {
            long numRegistros = file.length() / RECORD_SIZE;
            for (int i = 0; i < numRegistros; i++) {
                file.seek(i * RECORD_SIZE);
                byte[] data = new byte[RECORD_SIZE];
                file.readFully(data);
                String registro = new String(data, StandardCharsets.UTF_8);
                System.out.println(registro.trim());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Rellena una cadena con espacios hasta alcanzar la longitud deseada.
     */
    private String pad(String texto, int longitud) {
        if (texto.length() > longitud) {
            return texto.substring(0, longitud);
        }
        return String.format("%-" + longitud + "s", texto);
    }
}

