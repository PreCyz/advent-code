package year2022;

import base.DecBase;

import java.util.LinkedList;
import java.util.stream.Stream;

class Dec5 extends DecBase {

    public Dec5(String fileName) {
        super(fileName);
    }

    @Override
    public Dec5 readDefaultInput() {
        System.out.println("Reading default input.");
        inputStrings = new LinkedList<>(
                Stream.of("")
                        .toList()
        );
        return this;
    }

    @Override
    protected void calculatePart1() {
    }
}
