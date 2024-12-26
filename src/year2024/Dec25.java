package year2024;

import base.DecBase;

import java.util.*;
import java.util.stream.Stream;

class Dec25 extends DecBase {

    public Dec25(int year) {
        super(year, 25);
    }

    @Override
    public Dec25 readDefaultInput() {
        System.out.println("Reading default input.");
        inputStrings = new LinkedList<>(Stream.of(
                "#####",
                ".####",
                ".####",
                ".####",
                ".#.#.",
                ".#...",
                ".....",
                "",
                "#####",
                "##.##",
                ".#.##",
                "...##",
                "...#.",
                "...#.",
                ".....",
                "",
                ".....",
                "#....",
                "#....",
                "#...#",
                "#.#.#",
                "#.###",
                "#####",
                "",
                ".....",
                ".....",
                "#.#..",
                "###..",
                "###.#",
                "###.#",
                "#####",
                "",
                ".....",
                ".....",
                ".....",
                "#....",
                "#.#..",
                "#.#.#",
                "#####"
        ).toList());
        return this;
    }

    @Override
    protected void calculatePart1() {
        List<List<Integer>> locks = new ArrayList<>();
        List<List<Integer>> keys = new ArrayList<>();
        Iterator<String> iterator = inputStrings.iterator();
        while (iterator.hasNext()) {
            String line = iterator.next();
            if (line.isEmpty()) {
                continue;
            }
            if (!line.contains("#")) {
                ArrayList<Integer> key = new ArrayList<>(Stream.generate(() -> 0).limit(5).toList());
                for (int i = 0; i < 5; i++) {
                    line = iterator.next();
                    processIndex(line, 0, key);
                    processIndex(line, 1, key);
                    processIndex(line, 2, key);
                    processIndex(line, 3, key);
                    processIndex(line, 4, key);
                }
                line = iterator.next();
                keys.add(key);
            } else if (!line.contains(".")) {
                ArrayList<Integer> lock = new ArrayList<>(Stream.generate(() -> 0).limit(5).toList());
                for (int i = 0; i < 5; i++) {
                    line = iterator.next();
                    processIndex(line, 0, lock);
                    processIndex(line, 1, lock);
                    processIndex(line, 2, lock);
                    processIndex(line, 3, lock);
                    processIndex(line, 4, lock);
                }
                line = iterator.next();
                locks.add(lock);
            }
        }

        long sum = 0;
        for (List<Integer> lock : locks) {
            for (List<Integer> key : keys) {
                boolean match = true;
                for (int i = 0; i < 5; i++) {
                    if (lock.get(i) + key.get(i) > 5) {
                        match = false;
                        break;
                    }
                }
                if (match) {
                    sum++;
                }
            }
        }

        System.out.printf("Part 1 - Sum %s%n", sum);
    }

    void processIndex(String line, int index, ArrayList<Integer> array) {
        if (line.charAt(index) == '#') {
            array.set(index, array.get(index) + 1);
        }
    }

    @Override
    protected void calculatePart2() {

//            System.out.printf("Part 2 - Sum %s%n", max);
    }
}