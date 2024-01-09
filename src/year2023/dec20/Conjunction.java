package year2023.dec20;

import java.util.*;

public class Conjunction extends Module {
    private final Map<String, Pulse> inputModuleMap;
    private final LinkedList<InternalStateUpdates> internalModuleUpdates = new LinkedList<>();

    private record InternalStateUpdates(String sourceId, Pulse pulse, Module destination) {}

    public Conjunction(String id, List<Module> destinationModules) {
        super(id);
        this.destinationModules = new ArrayList<>(destinationModules);
        inputModuleMap = new HashMap<>();
    }

    public Conjunction(String id) {
        this(id, new ArrayList<>());
    }

    @Override
    public void updateInput(String id) {
        inputModuleMap.put(id, Pulse.LOW);
    }

    @Override
    public void update(Pulse pulse, Module source) {
        super.update(pulse, source);
        internalModuleUpdates.add(new InternalStateUpdates(source.id(), pulse, this));
    }

    @Override
    public List<Module> sendPulse() {
        if (internalModuleUpdates.isEmpty()) {
            return Collections.emptyList();
        }
        super.updates.removeFirst();
        InternalStateUpdates update = internalModuleUpdates.removeFirst();
        inputModuleMap.put(update.sourceId(), update.pulse());
        EnumSet<Pulse> distinctPulses = EnumSet.copyOf(inputModuleMap.values());
        if (distinctPulses.stream().allMatch(it -> it == Pulse.HIGH)) {
            destinationModules.forEach(it -> it.update(Pulse.LOW, this));
        } else {
            destinationModules.forEach(it -> it.update(Pulse.HIGH, this));
        }
        return new ArrayList<>(destinationModules);
    }

    @Override
    public boolean hasMultipleUpdates() {
        return internalModuleUpdates.size() > 1;
    }
}
