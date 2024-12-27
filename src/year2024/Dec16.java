package year2024;

import base.DecBase;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class Dec16 extends DecBase {

    public Dec16(int year) {
        super(year, 16);
    }

    @Override
    public Dec16 readDefaultInput() {
        System.out.println("Reading default input.");
        inputStrings = new LinkedList<>(Stream.of(
                "###############",
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
                "###############"
                /*"#################",
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
                "#################"*/
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

        /*dijkstra.calculatePath(endNode);
        System.out.println(dijkstra.path.reversed());*/
        /*dijkstra.updateGrid(endNode, grid);
        GridUtils.writeToFile(grid);
        dijkstra.printDistanceMap(grid,2, 4);*/

//        107468 - HIT
        int distance = dijkstra.getDistance(endNode);
        System.out.printf("Part 1 - Sum %d%n", distance);
    }

    @Override
    protected void calculatePart2() {
        Node startNode = null, endNode = null, end_1Node = null;
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
                    end_1Node = new Node(nodeNumber - 1, 0, new Point(x, y + 1, charArray[x], Direction.UP));
                }
                nodeNumber++;
                grid[y][x] = charArray[x];
            }
            y++;
        }

        Dijkstra start2end = new Dijkstra(inputStrings.size() * inputStrings.getFirst().length());
        start2end.dijkstra(maze, startNode);
        int[] distFromSource = start2end.distance;

        Dijkstra end2start = new Dijkstra(inputStrings.size() * inputStrings.getFirst().length());
        end2start.dijkstra(maze, end_1Node);
        int[] distFromDestination = end2start.distance;

        int nodesInShortestPath = 0;
        int min = distFromSource[endNode.number];

        for (Node node : maze) {
            if (distFromSource[node.number] != Integer.MAX_VALUE && distFromDestination[node.number] != Integer.MAX_VALUE) {
                System.out.printf("%d + %d == %d (%d).%n", distFromSource[node.number], distFromDestination[node.number], distFromSource[node.number] + distFromDestination[node.number], min);
            }
            if (distFromSource[node.number] + distFromDestination[node.number] + 1 == min) {

                System.out.printf("%d + %d + 1 == %d.%n",
                        distFromSource[node.number], distFromDestination[node.number], min);
                nodesInShortestPath++;

                if (node.number == startNode.number) {
                    System.out.println("This is start " + node.number);
                }
                if (node.number == endNode.number) {
                    System.out.println("This is end " + node.number);
                }
                if (node.number == end_1Node.number) {
                    System.out.println("This is end - 1 " + node.number);
                }
            }
        }

//        nodesInShortestPath += 2; //endNode and endNode -1
        System.out.printf("Part 1 - Sum %d%n", nodesInShortestPath);
    }

    record Visited(int x, int y, Direction direction){}

    static class Dijkstra {
        private final int totalNodes;
        private final int[] distance;
        private final Set<Visited> visited;
        private final PriorityQueue<Node> pQue;

        List<Node> adjacent;
        Node[] parents;
        ArrayList<Point> path;

        public Dijkstra(int totalNodes) {
            this.totalNodes = totalNodes;
            distance = new int[totalNodes];
            visited = new HashSet<>();
            pQue = new PriorityQueue<>(totalNodes, Node.COMPARATOR);
            parents = new Node[totalNodes];
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
                if (visited.contains(new Visited(ux.point.x, ux.point.y, ux.point.direction))) {
                    continue;
                }
                visited.add(new Visited(ux.point.x, ux.point.y, ux.point.direction));
                if (ux.cost < distance[ux.number]) {
                    distance[ux.number] = ux.cost;
                }
                processNeighbours(ux);
            }
        }

        private void processNeighbours(Node ux) {
            for (Node vx : findNeighbours(ux)) {
                if (!visited.contains(new Visited(vx.point.x, vx.point.y, vx.point.direction))) {
                    int newDist = ux.cost + vx.cost ;
                    parents[vx.number] = ux;
                    pQue.add(new Node(vx.number, newDist, vx.point));
                }
            }
        }

        private Set<Node> findNeighbours(Node ux) {
            Set<Node> neighbours = new HashSet<>();
            int noRotation = 0;
            int rotation = 1000;

            switch (ux.point.direction) {
                case UP -> {
                    neighbours.addAll(getNeighbour(ux, Direction.UP, noRotation));
                    neighbours.addAll(getNeighbour(ux, Direction.RIGHT, rotation));
                    neighbours.addAll(getNeighbour(ux, Direction.LEFT, rotation));
                    if (neighbours.isEmpty()) {
                        neighbours.addAll(getNeighbour(ux, Direction.DOWN, 2 * rotation));
                    }
                }
                case DOWN -> {
                    neighbours.addAll(getNeighbour(ux, Direction.DOWN, noRotation));
                    neighbours.addAll(getNeighbour(ux, Direction.RIGHT, rotation));
                    neighbours.addAll(getNeighbour(ux, Direction.LEFT, rotation));
                    if (neighbours.isEmpty()) {
                        neighbours.addAll(getNeighbour(ux, Direction.UP, 2 * rotation));
                    }
                }
                case LEFT -> {
                    neighbours.addAll(getNeighbour(ux, Direction.LEFT, noRotation));
                    neighbours.addAll(getNeighbour(ux, Direction.UP, rotation));
                    neighbours.addAll(getNeighbour(ux, Direction.DOWN, rotation));
                    if (neighbours.isEmpty()) {
                        neighbours.addAll(getNeighbour(ux, Direction.RIGHT, 2 * rotation));
                    }
                }
                case RIGHT -> {
                    neighbours.addAll(getNeighbour(ux, Direction.RIGHT, noRotation));
                    neighbours.addAll(getNeighbour(ux, Direction.UP, rotation));
                    neighbours.addAll(getNeighbour(ux, Direction.DOWN, rotation));
                    if (neighbours.isEmpty()) {
                        neighbours.addAll(getNeighbour(ux, Direction.LEFT, 2 * rotation));
                    }
                }
            }
            return neighbours;
        }

        private Set<Node> getNeighbour(Node ux, Direction direction, final int additionalCost) {
            int newX = ux.point.x + direction.mvX;
            int newY = ux.point.y + direction.mvY;
            return adjacent.stream()
                    .filter(n -> n.point.value != '#')
                    .filter(n -> n.point.x == newX)
                    .filter(n -> n.point.y == newY)
                    .map(n -> new Node(n.number, 1 + additionalCost, new Point(newX, newY, n.point.value, direction)))
                    .collect(Collectors.toSet());
        }

        void calculatePath(Node node) {
            path = new ArrayList<>(totalNodes);
            int startIndex = node.number;
            while (true) {
                Node tmp = parents[startIndex];
                if (tmp == null) {
                    break;
                }
                Point point = tmp.point;
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
        }

        void updateGrid(Node node, char[][] grid) {
            int startIndex = node.number;
            while (true) {
                Node tmp = parents[startIndex];
                if (tmp == null) {
                    break;
                }
                Point point = tmp.point;
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

        void printDistanceMap(char[][] grid, int digitInPoint, int digitInDistance) {
            String fileName = "distance.txt";
            try {
                Files.deleteIfExists(Paths.get(".", fileName));
            } catch (IOException e) {
                System.err.println("UPS! " + e.getMessage());
            }

            try (FileWriter fw = new FileWriter(fileName, true);
                 BufferedWriter bw = new BufferedWriter(fw)
            ) {
                String format = "(%"+digitInPoint+"d,%"+digitInPoint+"d)[%-"+digitInDistance+"s]\t";
                for (int y = 0; y < grid.length; y++) {
                    int yy = y;
                    for (int x = 0; x < grid[y].length; x++) {
                        int xx = x;
                        Node node = adjacent.stream().filter(n -> n.point.x == xx && n.point.y == yy).findFirst().get();

                        bw.write(format.formatted(node.point.x, node.point.y, distance[node.number] == Integer.MAX_VALUE ? "#" : distance[node.number]));
                    }
                    bw.newLine();
                }
            } catch (IOException e) {
                System.err.println("UPS! " + e.getMessage());
            }
        }
    }

    record Point(int x, int y, char value, Direction direction) {
        @Override
        public String toString() {
            return "{" + x + ", " + y + ", " + direction + "}";
        }
    }

    static class Node implements Comparator<Node> {

        static Comparator<Node> COMPARATOR = Comparator.comparingInt(n -> n.cost);

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

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;

            Node node = (Node) o;
            return number == node.number;
        }

        @Override
        public int hashCode() {
            return number;
        }
    }
}