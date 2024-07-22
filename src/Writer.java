import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Writer
{
    private static final String resultPath = "results/";
    private static BufferedWriter writer;

    public static void setUp(String name){
        String fileName = name.substring(0, name.lastIndexOf(".")) + ".mhs";
        try {
             writer = new BufferedWriter(new FileWriter(resultPath + fileName));
        }catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static void writeOut(Instance instance, boolean interrupted) throws IOException
    {
        writeSummary(instance);
        writeSolution(instance);
        writer.flush();
    }

    private static void writeSummary (Instance instance, boolean interrupted) throws IOException
    {
        writer.write(";;; Matrix size: "+instance.getN().getFirst().size()+" x "+instance.getN().size());
        writer.newLine();
        writer.write(";;; |M'|: "+instance.getM1().size());
        writer.newLine();
        writer.write(";;; Suppressed columns: "+instance.emptyColumnsToString());
        writer.newLine();
        writer.write(";;; Solutions found: "+instance.getSolutions().size());
        writer.newLine();
        writer.write(";;; Solution cardinality: "+instance.calcMinMaxCard());
        writer.newLine();
        writer.write(";;; Number of generated Hypothesis per level: " + instance.perLevelHypotesisToString());
        writer.newLine();

        if (interrupted) {
            writer.write(";;; Cardinality reached during exploration: " + Main.maxCardExplored);
            writer.newLine();
        }
    }

    private static void writeSolution(Instance instance) throws IOException
    {
        writer.write(instance.solutionToString());
    }

    public static void write(String message) throws IOException
    {
        writer.write(";;; ErrorMessage: " + message);
        writer.flush();
    }
}
