package year2024;

import base.DecBase;
import year2024.coordinates.Direction;
import year2024.coordinates.LetterCoordinate;

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
        List<LetterCoordinate> ems = findNextLetter(x, y, 'M', grid, direction);
        if (!ems.isEmpty()) {
            for (LetterCoordinate em : ems) {
                List<LetterCoordinate> as = findNextLetter(em.X(), em.Y(), 'A', grid, direction);
                if (!as.isEmpty()) {
                    for (LetterCoordinate a : as) {
                        List<LetterCoordinate> ses = findNextLetter(a.X(), a.Y(), 'S', grid, direction);
                        sum += ses.size();
                    }
                }
            }
        }
        return sum;
    }

    private List<LetterCoordinate> findNextLetter(int startX, int startY, char nextLetter, char[][] grid, Direction direction) {
        List<LetterCoordinate> coordinates = new ArrayList<>(4);
        if (startY + direction.mvY() >= 0 &&
                startY + direction.mvY() < grid.length &&
                startX + direction.mvX() >= 0 &&
                startX + direction.mvX() < grid[startY + direction.mvY()].length &&
                grid[startY + direction.mvY()][startX + direction.mvX()] == nextLetter) {
            coordinates.add(new LetterCoordinate(startX + direction.mvX(), startY + direction.mvY(), nextLetter, direction));
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

        Map<Integer, List<LetterCoordinate>> masMap = new HashMap<>();
        int idx = 0;
        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[0].length; x++) {
                char M = grid[y][x];
                if (M == 'M') {
                    List<LetterCoordinate> masForDirection = getMasForDirection(x, y, grid, Direction.UP_RIGHT);
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

        Map<Integer, List<LetterCoordinate>> tmp = new HashMap<>(masMap);
        long sum = 0;
        for (Map.Entry<Integer, List<LetterCoordinate>> entry : masMap.entrySet()) {
            Optional<LetterCoordinate> A = entry.getValue().stream().filter(c -> c.sign() == 'A').findFirst();
            if (A.isPresent() && tmp.containsKey(entry.getKey())) {
                tmp.remove(entry.getKey());
                Optional<Integer> key = Optional.empty();
                for (Map.Entry<Integer, List<LetterCoordinate>> entry2 : tmp.entrySet()) {
                    Optional<LetterCoordinate> sharedA = entry2.getValue()
                            .stream()
                            .filter(c -> c.sign() == A.get().sign())
                            .filter(c -> c.direction() != A.get().direction())
                            .filter(c -> c.X() == A.get().X())
                            .filter(c -> c.Y() == A.get().Y())
                            .findAny();
                    if (sharedA.isPresent()) {
                        key = Optional.of(entry2.getKey());
                        sum++;
                        break;
                    }
                }
                key.ifPresent(tmp::remove);
            }
        }
        System.out.printf("Part 2 - Sum %d%n", sum);
    }

    private List<LetterCoordinate> getMasForDirection(int x, int y, char[][] grid, Direction direction) {
        List<LetterCoordinate> coordinates = new ArrayList<>(3);
        LetterCoordinate M = new LetterCoordinate(x, y, 'M', direction);
        Optional<LetterCoordinate> A = findMasNextLetter(M.X(), M.Y(), 'A', grid, direction);
        if (A.isPresent()) {
            Optional<LetterCoordinate> S = findMasNextLetter(A.get().X(), A.get().Y(), 'S', grid, direction);
            if (S.isPresent()) {
                coordinates.add(M);
                coordinates.add(A.get());
                coordinates.add(S.get());
            }
        }
        return coordinates;
    }

    private Optional<LetterCoordinate> findMasNextLetter(int startX, int startY, char nextLetter, char[][] grid, Direction direction) {
        if (startY + direction.mvY() >= 0 &&
                startY + direction.mvY() < grid.length &&
                startX + direction.mvX() >= 0 &&
                startX + direction.mvX() < grid[startY + direction.mvY()].length &&
                grid[startY + direction.mvY()][startX + direction.mvX()] == nextLetter) {
            return Optional.of(new LetterCoordinate(startX + direction.mvX(), startY + direction.mvY(), nextLetter, direction));
        }
        return Optional.empty();
    }
}