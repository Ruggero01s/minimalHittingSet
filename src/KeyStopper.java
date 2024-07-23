import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class KeyStopper
{
    static final String filename = "74L85.024.matrix";

    private static final String benchmarksPath = "benchmarks/benchmarks1/";
    Instance instance;

    public void start(boolean permuteRows, boolean permuteCols)
    {
        Thread computationThread = new Thread(() -> {
            try {
                Writer.setUp(filename);
                instance = Reader.readInstance(benchmarksPath+filename);
                Permutator permutator = new Permutator();
                //instance = permutator.permute(instance, permuteRows, permuteCols);
                Solver solver = new Solver();
                instance.setSolutions(new ArrayList<>(solver.solve(instance)));
                //System.out.println(solver.all.contains(new Hypothesis(new ArrayList<>(List.of(0,0,0,0,0,0,0,0,0,0,0,0,1,1,1)))));
            } catch (IOException e) {
                try {
                    Writer.write(e.getMessage());
                    System.exit(1);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        Thread inputThread = new Thread(() -> {
            try {
                checkForKeyPress(computationThread);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        inputThread.start();

        long startTime = System.currentTimeMillis();
        computationThread.start();

        while (computationThread.isAlive())
        {
            continue;
        }
        try {
            long endTime = System.currentTimeMillis();
            //System.out.println(computationThread.isAlive());
            instance.setTemporalPerformance(endTime-startTime);
            Writer.writeOut(instance, false);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.exit(0);
    }

    private void checkForKeyPress(Thread computationThread) throws IOException
    {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("\nProgram is Running...\nPress 'q' then 'Enter' to stop the program.");
        while (true)
        {
            if (reader.ready())
            {
                int input = reader.read();
                if (input == 'q')
                {
                    computationThread.interrupt();
                    Writer.writeOut(instance, true);
                    System.out.println("Stopping computation...");
                    System.exit(130);
                    break;
                }
            }
        }
    }
}