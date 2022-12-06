package year2015;

import base.DecBase;

import java.util.*;
import java.util.stream.Stream;

public class Dec5 extends DecBase {

    protected Dec5(String fileName) {
        super(fileName);
    }

    @Override
    public DecBase readDefaultInput() {
        System.out.println("Reading default input.");
        inputStrings = new LinkedList<>(Stream.of(
                "ugknbfddgicrmopn",
                "aaa",
                "jchzalrnumimnmhp",
                "haegwjzuvuyypxyu",
                "dvszwmarrgswjxmb",
                "xyxy",
                "aabcdefgaa",
                "aaa",
                "qjhvhtzxzqqjkmpb",
                "xxyxx",
                "uurcxstgmygtbstg",
                "ieodomkazucvgmuy"

        ).toList());
        return this;
    }

    boolean containsVowels(String line) {
        final int niceStringValue = 3;
        Map<Character, Integer> allowedVowels = new HashMap<>();
        allowedVowels.put('a', 0);
        allowedVowels.put('e', 0);
        allowedVowels.put('i', 0);
        allowedVowels.put('o', 0);
        allowedVowels.put('u', 0);
        for (char c : line.toCharArray()) {
            allowedVowels.computeIfPresent(c, (k, v) -> v + 1);
        }
        return allowedVowels.values().stream().mapToInt(a -> a).sum() >= niceStringValue;
    }

    boolean containsDoubleLetters(String line) {
        char[] charArray = line.toCharArray();
        for (int i = 1; i < charArray.length; i++) {
            char c = charArray[i];
            if (c == charArray[i - 1]) {
                return true;
            }
        }
        return false;
    }

    boolean containsForbiddenStrings(String line) {
        Set<String> forbiddenStrings = new HashSet<>(List.of("ab", "cd", "pq", "xy"));
        for (int i = 0; i <= line.length() - 2; i++) {
            String value = line.substring(i, i + 2);
            if (forbiddenStrings.contains(value)) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void calculatePart1() {
        int totalNiceStrings = 0;
        for (String line : inputStrings) {
            boolean isNiceString = containsVowels(line) && containsDoubleLetters(line) && !containsForbiddenStrings(line);
            totalNiceStrings += isNiceString ? 1 : 0;
        }
        System.out.printf("Part 1: %d strings are nice%n", totalNiceStrings);
    }

    @Override
    protected void calculatePart2() {
        int totalNiceStrings = 0;
        for (String line : inputStrings) {
            boolean isNiceString = containsPair(line) && containsLetterBetween(line);
            totalNiceStrings += isNiceString ? 1 : 0;
        }
        System.out.printf("Part 2: %d strings are nice%n", totalNiceStrings);
    }

    boolean containsPair(String line) {
        Map<String, Integer> charSet = new HashMap<>();
        final char[] chars = line.toCharArray();
        for (int i = 0; i <= chars.length - 2; i++) {
            String pair1 = line.substring(i, i + 2);
            String pair2 = "";
            if (i > 0) {
                pair2 = line.substring(i - 1, i + 1);
            }
            if (!pair1.equals(pair2)) {
                if (charSet.containsKey(pair1)) {
                    charSet.put(pair1, charSet.get(pair1) + 1);
                } else {
                    charSet.put(pair1, 1);
                }
            }
        }
        return charSet.values().stream().anyMatch(v -> v >= 2);
    }

    boolean containsLetterBetween(String line) {
        final char[] chars = line.toCharArray();
        for (int i = 1; i < chars.length - 1; i++) {
            if (chars[i -1] == chars[i +1]) {
                return true;
            }
        }
        return false;
    }
}









