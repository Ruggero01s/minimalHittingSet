public class Main {
    public static void main(String[] args) {
       Reader r = new Reader();
       Instance i = r.readInstance("benchmarks/benchmarks1/74L85.000.matrix");
       System.out.println(i);
    }
}