import java.util.*;

public class Solver {

    public List<Hypothesis> all = new ArrayList<>();

    public List<Hypothesis> solve(Instance instance)
    {

        Hypothesis h0 = new Hypothesis(Collections.nCopies(instance.getM1().size(),0));
        setFields(instance, h0);
        List<Hypothesis> current = new ArrayList<>();
        current.add(h0);

        List<Hypothesis> solutions = new ArrayList<>();
        do
        {
            List<Hypothesis> next = new ArrayList<>();
            for (int i = 0; i < current.size(); i++)
            {
                Hypothesis h = current.get(i);
                all.add(new Hypothesis(h));
                //h.reCalcHitVector(instance);
                if (h.isSolution())
                {
                    solutions.add(new Hypothesis(h));
                    current.remove(i);
                    i--;
                }
                else if (h.isNullSolution())
                {
                    next.addAll(generateChildren(instance, current, h));
                }
                else if (h.getBinaryRep().indexOf(1) != 0)
                {
                    Hypothesis h2s = h.globalInitial();

                    int preRemoveLength= current.size();
                    removeAllBiggerHypothesis(current, h2s);
                    int postRemoveLength= current.size();
                    i = i - (preRemoveLength - postRemoveLength);

                    Hypothesis hp = current.getFirst();
                    if (!hp.equals(h))
                       next = merge(next, generateChildren(instance, current, h));
                }
            }
            instance.getPerLevelHypothesis().add(current.size());
            instance.setMaxCardExplored(instance.getMaxCardExplored()+1);
            current = next;
        }
        while (!current.isEmpty());
        return solutions;
    }

    private void removeAllBiggerHypothesis(List<Hypothesis> current, Hypothesis h2s)
    {
        for (int i = 0; i < current.size(); i++)
            if (current.get(i).isGreater(h2s)) {
                current.remove(i);
                i--;
            }
        //current.removeIf(hypothesis -> hypothesis.isGreater(h2s));
    }

    private List<Hypothesis> merge(List<Hypothesis> next, List<Hypothesis> hypotheses) {
        next.addAll(hypotheses);
        Comparator<Hypothesis> hypothesisComparator = Solver::compare;
        next.sort(hypothesisComparator);
        return next.reversed();
    }

    private static int compare(Hypothesis h1, Hypothesis h2)
    {
        if(h1.equals(h2))
            return 0;

        return  h1.isGreater(h2) ? 1 : -1;
    }



    private List<Hypothesis> generateChildren(Instance instance, List<Hypothesis> current, Hypothesis h) {
        List<Hypothesis> children = new ArrayList<>();
        if (h.isNullSolution()){
            for (int i = 0; i < instance.getM1().size(); i++) {
                Hypothesis h1 = new Hypothesis(h.getBinaryRep());
                h1.getBinaryRep().set(i,1);
                setFields(instance, h1);
                children.add(h1);
            }
            return children;
        }

        Hypothesis hp = current.getFirst();
        for (int i = 0; i < h.getBinaryRep().indexOf(1); i++) {
            Hypothesis h1 = new Hypothesis(h.getBinaryRep());
            h1.getBinaryRep().set(i,1);
            setFields(instance, h1);
            h.propagate(h1);
            Hypothesis h2i = h1.initialPredecessor(h);
            Hypothesis h2f = h1.finalPredecessor(h);
            int counter = 0;
            /*for (Hypothesis hypothesis : current) {
                if ((h2i.isGreaterOrEqual(hypothesis)) && (hypothesis.isGreaterOrEqual(h2f))){
                    if (hypothesis.hammingDist(h1)==1 && hypothesis.hammingDist(h)==2){
                        hypothesis.propagate(h1);
                        counter++;
                    }
                }
            }*/
            while ((h2i.isGreaterOrEqual(hp)) && (hp.isGreaterOrEqual(h2f))) {
                if (hp.hammingDist(h1)==1 && hp.hammingDist(h)==2){
                    hp.propagate(h1);
                    counter++;
                }
                hp = current.get(current.indexOf(hp)+1);
            }
            if (counter == h.cardinality())
                children.add(h1);
        }
        return children;
    }

    private void setFields(Instance instance, Hypothesis h)
    {
        if (h.hammingDist(new Hypothesis(Collections.nCopies(instance.getM1().size(),0))) == 1)
            h.setHitVector(instance.getN1().get(h.getBinaryRep().indexOf(1)));
        else
            h.setHitVector(Collections.nCopies(instance.getN1().getFirst().size(),0));
    }
}
