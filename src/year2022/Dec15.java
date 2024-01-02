package year2022;

import base.DecBase;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Stream;

class Dec15 extends DecBase {

    private static final String ROCK = "x";
    private static final String SOURCE_OF_THE_SAND = "+";
    private static final String AIR = ".";
    private static final String SAND = "o";

    protected Dec15(int year) {
        super(year, 15);
    }

    @Override
    public Dec15 readDefaultInput() {
        System.out.println("Reading default input.");
        inputStrings = new LinkedList<>(Stream.of(
                "Sensor at x=2, y=18: closest beacon is at x=-2, y=15",
                "Sensor at x=9, y=16: closest beacon is at x=10, y=16",
                "Sensor at x=13, y=2: closest beacon is at x=15, y=3",
                "Sensor at x=12, y=14: closest beacon is at x=10, y=16",
                "Sensor at x=10, y=20: closest beacon is at x=10, y=16",
                "Sensor at x=14, y=17: closest beacon is at x=10, y=16",
                "Sensor at x=8, y=7: closest beacon is at x=2, y=10",
                "Sensor at x=2, y=0: closest beacon is at x=2, y=10",
                "Sensor at x=0, y=11: closest beacon is at x=2, y=10",
                "Sensor at x=20, y=14: closest beacon is at x=25, y=17",
                "Sensor at x=17, y=20: closest beacon is at x=21, y=22",
                "Sensor at x=16, y=7: closest beacon is at x=15, y=3",
                "Sensor at x=14, y=3: closest beacon is at x=15, y=3",
                "Sensor at x=20, y=1: closest beacon is at x=15, y=3"
        ).toList());
        return this;
    }

    int manhattanDistance(Coordinate first, Coordinate second) {
        return Math.abs(first.x() - second.x()) + Math.abs(first.y() - second.y());
    }

    record Coordinate(int x, int y) {
    }

    record Pair(Coordinate sensor, Coordinate beacon) {
    }

    int getMin(List<Integer> numbers) {
        return numbers.stream().mapToInt(it -> it).min().getAsInt();
    }

    @Override
    protected void calculatePart1() {
        LinkedList<Pair> pairs = getPairs();
        /*int shiftX = getMin(pairs.stream()
                .map(pair -> Math.min(pair.beacon().x(), pair.sensor().x()))
                .toList()
        );
        shiftX = (shiftX >= 0 ? 0 : Math.abs(shiftX));
        int shiftY = getMin(pairs.stream()
                .map(pair -> Math.min(pair.beacon().y(), pair.sensor().y()))
                .toList()
        );
        shiftY = (shiftY >= 0 ? 0 : Math.abs(shiftY));

        shiftX = 8;
        shiftY = 10;

        final String[][] grid = initGrid(pairs, shiftX, shiftY);
        printGrid(grid, shiftY);
        markNoBeacon(pairs, grid, shiftX, shiftY);
        printGrid(grid, shiftY);
        System.out.println(countNoBeacons(grid, 10 + shiftY));*/
        //final Set<Coordinate> beaconCandidates = positionsAvailableForBeacon(pairs, 20);
        final Set<Coordinate> beaconCandidates = positionsAvailableForBeacon(pairs, 2000000);
        int noBeacons = beaconCandidates.size();
        System.out.println("Part 1: " + noBeacons);

    }

    private Set<Coordinate> positionsAvailableForBeacon(LinkedList<Pair> pairs, int rowNumber) {
        final Set<Coordinate> beaconCoordinates = markNoBeacon(pairs, rowNumber);
        pairs.stream()
                .filter(it -> beaconCoordinates.contains(it.beacon()))
                .forEach(it -> beaconCoordinates.remove(it.beacon()));
        pairs.stream()
                .filter(it -> beaconCoordinates.contains(it.sensor()))
                .forEach(it -> beaconCoordinates.remove(it.beacon()));
        return beaconCoordinates;
    }

    int countNoBeacons(String[][] grid, int row) {
        int result = 0;
        for (String[] strings : grid) {
            if ("#".equals(strings[row])) {
                result++;
            }
        }
        return result;
    }


    private Set<Coordinate> markNoBeacon(LinkedList<Pair> pairs, int rowNumber) {
        LinkedHashSet<Coordinate> result = new LinkedHashSet<>();
        for (Pair pair : pairs) {
            int distance = manhattanDistance(pair.sensor(), pair.beacon());
            int startX = pair.sensor().x() - distance;
            int endX = pair.sensor().x() + distance;
            int startY = pair.sensor().y() - distance;
            int endY = pair.sensor().y() + distance;
            //System.out.printf("Sensor: %s, Beacon: %s startY: %d, endY: %d%n", pair.sensor(), pair.beacon(), startY, endY);

            if (startY <= rowNumber && endY >= rowNumber) {
                for (int x = startX; x < endX; x++) {
                    final int dynamicDistance = manhattanDistance(pair.sensor(), new Coordinate(x, rowNumber));
                    if (dynamicDistance <= distance) {
                        result.add(new Coordinate(x, rowNumber));
                    }
                }
            }
        }
        return result;
    }

