import org.junit.jupiter.api.*;

import proyecto1.EstrategiaVoraz;
import proyecto1.Solution;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;


class EstrategiaVorazTest {

    // Datos de prueba simulando diferentes tamaños de finca
    private int[] finca_5t;
    private int[] finca_10t;
    private int[] finca_12t;
    private int[] finca_15t;
    private int[] finca_18t;

    /**
     * ---------------------------------------------------------------
     * Inicialización de datos antes de cada prueba
     * Cada finca contiene tripletas (ts, tr, p)
     * ---------------------------------------------------------------
     */
    @BeforeEach
    void setUp() {
        finca_5t = new int[]{
            8,2,3,
            6,4,4,
            1,1,2,
            7,1,1,
            5,3,3
        };
        finca_10t = new int[]{
            12,4,3,
            8,3,4,
            15,5,2,
            6,2,3,
            10,3,1,
            20,6,4,
            5,2,2,
            18,4,3,
            7,3,1,
            14,5,2
        };
        finca_12t = new int[]{
            16,5,1,
            12,3,2,
            20,7,4,
            8,2,4,
            14,4,1,
            10,3,2,
            18,6,3,
            9,2,4,
            15,5,3,
            11,4,1,
            13,3,4,
            7,2,2
        };
        finca_15t = new int[]{
            18,6,1,
            12,4,4,
            22,8,3,
            9,2,4,
            15,5,1,
            11,3,3,
            20,7,2,
            8,2,4,
            16,5,1,
            13,4,2,
            10,3,4,
            14,4,1,
            19,6,4,
            7,2,1,
            17,5,3
        };
        finca_18t = new int[]{
            20,7,1,
            14,4,4,
            25,9,3,
            10,3,3,
            17,5,4,
            12,4,1,
            22,8,1,
            9,2,2,
            18,6,3,
            15,5,4,
            11,3,4,
            16,5,3,
            21,7,1,
            8,2,4,
            19,6,1,
            13,4,4,
            23,8,3,
            7,2,3
        };
    }


    
    // =======================================================
    // PRUEBAS BÁSICAS DE ESTRUCTURA Y ACCESO A DATOS
    // =======================================================

    @Test
    @DisplayName("Acceso correcto a valores ts, tr, p")
    void testAccesoValores() {
        // Verifica que las tripletas (ts, tr, p) se interpreten correctamente
        assertEquals(8, finca_5t[0]); // ts
        assertEquals(2, finca_5t[1]); // tr
        assertEquals(3, finca_5t[2]); // p
    }

    // =======================================================
    // FUNCIONALIDAD PRINCIPAL DEL ALGORITMO VORAZ
    // =======================================================

    @Test
    @DisplayName("Estrategia Voraz - Finca de 5 tablones")
    void testVoraz_5Tablones() {
        // Separar los arreglos (ts, tr, p)
        int n = finca_5t.length / 3;
        int[] ts = new int[n];
        int[] tr = new int[n];
        int[] p  = new int[n];
        for (int i = 0; i < n; i++) {
            ts[i] = finca_5t[i * 3];
            tr[i] = finca_5t[i * 3 + 1];
            p[i]  = finca_5t[i * 3 + 2];
        }

        // Ejecutar algoritmo y medir tiempo
        long inicio = System.nanoTime();
        Solution sol = EstrategiaVoraz.roV(ts, tr, p, n);
        long fin = System.nanoTime();
        double duracion = (fin - inicio) / 1_000_000.0;

        //Validaciones básicas
        assertNotNull(sol, "La solución no debe ser nula");
        assertEquals(5, sol.getProgramacionOptima().length, "Debe contener los 5 tablones");
        assertTrue(sol.getCrfTotal() >= 0, "El CRFΠ no puede ser negativo");

        
        // Ejecuta una vez el test e imprime los resultados en consola para verlos.
        assertEquals(27, sol.getCrfTotal(), "CRFΠ esperado incorrecto"); 
        assertArrayEquals(new int[]{1, 2, 4, 0, 3}, sol.getProgramacionOptima());


        //Impresión útil para el informe
        System.out.printf("Voraz (5 tablones): CRFΠ=%d | Orden=%s | Tiempo=%.4f ms%n",
                sol.getCrfTotal(), Arrays.toString(sol.getProgramacionOptima()), duracion);
    }

