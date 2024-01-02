package year2023;

import base.DecBase;

import java.util.*;
import java.util.stream.Stream;


class Dec9 extends DecBase {

    public Dec9(int year) {
        super(year, 9);
    }

    @Override
    public Dec9 readDefaultInput() {
        System.out.println("Reading default input.");
        inputStrings = new LinkedList<>(Stream.of(
                        "0 3 6 9 12 15",
                                "1 3 6 10 15 21",
                                "10 13 16 21 30 45"
                )
                .toList());
        return this;
    }

    @Override
    protected void calculatePart1() {
        ArrayList<Long> predictions = new ArrayList<>(inputStrings.size());
        for (String line : inputStrings) {
            LinkedList<Long> numbers = new LinkedList<>(Arrays.stream(line.split(" ")).map(Long::valueOf).toList());
            Long prediction = calculateLastNumber(numbers);
            predictions.add(numbers.getLast() + prediction);
        }

        long sum = predictions.stream().mapToLong(it -> it).sum();
        System.out.printf("Part 1 - Total score %d%n", sum);
    }

    private Long calculateLastNumber(LinkedList<Long> numbers) {
        LinkedList<Long> differences = new LinkedList<>();
        for (int i = 1; i < numbers.size(); i++) {
            differences.add(numbers.get(i) - numbers.get(i - 1));
        }
        if (differences.stream().allMatch(it -> it == 0)) {
            return 0L;
        }
        return differences.getLast() + calculateLastNumber(differences);
    }

    @Override
    protected void calculatePart2() {
        ArrayList<Long> predictions = new ArrayList<>(inputStrings.size());
        for (String line : inputStrings) {
            LinkedList<Long> numbers = new LinkedList<>(Arrays.stream(line.split(" ")).map(Long::valueOf).toList());
            Long prediction = calculateFirstNumber(numbers);
            predictions.add(numbers.getFirst() - prediction);
        }

        long sum = predictions.stream().mapToLong(it -> it).sum();
        System.out.printf("Part 2 - Total score %d%n", sum);
    }

    private Long calculateFirstNumber(LinkedList<Long> numbers) {
        LinkedList<Long> differences = new LinkedList<>();
        for (int i = 1; i < numbers.size(); i++) {
            differences.add(numbers.get(i) - numbers.get(i - 1));
        }
        if (differences.stream().allMatch(it -> it == 0)) {
            return 0L;
        }
        return differences.getFirst() - calculateFirstNumber(differences);
    }

}
