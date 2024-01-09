package year2023.dec20;

import java.util.*;

public class FlipFlop extends Module {
    private boolean switchOn;

    public FlipFlop(String id) {
        this(id, Collections.emptyList());
    }

    public FlipFlop(String id, List<Module> destinationModules) {
        super(id);
        this.switchOn = false;
        this.destinationModules = destinationModules;
    }

    @Override
    public List<Module> sendPulse() {
        if (super.updates.isEmpty()) {
            return Collections.emptyList();
        }

        Pulse update = super.updates.removeFirst();
        if (update == Pulse.LOW) {
            switchOn = !switchOn;
            if (switchOn) {
                destinationModules.forEach(it -> it.update(Pulse.HIGH, this));
            } else {
                destinationModules.forEach(it -> it.update(Pulse.LOW, this));
            }
            return new ArrayList<>(destinationModules);
        }
        return Collections.emptyList();
    }

}
