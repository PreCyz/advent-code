package year2023;

import base.DecBase;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.stream.Stream;

class Dec13 extends DecBase {

    public Dec13(String fileName) {
        super(fileName);
    }

    @Override
    public Dec13 readDefaultInput() {
        System.out.println("Reading default input.");
        inputStrings = new LinkedList<>(Stream.of(
                        "#.##..##.",
                        "..#.##.#.",
                        "##......#",
                        "##......#",
                        "..#.##.#.",
                        "..##..##.",
                        "#.#.##.#.",
                        "",
                        "#...##..#",
                        "#....#..#",
                        "..##..###",
                        "#####.##.",
                        "#####.##.",
                        "..##..###",
                        "#....#..#"
                )
                .toList());
        return this;
    }

    @Override
    protected void calculatePart1() {
        long sum = 0;
        ArrayList<String> strings = new ArrayList<>();
        Iterator<String> iterator = inputStrings.iterator();
        while (iterator.hasNext()) {
            String line = iterator.next();
            if (!line.isEmpty() && !line.equals("\n")) {
                strings.add(line);
            }
            if (line.isEmpty() || System.lineSeparator().equals(line) || !iterator.hasNext()) {
                sum += getScore(strings);
                strings.clear();
            }
        }
        System.out.printf("Part 1 - Total score %d%n", sum);
    }

    private long getScore(ArrayList<String> strings) {
        return 100L * getRowsScore(strings) + getColumnsScore(strings);
    }

    private int getRowsScore(ArrayList<String> strings) {
        ArrayList<Long> sums = new ArrayList<>(strings.size());
        for (String row : strings) {
            char[] chars = row.replaceAll("\\.", "0").replaceAll("#", "1").toCharArray();
            long sum = 0;
            for (char aChar : chars) {
                sum += Long.parseLong(aChar + "");
            }
            sums.add(sum);
        }

        for (int i = 1; i < sums.size(); i++) {
            long first = sums.get(i - 1);
            long second = sums.get(i);
            if (first == second && strings.get(i - 1).equals(strings.get(i))) {
                int firstIdx = i - 1 - 1;
                int secondIdx = i + 1;
                boolean isReflection = true;
                while (firstIdx >= 0 && secondIdx < sums.size()) {
                    isReflection &= strings.get(firstIdx).equals(strings.get(secondIdx));
                    firstIdx -= 1;
                    secondIdx += 1;
                }

                if (isReflection) {
                    return i;
                }
            }
        }
        return 0;
    }

    private long getColumnsScore(ArrayList<String> strings) {
        int colIdx = 0;
        ArrayList<String> newStrings = new ArrayList<>(Stream.generate(() -> "").limit(strings.get(0).length()).toList());

        while (colIdx < strings.get(0).length()) {
            for (String line : strings) {
                newStrings.set(colIdx, newStrings.get(colIdx) + line.charAt(colIdx));
            }
            colIdx++;
        }

        return getRowsScore(newStrings);
    }

    @Override
    protected void calculatePart2() {
        long sum = 0;
        System.out.printf("Part 2 - Total score %d%n", sum);
    }

}
