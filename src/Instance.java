import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The `Instance` class represents an instance of a problem with input matrices,
 * hypotheses, and various metrics related to the problem-solving process.
 */
public class Instance {
    protected String instanceName;
    protected List<List<Integer>> inputMatrix;
    protected List<List<Integer>> inputMatrix1 = new ArrayList<>();
    protected List<Hypothesis> solutions = new ArrayList<>();
    protected List<Integer> perLevelHypothesis = new ArrayList<>();
    protected List<Double> perLevelTime = new ArrayList<>();
    protected List<Integer> emptyColumns = new ArrayList<>();
    protected double executionTime;
    protected BigInteger exploredHypotheses = BigInteger.valueOf(1);
    long spatialPerformance;

    /**
     * Constructs an Instance with the specified name and input matrix.
     *
     * @param instanceName the name of the instance
     * @param input the input matrix
     */
    public Instance(String instanceName, List<List<Integer>> input) {
        this.instanceName = instanceName;
        this.inputMatrix = new ArrayList<>(input);
    }

    /**
     * Generates the input matrix1 by filtering rows from the input matrix that contain at least one '1'.
     * Populates the empty columns list with indices of columns that do not contain any '1'.
     */
    public void generateInputMatrix1() {
        for (int i = 0; i < inputMatrix.size(); i++) {
            if (inputMatrix.get(i).contains(1)) {
                inputMatrix1.add(inputMatrix.get(i));
            } else {
                emptyColumns.add(i);
            }
        }
    }

    public List<List<Integer>> getInputMatrix() {
        return inputMatrix;
    }

    public List<List<Integer>> getInputMatrix1() {
        return inputMatrix1;
    }

    public List<Double> getPerLevelTime() {
        return perLevelTime;
    }

    /**
     * Converts the solution hypotheses to a string representation, including any empty columns.
     *
     * @return the string representation of the solution hypotheses
     */
    public String solutionToString() {
        // Pads the solutions with the empty columns from the original matrix
        for (Integer column : emptyColumns) {
            for (Hypothesis solution : solutions) {
                solution.getBinaryRep().add(column, 0);
            }
        }

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
     * Converts the input matrix to a string representation.
     *
     * @return the string representation of the input matrix
     */
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

    /**
     * Calculates and returns the minimum and maximum cardinality of the solution hypotheses.
     *
     * @return the string representation of the minimum and maximum cardinality
     */
    public String calcMinMaxCard() {
        int min = Integer.MAX_VALUE;
        int max = -1;
        for (Hypothesis solution : solutions) {
            int card = solution.cardinality();
            if (card < min) {
                min = card;
            }
            if (card > max) {
                max = card;
            }
        }
        if (max == -1 && min == Integer.MAX_VALUE) {
            return "Min: NaN  Max: NaN";
        } else {
            return "Min: " + min + " Max: " + max;
        }
    }

    /**
     * Converts the list of empty columns to a string representation.
     *
     * @return the string representation of the empty columns
     */
    public String emptyColumnsToString() {
        StringBuilder emptyColumnsString = new StringBuilder();
        for (Integer emptyColumn : emptyColumns) {
            emptyColumnsString.append(emptyColumn + 1).append(" ");
        }
        return emptyColumnsString.toString();
    }

    /**
     * Converts the per-level hypotheses to a string representation.
     *
     * @param interrupted indicates whether the process was interrupted
     * @return the string representation of the per-level hypotheses
     */
    public String perLevelHypothesesToString(boolean interrupted) {
        int c = 1;
        if (interrupted) {
            c = 0;
        }

        StringBuilder perLevelHypotesisString = new StringBuilder();
        // todo cosa era -c?
        for (int i = 0; i < perLevelHypothesis.size() - c; i++) {
            perLevelHypotesisString.append(i).append(" -> ").append(perLevelHypothesis.get(i)).append(" || ");
        }
        return perLevelHypotesisString.substring(0, perLevelHypotesisString.length() - 4);
    }

    /**
     * Converts the per-level time to a string representation.
     *
     * @return the string representation of the per-level time
     */
    public String perLevelTimeToString() {
        StringBuilder perLevelTimeString = new StringBuilder();
        for (int i = 0; i < perLevelTime.size(); i++) {
            perLevelTimeString.append(i).append(" -> ").append(perLevelTime.get(i)).append(" ms || ");
        }
        return perLevelTimeString.substring(0, perLevelTimeString.length() - 4);
    }


    public List<Hypothesis> getSolutions() {
        return solutions;
    }


    public void setSolutions(List<Hypothesis> solutions) {
        this.solutions = new ArrayList<>(solutions);
    }


    public List<Integer> getPerLevelHypotheses() {
        return perLevelHypothesis;
    }


    public long getSpatialPerformance() {
        return spatialPerformance;
    }


    public void updateSpatialPerformance(long size) {
        if (size > this.spatialPerformance) {
            this.spatialPerformance = size;
        }
    }


    public String getInstanceName() {
        return instanceName;
    }


    public void setInstanceName(String instanceName) {
        this.instanceName = instanceName;
    }


    public double getExecutionTime() {
        return executionTime;
    }


    public void setExecutionTime(double executionTime) {
        this.executionTime = executionTime;
    }


    public BigInteger getExploredHypotheses() {
        return exploredHypotheses;
    }


    public void setExploredHypotheses(BigInteger exploredHypotheses) {
        this.exploredHypotheses = exploredHypotheses;
    }
}
