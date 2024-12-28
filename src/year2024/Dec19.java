package year2024;

import base.DecBase;

import java.util.*;
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

        // option 1
        towels.sort(Comparator.comparingInt(String::length).reversed());
        long sum = 0;
        for (String combination : combinations) {
            if (check(combination, towels)) {
                cache.put(combination, true);
                sum++;
            }
        }

        // option 2
        int maxlen = towels.stream().mapToInt(String::length).max().getAsInt();
        sum = 0;
        for (String combination : combinations) {
            sum += canObtain(combination, maxlen, towels) ? 1 : 0;

        }
        System.out.printf("Part 1 - Sum %s%n", sum);
    }

    boolean canObtain(String design, int maxlen, ArrayList<String> patterns) {
        if (design.isEmpty()) {
            return true;
        }
        if (cache.get(design) != null) {
            return cache.get(design);
        }
        for (int i = 1; i < Math.min(design.length(), maxlen) + 1; i++) {
            if (patterns.contains(design.substring(0, i)) && canObtain(design.substring(i), maxlen, patterns)) {
                cache.putIfAbsent(design, true);
                return true;
            }
        }
        cache.putIfAbsent(design, false);
        return false;
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

    Map<String, Long> cache2 = new HashMap<>();

    @Override
    protected void calculatePart2() {
        cache2.clear();
        ArrayList<String> towels = new ArrayList<>();
        ArrayList<String> combinations = new ArrayList<>();
        for (String input : inputStrings) {
            if (input.contains(",")) {
                towels = new ArrayList<>(Arrays.asList(input.split(", ")));
            } else if (!input.isEmpty()) {
                combinations.add(input);
            }
        }

        int maxlen = towels.stream().mapToInt(String::length).max().getAsInt();

        long sum = 0;
        for (String combination : combinations) {
            sum += numOfCombinations(combination, maxlen, towels);
        }

        System.out.printf("Part 2 - Sum %s%n", sum);
    }

    long numOfCombinations(String combination, int maxLength, ArrayList<String> towels) {
        if (combination.isEmpty()) {
            return 1;
        }
        if (cache2.get(combination) != null) {
            return cache2.get(combination);
        }
        long count = 0;
        for (int i = 1; i < Math.min(combination.length(), maxLength) + 1; i++) {
            if (towels.contains(combination.substring(0, i))) {
                count += numOfCombinations(combination.substring(i), maxLength, towels);
            }
        }
        cache2.put(combination, count);
        return count;
    }

}