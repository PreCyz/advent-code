package year2024;

import base.DecBase;

import java.util.*;
import java.util.stream.Stream;

class Dec1 extends DecBase {


    public Dec1(int year) {
        super(year, 1);
    }

    @Override
    public Dec1 readDefaultInput() {
        System.out.println("Reading default input.");
        inputStrings = new LinkedList<>(Stream.of(
                "3   4",
                "4   3",
                "2   5",
                "1   3",
                "3   9",
                "3   3"
        ).toList());
        return this;
    }

    @Override
    protected void calculatePart1() {
        List<Integer> one = new ArrayList<>(inputStrings.size());
        List<Integer> two = new ArrayList<>(inputStrings.size());
        inputStrings.forEach(line -> {
            String[] pair = line.split("   ");
            one.add(Integer.parseInt(pair[0]));
            two.add(Integer.parseInt(pair[1]));
        });
        one.sort(Integer::compareTo);
        two.sort(Integer::compareTo);
        long sum = 0;
        int size = one.size();
        for (int i = 0; i < size; i++) {
            sum += Math.abs(one.get(i) - two.get(i));
        }
        System.out.printf("Part 1 - Sum %d%n", sum);
    }

    @Override
    protected void calculatePart2() {
        List<Integer> one = new ArrayList<>(inputStrings.size());
        List<Integer> two = new ArrayList<>(inputStrings.size());
        inputStrings.forEach(line -> {
            String[] pair = line.split("   ");
            one.add(Integer.parseInt(pair[0]));
            two.add(Integer.parseInt(pair[1]));
        });
        one.sort(Integer::compareTo);
        two.sort(Integer::compareTo);
        long sum = 0;
        int size = one.size();
        for (int i = 0; i < size; i++) {
            Integer oneEl = one.get(i);
            sum += oneEl * two.stream().filter(e -> e.equals(oneEl)).count();
        }
        System.out.printf("Part 2 - Sum %d%n", sum);
    }
}
