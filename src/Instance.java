import java.util.ArrayList;
import java.util.List;


public class Instance
{
    private List <String> M = new ArrayList<>();
    private List <String> M1 = new ArrayList<>(); //the matrix m after the preprocessing
    private List<List<Integer>> N = new ArrayList<>();
    private List<List<Integer>> N1 = new ArrayList<>();

    private List<Hypothesis> solutions = new ArrayList<>();

    private List <Integer> emptyColumns = new ArrayList<>(); //list of columns removed from M

    public List<String> getM() {
        return M;
    }

    public List<String> getM1() {
        return M1;
    }

    public List<List<Integer>> getN() {
        return N;
    }

    public List<List<Integer>> getN1() {
        return N1;
    }

    public List<Integer> getEmptyColumns() {
        return emptyColumns;
    }

    public List<Hypothesis> getSolutions() {
        return solutions;
    }

    public void setSolutions(List<Hypothesis> solutions) {
        this.solutions = solutions;
    }

    public Instance(List<String> m, List<List<Integer>> n) {
        M = new ArrayList<>(m);
        N = new ArrayList<>(n);
        calculateM1();
        calculateN1();
    }

    private void calculateN1() {
        N1 = new ArrayList<>(N);
        N1.removeIf(x->!x.contains(1));
    }

    /**
     * Calculates M1 by creating a copy of M and then removing elements based on
     * the columns in N that are empty (contain only zeros).
     * The columns that are empty are tracked and their indices are stored in the
     * emptyColumns list.
     */
    private void calculateM1 ()
    {
        M1 = new ArrayList<>(M); // Create M1 as a copy of M
        int counter=0;

        for (int i=0;i<N.size(); i++)
        {
            if(!N.get(i).contains(1))
            {
                emptyColumns.add(i); // Record the index of the empty column
                M1.remove(i-counter); // Remove the corresponding element from M1, adjusting the index by counter
                counter++;
            }
        }
    }
}
