package Udistrital.avanzada.ArgollaLlanera.control;

import java.io.File;
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
 * @version 1.0
 */
public class ControlPersistencia {

    private RandomAccessFile file;
    private String filePath;

    // Tamaño fijo en bytes para cada registro.
    public static final int RECORD_SIZE = 160;

    /**
     * Crea una instancia para archivo por defecto (ruta fija) y asegura la existencia de la carpeta.
     */
    public ControlPersistencia() throws IOException {
        this("Specs/data/resultados.dat");
    }

    /**
     * Crea una instancia para archivo con ruta específica y asegura la existencia de la carpeta.
     * 
     * @param filePath ruta al archivo de acceso aleatorio
     * @throws IOException si el archivo no puede abrirse
     */
    public ControlPersistencia(String filePath) throws IOException {
        this.filePath = filePath;
        File archivo = new File(filePath);
        File padre = archivo.getParentFile();
        if (padre != null && !padre.exists()) {
            boolean creada = padre.mkdirs();
            if (!creada) {
                throw new IOException("No se pudo crear la carpeta para persistencia: " + padre.getAbsolutePath());
            }
        }
        this.file = new RandomAccessFile(filePath, "rw");
    }

    /**
     * Escribe un registro en el archivo al final, con campos de tamaño fijo.
     */
    public void escribirRegistro(String clave, String nombreEquipo, String[] jugadores, String resultado) {
        try {
            file.seek(file.length());  // Mover el puntero al final para appending
            StringBuilder registro = new StringBuilder();
            registro.append(padRight(clave, 20));
            registro.append(padRight(nombreEquipo, 30));
            for (String jugador : jugadores) {
                registro.append(padRight(jugador, 30));
            }
            registro.append(padRight(resultado, 20));

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
            e.printStackTrace();
        }
    }

    /**
     * Lee todos los registros del archivo y los imprime en consola,
     * mostrando el contenido estructurado en forma legible.
     */
    public void leerRegistros() {
        try (RandomAccessFile fileLectura = new RandomAccessFile(filePath, "r")) {
            long totalRegistros = fileLectura.length() / RECORD_SIZE;
            System.out.println("Registros guardados en archivo:");
            for (int i = 0; i < totalRegistros; i++) {
                fileLectura.seek(i * RECORD_SIZE);
                byte[] datos = new byte[RECORD_SIZE];
                fileLectura.readFully(datos);
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
