package year2015;

import base.DecBase;

import java.util.LinkedList;
import java.util.stream.Stream;

public class Dec6 extends DecBase {

    private static final int GRID_SIZE = 1000;

    final int[][] grid = new int[GRID_SIZE][GRID_SIZE];

    protected Dec6(String fileName) {
        super(fileName);
    }

    @Override
    public DecBase readDefaultInput() {
        System.out.println("Reading default input.");
        inputStrings = new LinkedList<>(Stream.of(
//                "turn on 0,0 through 999,999",
//                "toggle 0,0 through 999,0",
//                "turn off 499,499 through 500,500"
                "turn on 0,0 through 0,0",
                "toggle 0,0 through 999,999"
        ).toList());
        return this;
    }

    @Override
    protected void calculatePart1() {
        resetGrid();

        int lightsInTheLoop = 0;
        for (String line : inputStrings) {
            int startX = 0;
            int startY = 0;
            int endX = 0;
            int endY = 0;
            final String[] words = line.split(" ");
            if (line.startsWith("turn off") || line.startsWith("turn on")) {
                startX = Integer.parseInt(words[2].split(",")[0]);
                startY = Integer.parseInt(words[2].split(",")[1]);
                endX = Integer.parseInt(words[4].split(",")[0]);
                endY = Integer.parseInt(words[4].split(",")[1]);
            } else if (line.startsWith("toggle")) {
                startX = Integer.parseInt(words[1].split(",")[0]);
                startY = Integer.parseInt(words[1].split(",")[1]);
                endX = Integer.parseInt(words[3].split(",")[0]);
                endY = Integer.parseInt(words[3].split(",")[1]);
            }

            for (int x = startX; x <= endX; x++) {
                for (int y = startY; y <= endY; y++) {
                    if (line.startsWith("turn on")) {
                        lightsInTheLoop += grid[x][y] == 1 ? 0 : 1;
                        grid[x][y] = 1;
                    } else if (line.startsWith("turn off")) {
                        lightsInTheLoop += grid[x][y] == 0 ? 0 : -1;
                        grid[x][y] = 0;
                    } else if (line.startsWith("toggle")) {
                        lightsInTheLoop += grid[x][y] == 0 ? 1 : -1;
                        grid[x][y] = Math.abs(grid[x][y] - 1);
                    }
                }
            }
        }
        System.out.printf("Part 1: lights lit %d%n", lightsInTheLoop);
    }

    @Override
    protected void calculatePart2() {
        resetGrid();

        for (String line : inputStrings) {
            int startX = 0;
            int startY = 0;
            int endX = 0;
            int endY = 0;
            final String[] words = line.split(" ");
            if (line.startsWith("turn off") || line.startsWith("turn on")) {
                startX = Integer.parseInt(words[2].split(",")[0]);
                startY = Integer.parseInt(words[2].split(",")[1]);
                endX = Integer.parseInt(words[4].split(",")[0]);
                endY = Integer.parseInt(words[4].split(",")[1]);
            } else if (line.startsWith("toggle")) {
                startX = Integer.parseInt(words[1].split(",")[0]);
                startY = Integer.parseInt(words[1].split(",")[1]);
                endX = Integer.parseInt(words[3].split(",")[0]);
                endY = Integer.parseInt(words[3].split(",")[1]);
            }

            for (int x = startX; x <= endX; x++) {
                for (int y = startY; y <= endY; y++) {
                    if (line.startsWith("turn on")) {
                        grid[x][y] += 1;
                    } else if (line.startsWith("turn off")) {
                        grid[x][y] -= grid[x][y] > 0 ? 1 : 0;
                    } else if (line.startsWith("toggle")) {
                        grid[x][y] += 2;
                    }
                }
            }
        }

        long sumOfBrightness = 0;
        for (int x = 0; x < GRID_SIZE; x++) {
            for (int y = 0; y < GRID_SIZE; y++) {
                sumOfBrightness += grid[x][y];
            }
        }
        System.out.printf("Part 2: brightness %d%n", sumOfBrightness);
    }

    private void resetGrid() {
        for (int x = 0; x < GRID_SIZE; x++) {
            for (int y = 0; y < GRID_SIZE; y++) {
                grid[x][y] = 0;
            }
        }
    }
}