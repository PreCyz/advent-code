package year2024;

import base.DecBase;

import java.util.LinkedList;
import java.util.stream.Stream;

class Dec10 extends DecBase {

    public Dec10(int year) {
        super(year, 10);
    }

    @Override
    public Dec10 readDefaultInput() {
        System.out.println("Reading default input.");
        inputStrings = new LinkedList<>(Stream.of(
                ""
        ).toList());
        return this;
    }

    @Override
    protected void calculatePart1() {

//        System.out.printf("Part 1 - Sum %d%n", sum);
    }

    @Override
    protected void calculatePart2() {

//        System.out.printf("Part 2 - Sum %d%n", sum);
    }

}