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
    public static HashMap<Estado, Resultado> memo = new HashMap<>();

    // Solucion con programacion dinamica
    public static Solution roPD(int[] finca){

        if(finca.length == 0){
            return new Solution(0, finca);
        }

        else if (finca == null || finca.length % 3 != 0) {
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

    // Clase para representar el estado (conjunto de tablones disponibles + tiempo)
    public static class Estado {
        HashSet<Integer> tablones;
        int tiempo;

        public Estado(HashSet<Integer> tablones, int tiempo) {
            this.tablones = new HashSet<>(tablones);
            this.tiempo = tiempo;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Estado estado = (Estado) o;
            return tiempo == estado.tiempo && tablones.equals(estado.tablones);
        }

        @Override
        public int hashCode() {
            return Objects.hash(tablones, tiempo);
        }
    }

    // Clase para almacenar el resultado de un estado
    public static class Resultado {
        public int costo;
        public int primerTablon; // El tablón óptimo a regar en este estado

        Resultado(int costo, int primerTablon) {
            this.costo = costo;
            this.primerTablon = primerTablon;
        }
    }

    

    /**
     * Encuentra el costo mínimo y la decisión óptima para un estado dado
     * @return Resultado con el costo mínimo y el tablón a regar
     */
    public static Resultado resolverDP(int[] finca, int tiempo_actual, HashSet<Integer> tablones_disponibles) {
        
        // Caso base: no quedan tablones por regar
        if (tablones_disponibles.isEmpty()) {
            return new Resultado(0, -1);
        }

        // Crear estado actual
        Estado estadoActual = new Estado(tablones_disponibles, tiempo_actual);

        // Verificar si ya calculamos este estado
        if (memo.containsKey(estadoActual)) {
            return memo.get(estadoActual);
        }

        // Explorar todas las opciones
        int costoMinimo = Integer.MAX_VALUE;
        int mejorTablon = -1;

        for (int tablon : tablones_disponibles) {
            // Calcular costo de regar este tablón
            int costoActual = calculoCRF(finca, tiempo_actual, tablon);
            
            // Calcular nuevo tiempo después de regar
            int nuevoTiempo = tiempo_actual + wateredValue(finca, tablon);
            
            // Crear nuevo conjunto sin este tablón
            HashSet<Integer> nuevosDisponibles = new HashSet<>(tablones_disponibles);
            nuevosDisponibles.remove(tablon);
            
            // Resolver recursivamente para los tablones restantes
            Resultado resultadoRestante = resolverDP(finca, nuevoTiempo, nuevosDisponibles);
            
            // Calcular costo total de este camino
            int costoTotal = costoActual + resultadoRestante.costo;
            
            // Actualizar si encontramos un mejor camino
            if (costoTotal < costoMinimo) {
                costoMinimo = costoTotal;
                mejorTablon = tablon;
            }
        }

        // Guardar resultado en memo
        Resultado resultado = new Resultado(costoMinimo, mejorTablon);
        memo.put(estadoActual, resultado);
        
        return resultado;
    }

    /**
     * Reconstruye la programación óptima siguiendo las decisiones guardadas
     */
    public static int[] reconstruirProgramacion(int[] finca, HashSet<Integer> tablones_disponibles) {
        int n = finca.length / 3;
        int[] programacion = new int[n];
        int tiempo_actual = 0;
        
        HashSet<Integer> disponibles = new HashSet<>(tablones_disponibles);
        
        for (int i = 0; i < n; i++) {
            Estado estadoActual = new Estado(disponibles, tiempo_actual);
            Resultado resultado = memo.get(estadoActual);
            
            if (resultado == null) {
                System.err.println("Error: Estado no encontrado en memo");
                break;
            }
            
            int tablonOptimo = resultado.primerTablon;
            programacion[i] = tablonOptimo;
            
            // Actualizar para siguiente iteración
            tiempo_actual += wateredValue(finca, tablonOptimo);
            disponibles.remove(tablonOptimo);
        }
        
        return programacion;
    }

    /**
     * Calcula el costo total de una programación dada
     */
    public static int calcularCostoTotal(int[] finca, int[] programacion) {
        int costoTotal = 0;
        int tiempo = 0;
        
        for (int tablon : programacion) {
            costoTotal += calculoCRF(finca, tiempo, tablon);
            tiempo += wateredValue(finca, tablon);
        }
        
        return costoTotal;
    }

}
