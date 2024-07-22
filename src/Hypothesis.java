import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Hypothesis
{
    private List<Integer> binaryRep = new ArrayList<>();

    private List<Integer> hitVector = new ArrayList<>();

    public Hypothesis(List<Integer> binaryRep)
    {
        this.binaryRep = new ArrayList<>(binaryRep);
    }

    public Hypothesis(Hypothesis h)
    {
        this.binaryRep = new ArrayList<>(h.binaryRep);
        this.hitVector = new ArrayList<>(h.hitVector);
    }

    public List<Integer> getBinaryRep() {
        return binaryRep;
    }

    public List<Integer> getHitVector() {
        return hitVector;
    }

    public void setHitVector(List<Integer> hitVector) {
        this.hitVector = hitVector;
    }

    public boolean isImmediateSuccessorOf(Hypothesis h)
    {
        if(this.equals(h))
            return false;

        boolean first = true;

        for (int i=0; i<this.binaryRep.size(); i++)
        {
            if(!this.binaryRep.get(i).equals(h.getBinaryRep().get(i)))
            {
                if(!first)
                    return false;

                if(this.binaryRep.get(i) == 1 && h.getBinaryRep().get(i) == 0)
                    first = false;
                else
                    return false;
            }
        }
        return true;
    }

    public boolean isSolution(){
        return !hitVector.contains(0);
    }

    public boolean isNullSolution()
    {
        return !this.binaryRep.contains(1);
    }

    public boolean isGreater(Hypothesis h)
    {
        for (int i = 0; i < this.binaryRep.size(); i++)
        {
            if(this.binaryRep.get(i)>h.getBinaryRep().get(i))
                return true;
            else if(this.binaryRep.get(i)<h.getBinaryRep().get(i))
                return false;
        }
        return false; //The vector are equals
    }

    public Hypothesis initialPredecessor (Hypothesis h)
    {
        Hypothesis possiblePredecessor = new Hypothesis(this.binaryRep);
        boolean first = true;
        Hypothesis secondPossiblePredecessor = new Hypothesis(this.binaryRep);
        for( int i=possiblePredecessor.getBinaryRep().size()-1; i>=0; i--)
        {
            if(possiblePredecessor.getBinaryRep().get(i)==1 && first)
            {
                possiblePredecessor.getBinaryRep().set(i,0);
                first = false;
            }
            else if(secondPossiblePredecessor.getBinaryRep().get(i)==1 && !first)
            {
                secondPossiblePredecessor.getBinaryRep().set(i,0);
                break;
            }
        }

        return possiblePredecessor.getBinaryRep().equals(h.binaryRep) ? secondPossiblePredecessor : possiblePredecessor;
    }

    public Hypothesis finalPredecessor (Hypothesis h)
    {
        Hypothesis possiblePredecessor = new Hypothesis(this.binaryRep);
        boolean first = true;
        Hypothesis secondPossiblePredecessor = new Hypothesis(this.binaryRep);
        for(int i = 0; i < possiblePredecessor.getBinaryRep().size(); i++)
        {
            if(possiblePredecessor.getBinaryRep().get(i)==1 && first)
            {
                possiblePredecessor.getBinaryRep().set(i,0);
                first = false;
            }
            else if(secondPossiblePredecessor.getBinaryRep().get(i)==1 && !first)
            {
                secondPossiblePredecessor.getBinaryRep().set(i,0);
                break;
            }
        }

        return possiblePredecessor.getBinaryRep().equals(h.binaryRep) ? secondPossiblePredecessor : possiblePredecessor;
    }

    public int cardinality(){
        int card = 0;
        for (Integer element : binaryRep) {
            if (element == 1)
                card++;
        }
        return card;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Hypothesis that = (Hypothesis) o;
        return this.binaryRep.equals(that.binaryRep);
    }

    @Override
    public int hashCode() {
        return this.binaryRep.hashCode();
    }

    public void reCalcHitVector(Instance instance) {
        ArrayList<Integer> newHitVector = new ArrayList<>(Collections.nCopies(getHitVector().size(), 0));
        for (int i = 0; i < this.getBinaryRep().size(); i++) {
            if (this.getBinaryRep().get(i)==1){
                    for (int k = 0; k < instance.getN1().getFirst().size(); k++) {
                        if (instance.getN1().get(i).get(k) == 1) {
                            newHitVector.set(k, 1);
                        }
                }
            }
        }
        this.setHitVector(new ArrayList<>(newHitVector));
    }
}
