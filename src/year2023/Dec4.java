package year2023;

import base.DecBase;

import java.util.*;
import java.util.stream.Stream;

class Dec4 extends DecBase {

    public Dec4(int year) {
        super(year, 4);
    }

    @Override
    public Dec4 readDefaultInput() {
        System.out.println("Reading default input.");
        inputStrings = new LinkedList<>(Stream.of(
                        "Card 1: 41 48 83 86 17 | 83 86  6 31 17  9 48 53",
                        "Card 2: 13 32 20 16 61 | 61 30 68 82 17 32 24 19",
                        "Card 3:  1 21 53 59 44 | 69 82 63 72 16 21 14  1",
                        "Card 4: 41 92 73 84 69 | 59 84 76 51 58  5 54 83",
                        "Card 5: 87 83 26 28 32 | 88 30 70 12 93 22 82 36",
                        "Card 6: 31 18 13 56 72 | 74 77 10 23 35 67 36 11"
                )
                .toList());
        return this;
    }

    @Override
    protected void calculatePart1() {
        ArrayList<Long> worths = new ArrayList<>(inputStrings.size());
        for (String line : inputStrings) {
            ArrayList<String> winningCards = new ArrayList<>(Arrays.stream(
                    line.split(": ")[1].split(" \\| ")[0].split(" ")
            ).filter(v -> !"".equals(v)).toList());

            ArrayList<String> cardsIHave = new ArrayList<>(Arrays.stream(
                    line.split(": ")[1].split(" \\| ")[1].split(" ")
            ).filter(v -> !"".equals(v)).toList());

            long worth = 0;
            for (String winningCard: winningCards) {
                if (cardsIHave.contains(winningCard)) {
                    if (worth == 0) {
                        worth = 1;
                    } else {
                        worth *= 2;
                    }
                }
            }
            worths.add(worth);
        }
        long sum = worths.stream().mapToLong(it -> it).sum();
        System.out.printf("Part 1 - Total score %d%n", sum);
    }

    @Override
    protected void calculatePart2() {
        ArrayList<Integer> cards = new ArrayList<>(Stream.generate(() -> 1).limit(inputStrings.size() + 1).toList());
        int cardNumber = 0;
        for (String line : inputStrings) {
            cardNumber++;
            ArrayList<String> winningCards = new ArrayList<>(Arrays.stream(
                    line.split(": ")[1].split(" \\| ")[0].split(" ")
            ).filter(v -> !"".equals(v)).toList());

            ArrayList<String> cardsIHave = new ArrayList<>(Arrays.stream(
                    line.split(": ")[1].split(" \\| ")[1].split(" ")
            ).filter(v -> !"".equals(v)).toList());

            long noOfWinningCards = 0;
            for (String winningCard: winningCards) {
                if (cardsIHave.contains(winningCard)) {
                    noOfWinningCards++;
                }
            }

            if (noOfWinningCards > 0) {
                for (int j = 1; j <= cards.get(cardNumber); j++) {
                    for (int i = 1; i <= noOfWinningCards; i++) {
                        cards.set(cardNumber + i, cards.get(cardNumber + i) + 1);
                    }
                }
            }
        }

        cards.remove(0);
        long sum = cards.stream().mapToLong(it -> (long) it).sum();
        System.out.printf("Part 2 - Total score %d%n", sum);
    }

}
