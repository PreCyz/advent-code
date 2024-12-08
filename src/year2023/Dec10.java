package year2023;

import base.DecBase;
import utils.GridUtils;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.stream.Stream;

class Dec10 extends DecBase {

    final LinkedList<Point> loopPoints = new LinkedList<>();
    char[][] maze;

    public Dec10(int year) {
        super(year, 10);
    }

    private enum Shape {
        NORTH_SOUTH('|', 0, 1),
        EAST_WEST('-', 1, 0),
        NORTH_EAST('L', 1, 1),
        NORTH_WEST('J', -1, 1),
        SOUTH_WEST('7', -1, -1),
        SOUTH_EAST('F', 1, -1),
        GROUND('.', 0, 0),
        S('S', 0, 0);

        final char shape;
        final int nextX;
        final int nextY;

        Shape(char shape, int nextX, int nextY) {
            this.shape = shape;
            this.nextX = nextX;
            this.nextY = nextY;
        }

        static Shape valueOf(char c) {
            for (Shape value : Shape.values()) {
                if (value.shape == c) {
                    return value;
                }
            }
            throw new IllegalArgumentException(String.format("This shape is not mapped [%c]", c));
        }
    }

    @Override
    public Dec10 readDefaultInput() {
        System.out.println("Reading default input.");
        inputStrings = new LinkedList<>(Stream.of(
                        /*".....",
                        ".S-7.",
                        ".|.|.",
                        ".L-J.",
                        "....."*/

                /*"..F7.",
                        ".FJ|.",
                        "SJ.L7",
                        "|F--J",
                        "LJ..."*/

                /*"...........",
                        ".S-------7.",
                        ".|F-----7|.",
                        ".||.....||.",
                        ".||.....||.",
                        ".|L-7.F-J|.",
                        ".|..|.|..|.",
                        ".L--J.L--J.",
                        "..........."*/

                ".F----7F7F7F7F-7....",
                        ".|F--7||||||||FJ....",
                        ".||.FJ||||||||L7....",
                        "FJL7L7LJLJ||LJ.L-7..",
                        "L--J.L7...LJS7F-7L7.",
                        "....F-J..F7FJ|L7L7L7",
                        "....L7.F7||L7|.L7L7|",
                        ".....|FJLJ|FJ|F7|.LJ",
                        "....FJL-7.||.||||...",
                        "....L---J.LJ.LJLJ..."
                )
                .toList());
        return this;
    }

    private static class Point {
        final int x;
        final int y;
        final char value;
        Shape shape;

        boolean visited;

        public Point(int x, int y, char value) {
            this.x = x;
            this.y = y;
            this.value = value;
            this.shape = Shape.valueOf(value);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Point point = (Point) o;
            if (x != point.x) return false;
            return y == point.y;
        }

        @Override
        public int hashCode() {
            int result = x;
            result = 31 * result + y;
            return result;
        }
    }

    @Override
    protected void calculatePart1() {
        loopPoints.clear();
        maze = new char[inputStrings.getFirst().length()][inputStrings.size()];
        int y = 0;
        for (String line : inputStrings) {
            maze[y++] = line.toCharArray();
        }

        Point start = whereIsStart(maze);
        ArrayList<Point> possibleNeighbours = determinePossibleNeighbours(start, maze);

        Point currentPosition = possibleNeighbours.get(0);
        Point previousPosition = start;

        loopPoints.add(start);
        loopPoints.add(currentPosition);
        long step = 0;
        do {
            step++;
            Point newCurrentPosition = getNextPosition(currentPosition, previousPosition, maze);
            previousPosition = currentPosition;
            currentPosition = newCurrentPosition;
            loopPoints.add(newCurrentPosition);

        } while (currentPosition != null && currentPosition.shape != Shape.GROUND && !currentPosition.equals(start));

        long sum = step % 2 == 0 ? step / 2 : (step / 2) + 1;
        System.out.printf("Part 1 - Total score %d%n", sum);
    }