    @Test
    @DisplayName("Estrategia Voraz - Finca 12 tablones")
    void testVoraz_12Tablones() {
        int n = finca_12t.length / 3;
        int[] ts = new int[n];
        int[] tr = new int[n];
        int[] p  = new int[n];
        for (int i = 0; i < n; i++) {
            ts[i] = finca_12t[i * 3];
            tr[i] = finca_12t[i * 3 + 1];
            p[i]  = finca_12t[i * 3 + 2];
        }

        long inicio = System.nanoTime();
        Solution sol = EstrategiaVoraz.roV(ts, tr, p, n);
        long fin = System.nanoTime();
        double duracion = (fin - inicio) / 1_000_000.0;

        assertNotNull(sol);
        assertEquals(12, sol.getProgramacionOptima().length);
        assertTrue(sol.getCrfTotal() >= 0);

        
        assertEquals(331, sol.getCrfTotal());
        assertArrayEquals(new int[]{2, 3, 6, 8, 10, 7, 5, 11, 1, 9, 0, 4}, sol.getProgramacionOptima());


        System.out.printf("Voraz (12 tablones): CRFΠ=%d | Orden=%s | Tiempo=%.4f ms%n",
                sol.getCrfTotal(), Arrays.toString(sol.getProgramacionOptima()), duracion);
    }


    @Test
    @DisplayName("Estrategia Voraz - Finca 15 tablones")
    void testVoraz_15Tablones() {
        int n = finca_15t.length / 3;
        int[] ts = new int[n];
        int[] tr = new int[n];
        int[] p  = new int[n];
        for (int i = 0; i < n; i++) {
            ts[i] = finca_15t[i * 3];
            tr[i] = finca_15t[i * 3 + 1];
            p[i]  = finca_15t[i * 3 + 2];
        }

        long inicio = System.nanoTime();
        Solution sol = EstrategiaVoraz.roV(ts, tr, p, n);
        long fin = System.nanoTime();
        double duracion = (fin - inicio) / 1_000_000.0;

        assertNotNull(sol);
        assertEquals(15, sol.getProgramacionOptima().length);
        assertTrue(sol.getCrfTotal() >= 0);

        
        assertEquals(568, sol.getCrfTotal());
        assertArrayEquals(new int[]{1, 12, 10, 2, 7, 3, 14, 5, 6, 9, 0, 4, 8, 11, 13}, sol.getProgramacionOptima());


        System.out.printf("Voraz (15 tablones): CRFΠ=%d | Orden=%s | Tiempo=%.4f ms%n",
                sol.getCrfTotal(), Arrays.toString(sol.getProgramacionOptima()), duracion);
    }


    @Test
    @DisplayName("Estrategia Voraz - Finca 18 tablones")
    void testVoraz_18Tablones() {
        int n = finca_18t.length / 3;
        int[] ts = new int[n];
        int[] tr = new int[n];
        int[] p  = new int[n];
        for (int i = 0; i < n; i++) {
            ts[i] = finca_18t[i * 3];
            tr[i] = finca_18t[i * 3 + 1];
            p[i]  = finca_18t[i * 3 + 2];
        }

        long inicio = System.nanoTime();
        Solution sol = EstrategiaVoraz.roV(ts, tr, p, n);
        long fin = System.nanoTime();
        double duracion = (fin - inicio) / 1_000_000.0;

        assertNotNull(sol);
        assertEquals(18, sol.getProgramacionOptima().length);
        assertTrue(sol.getCrfTotal() >= 0);

        
        assertEquals(1124, sol.getCrfTotal());
        assertArrayEquals(new int[]{9, 15, 4, 1, 10, 2, 16, 8, 13, 11, 3, 17, 7, 6, 0, 5, 12, 14}, sol.getProgramacionOptima());


        System.out.printf("Voraz (18 tablones): CRFΠ=%d | Orden=%s | Tiempo=%.4f ms%n",
                sol.getCrfTotal(), Arrays.toString(sol.getProgramacionOptima()), duracion);
        }


    // =======================================================
    // VALIDACIÓN DE HEURÍSTICA (ts / (p * tr))
    // =======================================================

