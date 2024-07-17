import java.util.*;

public class Main {

    static Hypothesis h0;
    static Instance instance;

    public static void main(String[] args) {
        Reader r = new Reader();
        instance = r.readInstance("benchmarks/benchmarks1/74L85.000.matrix");

        List<Hypothesis> result = calculateMHS();


        //writeOut(i, result);

        System.out.println(instance);
    }
}