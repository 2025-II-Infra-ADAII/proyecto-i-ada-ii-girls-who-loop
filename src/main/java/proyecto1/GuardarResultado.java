package proyecto1;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Clase encargada de guardar los resultados de la estrategia en un archivo de salida.
 */
public class GuardarResultado {

    /**
     * Guarda el resultado en la carpeta src/main/resources/salidas/
     * 
     */

    public static void guardar(Solution resultado, String nombreArchivo) {

        // Crear carpeta si no existe
        File carpetaSalidas = new File("src/main/resources/salidas");
        if (!carpetaSalidas.exists()) {
            carpetaSalidas.mkdirs();
        }

        // Si el usuario solo dio un nombre, guardar dentro de /salidas/
        if (!nombreArchivo.contains("/") && !nombreArchivo.contains("\\")) {
            nombreArchivo = "src/main/resources/salidas/" + nombreArchivo;
        }

        // Asegurar extensión .txt
        if (!nombreArchivo.endsWith(".txt")) {
            nombreArchivo += ".txt";
        }

        // Crear archivo y escribir resultado
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(nombreArchivo))) {
            bw.write(resultado.getCrfTotal() + "\n");
            for (int index : resultado.getProgramacionOptima()) {
                bw.write(index + "\n");
            }
            System.out.println("Resultado guardado en: " + nombreArchivo);
        } catch (IOException e) {
            System.err.println("Error al escribir archivo: " + e.getMessage());
        }
    }
}
