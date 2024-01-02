package year2022;

import base.DecBase;

import java.util.*;
import java.util.stream.Stream;

class Dec11 extends DecBase {
    protected Dec11(int year) {
        super(year, 11);
    }

    static class Monkey {
        int no;
        LinkedList<Long> startingItems;
        String operationType;
        String operationValue;
        int divisible;
        int monkeyIfTrue;
        int monkeyIfFalse;
        long totalInspections;
        //Object worryLevel;

        public Monkey(int no, LinkedList<Long> startingItems, String operationType, String operationValue, int divisible, int monkeyIfTrue, int monkeyIfFalse) {
            this.no = no;
            this.startingItems = startingItems;
            this.operationType = operationType;
            this.operationValue = operationValue;
            this.divisible = divisible;
            this.monkeyIfTrue = monkeyIfTrue;
            this.monkeyIfFalse = monkeyIfFalse;
            totalInspections = 0;
        }
    }

    @Override
    public Dec11 readDefaultInput() {
        System.out.println("Reading default input.");
        inputStrings = new LinkedList<>(Stream.of(
                "Monkey 0:",
                "  Starting items: 79, 98",
                "  Operation: new = old * 19",
                "  Test: divisible by 23",
                "    If true: throw to monkey 2",
                "    If false: throw to monkey 3",
                "",
                "Monkey 1:",
                "  Starting items: 54, 65, 75, 74",
                "  Operation: new = old + 6",
                "  Test: divisible by 19",
                "    If true: throw to monkey 2",
                "    If false: throw to monkey 0",
                "",
                "Monkey 2:",
                "  Starting items: 79, 60, 97",
                "  Operation: new = old * old",
                "  Test: divisible by 13",
                "    If true: throw to monkey 1",
                "    If false: throw to monkey 3",
                "",
                "Monkey 3:",
                "  Starting items: 74",
                "  Operation: new = old + 3",
                "  Test: divisible by 17",
                "    If true: throw to monkey 0",
                "    If false: throw to monkey 1"
        ).toList());
        return this;
    }

    @Override
    protected void calculatePart1() {
        ArrayList<Monkey> monkeys = new ArrayList<>();
        Iterator<String> lineIterator = inputStrings.iterator();
        while (lineIterator.hasNext()) {
            String line = lineIterator.next();
            if (line.isEmpty()) {
                continue;
            }
            int monkeyNo = Integer.parseInt(line.split(" ")[1].replace(":", ""));
            LinkedList<Long> startingItems = getStartingItems(lineIterator.next());
            String operation = lineIterator.next();
            String operationType = operation.contains("+") ? "+" : "*";
            String operationValue = operation.substring("  Operation: new = old ".length() + 2);
            int divisible = Integer.parseInt(lineIterator.next().substring("  Test: divisible by ".length()));
            int monkeyIfTrue = Integer.parseInt(lineIterator.next().substring("    If true: throw to monkey ".length()));
            int monkeyIfFalse = Integer.parseInt(lineIterator.next().substring("    If false: throw to monkey ".length()));
            monkeys.add(new Monkey(monkeyNo, startingItems, operationType, operationValue, divisible, monkeyIfTrue, monkeyIfFalse));
        }

        int round = 0;
        while (round < 20) {
            round++;
            for (Monkey monkey : monkeys) {
                while (!monkey.startingItems.isEmpty()) {
                    long startingItem = monkey.startingItems.removeFirst();
                    long worryLevel = 0;
                    long operationValue = "old".equals(monkey.operationValue) ? startingItem : Integer.parseInt(monkey.operationValue);
                    if ("*".equals(monkey.operationType)) {
                        worryLevel = startingItem * operationValue;
                    } else {
                        worryLevel = startingItem + operationValue;
                    }
                    worryLevel = Double.valueOf(Math.floor(worryLevel / 3d)).intValue();
                    int nextMonkey = worryLevel % monkey.divisible == 0 ? monkey.monkeyIfTrue : monkey.monkeyIfFalse;
                    monkeys.get(nextMonkey).startingItems.addLast(worryLevel);
                    monkey.totalInspections++;
                }
            }
//            System.out.printf("After round %d, the monkeys are holding items with these worry levels:%n", round);
//            for (Monkey monkey: monkeys) {
//                System.out.printf("Monkey %d: %s%n", monkey.no, monkey.startingItems);
//            }
        }
        for (Monkey monkey : monkeys) {
            System.out.printf("Monkey %d inspected items %d times.%n", monkey.no, monkey.totalInspections);
        }
        final long max1 = monkeys.stream().mapToLong(m -> m.totalInspections).max().orElseThrow();
        long max2 = monkeys.stream().mapToLong(m -> m.totalInspections).filter(i -> i != max1).max().orElseThrow();
        System.out.println("Part 1: " + (max1 * max2));
    }

