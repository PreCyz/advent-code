package base;

import utils.Utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public abstract class DecBase implements Runnable {
    protected LinkedList<String> inputStrings = new LinkedList<>();
    private final int year;
    private final int day;

    protected DecBase(int year, int day) {
        this.year = year;
        this.day = day;
    }

    public DecBase readInput() throws IOException {
        System.out.printf("%nReading input from [%s]%n", getFilePath());
        inputStrings = new LinkedList<>();
        try (Scanner scanner = new Scanner(new FileInputStream(getFilePath().toFile()))) {
            while (scanner.hasNext()) {
                final String nextLine = scanner.nextLine();
                if (nextLine != null && !nextLine.isEmpty()) {
                    inputStrings.add(nextLine);
                }
            }
        }
        return this;
    }

    private String getFileName() {
        return "%d.txt".formatted(day);
    }

    protected Path getFilePath() {
        return Paths.get("", "inputs", String.valueOf(year), getFileName());
    }

    private void downloadInput(String cookieSession) {
        if (!Files.exists(getFilePath())) {
            Utils.downloadInput(year, day, cookieSession);
        }
    }

    @Override
    public void run() {
        System.out.printf("Calculating ... %s%n", this.getClass().getSimpleName());
        LocalDateTime start = LocalDateTime.now();
        //calculatePart1();
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

    public static void runTasks(List<DecBase> adventTasks, String cookieSession) {
        for (DecBase task : adventTasks) {
            try {
                System.out.printf("%nStarting new task %s%n", task.getClass().getSimpleName());
                System.out.printf("*******************************%n");
                //task.readDefaultInput().run();
                task.downloadInput(cookieSession);
                task.readInput().run();
                System.out.printf("*******************************%n");
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }
    }
}
