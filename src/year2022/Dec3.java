package year2022;

import java.util.*;
import java.util.stream.Stream;

public class Dec3 extends Dec2 {

    public Dec3(String fileName) {
        super(fileName);
    }

    @Override
    protected Dec3 readDefaultInput() {
        System.out.println("Reading default input");
        inputStrings = new LinkedList<>(
                Stream.of("vJrwpWtwJgWrhcsFMMfFFhFp",
                        "jqHRNqRjqzjGDLGLrsFMfFZSrLrFZsSL",
                        "PmmdzqPrVvPwwTWBwg",
                        "wMqvLMZHhHMvwLHjbvcjnnSBnvTQFn",
                        "ttgJtRGJQctTZtZT",
                        "CrZsJsPPZsGzwwsLwLmpwMDw")
                        .toList()
        );
        return this;
    }

    @Override
    protected void calculate() {
        long sum = 0;
        for (String rucksack : inputStrings) {
            Map<String, Integer> compartment1 = new HashMap<>();
            Map<String, Integer> compartment2 = new HashMap<>();
            char[] chars = rucksack.substring(0, rucksack.length() / 2).toCharArray();
            for (char c : chars) {
                compartment1.put(String.valueOf(c), priorityMap().get(String.valueOf(c)));
            }
            chars = rucksack.substring(rucksack.length() / 2).toCharArray();
            for (char c : chars) {
                compartment2.put(String.valueOf(c), priorityMap().get(String.valueOf(c)));
            }

            for (Map.Entry<String, Integer> entry : compartment1.entrySet()) {
                if (compartment2.containsKey(entry.getKey())) {
                    sum += entry.getValue();
                    break;
                }
            }
        }
        System.out.printf("Part 1: %d%n", sum);

        sum = 0;
        int idx = 1;
        Map<String, Integer> rucksack1 = new HashMap<>();
        Map<String, Integer> rucksack2 = new HashMap<>();
        Map<String, Integer> rucksack3 = new HashMap<>();
        for (String rucksack : inputStrings) {
            char[] chars = rucksack.toCharArray();

            switch (idx) {
                case 1 -> {
                    for (char c : chars) {
                        rucksack1.put(String.valueOf(c), priorityMap().get(String.valueOf(c)));
                    }
                }
                case 2 -> {
                    for (char c : chars) {
                        rucksack2.put(String.valueOf(c), priorityMap().get(String.valueOf(c)));
                    }
                }
                default -> {
                    for (char c : chars) {
                        rucksack3.put(String.valueOf(c), priorityMap().get(String.valueOf(c)));
                    }
                }
            }


            if (idx == 3) {
                for (Map.Entry<String, Integer> entry : rucksack1.entrySet()) {
                    if (rucksack2.containsKey(entry.getKey()) && rucksack3.containsKey(entry.getKey())) {
                        sum += entry.getValue();
                        rucksack1 = new HashMap<>();
                        rucksack2 = new HashMap<>();
                        rucksack3 = new HashMap<>();
                        break;
                    }
                }
                idx = 1;
            } else {
                idx++;
            }
        }
        System.out.printf("Part 2: %d%n", sum);

    }

    private Map<String, Integer> priorityMap() {
        Map<String, Integer> priorityMap = new HashMap<>();
        priorityMap.put("a", 1);
        priorityMap.put("b", 2);
        priorityMap.put("c", 3);
        priorityMap.put("d", 4);
        priorityMap.put("e", 5);
        priorityMap.put("f", 6);
        priorityMap.put("g", 7);
        priorityMap.put("h", 8);
        priorityMap.put("i", 9);
        priorityMap.put("j", 10);
        priorityMap.put("k", 11);
        priorityMap.put("l", 12);
        priorityMap.put("m", 13);
        priorityMap.put("n", 14);
        priorityMap.put("o", 15);
        priorityMap.put("p", 16);
        priorityMap.put("q", 17);
        priorityMap.put("r", 18);
        priorityMap.put("s", 19);
        priorityMap.put("t", 20);
        priorityMap.put("u", 21);
        priorityMap.put("v", 22);
        priorityMap.put("w", 23);
        priorityMap.put("x", 24);
        priorityMap.put("y", 25);
        priorityMap.put("z", 26);
        priorityMap.put("A", 27);
        priorityMap.put("B", 28);
        priorityMap.put("C", 29);
        priorityMap.put("D", 30);
        priorityMap.put("E", 31);
        priorityMap.put("F", 32);
        priorityMap.put("G", 33);
        priorityMap.put("H", 34);
        priorityMap.put("I", 35);
        priorityMap.put("J", 36);
        priorityMap.put("K", 37);
        priorityMap.put("L", 38);
        priorityMap.put("M", 39);
        priorityMap.put("N", 40);
        priorityMap.put("O", 41);
        priorityMap.put("P", 42);
        priorityMap.put("Q", 43);
        priorityMap.put("R", 44);
        priorityMap.put("S", 45);
        priorityMap.put("T", 46);
        priorityMap.put("U", 47);
        priorityMap.put("V", 48);
        priorityMap.put("W", 49);
        priorityMap.put("X", 50);
        priorityMap.put("Y", 51);
        priorityMap.put("Z", 52);
        return priorityMap;
    }
}
