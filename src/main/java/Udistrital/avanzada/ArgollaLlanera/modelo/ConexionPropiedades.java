/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Udistrital.avanzada.ArgollaLlanera.modelo;

/**
 *
 * @author juan-
 */
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Properties;

/**
 * Utilidad para cargar y exponer archivos de propiedades (.properties).
 * <p>
 * Provee métodos estáticos para crear instancias a partir de una ruta de
 * archivo en disco o desde el classpath, y métodos de consulta convenientes.
 * </p>
 */
public class ConexionPropiedades {

	private final Properties props;

	private ConexionPropiedades(Properties props) {
		this.props = props;
	}

	/**
	 * Carga propiedades desde un archivo en disco.
	 *
	 * @param ruta ruta al archivo .properties (absoluta o relativa)
	 * @return instancia de ConexionPropiedades con las propiedades cargadas
	 * @throws IOException si no existe el archivo o hay error de lectura
	 */
	public static ConexionPropiedades loadFromFile(String ruta) throws IOException {
		Objects.requireNonNull(ruta, "ruta no puede ser null");
		Path p = Paths.get(ruta);
		if (!Files.exists(p)) {
			// intentar también como archivo relativo al proyecto
			File f = new File(ruta);
			if (!f.exists()) {
				throw new IOException("Archivo de propiedades no encontrado: " + ruta);
			}
			p = f.toPath();
		}
		Properties props = new Properties();
		try (InputStream is = Files.newInputStream(p)) {
			props.load(is);
		}
		return new ConexionPropiedades(props);
	}

	/**
	 * Carga propiedades desde un recurso en el classpath.
	 *
	 * @param resource ruta del recurso en classpath (p. ej. "config/archivo.properties")
	 * @return instancia de ConexionPropiedades
	 * @throws IOException si no se puede leer el recurso
	 */
	public static ConexionPropiedades loadFromClasspath(String resource) throws IOException {
		Objects.requireNonNull(resource, "resource no puede ser null");
		try (InputStream is = ConexionPropiedades.class.getClassLoader().getResourceAsStream(resource)) {
			if (is == null) throw new IOException("Recurso no encontrado en classpath: " + resource);
			Properties props = new Properties();
			props.load(is);
			return new ConexionPropiedades(props);
		}
	}

	/**
	 * Intenta cargar desde archivo y si falla devuelve null (no lanza excepción).
	 */
	public static ConexionPropiedades tryLoadFromFile(String ruta) {
		try {
			return loadFromFile(ruta);
		} catch (IOException ex) {
			return null;
		}
	}

	/**
	 * Obtiene el valor de la propiedad o null si no existe.
	 */
	public String get(String key) {
		return props.getProperty(key);
	}

	/**
	 * Obtiene el valor de la propiedad o el valor por defecto si no existe.
	 */
	public String getOrDefault(String key, String def) {
		return props.getProperty(key, def);
	}

	/**
	 * Devuelve el objeto Properties subyacente (lectura).
	 */
	public Properties asProperties() {
		return (Properties) props.clone();
	}

/**
 * Carga una lista de equipos desde un archivo de propiedades (.properties).
 *
 * <p>Este método lee un archivo de configuración que guarda la información 
 * de varios equipos y sus jugadores. Usa la clase {@link ConexionPropiedades} 
 * para acceder a los datos y crear objetos {@link Equipo} y {@link Jugador} 
 * según la cantidad que haya registrada en el archivo.</p>
 * @param rutaArchivo ruta al archivo .properties desde el cual se cargarán los datos.
 * @return una lista de objetos {@link Equipo} completamente inicializados.
 * @throws IOException si ocurre un error al leer el archivo de propiedades.
 */
public static List<Equipo> cargarEquiposDesdeProperties(String rutaArchivo) throws IOException {
    ConexionPropiedades conexion = loadFromFile(rutaArchivo);
    Properties props = conexion.asProperties();

    List<Equipo> equipos = new ArrayList<>();
    int count = Integer.parseInt(props.getProperty("numeroEquipos.count", "0"));

    for (int i = 1; i <= count; i++) {
        String clave = props.getProperty("equipo." + i + ".clave");
        String nombre = props.getProperty("equipo." + i + ".name");
        int numJugadores = Integer.parseInt(props.getProperty("equipo." + i + ".numeroJugadores", "0"));

        Equipo equipo = new Equipo(clave, nombre);

        for (int j = 1; j <= numJugadores; j++) {
            String jnombre = props.getProperty("equipo." + i + ".player." + j + ".name");
            if (jnombre == null) continue;
            String japodo = props.getProperty("equipo." + i + ".player." + j + ".nick", "");
            String jfoto = props.getProperty("equipo." + i + ".player." + j + ".photo", "");
            Jugador jugador = new Jugador(jnombre, japodo, jfoto);
            equipo.agregarJugador(jugador);
        }

        equipos.add(equipo);
    }

    return equipos;
}


}
