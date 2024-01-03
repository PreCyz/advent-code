package utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.stream.Stream;

public class Sequence {
    public int startIdx;
    public int length;
    public final ArrayList<Long> elements;

    public Sequence(){
        elements = new ArrayList<>();
    }
    private Sequence(int startIdx, int length, ArrayList<Long> elements) {
        this.startIdx = startIdx;
        this.length = length;
        this.elements = elements;
    }

    public static Sequence findSequence(ArrayList<Long> sums) {
        LinkedHashSet<Long> sequenceSet = new LinkedHashSet<>();
        Map<Long, ArrayList<Integer>> sequenceMap = new HashMap<>();
        int startIdx = -1;
        for (int i = 0; i < sums.size(); i++) {
            Long sum = sums.get(i);
            if (sequenceSet.contains(sum)) {
                if (sequenceMap.containsKey(sum)) {
                    sequenceMap.get(sum).add(i);
                } else {
                    sequenceMap.put(sum, new ArrayList<>(Stream.of(i).toList()));
                    if (startIdx == -1) {
                        startIdx = i;
                        //System.out.println("Found cycle start: " + startIdx);
                    }
                }
            } else {
                sequenceSet.add(sum);
                startIdx = -1;
                sequenceMap.clear();
            }
        }

        int sequenceLength = -1;
        for (Map.Entry<Long, ArrayList<Integer>> entry : sequenceMap.entrySet()) {
            ArrayList<Integer> indexes = entry.getValue();
            for (int i = 1, size = indexes.size(); i < size; i++) {
                int length = indexes.get(i) - indexes.get(i - 1);
                if (sequenceLength == -1) {
                    sequenceLength = length;
                } else if (sequenceLength != length) {
                    sequenceLength = -1;
                    break;
                }
            }
            if (sequenceLength != -1) {
                System.out.println("Found sequence length: " + sequenceLength);
                System.out.println("Sequence start at index : " + (startIdx - sequenceLength));
                System.out.println("The first element of cycle is: " + sums.get(startIdx - sequenceLength));
                break;
            }
        }

        return new Sequence(startIdx - sequenceLength, sequenceLength, new ArrayList<>(sums.subList(startIdx - sequenceLength, startIdx)));
    }
}
