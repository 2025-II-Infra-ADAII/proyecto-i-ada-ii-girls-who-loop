package proyecto1;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        System.out.println("============= Estrategia Voraz =============");
        // Ejemplo: src/main/resources/entradas/10_tablones.txt

        // Leer archivo con LectorArchivo
        LectorArchivo lector = new LectorArchivo();
        int[] farm = lector.leerArchivo();

        if (farm.length == 0) {
            System.err.println("No se pudo leer el archivo o está vacío.");
            return;
        }

        // Calcular número de tablones
        int n = farm.length / 3;
        System.out.println("Número de tablones: " + n);

        // Separar ts, tr, p
        int[] ts = new int[n];
        int[] tr = new int[n];
        int[] p  = new int[n];

        for (int i = 0; i < n; i++) {
            ts[i] = survivalValue(farm, i);
            tr[i] = wateredValue(farm, i);
            p[i]  = priorityValue(farm, i);
        }

        // Medir tiempo de ejecución de la estrategia voraz
        long inicio = System.nanoTime();
        Result resultado = EstrategiaVoraz.roV(ts, tr, p, n);
        long fin = System.nanoTime();

        // Calcular duración en milisegundos
        double duracion = (fin - inicio) / 1_000_000.0;

        
        // Mostrar resultado por consola
        System.out.println("Resultado de la estrategia voraz:");
        System.out.println(resultado);
        System.out.printf("Tiempo de ejecución: %.4f ms%n", duracion);


        // Guardar resultado en archivo
        Scanner scanner = new Scanner(System.in);
        System.out.print("Ingrese la ruta del archivo de salida (guardar resultado): ");
        String nombreArchivo = scanner.nextLine().trim();

        GuardarResultado.guardar(resultado, nombreArchivo);

        scanner.close();
    }


    public static int survivalValue(int[] farm, int indexTablon) {
        return farm[indexTablon*3];
    }

    public static int wateredValue(int[] farm, int indexTablon) {
        return farm[indexTablon*3 + 1];
    }

    public static int priorityValue(int[] farm, int indexTablon){
        return farm[indexTablon*3 + 2];
    }
}