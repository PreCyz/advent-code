package year2023;

import base.DecBase;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Scanner;
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

    public DecBase readInput() throws IOException {
        System.out.printf("%nReading input from [%s]%n", getFileName());
        inputStrings = new LinkedList<>();
        try (Scanner scanner = new Scanner(new FileInputStream(getFileName()))) {
            while (scanner.hasNext()) {
                final String nextLine = scanner.nextLine();
                if (nextLine != null) {
                    inputStrings.add(nextLine);
                }
            }
        }
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
                sum += 100L * getScore(strings) + getScore(transpose(strings));
                strings.clear();
            }
        }
        System.out.printf("Part 1 - Total score %d%n", sum);
    }

    private int getScore(ArrayList<String> strings) {
        ArrayList<Long> sums = getSums(strings);

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

    private static ArrayList<Long> getSums(ArrayList<String> strings) {
        ArrayList<Long> sums = new ArrayList<>(strings.size());
        for (String row : strings) {
            char[] chars = row.replaceAll("\\.", "0").replaceAll("#", "1").toCharArray();
            long sum = 0;
            for (char aChar : chars) {
                sum += Long.parseLong(aChar + "");
            }
            sums.add(sum);
        }
        return sums;
    }

    private ArrayList<String> transpose(ArrayList<String> strings) {
        int colIdx = 0;
        ArrayList<String> newStrings = new ArrayList<>(Stream.generate(() -> "").limit(strings.get(0).length()).toList());

        while (colIdx < strings.get(0).length()) {
            for (String line : strings) {
                newStrings.set(colIdx, newStrings.get(colIdx) + line.charAt(colIdx));
            }
            colIdx++;
        }
        return newStrings;
    }

    @Override
    protected void calculatePart2() {
        long sum = 0;
        final ArrayList<String> strings = new ArrayList<>();
        Iterator<String> iterator = inputStrings.iterator();
        while (iterator.hasNext()) {
            String line = iterator.next();
            if (!line.isEmpty() && !line.equals("\n")) {
                strings.add(line);
            }
            if (line.isEmpty() || System.lineSeparator().equals(line) || !iterator.hasNext()) {
                System.out.println("Row smudge");
                sum += 100L * getScore(smudgeRows(strings)) + getScore(smudgeRows(transpose(strings)));
                strings.clear();
            }
        }
        System.out.printf("Part 2 - Total score %d%n", sum);
    }

    private ArrayList<String> smudgeRows(ArrayList<String> strings) {
        ArrayList<Long> sums = getSums(strings);
        ArrayList<String> input = new ArrayList<>(strings);

        boolean stopSearching = false;
        for (int i = 0; i < sums.size() - 1; i++) {
            for (int j = i + 1; j < sums.size(); j++) {
                if (i != j && Math.abs(sums.get(i) - sums.get(j)) == 1) {
                    String first = strings.get(i);
                    String second = strings.get(j);

                    boolean diffExists = false;
                    for (int k = 0; k < first.length(); k++) {
                        if (first.charAt(k) != second.charAt(k)) {
                            if (diffExists) {
                                diffExists = false;
                                break;
                            } else {
                                diffExists = true;
                            }
                        }
                    }

                    if (diffExists) {
                        int idx = 0;
                        while (first.charAt(idx) == second.charAt(idx)) {
                            idx++;
                        }
                        char[] value = first.toCharArray();
                        value[idx] = (value[idx] == '.' ? '#' : '.');
                        input.set(i, String.join("", String.valueOf(value)));
                        stopSearching = true;
                        System.out.printf("Found smudge%nold [%s]%nnew [%s]%n", first, String.valueOf(value));
                        break;
                    }
                }
            }
            if (stopSearching) {
                break;
            }
        }
        if (!stopSearching) {
            System.out.println("No smudge");
        }
        return input;
    }

}
