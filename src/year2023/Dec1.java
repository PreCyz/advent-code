package year2023;

import base.DecBase;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

class Dec1 extends DecBase {


    public Dec1(String fileName) {
        super(fileName);
    }

    @Override
    public Dec1 readDefaultInput() {
        System.out.println("Reading default input.");
        inputStrings = new LinkedList<>(Stream.of("eighteight9dnvcqznjvfpreight").toList());
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

        System.out.printf("Sum %d%n", sums.stream().mapToInt(Long::intValue).sum());
    }

    private final Map<String, Integer> numbers = Map.of("one", 1, "two", 2, "three", 3, "four", 4, "five", 5, "six", 6, "seven", 7, "eight", 8, "nine", 9);

    private static class Pair {
        String number;
        Integer index;

        public Pair(String number, Integer index) {
            this.number = number;
            this.index = index;
        }
    }

    @Override
    protected void calculatePart2() {
        ArrayList<Long> sums = new ArrayList<>();

        for (String value : inputStrings) {

            Pair minPair = new Pair(null, Integer.MAX_VALUE);
            Pair maxPair = new Pair(null, Integer.MIN_VALUE);

            List<Pair> pairs = new ArrayList<>();
            for (String number : numbers.keySet()) {
                int idx = value.indexOf(number);

                if (idx >= 0 && value.split(number).length > 0) {
                    String[] split = value.split(number);
                }


                if (idx >= 0) {
                    pairs.add(new Pair(number, idx));
                }
                if (idx >= 0) {
                    if (idx < minPair.index) {
                        minPair = new Pair(number, idx);
                    }
                    if (idx > maxPair.index) {
                        maxPair = new Pair(number, idx);
                    }
                }
            }


            final Pair min = new Pair(minPair.number, minPair.index);
            final Pair max = new Pair(maxPair.number, maxPair.index);

            String digit1 = null, digit2 = null;
            for (int i = 0; i < value.length(); i++) {
                char c1 = value.charAt(i);
                char c2 = value.charAt(value.length() - i - 1);
                if (digit1 == null && Character.isDigit(c1)) {
                    if (i < minPair.index) {
                        digit1 = String.valueOf(c1);
                    } else {
                        digit1 = String.valueOf(numbers.get(
                                pairs.stream()
                                        .filter(p -> p.index.equals(min.index))
                                        .findFirst()
                                        .map(p -> p.number)
                                        .orElseThrow()
                        ));
                    }
                }
                if (digit2 == null && Character.isDigit(c2)) {
                    if (value.length() - i - 1 > max.index) {
                        digit2 = String.valueOf(c2);
                    } else {
                        digit2 = String.valueOf(numbers.get(
                                pairs.stream()
                                        .filter(p -> p.index.equals(max.index))
                                        .findFirst()
                                        .map(p -> p.number)
                                        .orElseThrow()
                        ));
                    }
                }
                if (digit1 != null && digit2 != null) {
                    sums.add(Long.parseLong(digit1 + digit2));
                    break;
                }
            }
            if (digit1 == null && digit2 == null) {
                digit1 = String.valueOf(numbers.get(
                        pairs.stream()
                                .filter(p -> p.index.equals(min.index))
                                .findFirst()
                                .map(p -> p.number)
                                .orElseThrow()
                ));
                digit2 = String.valueOf(numbers.get(
                        pairs.stream()
                                .filter(p -> p.index.equals(max.index))
                                .findFirst()
                                .map(p -> p.number)
                                .orElseThrow()
                ));
                sums.add(Long.parseLong(digit1 + digit2));
            }
        }

        System.out.printf("Top sum %d%n", sums.stream().mapToInt(Long::intValue).sum());
    }
}
