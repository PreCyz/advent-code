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
//                "[[10],[6]]",
//                "[[4,10,[[4,2,2,1,9],8,[6,3,0],[3,4,6,7]]],[[5,5,[4],6],3],[[],10,5,9],[5,10]]"
//                "[[],[[[4,2,0],[7,6,9,3],[6,0,0,7],1]],[],[[5,5,5,[3]],8],[[3,1,[5,9],9,[7]],7,8,[[10,1,6,4],10]]]",
//"[[[[]],[[0],[],[3,4,10]]],[],[1,8,[7,6],[]],[[],[[1,4,8,0,7],5,[4,9,2,4,9],[3,6,7,9]],[[0,8,8,1,7],2,[0,8,9,4],[6,5,8,0],4],7,7],[3,[[2,4,6,0,10],6,[0,3],0,3]]]"
        ).toList());
        return this;
    }

    record Pair(Packet left, Packet right) {
    }


    static class Packet {
        LinkedList<Object> list;

        Packet() {
            list = new LinkedList<>();
        }

        Packet init(String value) {
            //System.out.println(value);
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
        System.out.println(pairs.size());

        LinkedList<Integer> indices = new LinkedList<>();
        for (int i = 0; i < pairs.size(); i++) {
            Pair pair = pairs.get(i);
            try {
                if (rightOrder(pair.left.list, pair.right.list)) {
                    indices.add(i + 1);
                }
            }catch (Exception e) {
                if (Boolean.parseBoolean(e.getMessage())) {
                    indices.add(i + 1);
                }
            }
        }
        System.out.println("Part 1: " + indices.stream().mapToInt(it -> it).sum());

    }

    boolean rightOrder(LinkedList<Object> left, LinkedList<Object> right) throws Exception {
        if (left.isEmpty() && !right.isEmpty()) {
            throw new Exception("" + true);
        } else if (!left.isEmpty() && right.isEmpty()) {
            throw new Exception("" + false);
        } else if (left.isEmpty()) {
            return true;
        }
        boolean result = true;
        while (!left.isEmpty()) {
            if (right.isEmpty()) {
                throw new Exception("" + false);
            }

            final Object leftEl = left.removeFirst();
            final Object rightEl = right.removeFirst();
            if (leftEl instanceof Integer && rightEl instanceof Integer) {
                int lInt = Integer.class.cast(leftEl).intValue();
                int rInt = Integer.class.cast(rightEl).intValue();
                if (lInt != rInt) {
                    if (lInt > rInt) {
                        throw new Exception("" + false);
                    } else {
                        throw new Exception("" + true);
                    }
                }
            } else if (leftEl instanceof LinkedList<?> && rightEl instanceof Integer) {
                result &= rightOrder(LinkedList.class.cast(leftEl), new LinkedList<>(List.of(rightEl)));
            } else if (leftEl instanceof Integer && rightEl instanceof LinkedList<?>) {
                result &= rightOrder(new LinkedList<>(List.of(leftEl)), LinkedList.class.cast(rightEl));
            } else if (leftEl instanceof LinkedList<?> && rightEl instanceof LinkedList<?>) {
                result &= rightOrder(LinkedList.class.cast(leftEl), LinkedList.class.cast(rightEl));
            }
        }
        return result;
    }
}
