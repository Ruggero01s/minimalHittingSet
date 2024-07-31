import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Reader {

    private static List<List<Integer>> input = new ArrayList<>();


    public static Instance readInstance(String basePath, String fileName) throws IOException {
        input = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(basePath + fileName));
        String line;
        List<Integer> newCols = new ArrayList<>();
        List<Integer> newRows = new ArrayList<>();
        while ((line = br.readLine()) != null) {
            if (line.contains(";;;")) {
                continue;
            } else if (line.contains("Cols")) {
                newCols = parsePermCols(line);
            } else if (line.contains("Rows")) {
                newRows = parsePermRows(line);
            } else {
                List<Integer> row = createRow(line);
                if (row != null)
                    input.add(row);
            }
        }
        input = invertMatrix(input);
        if (newCols.isEmpty() && newRows.isEmpty())
            return new Instance(fileName, input);
        else {
            String[] parts = fileName.split("\\.");
            StringBuilder originInstanceName = new StringBuilder();
            for (int i = 0; i < parts.length-2; i++) {
                originInstanceName.append(parts[i]).append(".");
            }
            originInstanceName.append("matrix");
            return new Permutation(new Instance(fileName, input), originInstanceName.toString(), newCols, newRows);
        }
    }

    public static List<List<Integer>> invertMatrix(List<List<Integer>> matrix) {
        if (matrix == null || matrix.isEmpty()) {
            throw new IllegalArgumentException("Matrix cannot be null or empty");
        }

        int numRows = matrix.size();
        int numCols = matrix.getFirst().size();

        List<List<Integer>> invertedMatrix = new ArrayList<>();

        // Initialize the inverted matrix with empty lists
        for (int col = 0; col < numCols; col++) {
            invertedMatrix.add(new ArrayList<>());
        }

        // Fill the inverted matrix
        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
                invertedMatrix.get(col).add(matrix.get(row).get(col));
            }
        }

        return invertedMatrix;
    }

    private static List<Integer> createRow(String line) throws IOException {
        List<Integer> row = new ArrayList<>();
        if (line.isBlank())
            return null;
        line = line.replaceAll("\\s+", "");
        char[] chars = line.toCharArray();

		for (char aChar : chars)
		{
			if (aChar=='-')
				return row;
			else
				row.add(Character.getNumericValue(aChar));
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
            int index = Integer.parseInt(matcher.group(1)) - 1;
            String value = matcher.group(2);

            // Assicura che la lista abbia abbastanza spazio per l'indice corrente
            while (resultList.size() <= index)
            {
                resultList.add(null);
            }

            // Aggiunge il valore all'indice specificato
            resultList.set(index, value);
        }

        return resultList;
    }

    private static List<Integer> parsePermCols(String line) {
        List<Integer> colsList = new ArrayList<>();

        String cleanedInput = line.replace("Cols: ", "").replace("[", "").replace("]", "");

        String[] stringArray = cleanedInput.split(", ");

        for (String s : stringArray) {
            colsList.add(Integer.parseInt(s));
        }
        return colsList;
    }

    private static List<Integer> parsePermRows(String line) {
        List<Integer> rowsList = new ArrayList<>();

        String cleanedInput = line.replace("Rows: ", "").replace("[", "").replace("]", "");

        String[] stringArray = cleanedInput.split(", ");

        for (String s : stringArray) {
            rowsList.add(Integer.parseInt(s));
        }
        return rowsList;
    }

}
