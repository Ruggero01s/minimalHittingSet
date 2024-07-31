import java.util.ArrayList;
import java.util.List;

/**
 * The `Hypothesis` class represents a hypothesis with a binary representation and a hit vector.
 * It provides methods to manipulate and compare hypotheses.
 */
public class Hypothesis {
    private List<Integer> binaryRep;
    private List<Integer> hitVector;

    /**
     * Constructs a `Hypothesis` with the given binary representation and hit vector.
     *
     * @param binaryRep the binary representation of the hypothesis
     * @param hitVector the hit vector of the hypothesis
     */
    public Hypothesis(List<Integer> binaryRep, List<Integer> hitVector) {
        this.binaryRep = new ArrayList<>(binaryRep);
        this.hitVector = new ArrayList<>(hitVector);
    }

    /**
     * Constructs a `Hypothesis` with the given binary representation.
     *
     * @param binaryRep the binary representation of the hypothesis
     */
    public Hypothesis(List<Integer> binaryRep) {
        this.binaryRep = new ArrayList<>(binaryRep);
    }

    /**
     * Copy constructor to create a new `Hypothesis` from an existing one.
     *
     * @param h the hypothesis to copy
     */
    public Hypothesis(Hypothesis h) {
        this.binaryRep = new ArrayList<>(h.getBinaryRep());
        this.hitVector = new ArrayList<>(h.getHitVector());
    }


    public List<Integer> getBinaryRep() {
        return binaryRep;
    }



    public List<Integer> getHitVector() {
        return hitVector;
    }


    public void setHitVector(List<Integer> hitVector) {
        this.hitVector = new ArrayList<>(hitVector);
    }

    /**
     * Checks if this hypothesis is greater than the given hypothesis.
     *
     * @param hypothesis the hypothesis to compare with
     * @return true if this hypothesis is greater, false otherwise
     */
    public boolean isGreater(Hypothesis hypothesis) {
        for (int i = 0; i < this.binaryRep.size(); i++) {
            if (this.binaryRep.get(i) > hypothesis.binaryRep.get(i))
                return true;
            if (this.binaryRep.get(i) < hypothesis.binaryRep.get(i))
                return false;
        }
        return false;
    }


    /**
     * Checks if this hypothesis is a solution.
     * A hypothesis is a solution if its hit vector does not contain 0.
     *
     * @return true if this hypothesis is a solution, false otherwise
     */
    public boolean isSolution() {
        return !this.hitVector.contains(0);
    }


    /**
     * Calculates the cardinality of the hypothesis.
     * Cardinality is the number of 1s in the binary representation.
     *
     * @return the cardinality of the hypothesis
     */
    public int cardinality() {
        int cardinality = 0;

        for (int bit : this.binaryRep)
            if (bit == 1)
                cardinality++;

        return cardinality;
    }

    /**
     * Propagates the hit vector from this hypothesis to the given hypothesis.
     *
     * @param h1 the hypothesis to propagate to
     */
    public void propagate(Hypothesis h1) {
        for (int i = 0; i < this.hitVector.size(); i++) {
            if (this.hitVector.get(i) == 1)
                h1.hitVector.set(i, 1);
        }
    }

    /**
     * Calculates the Hamming distance between this hypothesis and the given hypothesis.
     * Hamming distance is the number of positions at which the corresponding bits are different.
     *
     * @param h the hypothesis to compare with
     * @return the Hamming distance between the two hypotheses
     */
    public int hammingDist(Hypothesis h) {
        int distance = 0;
        for (int i = 0; i < h.binaryRep.size(); i++)
            if (!h.binaryRep.get(i).equals(this.binaryRep.get(i)))
                distance++;

        return distance;
    }

    /**
     * Finds the initial predecessor of this hypothesis relative to the given generating parent.
     *
     * @param generatingParent the generating parent hypothesis
     * @return the initial predecessor hypothesis
     */
    public Hypothesis initialPredecessor(Hypothesis generatingParent) {
        // Create a copy of the current binary representation for possible predecessor
        Hypothesis possiblePredecessor = new Hypothesis(this.binaryRep);
        boolean first = true;
        // Create a second possible predecessor to handle initial == h case
        Hypothesis secondPossiblePredecessor = new Hypothesis(this.binaryRep);

        // Iterate over the binary representation from the END to the START
        for (int i = possiblePredecessor.getBinaryRep().size() - 1; i >= 0; i--) {
            // Set the first encountered 1 to 0
            if (possiblePredecessor.getBinaryRep().get(i) == 1 && first) {
                possiblePredecessor.getBinaryRep().set(i, 0);
                first = false;
                // Set the next encountered 1 to 0 for the second possible predecessor
            } else if (secondPossiblePredecessor.getBinaryRep().get(i) == 1 && !first) {
                secondPossiblePredecessor.getBinaryRep().set(i, 0);
                break;
            }
        }

        // Return the appropriate predecessor based on comparison with the generating parent
        return possiblePredecessor.equals(generatingParent) ? secondPossiblePredecessor : possiblePredecessor;
    }

    /**
     * Finds the final predecessor of this hypothesis relative to the given generating parent.
     *
     * @param generatingParent the generating parent hypothesis
     * @return the final predecessor hypothesis
     */
    public Hypothesis finalPredecessor(Hypothesis generatingParent) {
        // Create a copy of the current binary representation for possible predecessor
        Hypothesis possiblePredecessor = new Hypothesis(this.binaryRep);
        boolean first = true;
        // Create a second possible predecessor to handle final == h case
        Hypothesis secondPossiblePredecessor = new Hypothesis(this.binaryRep);
        int binaryRepSize = possiblePredecessor.getBinaryRep().size();

        // Iterate over the binary representation from the START to the END
        for (int i = 0; i < binaryRepSize; i++) {
            // Set the first encountered 1 to 0
            if (possiblePredecessor.getBinaryRep().get(i) == 1 && first) {
                possiblePredecessor.getBinaryRep().set(i, 0);
                first = false;
                // Set the next encountered 1 to 0 for the second possible predecessor
            } else if (secondPossiblePredecessor.getBinaryRep().get(i) == 1 && !first) {
                secondPossiblePredecessor.getBinaryRep().set(i, 0);
                break;
            }
        }

        // Return the appropriate predecessor based on comparison with the generating parent
        return possiblePredecessor.equals(generatingParent) ? secondPossiblePredecessor : possiblePredecessor;
    }

    /**
     * Finds the global initial hypothesis by setting the first bit to 1 and the last bit to 0.
     *
     * @return the global initial hypothesis
     */
    public Hypothesis globalInitial() {
        Hypothesis globalInitial = new Hypothesis(this);

        globalInitial.binaryRep.set(0, 1);
        globalInitial.binaryRep.set(globalInitial.binaryRep.lastIndexOf(1), 0);

        return globalInitial;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Hypothesis that = (Hypothesis) o;
        return binaryRep.equals(that.binaryRep);
    }

    @Override
    public int hashCode() {
        int result = binaryRep.hashCode();
        result = 31 * result + (hitVector != null ? hitVector.hashCode() : 0);
        return result;
    }
}
