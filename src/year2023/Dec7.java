package year2023;

import base.DecBase;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;


class Dec7 extends DecBase {

    public Dec7(String fileName) {
        super(fileName);
    }

    private static final Map<Character, Integer> CARDS = new HashMap<>();

    static {
        CARDS.put('2', 0);
        CARDS.put('3', 1);
        CARDS.put('4', 2);
        CARDS.put('5', 3);
        CARDS.put('6', 4);
        CARDS.put('7', 5);
        CARDS.put('8', 6);
        CARDS.put('9', 7);
        CARDS.put('T', 8);
        CARDS.put('J', 9);
        CARDS.put('Q', 10);
        CARDS.put('K', 11);
        CARDS.put('A', 12);
    }

    private static final Map<Character, Integer> CARDS_J = new HashMap<>();

    static {
        CARDS_J.put('2', 0);
        CARDS_J.put('3', 1);
        CARDS_J.put('4', 2);
        CARDS_J.put('5', 3);
        CARDS_J.put('6', 4);
        CARDS_J.put('7', 5);
        CARDS_J.put('8', 6);
        CARDS_J.put('9', 7);
        CARDS_J.put('T', 8);
        CARDS_J.put('J', -1);
        CARDS_J.put('Q', 10);
        CARDS_J.put('K', 11);
        CARDS_J.put('A', 12);
    }


    @Override
    public Dec7 readDefaultInput() {
        System.out.println("Reading default input.");
        inputStrings = new LinkedList<>(Stream.of(
                        "32T3K 765",
                        "T55J5 684",
                        "KK677 28",
                        "KTJJT 220",
                        "QQQJA 483"
                )
                .toList());
        return this;
    }

    private enum Type {
        FIVE_OF_A_KIND(6),
        FOUR_OF_A_KIND(5),
        FULL_HOUSE(4),
        THREE_OF_A_KIND(3),
        TWO_PAIR(2),
        ONE_PAIR(1),
        HIGH_CARD(0);
        final int strength;

        Type(int strength) {
            this.strength = strength;
        }

        static Type getType(char[] cards) {
            Map<Character, Integer> labelsMap = new HashMap<>();
            for (char label : cards) {
                if (labelsMap.containsKey(label)) {
                    int numberOfCards = labelsMap.get(label) + 1;
                    labelsMap.replace(label, numberOfCards);
                } else {
                    labelsMap.put(label, 1);
                }
            }
            if (labelsMap.size() == 5) {
                return HIGH_CARD;
            } else if (labelsMap.size() == 4) {
                return ONE_PAIR;
            } else if (labelsMap.size() == 3) {
                int multiple = 1;
                for (Integer key : labelsMap.values()) {
                    multiple *= key;
                }
                if (multiple == 4) {
                    return TWO_PAIR;
                } else if (multiple == 3) {
                    return THREE_OF_A_KIND;
                }
            } else if (labelsMap.size() == 2) {
                int multiple = 1;
                for (Integer key : labelsMap.values()) {
                    multiple *= key;
                }
                if (multiple == 6) {
                    return FULL_HOUSE;
                } else if (multiple == 4) {
                    return FOUR_OF_A_KIND;
                }
            }
            return FIVE_OF_A_KIND;
        }

