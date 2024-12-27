package year2024;

import base.DecBase;

import java.util.*;
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
        Set<String> computers = new HashSet<>();
        for (String input : inputStrings) {
            String[] split = input.split("-");
            computers.add(split[0]);
            computers.add(split[1]);
        }
        Set<Set<String>> groups = new HashSet<>();
        for (String computer : computers) {
            Set<String> group = new HashSet<>();
            group.add(computer);
            Set<String> later = new HashSet<>();
            for (String connection : inputStrings) {
                if (connection.contains(computer)) {
                    var comps = connection.split("-");
                    if (comps[0].equals(computer)) {
                        later.add(comps[1]);
                    } else {
                        later.add(comps[0]);
                    }
                }
            }
            if (later.size() > 1) {
                group.addAll(getConnections(later));
            }
            groups.add(group);
        }

        long sum = 0;
        for (Set<String> set : groups) {
            sum += set.stream().filter(s -> s.startsWith("t")).count();
        }

        System.out.printf("Part 1 - Sum %s%n", sum);
    }

    /*private void belongedToGroup(Set<String> later, Set<String> group) {
        LinkedList<String> comps = new LinkedList<>(later);
        for (String connection : inputStrings) {
            if (connection.contains(comps.getFirst()) && connection.contains(comps.getLast())) {
                if (group.size() > 1) {
                    for (String comp : group) {
                        boolean isPartOfGroup = true;
                        for (String s : group) {
                            isPartOfGroup &= isConnected(comp, s);
                            if (!isPartOfGroup) {
                                break;
                            }
                        }


                    }

                } else {
                    group.add(comps.getFirst());
                    group.add(comps.getLast());
                }
                break;
            }
        }
    }

    boolean isConnected(String comp1, String comp2) {
        for (String connection : inputStrings) {
            if (connection.contains(comp1) && connection.contains(comp2)) {
                return true;
            }
        }
        return false;
    }*/

    Set<String> getConnections(Set<String> later) {
        Set<String> result = new HashSet<>();
        Set<String> compi = new HashSet<>(later);
        Set<String> compj = new HashSet<>(later);
        Iterator<String> iterator = compi.iterator();
        while (iterator.hasNext() && !compj.isEmpty()) {
            String comp1 = iterator.next();
            compj.remove(comp1);
            int size = result.size();
            Set<String> tmp = new HashSet<>(compj);
            for (String comp2 : compj) {
                for (String connection : inputStrings) {
                    if (connection.contains(comp1) && connection.contains(comp2)) {
                        result.add(comp1);
                        result.add(comp2);
                        tmp.remove(comp2);
                        break;
                    }
                }
                if (result.size() != size) {
                    break;
                }
            }
            if (result.size() != size) {
                compj = new HashSet<>(tmp);
            }
        }
        return result;
    }

    @Override
    protected void calculatePart2() {

//            System.out.printf("Part 2 - Sum %s%n", max);
    }
}