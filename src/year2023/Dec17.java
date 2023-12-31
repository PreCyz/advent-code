package year2023;

import base.DecBase;

import java.util.*;
import java.util.stream.Stream;

public class Dec17 extends DecBase {

    public Dec17(String fileName) {
        super(fileName);
    }

    public enum Direction {START, NORTH, SOUTH, WEST, EAST}

    public static class CityBlock implements Comparable<CityBlock> {
        public int number;
        public int heatLoss;
        public int x;
        public int y;
        public final LinkedList<Route> route;

        public CityBlock(int number, int heatLoss, int x, int y) {
            this.number = number;
            this.heatLoss = heatLoss;
            this.x = x;
            this.y = y;
            route = new LinkedList<>();
        }

        CityBlock(CityBlock cityBlock, List<Route> steps) {
            this.number = cityBlock.number;
            this.heatLoss = cityBlock.heatLoss;
            this.x = cityBlock.x;
            this.y = cityBlock.y;
            this.route = new LinkedList<>(steps);
        }

        public CityBlock(CityBlock cityBlock, int heatLoss) {
            this(cityBlock, cityBlock.route);
            this.heatLoss = heatLoss;
        }

        @Override
        public String toString() {
            return "CityBlock{" +
                    "number=" + number +
                    ", heatLoss=" + heatLoss +
                    ", steps=" + route +
                    '}';
        }

        @Override
        public int compareTo(CityBlock o) {
            if (heatLoss < o.heatLoss) {
                return -1;
            } else if (heatLoss > o.heatLoss) {
                return 1;
            }
            return 0;
        }
    }

    public static class Route {
        Direction direction;
        int heatLoss;

        Route(Direction direction, CityBlock cityBlock) {
            this.direction = direction;
            this.heatLoss = cityBlock.heatLoss;
        }

        @Override
        public String toString() {
            return "Route{" +
                    "direction=" + direction +
                    ", value=" + heatLoss +
                    '}';
        }
    }

    @Override
    public Dec17 readDefaultInput() {
        System.out.println("Reading default input.");
        inputStrings = new LinkedList<>(Stream.of(
                "2413432311323",
                "3215453535623",
                "3255245654254",
                "3446585845452",
                "4546657867536",
                "1438598798454",
                "4457876987766",
                "3637877979653",
                "4654967986887",
                "4564679986453",
                "1224686865563",
                "2546548887735",
                "4322674655533"
        ).toList());
        return this;
    }

    @Override
    protected void calculatePart1() {
        int nodeNumber = 0;
        final CityBlock[][] cityBlocks = new CityBlock[inputStrings.size()][inputStrings.getFirst().length()];
        for (int y = 0; y < inputStrings.size(); ++y) {
            String line = inputStrings.get(y);
            char[] charArray = line.toCharArray();
            for (int x = 0; x < charArray.length; ++x) {
                int heatLoss = Integer.parseInt(String.valueOf(charArray[x]));
                cityBlocks[y][x] = new CityBlock(nodeNumber++, heatLoss, x, y);
            }
        }

        int totalNodes = inputStrings.size() * inputStrings.getFirst().length();
        CityBlock startNode = new CityBlock(cityBlocks[0][0], 0);
        int numberOfLastSteps = 3;

        Dijkstra dijkstra = new Dijkstra(totalNodes, cityBlocks, numberOfLastSteps);
        dijkstra.dijkstra(startNode);

        int destinationNodeNumber = totalNodes - 1;
        long sum = dijkstra.heatLosses[destinationNodeNumber];
        System.out.printf("Part 1 - Total score %d%n", sum);
        System.out.printf("Part 1 - directions to %d: %s%n", destinationNodeNumber, dijkstra.directionsMap.get(destinationNodeNumber));
    }

    public static class Dijkstra {

        public final int[] heatLosses;
        protected final int numberOfLastSteps;
        protected final Set<Integer> visited;
        final PriorityQueue<CityBlock> pQue;
        protected final int totalNodes;
        protected final CityBlock[][] cityBlocks;

        public final Map<Integer, LinkedList<Route>> directionsMap;

