package base;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

public abstract class DecBase implements Runnable {
    protected LinkedList<String> inputStrings = new LinkedList<>();
    private final String fileName;

    protected DecBase(String fileName) {
        this.fileName = fileName;
    }

    protected DecBase() {
        this("");
    }

    public DecBase readInput() throws IOException {
        System.out.printf("%nReading input from [%s]%n", getFileName());
        inputStrings = new LinkedList<>();
        try (Scanner scanner = new Scanner(new FileInputStream(getFileName()))) {
            while (scanner.hasNext()) {
                final String nextLine = scanner.nextLine();
                if (nextLine != null && !"".equals(nextLine)) {
                    inputStrings.add(nextLine);
                }
            }
        }
        return this;
    }

    public String getFileName() {
        return fileName;
    }

    @Override
    public void run() {
        System.out.printf("Calculating ... %s%n", this.getClass().getSimpleName());
        calculatePart1();
        calculatePart2();
    }

    protected void calculatePart2() {
        System.out.println("No need to implement method calculatePart2.");
    }

    public abstract DecBase readDefaultInput();
    protected abstract void calculatePart1();

    public static void runTasks(List<DecBase> adventTasks) {
        for (DecBase task : adventTasks) {
            try {
                System.out.printf("%nStarting new task %s%n", task.getClass().getSimpleName());
                System.out.printf("*******************************%n");
                task.readDefaultInput().run();
                task.readInput().run();
                System.out.printf("*******************************%n");
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }
    }
}
