package year2024;

import base.DecBase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.stream.Stream;

class Dec11 extends DecBase {

    public Dec11(int year) {
        super(year, 11);
    }

    @Override
    public Dec11 readDefaultInput() {
        System.out.println("Reading default input.");
        inputStrings = new LinkedList<>(Stream.of(
//                "0 1 10 99 999",
                "125 17"
//                ""
        ).toList());
        return this;
    }

    @Override
    protected void calculatePart1() {
        ArrayList<String> stones = new ArrayList<>(Arrays.stream(inputStrings.getFirst().split(" ")).toList());
        int numberOfBlinks = 25;
        int blinks = 0;
        do {
            ArrayList<String> newStonesSetup = new ArrayList<>(stones.size());
            for (String stone : stones) {
                if ("0".equals(stone)) {
                    newStonesSetup.add(zeroBecomesOne(stone));
                } else if (stone.length() % 2 == 0) {
                    String[] splitStones = splitInHalf(stone);
                    newStonesSetup.add(splitStones[0]);
                    newStonesSetup.add(splitStones[1]);
                } else {
                    newStonesSetup.add(String.valueOf(Long.parseLong(stone) * 2024));
                }
            }
            stones = new ArrayList<>(newStonesSetup);
            blinks++;
        } while (blinks < numberOfBlinks);

        System.out.printf("Part 1 - Sum %d%n", stones.size());
    }

    String zeroBecomesOne(String zero) {
        return "1";
    }

    String[] splitInHalf(String stone) {
        String firstHalf = replaceLeadingZeros(stone.substring(0, stone.length() / 2));
        String secondHalf = replaceLeadingZeros(stone.substring(stone.length() / 2));
        return new String[]{firstHalf, secondHalf};
    }

    String replaceLeadingZeros(String value) {
        while (value.startsWith("0") && value.length() > 1) {
            value = value.substring(1);
        }
        return value;
    }

    @Override
    protected void calculatePart2() {
        ArrayList<String> stones = new ArrayList<>(Arrays.stream(inputStrings.getFirst().split(" ")).filter(s -> !s.isEmpty()).toList());
        long sum = 0;
        for (String stone : stones) {
            sum = blink(stone, 0, 0);
        }
        sum += stones.size();
        System.out.printf("Part 2 - Sum %d%n", sum);
    }

    private long blink(String stone, long sum, int blink) {
        if (blink == 75) {
            return sum;
        }
        ArrayList<String> newStonesSetup = new ArrayList<>(2);
        if ("0".equals(stone)) {
            newStonesSetup.add(zeroBecomesOne(stone));
        } else if (stone.length() % 2 == 0) {
            String[] splitStones = splitInHalf(stone);
            newStonesSetup.add(splitStones[0]);
            newStonesSetup.add(splitStones[1]);
        } else {
            newStonesSetup.add(String.valueOf(Long.parseLong(stone) * 2024));
        }
        sum += newStonesSetup.size();

        for (String newStone : newStonesSetup) {
            System.out.printf("blink %d, stone %s, sum: %d%n", blink + 1, newStone, sum);
            sum = blink(newStone, sum, blink + 1);
        }
        return sum;
    }


}