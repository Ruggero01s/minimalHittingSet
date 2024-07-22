import java.io.IOException;
import java.util.ArrayList;

public class Main {

    static final String filename = "74L85.024.matrix";

    public static void main(String[] args) {
        Instance instance = null;
        try {
            instance = Reader.readInstance(filename);
            Writer.setUp(filename);
        } catch (IOException e)
        {
            try {
                Writer.write(e.getMessage());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        Solver solver = new Solver();
        instance.setSolutions(new ArrayList<>(solver.solve(instance)));
        try {
            Writer.writeOut(instance,false);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

}