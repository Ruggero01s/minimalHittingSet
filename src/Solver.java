import java.math.BigInteger;
import java.util.*;

public class Solver
{

    public static final double NANO_TO_MILLI_RATE = 1e6;

    public void solve(Instance instance) {
        long startTime = System.nanoTime();
        instance.generateInputMatrix1();
        Hypothesis h0 = new Hypothesis(Collections.nCopies(instance.getInputMatrix1().size(), 0));
        setFields(instance, h0);
        instance.getPerLevelHypothesis().add(1);
        List<Hypothesis>  current = generateSingletons(instance, h0);
        instance.getPerLevelTime().add(((double) (System.nanoTime() - startTime) / NANO_TO_MILLI_RATE));
        instance.getPerLevelHypothesis().add(current.size());
        do
        {
            long startLevelTimer = System.nanoTime();
            List<Hypothesis> next = new ArrayList<>();
            int currentSize = current.size();
            for (int i = 0; i < currentSize; i++)
            {
                Hypothesis h = current.get(i);
                instance.setExploredHypotesis(instance.getExploredHypothesis().add(BigInteger.valueOf(1)));
                if (h.isSolution())
                {
                    instance.getSolutions().add(new Hypothesis(h));
                    current.remove(i);
                    i--;
                    currentSize--;
                }
                else if (h.getBinaryRep().indexOf(1) != 0)
                {
                    Hypothesis h2s = h.globalInitial();
                    int removeSize = removeAllBiggerHypothesis(current, h2s);
                    i -= removeSize;
                    currentSize-=removeSize;
                    Hypothesis hp = current.getFirst();
                    if (!hp.equals(h))
                        merge(next, generateChildren(instance, current, h));
                    else
                        System.out.println("fuk"); //todo remove debug
                }
            }
            long endLevelTimer = System.nanoTime();
            instance.getPerLevelTime().add(((double) (endLevelTimer - startLevelTimer) / NANO_TO_MILLI_RATE));
            if(!next.isEmpty())
                if (next.getFirst().cardinality() > instance.getInputMatrix1().getFirst().size())
                    break;
            instance.getPerLevelHypothesis().add(next.size());
            instance.updateSpatialPerformance(Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory());
            current = next;
        }
        while (!current.isEmpty());
        long endTime = System.nanoTime();
        instance.setTemporalPerformance((double) (endTime - startTime) / NANO_TO_MILLI_RATE);
    }

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
                next.add(hypothesis);
        }
    }


    private List<Hypothesis> generateChildren(Instance instance, List<Hypothesis> current, Hypothesis h)
    {
        List<Hypothesis> children = new ArrayList<>();
        int firstIndexOf1InH = h.getBinaryRep().indexOf(1);
        for (int i = 0; i < firstIndexOf1InH; i++)
        {
            Hypothesis h1 = new Hypothesis(h.getBinaryRep());
            h1.getBinaryRep().set(i, 1);
            setFields(instance, h1);
            h.propagate(h1);
            Hypothesis h2i = h1.initialPredecessor(h);
            Hypothesis h2f = h1.finalPredecessor(h);
            int h2iIndex = findInitial(current, h2i);
            int h2fIndex = findFinal(current, h2iIndex, h2f);
            int counter = 0;
            if (h2iIndex > -1 && h2fIndex > -1)
            {
                for (int j = h2iIndex; j <= h2fIndex; j++)
                {
                    if (current.get(j).hammingDist(h1) == 1)
                    {
                        current.get(j).propagate(h1);
                        counter++;
                    }
                }
            }
            if (counter == h.cardinality())
                children.add(h1);
        }
        return children;
    }

    private int findFinal(List<Hypothesis> current, int h2iIndex, Hypothesis h2f)
    {
        if (h2iIndex != -1)
        {
            int currentSize = current.size();
            for (int i = h2iIndex; i < currentSize; i++)
            {
                if (current.get(i).equals(h2f))
                    return i;
            }
        }
        return -1;
    }

    private int findInitial(List<Hypothesis> current, Hypothesis h2i) {
        return current.indexOf(h2i);
    }

    private List<Hypothesis> generateSingletons(Instance instance, Hypothesis h) {
        List<Hypothesis> children = new ArrayList<>();
        int matrix1Size = instance.getInputMatrix1().size();
        for (int i = 0; i < matrix1Size; i++)
        {
            Hypothesis h1 = new Hypothesis(h.getBinaryRep());
            h1.getBinaryRep().set(i, 1);
            setFieldsSingleton(instance, h1);
            children.add(h1);
        }
        return children;
    }

    private void setFields(Instance instance, Hypothesis h)
    {
        h.setHitVector(Collections.nCopies(instance.getInputMatrix1().getFirst().size(), 0));
    }

    private void setFieldsSingleton(Instance instance, Hypothesis singleton)
    {
        singleton.setHitVector(instance.getInputMatrix1().get(singleton.getBinaryRep().indexOf(1)));
    }
}
