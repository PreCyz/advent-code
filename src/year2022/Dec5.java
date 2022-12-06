package year2022;

import base.DecBase;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class Dec5 extends DecBase {

    private LinkedList<Move> procedure;
    private ArrayList<LinkedList<String>> stacks;

    public Dec5(String fileName) {
        super(fileName);
    }

    private static class Move {
        int cratesNumber;
        int fromStack;
        int toStack;

        public Move(int cratesNumber, int fromStack, int toStack) {
            this.cratesNumber = cratesNumber;
            this.fromStack = fromStack;
            this.toStack = toStack;
        }

        @Override
        public String toString() {
            return "Move{" +
                    "cratesNumber=" + cratesNumber +
                    ", fromStack=" + fromStack +
                    ", toStack=" + toStack +
                    '}';
        }
    }

    @Override
    public Dec5 readDefaultInput() {
        System.out.println("Reading default input.");
        inputStrings = new LinkedList<>(Stream.of(
                "    [D]    ",
                "[N] [C]    ",
                "[Z] [M] [P]",
                " 1   2   3 ",
                "",
                "move 1 from 2 to 1",
                "move 3 from 1 to 3",
                "move 2 from 2 to 1",
                "move 1 from 1 to 2"
        ).toList());
        stacks = createStackFromLines();
        createProcedure();
        return this;
    }

    private void createProcedure() {
        boolean wasEmptyLine = false;
        procedure = new LinkedList<>();
        for (String line : inputStrings) {
            if (wasEmptyLine) {
                final String[] words = line.split(" ");
                procedure.add(new Move(Integer.parseInt(words[1]), Integer.parseInt(words[3]), Integer.parseInt(words[5])));
            } else if (line == null || line.isBlank() || line.isEmpty()) {
                wasEmptyLine = true;
            }
        }
        System.out.println(procedure);
    }

    @Override
    public DecBase readInput() throws IOException {
        System.out.printf("%nReading input from [%s]%n", getFileName());
        inputStrings = new LinkedList<>();
        try (Scanner scanner = new Scanner(new FileInputStream(getFileName()))) {
            while (scanner.hasNext()) {
                inputStrings.add(scanner.nextLine());
            }
        }

        createProcedure();
        return this;
    }

    @Override
    protected void calculatePart1() {
        stacks = createStackFromLines();
        for (Move move : procedure) {
            final LinkedList<String> fromStack = stacks.get(move.fromStack - 1);
            final LinkedList<String> toStack = stacks.get(move.toStack - 1);
            for (int crate = 0; crate < move.cratesNumber; crate++) {
                toStack.addFirst(fromStack.removeFirst());
            }
        }

        System.out.printf("Part 1: first crates %s%n",
                stacks.stream().map(LinkedList::getFirst).collect(Collectors.joining())
        );
    }

    @Override
    protected void calculatePart2() {
        stacks = createStackFromLines();
        for (Move move : procedure) {
            final LinkedList<String> fromStack = stacks.get(move.fromStack - 1);
            final LinkedList<String> toStack = stacks.get(move.toStack - 1);
            for (int crate = move.cratesNumber - 1; crate >= 0; crate--) {
                toStack.addFirst(fromStack.remove(crate));
            }
        }

        System.out.printf("Part 2: first crates %s%n",
                stacks.stream().map(LinkedList::getFirst).collect(Collectors.joining())
        );
    }

    private ArrayList<LinkedList<String>> createStackFromLines() {
        final Pattern isDigit = Pattern.compile("\\d+");
        List<String> stackLines = new ArrayList<>();
        int numberOfStacks = 0;
        for (String line : inputStrings) {
            String withoutSpaces = Pattern.compile("\\s").matcher(line).replaceAll("");
            if (isDigit.matcher(withoutSpaces).matches()) {
                final String[] stackNumber = line.split(" ");
                numberOfStacks = Integer.parseInt(Arrays.stream(stackNumber)
                        .filter(s -> !s.isEmpty())
                        .collect(Collectors.toCollection(LinkedList::new))
                        .getLast()
                );
                System.out.printf("Number of stacks %d%n", numberOfStacks);
                break;
            } else {
                stackLines.add(line);
            }
        }

        ArrayList<LinkedList<String>> newStacks = new ArrayList<>(numberOfStacks);
        for (int i = 0; i < numberOfStacks; i++) {
            newStacks.add(i, new LinkedList<>());
        }

        for (String line : stackLines) {
            for (int i = 0; i < numberOfStacks; i++) {
                final int crateOccurrence = 4 * i;
                if (crateOccurrence + 3 <= line.length()) {
                    String crate = line.substring(crateOccurrence, crateOccurrence + 3);
                    if (!crate.isBlank()) {
                        newStacks.get(i).add(crate.replace("[", "").replace("]", ""));
                    }
                }
            }
        }

        return newStacks;
    }
}
