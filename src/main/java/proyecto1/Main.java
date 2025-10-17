package proyecto1;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        // Leer finca desde archivo
        List<int[]> finca = LectorArchivo.leerArchivo();
        if (finca.isEmpty()) {
            System.err.println("No se pudo leer la finca o esta vacia.");
            return;
        }

        // Selección de algoritmo
        System.out.println("Seleccione el metodo a ejecutar:");
        System.out.println("1. Fuerza Bruta");
        System.out.println("2. Voraz");
        System.out.println("3. Programación Dinamica");

        Scanner sc = new Scanner(System.in);
        int opcion = sc.nextInt();
        sc.close();

        Solution.Resultado resultado;

        switch (opcion) {
            case 1 -> resultado = Solution.roFB(finca);
            case 2 -> resultado = Solution.roV(finca);
            case 3 -> resultado = Solution.roPD(finca);
            default -> {
                System.err.println("Opcion no valida.");
                return;
            }
        }

        if (resultado.costo == -1) return; // caso fuerza bruta no viable

        System.out.println(resultado.costo);
        for (int idx : resultado.orden) {
            System.out.println(idx);
        }

        System.out.printf("\nTiempo de ejecucion (ms): %.3f%n", resultado.tiempoEjecucionNs / 1_000_000.0);

        try (FileWriter writer = new FileWriter("salida.txt")) {
            writer.write(resultado.costo + "\n");
            for (int idx : resultado.orden) {
                writer.write(idx + "\n");
            }

            writer.write("\nTiempo de ejecucion (ms): " + (resultado.tiempoEjecucionNs / 1_000_000.0));
        } catch (IOException e) {
            System.err.println("Error al escribir el archivo: " + e.getMessage());
        }
    }
}
