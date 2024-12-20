package year2024;

import base.DecBase;
import utils.GridUtils;

import java.util.*;
import java.util.stream.Stream;

class Dec16 extends DecBase {

    public Dec16(int year) {
        super(year, 16);
    }

    record Point(int x, int y, char value, Direction direction) {
        public String toString2() {
            return "Point{" +
                    "x=" + x +
                    ", y=" + y +
                    '}';
        }
    }

    @Override
    public Dec16 readDefaultInput() {
        System.out.println("Reading default input.");
        inputStrings = new LinkedList<>(Stream.of(
                /*"###############",
                "#.......#....E#",
                "#.#.###.#.###.#",
                "#.....#.#...#.#",
                "#.###.#####.#.#",
                "#.#.#.......#.#",
                "#.#.#####.###.#",
                "#...........#.#",
                "###.#.#####.#.#",
                "#...#.....#.#.#",
                "#.#.#.###.#.#.#",
                "#.....#...#.#.#",
                "#.###.#.#.#.#.#",
                "#S..#.....#...#",
                "###############"*/
                "#################",
                "#...#...#...#..E#",
                "#.#.#.#.#.#.#.#.#",
                "#.#.#.#...#...#.#",
                "#.#.#.#.###.#.#.#",
                "#...#.#.#.....#.#",
                "#.#.#.#.#.#####.#",
                "#.#...#.#.#.....#",
                "#.#.#####.#.###.#",
                "#.#.#.......#...#",
                "#.#.###.#####.###",
                "#.#.#...#.....#.#",
                "#.#.#.#####.###.#",
                "#.#.#.........#.#",
                "#.#.#.#########.#",
                "#S#.............#",
                "#################"
        ).toList());
        return this;
    }

    enum Direction {
        UP(0, -1, '^'),
        DOWN(0, 1, 'v'),
        LEFT(-1, 0, '<'),
        RIGHT(1, 0, '>');

        final int mvX;
        final int mvY;
        final char c;

        Direction(int mvX, int mvY, char c) {
            this.mvX = mvX;
            this.mvY = mvY;
            this.c = c;
        }
    }

    @Override
    protected void calculatePart1() {
        Node startNode = null, endNode = null;
        List<List<Node>> maze = new ArrayList<>(inputStrings.size());
        int y = 0;
        int nodeNumber = 0;
        char[][] grid = new char[inputStrings.size()][inputStrings.getFirst().length()];
        for (String input : inputStrings) {
            ArrayList<Node> row = new ArrayList<>(input.length());
            char[] charArray = input.toCharArray();
            for (int x = 0; x < input.length(); x++) {
                Point point = new Point(x, y, charArray[x], null);
                Node node = new Node(nodeNumber, 0, point);
                if (charArray[x] == '#') {
                    node = new Node(nodeNumber, 0, point);
                }
                row.add(node);

                if (charArray[x] == 'S') {
                    startNode = new Node(nodeNumber, 0, new Point(x, y, charArray[x], Direction.LEFT));
                }
                if (charArray[x] == 'E') {
                    endNode = new Node(nodeNumber, 0, point);
                }
                nodeNumber++;
                grid[y][x] = charArray[x];
            }
            y++;
            maze.add(row);
        }

        Dijkstra dijkstra = new Dijkstra(inputStrings.size() * inputStrings.getFirst().length());
        dijkstra.dijkstra(maze, startNode, endNode);
        int distance = dijkstra.getDistance(endNode);
//        dijkstra.printPath(endNode.number, grid);
        GridUtils.writeToFile(grid);

        long sum = 0;
//        107476
//        106476
        System.out.printf("Part 1 - Sum %d%n", distance);
    }

    @Override
    protected void calculatePart2() {

//        System.out.printf("Part 2 - Sum[%b] %d%n", move, move);
    }

    static class Node implements Comparator<Node> {

        int number;
        int price;
        Point point;

        Node(int number, int price, Point point) {
            this.number = number;
            this.price = price;
            this.point = point;
        }

        @Override
        public int compare(Node n1, Node n2) {
            return Integer.compare(n1.price, n2.price);
        }

        static Comparator<Node> COMPARATOR = Comparator.comparingInt(o -> o.price);

        @Override
        public String toString() {
            return "Node{number=" + number + ", price=" + price + ", point=" + point + '}';
        }
    }

    static class Dijkstra {

        private static final int NO_PARENT = -1;

