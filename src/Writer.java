import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class Writer
{
    private static final String resultPath = "results/";
    private String fileName;
    private BufferedWriter writer;


    public Writer(String fileName){
        fileName = fileName.substring(0,fileName.lastIndexOf(".")) + ".mhs";
        try {
             writer = new BufferedWriter(new FileWriter(resultPath + fileName));
        }catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void writeOut(Instance instance) throws IOException
    {
        writeSummary(instance);
        writeSolution(instance);
        writer.flush();
    }

    private void writeSummary (Instance instance) throws IOException
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
    }

    private void writeSolution(Instance instance) throws IOException
    {
        writer.write(instance.solutionToString());
    }

    public void write(String message) throws IOException {
        writer.write(";;; ErrorMessage: " + message);
        writer.flush();
    }
}
