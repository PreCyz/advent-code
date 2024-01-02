package year2023;

import base.DecBase;

import java.util.*;
import java.util.stream.Stream;

class Dec16 extends DecBase {

    public Dec16(int year) {
        super(year, 16);
    }

    private static class Point {
        final int x;
        final int y;
        final char value;
        boolean energized;
        Direction direction;

        Point(int x, int y, char value) {
            this.x = x;
            this.y = y;
            this.value = value;
            energized = false;
        }

        Point(int x, int y, char value, Direction direction) {
            this(x, y, value);
            this.direction = direction;
        }

        @Override
        public String toString() {
            return "Point{" +
                    "x=" + x +
                    ", y=" + y +
                    ", value=" + value +
                    ", direction=" + direction +
                    '}';
        }
    }

    private enum Direction {
        RIGHT, LEFT, UP, DOWN;
    }

    @Override
    public Dec16 readDefaultInput() {
        System.out.println("Reading default input.");
        inputStrings = new LinkedList<>(Stream.of(
                ".|...\\....",
                "|.-.\\.....",
                ".....|-...",
                "........|.",
                "..........",
                ".........\\",
                "..../.\\\\..",
                ".-.-/..|..",
                ".|....-|.\\",
                "..//.|...."
        ).toList());
        return this;
    }

    @Override
    protected void calculatePart1() {
        char[][] points = new char[inputStrings.size()][inputStrings.getFirst().length()];
        for (int y = 0; y < inputStrings.size(); ++y) {
            String line = inputStrings.get(y);
            for (int x = 0; x < line.toCharArray().length; ++x) {
                points[y][x] = line.charAt(x);
            }
        }

        LinkedHashMap<String, Point> energized = new LinkedHashMap<>();
        energized = processPoint(new Point(0, 0, points[0][0]), Direction.RIGHT, points, energized);

        //print2(points, energized);

        HashMap<String, Integer> result = new HashMap<>();
        energized.forEach((k, v) -> result.putIfAbsent(k.substring(0, k.indexOf(" ")), 1));

        long sum = result.size();
        System.out.printf("Part 1 - Total score %d%n", sum);
    }

    private LinkedHashMap<String, Point> processPoint(Point entryPoint, Direction direction, char[][] points, LinkedHashMap<String, Point> energized) {
        if (entryPoint == null || energized.containsKey(createKey(entryPoint, direction))) {
            return energized;
        }
        entryPoint.energized = true;
        entryPoint.direction = direction;
        energized.putIfAbsent(createKey(entryPoint, direction), entryPoint);

        Point nextPoint;
        switch (entryPoint.value) {
            case '.' -> {
                nextPoint = getNextPoint(entryPoint, direction, points);
                energized = processPoint(nextPoint, direction, points, energized);
            }
            case '\\' -> {
                switch (direction) {
                    case RIGHT -> {
                        nextPoint = getNextPoint(entryPoint, Direction.DOWN, points);
                        energized = processPoint(nextPoint, Direction.DOWN, points, energized);
                    }
                    case UP -> {
                        nextPoint = getNextPoint(entryPoint, Direction.LEFT, points);
                        energized = processPoint(nextPoint, Direction.LEFT, points, energized);
                    }
                    case LEFT -> {
                        nextPoint = getNextPoint(entryPoint, Direction.UP, points);
                        energized = processPoint(nextPoint, Direction.UP, points, energized);
                    }
                    case DOWN -> {
                        nextPoint = getNextPoint(entryPoint, Direction.RIGHT, points);
                        energized = processPoint(nextPoint, Direction.RIGHT, points, energized);
                    }
                }
            }
            case '/' -> {
                switch (direction) {
                    case RIGHT -> {
                        nextPoint = getNextPoint(entryPoint, Direction.UP, points);
                        energized = processPoint(nextPoint, Direction.UP, points, energized);
                    }
                    case UP -> {
                        nextPoint = getNextPoint(entryPoint, Direction.RIGHT, points);
                        energized = processPoint(nextPoint, Direction.RIGHT, points, energized);
                    }
                    case LEFT -> {
                        nextPoint = getNextPoint(entryPoint, Direction.DOWN, points);
                        energized = processPoint(nextPoint, Direction.DOWN, points, energized);
                    }
                    case DOWN -> {
                        nextPoint = getNextPoint(entryPoint, Direction.LEFT, points);
                        energized = processPoint(nextPoint, Direction.LEFT, points, energized);
                    }
                }
            }
            case '|' -> {
                switch (direction) {
                    case RIGHT, LEFT -> {
                        nextPoint = getNextPoint(entryPoint, Direction.UP, points);
                        energized = processPoint(nextPoint, Direction.UP, points, energized);
                        nextPoint = getNextPoint(entryPoint, Direction.DOWN, points);
                        energized = processPoint(nextPoint, Direction.DOWN, points, energized);
                    }
                    case UP -> {
                        nextPoint = getNextPoint(entryPoint, Direction.UP, points);
                        energized = processPoint(nextPoint, Direction.UP, points, energized);
                    }
                    case DOWN -> {
                        nextPoint = getNextPoint(entryPoint, Direction.DOWN, points);
                        energized = processPoint(nextPoint, Direction.DOWN, points, energized);
                    }
                }
            }
            case '-' -> {
                switch (direction) {
                    case RIGHT -> {
                        nextPoint = getNextPoint(entryPoint, Direction.RIGHT, points);
                        energized = processPoint(nextPoint, Direction.RIGHT, points, energized);
                    }
                    case LEFT -> {
                        nextPoint = getNextPoint(entryPoint, Direction.LEFT, points);
                        energized = processPoint(nextPoint, Direction.LEFT, points, energized);
                    }
                    case UP, DOWN -> {
                        nextPoint = getNextPoint(entryPoint, Direction.LEFT, points);
                        energized = processPoint(nextPoint, Direction.LEFT, points, energized);
                        nextPoint = getNextPoint(entryPoint, Direction.RIGHT, points);
                        energized = processPoint(nextPoint, Direction.RIGHT, points, energized);
                    }
                }
            }
        }

        return energized;
    }

