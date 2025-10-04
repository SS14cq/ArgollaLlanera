/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Udistrital.avanzada.ArgollaLlanera.modelo;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 *
 * @author juanr
 */
public class Juego {
    private List<Equipo> equipos;
    private Map<Equipo, Integer> puntajes;
    private int rondaActual;
    private boolean empate;

    public Juego(List<Equipo> equipos) {
        this.equipos = equipos;
        this.puntajes = new HashMap<>();
        for (Equipo equipo : equipos) {
            puntajes.put(equipo, 0);
        }
        this.rondaActual = 1;
        this.empate = false;
    }

    /**
     * Simula una ronda de lanzamientos entre dos equipos.
     * @return El equipo ganador o null si hay empate.
     */
    public Equipo jugarRonda() {
        Collections.shuffle(equipos); // Turno aleatorio
        Equipo equipoA = equipos.get(0);
        Equipo equipoB = equipos.get(1);

        int puntosA = lanzarEquipo(equipoA);
        int puntosB = lanzarEquipo(equipoB);

        puntajes.put(equipoA, puntosA);
        puntajes.put(equipoB, puntosB);

        if (puntosA == 21 && puntosB == 21) {
            empate = true;
            return null;
        } else if (puntosA == 21 && puntosB < 21) {
            return equipoA;
        } else if (puntosB == 21 && puntosA < 21) {
            return equipoB;
        } else {
            return null; // No hay ganador aún
        }
    }

    /**
     * Simula los lanzamientos de un equipo.
     * @param equipo El equipo que lanza.
     * @return Total de puntos obtenidos.
     */
    private int lanzarEquipo(Equipo equipo) {
        int total = 0;
        for (Jugador jugador : equipo.getJugadores()) {
            String resultado = generarResultadoAleatorio();
            int puntos = calcularPuntos(resultado);
            jugador.setResultado(resultado);
            jugador.setPuntos(puntos);
            total += puntos;
        }
        return total;
    }

    /**
     * Genera aleatoriamente el resultado de un lanzamiento.
     */
    private String generarResultadoAleatorio() {
        String[] opciones = {"moñona", "engarzada", "hueco", "palmo", "timbre", "otro"};
        Random rand = new Random();
        return opciones[rand.nextInt(opciones.length)];
    }

    /**
     * Asigna puntos según el resultado del lanzamiento.
     */
    private int calcularPuntos(String resultado) {
        return switch (resultado) {
            case "moñona" -> 8;
            case "engarzada" -> 5;
            case "hueco" -> 3;
            case "palmo" -> 2;
            case "timbre" -> 1;
            default -> 0;
        };
    }

    public boolean hayEmpate() {
        return empate;
    }

    public Map<Equipo, Integer> getPuntajes() {
        return puntajes;
    }

    public int getRondaActual() {
        return rondaActual;
    }

    public void siguienteRonda() {
        rondaActual++;
    }
}

