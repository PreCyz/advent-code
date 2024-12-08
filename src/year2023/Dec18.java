package year2023;

import base.DecBase;
import utils.GridUtils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.stream.Stream;

public class Dec18 extends DecBase {

    public Dec18(int year) {
        super(year, 18);
    }

    public enum Direction {
        START, UP, DOWN, LEFT, RIGHT;

        static Direction valueOf(char c) {
            switch (c) {
                case 'U' -> {
                    return UP;
                }
                case 'D' -> {
                    return DOWN;
                }
                case 'L' -> {
                    return LEFT;
                }
                case 'R' -> {
                    return RIGHT;
                }
                default -> {
                    return START;
                }
            }
        }

        static Direction valueOfNumber(char c) {
            switch (c) {
                case '3' -> {
                    return UP;
                }
                case '1' -> {
                    return DOWN;
                }
                case '2' -> {
                    return LEFT;
                }
                case '0' -> {
                    return RIGHT;
                }
                default -> {
                    return START;
                }
            }
        }
    }

    private static class Trench {
        Direction direction;
        long meters;
        String hexadecimalRGB;

        public Trench(Direction direction, long meters, String hexadecimalRGB) {
            this.direction = direction;
            this.meters = meters;
            this.hexadecimalRGB = hexadecimalRGB;
        }

        static Trench valueOf(String line) {
            String[] split = line.split(" ");
            return new Trench(
                    Direction.valueOf(split[0].charAt(0)),
                    Integer.parseInt(split[1]),
                    split[2].replace(")", "").replace("(", "")
            );
        }
    }

    @Override
    public Dec18 readDefaultInput() {
        System.out.println("Reading default input.");
        inputStrings = new LinkedList<>(Stream.of(
                "R 6 (#70c710)",
                "D 5 (#0dc571)",
                "L 2 (#5713f0)",
                "D 2 (#d2c081)",
                "R 2 (#59c680)",
                "D 2 (#411b91)",
                "L 5 (#8ceee2)",
                "U 2 (#caa173)",
                "L 1 (#1b58a2)",
                "U 2 (#caa171)",
                "R 2 (#7807d2)",
                "U 3 (#a77fa3)",
                "L 2 (#015232)",
                "U 2 (#7a21e3)"
        ).toList());
        return this;
    }

    @Override
    protected void calculatePart1() {
        //brutalForceApproach();

        Point startPoint = new Point(0, 0);
        ArrayList<Point> points = new ArrayList<>(inputStrings.size() + 1);
        points.add(startPoint);

        int boundaryPoints = 0;
        for (String line : inputStrings) {
            String[] split = line.split(" ");
            Direction dir = Direction.valueOf(split[0].charAt(0));
            int distance = Integer.parseInt(split[1]);

            Point newPoint = createPoint(dir, startPoint, distance);
            points.add(newPoint);
            startPoint = newPoint;
            boundaryPoints += distance;
        }

        long sum = getSum(points, boundaryPoints);

        System.out.printf("Part 1 - Total score %d%n", sum);
    }

    private Point createPoint(Direction dir, Point startPoint, int distance) {
        Point newPoint = null;
        switch (dir) {
            case UP -> newPoint = new Point(startPoint.x, startPoint.y - distance);
            case DOWN -> newPoint = new Point(startPoint.x, startPoint.y + distance);
            case RIGHT -> newPoint = new Point(startPoint.x + distance, startPoint.y);
            case LEFT -> newPoint = new Point(startPoint.x - distance, startPoint.y);
        }
        return newPoint;
    }

    private static long getSum(ArrayList<Point> points, int boundaryPoints) {
        long A = 0;
        for (int i = 0; i < points.size(); i++) {
            Point pi = points.get(i);
            Point pi1 = pi;
            if (i - 1 >= 0) {
                pi1 = points.get(i - 1);
            }
            Point pi2 = pi;
            if (i + 1 < points.size()) {
                pi2 = points.get(i + 1);
            }
            A += pi.x * (pi2.y - pi1.y);
        }
        A = Math.abs(A) / 2;

        long internalPoints = A - boundaryPoints /2 + 1;
        return internalPoints + boundaryPoints;
    }

