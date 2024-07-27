import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Writer {
    private static final String resultPath = "results/";
    private static final String permutationsPath = "permutations/";
    private BufferedWriter writer;

    public Writer(String name) {
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
            writer.write(";;; The program was interrupted by the user.");
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
        writer.write(";;; Solutions found: " + instance.getSolutions().size());
        writer.newLine();
        writer.write(";;; Solution cardinality: " + instance.calcMinMaxCard());
        writer.newLine();
        writer.write(";;; Number of generated Hypothesis per level: " + instance.perLevelHypotesisToString(interrupted));
        writer.newLine();
        writer.write(";;; Time taken at each level: " + instance.perLevelTimeToString());
        writer.newLine();
        writer.write(";;; Maximum spatial occupation: " + instance.getSpatialPerformance() / 1000 + " KB");
        writer.newLine();
        if (!interrupted) {
            writer.write(";;; Time elapsed during computation: " + instance.getTemporalPerformance() + " milliseconds");
            writer.newLine();
        }
        if (interrupted) {
            writer.write(";;; Cardinality reached during exploration: " + instance.getMaxCardExplored());
            writer.newLine();
        }
    }

    private void writeSolution(Instance instance) throws IOException {
        writer.write(instance.solutionToString());
    }

    public void write(String message) throws IOException {
        writer.write(";;; ErrorMessage: " + message);
        writer.flush();
    }

    public void writePermutation(Permutation permutation) throws IOException {
        String permOutPath = permutationsPath + permutation.instanceName;
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
