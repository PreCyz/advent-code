package year2022;

import base.DecBase;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.stream.Stream;

class Dec6 extends DecBase {

    public Dec6(String fileName) {
        super(fileName);
    }

    @Override
    public Dec6 readDefaultInput() {
        System.out.println("Reading default input.");
        inputStrings = new LinkedList<>(Stream.of(
                "mjqjpqmgbljsphdztnvjfqwrcgsmlb",
                "bvwbjplbgvbhsrlpgdmjqwftvncz",
                "nppdvjthqldpwncqszvftbrmjlhg",
                "nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg",
                "zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw"
        ).toList());
        return this;
    }

    @Override
    protected void calculatePart1() {
        final int distinctCharacterMarker = 4;
        calculate(distinctCharacterMarker, "Part 1: start-of-pocket at %d%n");
    }

    private void calculate(int distinctCharacterMarker, String format) {
        for (String line : inputStrings) {
            final LinkedHashSet<Character> occurrenceSet = new LinkedHashSet<>();
            final char[] chars = line.toCharArray();
            int startOfPocketIndex = 0;
            for (int i = 0; i <= chars.length - distinctCharacterMarker; i++) {
                final String startOfPacketValue = line.substring(i, i + distinctCharacterMarker);
                for (char c : startOfPacketValue.toCharArray()) {
                    occurrenceSet.add(c);
                }
                if (occurrenceSet.size() == distinctCharacterMarker) {
                    startOfPocketIndex = i + distinctCharacterMarker;
                    break;
                } else {
                    occurrenceSet.clear();
                }
            }
            System.out.printf(format, startOfPocketIndex);
        }
    }

    @Override
    protected void calculatePart2() {
        final int distinctCharacterMarker = 14;
        calculate(distinctCharacterMarker, "Part 2: start-of-message at %d%n");
    }
}