    private Point getNextPosition(Point currentPosition, Point previous, char[][] maze) {

        char value;
        Point nextPoint1 = null, nextPoint2 = null;
        switch (currentPosition.shape) {
            case EAST_WEST: {
                if (currentPosition.x - 1 >= 0) {
                    value = maze[currentPosition.y][currentPosition.x - 1];
                    nextPoint1 = new Point(currentPosition.x - 1, currentPosition.y, value);
                }
                if (currentPosition.x + 1 < maze[currentPosition.y].length) {
                    value = maze[currentPosition.y][currentPosition.x + 1];
                    nextPoint2 = new Point(currentPosition.x + 1, currentPosition.y, value);
                }
                break;
            }
            case NORTH_SOUTH: {
                if (currentPosition.y - 1 >= 0) {
                    value = maze[currentPosition.y - 1][currentPosition.x];
                    nextPoint1 = new Point(currentPosition.x, currentPosition.y - 1, value);
                }
                if (currentPosition.y + 1 < maze.length) {
                    value = maze[currentPosition.y + 1][currentPosition.x];
                    nextPoint2 = new Point(currentPosition.x, currentPosition.y + 1, value);
                }
                break;
            }
            case NORTH_EAST: {
                if (currentPosition.x + 1 < maze[currentPosition.y].length) {
                    value = maze[currentPosition.y][currentPosition.x + 1];
                    nextPoint1 = new Point(currentPosition.x + 1, currentPosition.y, value);
                }
                if (currentPosition.y - 1 >= 0 ) {
                    value = maze[currentPosition.y - 1][currentPosition.x];
                    nextPoint2 = new Point(currentPosition.x, currentPosition.y - 1, value);
                }
                break;
            }
            case NORTH_WEST: {
                if (currentPosition.x - 1 >= 0) {
                    value = maze[currentPosition.y][currentPosition.x - 1];
                    nextPoint1 = new Point(currentPosition.x - 1, currentPosition.y, value);
                }
                if (currentPosition.y - 1 >= 0) {
                    value = maze[currentPosition.y - 1][currentPosition.x];
                    nextPoint2 = new Point(currentPosition.x, currentPosition.y - 1, value);
                }
                break;
            }
            case SOUTH_EAST: {
                if (currentPosition.x + 1 < maze[currentPosition.y].length) {
                    value = maze[currentPosition.y][currentPosition.x + 1];
                    nextPoint1 = new Point(currentPosition.x + 1, currentPosition.y, value);
                }
                if (currentPosition.y + 1 < maze.length) {
                    value = maze[currentPosition.y + 1][currentPosition.x];
                    nextPoint2 = new Point(currentPosition.x, currentPosition.y + 1, value);
                }

                break;
            }
            case SOUTH_WEST: {
                if (currentPosition.x - 1 >= 0) {
                    value = maze[currentPosition.y][currentPosition.x - 1];
                    nextPoint1 = new Point(currentPosition.x - 1, currentPosition.y, value);
                }
                if (currentPosition.y + 1 <= maze.length) {
                    value = maze[currentPosition.y + 1][currentPosition.x];
                    nextPoint2 = new Point(currentPosition.x, currentPosition.y + 1, value);
                }
                break;
            }
            default: {
                //GROUND,S
            }
        }
        if (previous.equals(nextPoint1)) {
            return nextPoint2;
        } else {
            return nextPoint1;
        }
    }

    private Point whereIsStart(char[][] maze) {
        for (int y = 0; y < maze.length; y++) {
            for (int x = 0; x < maze[0].length; x++) {
                if (maze[y][x] == 'S') {
                    return new Point(x, y, maze[y][x]);
                }
            }
        }
        throw new IllegalArgumentException("There is no S in the input");
    }

