import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Hypothesis
{
    private List<Integer> binaryRep;
    private List<Integer> hitVector;

    public Hypothesis(List<Integer> binaryRep, List<Integer> hitVector)
    {
        this.binaryRep = new ArrayList<>(binaryRep);
        this.hitVector = new ArrayList<>(hitVector);
    }

    public Hypothesis(List<Integer> binaryRep)
    {
        this.binaryRep = new ArrayList<>(binaryRep);
    }

    public Hypothesis(Hypothesis h)
    {
        this.binaryRep = new ArrayList<>(h.getBinaryRep());
        this.hitVector = new ArrayList<>(h.getHitVector());
    }

    public List<Integer> getBinaryRep()
    {
        return binaryRep;
    }

    public void setBinaryRep(List<Integer> binaryRep)
    {
        this.binaryRep = new ArrayList<>(binaryRep);
    }

    public List<Integer> getHitVector()
    {
        return hitVector;
    }

    public void setHitVector(List<Integer> hitVector)
    {
        this.hitVector = new ArrayList<>(hitVector);
    }

    public boolean isGreater (Hypothesis hypothesis)
    {
        for (int i = 0; i < this.binaryRep.size(); i++)
        {
            if(this.binaryRep.get(i) > hypothesis.binaryRep.get(i))
                return true;
            if(this.binaryRep.get(i) < hypothesis.binaryRep.get(i))
                return false;
        }

        return false;
    }

    public boolean isGreaterOrEqual(Hypothesis hypothesis)
    {
        if (this.equals(hypothesis))
            return true;
        else
            return this.isGreater(hypothesis);
    }

    public boolean isSolution()
    {
        return !this.hitVector.contains(0);
    }

    public boolean isNullSolution()
    {
        return !this.getBinaryRep().contains(1);
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
        result = 31 * result + hitVector.hashCode();
        return result;
    }

    public int cardinality()
    {
        int cardinality = 0;

        int binaryRepSize = this.binaryRep.size();
        for (int i = 0; i < binaryRepSize; i++)
            if(this.binaryRep.get(i) == 1)
                cardinality++;

        return cardinality;
    }

    public void propagate(Hypothesis h1)
    {
        int hitVectorSize = this.hitVector.size();
        for (int i = 0; i < hitVectorSize; i++) {
            if (this.hitVector.get(i) == 1)
                h1.hitVector.set(i, 1);
        }
    }

    public int hammingDist(Hypothesis h)
    {
        int distance = 0;
        int binaryRepSize = h.binaryRep.size();
        for (int i = 0; i < binaryRepSize; i++)
            if (!h.binaryRep.get(i).equals(this.binaryRep.get(i)))
                distance++;

        return distance;
    }

    public Hypothesis initialPredecessor(Hypothesis generatingParent)
    {
        Hypothesis possiblePredecessor = new Hypothesis(this.binaryRep);
        boolean first = true;
        Hypothesis secondPossiblePredecessor = new Hypothesis(this.binaryRep);
        for(int i=possiblePredecessor.getBinaryRep().size()-1; i>=0; i--)
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

        return possiblePredecessor.equals(generatingParent) ? secondPossiblePredecessor : possiblePredecessor;
    }

    public Hypothesis finalPredecessor(Hypothesis generatingParent)
    {
        Hypothesis possiblePredecessor = new Hypothesis(this.binaryRep);
        boolean first = true;
        Hypothesis secondPossiblePredecessor = new Hypothesis(this.binaryRep);
        int binaryRepSize = possiblePredecessor.getBinaryRep().size();
        for(int i=0; i<binaryRepSize; i++)
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

        return possiblePredecessor.equals(generatingParent) ? secondPossiblePredecessor : possiblePredecessor;
    }

    public Hypothesis globalInitial()
    {
        Hypothesis newH = new Hypothesis(this);

        newH.binaryRep.set(0,1);
        newH.binaryRep.set(newH.binaryRep.lastIndexOf(1),0);

        return newH;
    }
}
