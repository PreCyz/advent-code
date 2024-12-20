package year2024;

import base.DecBase;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

class Dec20 extends DecBase {

    public Dec20(int year) {
        super(year, 20);
    }

    record Point(int x, int y, char value) {
    }

    enum Direction {
        UP(0, -1),
        DOWN(0, 1),
        LEFT(-1, 0),
        RIGHT(1, 0);

        final int mvX;
        final int mvY;

        Direction(int mvX, int mvY) {
            this.mvX = mvX;
            this.mvY = mvY;
        }
    }

    @Override
    public Dec20 readDefaultInput() {
        System.out.println("Reading default input.");
        inputStrings = new LinkedList<>(Stream.of(
                "###############",
                "#...#...#.....#",
                "#.#.#.#.#.###.#",
                "#S#...#.#.#...#",
                "#######.#.#.###",
                "#######.#.#...#",
                "#######.#.###.#",
                "###..E#...#...#",
                "###.#######.###",
                "#...###...#...#",
                "#.#####.#.###.#",
                "#.#...#.#.#...#",
                "#.#.#.#.#.#.###",
                "#...#...#...###",
                "###############"
        ).toList());
        return this;
    }

    @Override
    protected void calculatePart1() {
        Node start = null, end = null;
        int maxY = inputStrings.size();
        int maxX = inputStrings.getFirst().length();
        int numberOfNodes = maxY * maxX;
        char[][] grid = new char[maxY][maxX];
        ArrayList<Node> trace = new ArrayList<>(numberOfNodes);
        ArrayList<Point> walls = new ArrayList<>(numberOfNodes);

        int y = 0;
        int nodeNumber = 0;
        for (String input : inputStrings) {
            char[] charArray = input.toCharArray();
            for (int x = 0; x < charArray.length; x++) {
                grid[y][x] = charArray[x];
                Point point = new Point(x, y, charArray[x]);
                if (charArray[x] == 'S') {
                    start = new Node(nodeNumber, 0, point);
                } else if (charArray[x] == 'E') {
                    end = new Node(nodeNumber, 0, point);
                }
                Node node = new Node(nodeNumber, 1, point);
                if (charArray[x] == '#') {
                    node = new Node(nodeNumber, -1, point);
                    if (x > 0 && x < maxX - 1 && y > 0 && y < maxY - 1) {
                        walls.add(point);
                    }
                }
                trace.add(node);
                nodeNumber++;
            }
            y++;
        }
        trace.trimToSize();
        Dijkstra dijkstra2 = new Dijkstra(trace.size());
        dijkstra2.dijkstra(trace, start);
        int distance = dijkstra2.getDistance(end);
        System.out.printf("Route distance %d%n", distance);

        AtomicInteger cheatCounter = new AtomicInteger(0);
        Map<Integer, Integer> cheats = new HashMap<>();
        List<CompletableFuture<Void>> cf= new ArrayList<>(walls.size());
        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        final Node s = start;
        final Node e = end;
        for (Point wall : walls) {
            cf.add(CompletableFuture.runAsync(() -> {
                int replacedNodeNumber = trace.stream()
                        .filter(n -> n.point.x == wall.x && n.point.y == wall.y)
                        .map(n -> n.number)
                        .findFirst()
                        .get();
                ArrayList<Node> newNodeSet = new ArrayList<>(trace
                        .stream()
                        .filter(n -> !(n.point.x == wall.x && n.point.y == wall.y))
                        .toList()
                );
                newNodeSet.add(new Node(replacedNodeNumber, 1, new Point(wall.x, wall.y, '.')));

                Dijkstra dijkstra = new Dijkstra(trace.size());
                dijkstra.dijkstra(newNodeSet, s);
                int newDistance = dijkstra.getDistance(e);
                if (newDistance < distance) {
                    int savings = distance - newDistance;
                    if (cheats.containsKey(savings)) {
                        cheats.put(savings, cheats.get(savings) + 1);
                    } else {
                        cheats.put(savings, 1);
                    }
                    System.out.printf("Wall (%d,%d) replaced, new distance: %d, savings: %d%n", wall.x, wall.y, newDistance, savings);
                }

                if (distance - newDistance >= 100) {
                    cheatCounter.incrementAndGet();
                }
            }, executor));
        }

        CompletableFuture.allOf(cf.toArray(new CompletableFuture[0])).join();

        cheats.forEach((k, v) -> System.out.printf("\tThere are %d cheats that save %d picoseconds%n", v, k));

        System.out.printf("Part 1 - Sum %s%n", cheatCounter.get());
    }

