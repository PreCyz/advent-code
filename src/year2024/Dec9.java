package year2024;

import base.DecBase;

import java.util.LinkedList;
import java.util.stream.Stream;

class Dec9 extends DecBase {

    public Dec9(int year) {
        super(year, 9);
    }

    @Override
    public Dec9 readDefaultInput() {
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