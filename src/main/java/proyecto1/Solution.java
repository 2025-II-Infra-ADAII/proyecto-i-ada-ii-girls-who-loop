package proyecto1;
import java.util.Arrays;

public class Solution {

    private int crf_total;
    private int[] programacion_optima;

    public Solution(int crf_total, int[] programacion_optima) {
        this.crf_total = crf_total;
        this.programacion_optima = programacion_optima;
    }

    public int getCrfTotal() {
        return crf_total;
    }

    public int[] getProgramacionOptima() {
        return programacion_optima;
    }

    @Override
    public String toString() {
        return "CRF Total: " + crf_total + "\nProgramación Óptima: " + Arrays.toString(programacion_optima);
    }
}