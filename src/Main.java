import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {

    static final String filename = "74L85.024.matrix";

    private static final String benchmarksPath = "benchmarks/benchmarks1/";

    public static void main(String[] args) {
        Instance instance = null;
        try {
            Writer.setUp(filename);
            instance = Reader.readInstance(benchmarksPath+filename);
            Solver solver = new Solver();
            instance.setSolutions(new ArrayList<>(solver.solve(instance)));
            Writer.writeOut(instance,false);
            System.out.println(solver.all.contains(new Hypothesis(new ArrayList<>(List.of(0,0,0,0,0,0,0,0,0,0,0,0,1,1,1)))));
        } catch (IOException e)
        {
            try {
                Writer.write(e.getMessage());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    /*public static void main(String[] args) {
        Instance instance = null;
        File dir = new File(benchmarksPath);
        File[] files = dir.listFiles();
        for (File file : files) {
            try {
                Writer.setUp(file.getName());
                instance = Reader.readInstance(benchmarksPath + file.getName());
                Solver solver = new Solver();
                instance.setSolutions(new ArrayList<>(solver.solve(instance)));
                Writer.writeOut(instance, false);
            } catch (IOException e) {
                try {
                    Writer.write(e.getMessage());
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }

    }*/

}