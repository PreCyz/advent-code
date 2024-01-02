package year2023;

import base.DecBase;

import java.util.LinkedList;
import java.util.stream.Stream;

class Dec2 extends DecBase {

    final int blue = 14;
    final int red = 12;
    final int green = 13;
    final String greenTxt = "green";
    final String blueTxt = "blue";
    final String redTxt = "red";
    final String gameTxt = "Game";
    public Dec2(int year) {
        super(year, 2);
    }

    @Override
    public Dec2 readDefaultInput() {
        System.out.println("Reading default input.");
        inputStrings = new LinkedList<>(Stream.of(
                        "Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green",
                        "Game 2: 1 blue, 2 green; 3 green, 4 blue, 1 red; 1 green, 1 blue",
                        "Game 3: 8 green, 6 blue, 20 red; 5 blue, 4 red, 13 green; 5 green, 1 red",
                        "Game 4: 1 green, 3 red, 6 blue; 3 green, 6 red; 3 green, 15 blue, 14 red",
                        "Game 5: 6 red, 1 blue, 3 green; 2 blue, 1 red, 2 green")
                .toList());
        return this;
    }

    @Override
    protected void calculatePart1() {
        long sum = 0;
        for (String line : inputStrings) {
            String gameId = line.split(":")[0];
            String ballsSet = line.split(":")[1];
            String[] game = ballsSet.split(";");
            boolean isAllowed = true;
            for (String round : game) {
                String[] balls = round.split(",");
                for (String ball : balls) {
                    if (ball.contains(blueTxt)) {
                        isAllowed &= isAllowed(ball, blueTxt, blue);
                    } else if (ball.contains(redTxt)) {
                        isAllowed &= isAllowed(ball, redTxt, red);
                    } else if (ball.contains(greenTxt)) {
                        isAllowed &= isAllowed(ball, greenTxt, green);
                    }
                }
            }
            if (isAllowed) {
                String id = gameId.replace(gameTxt, "").replaceAll(" ", "");
                sum += Integer.parseInt(id);
            }

        }
        System.out.printf("Part 1 - Total score %d%n", sum);
    }

    private boolean isAllowed(String ball, String text, int max) {
        String number = ball.replace(text, "").replaceAll(" ", "");
        int no = Integer.parseInt(number);
        return no <= max;
    }

    @Override
    protected void calculatePart2() {
        int sum = 0;
        for (String line : inputStrings) {
            String ballsSet = line.split(":")[1];
            String[] game = ballsSet.split(";");
            int maxGreen = 0, maxRed = 0, maxBlue = 0;
            for (String round : game) {
                String[] balls = round.split(",");
                for (String ball : balls) {
                    if (ball.contains(blueTxt)) {
                        maxBlue = getMaxValue(ball, blueTxt, maxBlue);
                    } else if (ball.contains(redTxt)) {
                        maxRed = getMaxValue(ball, redTxt, maxRed);
                    } else if (ball.contains(greenTxt)) {
                        maxGreen = getMaxValue(ball, greenTxt, maxGreen);
                    }
                }
            }
            sum += maxRed * maxGreen * maxBlue;
        }
        System.out.printf("Part 2 - Total score %d%n", sum);
    }

    private int getMaxValue(String ball, String text, int max) {
        String number = ball.replace(text, "").replaceAll(" ", "");
        return Math.max(Integer.parseInt(number), max);
    }
}
