package Udistrital.avanzada.ArgollaLlanera.control;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;

/**
 * Clase encargada de la persistencia de resultados del juego en un archivo de acceso aleatorio.
 * Gestiona la escritura y lectura de registros con tamaño fijo estructurados,
 * permitiendo guardar resultados de rondas en formato binario.
 * 
 * <p>Los registros almacenan: clave, nombre de equipo, 4 nombres de jugadores y resultado.</p>
 * 
 * La estructura fija facilita la lectura secuencial y acceso directo a datos.
 * Las cadenas se rellenan con espacios para garantizar longitud constante.</p>
 * 
 * Implementa lectura y escritura bajo codificación UTF-8.
 *  
 * @author juanr
 * @author Sofia modificado el 05-10-2025
 * @version 1.0
 */
public class ControlPersistencia {

    private RandomAccessFile file;
    private File filePath;

    /**
     * Tamaño fijo en bytes para cada registro.
     * Calculado como suma de longitudes de campos con relleno para cadenas:
     * Clave(20) + Nombre equipo(30) + 4x Jugadores(30) + Resultado(20) = 160 bytes
     */
    public static final int RECORD_SIZE = 160;

    /**
     * Crea una instancia para archivo por defecto (ruta fija).
     */
    public ControlPersistencia() {
        try {
			filePath = new File("src\\Specs\\Data\\resultados.dat");
			file = new RandomAccessFile(filePath, "rw");
		} catch (FileNotFoundException fnfe) {/* Archivo no encontrado */
                    
                }
    }

    /**
     * Crea una instancia para archivo con ruta específica.
     * 
     * @param filePath ruta al archivo de acceso aleatorio
     * @throws IOException si el archivo no puede abrirse
     */
    public ControlPersistencia(File filePath) throws IOException {
        this.filePath = filePath;
        file = new RandomAccessFile(filePath, "rw");
    }

    /**
     * Escribe un registro en el archivo al final, con campos de tamaño fijo.
     * 
     * @param clave la clave identificadora única de la partida
     * @param nombreEquipo nombre del equipo participante
     * @param jugadores array con nombres de 4 jugadores
     * @param resultado resultado final ("Ganó" o "Perdió")
     */
    public void escribirRegistro(String clave, String nombreEquipo, String[] jugadores, String resultado) {
        try (RandomAccessFile Rfile = new RandomAccessFile(filePath, "rw")) {
            Rfile.seek(Rfile.length());  // Mover el puntero al final para appending
            StringBuilder registro = new StringBuilder();
            registro.append(padRight(clave, 10));
            registro.append(padRight(nombreEquipo, 20));
            for (String jugador : jugadores) {
                registro.append(padRight(jugador, 20));
            }
            registro.append(padRight(resultado, 10));

            byte[] data = registro.toString().getBytes(StandardCharsets.UTF_8);

            // Asegurar longitud exacta del registro con relleno o truncado
            if (data.length > RECORD_SIZE) {
                byte[] truncado = new byte[RECORD_SIZE];
                System.arraycopy(data, 0, truncado, 0, RECORD_SIZE);
                data = truncado;
            } else if (data.length < RECORD_SIZE) {
                byte[] relleno = new byte[RECORD_SIZE];
                System.arraycopy(data, 0, relleno, 0, data.length);
                for (int i = data.length; i < RECORD_SIZE; i++) {
                    relleno[i] = ' ';
                }
                data = relleno;
            }

            file.write(data);
        } catch (IOException e) {
        }
    }

    /**
     * Lee todos los registros del archivo y los imprime en consola,
     * mostrando el contenido estructurado en forma legible.
     */
    public void leerRegistros() {
        try (RandomAccessFile Rfile = new RandomAccessFile(filePath, "r")) {
            long totalRegistros = Rfile.length() / RECORD_SIZE;
            System.out.println("Registros guardados en archivo:");
            for (int i = 0; i < totalRegistros; i++) {
                Rfile.seek(i * RECORD_SIZE);
                byte[] datos = new byte[RECORD_SIZE];
                Rfile.readFully(datos);
                String registro = new String(datos, StandardCharsets.UTF_8);
                System.out.println(registro.trim());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Rellena una cadena con espacios a la derecha para alcanzar longitud fija,
     * o la trunca si es demasiado larga.
     * 
     * @param texto cadena original
     * @param longitud longitud fija deseada
     * @return cadena formatada con longitud precisa
     */
    private String padRight(String texto, int longitud) {
        if (texto == null) texto = "";
        if (texto.length() > longitud) {
            return texto.substring(0, longitud);
        }
        return String.format("%-" + longitud + "s", texto);
    }

    /**
     * Cierra el archivo asociado si está abierto.
     */
    public void close() {
        try {
            if (file != null) file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
