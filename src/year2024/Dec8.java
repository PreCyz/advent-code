package year2024;

import base.DecBase;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class Dec8 extends DecBase {

    public Dec8(int year) {
        super(year, 8);
    }

    record Point(int x, int y, char frequency) { }

    @Override
    public Dec8 readDefaultInput() {
        System.out.println("Reading default input.");
        inputStrings = new LinkedList<>(Stream.of(
                "............",
                "........0...",
                ".....0......",
                ".......0....",
                "....0.......",
                "......A.....",
                "............",
                "............",
                "........A...",
                ".........A..",
                "............",
                "............"
        ).toList());
        return this;
    }

    @Override
    protected void calculatePart1() {
        String regEx = "[a-zA-Z\\d]{1}";
        char[][] grid = new char[inputStrings.size()][inputStrings.getFirst().length()];
        Set<Point> antennas = new HashSet<>();
        Pattern pattern = Pattern.compile(regEx);
        int y = 0;
        for (String line : inputStrings) {
            for (int x = 0; x < line.length(); x++) {
                grid[y][x] = line.charAt(x);
                if (pattern.matcher(String.valueOf(line.charAt(x))).matches()) {
                    antennas.add(new Point(x, y, grid[y][x]));
                }
            }
            y++;
        }

        List<Set<Point>> sameFrequencyPairs = new ArrayList<>();
        for (Character c : antennas.stream().map(Point::frequency).collect(Collectors.toSet())) {
            List<Point> sameFreqAntenna = antennas.stream().filter(p -> p.frequency() == c).toList();
            for (Point antenna : sameFreqAntenna) {
                List<Point> subset = new ArrayList<>(sameFreqAntenna);
                subset.remove(antenna);
                for (Point secondAntenna : subset) {
                    sameFrequencyPairs.add(Set.of(antenna, secondAntenna));
                }
            }
        }

        Set<Point> antiNodePoints = new HashSet<>();
        for (Set<Point> inlineAntenna : sameFrequencyPairs) {
            ArrayList<Point> pair = new ArrayList<>(inlineAntenna);
            findAntiNodePoint(pair.getFirst(), pair.getLast(), grid).ifPresent(antiNodePoints::add);
            findAntiNodePoint(pair.getLast(), pair.getFirst(), grid).ifPresent(antiNodePoints::add);
        }

        System.out.printf("Part 1 - Sum %d%n", antiNodePoints.size());
    }

    Optional<Point> findAntiNodePoint(Point main, Point second, char[][] grid) {
        int newX = main.x;
        if (main.x < second.x) {
            newX = main.x - (second.x - main.x);
        } else if (main.x > second.x) {
            newX = main.x + (main.x - second.x);
        }

        int newY = main.y;
        if (main.y < second.y) {
            newY = main.y - (second.y - main.y);
        } else if (main.y > second.y) {
            newY = main.y + (main.y - second.y);
        }

        if (isWithinBounds(newX, newY, grid)) {
            return Optional.of(new Point(newX, newY, grid[newY][newX]));
        }

        return Optional.empty();
    }

    private boolean isWithinBounds(int x, int y, char[][] grid) {
        return x >= 0 && x < grid[0].length && y >= 0 && y < grid.length;
    }

    @Override
    protected void calculatePart2() {
        String regEx = "[a-zA-Z\\d]{1}";
        char[][] grid = new char[inputStrings.size()][inputStrings.getFirst().length()];
        Set<Point> antennas = new HashSet<>();
        Pattern pattern = Pattern.compile(regEx);
        int y = 0;
        for (String line : inputStrings) {
            for (int x = 0; x < line.length(); x++) {
                grid[y][x] = line.charAt(x);
                if (pattern.matcher(String.valueOf(line.charAt(x))).matches()) {
                    antennas.add(new Point(x, y, grid[y][x]));
                }
            }
            y++;
        }

        List<Set<Point>> sameFrequencyPairs = new ArrayList<>();
        for (Character c : antennas.stream().map(Point::frequency).collect(Collectors.toSet())) {
            List<Point> sameFreqAntenna = antennas.stream().filter(p -> p.frequency() == c).toList();
            for (Point antenna : sameFreqAntenna) {
                List<Point> subset = new ArrayList<>(sameFreqAntenna);
                subset.remove(antenna);
                for (Point secondAntenna : subset) {
                    sameFrequencyPairs.add(Set.of(antenna, secondAntenna));
                }
            }
        }

        Set<Point> antiNodePoints = new HashSet<>(sameFrequencyPairs.stream().flatMap(Set::stream).toList());
        for (Set<Point> inlineAntenna : sameFrequencyPairs) {
            ArrayList<Point> pair = new ArrayList<>(inlineAntenna);
            antiNodePoints.addAll(findAntiNodePoints(pair.getFirst(), pair.getLast(), grid));
            antiNodePoints.addAll(findAntiNodePoints(pair.getLast(), pair.getFirst(), grid));
        }

        System.out.printf("Part 2 - Sum %d%n", antiNodePoints.size());
    }

    Set<Point> findAntiNodePoints(Point main, Point second, char[][] grid) {
        Set<Point> result = new HashSet<>();
        int startX = main.x;
        int startY = main.y;
        int secondX = second.x;
        int secondY = second.y;
        int newX = main.x;
        int newY = main.y;
        do {
            if (startX < secondX) {
                newX = startX - (secondX - startX);
            } else if (startX > secondX) {
                newX = startX + (startX - secondX);
            }

            if (startY < secondY) {
                newY = startY - (secondY - startY);
            } else if (startY > secondY) {
                newY = startY + (startY - secondY);
            }

            if (isWithinBounds(newX, newY, grid)) {
                result.add(new Point(newX, newY, grid[newY][newX]));
                secondX = startX;
                secondY = startY;
                startX = newX;
                startY = newY;
            }
        } while (isWithinBounds(newX, newY, grid));

        return result;
    }

}