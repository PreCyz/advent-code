import java.util.*;
import java.util.stream.Stream;

public class Dec1 extends DecBase {

    public Dec1(String fileName) {
        super(fileName);
    }

    protected Dec1() {
        super();
    }

    public Dec1 readDefaultInput() {
        System.out.println("Reading default input.");
        inputIntegers = new LinkedList<>(
                Stream.of(1000, 2000, 3000, null, 4000, null, 5000, 6000, null, 7000, 8000, 9000, null, 10000).toList()
        );
        return this;
    }

    public void calculate() {
        System.out.println("Calculating ...");
        ArrayList<Long> sums = new ArrayList<>();
        long calSum = 0;
        int dwarfWithMaxSum = 1;
        int dwarfNo = 1;
        long maxSum = 0;

        for (int i = 0, inputSize = inputIntegers.size(); i < inputSize; i++) {
            Integer cal = inputIntegers.get(i);
            if (cal != null) {
                calSum += cal;
            } else {
                sums.add(calSum);
                calSum = 0;
                dwarfNo++;
            }
            if (calSum > maxSum) {
                maxSum = calSum;
                dwarfWithMaxSum = dwarfNo;
            }
            if (i == inputSize - 1) {
                sums.add(calSum);
            }
        }

        LinkedHashMap<Integer, Long> top3 = new LinkedHashMap<>();
        top3.put(dwarfWithMaxSum, maxSum);
        calculateNextBiggest(sums, top3);
        calculateNextBiggest(sums, top3);

        top3.forEach((k, v) -> System.out.printf("Dwarf %d - sum %d%n", k, v));
        System.out.printf("Top 3 sum %d%n", top3.values().stream().mapToLong(l -> l).sum());
    }

    protected void calculateNextBiggest(ArrayList<Long> sums, LinkedHashMap<Integer, Long> top3) {
        int dwarfWithMaxSum = 0;
        long maxSum = 0;
        for (int i = 0, inputSize = sums.size(); i < inputSize; i++) {
            if (!top3.containsKey(i + 1) && sums.get(i) > maxSum) {
                maxSum = sums.get(i);
                dwarfWithMaxSum = i + 1;
            }
        }
        top3.put(dwarfWithMaxSum, maxSum);
    }
}