    private void markNoBeacon(LinkedList<Pair> pairs, String[][] grid, int shiftX, int shiftY) {
        for (Pair pair : pairs) {
            int distance = manhattanDistance(pair.sensor(), pair.beacon());
            int startX = pair.sensor().x() - distance;
            //shiftX = startX >= 0 ? shiftX : shiftX + Math.abs(startX);
            int startY = pair.sensor().y() - distance;
            //shiftY = startY >= 0 ? shiftY : shiftY + Math.abs(startY);

            for (int y = startY; y < pair.sensor().y() + distance; y++) {
                for (int x = startX; x < pair.sensor().x() + distance; x++) {
                    final int dynamicDistance = manhattanDistance(pair.sensor(), new Coordinate(x, y));
                    if (dynamicDistance <= distance) {
                        boolean condition = grid[x + shiftX][y + shiftY] == null;
                        condition |= ".".equals(grid[x + shiftX][y + shiftY]);
                        condition |= "".equals(grid[x + shiftX][y + shiftY]);
                        if (condition) {
                            grid[x + shiftX][y + shiftY] = "#";
                        }
                    }
                }
            }
        }
    }

    private LinkedList<Pair> getPairs() {
        LinkedList<Pair> pairs = new LinkedList<>();
        for (String line : inputStrings) {
            final String[] split = line.split(":");
            final String[] sens = split[0].replace("Sensor at ", "").split(", ");
            final Coordinate sensor = new Coordinate(
                    Integer.parseInt(sens[0].split("=")[1]), Integer.parseInt(sens[1].split("=")[1])
            );
            final String[] bec = split[1].replace(" closest beacon is at ", "").split(", ");
            final Coordinate beacon = new Coordinate(
                    Integer.parseInt(bec[0].split("=")[1]), Integer.parseInt(bec[1].split("=")[1])
            );
            pairs.add(new Pair(sensor, beacon));
        }
        return pairs;
    }

    String[][] initGrid(List<Pair> pairs, int shiftX, int shiftY) {

        String[][] grid = new String[38][37];
        for (int y = 0; y < grid[0].length; y++) {
            for (int x = 0; x < grid.length; x++) {
                grid[x][y] = ".";
            }
        }
        for (Pair pair : pairs) {
            grid[pair.sensor().x() + shiftX][pair.sensor().y() + shiftY] = "S";
            grid[pair.beacon().x() + shiftX][pair.beacon().y() + shiftY] = "B";
        }
        return grid;
    }

    void printGrid(String[][] grid, int shiftY) {
        System.out.println("\t\t\t\t1\t1\t1\t2\t2");
        System.out.println("\t\t2\t6\t0\t4\t8\t2\t6");
        for (int y = 0; y < grid[0].length; y++) {
            System.out.printf("%d\t", y - shiftY);
            for (int x = 0; x < grid.length; x++) {
                System.out.printf(grid[x][y]);
            }
            System.out.println();
        }
        System.out.println();
    }

    private static Comparator<Coordinate> coordinateComparator() {
        return (o1, o2) -> {
            if (o1.x() < o2.x()) {
                return -1;
            } else if (o1.x() > o2.x()) {
                return 1;
            }
            return 0;
        };
    }

    private final int beaconMin = 0;
    private final int beaconMax = 4000000;
    //    private final int beaconMax = 20;
    private final int tuningFreqFactor = 4000000;

