import java.util.ArrayList;
import java.util.List;

/**
 * The `Permutation` class extends the `Instance` class to represent a permuted version of an instance.
 * It includes additional functionality to handle new columns and rows for the permuted instance.
 */
public class Permutation extends Instance {
    private String originInstance;
    private List<Integer> newColumns;
    private List<Integer> newRows;

    /**
     * Constructs a Permutation with the specified instance, origin instance name, new columns, and new rows.
     *
     * @param instance the original instance to be permuted
     * @param originInstance the name of the origin instance
     * @param newColumns the list of new columns for the permutation
     * @param newRows the list of new rows for the permutation
     */
    public Permutation(Instance instance, String originInstance, List<Integer> newColumns, List<Integer> newRows) {
        super(instance.instanceName, instance.getInputMatrix());
        this.originInstance = originInstance;
        this.newColumns = newColumns;
        this.newRows = newRows;
    }

    /**
     * Returns the list of new columns for the permutation.
     *
     * @return the list of new columns
     */
    public List<Integer> getNewColumns() {
        return newColumns;
    }

    /**
     * Returns the list of new rows for the permutation.
     *
     * @return the list of new rows
     */
    public List<Integer> getNewRows() {
        return newRows;
    }

    /**
     * Returns the name of the origin instance.
     *
     * @return the name of the origin instance
     */
    public String getOriginInstance() {
        return originInstance;
    }

    /**
     * Converts the solution hypotheses to a string representation, including any empty columns,
     * and permutes the solutions to their original form.
     *
     * @return the string representation of the solution hypotheses
     */
    @Override
    public String solutionToString() {
        // Add empty columns back to the solutions
        for (Integer column : emptyColumns) {
            for (Hypothesis solution : solutions) {
                solution.getBinaryRep().add(column, 0);
            }
        }

        // Permute solutions to their original form
        solutions = new ArrayList<>(permuteSolutionsToOriginal());

        // Build the string representation of the solutions
        StringBuilder solutionString = new StringBuilder();
        for (Hypothesis solution : solutions) {
            for (Integer element : solution.getBinaryRep()) {
                solutionString.append(element).append(" ");
            }
            solutionString.append(" -\n");
        }

        return solutionString.toString();
    }

    /**
     * Permutes the solutions to their original form based on the new columns.
     *
     * @return a list of permuted hypotheses
     */
    private List<Hypothesis> permuteSolutionsToOriginal() {
        List<Hypothesis> newSolutions = new ArrayList<>();
        if (!newColumns.isEmpty()) {
            for (Hypothesis solution : solutions) {
                ArrayList<Integer> newBinRep = new ArrayList<>();
                for (int i = 0; i < newColumns.size(); i++) {
                    int index = newColumns.indexOf(i);
                    newBinRep.add(solution.getBinaryRep().get(index));
                }
                newSolutions.add(new Hypothesis(newBinRep));
            }
        }
        return newSolutions;
    }
}
