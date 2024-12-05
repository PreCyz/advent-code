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
        Map<Integer, Set<Integer>> allowedBeforeMap = new HashMap<>();
        for (String input : inputStrings) {
            if (!"".equals(input) && !"\n".equals(input) && !System.lineSeparator().equals(input)) {
                if (input.contains(",")) {
                    List<Integer> list = Arrays.stream(input.split(",")).map(Integer::parseInt).toList();
                    updates.add(list);
                    list.forEach(element -> allowedBeforeMap.putIfAbsent(element, new HashSet<>()));
                } else {
                    rules.add(Arrays.stream(input.split("\\|")).map(Integer::parseInt).toArray(Integer[]::new));
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

        List<List<Integer>> correctUpdates = new ArrayList<>();
        for (List<Integer> update : updates) {
            boolean correctUpdate = true;
            for (int i = update.size() - 1; i >= 0; i--) {
                int current = update.get(i);
                Set<Integer> allowedBefore = allowedBeforeMap.get(current);
                List<Integer> allBeforeCurrent = update.subList(0, i);

                if (!allowedBefore.containsAll(allBeforeCurrent)) {
                    correctUpdate = false;
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
        Map<Integer, Set<Integer>> allowedBeforeMap = new HashMap<>();
        for (String input : inputStrings) {
            if (!"".equals(input) && !"\n".equals(input) && !System.lineSeparator().equals(input)) {
                if (input.contains(",")) {
                    List<Integer> list = Arrays.stream(input.split(",")).map(Integer::parseInt).toList();
                    updates.add(list);
                    list.forEach(element -> allowedBeforeMap.putIfAbsent(element, new HashSet<>()));
                } else {
                    rules.add(Arrays.stream(input.split("\\|")).map(Integer::parseInt).toArray(Integer[]::new));
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
                int current = update.get(i);
                Set<Integer> allowedBefore = allowedBeforeMap.get(current);
                List<Integer> allBeforeCurrent = update.subList(0, i);

                if (!allowedBefore.containsAll(allBeforeCurrent)) {
                    correctUpdate = false;
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
            for (Integer current : incorrectUpdate) {
                List<Integer> correctOrder = correctUpdates.get(idx);
                if (correctOrder.isEmpty()) {
                    correctOrder.add(current);
                } else {
                    boolean addToEnd = true;
                    for (int i = 0, size = correctOrder.size(); i < size; i++) {
                        Integer existing = correctOrder.get(i);
                        if (allowedBeforeMap.get(existing).contains(current)) {
                            correctOrder.add(i, current);
                            addToEnd = false;
                            break;
                        }
                    }
                    if (addToEnd) {
                        correctOrder.add(current);
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