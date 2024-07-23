public class Main {

    private static boolean permuteRows = true;
    private static boolean permuteCols = true;

    public static void main(String[] args)
    {
        KeyStopper keyStopper = new KeyStopper();
        keyStopper.start(permuteRows,permuteCols);
    }
}