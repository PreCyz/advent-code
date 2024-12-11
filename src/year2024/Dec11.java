package year2024;

import base.DecBase;

import java.util.*;
import java.util.stream.Stream;

class Dec11 extends DecBase {

    public Dec11(int year) {
        super(year, 11);
    }

    record Pair(String stone, int blink) { }

    final Map<Pair, Long> cache = new HashMap<>();

    @Override
    public Dec11 readDefaultInput() {
        System.out.println("Reading default input.");
        inputStrings = new LinkedList<>(Stream.of(
                "125 17"
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
                    newStonesSetup.add("1");
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
        ArrayList<String> stones = new ArrayList<>(Arrays.stream(inputStrings.getFirst().split(" ")).toList());
        long sum = 0;
        for (String stone : stones) {
            sum += blink(stone, 75);
        }
        System.out.printf("Part 2 - Sum %d%n", sum);
    }

    private long blink(String stone, int blink) {
        Pair pair = new Pair(stone, blink);
        if (cache.containsKey(pair)) {
            return cache.get(pair);
        }
        if (blink == 0) {
            return 1;
        }
        if ("0".equals(stone)) {
            long result = blink("1", blink - 1);
            cache.put(new Pair("1", blink - 1), result);
            return result;
        } else if (stone.length() % 2 == 0) {
            String[] splitStones = splitInHalf(stone);

            long r1 = blink(splitStones[0], blink - 1);
            cache.put(new Pair(splitStones[0], blink - 1), r1);

            long r2 = blink(splitStones[1], blink - 1);
            cache.put(new Pair(splitStones[1], blink - 1), r2);

            return r1 + r2;
        }

        String sTmp = String.valueOf(Long.parseLong(stone) * 2024);
        long result = blink(sTmp, blink - 1);
        cache.put(new Pair(sTmp, blink - 1), result);

        return result;
    }

}