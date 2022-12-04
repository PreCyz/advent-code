package year2015;

import base.DecBase;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class Main2015 {

    public static void main(String[] args) {
        Path inputs = Paths.get("", "inputs", "2015");

        List<DecBase> adventTasks = List.of(
                //new Dec1(Paths.get(inputs.toString(),"dec_1.txt").toAbsolutePath().normalize().toString()),
                //new Dec2(Paths.get(inputs.toString(),"dec_2.txt").toAbsolutePath().normalize().toString()),
                new Dec3(Paths.get(inputs.toString(),"dec_3.txt").toAbsolutePath().normalize().toString())

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

