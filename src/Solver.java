import java.math.BigInteger;
import java.util.*;

public class Solver{

    public static final double NANO_TO_MILLI_RATE = 1e6;

    public void solve(Instance instance)
    {
        long startTime = System.nanoTime();

        // Generate M'
        instance.generateInputMatrix1();

        // Generate null hypothesis
        Hypothesis h0 = new Hypothesis(Collections.nCopies(instance.getInputMatrix1().size(), 0));
        setFields(instance, h0);
        instance.getPerLevelHypotheses().add(1);

        // Generate the one cardinality hypotheses
        List<Hypothesis>  current = generateSingletons(instance, h0);
        instance.getPerLevelTime().add(((double) (System.nanoTime() - startTime) / NANO_TO_MILLI_RATE));
        instance.getPerLevelHypotheses().add(current.size());

        Comparator<Hypothesis> comparator = Solver::compare;
        // Main cycle
        do
        {
            long startLevelTimer = System.nanoTime();
            List<Hypothesis> next = new ArrayList<>();
            int currentSize = current.size();

            // Cycle all hypotheses in current
            for (int i = 0; i < currentSize; i++)
            {
                Hypothesis h = current.get(i);

                // Using BigInteger as worst case number of hypotheses can be high
                instance.setExploredHypotheses(instance.getExploredHypotheses().add(BigInteger.valueOf(1)));


                if (h.isSolution())
                {
                    // If solution h is removed from current and counter decreased to not skip positions
                    instance.getSolutions().add(new Hypothesis(h));
                    current.remove(i);
                    i--;
                    currentSize--;
                }
                // First condition is that h must have at least a leading 0 to generate children
                // Second condition is the skip the generation of children with cardinality bigger than |N| as they will never be MHS
                else if (h.getBinaryRep().getFirst() == 0 && h.cardinality() < instance.getInputMatrix1().getFirst().size())
                {
                    Hypothesis h2s = h.globalInitial();

                    // Trim current removing hypotheses that have benne already explored and that don't contribute anymore to children generation
                    int removeSize = removeAllBiggerHypothesis(current, h2s);
                    i -= removeSize;
                    currentSize-=removeSize;

                    Hypothesis hp = current.getFirst();

                    if (!hp.equals(h))
                        // Generate h children and add them to next keeping the list sorted
                        //todo scegliere sorting
                        //merge(next, generateChildren(instance, current, h));
                        // Adding all the generated children to next without sorting it.
                        next.addAll(generateChildren(instance, current, h, i));
                }

                // Sampling for spatial performance benchmark
                instance.updateSpatialPerformance(Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory());
            }
            long endLevelTimer = System.nanoTime();
            instance.getPerLevelTime().add(((double) (endLevelTimer - startLevelTimer) / NANO_TO_MILLI_RATE));
            int nextSize = next.size();
            if(nextSize>0)
                // Saving the hypotheses per level
                instance.getPerLevelHypotheses().add(nextSize);

            // Sorting and reversing the next list only before assign it to be more efficient.
            next.sort(comparator);
            next = next.reversed();
            current = next;
            System.out.println("Completed level " + instance.getPerLevelHypotheses().size());
        }
        while (!current.isEmpty());
    }

    /**
     * Removes all Hypotheses bigger than a specified element in the given list
     * @param current list to remove from
     * @param h2s element to compare
     * @return number of elements removed
     */
    private int removeAllBiggerHypothesis(List<Hypothesis> current, Hypothesis h2s) {
        List<Hypothesis> toRemove = new ArrayList<>();
        int i = 0;
        while (current.get(i).isGreater(h2s))
        {
            toRemove.add(current.get(i));
            i++;
        }
        current.removeAll(toRemove);
        return i;
    }

    /**
     * Merges a list of hypotheses into another list in sorted order.
     * This method assumes that the input list {@param hypotheses} is sorted according
     * to the criteria defined by the Hypothesis.isGreater(Hypothesis) method.
     * Each hypothesis from the {@param hypotheses} list is inserted into the appropriate
     * position in the {@param next} list, maintaining the sorted order of {@param next}.
     *
     * @param next the list into which hypotheses will be merged. This list should already
     *             be sorted according to the criteria defined by the isGreater method.
     * @param hypotheses the list of hypotheses to be merged into the {@param next} list.
     *                   This list should also be sorted according to the criteria defined
     *                   by the isGreater method.
     */
    private void merge(List<Hypothesis> next, List<Hypothesis> hypotheses)
    {
        int lastIndex = 0;
        for (Hypothesis hypothesis : hypotheses)
        {
            boolean inserted = false;
            int nextSize = next.size();
            for (int i = lastIndex; i < nextSize; i++)
            {
                if (!next.get(i).isGreater(hypothesis)) {
                    inserted = true;
                    lastIndex = i + 1;
                    next.add(i, hypothesis);
                    break;
                }
            }
            if (!inserted)
            {
                next.add(hypothesis);
                lastIndex = next.size();
            }
        }
    }

    /**
     * Method used to compare two hypotheses
     * @param h1 first hypothesis
     * @param h2 second hypothesis
     * @return 1 if h1>h2, -1 se h2>h1, 0 se h1==h2
     */
    private static int compare(Hypothesis h1, Hypothesis h2) {
        if (h1.equals(h2))
            return 0;
        return h1.isGreater(h2) ? 1 : -1;
    }


