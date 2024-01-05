package year2023.dec19;

import java.util.ArrayList;

public class Workflow {

    public String name;
    public ArrayList<Condition> conditions;

    public Workflow(String name) {
        this.name = name;
        conditions = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "Workflow{" +
                "name='" + name + '\'' +
                '}';
    }
}
