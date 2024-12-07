package year2024;

import base.DecBase;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

class Dec6 extends DecBase {

    public Dec6(int year) {
        super(year, 6);
    }

    enum Direction {
        UP(0, -1),
        DOWN( 0, 1),
        LEFT( -1, 0),
        RIGHT( 1, 0);

        int mvX;
        int mvY;

        Direction(int mvX, int mvY) {
            this.mvX = mvX;
            this.mvY = mvY;
        }
    }

    record Point(int x, int y) { }

    record Point2(int x, int y, Direction direction) { }

    @Override
    public Dec6 readDefaultInput() {
        System.out.println("Reading default input.");
        inputStrings = new LinkedList<>(Stream.of(
                "....#.....",
                ".........#",
                "..........",
                "..#.......",
                ".......#..",
                "..........",
                ".#..^.....",
                "........#.",
                "#.........",
                "......#..."
        ).toList());
        return this;
    }

    @Override
    protected void calculatePart1() {
        char[][] grid = new char[inputStrings.size()][inputStrings.getFirst().length()];
        int column = 0;
        Direction direction = Direction.UP;
        Point point = null;
        Set<Point> visited = new HashSet<>();
        for (String line : inputStrings) {
            for (int row = 0; row < line.length(); row++) {
                grid[column][row] = line.charAt(row);
                if (grid[column][row] == '^') {
                    point = new Point(row, column);
                }
            }
            column++;
        }

        visited.add(point);
        Optional<Point2> nextPoint = goToNextPoint(point, direction, grid);
        while (nextPoint.isPresent()) {
            point = new Point(nextPoint.get().x, nextPoint.get().y);
            visited.add(point);
            nextPoint = goToNextPoint(point, nextPoint.get().direction, grid);
        }

        System.out.printf("Part 1 - Sum %d%n", visited.size());
    }

    private Optional<Point2> goToNextPoint(Point point, Direction direction, char[][] grid) {
        Point2 nextPoint = new Point2(point.x + direction.mvX, point.y + direction.mvY, direction);
        if (nextPoint.x < 0 || nextPoint.x >= grid.length || nextPoint.y < 0 || nextPoint.y >= grid[0].length) {
            return Optional.empty();
        }
        if (grid[nextPoint.y][nextPoint.x] == '#') {
            switch (direction) {
                case UP: {
                    nextPoint = new Point2(point.x + 1, point.y, Direction.RIGHT);
                    break;
                }
                case DOWN: {
                    nextPoint = new Point2(point.x - 1, point.y, Direction.LEFT);
                    break;
                }
                case LEFT: {
                    nextPoint = new Point2(point.x, point.y - 1, Direction.UP);
                    break;
                }
                case RIGHT: {
                    nextPoint = new Point2(point.x, point.y + 1, Direction.DOWN);
                    break;
                }
            }
        }
        return Optional.of(nextPoint);
    }

    @Override
    protected void calculatePart2() {
        char[][] grid = new char[inputStrings.size()][inputStrings.getFirst().length()];
        int column = 0;
        Point2 startPoint = null;
        for (String line : inputStrings) {
            for (int row = 0; row < line.length(); row++) {
                grid[column][row] = line.charAt(row);
                if (grid[column][row] == '^') {
                    startPoint = new Point2(row, column, Direction.UP);
                }
            }
            column++;
        }

        Set<Point2> visited = new HashSet<>();
        long sum = 0;
        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[y].length; x++) {
                char c = grid[y][x];
                if (c != '#' && c != '^') {
                    grid[y][x] = '#';
                    visited.add(startPoint);

                    Optional<Point2> nextPoint = nextPoint(startPoint, grid);
                    while (nextPoint.isPresent()) {
                        if(visited.contains(nextPoint.get())) {
                            sum++;
                            nextPoint = Optional.empty();
                        } else {
                            visited.add(nextPoint.get());
                            nextPoint = nextPoint(nextPoint.get(), grid);
                        }
                    }
                    grid[y][x] = c;
                    visited.clear();
                }
            }
        }

        System.out.printf("Part 2 - Sum %d%n", sum);
    }

    private Optional<Point2> nextPoint(Point2 point, char[][] grid) {
        Point2 nextPoint = new Point2(point.x + point.direction.mvX, point.y + point.direction.mvY, point.direction);
        if (nextPoint.x < 0 || nextPoint.x >= grid.length || nextPoint.y < 0 || nextPoint.y >= grid[0].length) {
            return Optional.empty();
        }
        if (grid[nextPoint.y][nextPoint.x] == '#' && nextPoint.direction == point.direction) {
            Direction newDirection = switch (point.direction) {
                case UP -> Direction.RIGHT;
                case DOWN -> Direction.LEFT;
                case LEFT -> Direction.UP;
                case RIGHT -> Direction.DOWN;
            };
            return Optional.of(new Point2(point.x, point.y, newDirection));
        }
        return Optional.of(nextPoint);
    }
}