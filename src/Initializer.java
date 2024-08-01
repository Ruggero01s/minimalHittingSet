import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Initializer {
    private String fileName;
    private String basePath;
    private Instance instance;
    boolean finished = false;
    Writer writer;
    long startTime;
    long endTime;
    long MAX_TIME_IN_SECONDS;
    boolean timeLimit;

    public static final double NANO_TO_MILLI_RATE = 1e6;
    public static final double NANO_TO_SECONDS_RATE = 1e9;


    public Initializer(String basePath, String fileName, long MAX_TIME) {
        this.basePath = basePath;
        this.fileName = fileName;
        writer = new Writer(fileName);
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
        Thread computationThread = new Thread(() -> {
            try {
                Solver solver = new Solver();
                solver.solve(instance);
            }
            catch (Exception e)
            {
                System.out.println("An Exception occurred during execution: " + e.getMessage());
                System.out.println("Printing partial results to file");
                try
                {
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

        inputThread.start();
        computationThread.start();


        // Wait for computation to finish
        while (computationThread.isAlive()) {
            continue;
        }
        endTime = System.nanoTime();
        instance.setExecutionTime((double) (endTime - startTime) / NANO_TO_MILLI_RATE);

        // Write output
        try {
            writer.writeOut(instance, false);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // To stop the other thread
        finished = true;
    }

    private void checkForKeyPressAndTimeLimit(Thread computationThread) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("\nProgram is computing instance \"" + instance.getInstanceName() + "\"...\nPress 'q' then 'Enter' to stop the program.");
        while (!finished) {
            endTime = System.nanoTime();
            long time = (endTime - startTime);
            if (timeLimit){
                if (time / NANO_TO_SECONDS_RATE >= MAX_TIME_IN_SECONDS) {
                    instance.setExecutionTime((double) time / NANO_TO_MILLI_RATE);
                    System.out.println("Max time reached, stopping computation...");
                    writer.writeOut(instance, true);
                    System.exit(130);
                    break;
                }
            }
            if (reader.ready()) {
                int input = reader.read();
                if (input == 'q') {
                    computationThread.interrupt();
                    instance.setExecutionTime((double) time / NANO_TO_MILLI_RATE);
                    System.out.println("Stopping computation...");
                    writer.writeOut(instance, true);
                    System.exit(130);
                    break;
                }
            }
        }
    }
}