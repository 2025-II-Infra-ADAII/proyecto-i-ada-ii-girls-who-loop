package proyecto1;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;

public class ProgramacionDinamica {

    // Funciones para acceder al valor de un tablón
    public static int survivalValue(int[] farm, int indexTablon) {
        return farm[indexTablon * 3];
    }

    public static int wateredValue(int[] farm, int indexTablon) {
        return farm[indexTablon * 3 + 1];
    }

    public static int priorityValue(int[] farm, int indexTablon) {
        return farm[indexTablon * 3 + 2];
    }

    public static int calculoCRF(int[] finca, int tiempo_inicio, int indice_tablon) {
        int penalizacion = Math.max(0, tiempo_inicio + wateredValue(finca, indice_tablon) - survivalValue(finca, indice_tablon));
        return priorityValue(finca, indice_tablon) * penalizacion;
    }

    // Mapa de memoización
    static HashMap<Estado, Resultado> memo = new HashMap<>();

    // Solucion con programacion dinamica
    public static Solution roPD(int[] finca){

        if (finca == null || finca.length % 3 != 0) {
            throw new IllegalArgumentException("Formato de finca invalido. Debe ser multiplo de 3.");
        }

        // Crear conjunto inicial de tablones
        HashSet<Integer> tablones_disponibles = new HashSet<>();
        for (int i = 0; i < finca.length / 3; i++) {
            tablones_disponibles.add(i);
        }

        // Limpiar memoización (importante si se ejecuta múltiples veces)
        memo.clear();

        // Resolver usando DP con memoización
        resolverDP(finca, 0, tablones_disponibles);

        // Reconstruir la programación óptima
        int[] programacionOptima = reconstruirProgramacion(finca, tablones_disponibles);

        // Verificar el costo
        int costoVerificado = calcularCostoTotal(finca, programacionOptima);

        return new Solution(costoVerificado, programacionOptima);

    }

}