    @Test
    @DisplayName("Comparar heurística ts / (p * tr) mantiene el orden correcto")
    void testHeuristicaOrden() {
        int[] ts = {10, 20, 30};
        int[] tr = {2, 5, 10};
        int[] p  = {1, 2, 3};

        // Ejecutar la estrategia voraz
        Solution sol = EstrategiaVoraz.roV(ts, tr, p, 3);
        int[] orden = sol.getProgramacionOptima();

        // Calcular heurísticas manualmente
        double[] heur = new double[3];
        for (int i = 0; i < 3; i++) heur[i] = (double) ts[i] / (p[i] * tr[i]);

        // Comprobar que la lista esté ordenada ascendentemente
        for (int i = 1; i < orden.length; i++) {
            assertTrue(heur[orden[i-1]] <= heur[orden[i]]);
        }
    }

    // =======================================================
    // VALIDACIÓN DE COMPLETITUD
    // =======================================================

    @Test
    @DisplayName("Verificar que todos los tablones aparecen en la programación")
    void testProgramacionCompleta() {
        int n = finca_10t.length / 3;
        int[] ts = new int[n];
        int[] tr = new int[n];
        int[] p  = new int[n];
        for (int i = 0; i < n; i++) {
            ts[i] = finca_10t[i*3];
            tr[i] = finca_10t[i*3+1];
            p[i]  = finca_10t[i*3+2];
        }

        // Ejecutar algoritmo
        Solution sol = EstrategiaVoraz.roV(ts, tr, p, n);

        // Validar que todos los tablones estén presentes una sola vez
        HashSet<Integer> set = new HashSet<>();
        for (int i : sol.getProgramacionOptima()) set.add(i);
        assertEquals(n, set.size());
    }

    // =======================================================
    // VERIFICACIÓN FORMAL DEL CRFΠ
    // =======================================================

    @Test
    @DisplayName("Validar CRFΠ según definición matemática del documento")
    void testFormulaCRF() {
        // Pequeña finca de ejemplo
        int[] ts = {8,6,5};
        int[] tr = {3,2,4};
        int[] p  = {4,3,1};

        Solution sol = EstrategiaVoraz.roV(ts, tr, p, 3);
        int[] orden = sol.getProgramacionOptima();

        // Calcular CRF manualmente según la fórmula del proyecto
        int tiempo = 0;
        int crfManual = 0;
        for (int i : orden) {
            int finRiego = tiempo + tr[i];
            int retraso = Math.max(0, finRiego - ts[i]);
            int penal = p[i] * retraso;
            crfManual += penal;
            tiempo = finRiego;
        }

        // Comparar con el CRF obtenido por el algoritmo
        assertEquals(crfManual, sol.getCrfTotal());
    }

    // =======================================================
    // PRUEBAS DE ESCALABILIDAD Y RENDIMIENTO
    // =======================================================

    @Test
    @DisplayName("Prueba de rendimiento (10, 100, 1000, 10000, 50000)")
    void testRendimiento() {
    int[] tamanios = {10, 100, 1000, 10000, 50000};
    Random rand = new Random(42);

    StringBuilder reporte = new StringBuilder();
    reporte.append("\n==== RESULTADOS DE PRUEBA DE RENDIMIENTO ====\n");

    for (int n : tamanios) {
        // Generar datos aleatorios simulando distintas fincas
        int[] ts = new int[n];
        int[] tr = new int[n];
        int[] p  = new int[n];
        for (int i = 0; i < n; i++) {
            ts[i] = rand.nextInt(30) + 10;
            tr[i] = rand.nextInt(5) + 1;
            p[i]  = rand.nextInt(4) + 1;
        }

        double totalTiempo = 0.0;
        int crfUltimo = 0;

        for (int rep = 1; rep <= 5; rep++) {
            long inicio = System.nanoTime();
            Solution sol = EstrategiaVoraz.roV(ts, tr, p, n);
            long fin = System.nanoTime();
            double tiempo = (fin - inicio) / 1_000_000.0;

            assertNotNull(sol);
            assertEquals(n, sol.getProgramacionOptima().length);
            assertTrue(sol.getCrfTotal() >= 0);

            totalTiempo += tiempo;
            crfUltimo = sol.getCrfTotal();

            // Guarda la info en el reporte (no imprime aún)
            reporte.append(String.format(
                    "Repetición %d | Tamaño %-6d | Tiempo: %.4f ms | CRFΠ=%d%n",
                    rep, n, tiempo, sol.getCrfTotal()));
        }

        double promedio = totalTiempo / 5.0;
        reporte.append(String.format(">> Promedio tamaño %-6d: %.4f ms%n%n", n, promedio));
    }

        // Imprime todo al final
        System.out.println(reporte.toString());
    }
}
