package year2022;

import java.util.LinkedList;
import java.util.stream.Stream;

public class Dec2 extends DecBase {

    private enum Actions {
        A(1, "ROCK", "") {
            @Override
            int calculateScore(Actions rule) {
                return switch (rule) {
                    case A, X -> 3;
                    case Z, C -> 6;
                    default -> 0;
                };
            }
            @Override
            Actions getWin() {
                return C;
            }
            @Override
            Actions getDraw() {
                return A;
            }
            @Override
            Actions getLoose() {
                return B;
            }
        },
        X(1, "ROCK", "LOOSE") {
            @Override
            int calculateScore(Actions rule) {
                return switch (rule) {
                    case A, X -> 3;
                    case C, Z -> 6;
                    default -> 0;
                };
            }
            @Override
            Actions getWin() {
                return C;
            }
            @Override
            Actions getDraw() {
                return A;
            }
            @Override
            Actions getLoose() {
                return B;
            }
        },
        B(2, "PAPER", "") {
            @Override
            int calculateScore(Actions rule) {
                return switch (rule) {
                    case B, Y -> 3;
                    case X, A -> 6;
                    default -> 0;
                };
            }
            @Override
            Actions getWin() {
                return A;
            }
            @Override
            Actions getDraw() {
                return B;
            }
            @Override
            Actions getLoose() {
                return C;
            }
        },
        Y(2, "PAPER", "DRAW") {
            @Override
            int calculateScore(Actions rule) {
                return switch (rule) {
                    case B, Y -> 3;
                    case A, X -> 6;
                    default -> 0;
                };
            }
            @Override
            Actions getWin() {
                return A;
            }
            @Override
            Actions getDraw() {
                return B;
            }
            @Override
            Actions getLoose() {
                return C;
            }
        },
        C(3, "SCISSORS", "") {
            @Override
            int calculateScore(Actions rule) {
                return switch (rule) {
                    case C, Z -> 3;
                    case Y, B -> 6;
                    default -> 0;
                };
            }
            @Override
            Actions getWin() {
                return B;
            }
            @Override
            Actions getDraw() {
                return C;
            }
            @Override
            Actions getLoose() {
                return A;
            }
        },
        Z(3, "SCISSORS", "WIN") {
            @Override
            int calculateScore(Actions rule) {
                return switch (rule) {
                    case C, Z -> 3;
                    case B, Y -> 6;
                    default -> 0;
                };
            }
            @Override
            Actions getWin() {
                return B;
            }
            @Override
            Actions getDraw() {
                return C;
            }
            @Override
            Actions getLoose() {
                return A;
            }
        };
        private final int value;
        private final String type;
        private final String score;

        Actions(int value, String type, String score) {
            this.value = value;
            this.type = type;
            this.score = score;
        }

        abstract int calculateScore(Actions rule);
        abstract Actions getWin();
        abstract Actions getDraw();
        abstract Actions getLoose();
    }

    public Dec2(String fileName) {
        super(fileName);
    }

    @Override
    protected Dec2 readDefaultInput() {
        System.out.println("Reading default input.");
        inputStrings = new LinkedList<>(
                Stream.of("A Y", "B X", "C Z").toList()
        );
        return this;
    }

    @Override
    protected void calculate() {
        long totalScore = 0;
        for (String game : inputStrings) {
            final String[] players = game.split(" ");
            Actions opponent = Actions.valueOf(players[0]);
            Actions me = Actions.valueOf(players[1]);
            totalScore += me.value + me.calculateScore(opponent);
        }
        System.out.printf("Part 1 - Total score %d%n", totalScore);

        totalScore = 0;
        for (String game : inputStrings) {
            final String[] players = game.split(" ");
            Actions opponent = Actions.valueOf(players[0]);
            Actions score = Actions.valueOf(players[1]);
            final Actions myAction = switch (score) {
                case X -> opponent.getWin();
                case Y -> opponent.getDraw();
                default -> opponent.getLoose();
            };
            totalScore += myAction.value + myAction.calculateScore(opponent);
        }

        System.out.printf("Part 2 - Total score %d%n", totalScore);
    }
}
