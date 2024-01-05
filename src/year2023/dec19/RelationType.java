package year2023.dec19;

import java.util.LinkedList;
import java.util.List;

public enum RelationType {
    GT, LT, COMMON, NONE;

    public static RelationType valueOf(List<Condition> conditions) {
        LinkedList<Character> relations = new LinkedList<>(conditions.stream().map(it -> it.relation).toList());
        long count = relations.stream().distinct().count();
        if (count == 1) {
            if (relations.getFirst() == '<') {
                return LT;
            } else {
                return GT;
            }
        } else if (relations.contains('<') && relations.contains('>')) {
            int ltMin = conditions.stream().filter(it -> it.relation == '<').mapToInt(it -> it.intValue).min().getAsInt();
            int gtMax = conditions.stream().filter(it -> it.relation == '>').mapToInt(it -> it.intValue).max().getAsInt();
            if (ltMin >= gtMax) {
                return COMMON;
            }
        }
        return NONE;
    }
}
