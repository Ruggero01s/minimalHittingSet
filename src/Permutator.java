import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Permutator
{
    private static boolean permuteRows = true;
    private static boolean permuteCols = true;

    private static String basePath = "benchmarks/benchmarks1/";
    private static String fileName = "74L85.000.matrix";
    private static Instance instanceToPermute;
    private static List<Permutation> permutations = new ArrayList<>();
    private static final int numberOfPermutations = 10;
    private static Writer writer;

    public static void setUp(boolean permRows, boolean permCols){
        permuteRows = permRows;
        permuteCols = permCols;
    }

    public static void main(String[] args) {
        writer = new Writer(fileName);
        try {
            instanceToPermute = Reader.readInstance(basePath, fileName);
        } catch (IOException e) {
            try {
                writer.write(";;; Error reading file ("+basePath+ fileName +"): "+ e.getMessage());
            } catch (IOException ex) {
                e.printStackTrace();
            }
        }
        for(int i = 0; i < numberOfPermutations; i++){
            Permutation permutedInstance = permute(instanceToPermute);
            permutedInstance.setInstanceName(generateName(permutedInstance, i));
            permutations.add(permutedInstance);
        }
        for (Permutation permutation : permutations) {
            try {
                writer.writePermutation(permutation);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static String generateName(Permutation perm, int i) {
        return perm.getOriginInstance().substring(perm.getOriginInstance().lastIndexOf("/")+1, perm.getOriginInstance().lastIndexOf(".")+1)+(i+1)+".matrix";
    }

    public static Permutation permute(Instance instance)
    {
        Random rand = new Random();

        List<Integer> newColumns = new ArrayList<>();
        for (int i = 0; i < instance.getInputMatrix().size(); i++)
            newColumns.add(i);

        List<Integer> newRows = new ArrayList<>();
        for (int i = 0; i < instance.getInputMatrix().getFirst().size(); i++)
            newRows.add(i);

        if (!permuteRows && !permuteCols)
            return new Permutation(new Instance(instance.instanceName, instance.getInputMatrix()),basePath+ fileName, newColumns, newRows);

        if (permuteCols)
            Collections.shuffle(newColumns, rand);

        if (permuteRows)
            Collections.shuffle(newRows, rand);

        List<List<Integer>> newN = new ArrayList<>();
        for (Integer newColumn : newColumns)
        {
            List<Integer> newNRow = new ArrayList<>();
            for (Integer newRow : newRows)
                newNRow.add(instance.getInputMatrix().get(newColumn).get(newRow));

            newN.add(newNRow);
        }
        Instance newInstance = new Instance(instance.instanceName, newN);
        //System.out.println(instance.inputMatrixToString());
        //System.out.println(newInstance.inputMatrixToString());
        return new Permutation(newInstance, basePath+ fileName, newColumns, newRows);
    }
}
