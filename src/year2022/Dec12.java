package year2022;

import base.DecBase;
import utils.Utils;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Stream;

class Dec12 extends DecBase {

    private static final char START_LOCATION = 'S';
    private static final char END_LOCATION = 'E';
    private static int MAX_Y;
    private static int MAX_X;
    private static char[][] ANSWER_GRID;
    private static char[][] GRID;
    private Point destination;
    private LinkedHashMap<String, Point> visitedMap;
    final Comparator<Point> elevationComparator = (o1, o2) -> {
        int el1 = getElevation(o1);
        int el2 = getElevation(o2);
        if (el1 > el2) {
            return 1;
        } else if (el1 < el2) {
            return -1;
        }
        return 0;
    };

    private static final char[] ELEVATION_VALUES = new char[]{
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v',
            'w', 'x', 'y', 'z'
    };

    enum Direction {
        UP('^'),
        DOWN('v'),
        LEFT('<'),
        RIGHT('>');

        final char val;

        Direction(char val) {
            this.val = val;
        }
    }

    static class Move {
        Point from;
        Point to;
        Direction direction;
        int elevation;

        Move(Point from, Point to, Direction direction, int elevation) {
            this.from = from;
            this.to = to;
            this.direction = direction;
            this.elevation = elevation;
        }

        boolean same(Point point) {
            return this.from.x == point.x && this.from.y == point.y;
        }
    }

    static class Point {
        int x;
        int y;
        Point parent;
        Direction direction;
        boolean visited;

        public Point(int x, int y) {
            this(x, y, null);
        }

        Point(int x, int y, Point parent) {
            this.x = x;
            this.y = y;
            this.parent = parent;
            direction = null;
        }

        boolean same(Point point) {
            return this.x == point.x && this.y == point.y;
        }

        @Override
        public String toString() {
            return "Point{" +
                    "x=" + x +
                    ", y=" + y +
                    ", move=" + Optional.ofNullable(direction).map(m -> m.val).orElseGet(() -> '_') +
                    ", parent=" + Optional.ofNullable(parent).map(Point::toString).orElseGet(() -> "_") +
                    '}';
        }
    }

    protected Dec12(int year) {
        super(year, 12);
    }

    @Override
    public Dec12 readDefaultInput() {
        System.out.println("Reading default input.");
        inputStrings = new LinkedList<>(Stream.of(
                "Sabqponm",
                "abcryxxl",
                "accszExk",
                "acctuvwj",
                "abdefghi"
        ).toList());
        return this;
    }

    @Override
    protected void calculatePart1() {
        MAX_Y = inputStrings.size();
        MAX_X = inputStrings.get(0).length();
        GRID = new char[MAX_X][MAX_Y];
        for (int y = inputStrings.size() - 1; y >= 0; y--) {
            String line = inputStrings.get(y);
            final char[] chars = line.toCharArray();
            for (int x = 0; x < chars.length; x++) {
                GRID[x][MAX_Y - y - 1] = chars[x];

            }
        }
        visitedMap = new LinkedHashMap<>();
        Utils.printGrid(GRID);

        Point startPoint = findPoint(GRID, START_LOCATION);
        Point endPoint = findPoint(GRID, END_LOCATION);

        findNextPoint(startPoint, endPoint);
        updateAnswerGrid();
        //Utils.printGrid(ANSWER_GRID);

        System.out.printf("Part 1: %n");
    }

    private void updateAnswerGrid() {
        char[][] answerGrid = initArray(MAX_Y);

        if (destination == null) {
            System.out.println("Could not find destination");
            return;
        }

        Point point = destination;
        answerGrid[point.x][point.y] = 'E';
        while (point.parent != null) {
            point = point.parent;
            answerGrid[point.x][point.y] = point.direction.val;
        }
    }

    private Point findPoint(char[][] grid, char value) {
        Point point = new Point(0, 0, null);
        for (int y = 0; y < grid[0].length; y++) {
            for (int x = 0; x < grid.length; x++) {
                if (grid[x][y] == value) {
                    point = new Point(x, y, null);
                    break;
                }
            }
        }
        return point;
    }

    private char[][] initArray(int length) {
        char[][] output = new char[length][length];
        for (int y = 0; y < length; y++) {
            for (int x = 0; x < length; x++) {
                output[x][y] = '.';
            }
        }
        return output;
    }

