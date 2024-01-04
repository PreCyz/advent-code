package utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

public class Sequence {
    public final int startIdx;
    public final int length;
    private final ArrayList<Long> elements;
    private final Map<Long, Integer> occurrenceMap;

    private Sequence(int startIdx, int length, ArrayList<Long> elements, Map<Long, Integer> occurrenceMap) {
        this.startIdx = startIdx;
        this.length = length;
        this.elements = elements;
        this.occurrenceMap = occurrenceMap;
    }

    public ArrayList<Long> elements() {
        return new ArrayList<>(elements);
    }

    public Map<Long, Integer> occurrenceMap() {
        return new LinkedHashMap<>(occurrenceMap);
    }

    public static Sequence findSequence(ArrayList<Long> sums) {
        LinkedHashMap<Long, ArrayList<Integer>> sequenceMap = new LinkedHashMap<>();
        int startIdx = findStartIdxAndUpdateSequenceMap(sums, sequenceMap);

        int sequenceLength = findSequenceLength(sequenceMap, startIdx, sums);

        Map<Long, Integer> occurrenceMap = new LinkedHashMap<>();
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

    private static int findStartIdxAndUpdateSequenceMap(ArrayList<Long> sums, Map<Long, ArrayList<Integer>> sequenceMap) {
        LinkedHashSet<Long> sequenceSet = new LinkedHashSet<>();
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
        return startIdx;
    }

    private static int findSequenceLength(Map<Long, ArrayList<Integer>> sequenceMap, int startIdx, ArrayList<Long> sums) {
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
                System.out.printf("%nFound sequence length: %d%n", sequenceLength);
                System.out.println("Sequence start at index : " + (startIdx - sequenceLength));
                System.out.printf("The first element of cycle is: %d%n%n", sums.get(startIdx - sequenceLength));
                break;
            }
        }
        return sequenceLength;
    }

    private static Map<Long, Integer> checkMultipleOccurrences(int sequenceLength, Map.Entry<Long, ArrayList<Integer>> entry) {
        Map<Long, Integer> occurrenceMap = new HashMap<>();

        LinkedList<Integer> indexes = new LinkedList<>(entry.getValue());
        if (indexes.size() == 1) {
            occurrenceMap.putIfAbsent(entry.getKey(), 1);
        } else {
            Set<Integer> occurrences = new HashSet<>();
            for (int j = 1, size = indexes.size(); j < size; j++) {
                occurrences.add(indexes.get(j) - indexes.getFirst());
            }
            long occurrence = occurrences.stream().map(it -> it % sequenceLength).distinct().count();
            occurrenceMap.putIfAbsent(entry.getKey(), (int) occurrence);
        }

        return occurrenceMap;
    }

}
