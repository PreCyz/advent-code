package year2024;

import base.DecBase;

import java.util.LinkedList;
import java.util.stream.Stream;

class Dec17 extends DecBase {

    static String OUTPUT = "";

    public Dec17(int year) {
        super(year, 17);
    }

    static class Register {
        String A;
        String B;
        String C;
        int instructionPointer;
        int programLength;
    }

    static class ComboOperand {
        int value;
        Register register;

        public ComboOperand(int value, Register register) {
            this.value = value;
            this.register = register;
        }

        String getOperand() {
            return switch (value) {
                case 0, 1, 2, 3 -> String.valueOf(value);
                case 4 -> register.A;
                case 5 -> register.B;
                case 6 -> register.C;
                case 7 -> String.valueOf(7);
                default -> throw new IllegalStateException("Unexpected value: " + value);
            };
        }
    }

    enum Instruction {
        adv {
            @Override
            void execute(Register register, ComboOperand combo) {
                int A = Integer.parseInt(register.A);
                int o = Integer.parseInt(combo.getOperand());
                double v = A / Math.pow(2, o);
                register.A = Double.valueOf(v).intValue() + "";
                register.instructionPointer += 2;
            }
        },
        bxl {
            @Override
            void execute(Register register, ComboOperand literal) {
                register.B = String.valueOf(Integer.parseInt(register.B) ^ Integer.parseInt(literal.getOperand()));
                register.instructionPointer += 2;
            }
        },
        bst {
            @Override
            void execute(Register register, ComboOperand comboOperand) {
                String result = "00" + Integer.toBinaryString(Integer.parseInt(comboOperand.getOperand()) % 8);
                result = result.substring(result.length() - 3);
                int ans = 0, i, p = 0;
                int len = result.length();
                for (i = len - 1; i >= 0; i--) {
                    if (result.charAt(i) == '1') {
                        ans += (int) Math.pow(2, p);
                    }
                    p++;
                }
                register.B = String.valueOf(ans);
                register.instructionPointer += 2;
            }
        },
        jnz {
            @Override
            void execute(Register register, ComboOperand comboOperand) {
                if (!"0".equals(register.A)) {
                    register.instructionPointer = Integer.parseInt(comboOperand.getOperand());
                } else {
                    register.instructionPointer += 2;
                }
            }
        },
        bxc {
            @Override
            void execute(Register register, ComboOperand comboOperand) {
                register.B = String.valueOf(Integer.parseInt(register.B) ^ Integer.parseInt(register.C));
                register.instructionPointer += 2;
            }
        },
        out {
            @Override
            void execute(Register register, ComboOperand comboOperand) {
                OUTPUT += Integer.parseInt(comboOperand.getOperand()) % 8 + ",";
                register.instructionPointer += 2;
            }
        },
        bdv {
            @Override
            void execute(Register register, ComboOperand comboOperand) {
                int A = Integer.parseInt(register.A);
                int o = Integer.parseInt(comboOperand.getOperand());
                double v = A / Math.pow(2, o);
                register.B = Double.valueOf(v).intValue() + "";
                register.instructionPointer += 2;
            }
        },
        cdv {
            @Override
            void execute(Register register, ComboOperand comboOperand) {
                int A = Integer.parseInt(register.A);
                int o = Integer.parseInt(comboOperand.getOperand());
                double v = A / Math.pow(2, o);
                register.C = Double.valueOf(v).intValue() + "";
                register.instructionPointer += 2;
            }
        };

        abstract void execute(Register register, ComboOperand operand);

        static Instruction ins(String opcode) {
            return switch (opcode) {
                case "0" -> adv;
                case "1" -> bxl;
                case "2" -> bst;
                case "3" -> jnz;
                case "4" -> bxc;
                case "5" -> out;
                case "6" -> bdv;
                case "7" -> cdv;
                default -> throw new IllegalStateException("Unexpected value: " + opcode);
            };
        }
    }

    @Override
    public Dec17 readDefaultInput() {
        System.out.println("Reading default input.");
        inputStrings = new LinkedList<>(Stream.of(
                "Register A: 729",
                "Register B: 0",
                "Register C: 0",

                "Program: 0,1,5,4,3,0"
        ).toList());
        return this;
    }

    @Override
    protected void calculatePart1() {
        OUTPUT = "";
        Register register = new Register();
        String[] program = new String[]{};
        register.instructionPointer = 0;
        for (String input : inputStrings) {
            if (input.startsWith("Register A: ")) {
                register.A = input.replace("Register A: ", "");
            } else if (input.startsWith("Register B: ")) {
                register.B = input.replace("Register B: ", "");
            } else if (input.startsWith("Register C: ")) {
                register.C = input.replace("Register C: ", "");
            } else if (input.startsWith("Program: ")) {
                program = input.replace("Program: ", "").split(",");
                register.programLength = program.length;
            }
        }

        do {
            Instruction ins = Instruction.ins(program[register.instructionPointer]);
            ComboOperand operand = new ComboOperand(Integer.parseInt(program[register.instructionPointer + 1]), register);
            ins.execute(register, operand);
        } while(register.instructionPointer < program.length);

        System.out.printf("Part 1 - Sum %s%n", OUTPUT.substring(0, OUTPUT.length() - 1));
    }

    @Override
    protected void calculatePart2() {

//        System.out.printf("Part 2 - Sum[%b] %d%n", move, move);
    }
}