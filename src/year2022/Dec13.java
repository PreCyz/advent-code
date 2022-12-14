package year2022;

import base.DecBase;

import java.util.*;
import java.util.stream.Stream;

class Dec13 extends DecBase {

    protected Dec13(String fileName) {
        super(fileName);
    }

    @Override
    public Dec13 readDefaultInput() {
        System.out.println("Reading default input.");
        inputStrings = new LinkedList<>(Stream.of(
                "[1,1,3,1,1]",
                "[1,1,5,1,1]",
                "",
                "[[1],[2,3,4]]",
                "[[1],4]",
                "",
                "[9]",
                "[[8,7,6]]",
                "",
                "[[4,4],4,4]",
                "[[4,4],4,4,4]",
                "",
                "[7,7,7,7]",
                "[7,7,7]",
                "",
                "[]",
                "[3]",
                "",
                "[[[]]]",
                "[[]]",
                "",
                "[1,[2,[3,[4,[5,6,7]]]],8,9]",
                "[1,[2,[3,[4,[5,6,0]]]],8,9]"
        ).toList());
        return this;
    }

    static class Terminator extends Exception {
        Result result;

        Terminator(Result result) {
            super(result.name());
            this.result = result;
        }
    }

    enum Result {
        RIGHT, WRONG, NO_IDEA;

        Result and(Result result) {
            if (this == WRONG || result == WRONG) {
                return WRONG;
            } else if (this == NO_IDEA) {
                return result;
            }
            return this;
        }
    }

    record Pair(Packet left, Packet right) { }

    static class Packet {
        LinkedList<Object> list;
        String value;

        Packet() {
            list = new LinkedList<>();
        }

        Packet init(String value) {
            this.value = value;
            list.addAll(createObject(value.substring(1, value.lastIndexOf("]"))));
            return this;
        }

        int findClosingBracket(String value) {
            int noOpenBrackets = 1;
            final char[] chars = value.toCharArray();
            for (int i = 1; i < chars.length; i++) {
                if (chars[i] == '[') {
                    noOpenBrackets++;
                } else if (chars[i] == ']') {
                    noOpenBrackets--;
                }
                if (noOpenBrackets == 0) {
                    return i;
                }
            }
            throw new IllegalArgumentException("Could not find closing bracket");
        }

        LinkedList<Object> createObject(String value) {

            if (value.startsWith("[")) {
                LinkedList<Object> result = new LinkedList<>();
                int closingBracketIdx = findClosingBracket(value);
                result.add(createObject(value.substring(1, closingBracketIdx)));
                if (!value.substring(closingBracketIdx + 1).isEmpty()) {
                    result.addAll(createObject(value.substring(closingBracketIdx + 1)));
                }
                return result;

            } else if (!value.isEmpty()) {

                if (value.startsWith(",")) {
                    value = value.substring(value.indexOf(",") + 1);
                }

                LinkedList<Object> result = new LinkedList<>();
                final int openBracket = value.indexOf("[");
                if (openBracket > 0) {
                    final String[] split = value.substring(0, openBracket).split(",");

                    for (String digit : split) {
                        if (!digit.isEmpty()) {
                            result.add(Integer.parseInt(digit));
                        }
                    }

                    result.addAll(createObject(value.substring(openBracket)));

                    return result;
                } else if (openBracket == 0) {
                    final int closingBracketIdx = findClosingBracket(value);
                    if (!value.substring(closingBracketIdx + 1).isEmpty()) {
                        result.addAll(createObject(value));
                    } else {
                        result.add(createObject(value.substring(1, value.length() - 1)));
                    }
                    return result;
                } else {
                    final String[] split = value.split(",");
                    for (String digit : split) {
                        if (!digit.isEmpty()) {
                            result.add(Integer.parseInt(digit));
                        }
                    }
                    return result;
                }
            }
            return new LinkedList<>();
        }
    }

    @Override
    protected void calculatePart1() {
        final Iterator<String> iterator = inputStrings.iterator();
        ArrayList<Pair> pairs = new ArrayList<>();
        while (iterator.hasNext()) {
            final String line = iterator.next();
            if (!line.isEmpty()) {
                pairs.add(new Pair(new Packet().init(line), new Packet().init(iterator.next())));
            }
        }
        System.out.printf("Pair size %d%n", pairs.size());

        LinkedList<Integer> indices = new LinkedList<>();
        for (int i = 0; i < pairs.size(); i++) {
            Pair pair = pairs.get(i);
            try {
                final Result result = rightOrder(pair.left.list, pair.right.list);
                if (result == Result.RIGHT) {
                    indices.add(i + 1);
                }
            } catch (Terminator e) {
                if (e.result == Result.RIGHT) {
                    indices.add(i + 1);
                }
            }
        }
        System.out.printf("Part 1: %d%n", indices.stream().mapToInt(it -> it).sum());
    }

