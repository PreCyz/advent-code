package year2015;

import base.DecBase;
import utils.Utils;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class Main2015 {

    public static void main(String[] args) {
        Path inputsDir = Paths.get("", "inputs", "2015");
        if (args.length > 0) {
            inputsDir = Paths.get("", args);
        }

        List<DecBase> adventTasks = List.of(
//                new Dec1(Utils.getFilePath(inputsDir, "dec_1.txt")),
//                new Dec2(Utils.getFilePath(inputsDir, "dec_2.txt")),
//                new Dec3(Utils.getFilePath(inputsDir, "dec_3.txt")),
//                new Dec4(Utils.getFilePath(inputsDir, "dec_4.txt")),
//                new Dec5(Utils.getFilePath(inputsDir, "dec_5.txt")),
                new Dec6(Utils.getFilePath(inputsDir, "dec_6.txt"))
        );

        DecBase.runTasks(adventTasks);
    }
}