    private String createKey(Point entryPoint, Direction direction) {
        return String.format("x=%s,y=%s %s", entryPoint.x, entryPoint.y, direction);
    }

    private Point getNextPoint(Point entryPoint, Direction direction, char[][] points) {
        switch (direction) {
            case RIGHT -> {
                return entryPoint.x + 1 >= points.length ? null : new Point(entryPoint.x + 1, entryPoint.y, points[entryPoint.y][entryPoint.x + 1], direction);
            }
            case LEFT -> {
                return entryPoint.x - 1 < 0 ? null : new Point(entryPoint.x - 1, entryPoint.y, points[entryPoint.y][entryPoint.x - 1], direction);
            }
            case UP -> {
                return entryPoint.y - 1 < 0 ? null : new Point(entryPoint.x, entryPoint.y - 1, points[entryPoint.y - 1][entryPoint.x], direction);
            }
            case DOWN -> {
                return entryPoint.y + 1 >= points[0].length ? null : new Point(entryPoint.x, entryPoint.y + 1, points[entryPoint.y + 1][entryPoint.x], direction);
            }
            default -> {
                return null;
            }
        }
    }

    private void print2(char[][] points, LinkedHashMap<String, Point> energized) {
        for (int y = 0; y < points.length; y++) {
            final int yy = y;
            for (int x = 0; x < points[y].length; x++) {
                final int xx = x;
                if (energized.keySet().stream().anyMatch(key -> getX(key) == xx && getY(key) == yy)) {
                    System.out.print("#");
                } else {
                    System.out.print(".");
                }
            }
            System.out.println();
        }
    }

    private int getY(String key) {
        return Integer.parseInt(key.substring(0, key.indexOf(" ")).split(",")[1].split("=")[1]);
    }

    private int getX(String key) {
        return Integer.parseInt(key.substring(0, key.indexOf(" ")).split(",")[0].split("=")[1]);
    }