    Result rightOrder(LinkedList<Object> left, LinkedList<Object> right) throws Terminator {
        if (left.isEmpty() && !right.isEmpty()) {
            throw new Terminator(Result.RIGHT);
        } else if (!left.isEmpty() && right.isEmpty()) {
            throw new Terminator(Result.WRONG);
        } else if (left.isEmpty()) {
            return Result.NO_IDEA;
        }
        Result result = Result.NO_IDEA;
        final Iterator<Object> lIter = left.iterator();
        final Iterator<Object> rIter = right.iterator();
        while (lIter.hasNext()) {
            if (!rIter.hasNext()) {
                throw new Terminator(Result.WRONG);
            }

            final Object leftEl = lIter.next();
            final Object rightEl = rIter.next();
            if (leftEl instanceof Integer && rightEl instanceof Integer) {
                int lInt = Integer.class.cast(leftEl).intValue();
                int rInt = Integer.class.cast(rightEl).intValue();
                if (lInt != rInt) {
                    if (lInt > rInt) {
                        throw new Terminator(Result.WRONG);
                    } else {
                        throw new Terminator(Result.RIGHT);
                    }
                }
            } else if (leftEl instanceof LinkedList<?> && rightEl instanceof Integer) {
                result = result.and(rightOrder(LinkedList.class.cast(leftEl), new LinkedList<>(List.of(rightEl))));
            } else if (leftEl instanceof Integer && rightEl instanceof LinkedList<?>) {
                result = result.and(rightOrder(new LinkedList<>(List.of(leftEl)), LinkedList.class.cast(rightEl)));
            } else if (leftEl instanceof LinkedList<?> && rightEl instanceof LinkedList<?>) {
                result = result.and(rightOrder(LinkedList.class.cast(leftEl), LinkedList.class.cast(rightEl)));
            }
            if (!lIter.hasNext() && rIter.hasNext()) {
                throw new Terminator(Result.RIGHT);
            }
        }
        return result;
    }

    @Override
    protected void calculatePart2() {
        final Iterator<String> iterator = inputStrings.iterator();
        ArrayList<Packet> packets = new ArrayList<>();
        packets.add(new Packet().init("[[2]]"));
        packets.add(new Packet().init("[[6]]"));
        while (iterator.hasNext()) {
            final String line = iterator.next();
            if (!line.isEmpty()) {
                packets.add(new Packet().init(line));
            }
        }

        ArrayList<Packet> sorted = getSorted(packets);

        int result = 1;
        for (int i = 0; i < sorted.size(); i++) {
            final Packet packet = sorted.get(i);
            if ("[[2]]".equals(packet.value) || "[[6]]".equals(packet.value)) {
                result *= (i + 1);
            }
        }
        System.out.printf("Part 2: %d%n", result);
    }

    private ArrayList<Packet> getSorted(ArrayList<Packet> packets) {
        ArrayList<Packet> sorted = new ArrayList<>(packets.size());
        for (Packet packet : packets) {
            LinkedList<Packet> tmp = new LinkedList<>();
            if (sorted.isEmpty()) {
                sorted.add(packet);
            } else {
                boolean wasPacketAdded = false;
                for (Packet value : sorted) {
                    if (!wasPacketAdded) {
                        try {
                            final Result result = rightOrder(packet.list, value.list);
                            if (EnumSet.of(Result.RIGHT, Result.NO_IDEA).contains(result)) {
                                tmp.add(packet);
                                wasPacketAdded = true;
                            }
                        } catch (Terminator e) {
                            if (EnumSet.of(Result.RIGHT, Result.NO_IDEA).contains(e.result)) {
                                tmp.add(packet);
                                wasPacketAdded = true;
                            }
                        }
                    }
                    tmp.add(value);
                }
                if (!wasPacketAdded) {
                    tmp.add(packet);
                }
                sorted.clear();
                sorted.addAll(tmp);
            }
        }
        return sorted;
    }
}