    /**
     * Generates a list of child hypotheses by modifying the binary representation of the given hypothesis.
     *
     * @param instance the current instance being processed
     * @param current the list of current hypotheses
     * @param h the hypothesis to generate children from
     * @return a list of child hypotheses generated from the given hypothesis
     */
    private List<Hypothesis> generateChildren(Instance instance, List<Hypothesis> current, Hypothesis h, int hIndex) {
        // List to store the generated child hypotheses
        List<Hypothesis> children = new ArrayList<>();

        // Find the index of the first occurrence of '1' in the binary representation of the hypothesis
        int firstIndexOf1InH = h.getBinaryRep().indexOf(1);

        // Iterate through each bit position before the first '1' in the binary representation
        for (int i = 0; i < firstIndexOf1InH; i++) {
            // Create a new hypothesis by copying the binary representation of the current hypothesis
            Hypothesis h1 = new Hypothesis(h.getBinaryRep());

            // Set the i-th bit to '1' in the new hypothesis
            h1.getBinaryRep().set(i, 1);

            // Set additional fields in the new hypothesis based on the current instance
            setFields(instance, h1);

            // Propagate the current hypothesis values to the new hypothesis
            h.propagate(h1);

            // Find the initial and final predecessors of the new hypothesis
            Hypothesis h2i = h1.initialPredecessor(h);
            Hypothesis h2f = h1.finalPredecessor(h);

            // Find the index positions of the initial and final predecessors in the current list of hypotheses
            int h2iIndex = findInitial(current, h2i, hIndex);
            int h2fIndex = findFinal(current, h2iIndex, h2f, hIndex);

            // Counter to track the number of hypotheses in 'current' that are at a Hamming distance of 1 from the new hypothesis
            int counter = 0;

            // If both initial and final predecessors are found in the current list
            if (h2iIndex > -1 && h2fIndex > -1) {
                // Iterate through the range of hypotheses from the initial to the final predecessor to count the "fathers"
                for (int j = h2iIndex; j <= h2fIndex; j++) {
                    // If the Hamming distance between the current hypothesis and the new hypothesis is 1 it's a father
                    if (current.get(j).hammingDist(h1) == 1) {
                        // Propagate the values from the current hypothesis to the new hypothesis
                        current.get(j).propagate(h1);
                        // Increment the counter
                        counter++;
                    }
                }
            }

            // If the counter equals the cardinality of the parent, add the new hypothesis to the children list
            if (counter == h.cardinality())
                children.add(h1);

        }

        // Return the list of generated child hypotheses
        return children;
    }


    /**
     * Finds the index of the final predecessor hypothesis in the list of current hypotheses.
     *
     * @param current the list of current hypotheses
     * @param h2iIndex the index of the initial predecessor hypothesis
     * @param h2f the final predecessor hypothesis to find
     * @return the index of the final predecessor hypothesis if found, otherwise -1
     */
    private int findFinal(List<Hypothesis> current, int h2iIndex, Hypothesis h2f) {
        // Check if the initial predecessor index is valid
        if (h2iIndex != -1) {
            // Get the size of the current list of hypotheses
            // Iterate through the list starting from the initial predecessor index
            for (int i = h2iIndex; i < currentSize; i++) {
                // If the current hypothesis equals the final predecessor hypothesis, return its index
                if (current.get(i).equals(h2f))
                    return i;
            }
        }
        // Return -1 if the final predecessor hypothesis is not found
        return -1;
    }

    /**
     * Finds the index of the initial predecessor hypothesis in the list of current hypotheses.
     *
     * @param current the list of current hypotheses
     * @param h2i the initial predecessor hypothesis to find
     * @return the index of the initial predecessor hypothesis if found, otherwise -1
     */
    private int findInitial(List<Hypothesis> current, Hypothesis h2i) {
        // Return the index of the initial predecessor hypothesis in the current list, or -1 if not found
        for (int i = 0; i < hIndex; i++)
        {
            if (current.get(i).equals(h2i))
                return i;
        }

        return -1;
    }



    /**
     * Generates a list of singleton child hypotheses by modifying the binary representation of the given hypothesis.
     *
     * @param instance the current instance being processed
     * @param h the hypothesis to generate singletons from
     * @return a list of singleton child hypotheses generated from the given hypothesis
     */
    private List<Hypothesis> generateSingletons(Instance instance, Hypothesis h) {
        // List to store the generated singleton child hypotheses
        List<Hypothesis> children = new ArrayList<>();

        // Get the size of the input matrix1 from the instance
        int matrix1Size = instance.getInputMatrix1().size();

        // Iterate through each position in the input matrix1
        for (int i = 0; i < matrix1Size; i++) {
            // Create a new hypothesis by copying the binary representation of the current hypothesis
            Hypothesis h1 = new Hypothesis(h.getBinaryRep());

            // Set the i-th bit to '1' in the new hypothesis
            h1.getBinaryRep().set(i, 1);

            // Set additional fields in the new hypothesis for singleton generation
            setFieldsSingleton(instance, h1);

            // Add the new hypothesis to the list of children
            children.add(h1);
        }

        // Return the list of generated singleton child hypotheses
        return children;
    }


    /**
     * Sets the fields of the given hypothesis for general hypothesis generation.
     *
     * @param instance the current instance being processed
     * @param h the hypothesis to set fields for
     */
    private void setFields(Instance instance, Hypothesis h) {
        // Initialize the hit vector of the hypothesis with zeros, the size matching the first row of the input matrix
        h.setHitVector(Collections.nCopies(instance.getInputMatrix1().getFirst().size(), 0));
    }

    /**
     * Sets the fields of the given singleton hypothesis.
     *
     * @param instance the current instance being processed
     * @param singleton the singleton hypothesis to set fields for
     */
    private void setFieldsSingleton(Instance instance, Hypothesis singleton) {
        // Set the hit vector of the singleton hypothesis based on the row of the input matrix corresponding to the first '1' in its binary representation
        singleton.setHitVector(instance.getInputMatrix1().get(singleton.getBinaryRep().indexOf(1)));
    }
}
