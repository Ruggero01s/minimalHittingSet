import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {

    private static boolean permuteRows = true;
    private static boolean permuteCols = true;

    public static void main(String[] args)
    {
        KeyStopper keyStopper = new KeyStopper();
        keyStopper.start(permuteRows,permuteCols);
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