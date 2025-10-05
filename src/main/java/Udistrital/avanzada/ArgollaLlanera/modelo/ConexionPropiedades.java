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

}
