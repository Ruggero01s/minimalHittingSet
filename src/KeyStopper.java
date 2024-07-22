import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class KeyStopper
{
/*
    public void start()
    {
        Thread computationThread = new Thread(() -> {
            try {
                Main.startComputation();
            } catch (IOException e) {
                try {
                    Writer.write(e.getMessage());
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
        computationThread.start();
        while (computationThread.isAlive())
        {
            continue;
        }
        try {
            System.out.println(computationThread.isAlive());
            Writer.writeOut(Main.instance, false);
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
                    Writer.writeOut(Main.instance, true);
                    System.out.println("Stopping computation...");
                    System.exit(130);
                    break;
                }
            }
        }
    }*/
}