package year2022;

import base.DecBase;

import java.util.LinkedList;
import java.util.stream.Stream;

class Dec4 extends DecBase {

    public Dec4(String fileName) {
        super(fileName);
    }

    @Override
    public Dec4 readDefaultInput() {
        System.out.println("Reading default input.");
        inputStrings = new LinkedList<>(
                Stream.of("2-4,6-8",
                                "2-3,4-5",
                                "5-7,7-9",
                                "2-8,3-7",
                                "6-6,4-6",
                                "2-6,4-8")
                        .toList()
        );
        return this;
    }

    @Override
    protected void calculatePart1() {
        long totalContains = 0;
        long totalPairOverlap = 0;
        for (String pair: inputStrings) {
            final String[] ranges = pair.split(",");
            String range1 = ranges[0];
            String range2 = ranges[1];
            final String[] range1Numbers = range1.split("-");
            int start1 = Integer.parseInt(range1Numbers[0]);
            int end1 = Integer.parseInt(range1Numbers[1]);

            final String[] range2Numbers = range2.split("-");
            int start2 = Integer.parseInt(range2Numbers[0]);
            int end2 = Integer.parseInt(range2Numbers[1]);

            boolean condition = start1 <= start2 && end1 >= end2;
            condition |= start2 <= start1 && end2 >= end1;
            if (condition) {
                totalContains += 1;
            }

            condition = start1 <= start2 && start2 <= end1;
            condition |= start1 <= end2 && end2 <= end1;
            condition |= start2 <= start1 && start1 <= end2;
            condition |= start2 <= end1 && end1 <= end2;

            if (condition) {
                totalPairOverlap += 1;
            }
        }
        System.out.printf("Part 1 - Total pairs: %d%n", totalContains);
        System.out.printf("Part 2 - Total pairs overlaps: %d%n", totalPairOverlap);
    }
}
