import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Reader {

    public List<String> M = new ArrayList<>();
    private List<List<Integer>> N = new ArrayList<>();

    public Instance readInstance(String filePath){
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.contains(";;; Map"))
                {
                    M = new ArrayList<>(parseMapLine(line));
                } else if (line.contains(";;;")) {
                    continue;
                }
                else {
                    List<Integer> row = createRow(line);
                    if (row != null)
                        N.add(row);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Instance(M,N);
    }

    private List<Integer> createRow(String line) throws IOException {
        List<Integer> row = new ArrayList<>();
        if (line.isBlank())
            return null;
        line = line.replaceAll("\\s+","");
        char[] chars = line.toCharArray();

        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == '-')
                return row;
            else {
                row.add(Character.getNumericValue(chars[i]));
            }
        }

        throw new IOException();
    }

    private static List<String> parseMapLine(String line) {
        // Crea una lista con una dimensione iniziale basata sul numero massimo trovato negli indici
        List<String> resultList = new ArrayList<>();

        // Pattern per trovare le coppie indice(valore)
        Pattern pattern = Pattern.compile("(\\d+)\\(([^)]+)\\)");
        Matcher matcher = pattern.matcher(line);

        while (matcher.find()) {
            int index = Integer.parseInt(matcher.group(1))-1;
            String value = matcher.group(2);

            // Assicura che la lista abbia abbastanza spazio per l'indice corrente
            while (resultList.size() <= index) {
                resultList.add(null);
            }

            // Aggiunge il valore all'indice specificato
            resultList.set(index, value);
        }

        return resultList;
    }



}
