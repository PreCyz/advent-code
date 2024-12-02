package year2024;

import base.DecBase;

import java.util.*;
import java.util.stream.Stream;

class Dec2 extends DecBase {

    public Dec2(int year) {
        super(year, 2);
    }

    @Override
    public Dec2 readDefaultInput() {
        System.out.println("Reading default input.");
        inputStrings = new LinkedList<>(Stream.of(
                "7 6 4 2 1",
                "1 2 7 8 9",
                "9 7 6 2 1",
                "1 3 2 4 5",
                "8 6 4 4 1",
                "1 3 6 7 9"
        ).toList());
        return this;
    }


    @Override

    protected void calculatePart1() {
        LinkedHashMap<Integer, List<Integer>> gradualReports = new LinkedHashMap<>();
        int idx = 0;
        for (String report : inputStrings) {
            List<Integer> reports = Arrays.stream(report.split(" ")).map(Integer::parseInt).toList();
            if ((isAllIncreasing(reports) || isAllDecreasing(reports)) && isSafe(reports)) {
                gradualReports.put(idx++, reports);
            }
        }
        System.out.printf("Part 1 - Sum %d%n", gradualReports.size());
    }

    private boolean isAllIncreasing(List<Integer> reports) {
        for (int i = 0, size = reports.size() - 1; i < size; i++) {
            int first = reports.get(i);
            int second = reports.get(i + 1);
            if (first >= second) {
                return false;
            }
        }
        return true;
    }

    private boolean isAllDecreasing(List<Integer> reports) {
        for (int i = 0, size = reports.size() - 1; i < size; i++) {
            int first = reports.get(i);
            int second = reports.get(i + 1);
            if (first <= second) {
                return false;
            }
        }
        return true;
    }

    private boolean isSafe(List<Integer> reports) {
        for (int i = 0, size = reports.size() - 1; i < size; i++) {
            int dif = Math.abs(reports.get(i) - reports.get(i + 1));
            if (dif < 1 || dif > 3) {
                return false;
            }
        }
        return true;
    }

    @Override
    protected void calculatePart2() {
        LinkedHashMap<Integer, List<Integer>> gradualReports = new LinkedHashMap<>();
        int idx = 0;
        for (String report : inputStrings) {
            List<Integer> reports = Arrays.stream(report.split(" ")).map(Integer::parseInt).toList();
            if ((isAllIncreasing(reports) || isAllDecreasing(reports)) && isSafe(reports)) {
                gradualReports.put(++idx, reports);
            } else {
                for (int i = 0; i < reports.size(); i++) {
                    List<Integer> tolerated = new ArrayList<>(reports);
                    tolerated.remove(i);
                    if ((isAllIncreasing(tolerated) || isAllDecreasing(tolerated)) && isSafe(tolerated)) {
                        gradualReports.put(++idx, tolerated);
                        break;
                    }
                }
            }
        }
        System.out.printf("Part 2 - Sum %d%n", gradualReports.size());
    }
}