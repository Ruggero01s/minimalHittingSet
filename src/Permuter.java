import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * The `Permuter` class handles the permutation of rows and columns of an instance's input matrix.
 * It generates multiple permutations of the input instance and writes them to files.
 */
public class Permuter {
    private static boolean permuteRows = true;
    private static boolean permuteCols = true;

    private static String basePath = "benchmarks/benchmarks1/";
    private static String fileName = "74L85.025.matrix";
    private static Instance instanceToPermute;
    private static List<Permutation> permutations = new ArrayList<>();
    private static final int numberOfPermutations = 100;
    private static Writer writer;

    /**
     * Sets up the permutation flags for rows and columns.
     *
     * @param permRows boolean indicating whether rows should be permuted
     * @param permCols boolean indicating whether columns should be permuted
     */
    public static void setUp(boolean permRows, boolean permCols) {
        permuteRows = permRows;
        permuteCols = permCols;
    }

    /**
     * The main method to execute the permutation process.
     *
     * @param args command line arguments (not used)
     */
    public static void main(String[] args) {
        writer = new Writer(fileName);
        try {
            // Read the instance from the file
            instanceToPermute = Reader.readInstance(basePath, fileName);
        } catch (IOException e) {
            try {
                // Log an error message if reading the file fails
                writer.write(";;; Error reading file (" + basePath + fileName + "): " + e.getMessage());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        // Generate the specified number of permutations
        for (int i = 0; i < numberOfPermutations; i++) {
            Permutation permutedInstance = permute(instanceToPermute);
            permutedInstance.setInstanceName(generateName(permutedInstance, i));
            permutations.add(permutedInstance);
        }

        // Write each permutation to a file
        for (Permutation permutation : permutations) {
            try {
                writer.writePermutation(permutation);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Generates a new name for the permuted instance.
     * The name will be the same as origin instance  plus the number of the permutation
     * Ex.: XXXXX.XXX.01.matrix
     *
     * @param perm the permutation instance
     * @param i the index of the permutation
     * @return the generated name for the permutation file
     */
    private static String generateName(Permutation perm, int i) {
        // Always write the i as two characters to keep file order in batch execution
        if (i + 1 < 10) {
            return perm.getOriginInstance().substring(perm.getOriginInstance().lastIndexOf("/") + 1, perm.getOriginInstance().lastIndexOf(".") + 1) + "0" + (i + 1) + ".matrix";
        } else {
            return perm.getOriginInstance().substring(perm.getOriginInstance().lastIndexOf("/") + 1, perm.getOriginInstance().lastIndexOf(".") + 1) + (i + 1) + ".matrix";
        }
    }

    /**
     * Permutes the rows and/or columns of the given instance based on the permutation flags.
     *
     * @param instance the original instance to be permuted
     * @return the permuted instance
     */
    public static Permutation permute(Instance instance) {
        Random rand = new Random();

        // Initialize new columns and rows
        List<Integer> newColumns = new ArrayList<>();
        for (int i = 0; i < instance.getInputMatrix().size(); i++)
            newColumns.add(i);

        List<Integer> newRows = new ArrayList<>();
        for (int i = 0; i < instance.getInputMatrix().getFirst().size(); i++)
            newRows.add(i);


        // Return a new permutation without shuffling if both rows and columns are not to be permuted
        if (!permuteRows && !permuteCols)
            return new Permutation(new Instance(instance.instanceName, instance.getInputMatrix()), basePath + fileName, newColumns, newRows);

        // Shuffle columns if permuteCols is true
        if (permuteCols)
            Collections.shuffle(newColumns, rand);

        // Shuffle rows if permuteRows is true
        if (permuteRows)
            Collections.shuffle(newRows, rand);

        // Create a new permuted input matrix
        List<List<Integer>> newN = new ArrayList<>();
        for (Integer newColumn : newColumns)
        {
            List<Integer> newNRow = new ArrayList<>();
            for (Integer newRow : newRows)
                newNRow.add(instance.getInputMatrix().get(newColumn).get(newRow));
            newN.add(newNRow);
        }

        // Create a new instance with the permuted input matrix
        Instance newInstance = new Instance(instance.instanceName, newN);
        return new Permutation(newInstance, basePath + fileName, newColumns, newRows);
    }
}