        public Dijkstra(int totalNodes, CityBlock[][] cityBlocks, int numberOfLastSteps) {
            this.cityBlocks = cityBlocks;
            this.totalNodes = totalNodes;
            heatLosses = new int[totalNodes];
            this.numberOfLastSteps = numberOfLastSteps;
            visited = new HashSet<>();
            pQue = new PriorityQueue<>(totalNodes);
            directionsMap = new LinkedHashMap<>();
        }

        public void dijkstra(CityBlock startNode) {
            for (int j = 0; j < totalNodes; j++) {
                heatLosses[j] = Integer.MAX_VALUE;
                directionsMap.put(j, new LinkedList<>());
            }

            pQue.add(startNode);

            directionsMap.replace(startNode.number, startNode.route);
            heatLosses[startNode.number] = 0;

            while (visited.size() != totalNodes) {
                if (pQue.isEmpty()) {
                    return;
                }

                CityBlock cityBlock = pQue.poll();
                int ux = cityBlock.number;

                if (visited.contains(ux)) {
                    continue;
                }

                visited.add(ux);
                processNeighbours(cityBlock);
            }
        }

        private void processNeighbours(CityBlock cityBlock) {
            int newHeatLoss;

            List<CityBlock> neighbours = findNeighbors(cityBlock);

            for (CityBlock neighbour : neighbours) {
                if (!visited.contains(neighbour.number)) {
                    newHeatLoss = heatLosses[cityBlock.number] + neighbour.heatLoss;

                    if (newHeatLoss < heatLosses[neighbour.number]) {
                        heatLosses[neighbour.number] = newHeatLoss;
                        directionsMap.replace(neighbour.number, new LinkedList<>(neighbour.route));
                    }

                    CityBlock toQueue = new CityBlock(neighbour, heatLosses[neighbour.number]);
                    pQue.add(toQueue);
                }
            }
        }

        protected ArrayList<CityBlock> findNeighbors(CityBlock cityBlock) {
            List<Route> steps = cityBlock.route;

            Direction lastStep = Direction.START;
            if (!steps.isEmpty()) {
                lastStep = steps.stream().map(it -> it.direction).toList().get(steps.size() - 1);
            }

            ArrayList<CityBlock> neighbors = new ArrayList<>(3);
            if (steps.size() >= numberOfLastSteps) {
                List<Direction> lastThreeSteps = steps.stream()
                        .map(it -> it.direction)
                        .toList()
                        .subList(steps.size() - numberOfLastSteps, steps.size());
                switch (lastStep) {
                    case EAST -> {
                        if (!lastThreeSteps.stream().allMatch(it -> it == Direction.EAST)) {
                            addEAST(cityBlock, steps).ifPresent(neighbors::add);
                        }
                        if (!lastThreeSteps.stream().allMatch(it -> it == Direction.SOUTH)) {
                            addSOUTH(cityBlock, steps).ifPresent(neighbors::add);
                        }
                        if (!lastThreeSteps.stream().allMatch(it -> it == Direction.NORTH)) {
                            addNORTH(cityBlock, steps).ifPresent(neighbors::add);
                        }
                    }
                    case WEST -> {
                        if (!lastThreeSteps.stream().allMatch(it -> it == Direction.WEST)) {
                            addWEST(cityBlock, steps).ifPresent(neighbors::add);
                        }
                        if (!lastThreeSteps.stream().allMatch(it -> it == Direction.SOUTH)) {
                            addSOUTH(cityBlock, steps).ifPresent(neighbors::add);
                        }
                        if (!lastThreeSteps.stream().allMatch(it -> it == Direction.NORTH)) {
                            addNORTH(cityBlock, steps).ifPresent(neighbors::add);
                        }
                    }
                    case NORTH -> {
                        if (!lastThreeSteps.stream().allMatch(it -> it == Direction.NORTH)) {
                            addNORTH(cityBlock, steps).ifPresent(neighbors::add);
                        }
                        if (!lastThreeSteps.stream().allMatch(it -> it == Direction.EAST)) {
                            addEAST(cityBlock, steps).ifPresent(neighbors::add);
                        }
                        if (!lastThreeSteps.stream().allMatch(it -> it == Direction.WEST)) {
                            addWEST(cityBlock, steps).ifPresent(neighbors::add);
                        }
                    }
                    case SOUTH -> {
                        if (!lastThreeSteps.stream().allMatch(it -> it == Direction.SOUTH)) {
                            addSOUTH(cityBlock, steps).ifPresent(neighbors::add);
                        }
                        if (!lastThreeSteps.stream().allMatch(it -> it == Direction.EAST)) {
                            addEAST(cityBlock, steps).ifPresent(neighbors::add);
                        }
                        if (!lastThreeSteps.stream().allMatch(it -> it == Direction.WEST)) {
                            addWEST(cityBlock, steps).ifPresent(neighbors::add);
                        }
                    }
                }
            } else {
                switch (lastStep) {
                    case START -> {
                        addEAST(cityBlock, steps).ifPresent(neighbors::add);
                        addWEST(cityBlock, steps).ifPresent(neighbors::add);
                        addSOUTH(cityBlock, steps).ifPresent(neighbors::add);
                        addNORTH(cityBlock, steps).ifPresent(neighbors::add);
                    }
                    case EAST -> {
                        addEAST(cityBlock, steps).ifPresent(neighbors::add);
                        addSOUTH(cityBlock, steps).ifPresent(neighbors::add);
                        addNORTH(cityBlock, steps).ifPresent(neighbors::add);
                    }
                    case WEST -> {
                        addWEST(cityBlock, steps).ifPresent(neighbors::add);
                        addSOUTH(cityBlock, steps).ifPresent(neighbors::add);
                        addNORTH(cityBlock, steps).ifPresent(neighbors::add);
                    }
                    case NORTH -> {
                        addNORTH(cityBlock, steps).ifPresent(neighbors::add);
                        addEAST(cityBlock, steps).ifPresent(neighbors::add);
                        addWEST(cityBlock, steps).ifPresent(neighbors::add);

                    }
                    case SOUTH -> {
                        addSOUTH(cityBlock, steps).ifPresent(neighbors::add);
                        addEAST(cityBlock, steps).ifPresent(neighbors::add);
                        addWEST(cityBlock, steps).ifPresent(neighbors::add);
                    }
                }
            }

            neighbors.trimToSize();
            return neighbors;
        }