    void findNextPoint(Point current, Point end) {
        if (current == null) {
            System.out.println("Point is null. Quiting.");
            return;
        }
        System.out.println(current);

        if (current.x == end.x && current.y == end.y) {
            System.out.println("Destination reached.");
            destination = current;
        } else {
            //visitedMap.put(current.x + "," + current.y, current);
            LinkedList<Move> moves = new LinkedList<>();
            final Consumer<Move> moveConsumer = it -> {
                final Optional<Move> any = moves.stream().filter(m -> m.elevation >= it.elevation).findAny();
                if (any.isPresent()) {
                    boolean doNotAdd = it.from.same(any.get().to);
                    //doNotAdd |= Math.abs(end.x - it.x)
                    moves.clear();
                    moves.add(it);
                }
            };
            moveRight(current).ifPresent(moves::add);
            moveLeft(current).ifPresent(moveConsumer);
            moveUp(current).ifPresent(moveConsumer);
            moveDown(current).ifPresent(moveConsumer);
            moves.forEach(m -> findNextPoint(m.to, end));
        }
    }

    private Optional<Move> moveLeft(Point current) {
        Optional<Move> move = Optional.empty();
        int x = current.x - 1;
        if (x >= 0) {
            final int elevation = getElevation(current, GRID[x][current.y]);
            boolean condition = current.parent == null && elevation == 0;
            condition |= Math.abs(elevation) <= 1 /*&& !visitedMap.containsKey(x + "," + current.y)*/;
            if (condition) {
                current.direction = Direction.LEFT;
                move = Optional.of(new Move(current, new Point(x, current.y, current), Direction.LEFT, elevation));
            }
        }
        return move;
    }

    private Optional<Move> moveRight(Point current) {
        Optional<Move> move = Optional.empty();
        int x = current.x + 1;
        if (MAX_X > x) {
            final int elevation = getElevation(current, GRID[x][current.y]);
            boolean condition = current.parent == null && elevation == 0;
            condition |= Math.abs(elevation) <= 1 /*&& !visitedMap.containsKey(x + "," + current.y)*/;
            if (condition) {
                current.direction = Direction.RIGHT;
                move = Optional.of(new Move(current, new Point(x, current.y, current), Direction.RIGHT, elevation));
            }
        }
        return move;
    }

    private Optional<Move> moveUp(Point current) {
        Optional<Move> move = Optional.empty();
        int y = current.y + 1;
        if (MAX_Y > y) {
            final int elevation = getElevation(current, GRID[current.x][y]);
            boolean condition = current.parent == null && elevation == 0;
            condition |= Math.abs(elevation) <= 1 /*&& !visitedMap.containsKey(current.x + "," + y)*/;
            if (condition) {
                current.direction = Direction.UP;
                move = Optional.of(new Move(current, new Point(current.x, y, current), Direction.UP, elevation));
            }
        }
        return move;
    }

    private Optional<Move> moveDown(Point current) {
        Optional<Move> move = Optional.empty();
        int y = current.y - 1;
        if (y >= 0) {
            final int elevation = getElevation(current, GRID[current.x][y]);
            boolean condition = current.parent == null && elevation == 0;
            condition |= Math.abs(elevation) <= 1 /*&& !visitedMap.containsKey(current.x + "," + y)*/;
            if (condition) {
                current.direction = Direction.DOWN;
                move = Optional.of(new Move(current, new Point(current.x, y, current), Direction.DOWN, elevation));
            }
        }
        return move;
    }

    int getElevation(Point point, char c) {
        int pointElevation = 0;
        int nextElevation = 0;
        for (int idx = 0; idx < ELEVATION_VALUES.length; idx++) {
            if (ELEVATION_VALUES[idx] == GRID[point.x][point.y]) {
                pointElevation = idx;
            }
            if (ELEVATION_VALUES[idx] == c) {
                nextElevation = idx;
            }
        }
        return nextElevation - pointElevation;
    }

    int getElevation(Point point) {
        int pointElevation = 0;
        for (int idx = 0; idx < ELEVATION_VALUES.length; idx++) {
            if (ELEVATION_VALUES[idx] == GRID[point.x][point.y]) {
                pointElevation = idx;
            }
        }
        return pointElevation;
    }
}
