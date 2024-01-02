package year2023;

import base.DecBase;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.stream.Stream;

public class Dec19 extends DecBase {

    public Dec19(int year) {
        super(year, 19);
    }

    public DecBase readInput() throws IOException {
        System.out.printf("%nReading input from [%s]%n", getFilePath());
        inputStrings = new LinkedList<>();
        try (Scanner scanner = new Scanner(new FileInputStream(getFilePath().toFile()))) {
            while (scanner.hasNext()) {
                final String nextLine = scanner.nextLine();
                if (nextLine != null) {
                    inputStrings.add(nextLine);
                }
            }
        }
        return this;
    }

    @Override
    public Dec19 readDefaultInput() {
        System.out.println("Reading default input.");
        inputStrings = new LinkedList<>(Stream.of(
                "px{a<2006:qkq,m>2090:A,rfg}",
                        "pv{a>1716:R,A}",
                        "lnx{m>1548:A,A}",
                        "rfg{s<537:gd,x>2440:R,A}",
                        "qs{s>3448:A,lnx}",
                        "qkq{x<1416:A,crn}",
                        "crn{x>2662:A,R}",
                        "in{s<1351:px,qqz}",
                        "qqz{s>2770:qs,m<1801:hdj,R}",
                        "gd{a>3333:R,R}",
                        "hdj{m>838:A,pv}",
                        "\n",
                        "{x=787,m=2655,a=1222,s=2876}",
                        "{x=1679,m=44,a=2067,s=496}",
                        "{x=2036,m=264,a=79,s=2244}",
                        "{x=2461,m=1339,a=466,s=291}",
                        "{x=2127,m=1623,a=2188,s=1013}"
        ).toList());
        return this;
    }

    @Override
    protected void calculatePart1() {
        long sum = 0;

        System.out.printf("Part 1 - Total score %d%n", sum);
    }

    @Override
    protected void calculatePart2() {
        long sum = 0;
        System.out.printf("Part 2 - Total score %d%n", sum);
    }
}