        private Optional<CityBlock> addEAST(CityBlock cityBlock, List<Route> steps) {
            Optional<CityBlock> result = Optional.empty();
            if (cityBlock.x + 1 < cityBlocks[cityBlock.y].length) {
                LinkedList<Route> newSteps = new LinkedList<>(steps);
                newSteps.addLast(new Route(Direction.EAST, cityBlocks[cityBlock.y][cityBlock.x + 1]));
                result = Optional.of(new CityBlock(cityBlocks[cityBlock.y][cityBlock.x + 1], newSteps));
            }
            return result;
        }

        private Optional<CityBlock> addWEST(CityBlock cityBlock, List<Route> steps) {
            Optional<CityBlock> result = Optional.empty();
            if (cityBlock.x - 1 >= 0) {
                LinkedList<Route> newSteps = new LinkedList<>(steps);
                newSteps.addLast(new Route(Direction.WEST, cityBlocks[cityBlock.y][cityBlock.x - 1]));
                result = Optional.of(new CityBlock(cityBlocks[cityBlock.y][cityBlock.x - 1], newSteps));
            }
            return result;
        }

        private Optional<CityBlock> addSOUTH(CityBlock cityBlock, List<Route> steps) {
            Optional<CityBlock> result = Optional.empty();
            if (cityBlock.y + 1 < cityBlocks.length) {
                LinkedList<Route> newSteps = new LinkedList<>(steps);
                newSteps.addLast(new Route(Direction.SOUTH, cityBlocks[cityBlock.y + 1][cityBlock.x]));
                result = Optional.of(new CityBlock(cityBlocks[cityBlock.y + 1][cityBlock.x], newSteps));
            }
            return result;
        }

        private Optional<CityBlock> addNORTH(CityBlock cityBlock, List<Route> steps) {
            Optional<CityBlock> result = Optional.empty();
            if (cityBlock.y - 1 >= 0) {
                LinkedList<Route> newSteps = new LinkedList<>(steps);
                newSteps.addLast(new Route(Direction.NORTH, cityBlocks[cityBlock.y - 1][cityBlock.x]));
                result = Optional.of(new CityBlock(cityBlocks[cityBlock.y - 1][cityBlock.x], newSteps));
            }
            return result;
        }
    }

    @Override
    protected void calculatePart2() {
        long sum = 0;
        System.out.printf("Part 2 - Total score %d%n", sum);
    }
}
