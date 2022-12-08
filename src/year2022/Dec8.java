package year2022;

import base.DecBase;

import java.util.LinkedList;
import java.util.stream.Stream;

class Dec8 extends DecBase {

    protected Dec8(String fileName) {
        super(fileName);
    }

    @Override
    public Dec8 readDefaultInput() {
        System.out.println("Reading default input.");
        inputStrings = new LinkedList<>(Stream.of(
                "30373",
                "25512",
                "65332",
                "33549",
                "35390"
        ).toList());
        return this;
    }

    @Override
    protected void calculatePart1() {
        int[][] treeGrid = new int[inputStrings.getFirst().length()][inputStrings.size()];
        System.out.printf("Grid size %d, %d%n", treeGrid.length, treeGrid[0].length);

        int column = 0;
        for (String line : inputStrings) {
            for (int row = 0; row < line.length(); row++) {
                treeGrid[row][column] = Integer.parseInt(String.valueOf(line.charAt(row)));
            }
            column++;
        }

        long totalVisibleTrees = treeGrid.length * 4L - 4L;
        System.out.printf("Visible trees from the outside: %d%n", totalVisibleTrees);

        long totalFromLeft = 0;
        long totalFromTop = 0;
        long totalFromBottom = 0;
        long totalFromRight = 0;
        for (int y = 1; y < treeGrid.length - 1; y++) {
            for (int x = 1; x < treeGrid.length - 1; x++) {
                boolean isVisibleFromTop = true;
                for (int yy = 0; yy < y; yy++) {
                    isVisibleFromTop &= treeGrid[x][y] > treeGrid[x][yy];
                }
                if (isVisibleFromTop) {
                    totalFromTop++;
                }

                boolean isVisibleFromBottom = true;
                for (int yy = treeGrid.length - 1; yy > y; yy--) {
                    isVisibleFromBottom &= treeGrid[x][y] > treeGrid[x][yy];
                }
                if (isVisibleFromBottom) {
                    totalFromBottom++;
                }

                boolean isVisibleFromLeft = true;
                for (int xx = 0; xx < x; xx++) {
                    isVisibleFromLeft &= treeGrid[x][y] > treeGrid[xx][y];
                }
                if (isVisibleFromLeft) {
                    totalFromLeft++;
                }

                boolean isVisibleFromRight = true;
                for (int xx = treeGrid.length - 1; xx > x; xx--) {
                    isVisibleFromRight &= treeGrid[x][y] > treeGrid[xx][y];
                }
                if (isVisibleFromRight) {
                    totalFromRight++;
                }
                if (isVisibleFromTop || isVisibleFromBottom || isVisibleFromLeft || isVisibleFromRight) {
                    totalVisibleTrees++;
                }
            }

        }
        System.out.printf("Visible trees from the left side: %d%n", totalFromLeft);
        System.out.printf("Visible trees from the right side: %d%n", totalFromRight);
        System.out.printf("Visible trees from the top side: %d%n", totalFromTop);
        System.out.printf("Visible trees from the bottom side: %d%n", totalFromBottom);
        System.out.printf("Part 1: Total visible trees: %d%n", totalVisibleTrees);
    }

    @Override
    protected void calculatePart2() {
        int[][] treeGrid = new int[inputStrings.getFirst().length()][inputStrings.size()];
        System.out.printf("Grid size %d, %d%n", treeGrid.length, treeGrid[0].length);

        int column = 0;
        for (String line : inputStrings) {
            for (int row = 0; row < line.length(); row++) {
                treeGrid[row][column] = Integer.parseInt(String.valueOf(line.charAt(row)));
            }
            column++;
        }

        long scenicScore = 0;
        for (int y = 1; y < treeGrid.length - 1; y++) {
            for (int x = 1; x < treeGrid.length - 1; x++) {
                long scenicScoreToTop = 0;
                for (int yy = y - 1; yy >= 0; yy--) {
                    scenicScoreToTop++;
                    if (treeGrid[x][y] <= treeGrid[x][yy]) {
                        break;
                    }
                }

                long scenicScoreToBottom = 0;
                for (int yy = y + 1; yy < treeGrid.length; yy++) {
                    scenicScoreToBottom++;
                    if (treeGrid[x][y] <= treeGrid[x][yy]) {
                        break;
                    }
                }

                long scenicScoreToLeft = 0;
                for (int xx = x - 1; xx >= 0; xx--) {
                    scenicScoreToLeft++;
                    if (treeGrid[x][y] <= treeGrid[xx][y]) {
                        break;
                    }
                }

                long scenicScoreToRight = 0;
                for (int xx = x + 1; xx < treeGrid.length; xx++) {
                    scenicScoreToRight++;
                    if (treeGrid[x][y] <= treeGrid[xx][y]) {
                        break;
                    }
                }
                scenicScore = Math.max(
                        scenicScore,
                        scenicScoreToRight * scenicScoreToBottom * scenicScoreToLeft * scenicScoreToTop
                );
            }

        }
        System.out.printf("Part 2: scenic score %d%n", scenicScore);
    }
}