    private void brutalForceApproach() {
        ArrayList<Trench> trenches = new ArrayList<>(inputStrings.stream().map(Trench::valueOf).toList());

        int maxXR = maxXR(trenches);
        int maxXL = maxXL(trenches);
        int maxYU = maxYU(trenches);
        int maxYD = maxYD(trenches);

        Trench[][] dugOutGrid = createDugOutGrid(maxYD, maxYU, maxXR, maxXL, trenches);

        final char[][] grid = buildGrid(dugOutGrid);
        GridUtils.writeToFile(grid);

        final char[][] finalDugout = createLavaDugout(grid);
        GridUtils.writeToFile(finalDugout);

        long sum = countTrench(finalDugout);
        System.out.printf("Part 1 - Total score %d%n", sum);
    }

    private int maxXR(ArrayList<Trench> trenches) {
        int max = 0;
        int sum = 0;
        for (Trench trench : trenches) {
            if (trench.direction == Direction.LEFT) {
                sum -= trench.meters;
            } else if (trench.direction == Direction.RIGHT) {
                sum += trench.meters;
            }
            if (max < sum) {
                max = sum;
            }
        }
        return max;
    }

    private int maxXL(ArrayList<Trench> trenches) {
        int max = 0;
        int sum = 0;
        for (Trench trench : trenches) {
            if (trench.direction == Direction.LEFT) {
                sum += trench.meters;
            } else if (trench.direction == Direction.RIGHT) {
                sum -= trench.meters;
            }
            if (max < sum) {
                max = sum;
            }
        }
        return max;
    }

    private int maxYD(ArrayList<Trench> trenches) {
        int max = 0;
        int sum = 0;
        for (Trench trench : trenches) {
            if (trench.direction == Direction.UP) {
                sum -= trench.meters;
            } else if (trench.direction == Direction.DOWN) {
                sum += trench.meters;
            }
            if (max < sum) {
                max = sum;
            }
        }
        return max;
    }

    private int maxYU(ArrayList<Trench> trenches) {
        int max = 0;
        int sum = 0;
        for (Trench trench : trenches) {
            if (trench.direction == Direction.UP) {
                sum += trench.meters;
            } else if (trench.direction == Direction.DOWN) {
                sum -= trench.meters;
            }
            if (max < sum) {
                max = sum;
            }
        }
        return max;
    }

    private Trench[][] createDugOutGrid(int maxYD, int maxYU, int maxXR, int maxXL, ArrayList<Trench> trenches) {
        int x = maxXL;
        int y = maxYU;

        Trench[][] dugOut = new Trench[maxYD + maxYU + 1][maxXR + maxXL + 1];
        dugOut[y][x] = new Trench(Direction.START, 1, "");

        for (Trench trench : trenches) {
            switch (trench.direction) {
                case RIGHT -> {
                    for (int i = x + 1; i <= x + trench.meters; ++i) {
                        dugOut[y][i] = new Trench(trench.direction, 1, trench.hexadecimalRGB);
                    }
                    x += trench.meters;
                }
                case LEFT -> {
                    for (int i = x - 1; i >= x - trench.meters; --i) {
                        dugOut[y][i] = new Trench(trench.direction, 1, trench.hexadecimalRGB);
                    }
                    x -= trench.meters;
                }
                case UP -> {
                    for (int i = y - 1; i >= y - trench.meters; --i) {
                        dugOut[i][x] = new Trench(trench.direction, 1, trench.hexadecimalRGB);
                    }
                    y -= trench.meters;
                }
                case DOWN -> {
                    for (int i = y + 1; i <= y + trench.meters; ++i) {
                        dugOut[i][x] = new Trench(trench.direction, 1, trench.hexadecimalRGB);
                    }
                    y += trench.meters;
                }
            }
        }
        return dugOut;
    }

