package utils;

import java.nio.file.Path;
import java.nio.file.Paths;

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

}
