package year2024;

import base.DecBase;

import java.util.List;

public class Main2024 {

    public static void main(String[] args) {
        List<DecBase> adventTasks = List.of(
//                new Dec1(2024),
//                new Dec2(2024),
//                new Dec3(2024),
//                new Dec4(2024),
//                new Dec5(2024),
//                new Dec6(2024)
//                new Dec7(2024),
//                new Dec8(2024),
//                new Dec9(2024),
//                new Dec10(2024),
//                new Dec11(2024),
//                new Dec12(2024),
                new Dec13(2024)
        );

        String cookieSession = args[0];
        DecBase.runTasks(adventTasks, cookieSession);
    }
}

