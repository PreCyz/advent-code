package year2015;

import base.DecBase;

import java.util.*;
import java.util.stream.Stream;

public class Dec3 extends DecBase {

    protected Dec3(String fileName) {
        super(fileName);
    }

    @Override
    public DecBase readDefaultInput() {
        System.out.println("Reading default input.");
        inputStrings = new LinkedList<>(
                Stream.of(
                        ">",
                        "^>v<",
                        "^v^v^v^v^v"
                ).toList()
        );
        return this;
    }

    private int getY(char c) {
        return switch (c) {
            case '^' -> 1;
            case 'v' -> -1;
            default -> 0;
        };
    }

    private int getX(char c) {
        return switch (c) {
            case '>' -> 1;
            case '<' -> -1;
            default -> 0;
        };
    }

    @Override
    protected void calculatePart1() {
        int x = 0;
        int y = 0;
        Map<String, Integer> map = new HashMap<>();
        map.put(getKey(x, y), 2);
        long uniqueHouses = 1;
        for (String input : inputStrings) {
            for (char c : input.toCharArray()) {
                x += getX(c);
                y += getY(c);
                String key = getKey(x, y);
                if (map.containsKey(key)) {
                    map.put(key, map.get(key) + 1);
                } else {
                    map.put(key, 1);
                    uniqueHouses++;
                }
            }
        }
        System.out.printf("Unique houses: %d%n", uniqueHouses);
    }

    private static String getKey(int x, int y) {
        return String.format("%d,%d", x, y);
    }

    @Override
    protected void calculatePart2() {
        int xS = 0;
        int yS = 0;
        int xR = 0;
        int yR = 0;
        Map<String, Integer> map = new HashMap<>();
        map.put(getKey(0, 0), 2);
        long uniqueHouses = 1;
        boolean santa = true;
        for (String input : inputStrings) {
            for (char c : input.toCharArray()) {
                String key;
                if (santa) {
                    xS += getX(c);
                    yS += getY(c);
                    key = getKey(xS, yS);
                    santa = false;
                } else {
                    xR += getX(c);
                    yR += getY(c);
                    key = getKey(xR, yR);
                    santa = true;
                }
                if (map.containsKey(key)) {
                    map.put(key, map.get(key) + 1);
                } else {
                    map.put(key, 1);
                    uniqueHouses++;
                }
            }
        }
        System.out.printf("Unique houses: %d%n", uniqueHouses);
    }
}
