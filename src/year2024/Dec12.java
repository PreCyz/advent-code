package year2024;

import base.DecBase;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class Dec12 extends DecBase {

    public Dec12(int year) {
        super(year, 12);
    }

    static class Point {
        int x;
        int y;
        char value;
        boolean visited;

        public Point(int x, int y, char value) {
            this.x = x;
            this.y = y;
            this.value = value;
            this.visited = false;
        }

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;

            Point point = (Point) o;
            return x == point.x && y == point.y && value == point.value && visited == point.visited;
        }

        @Override
        public int hashCode() {
            int result = x;
            result = 31 * result + y;
            result = 31 * result + value;
            result = 31 * result + Boolean.hashCode(visited);
            return result;
        }

        @Override
        public String toString() {
            return "Point{" +
                    "x=" + x +
                    ", y=" + y +
                    ", value=" + value +
                    ", visited=" + visited +
                    '}';
        }
    }

    @Override
    public Dec12 readDefaultInput() {
        System.out.println("Reading default input.");
        inputStrings = new LinkedList<>(Stream.of(
                "AAAA",
                "BBCD",
                "BBCC",
                "EEEC"

                /*"RRRRIICCFF",
                "RRRRIICCCF",
                "VVRRRCCFFF",
                "VVRCCCJFFF",
                "VVVVCJJCFE",
                "VVIVCCJJEE",
                "VVIIICJJEE",
                "MIIIIIJJEE",
                "MIIISIJEEE",
                "MMMISSJEEE"*/
        ).toList());
        return this;
    }

    @Override
    protected void calculatePart1() {
        Set<Point> points = new LinkedHashSet<>();
        char[][] grid = new char[inputStrings.size()][inputStrings.getFirst().length()];
        int y = 0;
        for (String line : inputStrings) {
            for (int x = 0; x < line.length(); x++) {
                grid[y][x] = line.charAt(x);
                points.add(new Point(x, y, grid[y][x]));
            }
            y++;
        }

        Map<Character, Set<Point>> poligonsMap = new HashMap<>();
        for (Character c : points.stream().map(p -> p.value).toList()) {
            poligonsMap.put(c, points.stream()
                    .filter(p -> p.value == c)
                    .collect(Collectors.toCollection(LinkedHashSet::new))
            );
        }

        Map<Character, List<Set<Point>>> fields = new HashMap<>();
        poligonsMap.forEach((k, v) -> fields.put(k, new ArrayList<>()));

        for (Map.Entry<Character, Set<Point>> entry : poligonsMap.entrySet()) {
            for (Point point : entry.getValue()) {
                if (!point.visited) {
                    LinkedHashSet<Point> neighbours = findNeighbours(point, entry.getValue());
                    fields.get(entry.getKey()).add(neighbours);
                }
            }
        }

        long sum = 0;
        for (Set<Point> pointSet : fields.values().stream().flatMap(Collection::stream).toList()) {
            long perimeter = 0;
            for (Point point : pointSet) {
                perimeter += perimeterValue(point, pointSet);
            }
            sum += pointSet.size() * perimeter;
        }

        System.out.printf("Part 1 - Sum %d%n", sum);
    }

    private LinkedHashSet<Point> findNeighbours(Point point, Set<Point> wholeSet) {
        point.visited = true;
        LinkedHashSet<Point> result = new LinkedHashSet<>();
        result.add(point);
        LinkedHashSet<Point> neighbours = wholeSet.stream()
                .filter(p -> areNeighbours(p, point))
                .filter(p -> !p.visited)
                .collect(Collectors.toCollection(LinkedHashSet::new));
        for (Point neighbour : neighbours) {
            result.addAll(findNeighbours(neighbour, wholeSet));
        }
        return result;
    }

    private boolean areNeighbours(Point p1, Point p2) {
        return (p1.x - p2.x == 0 && Math.abs(p1.y - p2.y) == 1) || (p1.y - p2.y == 0 && Math.abs(p1.x - p2.x) == 1);
    }

    private long perimeterValue(Point p1, Set<Point> field) {
        int count = (int) field.stream().filter(p -> areNeighbours(p1, p)).count();
        return switch (count) {
            case 0 -> 4L;
            case 1 -> 3L;
            case 2 -> 2L;
            case 3 -> 1L;
            case 4 -> 0L;
            default -> throw new IllegalStateException("Unexpected value: " + count);
        };
    }

    @Override
    protected void calculatePart2() {
        Set<Point> points = new LinkedHashSet<>();
        char[][] grid = new char[inputStrings.size()][inputStrings.getFirst().length()];
        int y = 0;
        for (String line : inputStrings) {
            for (int x = 0; x < line.length(); x++) {
                grid[y][x] = line.charAt(x);
                points.add(new Point(x, y, grid[y][x]));
            }
            y++;
        }

        Map<Character, Set<Point>> poligonsMap = new HashMap<>();
        for (Character c : points.stream().map(p -> p.value).toList()) {
            poligonsMap.put(c, points.stream()
                    .filter(p -> p.value == c)
                    .collect(Collectors.toCollection(LinkedHashSet::new))
            );
        }

        Map<Character, List<Set<Point>>> fields = new HashMap<>();
        poligonsMap.forEach((k, v) -> fields.put(k, new ArrayList<>()));

        for (Map.Entry<Character, Set<Point>> entry : poligonsMap.entrySet()) {
            for (Point point : entry.getValue()) {
                if (!point.visited) {
                    LinkedHashSet<Point> neighbours = findNeighbours(point, entry.getValue());
                    fields.get(entry.getKey()).add(neighbours);
                }
            }
        }

        long sum = 0;
        for (Set<Point> pointSet : fields.values().stream().flatMap(Collection::stream).toList()) {
            long perimeter = 0;
            for (Point point : pointSet) {
                perimeter += perimeterValue(point, pointSet);
            }
            sum += pointSet.size() * perimeter;
        }
        System.out.printf("Part 2 - Sum %d%n", sum);
    }

    /*int calculateSides(Set<Point> pointSet) {
        long distinctX = pointSet.stream().mapToInt(p -> p.x).distinct().count();
        long distinctY = pointSet.stream().mapToInt(p -> p.y).distinct().count();
        if (distinctX - distinctY == 0) {
            return 4;
        } else if (distinctY - distinctX != 0) {
            //x = 2, y = 3 2^3
            //x = 5, y = 5
        }

    }*/

}