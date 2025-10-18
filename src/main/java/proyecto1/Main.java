package proyecto1;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        System.out.println("=== Estrategia Voraz ===");

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

        // Llamar la estrategia voraz
        Result resultado = Solution.roV(ts, tr, p, n);

        // Mostrar resultado por consola
        System.out.println("Resultado de la estrategia voraz:");
        System.out.println(resultado);

        // Preguntar si desea guardar salida en archivo
        Scanner scanner = new Scanner(System.in);
        System.out.print("\nDesea guardar el resultado en un archivo? (s/n): ");
        String respuesta = scanner.nextLine();

        if (respuesta.equalsIgnoreCase("s")) {
            System.out.print("Ingrese la ruta del archivo de salida: ");
            String rutaSalida = scanner.nextLine();
            try {
                java.io.BufferedWriter bw = new java.io.BufferedWriter(new java.io.FileWriter(rutaSalida));
                bw.write(resultado.getCosto() + "\n");
                for (int index : resultado.getOrden()) {
                    bw.write(index + "\n");
                }
                bw.close();
                System.out.println("Resultado guardado en: " + rutaSalida);
            } catch (Exception e) {
                System.err.println("Error al escribir archivo: " + e.getMessage());
            }
        }

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