package proyecto1;

import java.util.*;

public class Solution {

    public static class Resultado {
        public List<Integer> orden;
        public int costo;
        public long tiempoEjecucionNs;

        public Resultado(List<Integer> orden, int costo, long tiempoEjecucionNs) {
            this.orden = orden;
            this.costo = costo;
            this.tiempoEjecucionNs = tiempoEjecucionNs;
        }
    }

    /* ============================================================
       FUERZA BRUTA (roFB)
    ============================================================ */
    public static Resultado roFB(List<int[]> finca) {
        int n = finca.size();

        if (n > 12) {
            System.err.println("⚠️ Fuerza bruta no es viable para n=" + n + ". Usa roV() o roPD().");
            return new Resultado(Collections.emptyList(), -1, 0);
        }

        long inicio = System.nanoTime();
        List<Integer> indices = new ArrayList<>();
        for (int i = 0; i < n; i++) indices.add(i);

        List<Integer> mejorOrden = new ArrayList<>();
        int[] mejorCosto = {Integer.MAX_VALUE}; // usamos array para pasar por referencia

        generarPermutaciones(finca, indices, 0, mejorCosto, mejorOrden);

        long fin = System.nanoTime();
        return new Resultado(mejorOrden, mejorCosto[0], fin - inicio);
    }

    private static void generarPermutaciones(List<int[]> finca, List<Integer> arr, int k, int[] mejorCosto, List<Integer> mejorOrden) {
        if (k == arr.size()) {
            int costo = calcularCosto(finca, arr);
            if (costo < mejorCosto[0]) {
                mejorCosto[0] = costo;
                mejorOrden.clear();
                mejorOrden.addAll(arr);
            }
            return;
        }

        for (int i = k; i < arr.size(); i++) {
            Collections.swap(arr, i, k);
            generarPermutaciones(finca, arr, k + 1, mejorCosto, mejorOrden);
            Collections.swap(arr, i, k);
        }
    }

    private static int calcularCosto(List<int[]> finca, List<Integer> perm) {
        int tiempo = 0, costoTotal = 0;
        for (int idx : perm) {
            int[] t = finca.get(idx);
            int ts = t[0], tr = t[1], p = t[2];
            int finRiego = tiempo + tr;
            costoTotal += p * Math.max(0, (finRiego - ts));
            tiempo = finRiego;
        }
        return costoTotal;
    }

    /* ============================================================
       VORAZ (roV)
    ============================================================ */
    public static Resultado roV(List<int[]> finca) {
        System.out.println("Ejecutando");
        return new Resultado(Collections.emptyList(), 0, 0);
    }

    /* ============================================================
       PROGRAMACIÓN DINÁMICA (roPD)
    ============================================================ */
    public static Resultado roPD(List<int[]> finca) {
        System.out.println("Ejecutando");
        return new Resultado(Collections.emptyList(), 0, 0);
    }
}
