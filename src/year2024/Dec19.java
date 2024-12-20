package year2024;

import base.DecBase;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class Dec19 extends DecBase {

    public Dec19(int year) {
        super(year, 18);
    }

    record Point(int x, int y) {
    }

    @Override
    public Dec19 readDefaultInput() {
        System.out.println("Reading default input.");
        inputStrings = new LinkedList<>(Stream.of(
                "r, wr, b, g, bwu, rb, gb, br",

                "brwrr",
                "bggr",
                "gbbr",
                "rrbgbr",
                "ubwu",
                "bwurrg",
                "brgr",
                "bbrgwb"
        ).toList());
        return this;
    }

    Map<String, Boolean> cache = new HashMap<>();

    @Override
    protected void calculatePart1() {
        ArrayList<String> towels = new ArrayList<>();
        ArrayList<String> combinations = new ArrayList<>();
        for (String input : inputStrings) {
            if (input.contains(",")) {
                cache = Arrays.stream(input.split(", "))
                        .collect(Collectors.toMap(v -> v, v -> Boolean.TRUE, (v1, v2) -> v1));
            } else if (!input.isEmpty()) {
                combinations.add(input);
            }
        }
        long sum = 0;

        for (String combination : combinations) {
            if (isPossible(combination)) {
                sum++;
            }
        }

        System.out.printf("Part 1 - Sum %s%n", sum);
    }

    boolean isPossible(String combination) {
        if (cache.containsKey(combination)) {
            return cache.get(combination);
        } else {
            for (int i = 1; i < combination.length() - 1; i++) {
                String split1 = combination.substring(combination.length() - i);
                String split2 = combination.substring(0, combination.length() - i);
                if (isPossible(split1)) {
                    cache.put(split1, Boolean.TRUE);
                    isPossible(split2);
                } else {
                    cache.put(split1, Boolean.FALSE);
                }
            }
        }
        return false;
    }

    @Override
    protected void calculatePart2() {

        System.out.printf("Part 2%n");
    }

}