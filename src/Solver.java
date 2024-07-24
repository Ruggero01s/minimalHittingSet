import java.util.*;

public class Solver {

    public List<Hypothesis> all = new ArrayList<>();

    public List<Hypothesis> solve(Instance instance) {
        long startTime = System.currentTimeMillis();
        instance.generateInputMatrix1();
        Hypothesis h0 = new Hypothesis(Collections.nCopies(instance.getInputMatrix1().size(), 0));
        setFields(instance, h0);
        List<Hypothesis> current = new ArrayList<>();
        current.add(h0);
        instance.getPerLevelHypothesis().add(1);
        List<Hypothesis> solutions = new ArrayList<>();
        do {
            long startLevelTimer = System.currentTimeMillis();
            List<Hypothesis> next = new ArrayList<>();
            for (int i = 0; i < current.size(); i++) {
                Hypothesis h = current.get(i);
                all.add(new Hypothesis(h));
                //h.reCalcHitVector(instance);
                if (h.isSolution()) {
                    solutions.add(new Hypothesis(h));
                    current.remove(i);
                    i--;
                } else if (h.isNullSolution()) {
                    next.addAll(generateChildren(instance, current, h));
                } else if (h.getBinaryRep().indexOf(1) != 0) {
                    Hypothesis h2s = h.globalInitial();

                    int preRemoveLength = current.size();
                    removeAllBiggerHypothesis(current, h2s);
                    int postRemoveLength = current.size();
                    i = i - (preRemoveLength - postRemoveLength);

                    Hypothesis hp = current.getFirst();
                    if (!hp.equals(h))
                        next = merge(next, generateChildren(instance, current, h));
                }
                instance.updateSpatialPerformance(Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory());
            }
            long endLevelTimer = System.currentTimeMillis();
            instance.getPerLevelTime().add(((double) (endLevelTimer - startLevelTimer) / 1000.0));
            instance.getPerLevelHypothesis().add(next.size());
            instance.setMaxCardExplored(instance.getMaxCardExplored() + 1);
            current = next;
        }
        while (!current.isEmpty());
        long endTime = System.currentTimeMillis();
        instance.setTemporalPerformance(endTime - startTime);
        return solutions;
    }

    private void removeAllBiggerHypothesis(List<Hypothesis> current, Hypothesis h2s) {
        //todo tornare subarray
        for (int i = 0; i < current.size(); i++) {
            if (current.get(i).isGreater(h2s)) {
                current.remove(i);
                i--;
            }
        }
        //current.removeIf(hypothesis -> hypothesis.isGreater(h2s));
    }

    private List<Hypothesis> merge(List<Hypothesis> next, List<Hypothesis> hypotheses) {
        next.addAll(hypotheses);
        Comparator<Hypothesis> hypothesisComparator = Solver::compare;
        next.sort(hypothesisComparator);
        return next.reversed();
    }

    private static int compare(Hypothesis h1, Hypothesis h2) {
        if (h1.equals(h2))
            return 0;

        return h1.isGreater(h2) ? 1 : -1;
    }


    private List<Hypothesis> generateChildren(Instance instance, List<Hypothesis> current, Hypothesis h) {
        List<Hypothesis> children = new ArrayList<>();
        if (h.isNullSolution()) {
            for (int i = 0; i < instance.getInputMatrix1().size(); i++) {
                Hypothesis h1 = new Hypothesis(h.getBinaryRep());
                h1.getBinaryRep().set(i, 1);
                setFields(instance, h1);
                children.add(h1);
            }
            return children;
        }

        //Hypothesis hp = current.getFirst();
        for (int i = 0; i < h.getBinaryRep().indexOf(1); i++) {
            Hypothesis h1 = new Hypothesis(h.getBinaryRep());
            h1.getBinaryRep().set(i, 1);
            setFields(instance, h1);
            h.propagate(h1);
            Hypothesis h2i = h1.initialPredecessor(h);
            Hypothesis h2f = h1.finalPredecessor(h);

            int h2iIndex = current.indexOf(h2i);
            int h2fIndex = current.indexOf(h2f);

            int counter = 0;
            if (h2iIndex > -1 && h2fIndex > -1) {
                for (int j = h2iIndex; j <= h2fIndex; j++) {
                    if (current.get(j).hammingDist(h1) == 1) {
                        current.get(j).propagate(h1);
                        counter++;
                    }
                }
            }

            /*int hpIndex = current.indexOf(hp);
            //System.out.println("hp index: " + hpIndex);
            //System.out.println("h2f index: " + h2fIndex);
            //System.out.println("h2i index: " + h2iIndex);
                //(h2i.isGreaterOrEqual(hp)) && (hp.isGreaterOrEqual(h2f))
            while (hpIndex >= h2iIndex && hpIndex <= h2fIndex) {
                if (current.get(hpIndex).hammingDist(h1) == 1) {
                    current.get(hpIndex).propagate(h1);
                    counter++;
                }
                //todo check che sia effettivamente next
                hpIndex += 1;
            }*/

            if (counter == h.cardinality())
                children.add(h1);
        }
        return children;
    }

    private void setFields(Instance instance, Hypothesis h) {
        if (h.hammingDist(new Hypothesis(Collections.nCopies(instance.getInputMatrix1().size(), 0))) == 1)
            h.setHitVector(instance.getInputMatrix1().get(h.getBinaryRep().indexOf(1)));
        else
            h.setHitVector(Collections.nCopies(instance.getInputMatrix1().getFirst().size(), 0));
    }
}
