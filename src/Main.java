import java.util.*;

public class Main {

    static Hypothesis h0;
    static Instance instance;
    static String basePath = "benchmarks/benchmarks1/";
    static String fileName = "74L85.016.matrix";

    public static void main(String[] args) {
        Reader r = new Reader();
        Writer w = new Writer();
        instance = r.readInstance(basePath + fileName);

        instance.setSolutions(calculateMHS());


        w.writeOut(fileName, instance);

        System.out.println(instance);
    }

     private static List<Hypothesis> calculateMHS() {
        h0 = new Hypothesis(new ArrayList<>(Collections.nCopies(instance.getM1().size(), 0)));
        setFields(h0);
        List<Hypothesis> current = new ArrayList<>(List.of(h0));
        List<Hypothesis> solutions = new ArrayList<>();
        do {
            List<Hypothesis> next = new ArrayList<>();
            for (int i = 0; i < current.size(); i++) {
                Hypothesis h = current.get(i);
                if (h.isSolution()) {
                    solutions.add(h);
                    current.remove(h);
                    i--;
                } else if (h.isNullSolution()) {
                    next.addAll(generateChildren(current, h));
                } else if (h.getBinaryRep().getFirst() != 1) {
                    Hypothesis h2 = globalInitial(h);
                    current = removeAllBiggerHypothesis(current, h2);
                    Hypothesis hp = current.getFirst();
                    if (!hp.equals(h))
                        next = new ArrayList<>(merge(next, generateChildren(current, h)));
                }
            }
            current = new ArrayList<>(next);
        } while (!current.isEmpty());

        return solutions;
    }

   /* private static List<Hypothesis> calculateMHS() {
        h0 = new Hypothesis(new ArrayList<>(Collections.nCopies(instance.getM1().size(), 0)));
        setFields(h0);
        List<Hypothesis> current = new ArrayList<>(List.of(h0));
        List<Hypothesis> solutions = new ArrayList<>();
        while (!current.isEmpty()) {
            List<Hypothesis> next = new ArrayList<>();
            Iterator<Hypothesis> iterator = current.iterator();
            while (iterator.hasNext()) {
                Hypothesis h = iterator.next();
                if (h.isSolution()) {
                    solutions.add(h);
                    iterator.remove();
                } else if (h.isNullSolution()) {
                    next.addAll(generateChildren(current, h));
                } else if (h.getBinaryRep().getFirst() != 1) { // Updated to get first element correctly
                    Hypothesis h2 = globalInitial(h);
                    removeAllBiggerHypothesis(current, h2);
                    if (!current.isEmpty() && !current.getFirst().equals(h)) {
                        next = new ArrayList<>(merge(next, generateChildren(current, h)));
                    }
                }
            }
            current = new ArrayList<>(next);
        }
        return solutions;
    }*/

    private static List<Hypothesis> generateChildren(List<Hypothesis> current, Hypothesis h) {
        List<Hypothesis> copyCurrent = new ArrayList<>(current);
        List<Hypothesis> children = new ArrayList<>();
        if (h.isNullSolution()) {
            for (int i = 0; i < instance.getM1().size(); i++) {
                Hypothesis h1 = new Hypothesis(h);
                h1.getBinaryRep().set(i, 1);
                setFields(h1);
                children.add(h1);
            }
            return children;
        } else {
            Hypothesis hp = copyCurrent.getFirst();
            for (int i = 0; i < h.getBinaryRep().indexOf(1); i++) {
                Hypothesis h1 = new Hypothesis(h);
                h1.getBinaryRep().set(i, 1);
                setFields(h1);
                propagate(h, h1);
                Hypothesis h2i = h1.initialPredecessor(h);
                Hypothesis h2f = h1.finalPredecessor(h);
                int counter = 0;
                while ((hp.getBinaryRep().equals(h2i.getBinaryRep()) || h2i.isGreater(h1)) && (hp.isGreater(h2f) || hp.getBinaryRep().equals(h2f.getBinaryRep()))) {
                    if (hammDist(hp, h1) == 1 && hammDist(hp, h) == 2) {
                        propagate(hp, h1);
                        counter++;
                    }
                    hp = copyCurrent.get(copyCurrent.indexOf(hp) + 1);
                }
                if (counter == h.cardinality())
                    children.add(h1);
            }
            return children;
        }
    }

    private static int hammDist(Hypothesis h1, Hypothesis h2) {
        int hammingDistance = 0;

        for (int i = 0; i < h1.getBinaryRep().size(); i++) {
            if (!h1.getBinaryRep().get(i).equals(h2.getBinaryRep().get(i))) {
                hammingDistance++;
            }
        }

        return hammingDistance;
    }


    private static List<Hypothesis> merge(List<Hypothesis> hlList1, List<Hypothesis> hlList2) {
        List<Hypothesis> merged = new ArrayList<>(hlList1);
        merged.addAll(hlList2);
        Comparator<Hypothesis> hypothesisComparator = Main::isGreater;
        merged.sort(hypothesisComparator);
        return merged.reversed();
    }

    private static int isGreater(Hypothesis h1, Hypothesis h2) {
        if (h1.getBinaryRep().equals(h2.getBinaryRep()))
            return 0;
        return h1.isGreater(h2) ? 1 : -1;
    }


    private static void removeAllBiggerHypothesis(List<Hypothesis> current, Hypothesis h2) {
        List<Hypothesis> toRemove = new ArrayList<>();

        for (Hypothesis hypothesis : current)
        {
            if (hypothesis.isGreater(h2)) {
                toRemove.add(hypothesis);
            }
        }
        current.removeAll(toRemove);
    }

    private static Hypothesis globalInitial(Hypothesis h) {
        Hypothesis globalInitial = new Hypothesis(h);

        globalInitial.getBinaryRep().set(h.getBinaryRep().lastIndexOf(1), 0);
        globalInitial.getBinaryRep().set(h.getBinaryRep().indexOf(0), 1);

        return globalInitial;
    }


    private static void setFields(Hypothesis h) {
        if (h.isImmediateSuccessorOf(h0))
            h.setHitVector(instance.getN1().get(h.getBinaryRep().indexOf(1))); //matrix column relevant to singleton h
        else
            h.setHitVector(new ArrayList<>(Collections.nCopies(instance.getN1().getFirst().size(), 0)));

    }


    private static void propagate(Hypothesis h, Hypothesis h1) {
        for (int i = 0; i < h.getHitVector().size(); i++) {
            if (h.getHitVector().get(i) == 1)
                h1.getHitVector().set(i, 1);
        }
    }


}