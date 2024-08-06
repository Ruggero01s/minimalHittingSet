import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Initializer {
    private String fileName;
    private String basePath;
    private Instance instance;
    private Solver solver;
    private int numThreads;
    boolean finished = false;
    boolean partial = false;
    Writer writer;
    long startTime;
    long endTime;
    long MAX_TIME_IN_SECONDS;
    boolean timeLimit;

    public static final double NANO_TO_MILLI_RATE = 1e6;
    public static final double NANO_TO_SECONDS_RATE = 1e9;


    public Initializer(String basePath, String fileName, long MAX_TIME, int numThreads) {
        this.basePath = basePath;
        this.fileName = fileName;
        writer = new Writer(fileName);
        this.numThreads = numThreads;
        if (MAX_TIME == -1)
            timeLimit = false;
        else {
            timeLimit = true;
            MAX_TIME_IN_SECONDS = MAX_TIME;}
    }

    public void start() {
        try {
            startTime = System.nanoTime();
            instance = Reader.readInstance(basePath, fileName);
        } catch (IOException e) {
            try {
                writer.write(";;; Error reading file (" + basePath + fileName + "): " + e.getMessage());
            } catch (IOException ex) {
                e.printStackTrace();
            }
        }


        // initializing the two threads
        //computation thread
        solver = new Solver();
        Thread computationThread = new Thread(() -> {
            try {
                solver.solve(instance, numThreads);
            }
            catch (Exception e)
            {
                System.out.println("An Exception occurred during execution: " + e.getMessage());
                System.out.println("Printing partial results to file");
                try
                {
                    partial = true;
                    writer.writeOut(instance,true);
                }
                catch (IOException ex)
                {
                    e.printStackTrace();
                }
            }


        });

        //thread for interrupting by input and time limit
        Thread inputThread = new Thread(() -> {
            try {
                checkForKeyPressAndTimeLimit(computationThread);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        System.out.println("\nProgram is computing instance \"" + instance.getInstanceName()+"\"");
        inputThread.start();
        computationThread.start();

        // Wait for computation to finish
        while (computationThread.isAlive()) {
            //continue;
        }
        endTime = System.nanoTime();
        instance.setExecutionTime((double) (endTime - startTime) / NANO_TO_MILLI_RATE);

        if (!partial) {
            // Write output
            try {
                writer.writeOut(instance, false);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // To stop the other thread
        finished = true;
    }

    private void checkForKeyPressAndTimeLimit(Thread computationThread) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        while (!finished) {
            endTime = System.nanoTime();
            long time = (endTime - startTime);
            if (timeLimit){
                if (time / NANO_TO_SECONDS_RATE >= MAX_TIME_IN_SECONDS) {
                    solver.interrupt();
                    System.out.println("Max time reached, stopping computation...");
                    break;
                }
            }
            if (reader.ready()) {
                int input = reader.read();
                if (input == 'q') {
                    solver.interrupt();
                    System.out.println("Stopping computation...");
                    while (computationThread.isAlive()) {
                        //continue;
                    }
                    System.exit(130);
                    break;
                }
            }
        }
    }
}