    private char[][] buildGrid(Trench[][] digOut) {
        char[][] grid = new char[digOut.length][digOut[0].length];
        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[y].length; x++) {
                if (digOut[y][x] != null) {
                    grid[y][x] = '#';
                } else {
                    grid[y][x] = '.';
                }
            }
        }
        return grid;
    }

    private char[][] createLavaDugout(char[][] grid) {

        final char[][] result = new char[grid.length][grid[0].length];

        for (int y = 0; y < grid.length; y++) {

            String row = String.valueOf(grid[y]);
            if (row.contains(".") && row.contains("#")) {
                boolean beginning = false;
                int startIdx = -1;
                int endIdx = -1;

                for (int x = 0; x < grid[y].length; x++) {

                    boolean isSeries = x + 1 < grid[y].length && grid[y][x] == '#' && grid[y][x + 1] == '#';
                    if (isSeries) {

                        int seriesStart = x;

                        while (x < grid[y].length && grid[y][x] == '#') {
                            result[y][x] = grid[y][x];
                            x++;
                        }

                        boolean skipSeries = y + 1 < grid.length && grid[y + 1][seriesStart] == '#' && grid[y + 1][x - 1] == '#';
                        skipSeries |= y - 1 >= 0 && grid[y - 1][seriesStart] == '#' && grid[y - 1][x - 1] == '#';
                        if (!skipSeries) {
                            if (beginning) {
                                endIdx = x - 1;
                            } else {
                                beginning = true;
                                startIdx = x - 1;
                            }
                        }
                        x--;

                    } else if (grid[y][x] == '#') {
                        if (beginning) {
                            endIdx = x;
                        } else {
                            beginning = true;
                            startIdx = x;
                        }
                    } else {
                        result[y][x] = grid[y][x];
                    }

                    if (beginning && endIdx > 0 && endIdx < grid[y].length) {
                        for (int idx = startIdx; idx <= endIdx; idx++) {
                            result[y][idx] = '#';
                        }
                    }

                    if (startIdx >= 0 && endIdx >= 0 && endIdx - startIdx == 1) {
                        startIdx = endIdx;
                        endIdx = -1;
                    } else if (startIdx >= 0 && endIdx >= 0 && endIdx - startIdx > 1) {
                        beginning = false;
                        startIdx = -1;
                        endIdx = -1;
                    }
                }
            } else {
                result[y] = grid[y];
            }
        }

        return result;
    }

    private long countTrench(char[][] grid) {
        int counter = 0;
        for (char[] chars : grid) {
            for (char aChar : chars) {
                counter += aChar == '#' ? 1 : 0;
            }
        }
        return counter;
    }

    /** Solution is based on these 2 articles
     *  <a href="https://en.wikipedia.org/wiki/Shoelace_formula">Shoelace formula</a>
     *  <a href="https://en.wikipedia.org/wiki/Pick%27s_theorem">Pick's theorem</a>
     */
    @Override
    protected void calculatePart2() {
        Point startPoint = new Point(0, 0);

        ArrayList<Point> points = new ArrayList<>(inputStrings.size() + 1);
        points.add(startPoint);

        int boundryPoints = 0;
        for (String line : inputStrings) {
            String[] split = line.split(" ");
            String color = split[2].replace(")", "")
                    .replace("(", "")
                    .replace("#", "");
            Direction dir = Direction.valueOfNumber(color.charAt(color.length() - 1));
            int distance = Long.decode( "0x" + color.substring(0, color.length() - 1)).intValue();

            Point newPoint = createPoint(dir, startPoint, distance);
            boundryPoints += distance;

            points.add(newPoint);
            startPoint = newPoint;
        }

        long sum = getSum(points, boundryPoints);

        System.out.printf("Part 2 - Total score %d%n", sum);
    }

    static class Point {
        long x;
        long y;

        Point(long x, long y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public String toString() {
            return "Point{" +
                    "x=" + x +
                    ", y=" + y +
                    '}';
        }
    }
}
