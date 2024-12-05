package year2024;

import base.DecBase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class Dec5 extends DecBase {

    public Dec5(int year) {
        super(year, 5);
    }

    @Override
    public Dec5 readDefaultInput() {
        System.out.println("Reading default input.");
        inputStrings = new LinkedList<>(Stream.of(
                "47|53",
                "97|13",
                "97|61",
                "97|47",
                "75|29",
                "61|13",
                "75|53",
                "29|13",
                "97|29",
                "53|29",
                "61|53",
                "97|53",
                "61|29",
                "47|13",
                "75|47",
                "97|75",
                "47|61",
                "75|61",
                "47|29",
                "75|13",
                "53|13",
                "",
                "75,47,61,53,29",
                "97,61,53,29,13",
                "75,29,13",
                "75,97,47,61,53",
                "61,13,29",
                "97,13,75,29,47"
        ).toList());
        return this;
    }

    @Override
    protected void calculatePart1() {
        List<Integer[]> rules = new ArrayList<>(inputStrings.size());
        List<List<Integer>> updates = new ArrayList<>(inputStrings.size());
        for (String input : inputStrings) {
            if (!"".equals(input) && !"\n".equals(input) && !System.lineSeparator().equals(input)) {
                if (input.contains(",")) {
                    updates.add(Arrays.stream(input.split(",")).map(Integer::parseInt).toList());
                } else {
                    rules.add(Arrays.stream(input.split("\\|")).map(Integer::parseInt).toArray(Integer[]::new));
                }
            }
        }

        Map<Integer, Set<Integer>> map = new HashMap<>();
        for (List<Integer> update : updates) {
            for (Integer element : update) {
                if (!map.containsKey(element)) {
                    map.put(element, new HashSet<>());
                }
            }
        }

        for (Map.Entry<Integer, Set<Integer>> entry : map.entrySet()) {
            final int number = entry.getKey();
            entry.setValue(rules.stream()
                    .filter(array -> array[1] == number)
                    .map(array -> array[0])
                    .collect(Collectors.toCollection(HashSet::new))
            );
        }

        List<List<Integer>> correctUpdates = new ArrayList<>();
        for (List<Integer> update : updates) {
            boolean correctUpdate = true;
            for (int i = update.size() - 1; i >= 0; i--) {
                int number = update.get(i);
                Set<Integer> allowedBefore = map.get(number);
                Integer[] allBefore = Arrays.copyOfRange(update.toArray(Integer[]::new), 0, i);

                for (Integer before : allBefore) {
                    if (!allowedBefore.contains(before)) {
                        correctUpdate = false;
                        break;
                    }
                }
                if (!correctUpdate) {
                    break;
                }
            }
            if (correctUpdate) {
                correctUpdates.add(update);
            }
        }

        long sum = 0;
        for (List<Integer> correctUpdate : correctUpdates) {
            int middleIdx = correctUpdate.size() / 2;
            sum += correctUpdate.get(middleIdx);
        }

        System.out.printf("Part 1 - Sum %d%n", sum);
    }

    @Override
    protected void calculatePart2() {
        List<Integer[]> rules = new ArrayList<>(inputStrings.size());
        List<List<Integer>> updates = new ArrayList<>(inputStrings.size());
        for (String input : inputStrings) {
            if (!"".equals(input) && !"\n".equals(input) && !System.lineSeparator().equals(input)) {
                if (input.contains(",")) {
                    updates.add(Arrays.stream(input.split(",")).map(Integer::parseInt).toList());
                } else {
                    rules.add(Arrays.stream(input.split("\\|")).map(Integer::parseInt).toArray(Integer[]::new));
                }
            }
        }

        Map<Integer, Set<Integer>> allowedBeforeMap = new HashMap<>();
        for (List<Integer> update : updates) {
            for (Integer element : update) {
                if (!allowedBeforeMap.containsKey(element)) {
                    allowedBeforeMap.put(element, new HashSet<>());
                }
            }
        }

        for (Map.Entry<Integer, Set<Integer>> entry : allowedBeforeMap.entrySet()) {
            final int number = entry.getKey();
            entry.setValue(rules.stream()
                    .filter(array -> array[1] == number)
                    .map(array -> array[0])
                    .collect(Collectors.toCollection(HashSet::new))
            );
        }

        List<List<Integer>> incorrectUpdates = new ArrayList<>();
        for (List<Integer> update : updates) {
            boolean correctUpdate = true;
            for (int i = update.size() - 1; i >= 0; i--) {
                int number = update.get(i);
                Set<Integer> allowedBefore = allowedBeforeMap.get(number);
                Integer[] allBefore = Arrays.copyOfRange(update.toArray(Integer[]::new), 0, i);

                for (Integer before : allBefore) {
                    if (!allowedBefore.contains(before)) {
                        correctUpdate = false;
                        break;
                    }
                }
                if (!correctUpdate) {
                    break;
                }
            }
            if (!correctUpdate) {
                incorrectUpdates.add(update);
            }
        }

        List<List<Integer>> correctUpdates = new ArrayList<>(incorrectUpdates.size());
        incorrectUpdates.forEach(iu -> correctUpdates.add(new ArrayList<>(iu.size())));

        int idx = 0;
        for (List<Integer> incorrectUpdate : incorrectUpdates) {
            for (Integer number : incorrectUpdate) {
                List<Integer> correctOrder = correctUpdates.get(idx);
                if (correctOrder.isEmpty()) {
                    correctOrder.add(number);
                } else {
                    boolean addToEnd = true;
                    for (int i = 0, size = correctOrder.size(); i < size; i++) {
                        Integer existing = correctOrder.get(i);
                        if (allowedBeforeMap.get(existing).contains(number)) {
                            correctOrder.add(i, number);
                            addToEnd = false;
                            break;
                        }
                    }
                    if (addToEnd) {
                        correctOrder.add(number);
                    }
                }
            }
            idx++;
        }

        long sum = 0;
        for (List<Integer> correctUpdate : correctUpdates) {
            int middleIdx = correctUpdate.size() / 2;
            sum += correctUpdate.get(middleIdx);
        }

        System.out.printf("Part 2 - Sum %d%n", sum);
    }
}