    @Override
    protected void calculatePart2() {
        dod(getPairs());
        /*inputStrings = new LinkedList<>(Stream.of(
                "Sensor at x=2, y=18: closest beacon is at x=-2, y=15",
                "Sensor at x=9, y=16: closest beacon is at x=10, y=16",
                "Sensor at x=13, y=2: closest beacon is at x=15, y=3",
                "Sensor at x=12, y=14: closest beacon is at x=10, y=16",
                "Sensor at x=10, y=20: closest beacon is at x=10, y=16",
                "Sensor at x=14, y=17: closest beacon is at x=10, y=16",
                "Sensor at x=8, y=7: closest beacon is at x=2, y=10",
                "Sensor at x=2, y=0: closest beacon is at x=2, y=10",
                "Sensor at x=0, y=11: closest beacon is at x=2, y=10",
                "Sensor at x=20, y=14: closest beacon is at x=25, y=17",
                "Sensor at x=17, y=20: closest beacon is at x=21, y=22",
                "Sensor at x=16, y=7: closest beacon is at x=15, y=3",
                "Sensor at x=14, y=3: closest beacon is at x=15, y=3",
                "Sensor at x=20, y=1: closest beacon is at x=15, y=3"
        ).toList());*/
        LocalDateTime start = LocalDateTime.now();
        LinkedList<Pair> pairs = filterPairs(getPairs());
        for (int i = beaconMin; i <= beaconMax; i++) {
            final Set<Coordinate> coordinates = markNoBeacon2(pairs, i);
            Set<Coordinate> possibleDistressCall = new TreeSet<>(coordinateComparator());
            possibleDistressCall.addAll(coordinates);
            if (possibleDistressCall.size() < beaconMax + 1) {
                final Optional<Coordinate> distressSignalCoordinates = getBeacon(possibleDistressCall);
                distressSignalCoordinates.ifPresent(coordinate ->
                        System.out.printf("Potential distress signal at coordinates %s - its tuning frequency is %d%n",
                                coordinate, tuningFrequency(coordinate.x(), coordinate.y())
                        ));

            }
            //if (i % 100 == 0) {
            System.out.printf("Row %d hundred/s took %d Seconds%n",
                    i,
                    Duration.between(start, LocalDateTime.now()).toSeconds()
            );
            start = LocalDateTime.now();
            //}
        }
    }

    private Set<Coordinate> markNoBeacon2(LinkedList<Pair> pairs, int rowNumber) {
        LinkedHashSet<Coordinate> result = new LinkedHashSet<>();
        for (Pair pair : pairs) {
            final LocalDateTime start = LocalDateTime.now();
            int distanceToBeacon = manhattanDistance(pair.sensor(), pair.beacon());
            int startX = pair.sensor().x() - distanceToBeacon;
            int endX = pair.sensor().x() + distanceToBeacon;
            int startY = pair.sensor().y() - distanceToBeacon;
            int endY = pair.sensor().y() + distanceToBeacon;

            for (int x = Math.max(startX, beaconMin); x <= Math.min(endX, beaconMax); x++) {
//                for (int y = Math.max(startY, beaconMin); y <= Math.min(endY, beaconMax); y++) {
                final int dynamicDistance = manhattanDistance(pair.sensor(), new Coordinate(x, rowNumber));
                if (dynamicDistance <= distanceToBeacon) {
                    result.add(new Coordinate(x, rowNumber));
                }
                //}
            }
//            System.out.printf("Pair %s took %d sec%n",
//                    pair,
//                    Duration.between(start, LocalDateTime.now()).toSeconds()
//            );
        }
        return result;
    }

    private LinkedList<Pair> filterPairs(LinkedList<Pair> pairs) {
        LinkedList<Pair> filteredPairs = new LinkedList<>();
        for (Pair pair : pairs) {
            int distance = manhattanDistance(pair.sensor(), pair.beacon());
            int startX = pair.sensor().x() - distance;
            int endX = pair.sensor().x() + distance;
            int startY = pair.sensor().y() - distance;
            int endY = pair.sensor().y() + distance;

            boolean yCondition = startY < beaconMin && endY < beaconMin;
            yCondition |= startY > beaconMax && endY > beaconMax;
            boolean xCondition = startX < beaconMin && endX < beaconMin;
            xCondition |= startX > beaconMax && endX > beaconMax;
            if (!yCondition && !xCondition) {
                filteredPairs.add(pair);
            }
        }
        return filteredPairs;
    }

    int tuningFrequency(int x, int y) {
        return x * tuningFreqFactor + y;
    }

    private Optional<Coordinate> getBeacon(Set<Coordinate> coordinates) {
        int idx = 0;
        for (Coordinate coordinate : coordinates) {
            if (coordinate.x() != idx) {
                return Optional.of(new Coordinate(idx, coordinate.y()));
            }
            idx++;
        }
        return Optional.empty();
    }

    void dod(LinkedList<Pair> pairs) {
        System.out.println("Dod begins");
        TreeSet<Coordinate> result = new TreeSet<>(coordinateComparator());
        for (int x = 0; x <= beaconMax; x++) {
            final LocalDateTime start = LocalDateTime.now();
            for (int y = 0; y < beaconMax; y++) {
                for (Pair pair : pairs) {
                    int sensorToBeaconDistance = manhattanDistance(pair.sensor(), pair.beacon());

                    final Coordinate point = new Coordinate(x, y);
                    final int sensorPointDistance = manhattanDistance(pair.sensor(), point);
                    if (sensorPointDistance > sensorToBeaconDistance) {
                        result.add(point);
                    } else {
                        result.remove(point);
                    }
                }
            }
            System.out.printf("Column %d done in %d seconds, result size is %d%n",
                    x, Duration.between(start, LocalDateTime.now()).toSeconds(), result.size());
        }
    }
}
