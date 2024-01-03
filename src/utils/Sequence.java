package utils;

import java.util.*;
import java.util.stream.Stream;

public class Sequence {
    public int startIdx;
    public int length;
    public final ArrayList<Long> elements;
    public final Map<Long, Integer> occurrenceMap;

    private Sequence(int startIdx, int length, ArrayList<Long> elements, Map<Long, Integer> occurrenceMap) {
        this.startIdx = startIdx;
        this.length = length;
        this.elements = elements;
        this.occurrenceMap = occurrenceMap;
    }

    public static Sequence findSequence(ArrayList<Long> sums) {
        LinkedHashSet<Long> sequenceSet = new LinkedHashSet<>();
        LinkedHashMap<Long, ArrayList<Integer>> sequenceMap = new LinkedHashMap<>();
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
        Map<Long, Integer> occurrenceMap = new LinkedHashMap<>();
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
                System.out.printf("%nFound sequence length: %d%n", sequenceLength);
                System.out.println("Sequence start at index : " + (startIdx - sequenceLength));
                System.out.printf("The first element of cycle is: %d%n%n", sums.get(startIdx - sequenceLength));
                break;
            }
        }

        for (Map.Entry<Long, ArrayList<Integer>> entry : sequenceMap.entrySet()) {
            occurrenceMap.putAll(checkMultipleOccurrences(sequenceLength, entry));
        }

        return new Sequence(
                startIdx - sequenceLength,
                sequenceLength,
                new ArrayList<>(sums.subList(startIdx - sequenceLength, startIdx)),
                occurrenceMap
        );
    }

    private static Map<Long, Integer> checkMultipleOccurrences(int sequenceLength, Map.Entry<Long, ArrayList<Integer>> entry) {
        Map<Long, Integer> occurrenceMap = new HashMap<>();
        ArrayList<Integer> indexes = entry.getValue();
        for (int i = 0; i < 1; i++) {
            Set<Integer> occurrences = new HashSet<>();
            for (int j = 1, size = indexes.size(); j < size; j++) {
                occurrences.add(indexes.get(j) - indexes.get(i));
            }
            long occurrence = occurrences.stream().map(it -> it % sequenceLength).distinct().count();
            occurrenceMap.putIfAbsent(entry.getKey(), (int) occurrence);
        }
        return occurrenceMap;
    }

}
