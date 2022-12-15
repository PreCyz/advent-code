package year2022;

import base.DecBase;

import java.util.LinkedList;
import java.util.stream.Stream;

class Dec14 extends DecBase {

    private static final String ROCK = "x";
    private static final String SOURCE_OF_THE_SAND = "+";
    private static final String AIR = ".";
    private static final String SAND = "o";

    protected Dec14(String fileName) {
        super(fileName);
    }

    @Override
    public Dec14 readDefaultInput() {
        System.out.println("Reading default input.");
        inputStrings = new LinkedList<>(Stream.of(
                "498,4 -> 498,6 -> 496,6",
                "503,4 -> 502,4 -> 502,9 -> 494,9"
        ).toList());
        return this;
    }

    @Override
    protected void calculatePart1() {
        String[][] grid = initGrid(inputStrings);
        final int unitOfSand = pourSand(grid);
        System.out.printf("Part 1: %d%n", unitOfSand);
    }

    private String[][] initGrid(LinkedList<String> inputStrings) {
        //String[][] grid = new String[510][510];
        String[][] grid = new String[1000][1000];
        grid[500][0] = SOURCE_OF_THE_SAND;
        for (String line : inputStrings) {
            final String[] coordinates = line.split(" -> ");
            for (int i = 1; i < coordinates.length; i++) {
                String[] start = coordinates[i - 1].split(",");
                String[] end = coordinates[i].split(",");
                for (int x = Math.min(Integer.parseInt(start[0]), Integer.parseInt(end[0]));
                     x <= Math.max(Integer.parseInt(start[0]), Integer.parseInt(end[0]));
                     x++
                ) {
                    for (int y = Math.min(Integer.parseInt(start[1]), Integer.parseInt(end[1]));
                         y <= Math.max(Integer.parseInt(start[1]), Integer.parseInt(end[1]));
                         y++
                    ) {
                        grid[x][y] = ROCK;
                    }
                }
            }
        }
        for (int y = 0; y < grid[0].length; y++) {
            for (int x = 0; x < grid.length; x++) {
                if (grid[x][y] == null || "".equals(grid[x][y])) {
                    grid[x][y] = AIR;
                }
            }
        }
        return grid;
    }

    private int pourSand(String[][] grid) {
        boolean isSpaceAvailable = true;
        int unitOfSand = 0;
        try {
            while (isSpaceAvailable) {
                //printGrid(grid);
                boolean keepMoving = true;
                int x = 500;
                int y = 0;

                while (keepMoving) {
                    if (isDownAvailable(grid, x, y)) {
                        y += 1;
                        //printSandDrop(grid, x, y);
                        throwAbyss(grid, x, y);
                        continue;
                    }
                    if (isLeftDownAvailable(grid, x, y)) {
                        x -= 1;
                        y += 1;
                        //printSandDrop(grid, x, y);
                        throwAbyss(grid, x, y);
                        continue;
                    }
                    if (isRightDownAvailable(grid, x, y)) {
                        x += 1;
                        y += 1;
                        //printSandDrop(grid, x, y);
                        throwAbyss(grid, x, y);
                        continue;
                    }
                    if (AIR.equals(grid[x][y])) {
                        grid[x][y] = SAND;
                        keepMoving = false;
                        //printSandDrop(grid, x, y);
                    } else {
                        isSpaceAvailable = false;
                    }
                }
                unitOfSand++;
            }
        } catch (Abyss ex) {
            System.out.println(ex.getMessage());
        }
        return unitOfSand;
    }

    private void throwAbyss(String[][] grid, int x, int y) throws Abyss {
        if (x == grid.length - 1) {
            throw new Abyss("positive X abyss");
        } else if (x < 0) {
            throw new Abyss("negative X abyss");
        } else if (y == grid[0].length - 1) {
            throw new Abyss("Y abyss");
        }
    }

    boolean isDownAvailable(String[][] grid, int currentX, int currentY) {
        return AIR.equals(grid[currentX][currentY + 1]);
    }

