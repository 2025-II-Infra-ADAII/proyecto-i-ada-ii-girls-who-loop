import org.junit.jupiter.api.*;

import proyecto1.FuerzaBruta;
import proyecto1.Solution;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para la clase FuerzaBruta.
 * Validan la correccion del calculo del CRF, la generacion de permutaciones
 * y la obtencion de la solucion optima para distintos tamanos de finca.
 */
class FuerzaBrutaTest {

    private int[] finca_5t;
    private int[] finca_6t;
    private int[] finca_7t;
    private int[] finca_8t;
    private int[] finca_9t;

    @BeforeEach
    void setUp() {
        finca_5t = new int[]{
            8,2,3,
            6,4,4,
            1,1,2,
            7,1,1,
            5,3,3
        };

        finca_6t = new int[]{
            10,3,2,
            8,2,4,
            5,1,3,
            12,4,1,
            9,3,3,
            6,2,2
        };

        finca_7t = new int[]{
            10,2,3,
            7,3,4,
            6,1,2,
            12,4,1,
            9,3,3,
            5,2,2,
            8,2,4
        };

        finca_8t = new int[]{
            10,3,2,
            6,2,4,
            8,2,3,
            12,5,1,
            9,3,2,
            5,2,3,
            7,2,4,
            11,4,1
        };

        finca_9t = new int[]{
            10,3,2,
            7,2,3,
            5,1,4,
            8,3,2,
            11,4,1,
            9,2,3,
            6,2,2,
            12,5,1,
            13,4,2
        };
    }

    // ===================== VALIDACION DE ENTRADAS =====================

    @Test
    @DisplayName("Finca nula lanza excepcion")
    void testFincaNula() {
        Exception ex = assertThrows(
            IllegalArgumentException.class,
            () -> FuerzaBruta.roFB(null)
        );
        assertTrue(ex.getMessage().toLowerCase().contains("invalido"));
    }

    @Test
    @DisplayName("Finca con formato invalido lanza excepcion")
    void testFormatoInvalido() {
        int[] fincaInvalida = {1, 2, 3, 4}; // No multiplo de 3
        Exception ex = assertThrows(
            IllegalArgumentException.class,
            () -> FuerzaBruta.roFB(fincaInvalida)
        );
        assertTrue(ex.getMessage().toLowerCase().contains("multiplo"));
    }

    @Test
    @DisplayName("Finca vacia retorna costo 0 y arreglo vacio")
    void testFincaVacia() {
        int[] fincaVacia = {};
        Solution sol = FuerzaBruta.roFB(fincaVacia);

        assertNotNull(sol);
        assertEquals(0, sol.getCrfTotal());
        assertEquals(0, sol.getProgramacionOptima().length);
    }

    // ===================== FUNCIONAMIENTO GENERAL =====================

    @Test
    @DisplayName("Resolver finca con 5 tablones")
    void testFinca5T() {
        Solution sol = FuerzaBruta.roFB(finca_5t);
        assertNotNull(sol);
        assertEquals(5, sol.getProgramacionOptima().length);
        assertEquals(18, sol.getCrfTotal()); // Valor conocido
    }

    @Test
    @DisplayName("Resolver finca con 6 tablones")
    void testFinca6T() {
        Solution sol = FuerzaBruta.roFB(finca_6t);
        assertNotNull(sol);
        assertEquals(6, sol.getProgramacionOptima().length);
        assertTrue(sol.getCrfTotal() >= 0);
    }

    @Test
    @DisplayName("Resolver finca con 7 tablones")
    void testFinca7T() {
        Solution sol = FuerzaBruta.roFB(finca_7t);
        assertNotNull(sol);
        assertEquals(7, sol.getProgramacionOptima().length);
        assertTrue(sol.getCrfTotal() >= 0);
    }

    @Test
    @DisplayName("Resolver finca con 8 tablones")
    void testFinca8T() {
        Solution sol = FuerzaBruta.roFB(finca_8t);
        assertNotNull(sol);
        assertEquals(8, sol.getProgramacionOptima().length);
        assertTrue(sol.getCrfTotal() >= 0);
    }

    @Test
    @DisplayName("Resolver finca con 9 tablones")
    void testFinca9T() {
        Solution sol = FuerzaBruta.roFB(finca_9t);
        assertNotNull(sol);
        assertEquals(9, sol.getProgramacionOptima().length);
        assertTrue(sol.getCrfTotal() >= 0);
    }

    // ===================== CONSISTENCIA =====================

    @Test
    @DisplayName("El costo calculado coincide con el orden optimo encontrado")
    void testCostoCoincide() {
        Solution sol = FuerzaBruta.roFB(finca_5t);
        int[] perm = sol.getProgramacionOptima();

        long time = 0, totalCRF = 0;
        for (int idx : perm) {
            int ts = finca_5t[idx * 3];
            int tr = finca_5t[idx * 3 + 1];
            int p  = finca_5t[idx * 3 + 2];
            time += tr;
            long delay = Math.max(0, time - ts);
            totalCRF += p * delay;
        }

        assertEquals(sol.getCrfTotal(), totalCRF);
    }

    @Test
    @DisplayName("La permutacion no contiene tablones repetidos")
    void testPermutacionSinRepetidos() {
        Solution sol = FuerzaBruta.roFB(finca_6t);
        int[] orden = sol.getProgramacionOptima();
        boolean[] vistos = new boolean[orden.length];

        for (int i : orden) {
            assertFalse(vistos[i], "El tablon " + i + " esta repetido");
            vistos[i] = true;
        }
    }

    @Test
    @DisplayName("Resultados deterministas (mismo input, mismo output)")
    void testDeterminismo() {
        Solution s1 = FuerzaBruta.roFB(finca_7t);
        Solution s2 = FuerzaBruta.roFB(finca_7t);

        assertEquals(s1.getCrfTotal(), s2.getCrfTotal());
        assertArrayEquals(s1.getProgramacionOptima(), s2.getProgramacionOptima());
    }

    // ===================== CASOS EXTREMOS =====================

    @Test
    @DisplayName("Finca con un solo tablon devuelve costo 0")
    void testUnTablon() {
        int[] finca1 = {10, 5, 2};
        Solution sol = FuerzaBruta.roFB(finca1);

        assertEquals(0, sol.getCrfTotal());
        assertEquals(1, sol.getProgramacionOptima().length);
        assertEquals(0, sol.getProgramacionOptima()[0]);
    }

    @Test
    @DisplayName("Finca con prioridades iguales sigue generando resultado valido")
    void testPrioridadesIguales() {
        int[] fincaIgual = {
            5, 2, 4,
            4, 3, 4,
            6, 1, 4
        };
        Solution sol = FuerzaBruta.roFB(fincaIgual);

        assertNotNull(sol);
        assertEquals(3, sol.getProgramacionOptima().length);
    }

    @Test
    @DisplayName("Finca de 3 tablones (caso conocido con CRF optimo 0)")
    void testFinca3TOptimo() {
        int[] finca3 = {
            4, 2, 3,  // ts, tr, p del tablon 0
            5, 1, 2,  // tablon 1
            6, 3, 1   // tablon 2
        };

        Solution sol = FuerzaBruta.roFB(finca3);

        assertNotNull(sol, "La solucion no debe ser nula");
        assertEquals(3, sol.getProgramacionOptima().length, "Debe incluir los 3 tablones");
        assertEquals(0, sol.getCrfTotal(), "El CRF optimo esperado es 0");
    }
}


