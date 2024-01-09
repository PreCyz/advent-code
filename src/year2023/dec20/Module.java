package year2023.dec20;

import java.util.*;

public class Module {
    private static long highPulseCounter = 0;
    private static long lowPulseCounter = 0;

    public static final LinkedHashMap<Integer, List<String>> rxMap = new LinkedHashMap<>();
    public static Integer currentIdx = 0;
    private final String id;
    protected List<Module> destinationModules;
    protected LinkedList<Pulse> updates = new LinkedList<>();

    public Module(String id) {
        this.id = id;
    }

    public String id() {
        return id;
    }

    public void incHighPulse() {
        highPulseCounter++;
    }

    public long getHighPulseCounter() {
        return highPulseCounter;
    }

    public void incLowPulse() {
        lowPulseCounter++;
    }

    public long getLowPulseCounter() {
        return lowPulseCounter;
    }

    public void addModules(List<Module> modules) {
        destinationModules = new ArrayList<>(modules);
    }

    public void updateInput(String id) {
        throw new RuntimeException("Not implemented");
    }


    public void update(Pulse pulse, Module source) {
        updates.add(pulse);
        if (pulse == Pulse.HIGH) {
            incHighPulse();
        } else if (pulse == Pulse.LOW) {
            incLowPulse();
        }
        /*if ("rx".equalsIgnoreCase(id())) {
            rxMap.putIfAbsent(currentIdx, new ArrayList<>(List.of("%s --%s".formatted(source.id(), pulse))));
            if (rxMap.containsKey(currentIdx)) {
                rxMap.get(currentIdx).add("%s --%s".formatted(source.id(), pulse));
            }
        }*/
//        System.out.printf("%s -%s-> %s%n", source.id(), pulse, id());
//        System.out.printf("[%s] updated by [%s] with pulse [%s] updates.size=[%d]%n",
//                id(), source.id(), pulse, updates.size());
    }

    public List<Module> sendPulse() {
        if ("rx".equalsIgnoreCase(id()) && updates.contains(Pulse.LOW)) {
//            Pulse pulse = updates.get(0);
//            if (Pulse.LOW == pulse) {
                throw new RuntimeException("rx has only 1 update with pulse equal LOW.");
//            }
        }
        //updates.forEach(pulse -> System.out.println("OUTPUT"));
        updates.clear();
        return Collections.emptyList();
    }

    public final long result() {
        return highPulseCounter * lowPulseCounter;
    }
    public final void resetResult() {
        highPulseCounter = 0;
        lowPulseCounter = 0;
    }

    public boolean hasMultipleUpdates() {
        return updates.size() > 1;
    }

    @Override
    public String toString() {
        return "Module{" + id + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Module module = (Module) o;

        return Objects.equals(id, module.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
