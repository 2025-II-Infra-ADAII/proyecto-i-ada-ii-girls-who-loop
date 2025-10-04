package proyecto1;

public class Main {

    public static void main(String[] args) {
        
        int n = 3;
        // finca representada con los valores de cada tablon contiguos, los 3 primeros valores representan ts, tr, p
        int[] farm = new int[n*3];
    }

    public static int survivalValue(int[] farm, int indexTablon) {
        return farm[indexTablon*3];
    }

    public static int wateredValue(int[] farm, int indexTablon) {
        return farm[indexTablon*3 + 1];
    }

    public static int priorityValue(int[] farm, int indexTablon){
        return farm[indexTablon*3 + 2];
    }
}