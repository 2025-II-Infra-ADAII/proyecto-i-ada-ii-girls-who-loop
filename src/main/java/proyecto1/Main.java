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
            System.err.println("No se pudo leer el archivo o esta vacio.");
            return;
        }

        // Calcular numero de tablones
        int n = farm.length / 3;
        System.out.println("Numero de tablones: " + n);

        // Separar ts, tr, p
        int[] ts = new int[n];
        int[] tr = new int[n];
        int[] p  = new int[n];

        for (int i = 0; i < n; i++) {
            ts[i] = survivalValue(farm, i);
            tr[i] = wateredValue(farm, i);
            p[i]  = priorityValue(farm, i);
        }

        // Medir tiempo de ejecucion de la estrategia voraz
        long inicio = System.nanoTime();
        Solution resultado = EstrategiaVoraz.roV(ts, tr, p, n);
        long fin = System.nanoTime();

        // Calcular duracion en milisegundos
        double duracion = (fin - inicio) / 1_000_000.0;

        // Mostrar resultado por consola
        System.out.println("Resultado de la estrategia voraz:");
        System.out.println(resultado);
        System.out.printf("Tiempo de ejecucion: %.4f ms%n", duracion);

        // Guardar resultado en archivo
        Scanner scanner = new Scanner(System.in);
        System.out.print("Ingrese la ruta del archivo de salida (guardar resultado): ");
        String nombreArchivo = scanner.nextLine().trim();

        GuardarResultado.guardar(resultado, nombreArchivo);

        // ===================== ESTRATEGIA FUERZA BRUTA =====================
        System.out.println("\n============= Estrategia Fuerza Bruta =============");
        System.out.println("Ejecutando Fuerza Bruta con " + n + " tablones...");

        long inicioFB = System.nanoTime();
        Solution solFB = FuerzaBruta.roFB(farm);
        long finFB = System.nanoTime();

        double duracionFB = (finFB - inicioFB) / 1_000_000.0;

        System.out.println("Costo minimo (FB): " + solFB.getCrfTotal());
        System.out.println("Orden optimo (FB): " + solFB);
        System.out.printf("Tiempo de ejecucion: %.4f ms%n", duracionFB);

        String archivoFB = nombreArchivo + "_fuerzabruta.txt";

        Solution resultadoFB = new Solution(
            solFB.getCrfTotal(),
            solFB.getProgramacionOptima()
        );

        GuardarResultado.guardar(resultadoFB, archivoFB);
        System.out.println("Resultado de fuerza bruta guardado en: " + archivoFB);

        scanner.close();


        // ===================== ESTRATEGIA PROGRAMACION DINAMICA =====================
        System.out.println("\n============= Estrategia Programacion Dinamica =============");
        System.out.println("Ejecutando Programacion Dinamica con " + n + " tablones...");

        long inicioPD = System.nanoTime();
        Solution solPD = ProgramacionDinamica.roPD(farm);
        long finPD = System.nanoTime();

        double duracionPD = (finPD - inicioPD) / 1_000_000.0;

        System.out.println("Costo minimo (PD): " + solPD.getCrfTotal());
        System.out.println("Orden optimo (PD): " + solPD);
        System.out.printf("Tiempo de ejecucion: %.4f ms%n", duracionPD);

        String archivoPD = nombreArchivo + "_programacionDinamica.txt";

        Solution resultadoPD = new Solution(
            solPD.getCrfTotal(),
            solPD.getProgramacionOptima()
        );

        GuardarResultado.guardar(resultadoPD, archivoPD);
        System.out.println("Resultado de programacion dinamica guardado en: " + archivoPD);

        scanner.close();


    }

    // ===================== Metodos auxiliares =====================
    public static int survivalValue(int[] farm, int indexTablon) {
        return farm[indexTablon * 3];
    }


    public static int wateredValue(int[] farm, int indexTablon) {
        return farm[indexTablon * 3 + 1];
    }


    public static int priorityValue(int[] farm, int indexTablon){
        return farm[indexTablon * 3 + 2];
    }
}
