package year2024;

import base.DecBase;

import java.util.*;
import java.util.stream.Stream;

class Dec4 extends DecBase {

    public Dec4(int year) {
        super(year, 4);
    }

    @Override
    public Dec4 readDefaultInput() {
        System.out.println("Reading default input.");
        inputStrings = new LinkedList<>(Stream.of(
                "MMMSXXMASM",
                "MSAMXMSMSA",
                "AMXSXMAAMM",
                "MSAMASMSMX",
                "XMASAMXAMM",
                "XXAMMXXAMA",
                "SMSMSASXSS",
                "SAXAMASAAA",
                "MAMMMXMMMM",
                "MXMXAXMASX"
        ).toList());
        return this;
    }

    private record Coordinates(int X, int Y, char letter, Direction direction) { }

    enum Direction {
        UP(0, -1),
        DOWN(0, 1),
        LEFT(-1, 0),
        RIGHT(1, 0),
        UP_RIGHT(1, -1),
        DOWN_RIGHT(1, 1),
        UP_LEFT(-1, -1),
        DOWN_LEFT(-1, 1);
        final int mvX;
        final int mvY;

        Direction(int mvX, int mvY) {
            this.mvX = mvX;
            this.mvY = mvY;
        }
    }

    @Override
    protected void calculatePart1() {
        char[][] grid = new char[inputStrings.size()][inputStrings.getFirst().length()];
        int column = 0;
        for (String line : inputStrings) {
            for (int row = 0; row < line.length(); row++) {
                grid[column][row] = line.charAt(row);
            }
            column++;
        }

        long sum = 0;
        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[0].length; x++) {
                char X = grid[y][x];
                if (X == 'X') {
                    sum += getXmasForDirection(y, x, grid, Direction.UP);
                    sum += getXmasForDirection(y, x, grid, Direction.UP_RIGHT);
                    sum += getXmasForDirection(y, x, grid, Direction.UP_LEFT);
                    sum += getXmasForDirection(y, x, grid, Direction.LEFT);
                    sum += getXmasForDirection(y, x, grid, Direction.RIGHT);
                    sum += getXmasForDirection(y, x, grid, Direction.DOWN);
                    sum += getXmasForDirection(y, x, grid, Direction.DOWN_RIGHT);
                    sum += getXmasForDirection(y, x, grid, Direction.DOWN_LEFT);
                }
            }
        }
        System.out.printf("Part 1 - Sum %d%n", sum);
    }

    private long getXmasForDirection(int y, int x, char[][] grid, Direction direction) {
        long sum = 0;
        List<Coordinates> ems = findNextLetter(x, y, 'M', grid, direction);
        if (!ems.isEmpty()) {
            for (Coordinates em : ems) {
                List<Coordinates> as = findNextLetter(em.X, em.Y, 'A', grid, direction);
                if (!as.isEmpty()) {
                    for (Coordinates a : as) {
                        List<Coordinates> ses = findNextLetter(a.X, a.Y, 'S', grid, direction);
                        sum += ses.size();
                    }
                }
            }
        }
        return sum;
    }

    private List<Coordinates> findNextLetter(int startX, int startY, char nextLetter, char[][] grid, Direction direction) {
        List<Coordinates> coordinates = new ArrayList<>(4);
        if (startY + direction.mvY >= 0 &&
                startY + direction.mvY < grid.length &&
                startX + direction.mvX >= 0 &&
                startX + direction.mvX < grid[startY + direction.mvY].length &&
                grid[startY + direction.mvY][startX + direction.mvX] == nextLetter) {
            coordinates.add(new Coordinates(startX + direction.mvX, startY + direction.mvY, nextLetter, direction));
        }
        return coordinates;
    }

    @Override
    protected void calculatePart2() {
        char[][] grid = new char[inputStrings.size()][inputStrings.getFirst().length()];
        int column = 0;
        for (String line : inputStrings) {
            for (int row = 0; row < line.length(); row++) {
                grid[column][row] = line.charAt(row);
            }
            column++;
        }

        Map<Integer, List<Coordinates>> masMap = new HashMap<>();
        int idx = 0;
        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[0].length; x++) {
                char M = grid[y][x];
                if (M == 'M') {
                    List<Coordinates> masForDirection = getMasForDirection(x, y, grid, Direction.UP_RIGHT);
                    if (!masForDirection.isEmpty()) {
                        masMap.put(idx++, masForDirection);
                    }
                    masForDirection = getMasForDirection(x, y, grid, Direction.UP_LEFT);
                    if (!masForDirection.isEmpty()) {
                        masMap.put(idx++, masForDirection);
                    }
                    masForDirection = getMasForDirection(x, y, grid, Direction.DOWN_RIGHT);
                    if (!masForDirection.isEmpty()) {
                        masMap.put(idx++, masForDirection);
                    }
                    masForDirection = getMasForDirection(x, y, grid, Direction.DOWN_LEFT);
                    if (!masForDirection.isEmpty()) {
                        masMap.put(idx++, masForDirection);
                    }
                }
            }
        }

        Map<Integer, List<Coordinates>> tmp = new HashMap<>(masMap);
        long sum = 0;
        for (Map.Entry<Integer, List<Coordinates>> entry : masMap.entrySet()) {
            Optional<Coordinates> A = entry.getValue().stream().filter(c -> c.letter == 'A').findFirst();
            if (A.isPresent() && tmp.containsKey(entry.getKey())) {
                tmp.remove(entry.getKey());
                Optional<Integer> key = Optional.empty();
                for (Map.Entry<Integer, List<Coordinates>> entry2 : tmp.entrySet()) {
                    Optional<Coordinates> any = entry2.getValue().stream()
                            .filter(c -> c.letter == A.get().letter)
                            .filter(c -> c.direction != A.get().direction)
                            .filter(c -> c.X == A.get().X)
                            .filter(c -> c.Y == A.get().Y)
                            .findAny();
                    if (any.isPresent()) {
                        sum++;
                        key = Optional.of(entry2.getKey());
                        break;
                    }
                }
                key.ifPresent(tmp::remove);
            }
        }
        System.out.printf("Part 2 - Sum %d%n", sum);
    }

    private List<Coordinates> getMasForDirection(int x, int y, char[][] grid, Direction direction) {
        List<Coordinates> coordinates = new ArrayList<>(3);
        Coordinates M = new Coordinates(x, y, 'M', direction);
        Optional<Coordinates> A = findMasNextLetter(M.X, M.Y, 'A', grid, direction);
        if (A.isPresent()) {
            Optional<Coordinates> S = findMasNextLetter(A.get().X, A.get().Y, 'S', grid, direction);
            if (S.isPresent()) {
                coordinates.add(M);
                coordinates.add(A.get());
                coordinates.add(S.get());
            }
        }
        return coordinates;
    }

    private Optional<Coordinates> findMasNextLetter(int startX, int startY, char nextLetter, char[][] grid, Direction direction) {
        if (startY + direction.mvY >= 0 &&
                startY + direction.mvY < grid.length &&
                startX + direction.mvX >= 0 &&
                startX + direction.mvX < grid[startY + direction.mvY].length &&
                grid[startY + direction.mvY][startX + direction.mvX] == nextLetter) {
            return Optional.of(new Coordinates(startX + direction.mvX, startY + direction.mvY, nextLetter, direction));
        }
        return Optional.empty();
    }
}