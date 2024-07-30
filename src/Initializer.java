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

        Thread computationThread = new Thread(() -> {
            Solver solver = new Solver();
            solver.solve(instance);
        });

        Thread inputThread = new Thread(() -> {
            try {
                checkForKeyPressAndTimeLimit(computationThread);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        inputThread.start();
        computationThread.start();

        while (computationThread.isAlive()) {
            continue;
        }
        endTime = System.nanoTime();
        instance.setExecutionTime((double) (endTime - startTime) / NANO_TO_MILLI_RATE);
        try {
            writer.writeOut(instance, false);
        } catch (IOException e) {
            e.printStackTrace();
        }
        finished = true;
        return;
    }

    private void checkForKeyPressAndTimeLimit(Thread computationThread) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("\nProgram is computing instance \"" + instance.getInstanceName() + "\"...\nPress 'q' then 'Enter' to stop the program.");
        while (!finished) {
            endTime = System.nanoTime();
            long time = (endTime - startTime);
            if (timeLimit){
                if (time / NANO_TO_SECONDS_RATE >= MAX_TIME_IN_SECONDS) {
                    System.out.println("Max time reached, stopping computation...");
                    instance.setExecutionTime((double) time / NANO_TO_MILLI_RATE);
                    writer.writeOut(instance, true);
                    System.exit(130);
                    break;
                }
            }
            if (reader.ready()) {
                int input = reader.read();
                if (input == 'q') {
                    computationThread.interrupt();
                    endTime = System.nanoTime();
                    System.out.println("Stopping computation...");
                    instance.setExecutionTime((double) time / NANO_TO_MILLI_RATE);
                    writer.writeOut(instance, true);
                    System.exit(130);
                    break;
                }
            }
        }
    }
}