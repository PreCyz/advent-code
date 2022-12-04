package year2015;

import base.DecBase;

import java.util.LinkedList;
import java.util.stream.Stream;

public class Dec1 extends DecBase {

    protected Dec1(String fileName) {
        super(fileName);
    }

    @Override
    public DecBase readDefaultInput() {
        System.out.println("Reading default input.");
        inputStrings = new LinkedList<>(
                Stream.of(
                        "(())",
                        "()()",
                        "(((",
                        "(()(()(",
                        "))(((((",
                        "())",
                        "))(",
                        ")))",
                        ")())())"
                ).toList()
        );
        return this;
    }

    @Override
    protected void calculatePart1() {
        for (String input : inputStrings) {
            int finalFloor = 0;
            for (char c : input.toCharArray()) {
                finalFloor += c == '(' ? 1 : -1;
            }
            System.out.printf("Input %s - floor %d%n", input.substring(0, 3), finalFloor);
        }
    }

    @Override
    protected void calculatePart2() {
        for (String input : inputStrings) {
            int idx = 1;
            int finalFloor = 0;
            for (char c : input.toCharArray()) {
                finalFloor += c == '(' ? 1 : -1;
                if (finalFloor == -1) {
                    break;
                } else {
                    idx++;
                }
            }
            System.out.printf("Minus -1 floor index %d%n", idx);
        }
    }
}
