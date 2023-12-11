package year2023;

import base.DecBase;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.stream.Collectors;
import java.util.stream.Stream;


class Dec11 extends DecBase {


    public Dec11(String fileName) {
        super(fileName);
    }

    @Override
    public Dec11 readDefaultInput() {
        System.out.println("Reading default input.");
        inputStrings = new LinkedList<>(Stream.of(
                "...#......",
                        ".......#..",
                        "#.........",
                        "..........",
                        "......#...",
                        ".#........",
                        ".........#",
                        "..........",
                        ".......#..",
                        "#...#....."
                )
                .toList());
        return this;
    }

    private static class Point {
        final String name;
        final int x;
        final int y;

        Point(String name, int x, int y) {
            this.name = name;
            this.x = x;
            this.y = y;
        }
    }

    @Override
    protected void calculatePart1() {
        char[][] sky = new char[inputStrings.getFirst().length()][inputStrings.size()];
        int y = 0;
        for (String line : inputStrings) {
            sky[y++] = line.toCharArray();
        }
        //Utils.printGrid2(sky);
        //System.out.println("####################");

        sky = expandData(sky);

        //Utils.printGrid2(sky);
        //System.out.println("####################");

        ArrayList<Point> galaxies = new ArrayList<>();
        int number = 1;
        for (y = 0; y < sky.length; y++) {
            for (int x = 0; x < sky[y].length; x++) {
                if(sky[y][x] == '#') {
                    galaxies.add(new Point("" + number++, x, y));
                }
            }
        }

        ArrayList<Long> distances = new ArrayList<>();
        for (int i = 0; i < galaxies.size() - 1; i++) {
            Point point1 = galaxies.get(i);
            for (int j = i+1; j < galaxies.size(); j++) {
                Point point2 = galaxies.get(j);
                int distance = Math.abs(point1.x - point2.x) + Math.abs(point1.y - point2.y);
                distances.add((long) distance);
                //System.out.printf("Distance %s -> %s == %d%n", point1.name, point2.name, distance);
            }
        }

        long sum = distances.stream().mapToLong(it -> it).sum();
        System.out.printf("Part 1 - Total score %d%n", sum);
    }

    private char[][] expandData(char[][] sky) {
        ArrayList<String> output = new ArrayList<>(sky.length);

        for (int y = 0; y < sky.length; y++) {
            boolean addRow = true;
            for (int x = 0; x < sky[y].length; x++) {
                if (sky[y][x] == '#') {
                    addRow = false;
                    break;
                }
            }

            StringBuilder stringBuilder = new StringBuilder();
            for (int x = 0; x < sky[y].length; x++) {
                stringBuilder.append(sky[y][x]);
            }
            output.add(stringBuilder.toString());

            if (addRow) {
                output.add(Stream.generate(() -> ".").limit(sky[y].length).collect(Collectors.joining()));
            }
        }

        char[][] tmp = new char[output.size()][output.get(0).length()];
        int idx = 0;
        for (String line : output) {
            tmp[idx++] = line.toCharArray();
        }

        ArrayList<Integer> whereToAdd = new ArrayList<>();
        for (int x = 0; x < tmp[0].length; x++) {
            boolean addColumn = true;
            for (int y = 0; y < tmp.length; y++) {
                if (tmp[y][x] == '#') {
                    addColumn = false;
                    break;
                }
            }

            if (addColumn) {
                whereToAdd.add(x);
            }
        }

        output.clear();
        for (int y = 0; y < tmp.length; y++) {
            StringBuilder stringBuilder = new StringBuilder();
            for (int x = 0; x < tmp[y].length; x++) {
                stringBuilder.append(tmp[y][x]);
            }
            output.add(stringBuilder.toString());
        }

        ArrayList<String> out = new ArrayList<>(output.size());
        for (String line : output) {
            StringBuilder builder = new StringBuilder();
            idx = 0;
            for (char c : line.toCharArray()) {
                builder.append(c);
                if (whereToAdd.contains(idx)) {
                    builder.append(".");
                }
                idx++;
            }
            out.add(builder.toString());
        }

        char[][] result = new char[out.size()][out.get(0).length()];
        idx = 0;
        for (String line : out) {
            result[idx++] = line.toCharArray();
        }

        return result;
    }

    @Override
    protected void calculatePart2() {
        int cosmologicalConst = 1;
        int expansion = 1000000 - cosmologicalConst;
        char[][] sky = new char[inputStrings.getFirst().length()][inputStrings.size()];
        int y = 0;
        for (String line : inputStrings) {
            sky[y++] = line.toCharArray();
        }

        ArrayList<Integer> rowNumbers = new ArrayList<>();
        for (y = 0; y < sky.length; y++) {
            boolean add = true;
            for (int x = 0; x < sky[y].length; x++) {
                if (sky[y][x] == '#') {
                    add = false;
                    break;
                }
            }
            if (add) {
                rowNumbers.add(y);
            }
        }

        ArrayList<Integer> columnNumbers = new ArrayList<>();
        for (int x = 0; x < sky[0].length; x++) {
            boolean add = true;
            for (y = 0; y < sky.length; y++) {
                if (sky[y][x] == '#') {
                    add = false;
                    break;
                }
            }
            if (add) {
                columnNumbers.add(x);
            }
        }

        ArrayList<Point> galaxies = new ArrayList<>();
        int number = 1;
        for (y = 0; y < sky.length; y++) {
            for (int x = 0; x < sky[y].length; x++) {
                if(sky[y][x] == '#') {
                    galaxies.add(new Point("" + number++, x, y));
                }
            }
        }

        ArrayList<Long> distances = new ArrayList<>();
        for (int i = 0; i < galaxies.size() - 1; i++) {
            Point point1 = galaxies.get(i);
            for (int j = i+1; j < galaxies.size(); j++) {
                Point point2 = galaxies.get(j);
                int counter = 0;
                if (point1.x > point2.x) {
                    for (Integer col : columnNumbers) {
                        if (col > point2.x && col < point1.x) {
                            counter++;
                        }
                    }
                } else if (point1.x < point2.x) {
                    for (Integer col : columnNumbers) {
                        if (col > point1.x && col < point2.x) {
                            counter++;
                        }
                    }
                }
                long xDistance = Math.abs(point1.x - point2.x) + (long) expansion * counter;

                counter = 0;
                if (point1.y > point2.y) {
                    for (Integer row : rowNumbers) {
                        if (row > point2.y && row < point1.y) {
                            counter++;
                        }
                    }
                } else if (point1.y < point2.y) {
                    for (Integer row : rowNumbers) {
                        if (row > point1.y && row < point2.y) {
                            counter++;
                        }
                    }
                }

                long yDistance = Math.abs(point1.y - point2.y) + (long) expansion * counter;

                long distance = xDistance + yDistance;
                distances.add(distance);
                //System.out.printf("Distance %s -> %s == %d%n", point1.name, point2.name, distance);
            }
        }

        long sum = distances.stream().mapToLong(it -> it).sum();
        System.out.printf("Part 2 - Total score %d%n", sum);
    }

}
