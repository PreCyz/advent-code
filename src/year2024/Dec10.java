package year2024;

import base.DecBase;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

class Dec10 extends DecBase {

    public Dec10(int year) {
        super(year, 10);
    }

    enum Direction {
        UP(0, -1),
        DOWN( 0, 1),
        LEFT( -1, 0),
        RIGHT( 1, 0);

        final int mvX;
        final int mvY;

        Direction(int mvX, int mvY) {
            this.mvX = mvX;
            this.mvY = mvY;
        }
    }

    record Point(int x, int y, int value) { }

    @Override
    public Dec10 readDefaultInput() {
        System.out.println("Reading default input.");
        inputStrings = new LinkedList<>(Stream.of(
                "89010123",
                "78121874",
                "87430965",
                "96549874",
                "45678903",
                "32019012",
                "01329801",
                "10456732"
        ).toList());
        return this;
    }

    @Override
    protected void calculatePart1() {
        char[][] grid = new char[inputStrings.size()][inputStrings.getFirst().length()];
        Set<Point> zeros = new HashSet<>();
        int y = 0;
        for (String line : inputStrings) {
            for (int x = 0; x < line.length(); x++) {
                grid[y][x] = line.charAt(x);
                if (grid[y][x] == '0') {
                    zeros.add(new Point(x, y, Integer.parseInt("" + grid[y][x])));
                }
            }
            y++;
        }

        long sum = 0;
        for (Point zero : zeros) {
            Set<Point> nines = new HashSet<>(find9(zero, grid));
            sum += nines.size();
        }

        System.out.printf("Part 1 - Sum %d%n", sum);
    }

    private Set<Point> find9(Point destinationPoint, char[][] grid) {
        Set<Point> nextPoints = findNextPoints(destinationPoint, grid);
        Set<Point> nines = new HashSet<>(nextPoints.stream().filter(p -> p.value == 9).toList());

        nextPoints.removeIf(next -> next.value == 9);
        for (Point nextPoint : nextPoints) {
            nines.addAll(find9(nextPoint, grid));
        }
        return nines;
    }

    private Set<Point> findNextPoints(Point point, char[][] grid) {
        Set<Point> nextPoints = new HashSet<>();
        getPointForDirection(point, Direction.UP, grid).ifPresent(nextPoints::add);
        getPointForDirection(point, Direction.DOWN, grid).ifPresent(nextPoints::add);
        getPointForDirection(point, Direction.LEFT, grid).ifPresent(nextPoints::add);
        getPointForDirection(point, Direction.RIGHT, grid).ifPresent(nextPoints::add);
        return nextPoints;
    }

    Optional<Point> getPointForDirection(Point point, Direction direction, char[][] grid) {
        int newX = point.x + direction.mvX;
        int newY = point.y + direction.mvY;

        if (isWithinBounds(newX, newY, grid)) {
            int nextValue = Integer.parseInt("" + grid[newY][newX]);
            if (nextValue - 1 == point.value) {
                return Optional.of(new Point(newX, newY, nextValue));
            }
        }
        return Optional.empty();
    }

    private boolean isWithinBounds(int x, int y, char[][] grid) {
        return x >= 0 && x < grid[0].length && y >= 0 && y < grid.length;
    }

    @Override
    protected void calculatePart2() {
        char[][] grid = new char[inputStrings.size()][inputStrings.getFirst().length()];
        Set<Point> zeros = new LinkedHashSet<>();
        int y = 0;
        for (String line : inputStrings) {
            for (int x = 0; x < line.length(); x++) {
                grid[y][x] = line.charAt(x);
                if (grid[y][x] == '0') {
                    zeros.add(new Point(x, y, Integer.parseInt("" + grid[y][x])));
                }
            }
            y++;
        }

        long sum = 0;
        for (Point zero : zeros) {
            List<Set<Point>> hikingTrails = new ArrayList<>(findDistinctHikingTrails(zero, grid));
            sum += hikingTrails.size();
        }

        System.out.printf("Part 2 - Sum %d%n", sum);
    }

    private List<LinkedHashSet<Point>> findDistinctHikingTrails(Point sourcePoint, char[][] grid) {
        List<LinkedHashSet<Point>> result = new ArrayList<>();
        Set<Point> nextPoints = findNextPoints(sourcePoint, grid);

        Set<Point> nines = new HashSet<>(nextPoints.stream().filter(p -> p.value == 9).toList());
        for (Point nine : nines) {
            LinkedHashSet<Point> newHackingTrail = new LinkedHashSet<>();
            newHackingTrail.add(nine);
            result.add(newHackingTrail);
        }
        nextPoints.removeIf(next -> next.value == 9);

        for (Point nextPoint : nextPoints) {
            List<LinkedHashSet<Point>> distinctHikingTrails = findDistinctHikingTrails(nextPoint, grid);
            for (LinkedHashSet<Point> distinctHikingTrail : distinctHikingTrails) {
                distinctHikingTrail.addFirst(nextPoint);
            }
            result.addAll(distinctHikingTrails);
        }
        return result;
    }

}