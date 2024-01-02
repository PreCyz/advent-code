package year2022;

import base.DecBase;

import java.util.*;
import java.util.stream.Stream;

class Dec16 extends DecBase {
    protected Dec16(int year) {
        super(year, 16);
    }

    @Override
    public Dec16 readDefaultInput() {
        System.out.println("Reading default input.");
        inputStrings = new LinkedList<>(Stream.of(
                "Valve AA has flow rate=0; tunnels lead to valves DD, II, BB",
                "Valve BB has flow rate=13; tunnels lead to valves CC, AA",
                "Valve CC has flow rate=2; tunnels lead to valves DD, BB",
                "Valve DD has flow rate=20; tunnels lead to valves CC, AA, EE",
                "Valve EE has flow rate=3; tunnels lead to valves FF, DD",
                "Valve FF has flow rate=0; tunnels lead to valves EE, GG",
                "Valve GG has flow rate=0; tunnels lead to valves FF, HH",
                "Valve HH has flow rate=22; tunnel leads to valve GG",
                "Valve II has flow rate=0; tunnels lead to valves AA, JJ",
                "Valve JJ has flow rate=21; tunnel leads to valve II"
        ).toList());
        return this;
    }

    class Valve {

        String name;
        int rate;
        List<Valve> valves;
        boolean opened;

        Valve(String name, int rate, List<Valve> valves, boolean opened) {
            this.name = name;
            this.rate = rate;
            this.valves = valves;
            this.opened = opened;
        }

        Valve(String name, int rate) {
            this(name, rate, new ArrayList<>(), false);
        }

        @Override
        public String toString() {
            return "Valve{" +
                    "name='" + name + '\'' +
                    ", rate=" + rate +
                    ", opened=" + opened +
                    '}';
        }
    }

    private Map<String, Valve> valveMap;

    @Override
    protected void calculatePart1() {
        valveMap = new HashMap<>();
        Map<String, String> valveMovesMap = new HashMap<>();
        for (String line : inputStrings) {
            String[] moveDetails = line.replace("Valve ", "")
                    .replace("has flow rate=", "")
                    .replace("tunnels lead to valves ", "")
                    .replace("tunnel leads to valve ", "")
                    .split(";");
            String[] valveDetails = moveDetails[0].split(" ");
            String name = valveDetails[0];
            int rate = Integer.parseInt(valveDetails[1]);
            valveMap.put(name, new Valve(name, rate));
            valveMovesMap.put(name, moveDetails[1]);
        }

        for (Map.Entry<String, String> entry : valveMovesMap.entrySet()) {
            Valve valve = valveMap.get(entry.getKey());
            String[] s = entry.getValue().replace(" ", "").split(",");
            for (String vMove : s) {
                valve.valves.add(valveMap.get(vMove));
            }
        }

//        valveMap.forEach((k, v) -> {
//            System.out.println(k + " = " + v);
//        });

        int pressureValue = 0;
        int minutes = 30;
        Valve currentValve = valveMap.get("AA");
        Valve nextValve = null;
        LinkedList<Valve> openedValves = new LinkedList<>();
        while (minutes != 0) {
            //move to next valve
            if (nextValve == null) {
                nextValve = getNextValve(currentValve);
            } else {
                //open the door
                nextValve.opened = true;
                openedValves.add(nextValve);
                currentValve = nextValve;
                nextValve = null;
                pressureValue += openedValves.stream().mapToInt(it -> it.rate).sum();
            }
            minutes--;
        }

        System.out.println(pressureValue);

    }

    private Valve getNextValve(Valve currentValve) {
        Valve result = null;
        for (Valve valve : currentValve.valves) {
            if (result == null && !valve.opened) {
                result = valve;
            } else if (result.rate < valve.rate && !valve.opened) {
                result = valve;
            }
        }
        return result;
    }

}
