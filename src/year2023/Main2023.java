package year2023;

import base.DecBase;

import java.util.List;

public class Main2023 {

    public static void main(String[] args) {
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
//                new Dec11(Utils.getFilePath(inputsDir, "dec_11.txt")),
//                new Dec12(Utils.getFilePath(inputsDir, "dec_12.txt")),
//                new Dec13(Utils.getFilePath(inputsDir, "dec_13.txt")),
//                new Dec14(2023),
//                new Dec15(Utils.getFilePath(inputsDir, "dec_15.txt")),
//                new Dec16(Utils.getFilePath(inputsDir, "dec_16.txt")),
//                new Dec17(Utils.getFilePath(inputsDir, "dec_17.txt")),
//                new Dec18(Utils.getFilePath(inputsDir, "dec_18.txt")),
//                new Dec19(2023),
                new Dec20(2023)
//                new Dec20(Utils.getFilePath(inputsDir, "dec_21.txt"))
//                new Dec20(Utils.getFilePath(inputsDir, "dec_22.txt"))
//                new Dec20(Utils.getFilePath(inputsDir, "dec_23.txt"))
//                new Dec20(Utils.getFilePath(inputsDir, "dec_24.txt"))
        );

        String cookieSession = args[0];
        DecBase.runTasks(adventTasks, cookieSession);
    }
}

