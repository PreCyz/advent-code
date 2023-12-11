package year2023;

import base.DecBase;
import utils.Utils;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class Main2023 {

    public static void main(String[] args) {
        Path inputsDir = Paths.get("", "inputs", "2023");
        if (args.length > 0) {
            System.out.printf("Args taken from the launch arguments: %s%n", Arrays.asList(args));
            inputsDir = Paths.get("", args);
        }

        List<DecBase> adventTasks = List.of(
//                new Dec1(Utils.getFilePath(inputsDir, "dec_1.txt")),
//                new Dec2(Utils.getFilePath(inputsDir, "dec_2.txt")),
//                new Dec3(Utils.getFilePath(inputsDir, "dec_3.txt")),
//                new Dec4(Utils.getFilePath(inputsDir, "dec_4.txt")),
//                new Dec5(Utils.getFilePath(inputsDir, "dec_5.txt"))
//                new Dec6(Utils.getFilePath(inputsDir, "dec_6.txt")),
//                new Dec7(Utils.getFilePath(inputsDir, "dec_7.txt")),
//                new Dec8(Utils.getFilePath(inputsDir, "dec_8.txt")),
//                new Dec9(Utils.getFilePath(inputsDir, "dec_9.txt")),
//                new Dec10(Utils.getFilePath(inputsDir, "dec_10.txt")),
                new Dec11(Utils.getFilePath(inputsDir, "dec_11.txt"))
//                new Dec12(Utils.getFilePath(inputsDir, "dec_12.txt")),
//                new Dec13(Utils.getFilePath(inputsDir, "dec_13.txt")),
//                new Dec14(Utils.getFilePath(inputsDir, "dec_14.txt")),
//                new Dec15(Utils.getFilePath(inputsDir, "dec_15.txt")),
//                new Dec16(Utils.getFilePath(inputsDir, "dec_16.txt")),
//                new Dec19(Utils.getFilePath(inputsDir, "dec_19.txt")),
//                new Dec20(Utils.getFilePath(inputsDir, "dec_20.txt"))
//                new Dec20(Utils.getFilePath(inputsDir, "dec_21.txt"))
//                new Dec20(Utils.getFilePath(inputsDir, "dec_22.txt"))
//                new Dec20(Utils.getFilePath(inputsDir, "dec_23.txt"))
//                new Dec20(Utils.getFilePath(inputsDir, "dec_24.txt"))
        );

        DecBase.runTasks(adventTasks);
    }
}