        static Type getTypeWithJ(char[] cards) {
            final char J = 'J';
            Map<Character, Integer> labelsMap = new HashMap<>();
            for (char label : cards) {
                if (labelsMap.containsKey(label)) {
                    int numberOfCards = labelsMap.get(label) + 1;
                    labelsMap.replace(label, numberOfCards);
                } else {
                    labelsMap.put(label, 1);
                }
            }
            if (labelsMap.size() == 5) {
                if (labelsMap.containsKey(J)) {
                    return ONE_PAIR;
                }
                return HIGH_CARD;
            } else if (labelsMap.size() == 4) {
                if (labelsMap.containsKey(J)) {
                    switch (labelsMap.get(J)) {
                        case 1, 2:
                            return THREE_OF_A_KIND;
                    }
                }
                return ONE_PAIR;
            } else if (labelsMap.size() == 3) {
                int multiple = 1;
                for (Integer key : labelsMap.values()) {
                    multiple *= key;
                }
                if (multiple == 4) {
                    if (labelsMap.containsKey(J)) {
                        switch (labelsMap.get(J)) {
                            case 1:
                                return FULL_HOUSE;
                            case 2:
                                return FOUR_OF_A_KIND;
                        }
                    }
                    return TWO_PAIR;
                } else if (multiple == 3) {
                    if (labelsMap.containsKey(J)) {
                        switch (labelsMap.get(J)) {
                            case 1, 3:
                                return FOUR_OF_A_KIND;
                        }
                    }
                    return THREE_OF_A_KIND;
                }
            } else if (labelsMap.size() == 2) {
                int multiple = 1;
                for (Integer key : labelsMap.values()) {
                    multiple *= key;
                }
                if (multiple == 6) {
                    if (labelsMap.containsKey(J)) {
                        switch (labelsMap.get(J)) {
                            case 2, 3:
                                return FIVE_OF_A_KIND;
                        }
                    }
                    return FULL_HOUSE;
                } else if (multiple == 4) {
                    if (labelsMap.containsKey(J)) {
                        return FIVE_OF_A_KIND;
                    }
                    return FOUR_OF_A_KIND;
                }
            }
            return FIVE_OF_A_KIND;
        }
    }

    private static class Hand {
        String value;
        char[] cards;
        Type type;
        int bid;

        Hand(char[] cards, Type type, int bid) {
            this.cards = cards;
            this.type = type;
            this.bid = bid;
            this.value = String.valueOf(cards);
        }

        @Override
        public String toString() {
            return value + " " + bid + " " + type;
        }

        static Comparator<Hand> comparator(Map<Character, Integer> cardsMap) {
            return (o1, o2) -> {
                if (o1.type.strength < o2.type.strength) {
                    return -1;
                } else if (o1.type.strength > o2.type.strength) {
                    return 1;
                } else {
                    for (int i = 0; i < o1.cards.length; i++) {
                        if (cardsMap.get(o1.cards[i]) < cardsMap.get(o2.cards[i])) {
                            return -1;
                        } else if (cardsMap.get(o1.cards[i]) > cardsMap.get(o2.cards[i])) {
                            return 1;
                        }
                    }
                }
                return 0;
            };
        }
    }

    @Override
    protected void calculatePart1() {
        Map<Type, ArrayList<Hand>> map = new HashMap<>();
        ArrayList<Hand> hands = new ArrayList<>(inputStrings.size());
        for (String line : inputStrings) {
            String handStr = line.split(" ")[0];
            int bid = Integer.parseInt(line.split(" ")[1]);
            Type type = Type.getType(handStr.toCharArray());
            Hand hand = new Hand(handStr.toCharArray(), type, bid);
            if (map.containsKey(type)) {
                map.get(type).add(hand);
            } else {
                map.put(type, new ArrayList<>(List.of(hand)));
            }
            hands.add(hand);
        }

        map.forEach((k, v) -> v.sort(Hand.comparator(CARDS)));
        hands.sort(Hand.comparator(CARDS));

        long sum = 0;
        for (int i = 0; i < hands.size(); i++) {
            long rank = i + 1;
            sum += rank * hands.get(i).bid;
        }
        System.out.printf("Part 1 - Total score %d%n", sum);
    }

    @Override
    protected void calculatePart2() {
        Map<Type, ArrayList<Hand>> map = new HashMap<>();
        ArrayList<Hand> hands = new ArrayList<>(inputStrings.size());
        for (String line : inputStrings) {
            String handStr = line.split(" ")[0];
            int bid = Integer.parseInt(line.split(" ")[1]);
            Type type = Type.getTypeWithJ(handStr.toCharArray());
            Hand hand = new Hand(handStr.toCharArray(), type, bid);
            if (map.containsKey(type)) {
                map.get(type).add(hand);
            } else {
                map.put(type, new ArrayList<>(List.of(hand)));
            }
            hands.add(hand);
        }

        map.forEach((k, v) -> v.sort(Hand.comparator(CARDS_J)));
        hands.sort(Hand.comparator(CARDS_J));

        long sum = 0;
        for (int i = 0; i < hands.size(); i++) {
            long rank = i + 1;
            sum += rank * hands.get(i).bid;
        }

        System.out.printf("Part 2 - Total score %d%n", sum);
    }

}
