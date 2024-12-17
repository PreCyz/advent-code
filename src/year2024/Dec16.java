package year2024;

import base.DecBase;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class Dec16 extends DecBase {

    public Dec16(int year) {
        super(year, 16);
    }

    @Override
    public Dec16 readDefaultInput() {
        System.out.println("Reading default input.");
        inputStrings = new LinkedList<>(Stream.of(
                "###############",
                "#.......#....E#",
                "#.#.###.#.###.#",
                "#.....#.#...#.#",
                "#.###.#####.#.#",
                "#.#.#.......#.#",
                "#.#.#####.###.#",
                "#...........#.#",
                "###.#.#####.#.#",
                "#...#.....#.#.#",
                "#.#.#.###.#.#.#",
                "#.....#...#.#.#",
                "#.###.#.#.#.#.#",
                "#S..#.....#...#",
                "###############"
        ).toList());
        return this;
    }

    static class Point {
        int x;
        int y;
        char value;
        boolean rotated;
        boolean visited;
        Direction direction;

        static Point valueOf(Point point) {
            return new Point(point.x, point.y, point.value);
        }

        public Point(int x, int y, char value) {
            this.x = x;
            this.y = y;
            this.value = value;
        }

        @Override
        public String toString() {
            return "{" +
                    "x=" + x +
                    ", y=" + y +
                    ", value=" + value +
                    ", rotated=" + rotated +
                    ", visited=" + visited +
                    ", direction=" + direction +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;

            Point point = (Point) o;
            return x == point.x && y == point.y && value == point.value && rotated == point.rotated && visited == point.visited && direction == point.direction;
        }

        @Override
        public int hashCode() {
            int result = x;
            result = 31 * result + y;
            result = 31 * result + value;
            result = 31 * result + Boolean.hashCode(rotated);
            result = 31 * result + Boolean.hashCode(visited);
            return result;
        }
    }

    enum Direction {
        UP(0, -1),
        DOWN(0, 1),
        LEFT(-1, 0),
        RIGHT(1, 0);

        final int mvX;
        final int mvY;

        Direction(int mvX, int mvY) {
            this.mvX = mvX;
            this.mvY = mvY;
        }
    }

    @Override
    protected void calculatePart1() {
        ArrayList<Point> maze = new ArrayList<>(inputStrings.getFirst().length() * inputStrings.size());
        int y = 0;
        char[][] grid = new char[inputStrings.size()][inputStrings.getFirst().length()];
        for (String input : inputStrings) {
            char[] charArray = input.toCharArray();
            for (int x = 0; x < input.length(); x++) {
                Point point = new Point(x, y, charArray[x]);
                if (charArray[x] == 'S') {
                    point.direction = Direction.RIGHT;
                }
                maze.add(point);
                grid[y][x] = charArray[x];
            }
            y++;
        }
        maze.trimToSize();

        Point startPoint = maze.stream().filter(p -> p.value == 'S').findFirst().get();

        List<Set<Point>> allPaths = new ArrayList<>(findPath(startPoint, maze));
        long sum = 0;
        System.out.printf("Part 1 - Sum %d%n", sum);
    }

    private List<LinkedHashSet<Point>> findPath(Point startPoint, ArrayList<Point> maze) {
        List<LinkedHashSet<Point>> result = new ArrayList<>();
        Set<Point> nextPoints = findNextPoints(startPoint, maze);

        List<Point> end = nextPoints.stream().filter(point -> point.value == 'E').toList();
        if (!end.isEmpty()) {
            result.add(new LinkedHashSet<>(end));
        }
        List<Point> rest = nextPoints.stream().filter(point -> point.value != 'E').toList();

        for (Point nextPoint : rest) {
            maze.stream().filter(p -> startPoint.x == p.x && startPoint.y == p.y).forEach(p -> p.visited = true);

            Point start = Point.valueOf(startPoint);
            start.visited = true;

            List<LinkedHashSet<Point>> paths = findPath(nextPoint, maze);
            for (LinkedHashSet<Point> path : paths) {
                path.addFirst(nextPoint);
                if (nextPoint.direction != start.direction) {
                    start.rotated = true;
                    path.addFirst(start);
                }
            }
            result.addAll(paths);
        }
        return result;
    }

    private Set<Point> findNextPoints(final Point startPoint, final ArrayList<Point> maze) {
        Set<Point> points = new HashSet<>();
        maze.stream()
                .filter(p -> p.x == startPoint.x + Direction.RIGHT.mvX)
                .filter(p -> p.y == startPoint.y)
                .filter(p -> p.value != '#')
                .filter(p -> !p.visited)
                .findFirst()
                .ifPresent( p -> {
                    p.direction = Direction.RIGHT;
                    points.add(p);
                });
        maze.stream()
                .filter(p -> p.x == startPoint.x + Direction.LEFT.mvX)
                .filter(p -> p.y == startPoint.y)
                .filter(p -> p.value != '#')
                .filter(p -> !p.visited)
                .findFirst()
                .ifPresent( p -> {
                    p.direction = Direction.LEFT;
                    points.add(p);
                });
        maze.stream()
                .filter(p -> p.y == startPoint.y + Direction.UP.mvY)
                .filter(p -> p.x == startPoint.x)
                .filter(p -> p.value != '#')
                .filter(p -> !p.visited)
                .findFirst()
                .ifPresent( p -> {
                    p.direction = Direction.UP;
                    points.add(p);
                });
        maze.stream()
                .filter(p -> p.y == startPoint.y + Direction.DOWN.mvY)
                .filter(p -> p.x == startPoint.x)
                .filter(p -> p.value != '#')
                .filter(p -> !p.visited)
                .findFirst()
                .ifPresent( p -> {
                    p.direction = Direction.DOWN;
                    points.add(p);
                });
        return points.stream().filter(p -> !(p.x == startPoint.x && p.y == startPoint.y)).collect(Collectors.toSet());
    }

    @Override
    protected void calculatePart2() {

//        System.out.printf("Part 2 - Sum[%b] %d%n", move, move);
    }
}