    private ArrayList<Point> determinePossibleNeighbours(Point start, char[][] maze) {
        ArrayList<Point> result = new ArrayList<>();
        char value;
        if (start.y - 1 >= 0) {
            value = maze[start.y - 1][start.x];
            Shape top = Shape.valueOf(value);
            if (EnumSet.of(Shape.SOUTH_EAST, Shape.SOUTH_WEST, Shape.NORTH_SOUTH).contains(top)) {
                result.add(new Point(start.x, start.y - 1, value));
            }
        }
        if (start.y + 1 < maze.length) {
            value = maze[start.y + 1][start.x];
            Shape bottom = Shape.valueOf(value);
            if (EnumSet.of(Shape.NORTH_EAST, Shape.NORTH_WEST, Shape.NORTH_SOUTH).contains(bottom)) {
                result.add(new Point(start.x, start.y + 1, value));
            }
        }

        if (start.x + 1 < maze[start.x].length) {
            value = maze[start.y][start.x + 1];
            Shape right = Shape.valueOf(value);
            if (EnumSet.of(Shape.NORTH_WEST, Shape.SOUTH_WEST, Shape.EAST_WEST).contains(right)) {
                result.add(new Point(start.x + 1, start.y, value));
            }
        }

        if (start.x - 1 >= 0) {
            value = maze[start.y][start.x - 1];
            Shape left = Shape.valueOf(value);
            if (EnumSet.of(Shape.NORTH_EAST, Shape.SOUTH_EAST, Shape.EAST_WEST).contains(left)) {
                result.add(new Point(start.x - 1, start.y, value));
            }
        }

        return result;
    }

    @Override
    protected void calculatePart2() {
        char[][] onlyLoop = new char[inputStrings.size()][inputStrings.getFirst().length()];
        char[][] finalGrid = new char[inputStrings.size()][inputStrings.getFirst().length()];
        for (int y = 0; y < inputStrings.size(); y++) {
            for (int x = 0; x < inputStrings.getFirst().length(); x++) {
                onlyLoop[y][x] = '.';
            }
        }
        System.out.println("##########################################");
        for (Point point : loopPoints) {
            onlyLoop[point.y][point.x] = point.value;
        }

        System.out.println("##########################################\n");
        GridUtils.printGridYFirst(onlyLoop);
        System.out.println("##########################################\n");

        long sum = 0;
        for (int y = 0; y < onlyLoop.length; y++) {
            for (int x = 0; x < onlyLoop[y].length; x++) {
                finalGrid[y][x] = onlyLoop[y][x];
                if (onlyLoop[y][x] == '.') {
                    boolean isInside = isInside(x, y, onlyLoop);
                    sum += isInside ? 1 : 0;
                    if (isInside) {
                        finalGrid[y][x] = 'I';
                    } else {
                        finalGrid[y][x] = 'O';
                    }
                }
            }
        }

        System.out.println("##########################################\n");
        GridUtils.printGridYFirst(finalGrid);
        System.out.println("##########################################\n");

        System.out.printf("Part 2 - Total score %d%n", sum);
    }

    private boolean isInside(int x, int y, char[][] grid) {
        boolean result = verticalCheck(x, y, grid);
        result &= horizontalCheck(x, y, grid);
        return result;
    }

    boolean verticalCheck(int x, int y, char[][] grid) {
        //even
        if (x == 0 || y == 0) {
            return false;
        }
        //bottom
        int bottomCounter = 0;
        for (int yy = y + 1; yy < grid.length; yy++) {
            if (grid[yy][x] == '-') {
                bottomCounter++;
            }
        }
        if (bottomCounter == 0) {
            return false;
        }
            //top
        int topCounter = 0;
        for (int yy = y - 1; yy >= 0; yy--) {
            if (grid[yy][x] == '-') {
                topCounter++;
            } else if (grid[yy][x] == 'F' && (grid[yy][x+1] == '7' || grid[yy][x+1] == 'J' || grid[yy][x+1] == '-')) {
                topCounter++;
            } else if (grid[yy][x] == 'L' && (grid[yy][x+1] == 'J' || grid[yy][x+1] == '7' || grid[yy][x+1] == '-')) {
                topCounter++;
            }
        }
        if (topCounter == 0) {
            return false;
        }

        return bottomCounter % 2 == 1 && topCounter % 2 == 1;
    }

    boolean horizontalCheck(int x, int y, char[][] grid) {
        //odd
        if (x == 0 || y == 0) {
            return false;
        }
        //right
        int rightCounter = 0;
        for (int xx = x + 1; xx < grid[y].length; xx++) {
            if (grid[y][xx] == '|') {
                rightCounter++;
            }
        }
        if (rightCounter == 0) {
            return false;
        }
        //left
        int leftCounter = 0;
        for (int xx = x - 1; xx >= 0; xx--) {
            if (grid[y][xx] == '|') {
                leftCounter++;
            }
        }
        if (leftCounter == 0) {
            return false;
        }

        return leftCounter % 2 == 1 && rightCounter % 2 == 1;
    }

}
