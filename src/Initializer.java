import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Initializer {
    private String fileName;
    private String basePath;
    private Instance instance;
    boolean finished = false;
    Writer writer;

    public Initializer(String basePath, String fileName) {
        this.basePath = basePath;
        this.fileName = fileName;
        writer = new Writer(fileName);
    }

    public void start() {
        try {
            instance = Reader.readInstance(basePath, fileName);
        } catch (IOException e) {
            try {
                writer.write(";;; Error reading file ("+ basePath + fileName+"): "+ e.getMessage());
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
                checkForKeyPress(computationThread);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        inputThread.start();
        computationThread.start();

        while (computationThread.isAlive()) {
            //continue;
        }

        try {
            writer.writeOut(instance, false);
        } catch (IOException e) {
            e.printStackTrace();
        }
        finished = true;
        return;
    }

    private void checkForKeyPress(Thread computationThread) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("\nProgram is computing instance \""+ instance.getInstanceName() + "\"...\nPress 'q' then 'Enter' to stop the program.");
        while (!finished) {
            if (reader.ready()) {
                int input = reader.read();
                if (input == 'q') {
                    computationThread.interrupt();
                    writer.writeOut(instance, true);
                    System.out.println("Stopping computation...");
                    System.exit(130);
                    break;
                }
            }
        }
    }
}