package year2022;

import java.util.LinkedList;
import java.util.stream.Stream;

public class Dec5 extends DecBase {

    public Dec5(String fileName) {
        super(fileName);
    }

    @Override
    protected Dec5 readDefaultInput() {
        System.out.println("Reading default input.");
        inputStrings = new LinkedList<>(
                Stream.of("")
                        .toList()
        );
        return this;
    }

    @Override
    protected void calculate() {

    }
}
