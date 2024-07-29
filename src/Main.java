import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;

public class Main {

    private static final String fileName = "74L85.000.1.matrix";
    private static final String basePath ="benchmarks/currentBenchmarks";

//    public static void main(String[] args)
//    {
//        ExecutorService executorService = Executors.newFixedThreadPool(16);
//
//        // Create a File object for the directory
//        File directory = new File(basePath);
//
//        // Check if the directory exists and is indeed a directory
//        if (directory.exists() && directory.isDirectory()) {
//            // Get all the files in the directory
//            File[] filesList = directory.listFiles();
//            if (filesList != null) {
//                for (File file : filesList) {
//                    Initializer initializer = new Initializer(basePath+"/", file.getName());
//                    executorService.submit(initializer::start);
//                }
//            }
//        }
//        executorService.shutdown();
//        try {
//            executorService.awaitTermination(10, TimeUnit.SECONDS);
//            System.exit(0);
//        } catch (InterruptedException e) {
//            System.exit(131);
//        }
//    }

    public static void main(String[] args)
    {
        // Create a File object for the directory
        File directory = new File(basePath);

        // Check if the directory exists and is indeed a directory
        if (directory.exists() && directory.isDirectory()) {
            // Get all the files in the directory
            File[] filesList = directory.listFiles();
            if (filesList != null) {
                for (File file : filesList) {
                    Initializer initializer = new Initializer(basePath+"/", file.getName());
                    initializer.start();
                }
            }
        }
        System.exit(0);
    }
}


//todo check soluzioni delle permutazioni
//todo fare programmino per le performance spaziali e temporali, ruba le righe della matrice e delle velocità con un parserino e fa un grafico