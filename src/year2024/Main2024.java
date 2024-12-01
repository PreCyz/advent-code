package year2024;

import base.DecBase;

import java.util.List;

public class Main2024 {

    public static void main(String[] args) {
        List<DecBase> adventTasks = List.of(
                new Dec1(2024)
        );

        String cookieSession = args[0];
        DecBase.runTasks(adventTasks, cookieSession);
    }
}

