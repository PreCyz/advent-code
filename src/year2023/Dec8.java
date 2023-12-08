package year2023;

import base.DecBase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.stream.Stream;


class Dec8 extends DecBase {

    public Dec8(String fileName) {
        super(fileName);
    }

    @Override
    public Dec8 readDefaultInput() {
        System.out.println("Reading default input.");
        inputStrings = new LinkedList<>(Stream.of(
                        /*"RL",
                                "",
                                "AAA = (BBB, CCC)",
                                "BBB = (DDD, EEE)",
                                "CCC = (ZZZ, GGG)",
                                "DDD = (DDD, DDD)",
                                "EEE = (EEE, EEE)",
                                "GGG = (GGG, GGG)",
                                "ZZZ = (ZZZ, ZZZ)"*/
                        "LR",
                        "",
                        "11A = (11B, XXX)",
                        "11B = (XXX, 11Z)",
                        "11Z = (11B, XXX)",
                        "22A = (22B, XXX)",
                        "22B = (22C, 22C)",
                        "22C = (22Z, 22Z)",
                        "22Z = (22B, 22B)",
                        "XXX = (XXX, XXX)"
                )
                .toList());
        return this;
    }

    @Override
    protected void calculatePart1() {
        Iterator<String> lineIterator = inputStrings.iterator();
        char[] instructions = lineIterator.next().toCharArray();

        Map<String, String> nodesMap = new HashMap<>();
        while (lineIterator.hasNext()) {
            String node = lineIterator.next();
            if (!node.isEmpty()) {
                String key = node.split(" = ")[0];
                nodesMap.put(key, node.split(" = ")[1]
                        .replace(" ", "")
                        .replace("(", "")
                        .replace(")", "")
                );
            }
        }
        String currentNode = "AAA";

        int step = 0;
        int idx = 0;
        while (!"ZZZ".equals(currentNode)) {
            step++;
            String nextNode = nodesMap.get(currentNode);
            char instruction = instructions[idx++];
            currentNode = nextNode.split(",")[instruction == 'L' ? 0 : 1];
            if (idx == instructions.length) {
                idx = 0;
            }
        }

        long sum = step;
        System.out.printf("Part 1 - Total score %d%n", sum);
    }

    @Override
    protected void calculatePart2() {
        Iterator<String> lineIterator = inputStrings.iterator();
        char[] instructions = lineIterator.next().toCharArray();

        Map<String, String> nodesMap = new HashMap<>();
        while (lineIterator.hasNext()) {
            String node = lineIterator.next();
            if (!node.isEmpty()) {
                String key = node.split(" = ")[0];
                nodesMap.put(key, node.split(" = ")[1]
                        .replace(" ", "")
                        .replace("(", "")
                        .replace(")", "")
                );
            }
        }
        ArrayList<String> currentNodes = new ArrayList<>(nodesMap.keySet()
                .stream()
                .filter(k -> k.endsWith("A"))
                .toList()
        );

        ArrayList<Integer> steps = new ArrayList<>(currentNodes.size());

        for (String currentNode : currentNodes) {
            int step = 0;
            int idx = 0;
            while (!currentNode.endsWith("Z")) {
                step++;
                int instr = instructions[idx] == 'L' ? 0 : 1;
                String nextNode = nodesMap.get(currentNode);
                currentNode = nextNode.split(",")[instr];
                idx++;
                if (idx == instructions.length) {
                    idx = 0;
                }
            }
            steps.add(step);
        }
        System.out.println(steps);

        int lcm = lowestCommonMultiple(steps);

        long sum = 1;
        for (Integer step : steps) {
            sum *= step / lcm;
        }
        sum *= lcm;
        System.out.printf("Part 2 - Total score %d%n", sum);
    }

    private int lowestCommonMultiple(ArrayList<Integer> numbers) {
        int reminder = -1;
        int lcm = 1;
        while (reminder != 0) {
            reminder = 0;
            lcm++;
            for (Integer number : numbers) {
                reminder += number % lcm;
            }
        }
        return lcm;
    }

}
