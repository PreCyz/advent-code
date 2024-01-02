package year2023;

import base.DecBase;

import java.util.*;
import java.util.stream.Stream;

class Dec15 extends DecBase {

    public Dec15(int year) {
        super(year, 15);
    }

    @Override
    public Dec15 readDefaultInput() {
        System.out.println("Reading default input.");
        inputStrings = new LinkedList<>(Stream.of("rn=1,cm-,qp=3,cm=2,qp-,pc=4,ot=9,ab=5,pc-,pc=6,ot=7").toList());
        return this;
    }

    private int hash(String sequence) {
        int currentValue = 0;
        for (int c : sequence.toCharArray()) {
            currentValue += c;
            currentValue *= 17;
            currentValue %= 256;
        }
        return currentValue;
    }

    @Override
    protected void calculatePart1() {
        long sum = 0;
        for (String line : inputStrings) {
            String[] split = line.split(",");
            for (String sequence : split) {
                sum += hash(sequence);
            }
        }
        System.out.printf("Part 1 - Total score %d%n", sum);
    }

    @Override
    protected void calculatePart2() {
        ArrayList<LinkedHashMap<String, Integer>> boxes = new ArrayList<>(
                Stream.generate(() -> new LinkedHashMap<String, Integer>()).limit(256).toList()
        );

        for (String line : inputStrings) {
            String[] split = line.split(",");
            for (String sequence : split) {
                if (sequence.contains("=")) {
                    String[] array = sequence.split("=");
                    int boxNo = hash(array[0]);
                    LinkedHashMap<String, Integer> hashMap = boxes.get(boxNo);
                    if (hashMap.containsKey(array[0])) {
                        hashMap.replace(array[0], Integer.parseInt(array[1]));
                    } else {
                        hashMap.put(array[0], Integer.parseInt(array[1]));
                    }
                } else if (sequence.contains("-")) {
                    String[] array = sequence.split("-");
                    int boxNo = hash(array[0]);
                    LinkedHashMap<String, Integer> hashMap = boxes.get(boxNo);
                    hashMap.remove(array[0]);
                }
            }
        }

        long sum = 0;
        for (int i = 0; i < boxes.size(); ++i) {
            LinkedHashMap<String, Integer> hashMap = boxes.get(i);
            if (!hashMap.isEmpty()) {
                int slot = 1;
                for (Integer focalLength : hashMap.values()) {
                    sum += (i + 1L) * slot++ * focalLength;
                }
            }
        }

        System.out.printf("Part 2 - Total score %d%n", sum);
    }
}
