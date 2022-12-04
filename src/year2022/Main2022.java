package year2022;

import base.DecBase;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class Main2022 {

    public static void main(String[] args) {
        Path inputs = Paths.get("", "inputs", "2022");

        List<DecBase> adventTasks = List.of(
                //new Dec1(Paths.get(inputs.toString(),"dec_1.txt").toAbsolutePath().normalize().toString()),
                //new Dec2(Paths.get(inputs.toString(),"dec_2.txt").toAbsolutePath().normalize().toString()),
                //new Dec3(Paths.get(inputs.toString(),"dec_3.txt").toAbsolutePath().normalize().toString()),
                //new Dec4(Paths.get(inputs.toString(),"dec_4.txt").toAbsolutePath().normalize().toString()),
                new Dec5(Paths.get(inputs.toString(),"dec_5.txt").toAbsolutePath().normalize().toString())
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

