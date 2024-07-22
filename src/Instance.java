import java.util.ArrayList;
import java.util.List;


public class Instance
{
    private List<String> M = new ArrayList<>();
    private List<List<Integer>> N = new ArrayList<>();

    private List<String> M1 = new ArrayList<>();
    private List<List<Integer>> N1 = new ArrayList<>();

    private List<Hypothesis> solutions = new ArrayList<>();

    private List<Integer> perLevelHypothesis = new ArrayList<>();

    private List<Integer> emptyColumns = new ArrayList<>();



    private int maxCardExplored = 0;

    public Instance (List<String> m, List<List<Integer>> n)
    {
        this.M = new ArrayList<>(m);
        this.N = new ArrayList<>(n);
        generateM1andN1();
    }

    private void generateM1andN1()
    {
        for (int i = 0; i < M.size(); i++)
        {
            if(N.get(i).contains(1))
            {
                N1.add(N.get(i));
                M1.add(M.get(i));
            }
            else
                emptyColumns.add(i);
        }
    }

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

    public int getMaxCardExplored() {
        return maxCardExplored;
    }

    public void setMaxCardExplored(int maxCardExplored) {
        this.maxCardExplored = maxCardExplored;
    }

    public String solutionToString()
    {
        for (Integer column : emptyColumns)
            for (Hypothesis solution : solutions)
                solution.getBinaryRep().add(column,0);

        StringBuilder solutionString = new StringBuilder();
        for (Hypothesis solution : solutions)
        {
            for (Integer element : solution.getBinaryRep())
            {
                solutionString.append(element).append(" ");
            }
            solutionString.append(" -\n");
        }

        return solutionString.toString();
    }

    public String calcMinMaxCard() {
        int min = Integer.MAX_VALUE;
        int max = -1;
        for (Hypothesis solution : solutions) {
            int card = solution.cardinality();
            if (card < min)
                min = card;
            if (card > max)
                max = card;
        }
        if(max == -1 && min == Integer.MAX_VALUE)
            return "Min: NaN  Max: NaN";
        else
            return "Min: " + min + " Max: " + max;
    }

    public String emptyColumnsToString()
    {
        StringBuilder emptyColumnsString = new StringBuilder();
        for (int i=0;i<emptyColumns.size();i++)
        {
            emptyColumnsString.append(emptyColumns.get(i)+1).append(" ");
        }
        return emptyColumnsString.toString();
    }

    public String perLevelHypotesisToString()
    {
        //todo sistemare lo stampaggio
        StringBuilder perLevelHypotesisString = new StringBuilder();
        for (int i = 0; i < perLevelHypothesis.size(); i++)
        {
            perLevelHypotesisString.append(i+1).append(" : ").append(perLevelHypothesis.get(i)).append(" - ");
        }
        return perLevelHypotesisString.toString();
    }

    public List<Hypothesis> getSolutions() {
        return solutions;
    }

    public void setSolutions(List<Hypothesis> solutions) {
        this.solutions = new ArrayList<>(solutions);
    }

    public List<Integer> getPerLevelHypothesis() {
        return perLevelHypothesis;
    }

    public void setPerLevelHypothesis(List<Integer> perLevelHypothesis) {
        this.perLevelHypothesis = new ArrayList<>(perLevelHypothesis);
    }
}
