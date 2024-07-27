import java.util.ArrayList;
import java.util.List;

public class Main {
//
//    private static final String fileName = "74L85.000.matrix";
//    private static final String basePath ="benchmarks/benchmarks1/";
    private static final String fileName = "74L85.000.1.matrix";
    private static final String basePath ="permutations/";

    public static void main(String[] args)
    {
        Initializer initializer = new Initializer(basePath, fileName);
        initializer.start();
    }
}


//todo check soluzioni delle permutazioni
//todo fare programmino per le performance spaziali e temporali, ruba le righe della matrice e delle velocità con un parserino e fa un grafico