package year2023;

import base.DecBase;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.stream.Stream;

class Dec6 extends DecBase {

    public Dec6(String fileName) {
        super(fileName);
    }

    @Override
    public Dec6 readDefaultInput() {
        System.out.println("Reading default input.");
        inputStrings = new LinkedList<>(Stream.of(
                        "Time:      7  15   30",
                                "Distance:  9  40  200"
                )
                .toList());
        return this;
    }

    @Override
    protected void calculatePart1() {
        ArrayList<Integer> times = new ArrayList<>(Arrays.stream(
                inputStrings.getFirst().replace("Time: ", "").strip().split(" ")
        ).filter(it -> !it.isEmpty()).mapToInt(Integer::valueOf).boxed().toList());
        ArrayList<Integer> distances = new ArrayList<>(Arrays.stream(
                inputStrings.getLast().replace("Distance: ", "").strip().split(" ")
        ).filter(it -> !it.isEmpty()).mapToInt(Integer::valueOf).boxed().toList());

        int[] ways = new int[times.size()];
        final int speedIncrease = 1;
        for (int race = 0; race < times.size(); race++) {
            int time = times.get(race);
            int distance = distances.get(race);
            int totalWays = 0;
            for (int holdTime = 1; holdTime < time; holdTime++) {
                int actualSpeed = holdTime * speedIncrease;
                int actualDistance = actualSpeed * (time - holdTime);
                if (actualDistance > distance) {
                    totalWays++;
                }
            }
            ways[race] = totalWays;
        }

        long sum = 1;
        for (int way : ways) {
            sum *= way;
        }
        System.out.printf("Part 1 - Total score %d%n", sum);
    }

    @Override
    protected void calculatePart2() {
        BigInteger time = new BigInteger(inputStrings.getFirst().replace("Time:", "").replaceAll(" ", ""));
        BigInteger distance = new BigInteger(inputStrings.getLast().replace("Distance:", "").replaceAll(" ", ""));

        final BigInteger speedIncrease = BigInteger.ONE;
        long totalWays = 0;
        BigInteger holdTime = BigInteger.ONE;
        while (holdTime.compareTo(time) < 0) {
            BigInteger actualSpeed = holdTime.multiply(speedIncrease);
            BigInteger actualDistance = actualSpeed.multiply(time.subtract(holdTime));
            if (actualDistance.compareTo(distance) > 0) {
                totalWays++;
            }
            holdTime = holdTime.add(BigInteger.ONE);
        }

        System.out.printf("Part 2 - Total score %d%n", totalWays);
    }

}
