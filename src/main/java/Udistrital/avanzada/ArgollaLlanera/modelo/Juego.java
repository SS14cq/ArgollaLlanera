package Udistrital.avanzada.ArgollaLlanera.modelo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.Random;

public class Juego {
    private List<Equipo> equipos;
    private Map<Equipo, Integer> puntajes;
    private boolean empate;

    public Juego(List<Equipo> equipos) {
        this.equipos = equipos;
        this.puntajes = new HashMap<>();
        for (Equipo equipo : equipos) {
            puntajes.put(equipo, 0);
        }
        this.empate = false;
    }

    public int lanzarEquipo(Equipo equipo) {
        // Se usa solo para turno completo (ya no se usa así en Control)
        int total = 0;
        for (Jugador jugador : equipo.getJugadores()) {
            String resultado = generarResultadoAleatorio();
            int puntos = calcularPuntos(resultado);
            jugador.setResultado(resultado);
            jugador.setPuntos(puntos);
            total += puntos;
        }
        puntajes.put(equipo, puntajes.get(equipo) + total);
        return puntajes.get(equipo);
    }

    // Métodos para lanzar por jugador individual (añadidos para ControlJuego)
    public String generarResultadoAleatorio() {
        String[] opciones = {"moñona", "engarzada", "hueco", "palmo", "timbre", "otro"};
        Random rand = new Random();
        return opciones[rand.nextInt(opciones.length)];
    }

    public int calcularPuntos(String resultado) {
        switch(resultado) {
            case "moñona": return 8;
            case "engarzada": return 5;
            case "hueco": return 3;
            case "palmo": return 2;
            case "timbre": return 1;
            default: return 0;
        }
    }

    public boolean equipoHaGanado(Equipo equipo) {
        return puntajes.get(equipo) >= 21;
    }

    public boolean hayEmpate() {
        List<Integer> puntos = new ArrayList<>(puntajes.values());
        int conteoMayoresIguales21 = 0;
        for (Integer p : puntos) {
            if (p >= 21) conteoMayoresIguales21++;
        }
        return conteoMayoresIguales21 > 1;
    }

    public Map<Equipo, Integer> getPuntajes() {
        return puntajes;
    }
}
