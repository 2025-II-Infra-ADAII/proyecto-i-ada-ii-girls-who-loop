package proyecto1;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Clase que implementa la estrategia Voraz del proyecto.
 * Ordena los tablones según la heurística ts / (p * tr)
 * y calcula el costo total (CRFΠ).
 */

public class EstrategiaVoraz {

    /**
     * Método principal de la estrategia voraz.
     * 
     * @param ts arreglo con tiempos de supervivencia
     * @param tr arreglo con tiempos de riego
     * @param p  arreglo con prioridades
     * @param n  número total de tablones
     * @return objeto Result con el costo total y el orden de riego
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
