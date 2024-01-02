package year2022;

import base.DecBase;

import java.util.LinkedList;
import java.util.stream.Stream;

class Dec25 extends DecBase {
    protected Dec25(int year) {
        super(year, 25);
    }

    enum SNAFU {
        TWO("2", 2), ONE("1", 1), ZERO("0", 0), MINUS("-", -1), DOUBLE_MINUS("=", -2);

        private final String string;
        private final int value;
        SNAFU(String string, int value) {
            this.string = string;
            this.value = value;
        }
    }

    @Override
    public Dec25 readDefaultInput() {
        System.out.println("Reading default input.");
        inputStrings = new LinkedList<>(Stream.of(
                "1=-0-2",
                        "12111",
                        "2=0=",
                        "21",
                        "2=01",
                        "111",
                        "20012",
                        "112",
                        "1=-1=",
                        "1-12",
                        "12",
                        "1=",
                        "122"
        ).toList());
        return this;
    }

    @Override
    protected void calculatePart1() {

        System.out.printf("Part 1: sum = %s%n", "a");
    }

}
