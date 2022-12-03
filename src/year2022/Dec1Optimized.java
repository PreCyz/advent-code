package year2022;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

public class Dec1Optimized extends Dec1 {

    public Dec1Optimized(String fileName) {
        super(fileName);
    }

    public void calculate() {
        ArrayList<Long> sums = new ArrayList<>();
        long calSum = 0;
        int dwarfWithMaxSum = 1;
        int dwarfNo = 1;
        long maxSum = 0;

        inputIntegers = new LinkedList<>();
        try (Scanner scanner = new Scanner(new FileInputStream(getFileName()))) {
            while (scanner.hasNext()) {
                final String nextLine = scanner.nextLine();
                if (nextLine == null || "".equals(nextLine)) {
                    inputIntegers.add(null);
                    sums.add(calSum);
                    calSum = 0;
                    dwarfNo++;
                } else {
                    final int calories = Integer.parseInt(nextLine);
                    calSum += calories;
                    inputIntegers.add(calories);
                }

                if (calSum > maxSum) {
                    maxSum = calSum;
                    dwarfWithMaxSum = dwarfNo;
                }
                if (!scanner.hasNext()) {
                    sums.add(calSum);
                }
            }
        } catch (IOException ex) {
            System.exit(-2);
        }

        LinkedHashMap<Integer, Long> top3 = new LinkedHashMap<>();
        top3.put(dwarfWithMaxSum, maxSum);
        calculateNextBiggest(sums, top3);
        calculateNextBiggest(sums, top3);

        top3.forEach((k, v) -> System.out.printf("Dwarf %d - sum %d%n", k, v));
        System.out.printf("Top 3 sum %d%n", top3.values().stream().mapToLong(l -> l).sum());
    }

}
