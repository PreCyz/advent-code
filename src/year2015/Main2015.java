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
                //new Dec3(Paths.get(inputs.toString(),"dec_3.txt").toAbsolutePath().normalize().toString()),
                new Dec4(Paths.get(inputs.toString(),"dec_4.txt").toAbsolutePath().normalize().toString())

        );

        DecBase.runTasks(adventTasks);
    }
}