    @Override
    protected void calculatePart2() {

        System.out.printf("Part 2%n");
    }

    static class Node implements Comparator<Node> {

        int number;
        int cost;
        Point point;

        Node(int number, int cost, Point point) {
            this.number = number;
            this.cost = cost;
            this.point = point;
        }

        @Override
        public int compare(Node n1, Node n2) {
            return Integer.compare(n1.cost, n2.cost);
        }

        @Override
        public String toString() {
            return "Node{number=" + number + ", cost=" + cost + ", point=" + point + '}';
        }

        static Comparator<Node> COMPARATOR = Comparator.comparingInt(o -> o.cost);
    }

    static class Dijkstra {

        // Member variables of the class
        private final int[] distance;
        private final Set<Integer> settled;
        private final PriorityQueue<Node> pQue;

        // Total count of the vertices
        private final int totalNodes;
        List<Node> adjacent;

        // Constructor of the class
        public Dijkstra(int totalNodesNumber) {
            this.totalNodes = totalNodesNumber;
            distance = new int[totalNodesNumber];
            settled = new HashSet<>();
            pQue = new PriorityQueue<>(totalNodesNumber, Node.COMPARATOR);
        }

        public void dijkstra(List<Node> adjacent, Node startNode) {
            this.adjacent = adjacent;

            for (int j = 0; j < totalNodes; j++) {
                distance[j] = Integer.MAX_VALUE;
            }
            pQue.add(startNode);
            distance[startNode.number] = 0;

            while (!pQue.isEmpty()) {
                Node ux = pQue.remove();
                if (settled.contains(ux.number)) {
                    continue;
                }
                settled.add(ux.number);
                processNeighbours(ux);
            }
        }

        private void processNeighbours(Node ux) {
            int edgeDist = -1;
            int newDist = -1;

            // All neighbors of vx
            for (Node vx : findNeighbours(ux)) {
                // If the current node hasn't been already processed
                if (!settled.contains(vx.number)) {
                    edgeDist = vx.cost;
                    newDist = distance[ux.number] + edgeDist;

                    // If the new distance is lesser in the cost
                    if (newDist < distance[vx.number]) {
                        distance[vx.number] = newDist;
                    }

                    // Adding the current node to the priority queue pQue
                    pQue.add(new Node(vx.number, distance[vx.number], vx.point));
                }
            }
        }

        private List<Node> findNeighbours(Node ux) {
            ArrayList<Node> neighbours = new ArrayList<>(3);
            getNode(Direction.DOWN, ux).ifPresent(neighbours::add);
            getNode(Direction.UP, ux).ifPresent(neighbours::add);
            getNode(Direction.LEFT, ux).ifPresent(neighbours::add);
            getNode(Direction.RIGHT, ux).ifPresent(neighbours::add);
            return neighbours;
        }

        Optional<Node> getNode(Direction direction, Node node) {
            return adjacent.stream()
                    .filter(n -> n.point.x == node.point.x + direction.mvX)
                    .filter(n -> n.point.y == node.point.y + direction.mvY)
                    .filter(n -> n.point.value != '#')
                    .filter(n -> n.point.x >= 0 && n.point.y >= 0)
                    .findFirst();
        }

        public void print(int startNodeNumber) {
            for (int j = 0; j < distance.length; j++) {
                System.out.printf("%d to %d is %d%n", startNodeNumber, j, distance[j]);
            }
        }

        public int getDistance(Node destinationNode) {
            return distance[destinationNode.number];
        }
    }

}