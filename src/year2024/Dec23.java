package year2024;

import base.DecBase;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class Dec23 extends DecBase {

    public Dec23(int year) {
        super(year, 23);
    }

    @Override
    public Dec23 readDefaultInput() {
        System.out.println("Reading default input.");
        inputStrings = new LinkedList<>(Stream.of(
                "kh-tc",
                "qp-kh",
                "de-cg",
                "ka-co",
                "yn-aq",
                "qp-ub",
                "cg-tb",
                "vc-aq",
                "tb-ka",
                "wh-tc",
                "yn-cg",
                "kh-ub",
                "ta-co",
                "de-co",
                "tc-td",
                "tb-wq",
                "wh-td",
                "ta-ka",
                "td-qp",
                "aq-cg",
                "wq-ub",
                "ub-vc",
                "de-ta",
                "wq-aq",
                "wq-vc",
                "wh-yn",
                "ka-de",
                "kh-ta",
                "co-tc",
                "wh-qp",
                "tb-vc",
                "td-yn"
        ).toList());
        return this;
    }

    @Override
    protected void calculatePart1() {
        Map<String, Set<String>> computers = new HashMap<>();
        for (String input : inputStrings) {
            String[] split = input.split("-");
            if (computers.containsKey(split[0])) {
                computers.get(split[0]).add(split[1]);
            } else {
                computers.putIfAbsent(split[0], new HashSet<>(Set.of(split[1])));
            }
            if (computers.containsKey(split[1])) {
                computers.get(split[1]).add(split[0]);
            } else {
                computers.putIfAbsent(split[1], new HashSet<>(Set.of(split[0])));
            }
        }

        Set<String> tmp = new HashSet<>(computers.keySet());

        Set<Set<String>> groups = new LinkedHashSet<>();
        for (String comp : computers.keySet()) {
            var set1 = computers.get(comp);
            tmp.remove(comp);
            for (String comp2 : tmp) {
                if (!set1.contains(comp2)) {continue;}
                var set2 = new HashSet<>(computers.get(comp2));
                set2.retainAll(set1);
                for (String c : set2) {
                    if (computers.get(c).containsAll(Set.of(comp, comp2))) {
                        groups.add(new HashSet<>(Set.of(comp, comp2, c)));
                    }
                }
            }
        }

        long sum = 0;
        for (Set<String> set : groups) {
            if (set.stream().anyMatch(s -> s.startsWith("t"))) {
                sum++;
            }
        }
        System.out.printf("Part 1 - Sum %s%n", sum);
    }

    @Override
    protected void calculatePart2() {
        Map<String, Set<String>> computers = new HashMap<>();
        for (String input : inputStrings) {
            String[] split = input.split("-");
            if (computers.containsKey(split[0])) {
                computers.get(split[0]).add(split[1]);
            } else {
                computers.putIfAbsent(split[0], new HashSet<>(Set.of(split[1])));
            }
            if (computers.containsKey(split[1])) {
                computers.get(split[1]).add(split[0]);
            } else {
                computers.putIfAbsent(split[1], new HashSet<>(Set.of(split[0])));
            }
        }


        int score = Integer.MIN_VALUE;
        Set<String> group = new TreeSet<>(String::compareTo);

        for (String comp : computers.keySet()) {
            var set1 = new HashSet<>(computers.get(comp));

            LinkedHashMap<String, Integer> scoreMap = new LinkedHashMap<>(set1.stream().collect(Collectors.toMap(v -> v, v -> 1)));

            var tmp = new HashSet<>(computers.get(comp));
            for (String comp2 : set1) {
                tmp.remove(comp2);
                for (String c : tmp) {
                    if (computers.get(comp2).contains(c)) {
                        scoreMap.put(comp2, scoreMap.get(comp2) + 1);
                        scoreMap.put(c, scoreMap.get(c) + 1);
                    }
                }
            }
            int count = scoreMap.entrySet().stream().collect(Collectors.groupingBy(Map.Entry::getValue)).values().stream().mapToInt(List::size).max().orElse(0);
            Map<Integer, List<Map.Entry<String, Integer>>> collect = scoreMap.entrySet().stream().collect(Collectors.groupingBy(Map.Entry::getValue));
            Integer maxOccurrence = collect.entrySet().stream().filter(e -> e.getValue().size() == count).findFirst().get().getKey();
            if (count * maxOccurrence > score) {
                score = count * maxOccurrence;
                int finalMaxScore = maxOccurrence;
                group = new TreeSet<>(String::compareTo);
                group.addAll(scoreMap.entrySet().stream().filter(e -> e.getValue() == finalMaxScore).map(Map.Entry::getKey).collect(Collectors.toSet()));
                group.add(comp);
                System.out.printf("group %s score is %s%n", comp, scoreMap);
                System.out.printf("%s max occurrence:[%d] appears [%d]%n", comp, maxOccurrence, count);
            }
        }

        System.out.printf("Part 2 - Sum %s%n", String.join(",", group));
    }
}