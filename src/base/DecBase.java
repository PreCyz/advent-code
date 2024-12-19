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
        calculatePart1();
        printDuration(start);

        start = LocalDateTime.now();
        calculatePart2();
        printDuration(start);
    }

    private void printDuration(LocalDateTime start) {
        Duration duration = Duration.between(start, LocalDateTime.now());
        if (duration.toMinutes() > 0) {
            System.out.printf("Duration %d[m]:%d[s]:%d[ms]%n",
                    duration.toMinutesPart(), duration.toSecondsPart(), duration.toMillisPart()
            );
        } else if (duration.toSeconds() > 0) {
            System.out.printf("Duration %d[s]:%d[ms]%n", duration.toSecondsPart(), duration.toMillisPart());
        } else if (duration.toMillis() > 0) {
            System.out.printf("Duration %d[ms]%n", duration.toMillisPart());
        } else {
            if (duration.toNanosPart() > 0) {
                System.out.printf("Duration %d[ns]%n", duration.toNanosPart());
            }
        }
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
                task.readDefaultInput().run();
                task.downloadInput(cookieSession);
                task.readInput().run();
                System.out.printf("*******************************%n");
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }
    }
}
