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
     * @return objeto Solution con el costo total y el orden de riego
     */
    public static Solution roV(int[] ts, int[] tr, int[] p, int n) {

        // Crear lista de índices (0, 1, 2, ..., n-1)
        List<Integer> indices = new ArrayList<>();
        for (int i = 0; i < n; i++) indices.add(i);

        // Regla voraz: ordenar por ts / (p * tr)
        indices.sort(Comparator.comparingDouble(i ->
                (double) ts[i] / (p[i] * tr[i])
        ));

        int tiempoActual = 0;
        int costoTotal = 0;
        int[] orden = new int[n];
        int index = 0;

        // Calcular costo total según el orden elegido
        for (int i : indices) {
            int finRiego = tiempoActual + tr[i];
            int retraso = Math.max(0, finRiego - ts[i]);
            int penalizacion = p[i] * retraso;

            costoTotal += penalizacion;
            orden[index] = i;
            tiempoActual = finRiego;
            index++;
        }

        return new Solution(costoTotal, orden);
    }

    public static void main(String[] args) {
        Solution sol = roV(new int[] {1,2,3}, new int[] {4,5,2}, new int[]{6,7,2}, 3);
        System.out.println(sol.getCrfTotal());
    }
}
