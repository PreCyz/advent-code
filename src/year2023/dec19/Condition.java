package year2023.dec19;

public class Condition {
    public char part;
    public char relation;
    public int intValue;
    boolean negate;
    public String nextWorkflowName;
    public String currentWorkflowName;

    public Condition(String nextWorkflowName) {
        this.nextWorkflowName = nextWorkflowName;
    }

    public Condition(Condition condition) {
        this(condition.part, condition.relation, condition.intValue, condition.nextWorkflowName, condition.currentWorkflowName);
    }

    public Condition(char part, char relation, int intValue, String nextWorkflowName, String currentWorkflowName) {
        this.part = part;
        this.relation = relation;
        this.intValue = intValue;
        this.nextWorkflowName = nextWorkflowName;
        this.currentWorkflowName = currentWorkflowName;
    }

    public boolean isTerminator() {
        return isAccepted() || isRejected();
    }

    public boolean isAccepted() {
        return intValue == 0 && "A".equals(nextWorkflowName);
    }

    public boolean isRejected() {
        return intValue == 0 && "R".equals(nextWorkflowName);
    }

    public void negate() {
        negate = true;
        if (relation == '>') {
            relation = '<';
            intValue++;
        } else {
            relation = '>';
            intValue--;
        }
    }

    @Override
    public String toString() {
        return "Condition{" +
                part + relation + intValue +
                ", negate=" + negate +
                ", currentWorkflowName='" + currentWorkflowName + '\'' +
                ", nextWorkflowName='" + nextWorkflowName + '\'' +
                '}';
    }

    public String name() {
        return "" + part + relation + intValue;
    }

    public boolean isOnlyNextWorkflowName() {
        return !isTerminator() && intValue == 0 && !nextWorkflowName.isEmpty() && currentWorkflowName == null;
    }
}
