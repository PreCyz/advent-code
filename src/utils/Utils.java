package utils;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public final class Utils {

    private Utils() {}

    public static String getFilePath(Path dir, String fileName) {
        return Paths.get(dir.toString(), fileName).toAbsolutePath().normalize().toString();
    }

    public static String[][] adjustGrid(String[][] grid, int x, int y) {
        int newX = grid.length;
        int newY = grid[0].length;
        if (x < 0) {
            newX += Math.abs(x);
        } else if (x >= grid.length) {
            newX += x - grid.length;
        }
        if (y < 0) {
            newY += Math.abs(y);
        } else if (y >= grid[0].length) {
            newY += y - grid[0].length;
        }

        String[][] adjustedGrid = new String[newX][newY];
        for (int yy = 0; yy < newY; yy++) {
            for (int xx = 0; xx < newX; xx++) {
                adjustedGrid[xx][yy] = ".";
            }
        }

        for (int yy = 0; yy < grid[0].length; yy++) {
            for (int xx = 0; xx < grid.length; xx++) {
                newX = xx;
                newY = yy;
                if (x < 0) {
                    newX = Math.abs(xx - x);
                }
                if (y < 0) {
                    newY = Math.abs(yy - y);
                }
                adjustedGrid[newX][newY] = grid[xx][yy];
            }
        }
        return adjustedGrid;
    }

    public static void main(String[] args) {
        String[][] grid = new String [][] {
                {"3", "1"},
                {"4", "2"}
        };
        printGrid2(grid);

        System.out.println("After adjustment");
        String[][] strings = adjustGrid(grid, -1, -1);
        printGrid2(strings);
        System.out.println("After adjustment");
        strings = adjustGrid(strings, 3, 3);
        printGrid2(strings);
        System.out.println("After adjustment");
        strings = adjustGrid(strings, 4, 4);
        printGrid2(strings);

        System.out.println("After adjustment");
        strings = adjustGrid(strings, -2, 5);
        printGrid2(strings);

    }

    static void printGrid2(String[][] grid) {
        for (int y = grid[0].length - 1; y >= 0; y--) {
            for (int x = 0; x < grid.length; x++) {
                System.out.printf(grid[x][y]);
            }
            System.out.println();
        }
    }

    public static void printGrid(char[][] grid) {
        for (int y = grid[0].length - 1; y >= 0; y--) {
            for (int x = 0; x < grid.length; x++) {
                System.out.print(grid[x][y]);
            }
            System.out.println();
        }
    }

    public static void printGridYFirst(char[][] grid) {
        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[y].length; x++) {
                System.out.print(grid[y][x]);
            }
            System.out.println();
        }
    }

    public static ArrayList<String> transpose(List<String> strings) {
        int colIdx = 0;
        ArrayList<String> newStrings = new ArrayList<>(Stream.generate(() -> "").limit(strings.get(0).length()).toList());

        while (colIdx < strings.get(0).length()) {
            for (String line : strings) {
                newStrings.set(colIdx, newStrings.get(colIdx) + line.charAt(colIdx));
            }
            colIdx++;
        }
        return newStrings;
    }

    public static void writeToFile(char[][] grid) {
        String fileName = "output.txt";
        try {
            Files.deleteIfExists(Paths.get(".", fileName));
        } catch (IOException e) {
            System.err.println("UPS! " + e.getMessage());
        }

        try (FileWriter fw = new FileWriter(fileName, true);
             BufferedWriter bw = new BufferedWriter(fw)
        ) {
            for (char[] chars : grid) {
                for (char aChar : chars) {
                    bw.write(aChar);
                }
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("UPS! " + e.getMessage());
        }
    }

}
