import java.io.FileInputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Scanner;

abstract class DecBase {
    protected LinkedList<Integer> inputIntegers;
    private final String fileName;

    protected DecBase(String fileName) {
        this.fileName = fileName;
    }

    protected DecBase() {
        this("");
    }

    protected DecBase readInput() throws IOException {
        System.out.printf("Reading input from [%s]%n", fileName);
        inputIntegers = new LinkedList<>();
        try (Scanner scanner = new Scanner(new FileInputStream(fileName))) {
            while (scanner.hasNext()) {
                final String nextLine = scanner.nextLine();
                if (nextLine == null || "".equals(nextLine)) {
                    inputIntegers.add(null);
                } else {
                    inputIntegers.add(Integer.parseInt(nextLine));
                }
            }
        }
        return this;
    }

    public String getFileName() {
        return fileName;
    }

    protected abstract DecBase readDefaultInput();
    protected abstract void calculate();

}
