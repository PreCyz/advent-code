package year2022;

import base.DecBase;

import java.util.LinkedList;
import java.util.Optional;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toCollection;

class Dec10 extends DecBase {
    protected Dec10(String fileName) {
        super(fileName);
    }

    @Override
    public Dec10 readDefaultInput() {
        System.out.println("Reading default input.");
        inputStrings = new LinkedList<>(Stream.of(
                "addx 15", "addx -11", "addx 6", "addx -3", "addx 5", "addx -1", "addx -8", "addx 13", "addx 4",
                        "noop", "addx -1", "addx 5", "addx -1", "addx 5", "addx -1", "addx 5", "addx -1", "addx 5",
                        "addx -1", "addx -35", "addx 1", "addx 24", "addx -19", "addx 1", "addx 16", "addx -11", "noop",
                        "noop", "addx 21", "addx -15", "noop", "noop", "addx -3", "addx 9", "addx 1", "addx -3", "addx 8",
                        "addx 1", "addx 5", "noop", "noop", "noop", "noop", "noop", "addx -36", "noop", "addx 1", "addx 7",
                        "noop", "noop", "noop", "addx 2", "addx 6", "noop", "noop", "noop", "noop", "noop", "addx 1",
                        "noop", "noop", "addx 7", "addx 1", "noop", "addx -13", "addx 13", "addx 7", "noop", "addx 1",
                        "addx -33", "noop", "noop", "noop", "addx 2", "noop", "noop", "noop", "addx 8", "noop", "addx -1",
                        "addx 2", "addx 1", "noop", "addx 17", "addx -9", "addx 1", "addx 1", "addx -3", "addx 11", "noop",
                        "noop", "addx 1", "noop", "addx 1", "noop", "noop", "addx -13", "addx -19", "addx 1", "addx 3",
                        "addx 26", "addx -30", "addx 12", "addx -1", "addx 3", "addx 1", "noop", "noop", "noop", "addx -9",
                        "addx 18", "addx 1", "addx 2", "noop", "noop", "addx 9", "noop", "noop", "noop", "addx -1", "addx 2",
                        "addx -37", "addx 1", "addx 3", "noop", "addx 15", "addx -21", "addx 22", "addx -6", "addx 1",
                        "noop", "addx 2", "addx 1", "noop", "addx -10", "noop", "noop", "addx 20", "addx 1", "addx 2",
                        "addx 2", "addx -6", "addx -11", "noop", "noop", "noop"
        ).toList());

//        inputStrings = new LinkedList<>(Stream.of(
//                "noop", "addx 3", "addx -5"
//        ).toList());
        return this;
    }

    record SignalRegistry(long x){}
    static class Instruction {
        String command;
        int value;
        int cycles;
        Instruction(String[] instruction) {
            command = instruction[0];
            cycles = 1;
            if (instruction.length > 1) {
                value = Integer.parseInt(instruction[1]);
                cycles = 2;
            }
        }

        boolean isNoop() {
            return cycles == 1;
        }
    }

    @Override
    protected void calculatePart1() {
        LinkedList<Instruction> instructions = inputStrings.stream()
                .map(l -> new Instruction(l.split(" ")))
                .collect(toCollection(LinkedList::new));
        LinkedList<Long> signalStrengths = new LinkedList<>();

        long x = 1;
        int cycle = 0;
        Optional<Instruction> instruction = Optional.empty();
        int blocked = 0;

        while(!instructions.isEmpty() || blocked >= 0)  {
            cycle++;
            if (blocked == 0) {
                if (instruction.isPresent() && !instruction.get().isNoop()) {
                    x += instruction.get().value;
                }
                if (!instructions.isEmpty()) {
                    instruction = Optional.of(instructions.removeFirst());
                    blocked = instruction.get().cycles;
                }
            }

            blocked--;
            if (cycle == 20 || (cycle - 20) % 40 == 0) {
                signalStrengths.add(cycle * x);
                //System.out.printf("Signal strength in cycle %d %d%n", cycle, cycle * x);
            }
        }

        System.out.printf("Print 1: X=%d cycles=%d%n", x, cycle);
        System.out.printf("Print 1: total signal strength %d%n", signalStrengths.stream().mapToLong(l -> l).sum());
    }

    @Override
    protected void calculatePart2() {
        LinkedList<Instruction> instructions = inputStrings.stream()
                .map(l -> new Instruction(l.split(" ")))
                .collect(toCollection(LinkedList::new));
        LinkedList<Long> signalStrengths = new LinkedList<>();

        long x = 1;
        int cycle = 0;
        Optional<Instruction> instruction = Optional.empty();
        int blocked = 0;
        int spriteSize = 3;
        int crt = 0;

        while(!instructions.isEmpty() || blocked >= 0)  {
            cycle++;
            if (blocked == 0) {
                if (instruction.isPresent() && !instruction.get().isNoop()) {
                    x += instruction.get().value;
                }
                if (!instructions.isEmpty()) {
                    instruction = Optional.of(instructions.removeFirst());
                    blocked = instruction.get().cycles;
                }
            }

            blocked--;
            if (cycle == 20 || (cycle - 20) % 40 == 0) {
                signalStrengths.add(cycle * x);
            }

            if (cycle % 40 == 0) {
                if (Math.abs(x - crt) <= 1) {
                    System.out.println("#");
                } else {
                    System.out.println(".");
                }
                crt = 0;
            } else {
                if (Math.abs(x - crt) <= 1) {
                    System.out.print("#");
                } else {
                    System.out.print(".");
                }
                crt++;
            }
        }
        System.out.println("Print 2");
    }
}
