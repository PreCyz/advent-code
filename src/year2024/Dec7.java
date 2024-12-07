package year2024;

import base.DecBase;

import java.util.*;
import java.util.stream.Stream;

class Dec7 extends DecBase {

    public Dec7(int year) {
        super(year, 7);
    }

    enum Operand {
        add, multiply, concatenation;

        long calculate(long a, long b) {
            return switch (this) {
                case add -> a + b;
                case multiply -> a * b;
                case concatenation -> Long.parseLong("" + a + b);
            };
        }
    }

    record Equation(long result, List<Integer> numbers) { }

    @Override
    public Dec7 readDefaultInput() {
        System.out.println("Reading default input.");
        inputStrings = new LinkedList<>(Stream.of(
                "190: 10 19",
                "3267: 81 40 27",
                "83: 17 5",
                "156: 15 6",
                "7290: 6 8 6 15",
                "161011: 16 10 13",
                "192: 17 8 14",
                "21037: 9 7 18 13",
                "292: 11 6 16 20"
        ).toList());
        return this;
    }

    @Override
    protected void calculatePart1() {
        ArrayList<Equation> equations = new ArrayList<>(inputStrings.size());
        for (String input : inputStrings) {
            String[] split = input.split(": ");
            equations.add(new Equation(
                    Long.parseLong(split[0]),
                    new ArrayList<>(Stream.of(split[1].split(" ")).map(Integer::parseInt).toList())
            ));
        }

        List<Long> totalCalibrationResults = new ArrayList<>();
        for (Equation equation : equations) {
            List<List<Operand>> newOperandList = getNewOperandList(equation.numbers().size() - 1);

            for (List<Operand> operands : newOperandList) {
                Iterator<Operand> iterator = operands.iterator();
                long firstNumber = equation.numbers().getFirst();
                for (int i = 1; i < equation.numbers().size(); i++) {
                    int secondNumber = equation.numbers().get(i);
                    Operand operand = iterator.next();
                    firstNumber = operand.calculate(firstNumber, secondNumber);
                }
                if (equation.result == firstNumber) {
                    totalCalibrationResults.add(equation.result());
                    break;
                }
            }
        }

        System.out.printf("Part 1 - Sum %d%n", totalCalibrationResults.stream().mapToLong(i -> i).sum());
    }

    private List<List<Operand>> getNewOperandList(int size) {
        if (size == 1) {
            List<Operand> add = new ArrayList<>(List.of(Operand.add));
            List<Operand> multiply = new ArrayList<>(List.of(Operand.multiply));
            return new ArrayList<>(List.of(add, multiply));
        }
        int firstHalfSize = size / 2;
        int secondHalfSize = size / 2;
        if (size % 2 != 0) {
            secondHalfSize = 1 + size / 2;
        }
        List<List<Operand>> firstHalfOperandList = getNewOperandList(firstHalfSize);
        List<List<Operand>> secondHalfOperandList = getNewOperandList(secondHalfSize);
        List<List<Operand>> result = new ArrayList<>();
        for (List<Operand> firstHalf : firstHalfOperandList) {
            for (List<Operand> secondHalf : secondHalfOperandList) {
                List<Operand> tmp = new ArrayList<>(firstHalf);
                tmp.addAll(secondHalf);
                result.add(tmp);
            }
        }
        return result;
    }

    @Override
    protected void calculatePart2() {
        ArrayList<Equation> equations = new ArrayList<>(inputStrings.size());
        for (String input : inputStrings) {
            String[] split = input.split(": ");
            equations.add(new Equation(
                    Long.parseLong(split[0]),
                    new ArrayList<>(Stream.of(split[1].split(" ")).map(Integer::parseInt).toList())
            ));
        }

        List<Long> totalCalibrationResults = new ArrayList<>();
        for (Equation equation : equations) {
            List<List<Operand>> newOperandList = getNewOperandList2(equation.numbers().size() - 1);

            for (List<Operand> operands : newOperandList) {
                Iterator<Operand> iterator = operands.iterator();
                long firstNumber = equation.numbers().getFirst();
                for (int i = 1; i < equation.numbers().size(); i++) {
                    int secondNumber = equation.numbers().get(i);
                    Operand operand = iterator.next();
                    firstNumber = operand.calculate(firstNumber, secondNumber);
                }
                if (equation.result == firstNumber) {
                    totalCalibrationResults.add(equation.result());
                    break;
                }
            }
        }
        System.out.printf("Part 2 - Sum %d%n", totalCalibrationResults.stream().mapToLong(i -> i).sum());
    }

    private List<List<Operand>> getNewOperandList2(int size) {
        if (size == 1) {
            List<Operand> add = new ArrayList<>(List.of(Operand.add));
            List<Operand> multiply = new ArrayList<>(List.of(Operand.multiply));
            List<Operand> concatenation = new ArrayList<>(List.of(Operand.concatenation));
            return new ArrayList<>(List.of(add, multiply, concatenation));
        }
        int firstHalfSize = size / 2;
        int secondHalfSize = size / 2;
        if (size % 2 != 0) {
            secondHalfSize = 1 + size / 2;
        }
        List<List<Operand>> firstHalfOperandList = getNewOperandList2(firstHalfSize);
        List<List<Operand>> secondHalfOperandList = getNewOperandList2(secondHalfSize);
        List<List<Operand>> result = new ArrayList<>();
        for (List<Operand> firstHalf : firstHalfOperandList) {
            for (List<Operand> secondHalf : secondHalfOperandList) {
                List<Operand> tmp = new ArrayList<>(firstHalf);
                tmp.addAll(secondHalf);
                result.add(tmp);
            }
        }
        return result;
    }
}