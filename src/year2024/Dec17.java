package year2024;

import base.DecBase;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class Dec17 extends DecBase {


    public Dec17(int year) {
        super(year, 17);
    }

    static class Register {
        long A;
        long B;
        long C;
        int instructionPointer;
        String output;

        public Register() {
            instructionPointer = 0;
            output = "";
        }

        public Register(long a, long b, long c) {
            this();
            A = a;
            B = b;
            C = c;
        }

        String output() {
            return output.isEmpty() ? "" : output.substring(0, output.length() - 1);
        }
    }

    static class Operand {
        int value;
        Register register;

        public Operand(int value, Register register) {
            this.value = value;
            this.register = register;
        }

        long combo() {
            return switch (value) {
                case 0, 1, 2, 3 -> literal();
                case 4 -> register.A;
                case 5 -> register.B;
                case 6 -> register.C;
                case 7 -> throw new IllegalStateException("Combo operand 7 is reserved and will not appear in valid programs.");
                default -> throw new IllegalStateException("Unexpected value: " + value);
            };
        }

        int literal() {
            return switch (value) {
                case 0, 1, 2, 3, 4, 5, 6, 7 -> value;
                default -> throw new IllegalStateException("Unexpected value: " + value);
            };
        }
    }

    enum Instruction {
        adv {
            @Override
            void execute(Register register, Operand combo) {
                long numerator = register.A;
                double denominator = Math.pow(2, combo.combo());
                register.A = (int) (numerator / denominator);
                register.instructionPointer += 2;
            }
        },
        bxl {
            @Override
            void execute(Register register, Operand literal) {
                register.B = register.B ^ literal.literal();
                register.instructionPointer += 2;
            }
        },
        bst {
            @Override
            void execute(Register register, Operand operand) {
                long modulo8 = operand.combo() % 8;
                String result = "00" + Long.toBinaryString(modulo8);
                result = result.substring(result.length() - 3);
                int ans = 0, i, p = 0;
                int len = result.length();
                for (i = len - 1; i >= 0; i--) {
                    if (result.charAt(i) == '1') {
                        ans += (int) Math.pow(2, p);
                    }
                    p++;
                }
                register.B = ans;
                register.instructionPointer += 2;
            }
        },
        jnz {
            @Override
            void execute(Register register, Operand operand) {
                if (register.A != 0) {
                    register.instructionPointer = operand.literal();
                } else {
                    register.instructionPointer += 2;
                }
            }
        },
        bxc {
            @Override
            void execute(Register register, Operand operand) {
                register.B = register.B ^ register.C;
                register.instructionPointer += 2;
            }
        },
        out {
            @Override
            void execute(Register register, Operand operand) {
                register.output += operand.combo() % 8 + ",";
                register.instructionPointer += 2;
            }
        },
        bdv {
            @Override
            void execute(Register register, Operand operand) {
                long numerator = register.A;
                double denominator = Math.pow(2, operand.combo());
                register.B = (int) (numerator / denominator);
                register.instructionPointer += 2;
            }
        },
        cdv {
            @Override
            void execute(Register register, Operand operand) {
                long numerator = register.A;
                double denominator = Math.pow(2, operand.combo());
                register.C = (int) (numerator / denominator);
                register.instructionPointer += 2;
            }
        };

        abstract void execute(Register register, Operand operand);

        static Instruction ins(int opcode) {
            return switch (opcode) {
                case 0 -> adv;
                case 1 -> bxl;
                case 2 -> bst;
                case 3 -> jnz;
                case 4 -> bxc;
                case 5 -> out;
                case 6 -> bdv;
                case 7 -> cdv;
                default -> throw new IllegalStateException("Unexpected value: " + opcode);
            };
        }
    }

    @Override
    public Dec17 readDefaultInput() {
        System.out.println("Reading default input.");
        inputStrings = new LinkedList<>(Stream.of(
                "Register A: 2024",
                "Register B: 0",
                "Register C: 0",

                "Program: 0,3,5,4,3,0"
        ).toList());
        return this;
    }

    @Override
    protected void calculatePart1() {
        Register register = new Register();
        Integer[] program = new Integer[]{};
        for (String input : inputStrings) {
            if (input.startsWith("Register A: ")) {
                register.A = Integer.parseInt(input.replace("Register A: ", ""));
            } else if (input.startsWith("Register B: ")) {
                register.B = Integer.parseInt(input.replace("Register B: ", ""));
            } else if (input.startsWith("Register C: ")) {
                register.C = Integer.parseInt(input.replace("Register C: ", ""));
            } else if (input.startsWith("Program: ")) {
                program = Arrays.stream(input.replace("Program: ", "").split(",")).map(Integer::parseInt).toArray(Integer[]::new);
            }
        }

        do {
            Instruction ins = Instruction.ins(program[register.instructionPointer]);
            Operand operand = new Operand(program[register.instructionPointer + 1], register);
            ins.execute(register, operand);
        } while(register.instructionPointer < program.length);

        System.out.printf("Part 1 - Sum %s%n", register.output());
    }

    @Override
    protected void calculatePart2() {
        //2,0,7,3,0,3,1,3,7
        long initialA = 0;
        long initialB = 0;
        long initialC = 0;
        Integer[] program = new Integer[]{};
        for (String input : inputStrings) {
            if (input.startsWith("Register A: ")) {
                initialA = Long.parseLong(input.replace("Register A: ", ""));
            } else if (input.startsWith("Register B: ")) {
                initialB = Long.parseLong(input.replace("Register B: ", ""));
            } else if (input.startsWith("Register C: ")) {
                initialC = Long.parseLong(input.replace("Register C: ", ""));
            } else if (input.startsWith("Program: ")) {
                program = Arrays.stream(input.replace("Program: ", "").split(",")).map(Integer::parseInt).toArray(Integer[]::new);
            }
        }

        String programValue = Arrays.stream(program).map(String::valueOf).collect(Collectors.joining(","));
        long sum = 0;

//        for (long i = Integer.MAX_VALUE - 1; i >= 0; i--) {
        for (long i = 580000000; i >= 0; i--) {
            Register register = new Register(i, initialB, initialC);
            if (i == initialA) {
                continue;
            }
            try {
                register.A = i;
                do {
                    Instruction ins = Instruction.ins(program[register.instructionPointer]);
                    Operand operand = new Operand(program[register.instructionPointer + 1], register);
                    ins.execute(register, operand);
                } while (register.instructionPointer < program.length);
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
            if (register.output().equals(programValue)) {
                sum = i;
                break;
            }
            if (i % 2_500_000 == 0) System.out.printf("%d%n", i);
        }

        System.out.printf("Part 2 - Sum %d%n", sum);
    }
}