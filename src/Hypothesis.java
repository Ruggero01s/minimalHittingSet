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
        this.hitVector = new ArrayList<>(hitVector);;
    }

    public Hypothesis(List<Integer> binaryRep)
    {
        this.binaryRep = new ArrayList<>(binaryRep);
        //todo calcHitVector???
    }

    public Hypothesis(Hypothesis h)
    {
        this.binaryRep = new ArrayList<>(h.getBinaryRep());
        this.hitVector = new ArrayList<>(h.getHitVector());
    }

    public List<Integer> getBinaryRep() {
        return binaryRep;
    }

    public void setBinaryRep(List<Integer> binaryRep) {
        this.binaryRep = new ArrayList<>(binaryRep);
    }

    public List<Integer> getHitVector() {
        return hitVector;
    }

    public void setHitVector(List<Integer> hitVector) {
        this.hitVector = hitVector;
    }

    public boolean isGreater (Hypothesis hypothesis)
    {
        if(this.equals(hypothesis))
            return false;

        for (int i = 0; i < this.binaryRep.size(); i++)
        {
            if(this.binaryRep.get(i) > hypothesis.binaryRep.get(i))
                return true;
            if(this.binaryRep.get(i) < hypothesis.binaryRep.get(i))
                return false;
        }

        return false;
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

        for (int i = 0; i < this.binaryRep.size(); i++)
            if(this.binaryRep.get(i) == 1)
                cardinality++;

        return cardinality;
    }
}
