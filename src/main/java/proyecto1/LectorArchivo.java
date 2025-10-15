package proyecto1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class LectorArchivo {
    
    public int[] leerArchivo() {
        Scanner scanner = new Scanner(System.in);
        
        System.out.print("Ingrese la ruta del archivo: ");
        String ruta = scanner.nextLine();
        
        ArrayList<Integer> numeros = new ArrayList<>();
        
        try (BufferedReader br = new BufferedReader(new FileReader(ruta))) {
            // Leer la primera línea (n)
            String primeraLinea = br.readLine();
            int n = Integer.parseInt(primeraLinea.trim());
            
            // Leer las siguientes n líneas
            for (int i = 0; i < n; i++) {
                String linea = br.readLine();
                if (linea != null) {
                    // Separar por comas y convertir a enteros
                    String[] valores = linea.split(",");
                    for (String valor : valores) {
                        numeros.add(Integer.parseInt(valor.trim()));
                    }
                }
            }
            
        } catch (IOException e) {
            System.err.println("Error al leer el archivo: " + e.getMessage());
            scanner.close();
            return new int[0];
        } catch (NumberFormatException e) {
            System.err.println("Error al convertir números: " + e.getMessage());
            scanner.close();
            return new int[0];
        }
        
        // Convertir ArrayList a array primitivo
        int[] resultado = new int[numeros.size()];
        for (int i = 0; i < numeros.size(); i++) {
            resultado[i] = numeros.get(i);
        }

        scanner.close();
        
        return resultado;
    }
}