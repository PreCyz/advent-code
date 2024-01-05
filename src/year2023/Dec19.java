package year2023;

import base.DecBase;
import utils.Utils;
import year2023.dec19.Node;
import year2023.dec19.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.stream.*;

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
                "hdj{m>838:A,pv}"/*,
                "\n",
                "{x=787,m=2655,a=1222,s=2876}",
                "{x=1679,m=44,a=2067,s=496}",
                "{x=2036,m=264,a=79,s=2244}",
                "{x=2461,m=1339,a=466,s=291}",
                "{x=2127,m=1623,a=2188,s=1013}"*/
                //167409079868000 distinct combinations of ratings that will be accepted
        ).toList());
        return this;
    }

    @Override
    protected void calculatePart1() {
        Map<String, Workflow> workflowMap = new HashMap<>();
        ArrayList<Part> parts = new ArrayList<>();
        for (String line : inputStrings) {
            if (line.startsWith("{")) {
                parts.add(createPart(line));
            } else if (!line.isEmpty() && !line.equals("\n")) {
                Workflow workflow = createWorkflow(line);
                workflowMap.put(workflow.name, workflow);
            }
        }

        ArrayList<Part> acceptedParts = new ArrayList<>();
        for (Part part : parts) {
            boolean isAccepted = isPartApproved(part, workflowMap.get("in"), workflowMap);
            if (isAccepted) {
                acceptedParts.add(part);
            }
        }

        long sum = acceptedParts.stream().mapToLong(it -> it.x + it.m + it.a + it.s).sum();
        System.out.printf("Part 1 - Total score %d%n", sum);
    }

    private boolean isPartApproved(Part part, Workflow workflow, Map<String, Workflow> workflowMap) {
        ArrayList<Condition> conditions = workflow.conditions;
        for (Condition condition : conditions) {
            Condition result = executeCondition(part, condition, workflowMap);
            if (result != null) {
                if (result.isAccepted()) {
                    return true;
                } else if (result.isRejected()) {
                    return false;
                } else {
                    return isPartApproved(part, workflowMap.get(result.nextWorkflowName), workflowMap);
                }
            }
        }
        return false;
    }

    Condition executeCondition(Part part, Condition condition, Map<String, Workflow> workflowMap) {
        if (condition == null) {
            return null;
        }
        if (condition.isTerminator()) {
            return condition;
        }
        if (condition.relation == '>') {
            switch (condition.part) {
                case 'x' -> {
                    return executeCondition(part, getConditionGt(part.x, condition), workflowMap);
                }
                case 'm' -> {
                    return executeCondition(part, getConditionGt(part.m, condition), workflowMap);
                }
                case 'a' -> {
                    return executeCondition(part, getConditionGt(part.a, condition), workflowMap);
                }
                case 's' -> {
                    return executeCondition(part, getConditionGt(part.s, condition), workflowMap);
                }
            }
        } else if (condition.relation == '<') {
            switch (condition.part) {
                case 'x' -> {
                    return executeCondition(part, getConditionLt(part.x, condition), workflowMap);
                }
                case 'm' -> {
                    return executeCondition(part, getConditionLt(part.m, condition), workflowMap);
                }
                case 'a' -> {
                    return executeCondition(part, getConditionLt(part.a, condition), workflowMap);
                }
                case 's' -> {
                    return executeCondition(part, getConditionLt(part.s, condition), workflowMap);
                }
            }
        } else {
            return new Condition(condition.nextWorkflowName);
        }
        return null;
    }

    private Condition getConditionGt(int rating, Condition condition) {
        if (rating > condition.intValue) {
            if ("A".equals(condition.nextWorkflowName)) {
                return new Condition("A");
            } else if ("R".equals(condition.nextWorkflowName)) {
                return new Condition("R");
            }
            return new Condition(condition.nextWorkflowName);
        } else {
            return null;
        }
    }

    private Condition getConditionLt(int rating, Condition condition) {
        if (rating < condition.intValue) {
            if ("A".equals(condition.nextWorkflowName)) {
                return new Condition("A");
            } else if ("R".equals(condition.nextWorkflowName)) {
                return new Condition("R");
            }
            return new Condition(condition.nextWorkflowName);
        } else {
            return null;
        }
    }

    private Part createPart(String line) {
        String[] split = line.substring(1).replace("}", "").split(",");
        int x = -1, m = -1, a = -1, s = -1;
        for (String p : split) {
            if (p.contains("x")) {
                x = Integer.parseInt(p.split("=")[1]);
            } else if (p.contains("m")) {
                m = Integer.parseInt(p.split("=")[1]);
            } else if (p.contains("a")) {
                a = Integer.parseInt(p.split("=")[1]);
            } else if (p.contains("s")) {
                s = Integer.parseInt(p.split("=")[1]);
            }
        }
        return new Part(x, m, a, s);
    }

    Workflow createWorkflow(String line) {
        String workflowName = line.split("\\{")[0];
        String conditions = line.split("\\{")[1].replace("}", "");
        String[] instructions = conditions.split(",");

        Workflow workflow = new Workflow(workflowName);

        for (String ins : instructions) {
            if (ins.contains(">") || ins.contains("<")) {
                char part = ins.charAt(0);
                char relation = ins.charAt(1);
                String[] split = ins.substring(2).split(":");
                int intValue = Integer.parseInt(split[0]);
                String nextFlowName = split[1];
                workflow.conditions.add(new Condition(part, relation, intValue, nextFlowName, workflowName));
            } else {
                workflow.conditions.add(new Condition(ins));
            }
        }
        return workflow;
    }

    @Override
    protected void calculatePart2() {
        Map<String, Workflow> workflowMap = new HashMap<>();
        for (String line : inputStrings) {
            if (line.isEmpty()) {
                break;
            }
            Workflow workflow = createWorkflow(line);
            workflowMap.put(workflow.name, workflow);
        }

        Node root = buildTree(workflowMap);

        result = new LinkedList<>();
        result.add(new ArrayList<>());
        findAllA(root);
        result.removeLast();

        Utils.writeToFile(result);

        long sum = 0;
        long sumR = 0;
        for (ArrayList<Condition> conditions : result) {
//            System.out.println(conditions.stream().map(Condition::name).collect(Collectors.joining(" ^ ")));
            long partSum = 1;

            long x = findPartCount('x', conditions);
//            System.out.printf("X=%d", x);
            Set<Integer> range = findRangeByPartName('x', conditions);
//            System.out.printf("[%d] ", range.size());
            partSum *= range.size();

            long m = findPartCount('m', conditions);
//            System.out.printf("M=%d", m);
            range = findRangeByPartName('m', conditions);
//            System.out.printf("[%d] ", range.size());
            partSum *= range.size();

            long s = findPartCount('s', conditions);
//            System.out.printf("S=%d", s);
            range = findRangeByPartName('s', conditions);
//            System.out.printf("[%d] ", range.size());
            partSum *= range.size();

            long a = findPartCount('a', conditions);
//            System.out.printf("A=%d", a);
            range = findRangeByPartName('a', conditions);
//            System.out.printf("[%d]%n", range.size());
            partSum *= range.size();

//            System.out.printf("X*M*S*A= %d [range]  combinations%n", partSum);
            sumR+=partSum;

            sum+= x * m * s * a;
//            System.out.printf("X*M*S*A= %d distinct combinations%n%n", partSum);
        }

        System.out.printf("Part 2 - Total score %d%n", sum);
        System.out.printf("Part 2 - Total score %d[R]%n", sumR);
    }

    private Node buildTree(Map<String, Workflow> workflowMap) {
        Condition startCondition = getNextCondition("in", workflowMap);
        Node root = new Node(startCondition.name(), startCondition);

        Condition secondCondition = getNextCondition(startCondition.nextWorkflowName, workflowMap);
        root.trueNode = addRecursive(root.trueNode, secondCondition, workflowMap);

        Condition in = getNextCondition("in", workflowMap);
        if (in.isOnlyNextWorkflowName()) {
            in = getNextCondition(in.nextWorkflowName, workflowMap);
        }
        root.falseNode = addRecursive(root.falseNode, in, workflowMap);

        return root;
    }

    Condition getNextCondition(String workflowName, Map<String, Workflow> workflowMap) {
        if ("A".equals(workflowName) || "R".equals(workflowName)) {
            return new Condition(workflowName);
        }
        Workflow workflow = workflowMap.get(workflowName);
        if (workflow == null || workflow.conditions.isEmpty()) {
            return null;
        }
        return workflow.conditions.removeFirst();
    }

    private LinkedList<ArrayList<Condition>> result;

    private Node addRecursive(Node current, Condition condition, Map<String, Workflow> workflowMap) {
        if (condition == null) {
            return current;
        }
        if (current == null) {
            if (condition.isTerminator()) {
                return new Node(condition.nextWorkflowName, null);
            }
            Node node = new Node(condition.name(), condition);
            node.trueNode = addRecursive(node.trueNode, getNextCondition(condition.nextWorkflowName, workflowMap), workflowMap);

            Condition removed = getNextCondition(condition.currentWorkflowName, workflowMap);
            if (removed == null) {
                removed = getNextCondition(condition.currentWorkflowName, workflowMap);
            } else if (removed.isOnlyNextWorkflowName()) {
                removed = getNextCondition(removed.nextWorkflowName, workflowMap);
            }
            node.falseNode = addRecursive(node.falseNode, removed, workflowMap);
            return node;
        }
        return current;
    }

    private void findAllA(Node node) {
        if (node.condition == null) {
            if ("A".equals(node.name)) {
                result.removeLast();
                result.addLast(new ArrayList<>(node.conditionsSoFar));
                result.addLast(new ArrayList<>());
            }
            return;
        }

        node.conditionsSoFar.add(node.condition);

        if (node.trueNode != null) {
            node.trueNode.conditionsSoFar.addAll(node.conditionsSoFar);
            findAllA(node.trueNode);
        }
        if (node.falseNode != null) {
            Condition negate = new Condition(node.condition);
            negate.negate();
            node.falseNode.conditionsSoFar.addAll(node.conditionsSoFar);
            node.falseNode.conditionsSoFar.set(node.conditionsSoFar.size() - 1, negate);
            findAllA(node.falseNode);
        }
    }

    private int findPartCount(char partName, ArrayList<Condition> conditions) {
        final int MAX_VALUE = 4000;
        List<Condition> conditionsByPart = conditions.stream().filter(it -> it.part == partName).toList();
        if (conditionsByPart.isEmpty()) {
            return MAX_VALUE;
        } else {
            switch (RelationType.valueOf(conditionsByPart)) {
                case GT -> {
                    return MAX_VALUE - conditionsByPart.stream().mapToInt(it -> it.intValue).max().getAsInt();
                }
                case LT -> {
                    return conditionsByPart.stream().mapToInt(it -> it.intValue).min().getAsInt() - 1;
                }
                case COMMON -> {
                    int ltMin = conditionsByPart.stream().filter(it -> it.relation == '<')
                            .mapToInt(it -> it.intValue)
                            .min()
                            .getAsInt();
                    int gtMax = conditionsByPart.stream()
                            .filter(it -> it.relation == '>')
                            .mapToInt(it -> it.intValue)
                            .max()
                            .getAsInt();
                    return ltMin - gtMax - 1;
                }
                default -> {
                    return 0;
                }
            }
        }
    }

    private Set<Integer> findRangeByPartName(char partName, ArrayList<Condition> conditions) {
        final int MAX_VALUE = 4000;
        List<Condition> conditionsByPart = conditions.stream().filter(it -> it.part == partName).toList();
        if (conditionsByPart.isEmpty()) {
            return IntStream.range(1, MAX_VALUE + 1).boxed().collect(Collectors.toCollection(HashSet::new));
        } else {
            switch (RelationType.valueOf(conditionsByPart)) {
                case GT -> {
                    int rangeStart = conditionsByPart.stream().mapToInt(it -> it.intValue).max().getAsInt() + 1;
                    return IntStream.range(rangeStart, MAX_VALUE + 1).boxed().collect(Collectors.toCollection(HashSet::new));
                }
                case LT -> {
                    int rangeEnd = conditionsByPart.stream().mapToInt(it -> it.intValue).min().getAsInt();
                    return IntStream.range(1, rangeEnd).boxed().collect(Collectors.toCollection(HashSet::new));
                }
                case COMMON -> {
                    int ltMin = conditionsByPart.stream().filter(it -> it.relation == '<')
                            .mapToInt(it -> it.intValue)
                            .min()
                            .getAsInt();
                    int gtMax = conditionsByPart.stream()
                            .filter(it -> it.relation == '>')
                            .mapToInt(it -> it.intValue)
                            .max()
                            .getAsInt() + 1;
                    return IntStream.range(gtMax, ltMin).boxed().collect(Collectors.toCollection(HashSet::new));
                }
                default -> {
                    return new HashSet<>();
                }
            }
        }
    }

}
