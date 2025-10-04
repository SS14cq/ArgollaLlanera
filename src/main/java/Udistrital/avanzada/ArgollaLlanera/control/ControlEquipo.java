package Udistrital.avanzada.ArgollaLlanera.control;

import Udistrital.avanzada.ArgollaLlanera.modelo.ConexionPropiedades;
import Udistrital.avanzada.ArgollaLlanera.modelo.Equipo;
import Udistrital.avanzada.ArgollaLlanera.modelo.Jugador;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Gestor de equipos (servicio en memoria).
 *
 * <p>La clase centraliza operaciones sobre equipos: creación, obtención,
 * listado y la manipulación básica de jugadores dentro de un equipo.
 * Además, ofrece carga inicial desde un archivo de propiedades con el
 * formato esperado en <code>src/Specs/Data/Equipos.properties</code>.
 *</p>
 *
 * <h3>Formato del archivo .properties esperado</h3>
 * <pre>
 * numeroEquipos.count=2
 *
 * equipo.1.clave=E001
 * equipo.1.name=ELN
 * equipo.1.numeroJugadores=4
 * equipo.1.player.1.name=Sofia
 * equipo.1.player.1.nick=Araucana
 * equipo.1.player.1.photo=Imagenes/Jugador.jpg
 * ...
 * </pre>
 *
 * <p>La carga desde archivo se realiza automáticamente en el constructor
 * por defecto si existe el archivo <code>src/Specs/Data/Equipos.properties</code>.
 * Para cargar desde otra ruta utilice el constructor {@link #ControlEquipo(String)}.
 *</p>
 *
 * @see Udistrital.avanzada.ArgollaLlanera.modelo.Equipo
 * @see Udistrital.avanzada.ArgollaLlanera.modelo.Jugador
 * @see Udistrital.avanzada.ArgollaLlanera.modelo.ConexionPropiedades
 */
public class ControlEquipo {
	private final Map<String, Equipo> equipos;

	public ControlEquipo() {
		this.equipos = new HashMap<>();
		// Intentar cargar archivo de propiedades por defecto (ruta relativa al proyecto)
		ConexionPropiedades cp = ConexionPropiedades.tryLoadFromFile("src/Specs/Data/Equipos.properties");
		if (cp != null) {
			try {
				cargarDesdeProperties(cp.asProperties());
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	/**
	 * Constructor que carga equipos desde una ruta de propiedades especificada.
	 *
	* @param propertiesPath ruta al archivo .properties (absoluta o relativa)
	* @throws IOException si no se puede leer el archivo de propiedades
	 */
	public ControlEquipo(String propertiesPath) throws IOException {
		this.equipos = new HashMap<>();
		ConexionPropiedades cp = ConexionPropiedades.loadFromFile(propertiesPath);
		cargarDesdeProperties(cp.asProperties());
	}
	/**
	 * Carga equipos y jugadores desde un objeto {@link Properties}.
	 *
	 * <p>El método interpreta las claves según el formato descrito en la
	 * documentación de la clase: <code>equipo.N.clave</code>,
	 * <code>equipo.N.name</code>, <code>equipo.N.numeroJugadores</code> y
	 * <code>equipo.N.player.M.*</code> para cada jugador del equipo.</p>
	 *
	 * @param props propiedades ya cargadas
	 * @throws IOException si ocurre un error al interpretar las propiedades (poco probable)
	 */
	private void cargarDesdeProperties(Properties props) throws IOException {
		String countStr = props.getProperty("numeroEquipos.count");
		int count = -1;
		if (countStr != null) {
			try {
				count = Integer.parseInt(countStr.trim());
			} catch (NumberFormatException ex) {
				count = -1;
			}
		}

		if (count > 0) {
			for (int i = 1; i <= count; i++) {
				String clave = props.getProperty(String.format("equipo.%d.clave", i));
				String nombre = props.getProperty(String.format("equipo.%d.name", i));
				if (clave == null || nombre == null) continue;
				Equipo e = crearEquipo(clave, nombre);
				String numJugStr = props.getProperty(String.format("equipo.%d.numeroJugadores", i));
				int numJug = -1;
				if (numJugStr != null) {
					try { numJug = Integer.parseInt(numJugStr.trim()); } catch (NumberFormatException ex) { numJug = -1; }
				}
				int limit = (numJug > 0) ? numJug : Equipo.MAX_JUGADORES;
				for (int j = 1; j <= limit && e != null; j++) {
					String pname = props.getProperty(String.format("equipo.%d.player.%d.name", i, j));
					if (pname == null) continue;
					String pnick = props.getProperty(String.format("equipo.%d.player.%d.nick", i, j));
					String pphoto = props.getProperty(String.format("equipo.%d.player.%d.photo", i, j));
					Jugador p = new Jugador(pname, pnick == null ? "" : pnick, pphoto == null ? "" : pphoto);
					e.agregarJugador(p);
				}
			}
		} else {
			// fallback: detectar equipos presentes hasta que no haya clave
			for (int i = 1; ; i++) {
				String clave = props.getProperty(String.format("equipo.%d.clave", i));
				if (clave == null) break;
				String nombre = props.getProperty(String.format("equipo.%d.name", i));
				Equipo e = crearEquipo(clave, nombre == null ? clave : nombre);
				String numJugStr = props.getProperty(String.format("equipo.%d.numeroJugadores", i));
				int numJug = -1;
				if (numJugStr != null) {
					try { numJug = Integer.parseInt(numJugStr.trim()); } catch (NumberFormatException ex) { numJug = -1; }
				}
				int limit = (numJug > 0) ? numJug : Equipo.MAX_JUGADORES;
				for (int j = 1; j <= limit && e != null; j++) {
					String pname = props.getProperty(String.format("equipo.%d.player.%d.name", i, j));
					if (pname == null) break;
					String pnick = props.getProperty(String.format("equipo.%d.player.%d.nick", i, j));
					String pphoto = props.getProperty(String.format("equipo.%d.player.%d.photo", i, j));
					Jugador p = new Jugador(pname, pnick == null ? "" : pnick, pphoto == null ? "" : pphoto);
					e.agregarJugador(p);
				}
			}
		}
	}

	/**
	 * Crea un nuevo equipo si la clave no existe ya.
	 *
	 * @param clave identificador 
	 * @param nombre nombre del equipo
	 * @return el equipo creado, o null si ya existía la clave
	 */
	public Equipo crearEquipo(String clave, String nombre) {
		if (clave == null || nombre == null) {
			throw new IllegalArgumentException("Clave y nombre no pueden ser nulos");
		}
		if (equipos.containsKey(clave)) {
			return null;
		}
		Equipo e = new Equipo(clave, nombre);
		equipos.put(clave, e);
		return e;
	}

	/**
	 * Obtiene un equipo por su clave.
	 */
	public Equipo getEquipo(String clave) {
		return equipos.get(clave);
	}

	/**
	 * Lista todos los equipos registrados.
	 */
	public Collection<Equipo> listarEquipos() {
		return Collections.unmodifiableCollection(new ArrayList<>(equipos.values()));
	}

	/**
	 * Agrega un jugador a un equipo identificado por clave.
	 *
	 * @return true si se agregó exitosamente, false si el equipo no existe o está lleno
	 */
	public boolean agregarJugadorAEquipo(String clave, Jugador jugador) {
		Equipo e = equipos.get(clave);
		if (e == null || jugador == null) return false;
		return e.agregarJugador(jugador);
	}

	/**
	 * Quita un equipo del registro.
	 *
	 * @return el equipo removido o null si no existía
	 */
	public Equipo removerEquipo(String clave) {
		return equipos.remove(clave);
	}

	/**
	 * Calcula la suma de puntos de todos los jugadores del equipo.
	 *
	 * @return puntaje total del equipo o -1 si el equipo no existe
	 */
	public int puntosEquipo(String clave) {
		Equipo e = equipos.get(clave);
		if (e == null) return -1;
		return e.getJugadores().stream().mapToInt(Jugador::getPuntos).sum();
	}

	/**
	 * Asigna (suma) puntos a cada jugador del equipo. Útil para rondas donde todo el equipo gana.
	 *
	 * @return true si se aplicó exitosamente, false si el equipo no existe
	 */
	public boolean asignarPuntosEquipo(String clave, int puntos) {
		Equipo e = equipos.get(clave);
		if (e == null) return false;
		e.getJugadores().forEach(j -> j.agregarPuntosJugador(puntos));
		return true;
	}

}
