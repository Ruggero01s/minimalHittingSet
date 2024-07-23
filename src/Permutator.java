import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Permutator
{

    public Instance permute(Instance instance, boolean permuteRows, boolean permuteColumns)
    {
        if (!permuteRows && !permuteColumns)
            return new Instance(instance.getM(), instance.getN());

        List<Integer> newColumns = new ArrayList<>();
        for (int i = 0; i < instance.getM().size(); i++)
            newColumns.add(i);

        if (permuteColumns)
            Collections.shuffle(newColumns, new Random());

        List<Integer> newRows = new ArrayList<>();
        for (int i = 0; i < instance.getN().getFirst().size(); i++)
            newRows.add(i);

        if (permuteRows)
            Collections.shuffle(newRows, new Random());


        List<String> newM = new ArrayList<>();
        List<List<Integer>> newN = new ArrayList<>();
        //todo fix shuffling
        for (int i = 0; i < newColumns.size(); i++) {
            newM.add(instance.getM().get(newColumns.get(i)));
            List<Integer> newNRow = new ArrayList<>();
            for (int j = 0; j < newRows.size(); j++) {
                newNRow.add(instance.getN().get(newColumns.get(i)).get(newRows.get(j)));
            }
            newN.add(newNRow);
        }


        return new Instance(newM, newN);
    }
}
