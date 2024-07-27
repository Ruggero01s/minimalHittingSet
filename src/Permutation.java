import java.util.ArrayList;
import java.util.List;

public class Permutation extends Instance {
    private String originInstance;
    private List<Integer> newColumns = new ArrayList<>();
    private List<Integer> newRows = new ArrayList<>();

    public Permutation(Instance instance, String originInstance, List<Integer> newColumns, List<Integer> newRows) {
        super(instance.instanceName, instance.getInputMatrix());
        this.originInstance = originInstance;
        this.newColumns = newColumns;
        this.newRows = newRows;
    }


    public List<Integer> getNewColumns() {
        return newColumns;
    }

    public List<Integer> getNewRows() {
        return newRows;
    }

    public String getOriginInstance() {
        return originInstance;
    }

    public String solutionToString() {
        for (Integer column : emptyColumns)
            for (Hypothesis solution : solutions)
                solution.getBinaryRep().add(column, 0);


        solutions = new ArrayList<>(permuteSolutionsToOriginal());

        StringBuilder solutionString = new StringBuilder();
        for (Hypothesis solution : solutions) {
            for (Integer element : solution.getBinaryRep()) {
                solutionString.append(element).append(" ");
            }
            solutionString.append(" -\n");
        }

        return solutionString.toString();
    }

    private List<Hypothesis> permuteSolutionsToOriginal() {
        //todo check, mi sembra che non stia facendo giusto
        List<Hypothesis> newSolutions = new ArrayList<>();
        if (!newColumns.isEmpty()) {
            for (Hypothesis solution : solutions) {
                ArrayList<Integer> newBinRep = new ArrayList<>();
                for (Integer newColumn : newColumns) {
                    newBinRep.add(solution.getBinaryRep().get(newColumn));
                }
                newSolutions.add(new Hypothesis(newBinRep));
            }
        }
        return newSolutions;
    }

}
