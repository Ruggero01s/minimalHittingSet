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
        writeSummary(instance, interrupted);
        writeSolution(instance);
        writer.flush();
    }

    private static void writeSummary (Instance instance, boolean interrupted) throws IOException
    {
        if (interrupted)
        {
            writer.write(";;; The solution was interrupted by the user.");
            writer.newLine();
        }
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
        writer.write(";;; Number of generated Hypothesis per level: " + instance.perLevelHypotesisToString(interrupted));
        writer.newLine();
        writer.write(";;; Time taken at each level: " + instance.perLevelTimeToString());
        writer.newLine();
        writer.write(";;; Maximum spatial occupation: " + instance.getSpatialPerformance()/1000+ " KB");
        writer.newLine();
        if(!interrupted)
        {
            writer.write(";;; Time elapsed during computation: " + instance.getTemporalPerformance()/1000.0+ " seconds");
            writer.newLine();
        }
        if (interrupted)
        {
            writer.write(";;; Cardinality reached during exploration: " + instance.getMaxCardExplored());
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
