package proyecto1;

import java.util.Arrays;

/**
 * Implementacion de la solucion por fuerza bruta.
 *
 * Interfaz esperada: roFB(int[] farm)
 * - farm: array plano donde cada tablon ocupa 3 posiciones: [ts0, tr0, p0, ts1, tr1, p1, ...]
 * - devuelve Solution(crf_total, programacion_optima)
 */
public class FuerzaBruta {

    public static Solution roFB(int[] farm) {
        if (farm == null || farm.length % 3 != 0) {
            throw new IllegalArgumentException("Formato de finca invalido. Debe ser multiplo de 3.");
        }

        int n = farm.length / 3;
        boolean[] used = new boolean[n];
        int[] current = new int[n];
        BestHolder best = new BestHolder();
        best.bestCost = Long.MAX_VALUE;
        best.bestPerm = new int[n];

        backtrack(farm, n, 0, used, current, best);

        return new Solution((int) best.bestCost, best.bestPerm);
    }

    // Holder para resultados optimos
    private static class BestHolder {
        long bestCost;
        int[] bestPerm;
    }

    // Funcion recursiva: genera todas las permutaciones posibles
    private static void backtrack(int[] farm, int n, int depth, boolean[] used, int[] current, BestHolder best) {
        if (depth == n) {
            long cost = evaluateCRF(farm, current);
            if (cost < best.bestCost) {
                best.bestCost = cost;
                best.bestPerm = Arrays.copyOf(current, n);
            }
            return;
        }

        for (int i = 0; i < n; i++) {
            if (!used[i]) {
                used[i] = true;
                current[depth] = i;
                backtrack(farm, n, depth + 1, used, current, best);
                used[i] = false;
            }
        }
    }

    /**
     * Calcula el CRF total para una permutacion dada.
     * Formula: Sumatoria de p_i * max(0, (tiempo_finalizacion_i - ts_i))
     */
    private static long evaluateCRF(int[] farm, int[] permutation) {
        int n = permutation.length;
        long time = 0;
        long totalCRF = 0;

        for (int idx = 0; idx < n; idx++) {
            int tabIndex = permutation[idx];
            int ts = farm[tabIndex * 3];       // tiempo de supervivencia
            int tr = farm[tabIndex * 3 + 1];   // tiempo de regado
            int p  = farm[tabIndex * 3 + 2];   // prioridad

            time += tr;
            long delay = Math.max(0, time - ts);
            totalCRF += (long) p * delay;
        }

        return totalCRF;
    }
}
