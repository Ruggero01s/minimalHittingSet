import java.io.IOException;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Lock;

public class Main {

    static Hypothesis h0;
    static Instance instance;
    static String basePath = "benchmarks/benchmarks1/";
    static String fileName = "74L85.000.matrix";
    static int maxCardExplored = 0;
    static Lock lock = new ReentrantLock();

    /*public static void main(String[] args) {
        Reader r = new Reader();
        File dir = new File(basePath);
        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
				fileName = file.getName();
                Writer w = new Writer(fileName);
                try {
                    instance = r.readInstance(basePath + fileName);
                    instance.setSolutions(calculateMHS());
                    w.writeOut(instance);
                } catch (IOException e) {
                    try {
                        w.write(e.getMessage());
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }

    }*/

    public static void main(String[] args) {
        Writer.setUp(fileName);
        // KeyStopper stopper = new KeyStopper();
        //stopper.start();
        try {
            startComputation();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void startComputation() throws IOException {
        instance = Reader.readInstance(basePath + fileName);
        instance.setSolutions(calculateMHS());
        Writer.writeOut(Main.instance, false);
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
                //h.reCalcHitVector(instance);
                if (h.isSolution()) {
                    solutions.add(h);
                    current.remove(h);
                    i--;
                } else if (h.isNullSolution()) {
                    next.addAll(generateChildren(current, h));
                } else if (h.getBinaryRep().getFirst() != 1) {
                    Hypothesis h2 = globalInitial(h);
                    i -= removeAllBiggerHypothesis(current, h2);
                    Hypothesis hp = current.getFirst();
                    if (!hp.equals(h) && h.cardinality() < instance.getN().getFirst().size())
                        next = new ArrayList<>(merge(next, generateChildren(current, h)));
                }
            }
            current = new ArrayList<>(next);
            lock.lock();
            try {
                instance.getPerLevelHypotesis().add(current.size());
                maxCardExplored++;
            } finally {
                lock.unlock();
            }
        } while (!current.isEmpty());
        return solutions;
    }

    private static List<Hypothesis> generateChildren(List<Hypothesis> current, Hypothesis h) {
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
            Hypothesis hp = current.getFirst();
            for (int i = 0; i < h.getBinaryRep().indexOf(1); i++) {
                Hypothesis h1 = new Hypothesis(h);
                h1.getBinaryRep().set(i, 1);
                setFields(h1);
                propagate(h, h1);
                updateHitVector(h1, i);
                Hypothesis h2i = h1.initialPredecessor(h);
                Hypothesis h2f = h1.finalPredecessor(h);
                int counter = 0;

                /*while ((hp.equals(h2i) || h2i.isGreater(hp)) && (hp.isGreater(h2f) || hp.equals(h2f))) {
                    if (hammDist(hp, h1) == 1 && hammDist(hp, h) == 2) {
                        propagate(hp, h1);
                        counter++;
                    }
                    int hpIndex = current.indexOf(hp) + 1;
                    if (hpIndex < current.size()) {
                        hp = current.get(hpIndex);
                    }
                }
                if (counter == h.cardinality()) {
                    children.add(h1);
                //System.out.println("    Generated child: " + h1.getBinaryRep());
                //} else {
                //System.out.println("    Skipped child: " + h1.getBinaryRep());
                }*/

                children.add(h1);
            }
            return children;
        }
    }

    private static void updateHitVector(Hypothesis h1, int i) {
        for (int k = 0; k < instance.getN1().getFirst().size(); k++) {
            if (instance.getN1().get(i).get(k) == 1) {
                h1.getHitVector().set(k, 1);
            }
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


    private static int removeAllBiggerHypothesis(List<Hypothesis> current, Hypothesis h2) {
        //todo current.removeIf(h -> h.isGreater(h2));
        int h2index = current.indexOf(h2);
        int counter = 0;
        for (int i = 0; i < h2index; i++) {
            Hypothesis h = current.get(i);
            if (h.isGreater(h2)) {
                current.remove(i);
                i--;
                counter++;
            }
        }
        return counter;
    }

    private static Hypothesis globalInitial(Hypothesis h) {
        Hypothesis globalInitial = new Hypothesis(h);

        globalInitial.getBinaryRep().set(h.getBinaryRep().lastIndexOf(1), 0);
        globalInitial.getBinaryRep().set(h.getBinaryRep().getFirst(), 1);

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