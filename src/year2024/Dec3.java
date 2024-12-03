package year2024;

import base.DecBase;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

class Dec3 extends DecBase {

    public Dec3(int year) {
        super(year, 3);
    }

    @Override
    public Dec3 readDefaultInput() {
        System.out.println("Reading default input.");
        inputStrings = new LinkedList<>(Stream.of(
//                "xmul(2,4)%&mul[3,7]!@^do_not_mul(5,5)+mul(32,64]then(mul(11,8)mul(8,5))",
                "xmul(2,4)&mul[3,7]!^don't()_mul(5,5)+mul(32,64](mul(11,8)undo()?mul(8,5))"
        ).toList());
        return this;
    }


    @Override
    protected void calculatePart1() {
        String regex = "mul\\(\\d{1,3},\\d{1,3}\\)";
        ArrayList<String> mulls = new ArrayList<>();
        Pattern pattern = Pattern.compile(regex);
        for (String input : inputStrings) {
            Matcher matcher = pattern.matcher(input);
            while (matcher.find()) {
                mulls.add(matcher.group());
            }
        }

        String mullRegex = "\\d{1,3}";
        pattern = Pattern.compile(mullRegex);
        long sum = 0;
        for (String mull : mulls) {
            Matcher matcher = pattern.matcher(mull);
            long multiply = 1;
            while (matcher.find()) {
                multiply *= Integer.parseInt(matcher.group());
            }
            sum += multiply;
        }

        System.out.printf("Part 1 - Sum %d%n", sum);
    }

    @Override
    protected void calculatePart2() {
        String regex = "(mul\\(\\d{1,3},\\d{1,3}\\))|(don't\\(\\))|(do\\(\\))";
        ArrayList<String> instructions = new ArrayList<>();
        Pattern pattern = Pattern.compile(regex);
        for (String input : inputStrings) {
            Matcher matcher = pattern.matcher(input);
            while (matcher.find()) {
                instructions.add(matcher.group());
            }
        }

        String mullRegex = "\\d{1,3}";
        Pattern mullPattern = Pattern.compile(mullRegex);
        boolean isDo = true;
        long sum = 0;
        for (String instruction : instructions) {
            if ("don't()".equals(instruction)) {
                isDo = false;
            } else if ("do()".equals(instruction)) {
                isDo = true;
            } else if (isDo) {
                Matcher matcher = mullPattern.matcher(instruction);
                long multiply = 1;
                while (matcher.find()) {
                    multiply *= Integer.parseInt(matcher.group());
                }
                sum += multiply;
            }
        }

        System.out.printf("Part 2 - Sum %d%n", sum);
    }
}