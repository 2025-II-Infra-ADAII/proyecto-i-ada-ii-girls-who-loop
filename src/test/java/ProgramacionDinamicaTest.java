import org.junit.jupiter.api.*;

import proyecto1.ProgramacionDinamica;
import proyecto1.Solution;

import java.util.HashSet;
import static org.junit.jupiter.api.Assertions.*;

class ProgramacionDinamicaTest {

    private int[] finca_5t;
    private int[] finca_10t;
    private int[] finca_12t;
    private int[] finca_15t;
    private int[] finca_18t;

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
            16, 5, 1,
            12, 3, 2,
            20, 7, 4,
            8,  2, 4,
            14, 4, 1,
            10, 3, 2,
            18, 6, 3,
            9,  2, 4,
            15, 5, 3,
            11, 4, 1,
            13, 3, 4,
            7,  2, 2 
        };
        finca_15t = new int[]{
            18, 6, 1,
            12, 4, 4,
            22, 8, 3,
            9,  2, 4,
            15, 5, 1,
            11, 3, 3,
            20, 7, 2,
            8,  2, 4,
            16, 5, 1,
            13, 4, 2,
            10, 3, 4,
            14, 4, 1,
            19, 6, 4,
            7,  2, 1,
            17, 5, 3
        };
        finca_18t = new int[]{
            20, 7, 1,
            14, 4, 4,
            25, 9, 3,
            10, 3, 3,
            17, 5, 4,
            12, 4, 1,
            22, 8, 1,
            9,  2, 2,
            18, 6, 3,
            15, 5, 4,
            11, 3, 4,
            16, 5, 3,
            21, 7, 1,
            8,  2, 4,
            19, 6, 1,
            13, 4, 4,
            23, 8, 3,
            7,  2, 3 
        };
    }

    @AfterEach
    void tearDown() {
        ProgramacionDinamica.memo.clear();
    }

    // ========== PRUEBAS DE FUNCIONES DE ACCESO ==========

    @Test
    @DisplayName("Obtener valor de supervivencia del tablón")
    void testSurvivalValue() {
        assertEquals(12, ProgramacionDinamica.survivalValue(finca_10t, 0));
        assertEquals(12, ProgramacionDinamica.survivalValue(finca_15t, 1));
        assertEquals(25, ProgramacionDinamica.survivalValue(finca_18t, 2));
    }

    @Test
    @DisplayName("Obtener valor de riego del tablón")
    void testWateredValue() {
        assertEquals(1, ProgramacionDinamica.wateredValue(finca_5t, 3));
        assertEquals(4, ProgramacionDinamica.wateredValue(finca_12t, 4));
        assertEquals(4, ProgramacionDinamica.wateredValue(finca_18t, 5));
    }

    @Test
    @DisplayName("Obtener valor de prioridad del tablón")
    void testPriorityValue() {
        assertEquals(2, ProgramacionDinamica.priorityValue(finca_10t, 6));
        assertEquals(4, ProgramacionDinamica.priorityValue(finca_12t, 7));
        assertEquals(1, ProgramacionDinamica.priorityValue(finca_15t, 8));
    }

    // ========== PRUEBAS DE CÁLCULO CRF ==========

    @Test
    @DisplayName("Calcular CRF sin penalización")
    void testCalculoCRF_SinPenalizacion() {
        int crf = ProgramacionDinamica.calculoCRF(finca_18t, 0, 0);
        assertEquals(0, crf);
    }

    @Test
    @DisplayName("Calcular CRF con penalización")
    void testCalculoCRF_ConPenalizacion() {
        int crf = ProgramacionDinamica.calculoCRF(finca_15t, 12, 8);
        assertEquals(1, crf);
    }

    @Test
    @DisplayName("Calcular CRF con diferentes tiempos de inicio")
    void testCalculoCRF_DiferentesTiempos() {
        
        assertEquals(3, ProgramacionDinamica.calculoCRF(finca_5t, 3, 4));
        
        assertEquals(4, ProgramacionDinamica.calculoCRF(finca_10t, 8, 8));
        
        assertEquals(10, ProgramacionDinamica.calculoCRF(finca_12t, 10, 11));
    }

    // ========== PRUEBAS DE VALIDACIÓN ==========

    @Test
    @DisplayName("Rechazar finca nula")
    void testRoPD_FincaNula() {
        Exception exception = assertThrows(
            IllegalArgumentException.class,
            () -> ProgramacionDinamica.roPD(null)
        );
        assertTrue(exception.getMessage().contains("invalido"));
    }

    @Test
    @DisplayName("Rechazar finca con formato inválido")
    void testRoPD_FormatoInvalido() {
        int[] fincaInvalida = new int[]{1, 2, 3, 4}; 
        
        Exception exception = assertThrows(
            IllegalArgumentException.class,
            () -> ProgramacionDinamica.roPD(fincaInvalida)
        );
        assertTrue(exception.getMessage().contains("multiplo de 3"));
    }

    @Test
    @DisplayName("Aceptar finca vacía")
    void testRoPD_FincaVacia() {
        int[] fincaVacia = new int[]{};
        Solution solution = ProgramacionDinamica.roPD(fincaVacia);
        
        assertNotNull(solution);
        assertEquals(0, solution.getCrfTotal());
        assertEquals(0, solution.getProgramacionOptima().length);
    }

    // ========== PRUEBAS DE SOLUCIÓN ÓPTIMA ==========

    @Test
    @DisplayName("Resolver finca con 5 tablones")
    void testRoPD_Finca5T() {
        Solution solution = ProgramacionDinamica.roPD(finca_5t);
        
        assertNotNull(solution);
        assertNotNull(solution.getCrfTotal());
        assertEquals(5, solution.getProgramacionOptima().length);
        assertEquals(18, solution.getCrfTotal());
        assertArrayEquals(new int[] {2,1,3,0,4}, solution.getProgramacionOptima());
    }

    @Test
    @DisplayName("Resolver finca con 10 tablones")
    void testRoPD_Finca10T() {
        Solution solution = ProgramacionDinamica.roPD(finca_10t);
        System.out.println(solution);
        assertNotNull(solution);
        assertNotNull(solution.getCrfTotal());
        assertEquals(10, solution.getProgramacionOptima().length);
        assertEquals(114, solution.getCrfTotal());
        assertArrayEquals(new int[] {3,6,1,0,7,5,2,9,4,8}, solution.getProgramacionOptima());
    }

    @Test
    @DisplayName("Resolver finca con 12 tablones")
    void testRoPD_Finca12T() {
        Solution solution = ProgramacionDinamica.roPD(finca_12t);
        
        assertNotNull(solution);
        assertNotNull(solution.getCrfTotal());
        assertEquals(177, solution.getCrfTotal());
        assertArrayEquals(new int[] {3, 5, 11, 7, 10, 1, 8, 2, 6, 4, 9, 0}, solution.getProgramacionOptima());
    }

    @Test
    @DisplayName("Resolver finca con 15 tablones")
    void testRoPD_Finca15T() {
        Solution solution = ProgramacionDinamica.roPD(finca_15t);
        
        assertNotNull(solution);
        assertNotNull(solution.getCrfTotal());
        assertEquals(363, solution.getCrfTotal());
        assertArrayEquals(new int[] {3, 5, 7, 10, 1, 12, 14, 9, 13, 2, 6, 11, 4, 8, 0}, solution.getProgramacionOptima());
    }

    @Test
    @DisplayName("Resolver finca con 18 tablones")
    void testRoPD_Finca18T() {
        Solution solution = ProgramacionDinamica.roPD(finca_18t);
        
        assertNotNull(solution);
        assertNotNull(solution.getCrfTotal());
        assertEquals(718, solution.getCrfTotal());
        assertArrayEquals(new int[] {3, 13, 17, 7, 10, 1, 15, 4, 9, 11, 8, 16, 2, 5, 14, 0, 12, 6}, solution.getProgramacionOptima());
    }

    @Test
    @DisplayName("La programación contiene todos los tablones")
    void testRoPD_TodosLosTablones() {
        Solution solution = ProgramacionDinamica.roPD(finca_15t);
        
        HashSet<Integer> tablonesEnProgramacion = new HashSet<>();
        for (int tablon : solution.getProgramacionOptima()) {
            tablonesEnProgramacion.add(tablon);
        }
        
        assertEquals(15, tablonesEnProgramacion.size());
        assertTrue(tablonesEnProgramacion.contains(0));
        assertTrue(tablonesEnProgramacion.contains(1));
        assertTrue(tablonesEnProgramacion.contains(2));
    }

    @Test
    @DisplayName("Verificar que el costo calculado coincide con la programación")
    void testRoPD_CostoCoincideConProgramacion() {
        Solution solution = ProgramacionDinamica.roPD(finca_18t);
        
        int costoCalculado = ProgramacionDinamica.calcularCostoTotal(
            finca_18t, 
            solution.getProgramacionOptima()
        );
        
        assertEquals(solution.getCrfTotal(), costoCalculado);
    }

    @Test
    @DisplayName("Calcular costo total de una programación específica")
    void testCalcularCostoTotal() {
        int[] programacion = new int[]{3, 8};
        int costoTotal = ProgramacionDinamica.calcularCostoTotal(finca_12t, programacion);
        assertEquals(0, costoTotal);
    }

    @Test
    @DisplayName("Calcular costo total de programación inversa")
    void testCalcularCostoTotal_OrdenInverso() {
        int[] programacion = new int[]{3, 8}; 
        int costoTotal = ProgramacionDinamica.calcularCostoTotal(finca_12t, programacion);
        assertEquals(0, costoTotal);
    }

    // ========== PRUEBAS DE RESOLVER DP ==========

    @Test
    @DisplayName("Resolver DP con conjunto vacío devuelve costo 0")
    void testResolverDP_ConjuntoVacio() {
        HashSet<Integer> vacio = new HashSet<>();
        ProgramacionDinamica.Resultado resultado = 
            ProgramacionDinamica.resolverDP(finca_10t, 0, vacio);
        
        assertEquals(0, resultado.costo);
        assertEquals(-1, resultado.primerTablon);
    }

    @Test
    @DisplayName("Resolver DP con un solo tablón")
    void testResolverDP_UnTablon() {
        HashSet<Integer> unTablon = new HashSet<>();
        unTablon.add(8);
        
        ProgramacionDinamica.Resultado resultado = 
            ProgramacionDinamica.resolverDP(finca_18t, 0, unTablon);
        
        assertEquals(0, resultado.costo); 
        assertEquals(8, resultado.primerTablon);
    }

    @Test
    @DisplayName("Memoización funciona correctamente")
    void testMemoizacion() {
        HashSet<Integer> tablones = new HashSet<>();
        tablones.add(0);
        tablones.add(1);
        
        ProgramacionDinamica.resolverDP(finca_12t, 0, tablones);
        int tamañoMemo1 = ProgramacionDinamica.memo.size();
        
        ProgramacionDinamica.resolverDP(finca_12t, 0, tablones);
        int tamañoMemo2 = ProgramacionDinamica.memo.size();
        
        assertEquals(tamañoMemo1, tamañoMemo2);
        assertTrue(ProgramacionDinamica.memo.size() > 0);
    }


    // ========== PRUEBAS DE CLASES INTERNAS ==========

    @Test
    @DisplayName("Estado se compara correctamente por equals")
    void testEstado_Equals() {
        HashSet<Integer> set1 = new HashSet<>();
        set1.add(0);
        set1.add(1);
        set1.add(2);
        
        HashSet<Integer> set2 = new HashSet<>();
        set2.add(0);
        set2.add(1);
        set2.add(2);
        
        ProgramacionDinamica.Estado estado1 = 
            new ProgramacionDinamica.Estado(set1, 8);
        ProgramacionDinamica.Estado estado2 = 
            new ProgramacionDinamica.Estado(set2, 8);
        
        assertEquals(estado1, estado2);
        assertEquals(estado1.hashCode(), estado2.hashCode());
    }

    @Test
    @DisplayName("Estados diferentes no son iguales")
    void testEstado_NoEquals() {
        HashSet<Integer> set1 = new HashSet<>();
        set1.add(0);
        
        HashSet<Integer> set2 = new HashSet<>();
        set2.add(1);
        
        ProgramacionDinamica.Estado estado1 = 
            new ProgramacionDinamica.Estado(set1, 5);
        ProgramacionDinamica.Estado estado2 = 
            new ProgramacionDinamica.Estado(set2, 5);
        
        assertNotEquals(estado1, estado2);
    }

    // ========== PRUEBAS DE CASOS EXTREMOS ==========

    @Test
    @DisplayName("Finca con un solo tablón")
    void testRoPD_UnSoloTablon() {
        int[] fincaUnitaria = new int[]{10, 5, 3};
        Solution solution = ProgramacionDinamica.roPD(fincaUnitaria);
        
        assertEquals(1, solution.getProgramacionOptima().length);
        assertEquals(0, solution.getProgramacionOptima()[0]);
        assertEquals(0, solution.getCrfTotal()); 
    }

    @Test
    @DisplayName("Finca con ts y tr altos")
    void testRoPD_ValoresExtremos() {
        int[] fincaExtrema = new int[]{
            1000, 800, 4,
            2000, 1000, 1,
            5000, 4000, 2
        };
        
        Solution solution = ProgramacionDinamica.roPD(fincaExtrema);
        
        assertNotNull(solution);
        assertEquals(3, solution.getProgramacionOptima().length);
        assertEquals(1600, solution.getCrfTotal());
    }

    @Test
    @DisplayName("Finca con prioridades iguales")
    void testRoPD_PrioridadesIguales() {
        int[] fincaIgual = new int[]{
            5, 2, 4, 
            4, 3, 4,  
            6, 1, 4   
        };
        
        Solution solution = ProgramacionDinamica.roPD(fincaIgual);
        
        assertNotNull(solution);
        assertEquals(3, solution.getProgramacionOptima().length);
    }
    
}