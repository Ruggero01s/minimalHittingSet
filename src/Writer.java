import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class Writer
{
    String resultPath = "results/";

    public void writeOut(String fileName, Instance instance)
    {
        fileName = fileName.substring(0,fileName.lastIndexOf(".")) + ".mhs";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(resultPath + fileName)))
        {
           writeSummary(writer, instance);
           writeSolution(writer,instance);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

    }

    private void writeSummary (BufferedWriter writer, Instance instance) throws IOException
    {
        writer.write(";;; Matrix size: "+instance.getN().getFirst().size()+" x "+instance.getN().size());
        writer.newLine();
        writer.write(";;; Solutions found: "+instance.getSolutions().size());
        writer.newLine();

        writer.write(";;; Solution cardinality: "+instance.calcMinMaxCard());
        writer.newLine();
    }

    private void writeSolution(BufferedWriter writer, Instance instance) throws IOException
    {
        writer.write(instance.solutionToString());
    }

}
