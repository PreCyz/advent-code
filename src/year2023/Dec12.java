package year2023;

import base.DecBase;

import java.util.*;
import java.util.stream.Stream;

class Dec12 extends DecBase {

    public Dec12(int year) {
        super(year, 12);
    }

    @Override
    public Dec12 readDefaultInput() {
        System.out.println("Reading default input.");
        inputStrings = new LinkedList<>(Stream.of(
                "???.### 1,1,3",
                        ".??..??...?##. 1,1,3",
                        "?#?#?#?#?#?#?#? 1,3,1,6",
                        "????.#...#... 4,1,1",
                        "????.######..#####. 1,6,5",
                        "?###???????? 3,2,1"
                )
                .toList());
        return this;
    }

    @Override
    protected void calculatePart1() {
        for (String line : inputStrings) {
            ArrayList<Integer> numbers = new ArrayList<>(Arrays.stream(line.split(" ")[1].split(",")).map(Integer::parseInt).toList());
            ArrayList<String> parts = new ArrayList<>(Arrays.stream(line.split(" ")[0].split("\\.+")).filter(it -> !it.isEmpty()).toList());

            boolean a = false;
        }
        long sum = 0;
        System.out.printf("Part 1 - Total score %d%n", sum);
    }

    @Override
    protected void calculatePart2() {
        long sum = 0;
        System.out.printf("Part 2 - Total score %d%n", sum);
    }

}
