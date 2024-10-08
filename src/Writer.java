import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Writer
{
    private static final String resultPath = "results/";
    private static final String permutationsPath = "permutations/";
    private static final String errorPath = "permutations/error.log";
    private BufferedWriter writer;

    public Writer(String name) {
        // Create output name bt replacing .matrix with .mhs
        String fileName = name.substring(0, name.lastIndexOf(".")) + ".mhs";
        try {
            writer = new BufferedWriter(new FileWriter(resultPath + fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeOut(Instance instance, boolean interrupted) throws IOException {
        writeSummary(instance, interrupted);
        writeSolution(instance);
        writer.flush();
    }

    private void writeSummary(Instance instance, boolean interrupted) throws IOException {
        if (interrupted) {
            writer.write(";;; The program was interrupted prematurely.");
            writer.newLine();
            writer.write(";;; The program was exploring the level: "+ (instance.getPerLevelHypotheses().size() - 1));
            writer.newLine();
        }
        if (instance instanceof Permutation permutation) {
            writer.write(";;; Permutation of " + permutation.getOriginInstance());
            writer.newLine();
            writer.write(";;; New order of columns indexes: " + permutation.getNewColumns().toString());
            writer.newLine();
            writer.write(";;; New order of rows indexes: " + permutation.getNewRows().toString());
            writer.newLine();
        }
        writer.write(";;; Matrix size: " + instance.getInputMatrix().getFirst().size() + " x " + instance.getInputMatrix().size());
        writer.newLine();
        writer.write(";;; |M'|: " + instance.getInputMatrix1().size());
        writer.newLine();
        writer.write(";;; Suppressed columns: " + instance.emptyColumnsToString());
        writer.newLine();
        writer.write(";;; Number of explored Hypotheses: " + instance.getExploredHypotheses());
        writer.newLine();
        writer.write(";;; Number of generated Hypotheses per level: " + instance.perLevelHypothesesToString());
        writer.newLine();
        writer.write(";;; Execution time: " + instance.getExecutionTime() + " ms");
        writer.newLine();
        writer.write(";;; Time taken at each level: " + instance.perLevelTimeToString());
        writer.newLine();
        writer.write(";;; Maximum spatial occupation: " + instance.getSpatialPerformance() / 1000 + " KB");
        writer.newLine();
        writer.write(";;; Solutions found: " + instance.getSolutions().size() +" || Cardinality: " + instance.calcMinMaxCard());
        writer.newLine();
    }

    private void writeSolution(Instance instance) throws IOException {
        writer.write(instance.solutionToString());
    }

    public void writeError(String message) throws IOException {
        BufferedWriter errorWriter = new BufferedWriter(new FileWriter(errorPath));
        errorWriter.write(";;; ErrorMessage: " + message);
        errorWriter.flush();
    }

    public void writePermutation(Permutation permutation) throws IOException {
        String permOutPath = permutationsPath + permutation.getInstanceName();
        BufferedWriter permutationWriter = new BufferedWriter(new FileWriter(permOutPath));
        permutationWriter.write(";;; Permutation of: ''" + permutation.getOriginInstance()+"''");
        permutationWriter.newLine();
        permutationWriter.write(";;; New order of columns indexes: ");
        permutationWriter.newLine();
        permutationWriter.write("Cols: " + permutation.getNewColumns().toString());
        permutationWriter.newLine();
        permutationWriter.write(";;; New order of rows indexes: ");
        permutationWriter.newLine();
        permutationWriter.write("Rows: " + permutation.getNewRows().toString());
        permutationWriter.newLine();
        permutationWriter.write(permutation.inputMatrixToString());
        permutationWriter.close();
    }
}
