package proyecto1;

import java.util.*;

public class Solution {


    public int crf_total;
    public int[] programacion_optima;

    public Solution(int crf_total, int[] programacion_optima) {
        this.crf_total = crf_total;
        this.programacion_optima = programacion_optima;
    }
    /**
     * Estrategia Voraz: roV
     * Ordena los tablones según la heurística ts / (p * tr)
     * y calcula el costo total.
     *
     * @param ts arreglo de tiempos de supervivencia
     * @param tr arreglo de tiempos de regado
     * @param p  arreglo de prioridades
     * @param n  número de tablones
     * @return objeto Result con el orden y el costo total
     */
    public static Result roV(int[] ts, int[] tr, int[] p, int n) {

        // Crear lista de índices (0, 1, 2, ..., n-1)
        List<Integer> indices = new ArrayList<>();
        for (int i = 0; i < n; i++) indices.add(i);

        // Regla voraz: ordenar por ts / (p * tr)
        indices.sort(Comparator.comparingDouble(i ->
                (double) ts[i] / (p[i] * tr[i])
        ));

        int tiempoActual = 0;
        int costoTotal = 0;
        List<Integer> orden = new ArrayList<>();

        // Calcular costo total según el orden elegido
        for (int i : indices) {
            int finRiego = tiempoActual + tr[i];
            int retraso = Math.max(0, finRiego - ts[i]);
            int penalizacion = p[i] * retraso;

            costoTotal += penalizacion;
            orden.add(i);

            tiempoActual = finRiego;
        }

        return new Result(orden, costoTotal);
    }
}
