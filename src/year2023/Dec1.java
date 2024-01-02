package year2023;

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
                "two1nine",
                "eightwothree",
                "abcone2threexyz",
                "xtwone3four",
                "4nineeightseven2",
                "zoneight234",
                "7pqrstsixteen"
        ).toList());
        return this;
    }

    @Override
    protected void calculatePart1() {
        ArrayList<Long> sums = new ArrayList<>();

        for (String value : inputStrings) {
            String digit1 = null, digit2 = null;
            for (int i = 0; i < value.length(); i++) {
                char c1 = value.charAt(i);
                char c2 = value.charAt(value.length() - i - 1);
                if (digit1 == null && Character.isDigit(c1)) {
                    digit1 = String.valueOf(c1);
                }
                if (digit2 == null && Character.isDigit(c2)) {
                    digit2 = String.valueOf(c2);
                }
                if (digit1 != null && digit2 != null) {
                    sums.add(Long.parseLong(digit1 + digit2));
                    break;
                }
            }
        }

        System.out.printf("Part 1 - Sum %d%n", sums.stream().mapToInt(Long::intValue).sum());
    }

    private static final Map<String, Integer> numbers = Map.of("one", 1, "two", 2, "three", 3, "four", 4, "five", 5, "six", 6, "seven", 7, "eight", 8, "nine", 9);

    private static class Pair {
        Integer number;
        String numberTxt;
        Integer index;

        public Pair(final String numberTxt, final Integer index) {
            this.numberTxt = numberTxt;
            this.number = numbers.get(numberTxt);
            this.index = index;
        }

        public Pair(final Integer number, final Integer index) {
            this.numberTxt = numbers.entrySet().stream().filter(e -> e.getValue().equals(number)).findFirst().map(Map.Entry::getKey).orElseThrow();
            this.number = number;
            this.index = index;
        }
    }

    @Override
    protected void calculatePart2() {
        ArrayList<Long> sums = new ArrayList<>();

        for (String value : inputStrings) {

            Pair minPair = new Pair("zero", Integer.MAX_VALUE);
            Pair maxPair = new Pair("zero", Integer.MIN_VALUE);

            for (String number : numbers.keySet()) {
                int idx = value.indexOf(number);

                if (idx >= 0) {
                    if (idx < minPair.index) {
                        minPair = new Pair(number, idx);
                    }
                    if (idx > maxPair.index) {
                        maxPair = new Pair(number, idx);
                    }

                    String tmp = value.replaceFirst(number, "");
                    int replacementCounter = 1;
                    while (tmp.contains(number)) {
                        int actualIdx = tmp.indexOf(number) + replacementCounter * number.length();
                        if (actualIdx < minPair.index) {
                            minPair = new Pair(number, actualIdx);
                        }
                        if (actualIdx > maxPair.index) {
                            maxPair = new Pair(number, actualIdx);
                        }
                        tmp = tmp.replaceFirst(number, "");
                        replacementCounter++;
                    }
                }
            }

            for (int i = 0; i < value.length(); i++) {
                char c1 = value.charAt(i);
                char c2 = value.charAt(value.length() - i - 1);
                if (Character.isDigit(c1)) {
                    if (i < minPair.index) {
                        minPair = new Pair(Integer.parseInt(String.valueOf(c1)), i);
                    }
                }
                if (Character.isDigit(c2)) {
                    if (value.length() - i - 1 > maxPair.index) {
                        maxPair = new Pair(Integer.parseInt(String.valueOf(c2)), value.length() - i - 1);
                    }
                }
            }

            sums.add(Long.valueOf(minPair.number + "" + maxPair.number));
        }

        System.out.printf("Part 2 - Sum %d%n", sums.stream().mapToInt(Long::intValue).sum());
    }
}
