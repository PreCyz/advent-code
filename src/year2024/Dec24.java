package year2024;

import base.DecBase;

import java.util.*;
import java.util.stream.Stream;

class Dec24 extends DecBase {

    public Dec24(int year) {
        super(year, 24);
    }

    @Override
    public Dec24 readDefaultInput() {
        System.out.println("Reading default input.");
        inputStrings = new LinkedList<>(Stream.of(
                "x00: 1",
                "x01: 1",
                "x02: 1",
                "y00: 0",
                "y01: 1",
                "y02: 0",

                "x00 AND y00 -> z00",
                "x01 XOR y01 -> z01",
                "x02 OR y02 -> z02"
        ).toList());
        return this;
    }

    enum Gate {
        AND {
            @Override
            byte calculate(byte b1, byte b2) {
                return Byte.parseByte(Integer.toBinaryString(b1 & b2));
            }
        },
        OR {
            @Override
            byte calculate(byte b1, byte b2) {
                return Byte.parseByte(Integer.toBinaryString(b1 | b2));
            }
        },
        XOR {
            @Override
            byte calculate(byte b1, byte b2) {
                return Byte.parseByte(Integer.toBinaryString(b1 ^ b2));
            }
        };
        abstract byte calculate(byte b1, byte b2);
    }

    record Instruction (String wire1, String wire2, Gate gate, Map<String, String> basicWires){
        byte calculate() {
            return gate.calculate(Byte.parseByte(basicWires.get(wire1)), Byte.parseByte(basicWires.get(wire2)));
        }
        boolean isBasic() {
            return (wire1.startsWith("x") || wire1.startsWith("y"))
                    && (wire2.startsWith("x") || wire2.startsWith("y"));
        }
    }

    @Override
    protected void calculatePart1() {
        Map<String, String> basicWires = new HashMap<>();
        Map<String, Instruction> instructions = new HashMap<>();
        for (String input : inputStrings) {
            if (input.contains(": ")) {
                String[] split = input.split(": ");
                basicWires.put(split[0], split[1]);
            } else if (input.contains(" -> ")) {
                String[] split = input.split(" ");
                instructions.put(split[4], new Instruction(split[0], split[2], Gate.valueOf(split[1]), basicWires));
            }
        }
        List<String> zets = instructions.keySet().stream().filter(k -> k.startsWith("z")).sorted((o1, o2) -> {
            int i1 = Integer.parseInt(o1.replace("z", ""));
            int i2 = Integer.parseInt(o2.replace("z", ""));
            return Integer.compare(i1, i2);
        }).toList();

        StringBuilder result = new StringBuilder();
        for (String z : zets) {
            result.insert(0, getValue(instructions.get(z), instructions));
        }

        System.out.printf("Part 1 - Sum %s%n", Long.parseLong(result.toString(), 2));
    }

    byte getValue(Instruction instruction, Map<String, Instruction> instructions) {
        if (instruction.isBasic()) {
            return instruction.calculate();
        }
        return instruction.gate.calculate(
                getValue(instructions.get(instruction.wire1), instructions),
                getValue(instructions.get(instruction.wire2), instructions)
        );
    }

    @Override
    protected void calculatePart2() {

//            System.out.printf("Part 2 - Sum %s%n", max);
    }
}