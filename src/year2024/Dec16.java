package year2024;

import base.DecBase;
import utils.GridUtils;

import java.util.*;
import java.util.stream.Stream;

class Dec16 extends DecBase {

    public Dec16(int year) {
        super(year, 16);
    }

    record Point(int x, int y, char value, Direction direction) { }

    static class Node implements Comparator<Node> {

        static Comparator<Node> COMPARATOR = Comparator.comparingInt(o -> o.cost);
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
            return COMPARATOR.compare(n1, n2);
        }

        @Override
        public String toString() {
            return "Node{number=" + number + ", cost=" + cost + ", point=" + point + '}';
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

    @Override
    protected void calculatePart1() {
        Node startNode = null, endNode = null;
        List<Node> maze = new ArrayList<>(inputStrings.size());
        int y = 0;
        int nodeNumber = 0;
        char[][] grid = new char[inputStrings.size()][inputStrings.getFirst().length()];
        for (String input : inputStrings) {
            char[] charArray = input.toCharArray();
            for (int x = 0; x < input.length(); x++) {
                Point point = new Point(x, y, charArray[x], null);
                Node node = new Node(nodeNumber, 0, point);
                if (charArray[x] == '#') {
                    node = new Node(nodeNumber, 0, point);
                }
                maze.add(node);

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
        }

        Dijkstra dijkstra = new Dijkstra(inputStrings.size() * inputStrings.getFirst().length());
        dijkstra.dijkstra(maze, startNode);
        int distance = dijkstra.getDistance(endNode);
        dijkstra.updateGrid(endNode, grid);
        GridUtils.writeToFile(grid);

//        107476
//        106476
        System.out.printf("Part 1 - Sum %d%n", distance);

        dijkstra.calculatePath(endNode);
        Map<Integer, Direction> map = new HashMap<>();
        Direction prev = null;
        int turns = 0;
        for (Point point : dijkstra.path.reversed()) {
            Direction current = point.direction;
            if (prev == null || prev != current) {
                map.put(++turns, point.direction);
            }
            prev = current;
        }
/*        map.forEach((k, v) -> {
            System.out.printf("%s -> ", v);
        });
        System.out.println("END");*/
        int sum = dijkstra.path.size() + (turns - 1) * 1000;
        System.out.printf("Steps: %d, Turns: %d Distance: %d%n", dijkstra.path.size(), turns - 1, sum);
    }

    @Override
    protected void calculatePart2() {

//        System.out.printf("Part 2 - Sum[%b] %d%n", move, move);
    }

    static class Dijkstra {
        private final int totalNodes;
        private final int[] distance;
        private final Set<Integer> visited;
        private final PriorityQueue<Node> pQue;

        List<Node> adjacent;
        Point[] parents;
        ArrayList<Point> path;

        public Dijkstra(int totalNodes) {
            this.totalNodes = totalNodes;
            distance = new int[totalNodes];
            visited = new HashSet<>();
            pQue = new PriorityQueue<>(totalNodes, Node.COMPARATOR);
            parents = new Point[totalNodes];
        }

        public void dijkstra(List<Node> adjacent, Node startNode) {
            this.adjacent = adjacent;

            for (int j = 0; j < totalNodes; j++) {
                distance[j] = Integer.MAX_VALUE;
            }

            pQue.add(startNode);
            distance[startNode.number] = 0;
            parents[startNode.number] = null;

            while (!pQue.isEmpty()) {
                Node ux = pQue.remove();
                if (visited.contains(ux.number)) {
                    continue;
                }
                visited.add(ux.number);
                processNeighbours(ux);
            }
        }

        private void processNeighbours(Node ux) {
            for (Node vx : findNeighbours(ux)) {
                if (!visited.contains(vx.number)) {
                    int edgeDist = vx.cost;
                    int newDist = distance[ux.number] + edgeDist;

                    if (newDist < distance[vx.number]) {
                        distance[vx.number] = newDist;
                        parents[vx.number] = ux.point;
                    }
                    pQue.add(new Node(vx.number, distance[vx.number], vx.point));
                }
            }
        }

        private ArrayList<Node> findNeighbours(Node ux) {
            ArrayList<Node> neighbours = new ArrayList<>(3);
            int noRotation = 0;
            int rotation = 1000;

            switch (ux.point.direction) {
                case UP -> {
                    neighbours.addAll(getNeighbour(ux, Direction.UP, noRotation));
                    neighbours.addAll(getNeighbour(ux, Direction.RIGHT, rotation));
                    neighbours.addAll(getNeighbour(ux, Direction.LEFT, rotation));
                }
                case DOWN -> {
                    neighbours.addAll(getNeighbour(ux, Direction.DOWN, noRotation));
                    neighbours.addAll(getNeighbour(ux, Direction.RIGHT, rotation));
                    neighbours.addAll(getNeighbour(ux, Direction.LEFT, rotation));
                }
                case LEFT -> {
                    neighbours.addAll(getNeighbour(ux, Direction.LEFT, noRotation));
                    neighbours.addAll(getNeighbour(ux, Direction.UP, rotation));
                    neighbours.addAll(getNeighbour(ux, Direction.DOWN, rotation));
                }
                case RIGHT -> {
                    neighbours.addAll(getNeighbour(ux, Direction.RIGHT, noRotation));
                    neighbours.addAll(getNeighbour(ux, Direction.UP, rotation));
                    neighbours.addAll(getNeighbour(ux, Direction.DOWN, rotation));
                }
            }
            neighbours.trimToSize();
            return neighbours;
        }

        private List<Node> getNeighbour(Node ux, Direction direction, final int additionalCost) {
            int newX = ux.point.x + direction.mvX;
            int newY = ux.point.y + direction.mvY;
            return adjacent.stream()
                    .filter(n -> n.point.value != '#')
                    .filter(n -> n.point.x == newX)
                    .filter(n -> n.point.y == newY)
                    .map(n -> new Node(n.number, 1 + additionalCost, new Point(newX, newY, n.point.value, direction)))
                    .toList();
        }

        void calculatePath(Node node) {
            path = new ArrayList<>(totalNodes);
            int startIndex = node.number;
            while (true) {
                Point point = parents[startIndex];
                if (point == null) {
                    break;
                }
                path.add(point);
                Optional<Node> newNode = adjacent.stream()
                        .filter(n -> n.point.x == point.x && n.point.y == point.y)
                        .findFirst();
                if (newNode.isPresent()) {
                    startIndex = newNode.get().number;
                } else {
                    break;
                }
            }
            path.trimToSize();
//            System.out.printf("%s", path.stream().map(Record::toString).collect(Collectors.joining("->")));
        }

        void updateGrid(Node node, char[][] grid) {
            int startIndex = node.number;
            while (true) {
                Point point = parents[startIndex];
                if (point == null) {
                    break;
                }
                grid[point.y][point.x] = '0';
                Optional<Node> newNode = adjacent.stream()
                        .filter(n -> n.point.x == point.x && n.point.y == point.y)
                        .findFirst();
                if (newNode.isPresent()) {
                    startIndex = newNode.get().number;
                } else {
                    break;
                }
            }
        }

        public int getDistance(Node destinationNode) {
            return distance[destinationNode.number];
        }
    }
}