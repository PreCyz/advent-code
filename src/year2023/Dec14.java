package year2023;

import base.DecBase;
import utils.Sequence;
import utils.Utils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

class Dec14 extends DecBase {

    public Dec14(int year) {
        super(year, 14);
    }

    @Override
    public Dec14 readDefaultInput() {
        System.out.println("Reading default input.");
        inputStrings = new LinkedList<>(Stream.of(
                        "O....#....",
                        "O.OO#....#",
                        ".....##...",
                        "OO.#O....O",
                        ".O.....O#.",
                        "O.#..O.#.#",
                        "..O..#O..O",
                        ".......O..",
                        "#....###..",
                        "#OO..#...."
                )
                .toList());
        return this;
    }

    @Override
    protected void calculatePart1() {
        ArrayList<String> input = Utils.transpose(new ArrayList<>(inputStrings));

        ArrayList<String> output = tiltNorth(input);

        long sum = getSum(output);
        System.out.printf("Part 1 - Total score %d%n", sum);
    }

    private ArrayList<String> tiltNorth(List<String> input) {
        ArrayList<String> output = new ArrayList<>(input);

        for (int j = 0; j < input.size(); ++j) {
            String row = input.get(j);
            if (!row.contains("O") || !row.contains(".")) {
                continue;
            }
            for (int i = row.length() - 1; i >= 0; --i) {
                char c = row.charAt(i);
                if (c == 'O') {
                    int rockIdx = findNextRockDec(output.get(j), i - 1);
                    int freeSpotIdx = findNextFreeSpotDec(rockIdx, output.get(j), i - 1);
                    if (i - 1 >= 0 && freeSpotIdx > rockIdx) {
                        char[] newValue = output.get(j).toCharArray();
                        newValue[freeSpotIdx] = 'O';
                        newValue[i] = '.';
                        output.set(j, String.valueOf(newValue));
                    }
                }
            }
        }
        return output;
    }

    private int findNextRockDec(String row, int beginIdx) {
        for (int i = beginIdx; i >= 0; --i) {
            if (row.charAt(i) == '#') {
                return i;
            }
        }
        return -1;
    }

    private int findNextFreeSpotDec(int rockIdx, String row, int beginIdx) {
        int freeSpotIdx = -1;
        for (int i = beginIdx; i >= 0; --i) {
            if (i <= rockIdx) {
                break;
            }
            if (row.charAt(i) == '.') {
                freeSpotIdx = i;
            }
        }
        return freeSpotIdx;
    }

    @Override
    protected void calculatePart2() {
        final int numberOfCycles = 1000000000;

        ArrayList<String> output = new ArrayList<>(inputStrings);
        ArrayList<Long> sums = new ArrayList<>(numberOfCycles);

        Sequence sequence = new Sequence();

        for (int cycle = 1; cycle <= numberOfCycles; ++cycle) {
            output = Utils.transpose(tiltNorth(Utils.transpose(output)));
            output = tiltWest(output);
            output = Utils.transpose(tiltSouth(Utils.transpose(output)));
            output = tiltEast(output);
            sums.add(getSum(Utils.transpose(output)));

            if (cycle % 250 == 0) {
                sequence = Sequence.findSequence(sums);
                break;
            }
        }

        int number = (numberOfCycles - 1 - sequence.startIdx) % sequence.length;
        long sum = sums.get(number + sequence.startIdx);
        //right answer is 93102
        System.out.printf("Part 2 - Total score %d%n", sum);
    }

    private static long getSum(ArrayList<String> output) {
        long sum = 0;
        for (String row : output) {
            char[] charArray = row.toCharArray();
            for (int j = 0; j < charArray.length; j++) {
                char c = charArray[j];
                sum += c == 'O' ? charArray.length - j : 0;
            }
        }
        return sum;
    }

    private ArrayList<String> tiltWest(List<String> input) {
        return tiltNorth(input);
    }

    private int findNextRockInc(String row, int beginIdx) {
        for (int i = beginIdx; i < row.length(); ++i) {
            if (row.charAt(i) == '#') {
                return i;
            }
        }
        return row.length();
    }

    private int findNextFreeSpotInc(int rockIdx, String row, int beginIdx) {
        int freeSpotIdx = row.length();
        for (int i = beginIdx; i < row.length(); ++i) {
            if (i >= rockIdx) {
                break;
            }
            if (row.charAt(i) == '.') {
                freeSpotIdx = i;
            }
        }
        return freeSpotIdx;
    }

    private ArrayList<String> tiltSouth(List<String> input) {
        ArrayList<String> output = new ArrayList<>(input);

        for (int j = 0; j < input.size(); ++j) {
            String row = input.get(j);
            if (!row.contains("O") || !row.contains(".")) {
                continue;
            }
            for (int i = 0; i < row.length(); ++i) {
                char c = row.charAt(i);
                if (c == 'O') {
                    int rockIdx = findNextRockInc(output.get(j), i + 1);
                    int freeSpotIdx = findNextFreeSpotInc(rockIdx, output.get(j), i + 1);
                    if (i + 1 < row.length() && freeSpotIdx < rockIdx) {
                        char[] newValue = output.get(j).toCharArray();
                        newValue[freeSpotIdx] = 'O';
                        newValue[i] = '.';
                        output.set(j, String.valueOf(newValue));
                    }
                }
            }
        }
        return output;
    }

    private ArrayList<String> tiltEast(List<String> output) {
        return tiltSouth(output);
    }

}