        private final int[] distance;
        private final Set<Integer> visited;
        private final PriorityQueue<Node> pQue;

        private final int totalNodes;
        List<List<Node>> adjacent;
        int[] parents;

        public Dijkstra(int totalNodes) {
            this.totalNodes = totalNodes;
            distance = new int[totalNodes];
            visited = new HashSet<>();
            pQue = new PriorityQueue<>(totalNodes, Node.COMPARATOR);
            parents = new int[totalNodes];
        }

        public void dijkstra(List<List<Node>> adjacent, Node startNode, Node endNode) {
            this.adjacent = adjacent;

            for (int j = 0; j < totalNodes; j++) {
                distance[j] = Integer.MAX_VALUE;
            }

            pQue.add(startNode);
            distance[startNode.number] = 0;
            parents[startNode.number] = NO_PARENT;

            while (!pQue.isEmpty()) {
                Node ux = pQue.remove();
                if (visited.contains(ux.number)) {
                    continue;
                }
                visited.add(ux.number);
                eNeighbours(ux);
            }
        }

        private void eNeighbours(Node ux) {
            int edgeDist = -1;
            int newDist = -1;

            // All neighbors of vx
            for (Node vx : findNeighbours(ux)) {
                // If the current node hasn't been already processed
                if (!visited.contains(vx.number)) {
                    edgeDist = vx.price;
                    newDist = distance[ux.number] + edgeDist;

                    // If the new distance is lesser in the cost
                    if (newDist < distance[vx.number]) {
                        distance[vx.number] = newDist;
                        parents[vx.number] = ux.number;
                        /*System.out.printf("%d(%d,%d) -> %d(%d,%d) [%d]%n",
                                ux.number, ux.point.x, ux.point.y,
                                vx.number, vx.point.x, vx.point.y, newDist);*/
                    }

                    // Adding the current node to the priority queue pQue
                    pQue.add(new Node(vx.number, distance[vx.number], vx.point));
                }
            }
        }

        private ArrayList<Node> findNeighbours(Node ux) {
            ArrayList<Node> neighbours = new ArrayList<>(3);

            switch (ux.point.direction) {
                case UP -> {
                    neighbours.addAll(getNeighbour(ux, Direction.UP, 0));
                    neighbours.addAll(getNeighbour(ux, Direction.RIGHT, 1000));
                    neighbours.addAll(getNeighbour(ux, Direction.LEFT, 1000));
                }
                case DOWN -> {
                    neighbours.addAll(getNeighbour(ux, Direction.DOWN, 0));
                    neighbours.addAll(getNeighbour(ux, Direction.RIGHT, 1000));
                    neighbours.addAll(getNeighbour(ux, Direction.LEFT, 1000));
                }
                case LEFT -> {
                    neighbours.addAll(getNeighbour(ux, Direction.LEFT, 0));
                    neighbours.addAll(getNeighbour(ux, Direction.UP, 1000));
                    neighbours.addAll(getNeighbour(ux, Direction.DOWN, 1000));
                }
                case RIGHT -> {
                    neighbours.addAll(getNeighbour(ux, Direction.RIGHT, 0));
                    neighbours.addAll(getNeighbour(ux, Direction.UP, 1000));
                    neighbours.addAll(getNeighbour(ux, Direction.DOWN, 1000));
                }
            }
            neighbours.trimToSize();
            return neighbours;
        }

        void printPath(int startVertex, char[][] grid) {
            if (startVertex == NO_PARENT || parents[startVertex] == Integer.MAX_VALUE) {
                return;
            }
            printPath(parents[startVertex], grid);
            adjacent.stream()
                    .flatMap(List::stream)
                    .filter(n -> n.number == startVertex)
                    .findFirst()
                    .ifPresent(n -> grid[n.point.y][n.point.x] = '0');
        }

        private List<Node> getNeighbour(Node ux, Direction direction, final int additionalCost) {
            int newX;
            int newY;
            newX = ux.point.x + direction.mvX;
            newY = ux.point.y + direction.mvY;
            return adjacent.stream()
                    .flatMap(List::stream)
                    .filter(n -> n.point.x == newX)
                    .filter(n -> n.point.y == newY)
                    .filter(n -> n.point.value != '#')
                    .map(n -> new Node(n.number, 1 + additionalCost, new Point(newX, newY, n.point.value, direction)))
                    .toList();
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