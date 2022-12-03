package year2022;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        List<DecBase> adventTasks = List.of(
                new Dec1("C:\\work\\workspace\\advent-code\\inputs\\dec_1.txt"),
                new Dec2("C:\\work\\workspace\\advent-code\\inputs\\dec_2.txt"),
                new Dec3("C:\\work\\workspace\\advent-code\\inputs\\dec_3.txt")
        );

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

