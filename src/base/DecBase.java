package base;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

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
                if (nextLine != null && !nextLine.isEmpty()) {
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
        LocalDateTime start = LocalDateTime.now();
        calculatePart1();
        Duration duration = Duration.between(start, LocalDateTime.now());
        System.out.printf("Duration %s[m] %s[s]%n", duration.toMinutesPart(), duration.toSecondsPart());

        start = LocalDateTime.now();
        calculatePart2();
        duration = Duration.between(start, LocalDateTime.now());
        System.out.printf("Duration %d[m]:%d[s]:%d[milli]%n",
                duration.toMinutesPart(), duration.toSecondsPart(), duration.toMillisPart()
        );
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
