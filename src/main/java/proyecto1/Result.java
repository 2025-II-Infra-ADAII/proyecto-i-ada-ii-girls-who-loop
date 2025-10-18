package proyecto1;

import java.util.List;

public class Result {
    private final List<Integer> orden;
    private final int costo;

    public Result(List<Integer> orden, int costo) {
        this.orden = orden;
        this.costo = costo;
    }

    public List<Integer> getOrden() { return orden; }
    public int getCosto() { return costo; }

    @Override
    public String toString() {
        return "Costo total: " + costo + "\nOrden: " + orden;
    }
}
