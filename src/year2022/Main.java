package year2022;

import java.io.IOException;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException {
        List<DecBase> adventTasks = List.of(
                //new year2022.Dec1Optimized("C:\\work\\workspace\\advent-code\\inputs\\dec_1.txt"),
                //new year2022.Dec1("C:\\work\\workspace\\advent-code\\inputs\dec_1.txt"),
                //new year2022.Dec2("C:\\work\\workspace\\advent-code\\inputs\\dec_2.txt"),
                //new year2022.Dec2("C:\\work\\workspace\\advent-code\\inputs\\dec_2.txt"),
                new Dec3("C:\\work\\workspace\\advent-code\\inputs\\dec_3.txt")
        );

        adventTasks.forEach(decBase -> {
            try {
                decBase.readDefaultInput().calculate();
                System.out.println("\t\t#######");
                decBase.readInput().calculate();
                System.err.println("*******************************");
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        });
    }
}

