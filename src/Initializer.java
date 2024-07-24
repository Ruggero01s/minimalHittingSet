import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Initializer {
    private String fileName;
    private String basePath;
    private Instance instance;

    public Initializer(String basePath, String fileName) {
        this.basePath = basePath;
        this.fileName = fileName;
    }

    public void start() {
        try {
            instance = Reader.readInstance(basePath, fileName);
        } catch (IOException e) {
            try {
                Writer.write(";;; Error reading file ("+ basePath + fileName+"): "+ e.getMessage());
            } catch (IOException ex) {
                e.printStackTrace();
            }
        }

        Thread computationThread = new Thread(() -> {
            Writer.setUp(fileName);
            Solver solver = new Solver();
            instance.setSolutions(new ArrayList<>(solver.solve(instance)));
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
            Writer.writeOut(instance, false);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.exit(0);
    }

    private void checkForKeyPress(Thread computationThread) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("\nProgram is Running...\nPress 'q' then 'Enter' to stop the program.");
        while (true) {
            if (reader.ready()) {
                int input = reader.read();
                if (input == 'q') {
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