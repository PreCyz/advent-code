package year2024;

import base.DecBase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class Dec19 extends DecBase {

    public Dec19(int year) {
        super(year, 19);
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
                towels = new ArrayList<>(Arrays.asList(input.split(", ")));
                cache = Arrays.stream(input.split(", "))
                        .collect(Collectors.toMap(v -> v, v -> Boolean.TRUE, (v1, v2) -> v1));
            } else if (!input.isEmpty()) {
                combinations.add(input);
            }
        }

        towels.sort(Comparator.comparingInt(String::length).reversed());

        long sum = 0;
        for (String combination : combinations) {
            if (check(combination, towels)) {
                cache.put(combination, true);
                sum++;
            }
        }
        System.out.printf("Part 1 - Sum %s%n", sum);
    }

    boolean check(String combination, ArrayList<String> towels) {
        if (cache.containsKey(combination)) {
            return cache.get(combination);
        }
        for (String towel : towels) {
            String[] split = combination.split(towel);
            if (split.length == 1) {
                if (split[0].length() == combination.length()) {
                    cache.put(split[0], false);
                } else {
                    cache.put(split[0], check(split[0], towels));
                }
            } else {
                boolean result = true;
                for (String s : split) {
                    if (!s.isEmpty()) {
                        boolean check = check(s, towels);
                        cache.put(s, check);
                        result &= check;
                    }
                }
                if (result) {
                    return true;
                }
            }
        }
        return cache.getOrDefault(combination, false);
    }

    @Override
    protected void calculatePart2() {
        ArrayList<String> towels = new ArrayList<>();
        ArrayList<String> combinations = new ArrayList<>();
        for (String input : inputStrings) {
            if (input.contains(",")) {
                towels = new ArrayList<>(Arrays.asList(input.split(", ")));
                cache = Arrays.stream(input.split(", "))
                        .collect(Collectors.toMap(v -> v, v -> Boolean.TRUE, (v1, v2) -> v1));
            } else if (!input.isEmpty()) {
                combinations.add(input);
            }
        }

        towels.sort(Comparator.comparingInt(String::length).reversed());

        long sum = 0;
        for (String combination : combinations) {
            if (check(combination, towels)) {
                cache.put(combination, true);
                sum++;
            }
        }
        System.out.printf("Part 2 - Sum %s%n", sum);
    }

    boolean check(String combination, ArrayList<String> towels) {
        if (cache.containsKey(combination)) {
            return cache.get(combination);
        }
        for (String towel : towels) {
            String[] split = combination.split(towel);
            if (split.length == 1) {
                if (split[0].length() == combination.length()) {
                    cache.put(split[0], false);
                } else {
                    cache.put(split[0], check(split[0], towels));
                }
            } else {
                boolean result = true;
                for (String s : split) {
                    if (!s.isEmpty()) {
                        boolean check = check(s, towels);
                        cache.put(s, check);
                        result &= check;
                    }
                }
                if (result) {
                    return true;
                }
            }
        }
        return cache.getOrDefault(combination, false);
    }

}