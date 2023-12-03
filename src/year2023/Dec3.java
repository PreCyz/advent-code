package year2023;

import base.DecBase;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.stream.Stream;

class Dec3 extends DecBase {

    public Dec3(String fileName) {
        super(fileName);
    }

    @Override
    public Dec3 readDefaultInput() {
        System.out.println("Reading default input.");
        inputStrings = new LinkedList<>(Stream.of(
                        "467..114..",
                        "...*......",
                        "..35..633.",
                        "......#...",
                        "617*......",
                        ".....+.58.",
                        "..592.....",
                        "......755.",
                        "...$.*....",
                        ".664.598.."
                )
                .toList());
        return this;
    }

    private static class Point {
        int x;
        int y;
        char _char;

        public Point(int x, int y, char value) {
            this.x = x;
            this.y = y;
            this._char = value;
        }
    }

    @Override
    protected void calculatePart1() {
        long sum = 0;
        final char[][] array = new char[inputStrings.getFirst().length()][inputStrings.size()];

        int y = 0;
        for (String line : inputStrings) {
            int x = 0;
            for (char c : line.toCharArray()) {
                array[x++][y] = c;
            }
            y++;
        }

        ArrayList<Point> points = new ArrayList<>(1000);

        for (y = 0; y < array.length; y++) {
            for (int x = 0; x < array[0].length; x++) {
                char ch = array[x][y];
                if (!Character.isDigit(ch) && ch != '.') {
                    points.add(new Point(x, y, ch));
                }
            }
        }

        ArrayList<Integer> numbers = new ArrayList<>();
        for (Point point : points) {
            HashSet<Integer> numbersAroundCharacter = new HashSet<>();
            if (isNumber(array, point.x + 1, point.y)) {
                numbersAroundCharacter.add(findNumber(array, point.x + 1, point.y));
            }
            if (isNumber(array, point.x - 1, point.y)) {
                numbersAroundCharacter.add(findNumber(array, point.x - 1, point.y));
            }
            if (isNumber(array, point.x, point.y + 1)) {
                numbersAroundCharacter.add(findNumber(array, point.x, point.y + 1));
            }
            if (isNumber(array, point.x, point.y - 1)) {
                numbersAroundCharacter.add(findNumber(array, point.x, point.y - 1));
            }
            if (isNumber(array, point.x - 1, point.y - 1)) {
                numbersAroundCharacter.add(findNumber(array, point.x - 1, point.y - 1));
            }
            if (isNumber(array, point.x + 1, point.y - 1)) {
                numbersAroundCharacter.add(findNumber(array, point.x + 1, point.y - 1));
            }
            if (isNumber(array, point.x + 1, point.y + 1)) {
                numbersAroundCharacter.add(findNumber(array, point.x + 1, point.y + 1));
            }
            if (isNumber(array, point.x - 1, point.y + 1)) {
                numbersAroundCharacter.add(findNumber(array, point.x - 1, point.y + 1));
            }
            numbers.addAll(numbersAroundCharacter);
        }

        System.out.printf("Part 1 - Total score %d%n", numbers.stream().mapToInt(it -> it).sum());
    }

    boolean isNumber(char[][] array, int x, int y) {
        try {
            return Character.isDigit(array[x][y]);
        } catch (Exception ex) {
            return false;
        }
    }

    int findNumber(char[][] array, int x, int y) {
        String number = String.valueOf(array[x][y]);
        int idx = 1;
        char nextCh;
        do {
            try {
                nextCh = array[x + idx][y];
                if (Character.isDigit(nextCh)) {
                    number += nextCh;
                    idx++;
                }
            } catch (Exception ex) {
                break;
            }
        } while (Character.isDigit(nextCh));

        idx = 1;
        do {
            try {
                nextCh = array[x - idx][y];
                if (Character.isDigit(nextCh)) {
                    number = nextCh + number;
                    idx++;
                }
            } catch (Exception ex) {
                break;
            }
        } while (Character.isDigit(nextCh));
        return Integer.parseInt(number);
    }

    @Override
    protected void calculatePart2() {
        int sum = 0;

        System.out.printf("Part 2 - Total score %d%n", sum);
    }

}
