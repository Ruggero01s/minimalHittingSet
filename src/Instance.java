import java.util.ArrayList;
import java.util.List;


public class Instance {
    String instanceName;

    List<List<Integer>> inputMatrix = new ArrayList<>();

    List<List<Integer>> inputMatrix1 = new ArrayList<>();

    List<Hypothesis> solutions = new ArrayList<>();

    List<Integer> perLevelHypothesis = new ArrayList<>();
    List<Double> perLevelTime = new ArrayList<>();

    List<Integer> emptyColumns = new ArrayList<>();

    public double getTemporalPerformance() {
        return temporalPerformance;
    }

    public void setTemporalPerformance(double temporalPerformance) {
        this.temporalPerformance = temporalPerformance;
    }

    double temporalPerformance;
    long spatialPerformance;

    int maxCardExplored = 0;

    public Instance(String instanceName, List<List<Integer>> input) {
        this.instanceName = instanceName;
        this.inputMatrix = new ArrayList<>(input);
    }

    public Instance(Instance instance) {
        this.instanceName = instance.instanceName;
        this.inputMatrix = new ArrayList<>(instance.inputMatrix);
    }

    public void generateInputMatrix1() {
        for (int i = 0; i < inputMatrix.size(); i++) {
            if (inputMatrix.get(i).contains(1)) {
                inputMatrix1.add(inputMatrix.get(i));
            } else
                emptyColumns.add(i);
        }
    }


    public List<List<Integer>> getInputMatrix() {
        return inputMatrix;
    }

    public List<List<Integer>> getInputMatrix1() {
        return inputMatrix1;
    }

    public int getMaxCardExplored() {
        return maxCardExplored;
    }

    public void setMaxCardExplored(int maxCardExplored) {
        this.maxCardExplored = maxCardExplored;
    }

    public List<Double> getPerLevelTime() {
        return perLevelTime;
    }

    public String solutionToString() {
        for (Integer column : emptyColumns)
            for (Hypothesis solution : solutions)
                solution.getBinaryRep().add(column, 0);

        StringBuilder solutionString = new StringBuilder();
        for (Hypothesis solution : solutions) {
            for (Integer element : solution.getBinaryRep()) {
                solutionString.append(element).append(" ");
            }
            solutionString.append(" -\n");
        }

        return solutionString.toString();
    }

    public String inputMatrixToString() {
        List<List<Integer>> inputMatrix = new ArrayList<>(Reader.invertMatrix(this.inputMatrix));

        StringBuilder stringBuilder = new StringBuilder();
        for (List<Integer> row : inputMatrix) {
            for (Integer column : row) {
                stringBuilder.append(column).append(" ");
            }
            stringBuilder.append(" -\n");
        }
        return stringBuilder.toString();
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
        if (max == -1 && min == Integer.MAX_VALUE)
            return "Min: NaN  Max: NaN";
        else
            return "Min: " + min + " Max: " + max;
    }

    public String emptyColumnsToString() {
        StringBuilder emptyColumnsString = new StringBuilder();
        for (int i = 0; i < emptyColumns.size(); i++) {
            emptyColumnsString.append(emptyColumns.get(i) + 1).append(" ");
        }
        return emptyColumnsString.toString();
    }

    public String perLevelHypotesisToString(boolean interrupted) {
        int c = 1;
        if (interrupted)
            c = 0;
        StringBuilder perLevelHypotesisString = new StringBuilder();
        for (int i = 0; i < perLevelHypothesis.size() - c; i++) {
            perLevelHypotesisString.append(i).append(" -> ").append(perLevelHypothesis.get(i)).append(" || ");
        }
        return perLevelHypotesisString.toString().substring(0, perLevelHypotesisString.length() - 4);
    }

    public String perLevelTimeToString() {
        StringBuilder perLevelTimeString = new StringBuilder();
        for (int i = 0; i < perLevelTime.size(); i++) {
            perLevelTimeString.append(i).append(" -> ").append(perLevelTime.get(i)).append("ms || ");
        }
        return perLevelTimeString.toString().substring(0, perLevelTimeString.length() - 4);
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

    public long getSpatialPerformance() {
        return spatialPerformance;
    }

    public void updateSpatialPerformance(long size) {
        if (size > this.spatialPerformance)
            this.spatialPerformance = size;
    }

    public String getInstanceName() {
        return instanceName;
    }

    public void setInstanceName(String instanceName) {
        this.instanceName = instanceName;
    }
}
