package year2023;

import base.DecBase;
import year2023.dec20.Module;
import year2023.dec20.*;

import java.util.*;
import java.util.stream.Stream;

public class Dec20 extends DecBase {

    public Dec20(int year) {
        super(year, 20);
    }

    @Override
    public Dec20 readDefaultInput() {
        System.out.println("Reading default input.");
        inputStrings = new LinkedList<>(Stream.of(
                /*"broadcaster -> a, b, c",
                "%a -> b",
                "%b -> c",
                "%c -> inv",
                "&inv -> a"*/
                "broadcaster -> a",
                        "%a -> inv, con",
                        "&inv -> b",
                        "%b -> con",
                        "&con -> output"
        ).toList());
        return this;
    }

    @Override
    protected void calculatePart1() {
        Map<String, String> map = new LinkedHashMap<>();
        for (String line : inputStrings) {
            String[] split = line.split(" -> ");
            map.put(split[0], split[1]);
        }

        Map<String, Module> moduleMap = createModuleMap(map);
        updateModules(map, moduleMap);

        ButtonModule button = new ButtonModule(moduleMap.get("broadcaster"));
        button.resetResult();

        for (int i = 1; i <= 1000; i++) {
//            System.out.println(i);
            processModules(button.sendPulse());
//            System.out.printf("HIGH: [%s] LOW: [%s]%n", button.getHighPulseCounter(), button.getLowPulseCounter());
        }

        long sum = button.result();
        System.out.printf("Part 1 - Total score %d%n", sum);
    }

    private void processModules(Collection<Module> modules) {
        Set<Module> nextModules = new LinkedHashSet<>();
        for (Module module : modules) {

            while(module.hasMultipleUpdates()) {
                nextModules.addAll(module.sendPulse());
            }
            nextModules.addAll(module.sendPulse());
        }

        if (!nextModules.isEmpty()) {
            processModules(nextModules);
        }
    }

    private Map<String, Module> createModuleMap(Map<String, String> map) {
        Map<String, Module> moduleMap = new LinkedHashMap<>();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (entry.getKey().startsWith("broadcaster")) {
                Broadcaster module = new Broadcaster();
                moduleMap.put(module.id(), module);
            } else if (entry.getKey().startsWith("%")) {
                FlipFlop flipFlop = new FlipFlop(entry.getKey().substring(1));
                moduleMap.put(flipFlop.id(), flipFlop);
            } else if (entry.getKey().startsWith("&")) {
                Conjunction conjunction = new Conjunction(entry.getKey().substring(1));
                moduleMap.put(conjunction.id(), conjunction);
            } else {
                Module module = new Module(entry.getKey());
                moduleMap.put(module.id(), module);
            }
        }
        return moduleMap;
    }

    private void updateModules(Map<String, String> map, Map<String, Module> moduleMap) {
        for (Map.Entry<String, String> entry : map.entrySet()) {
            String key = entry.getKey();
            if (!key.startsWith("broadcaster")) {
                key = key.substring(1);
            }

            String[] moduleNames = entry.getValue().split(", ");
            ArrayList<Module> modules = new ArrayList<>(moduleNames.length);
            for (String name : moduleNames) {
                Module module = moduleMap.get(name);
                if (module == null) {
                    module = new Module(name);
                }

                if (module instanceof Conjunction) {
                    module.updateInput(key);
                }
                modules.add(module);
            }

            moduleMap.get(key).addModules(modules);
        }
    }

    @Override
    protected void calculatePart2() {

        Map<String, String> map = new LinkedHashMap<>();
        for (String line : inputStrings) {
            String[] split = line.split(" -> ");
            map.put(split[0], split[1]);
        }

        Map<String, Module> moduleMap = createModuleMap(map);
        updateModules(map, moduleMap);

        ButtonModule button = new ButtonModule(moduleMap.get("broadcaster"));
        button.resetResult();

        long sum = 0;
        for (int i = 1; i <= 100_000_000; i++) {
            try {
                processModules(button.sendPulse());
                if (i % 100_000 == 0) {
                    System.out.println(i);
                }
            } catch (Exception ex) {
                System.err.println(ex.getMessage());
                System.out.println("Module rx received only 1 LOW pulse.");
                sum = i;
            }
        }

        System.out.printf("Part 2 - Total score %d%n", sum);
    }

}