    @Override
    protected void calculatePart2() {
        char[][] points = new char[inputStrings.size()][inputStrings.getFirst().length()];
        for (int y = 0; y < inputStrings.size(); ++y) {
            String line = inputStrings.get(y);
            for (int x = 0; x < line.toCharArray().length; ++x) {
                points[y][x] = line.charAt(x);
            }
        }

        long sum = 0;
        LinkedHashMap<String, Point> energized = new LinkedHashMap<>();
        HashMap<String, Integer> result = new HashMap<>();

        int x, y;
        for (int i = 0; i < points.length; ++i) {
            x = 0;
            y = i;
            if (y == 0) {
                energized = processPoint(new Point(x, y, points[y][x]), Direction.DOWN, points, energized);
                energized.forEach((k, v) -> result.putIfAbsent(k.substring(0, k.indexOf(" ")), 1));
                sum = sum < result.size() ? result.size() : sum;
                energized.clear();
                result.clear();
            } else if (y == points.length - 1) {
                energized = processPoint(new Point(x, y, points[y][x]), Direction.UP, points, energized);
                energized.forEach((k, v) -> result.putIfAbsent(k.substring(0, k.indexOf(" ")), 1));
                sum = sum < result.size() ? result.size() : sum;
                energized.clear();
                result.clear();
            }
            energized = processPoint(new Point(x, y, points[y][x]), Direction.RIGHT, points, energized);
            energized.forEach((k, v) -> result.putIfAbsent(k.substring(0, k.indexOf(" ")), 1));
            sum = sum < result.size() ? result.size() : sum;
            energized.clear();
            result.clear();

            x = points[y].length - 1;
            if (y == 0) {
                energized = processPoint(new Point(x, y, points[y][x]), Direction.DOWN, points, energized);
                energized.forEach((k, v) -> result.putIfAbsent(k.substring(0, k.indexOf(" ")), 1));
                sum = sum < result.size() ? result.size() : sum;
                energized.clear();
                result.clear();
            } else if (y == points.length - 1) {
                energized = processPoint(new Point(x, y, points[y][x]), Direction.UP, points, energized);
                energized.forEach((k, v) -> result.putIfAbsent(k.substring(0, k.indexOf(" ")), 1));
                sum = sum < result.size() ? result.size() : sum;
                energized.clear();
                result.clear();
            }
            energized = processPoint(new Point(x, y, points[y][x]), Direction.LEFT, points, energized);
            energized.forEach((k, v) -> result.putIfAbsent(k.substring(0, k.indexOf(" ")), 1));
            sum = sum < result.size() ? result.size() : sum;
            energized.clear();
            result.clear();
        }

        for (int i = 0; i < points[0].length; ++i) {
            x = i;
            y = 0;
            if (x == 0) {
                energized = processPoint(new Point(x, y, points[y][x]), Direction.RIGHT, points, energized);
                energized.forEach((k, v) -> result.putIfAbsent(k.substring(0, k.indexOf(" ")), 1));
                sum = sum < result.size() ? result.size() : sum;
                energized.clear();
                result.clear();
            } else if (x == points[y].length - 1) {
                energized = processPoint(new Point(x, y, points[y][x]), Direction.LEFT, points, energized);
                energized.forEach((k, v) -> result.putIfAbsent(k.substring(0, k.indexOf(" ")), 1));
                sum = sum < result.size() ? result.size() : sum;
                energized.clear();
                result.clear();
            }
            energized = processPoint(new Point(x, y, points[y][x]), Direction.DOWN, points, energized);
            energized.forEach((k, v) -> result.putIfAbsent(k.substring(0, k.indexOf(" ")), 1));
            sum = sum < result.size() ? result.size() : sum;
            energized.clear();
            result.clear();

            y = points[0].length - 1;
            if (x == 0) {
                energized = processPoint(new Point(x, y, points[y][x]), Direction.RIGHT, points, energized);
                energized.forEach((k, v) -> result.putIfAbsent(k.substring(0, k.indexOf(" ")), 1));
                sum = sum < result.size() ? result.size() : sum;
                energized.clear();
                result.clear();
            } else if (x == points[y].length - 1) {
                energized = processPoint(new Point(x, y, points[y][x]), Direction.LEFT, points, energized);
                energized.forEach((k, v) -> result.putIfAbsent(k.substring(0, k.indexOf(" ")), 1));
                sum = sum < result.size() ? result.size() : sum;
                energized.clear();
                result.clear();
            }
            energized = processPoint(new Point(x, y, points[y][x]), Direction.UP, points, energized);
            energized.forEach((k, v) -> result.putIfAbsent(k.substring(0, k.indexOf(" ")), 1));
            sum = sum < result.size() ? result.size() : sum;
            energized.clear();
            result.clear();
        }

        System.out.printf("Part 2 - Total score %d%n", sum);
    }
}