    boolean isLeftDownAvailable(String[][] grid, int currentX, int currentY) {
        if (AIR.equals(grid[currentX - 1][currentY + 1])) {
            return true;
        } else if (ROCK.equals(grid[currentX - 1][currentY + 1]) || SAND.equals(grid[currentX - 1][currentY + 1])) {
            return false;
        }
        return isLeftDownAvailable(grid, currentX - 2, currentY + 2);
    }

    boolean isRightDownAvailable(String[][] grid, int currentX, int currentY) {
        if (AIR.equals(grid[currentX + 1][currentY + 1])) {
            return true;
        } else if (ROCK.equals(grid[currentX + 1][currentY + 1]) || SAND.equals(grid[currentX + 1][currentY + 1])) {
            return false;
        }
        return isRightDownAvailable(grid, currentX + 2, currentY + 2);
    }

    static class Abyss extends Exception {
        public Abyss(String message) {
            super(message);
        }
    }

    void printSandDrop(String[][] grid, int xDrop, int yDrop) {
        for (int y = 0; y <= 9; y++) {
            for (int x = 494; x < 504; x++) {
                if (y == yDrop && x == xDrop) {
                    System.out.printf(SAND);
                } else {
                    System.out.printf(grid[x][y]);
                }
            }
            System.out.println();
        }
        System.out.println();
    }

    void printGrid(String[][] grid) {
        for (int y = 0; y < 12; y++) {
            for (int x = 488; x < 512; x++) {
                System.out.printf(grid[x][y]);
            }
            System.out.println();
        }
        System.out.println();
    }

    @Override
    protected void calculatePart2() {
        String[][] grid = initGrid(inputStrings);
        int floorLevel = getAndInitFloorLevel(grid);
        final int unitOfSand = pourSand2(grid, floorLevel);
        System.out.printf("Part 2: %d%n", unitOfSand);

    }

    private int getAndInitFloorLevel(String[][] grid) {
        int highestPoint = 0;
        for (int y = 0; y < grid[0].length; y++) {
            for (int x = 0; x < grid.length; x++) {
                if (ROCK.equals(grid[x][y])) {
                    highestPoint = y;
                    break;
                }
            }
        }
        int floorLevel = highestPoint + 2;
        for (int x = 0; x < grid.length; x++) {
            grid[x][floorLevel] = ROCK;
        }
        return floorLevel;
    }

    private int pourSand2(String[][] grid, int floorLevel) {
        boolean isSpaceAvailable = true;
        int unitOfSand = 0;
        try {
            while (isSpaceAvailable) {
                boolean keepMoving = true;
                int x = 500;
                int y = 0;

                while (keepMoving) {
                    if (isDownAvailable(grid, x, y)) {
                        y += 1;
                        throwFloorLevel(grid, x, y, floorLevel);
                        continue;
                    }
                    if (isLeftDownAvailable(grid, x, y)) {
                        x -= 1;
                        y += 1;
                        throwFloorLevel(grid, x, y, floorLevel);
                        continue;
                    }
                    if (isRightDownAvailable(grid, x, y)) {
                        x += 1;
                        y += 1;
                        throwFloorLevel(grid, x, y, floorLevel);
                        continue;
                    }
                    if (AIR.equals(grid[x][y])) {
                        grid[x][y] = SAND;
                        keepMoving = false;
                    } else {
                        isSpaceAvailable = false;
                    }
                    if (y == 0) {
                        keepMoving = false;
                        isSpaceAvailable = false;
                    }
                }
                unitOfSand++;
            }
        } catch (Abyss ex) {
            System.out.println(ex.getMessage());
        }
        return unitOfSand;
    }

    private void throwFloorLevel(String[][] grid, int x, int y, int floorLevel) throws Abyss {
        if (x == grid.length - 1) {
            throw new Abyss("positive X abyss");
        } else if (x < 0) {
            throw new Abyss("negative X abyss");
        } else if (y == floorLevel) {
            throw new Abyss("Y abyss");
        }
    }
}