    LinkedList<Long> getStartingItems(String line) {
        LinkedList<Long> startingItems = new LinkedList<>();
        String[] items = line.substring("  Starting items: ".length()).split(" ");
        for (String item : items) {
            if (!item.isEmpty()) {
                startingItems.add(Long.parseLong(item.replace(",", "")));
            }
        }
        return startingItems;
    }

    @Override
    protected void calculatePart2() {
        ArrayList<Monkey> monkeys = new ArrayList<>();
        Iterator<String> lineIterator = inputStrings.iterator();
        while (lineIterator.hasNext()) {
            String line = lineIterator.next();
            if (line.isEmpty()) {
                continue;
            }
            int monkeyNo = Integer.parseInt(line.split(" ")[1].replace(":", ""));
            LinkedList<Long> startingItems = getStartingItems(lineIterator.next());
            String operation = lineIterator.next();
            String operationType = operation.contains("+") ? "+" : "*";
            String operationValue = operation.substring("  Operation: new = old ".length() + 2);
            int divisible = Integer.parseInt(lineIterator.next().substring("  Test: divisible by ".length()));
            int monkeyIfTrue = Integer.parseInt(lineIterator.next().substring("    If true: throw to monkey ".length()));
            int monkeyIfFalse = Integer.parseInt(lineIterator.next().substring("    If false: throw to monkey ".length()));
            monkeys.add(new Monkey(monkeyNo, startingItems, operationType, operationValue, divisible, monkeyIfTrue, monkeyIfFalse));
        }

        int lcm = getLowestCommonMultiple(monkeys);
        int round = 0;
        while (round < 10000) {
            round++;
            for (Monkey monkey : monkeys) {
                while (!monkey.startingItems.isEmpty()) {
                    long startingItem = monkey.startingItems.removeFirst();
                    long worryLevel = 0;
                    long operationValue = "old".equals(monkey.operationValue) ? startingItem : Integer.parseInt(monkey.operationValue);
                    if ("*".equals(monkey.operationType)) {
                        worryLevel = startingItem * operationValue;
                    } else {
                        worryLevel = startingItem + operationValue;
                    }

                    worryLevel = worryLevel % lcm;
                    int nextMonkey = worryLevel % monkey.divisible == 0 ? monkey.monkeyIfTrue : monkey.monkeyIfFalse;

                    monkeys.get(nextMonkey).startingItems.addLast(worryLevel);
                    monkey.totalInspections++;
                }
            }
        }
        for (Monkey monkey : monkeys) {
            System.out.printf("Monkey %d inspected items %d times.%n", monkey.no, monkey.totalInspections);
        }
        final long max1 = monkeys.stream().mapToLong(m -> m.totalInspections).max().orElseThrow();
        long max2 = monkeys.stream().mapToLong(m -> m.totalInspections).filter(i -> i != max1).max().orElseThrow();
        System.out.println("Part 2: " + (max1 * max2));
    }

    private int getLowestCommonMultiple(ArrayList<Monkey> monkeys) {
        int reminder = -1;
        int lcm = 1;
        while(reminder != 0) {
            reminder = 0;
            lcm++;
            for (Monkey monkey : monkeys) {
                reminder += lcm % monkey.divisible;
            }
        }
        return lcm;
    }
}
