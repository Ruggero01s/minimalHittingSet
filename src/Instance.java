import java.util.ArrayList;
import java.util.List;


public class Instance
{
    private List <String> M = new ArrayList<>();
    private List <String> M1 = new ArrayList<>(); //the matrix m after the preprocessing
    private List<List<Integer>> N = new ArrayList<>();

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

    public List<Integer> getEmptyColumns() {
        return emptyColumns;
    }

    public Instance(List<String> m, List<List<Integer>> n) {
        M = new ArrayList<>(m);
        N = new ArrayList<>(n);
        calculateM1();
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
        for (int j=0;j<N.get(0).size();j++) // Iterate through each column
        {
            boolean emptyColumn= true;
            for(int i=0;i<N.size(); i++)
            {
                if(N.get(i).get(j)==1)
                {
                    emptyColumn = false;
                    break;
                }
            }
            if(emptyColumn)
            {
                emptyColumns.add(j); // Record the index of the empty column
                M1.remove(j-counter); // Remove the corresponding element from M1, adjusting the index by counter
                counter++;
            }
        }
    }
}
