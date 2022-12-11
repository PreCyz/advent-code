package year2022;

import base.DecBase;

import java.util.*;
import java.util.stream.Stream;

class Dec9 extends DecBase {
    private static final String TAIL = "T";
    private static final String HEAD = "H";

    protected Dec9(String fileName) {
        super(fileName);
    }

    @Override
    public Dec9 readDefaultInput() {
        System.out.println("Reading default input.");
        inputStrings = new LinkedList<>(Stream.of(
                "R 4",
                "U 4",
                "L 3",
                "D 1",
                "R 4",
                "D 1",
                "L 5",
                "R 2"
        ).toList());
        return this;
    }

    @Override
    protected void calculatePart1() {
        calculate(2, "Part 1");
    }

    void calculate(int knotSize, String part) {
        Map<String, Knot> visitedMap = new LinkedHashMap<>();

        int yLength = inputStrings.stream()
                .filter(l -> l.startsWith("U"))
                .mapToInt(u -> Integer.parseInt(u.split(" ")[1]))
                .sum();
        int xLength = inputStrings.stream()
                .filter(l -> l.startsWith("R"))
                .mapToInt(r -> Integer.parseInt(r.split(" ")[1]))
                .sum();

        int gridLength = 2 * Math.max(xLength, yLength);
        String[][] grid = new String[gridLength][gridLength];
        initGrid(grid, gridLength);

        ArrayList<Knot> knots = new ArrayList<>(knotSize);
        for (int idx = 0; idx < knotSize; idx++) {
            if (idx == 0) {
                knots.add(idx, new Knot(gridLength / 2, gridLength / 2, HEAD));
            } else if (idx == knotSize - 1) {
                knots.add(idx, new Knot(gridLength / 2, gridLength / 2, TAIL));
            } else {
                knots.add(idx, new Knot(gridLength / 2, gridLength / 2, String.valueOf(idx)));
            }
        }

        grid[gridLength / 2][gridLength / 2] = TAIL;
        Knot lastTail = knots.get(knots.size() - 1);
        visitedMap.put(lastTail.key(), lastTail);

        for (String line : inputStrings) {
            String[] split = line.split(" ");
            String direction = split[0];
            int steps = Integer.parseInt(split[1]);

            Knot head = knots.get(0);
            for (int step = 1; step <= steps; step++) {

                switch (direction) {
                    case "U" -> head.y += 1;
                    case "D" -> head.y -= 1;
                    case "R" -> head.x += 1;
                    case "L" -> head.x -= 1;
                    default -> throw new IllegalArgumentException(direction);
                }
                updateGrid(grid, head.x, head.y, "H");

                Knot tmpHead = head;
                for (int knotNo = 1; knotNo < knots.size(); knotNo++) {
                    Knot tail = knots.get(knotNo);
                    //diagonal move y-wise
                    if (Math.abs(tmpHead.y - tail.y) > 1 && Math.abs(tmpHead.x - tail.x) > 0) {
                        tail.y += tmpHead.y - tail.y + (tmpHead.y > tail.y ? -1 : 1);
                        tail.x = tmpHead.x;
                        //diagonal move x-wise
                    } else if (Math.abs(tmpHead.x - tail.x) > 1 && Math.abs(tmpHead.y - tail.y) > 0) {
                        tail.x += tmpHead.x - tail.x + (tmpHead.x > tail.x ? -1 : 1);
                        tail.y = tmpHead.y;
                        // move only y-wise
                    } else if (Math.abs(tmpHead.y - tail.y) > 1) {
                        tail.y += tmpHead.y - tail.y + (tmpHead.y > tail.y ? -1 : 1);
                        tail.x = tmpHead.x;
                        //move only x-wise
                    } else if (Math.abs(tmpHead.x - tail.x) > 1) {
                        tail.x += tmpHead.x - tail.x + (tmpHead.x > tail.x ? -1 : 1);
                        tail.y = tmpHead.y;
                    }
                    updateGrid(grid, tail.x, tail.y, String.valueOf(tail.id));
                    tmpHead = tail;
                }
                visitedMap.put(tmpHead.key(), tmpHead);
                //printGrid2(grid);
                printGrid(grid);
            }
            //System.out.println(line + " step " + step + " knot " + knotNo);
            //System.out.println(line);
            //print3(gridLength, knots);
            //printGrid(grid, gridLength);
        }
        //printGrid2(grid);
        //System.out.println();
        //printGrid(grid);
        System.out.printf("%s: grid %d%n", part, calculateTales(grid, gridLength));
        System.out.printf("%s: key size %d%n", part, visitedMap.size());
    }

    void initGrid(String[][] grid, int length) {
        for (int y = 0; y < length; y++) {
            for (int x = 0; x < length; x++) {
                grid[x][y] = ".";
            }
        }
    }

    void printGrid(String[][] grid) {
        for (int y = grid[0].length - 1; y >= 0; y--) {
            for (int x = 0; x < grid.length; x++) {
                System.out.printf(TAIL.equals(grid[x][y]) ? "x" : ".");
            }
            System.out.println();
        }
    }

    long calculateTales(String[][] grid, int length) {
        long positions = 0;
        for (int y = 0; y < length; y++) {
            for (int x = 0; x < length; x++) {
                positions += Dec9.TAIL.equals(grid[x][y]) ? 1 : 0;
            }
        }
        return positions;
    }

    private static class Knot {
        Knot(int x, int y, String id) {
            this.x = x;
            this.y = y;
            this.id = id;
        }

        int x;
        int y;
        String id;

        public String key() {
            return x + "," + y;
        }
    }

    @Override
    protected void calculatePart2() {

//        inputStrings = new LinkedList<>(Stream.of(
//                "R 4",
//                "U 4",
//                "L 3",
//                "D 1",
//                "R 4",
//                "D 1",
//                "L 5",
//                "R 2"
//        ).toList());
//
//        inputStrings = new LinkedList<>(Stream.of(
//                "R 5",
//                "U 8",
//                "L 8",
//                "D 3",
//                "R 17",
//                "D 10",
//                "L 25",
//                "U 20"
//        ).toList());

        calculate(10, "Part 2 [2493]");
    }

    private void print3(int gridLength, ArrayList<Knot> knots) {
        String[][] tmpGrid = new String[gridLength][gridLength];
        initGrid(tmpGrid, gridLength);
        tmpGrid[gridLength / 2][gridLength / 2] = "s";
        for (Knot knot : knots) {
            tmpGrid[knot.x][knot.y] = knot.id;
        }
        printGrid2(tmpGrid);
    }

    void updateGrid(String[][] grid, int x, int y, String value) {
        if (!TAIL.equals(grid[x][y])) {
            grid[x][y] = value;
        }
    }

    void printGrid2(String[][] grid) {
        for (int y = grid[0].length - 1; y >= 0; y--) {
            for (int x = 0; x < grid.length; x++) {
                System.out.printf(grid[x][y]);
            }
            System.out.println();
        }
    }
}
