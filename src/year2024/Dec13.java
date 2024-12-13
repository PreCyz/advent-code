package year2024;

import base.DecBase;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

class Dec13 extends DecBase {

    public Dec13(int year) {
        super(year, 13);
    }

    @Override
    public Dec13 readDefaultInput() {
        System.out.println("Reading default input.");
        inputStrings = new LinkedList<>(Stream.of(
                "Button A: X+94, Y+34",
                "Button B: X+22, Y+67",
                "Prize: X=8400, Y=5400",

                "Button A: X+26, Y+66",
                "Button B: X+67, Y+21",
                "Prize: X=12748, Y=12176",

                "Button A: X+17, Y+86",
                "Button B: X+84, Y+37",
                "Prize: X=7870, Y=6450",

                "Button A: X+69, Y+23",
                "Button B: X+27, Y+71",
                "Prize: X=18641, Y=10279"
        ).toList());
        return this;
    }

    record Machine(int xA, int yA, int xB, int yB, int prizeX, int prizeY) {
    }

    @Override
    protected void calculatePart1() {
        ArrayList<Machine> machines = new ArrayList<>(inputStrings.size());
        Iterator<String> iterator = inputStrings.iterator();
        while (iterator.hasNext()) {
            String line = iterator.next();
            int xA = -1, yA = -1, xB = -1, yB = -1, prizeX = -1, prizeY = -1;
            if (line.startsWith("Button A: ")) {
                String[] split = line.replace("Button A: ", "").split(",");
                xA = Integer.parseInt(split[0].replace("X+", ""));
                yA = Integer.parseInt(split[1].replace(" Y+", ""));
                line = iterator.next();
            }
            if (line.startsWith("Button B: ")) {
                String[] split = line.replace("Button B: ", "").split(",");
                xB = Integer.parseInt(split[0].replace("X+", ""));
                yB = Integer.parseInt(split[1].replace(" Y+", ""));
                line = iterator.next();
            }
            if (line.startsWith("Prize: ")) {
                String[] split = line.replace("Prize: ", "").split(",");
                prizeX = Integer.parseInt(split[0].replace("X=", ""));
                prizeY = Integer.parseInt(split[1].replace(" Y=", ""));
            }
            machines.add(new Machine(xA, yA, xB, yB, prizeX, prizeY));
        }
        machines.trimToSize();

        long sum = 0;
        for (Machine machine : machines) {
            sum += getLowestMachineScore(machine, 200);
        }

        System.out.printf("Part 1 - Sum %d%n", sum);
    }

    record Result(int a, int b) { }

    private long getLowestMachineScore(Machine machine, int rounds) {
        List<Result> xResults = new ArrayList<>();
        for (int a = 1; a <= rounds; a++) {
            //a * machine.xA + b * machine.xB == machine.prizeX
            double b = (machine.prizeX - a * machine.xA)/ machine.xB;

            if (a * machine.yA + b * machine.yB == machine.prizeY) {
                xResults.add(new Result(a, Double.valueOf(b).intValue()));
            }
        }
        if (xResults.isEmpty()) {
            return 0;
        }
        return xResults.stream().mapToInt(r -> r.a * 3 + r.b).min().getAsInt();
    }

    record MachineLong(int xA, int yA, int xB, int yB, long prizeX, long prizeY) { }

    @Override
    protected void calculatePart2() {
        ArrayList<MachineLong> machines = new ArrayList<>(inputStrings.size());
        Iterator<String> iterator = inputStrings.iterator();
        while (iterator.hasNext()) {
            String line = iterator.next();
            int xA = -1, yA = -1, xB = -1, yB = -1;
            long prizeX = -1, prizeY = -1;
            if (line.startsWith("Button A: ")) {
                String[] split = line.replace("Button A: ", "").split(",");
                xA = Integer.parseInt(split[0].replace("X+", ""));
                yA = Integer.parseInt(split[1].replace(" Y+", ""));
                line = iterator.next();
            }
            if (line.startsWith("Button B: ")) {
                String[] split = line.replace("Button B: ", "").split(",");
                xB = Integer.parseInt(split[0].replace("X+", ""));
                yB = Integer.parseInt(split[1].replace(" Y+", ""));
                line = iterator.next();
            }
            if (line.startsWith("Prize: ")) {
                String[] split = line.replace("Prize: ", "").split(",");
                prizeX = Long.parseLong(split[0].replace("X=", "")) + 10_000_000_000_000L;
                prizeY = Long.parseLong(split[1].replace(" Y=", "")) + 10_000_000_000_000L;
            }
            machines.add(new MachineLong(xA, yA, xB, yB, prizeX, prizeY));
        }
        machines.trimToSize();

        long sum = 0;
        for (MachineLong machine : machines) {
            //sum += getLowestMachineScore2(machine, 10_000_000);
            sum += getLowestMachineScore2(machine, 1);
        }

        System.out.printf("Part 2 - Sum %d%n", sum);
    }

    private long getLowestMachineScore2(MachineLong machine, int rounds) {
        List<Result> xResults = new ArrayList<>();
        for (int a = 1; a <= rounds; a++) {
            //a * machine.xA + b * machine.xB == machine.prizeX
            double b = (machine.prizeX - a * machine.xA)/ machine.xB;

            if (a * machine.yA + b * machine.yB == machine.prizeY) {
                xResults.add(new Result(a, Double.valueOf(b).intValue()));
            }
        }
        if (xResults.isEmpty()) {
            return 0;
        }
        return xResults.stream().mapToInt(r -> r.a * 3 + r.b).min().getAsInt();
    }

}