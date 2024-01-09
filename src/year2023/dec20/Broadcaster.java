package year2023.dec20;

import java.util.*;

public class Broadcaster extends Module {

    public Broadcaster(List<Module> destinationModules) {
        super("broadcaster");
        this.destinationModules = new ArrayList<>(destinationModules);
    }

    public Broadcaster() {
        this(Collections.emptyList());
    }

    @Override
    public List<Module> sendPulse() {
        Pulse receivedPulse = super.updates.removeFirst();
        for (Module it : destinationModules) {
            it.update(receivedPulse, this);
        }
        return new ArrayList<>(destinationModules);
    }
}
