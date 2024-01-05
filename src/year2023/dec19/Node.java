package year2023.dec19;

import java.util.ArrayList;

public class Node {
    public Condition condition;
    public String name;

    public final ArrayList<Condition> conditionsSoFar = new ArrayList<>();
    public Node trueNode;
    public Node falseNode;

    public Node(String name, Condition condition) {
        this.condition = condition;
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    boolean isA() {
        return "A".equals(name) && condition == null;
    }

    boolean isR() {
        return "R".equals(name) && condition == null;
    }
}
