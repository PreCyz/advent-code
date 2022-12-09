package year2022;

import base.DecBase;

import java.util.LinkedList;
import java.util.stream.Stream;

class Dec9 extends DecBase {

    protected Dec9(String fileName) {
        super(fileName);
    }

    @Override
    public Dec9 readDefaultInput() {
        System.out.println("Reading default input.");
        inputStrings = new LinkedList<>(Stream.of(
                "R 4",
                "U 4",
                "L 3",
                "D 1",
                "R 4",
                "D 1",
                "L 5",
                "R 2"
        ).toList());
        return this;
    }

    @Override
    protected void calculatePart1() {

    }

    @Override
    protected void calculatePart2() {

    }
}
