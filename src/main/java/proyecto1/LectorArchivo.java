package proyecto1;

import java.io.*;
import java.util.*;

public class LectorArchivo {

    /**
     * Lee un archivo de texto con el formato:
     * n
     * ts,tr,p
     * ...
     * Devuelve una lista de tablones representada como una lista de arrays int[3].
     */
    public static List<int[]> leerArchivo() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Ingrese la ruta del archivo: ");
        String ruta = scanner.nextLine();

        List<int[]> finca = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(ruta))) {
            String primeraLinea = br.readLine();
            int n = Integer.parseInt(primeraLinea.trim());

            for (int i = 0; i < n; i++) {
                String linea = br.readLine();
                if (linea == null || linea.isEmpty()) continue;
                String[] valores = linea.split(",");
                int ts = Integer.parseInt(valores[0].trim());
                int tr = Integer.parseInt(valores[1].trim());
                int p = Integer.parseInt(valores[2].trim());
                finca.add(new int[]{ts, tr, p});
            }

        } catch (IOException e) {
            System.err.println("Error al leer el archivo: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("Formato incorrecto: el archivo contiene valores no numericos.");
        }

        return finca;
    }
}