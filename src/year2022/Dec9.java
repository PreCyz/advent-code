package year2022;

import base.DecBase;

import java.util.*;
import java.util.stream.Stream;

class Dec9 extends DecBase {
    private static final String TAIL = "T";

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
        int headX = gridLength / 2;
        int headY = gridLength / 2;
        int tailX = gridLength / 2;
        int tailY = gridLength / 2;
        grid[tailX][tailY] = TAIL;

        for (String line : inputStrings) {
            String[] split = line.split(" ");
            String direction = split[0];
            int steps = Integer.parseInt(split[1]);

            for (int i = 1; i <= steps; i++) {
                switch (direction) {
                    case "U" -> {
                        headY += 1;
                        if (Math.abs(headY - tailY) > 1) {
                            if (Math.abs(headX - tailX) > 0) {
                                //System.out.printf("U pull before tailX=%d, and after headX=%d %n", tailX, headX);
                                tailX = headX;
                            }
                            tailY += 1;
                            grid[tailX][tailY] = TAIL;
                        }
                    }
                    case "D" -> {
                        headY -= 1;
                        if (Math.abs(headY - tailY) > 1) {
                            if (Math.abs(headX - tailX) > 0) {
                                //System.out.printf("D pull before tailX=%d, and after headX=%d %n", tailX, headX);
                                tailX = headX;
                            }
                            tailY -= 1;
                            grid[tailX][tailY] = TAIL;
                        }
                    }
                    case "R" -> {
                        headX += 1;
                        if (Math.abs(headX - tailX) > 1) {
                            if (Math.abs(headY - tailY) > 0) {
                                //System.out.printf("R pull before tailY=%d, and after headY=%d %n", tailY, headY);
                                tailY = headY;
                            }
                            tailX += 1;
                            grid[tailX][tailY] = TAIL;
                        }
                    }
                    case "L" -> {
                        headX -= 1;
                        if (Math.abs(headX - tailX) > 1) {
                            if (Math.abs(headY - tailY) > 0) {
                                //System.out.printf("L pull before tailY=%d, and after headY=%d %n", tailY, headY);
                                tailY = headY;
                            }
                            tailX -= 1;
                            grid[tailX][tailY] = TAIL;
                        }
                    }
                    default -> throw new IllegalArgumentException(direction);
                }
                //System.out.printf("H[%d,%d] T[%d,%d]%n", headX, headY, tailX, tailY);
            }
        }

        //printGrid(grid, gridLength);
        System.out.printf("Part 1: %d%n", calculateTales(grid, gridLength, TAIL));
    }

    void initGrid(String[][] grid, int length) {
        for (int y = 0; y < length; y++) {
            for (int x = 0; x < length; x++) {
                grid[x][y] = ".";
            }
        }
    }

    void printGrid(String[][] grid, int length) {
        for (int y = length - 1; y >= 0; y--) {
            for (int x = 0; x < length; x++) {
                System.out.printf("9".equals(grid[x][y]) ? "x" : ".");
                //System.out.printf(!"".equals(grid[x][y]) ? grid[x][y] : ".");
            }
            System.out.println();
        }
    }

    long calculateTales(String[][] grid, int length, String value) {
        long positions = 0;
        for (int y = 0; y < length; y++) {
            for (int x = 0; x < length; x++) {
                positions += value.equals(grid[x][y]) ? 1 : 0;
            }
        }
        return positions;
    }

    private static class Knot {
        Knot(int x, int y, String no) {
            this.x = x;
            this.y = y;
            this.no = no;
        }

        int x;
        int y;
        String no;

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

        Set<String> visited = new LinkedHashSet<>();
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

        final int knotSize = 10;
        ArrayList<Knot> knots = new ArrayList<>(knotSize);
        for (int idx = 0; idx < knotSize; idx++) {
            knots.add(idx, new Knot(gridLength / 2, gridLength / 2, idx == 0 ? "H" : String.valueOf(idx)));
        }

        grid[gridLength / 2][gridLength / 2] = String.valueOf(knotSize - 1);
        Knot lastTail = knots.get(knots.size() - 1);
        visited.add(lastTail.key());
        visitedMap.put(lastTail.key(), lastTail);

        for (String line : inputStrings) {
            //System.out.printf("%n== %s ==%n", line);
            String[] split = line.split(" ");
            String direction = split[0];
            int steps = Integer.parseInt(split[1]);

            Knot head = knots.get(0);
            for (int step = 1; step <= steps; step++) {
                //System.out.printf("%n== %s ==, step: %d%n", line, step);

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
                        tail.x = Math.abs(tmpHead.x - tail.x) > 1 ? tmpHead.x - 1 : tmpHead.x;
                    //diagonal move x-wise
                    } else if (Math.abs(tmpHead.x - tail.x) > 1 && Math.abs(tmpHead.y - tail.y) > 0) {
                        tail.x += tmpHead.x - tail.x + (tmpHead.x > tail.x ? -1 : 1);
                        tail.y = Math.abs(tmpHead.y - tail.y) > 1 ? tmpHead.y - 1 : tmpHead.y;
                    // move only y-wise
                    } else if (Math.abs(tmpHead.y - tail.y) > 1) {
                        tail.y += tmpHead.y - tail.y + (tmpHead.y > tail.y ? -1 : 1);
                    //move only x-wise
                    } else if (Math.abs(tmpHead.x - tail.x) > 1) {
                        tail.x += tmpHead.x - tail.x + (tmpHead.x > tail.x ? -1 : 1);
                    }
                    updateGrid(grid, tail.x, tail.y, String.valueOf(knotNo));
                    tmpHead = tail;
                }
                if ("9".equals(tmpHead.no)) {
                    visited.add(tmpHead.key());
                    visitedMap.put(tmpHead.key(), tmpHead);
                }
                //printGrid2(grid, gridLength);
                //printGrid(grid, gridLength);
            }
            //System.out.println(line + " step " + step + " knot " + knotNo);
            //print3(gridLength, knots);
            //System.out.println(line);
            //printGrid(grid, gridLength);

//            initGrid(grid, gridLength);
//            grid[gridLength / 2][gridLength / 2] = "s";
//            for (Knot knot : knots) {
//                grid[knot.x][knot.y] = knot.no;
//            }
//            printGrid2(grid, gridLength);
        }
        //printGrid2(grid, gridLength);
        //System.out.println();
        //printGrid(grid, gridLength);
        System.out.printf("Part 2: grid %d%n", calculateTales(grid, gridLength, "" + (knotSize - 1)));
        System.out.printf("Part 2: set size %d%n", visited.size());
        System.out.printf("Part 2: key size %d%n", visitedMap.size());
        //printGrid2(grid, gridLength);
    }

    private void print3(int gridLength, ArrayList<Knot> knots) {
        String[][] tmpGrid = new String[gridLength][gridLength];
        initGrid(tmpGrid, gridLength);
        tmpGrid[gridLength / 2][gridLength / 2] = "s";
        for (Knot knot : knots) {
            tmpGrid[knot.x][knot.y] = knot.no;
        }
        printGrid2(tmpGrid, gridLength);
    }

    void updateGrid(String[][] grid, int x, int y, String value) {
        if (!"9".equals(grid[x][y])) {
            grid[x][y] = value;
        }
    }

    void printGrid2(String[][] grid, int length) {
        for (int y = length - 1; y >= 0; y--) {
            for (int x = 0; x < length; x++) {
                System.out.printf(grid[x][y]);
            }
            System.out.println();
        }
    }
}
