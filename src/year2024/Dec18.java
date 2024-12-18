package year2024;

import base.DecBase;

import java.util.*;
import java.util.stream.Stream;

class Dec18 extends DecBase {

    public Dec18(int year) {
        super(year, 18);
    }

    record Point(int x, int y) {}

    @Override
    public Dec18 readDefaultInput() {
        System.out.println("Reading default input.");
        inputStrings = new LinkedList<>(Stream.of(
                "5,4",
                "4,2",
                "4,5",
                "3,0",
                "2,1",
                "6,3",
                "2,4",
                "1,5",
                "0,6",
                "3,3",
                "2,6",
                "5,1",
                "1,2",
                "5,5",
                "2,5",
                "6,5",
                "1,4",
                "0,4",
                "6,4",
                "1,1",
                "6,1",
                "1,0",
                "0,5",
                "1,6",
                "2,0"
        ).toList());
        return this;
    }

    @Override
    protected void calculatePart1() {
        int gridWidth = 71;
        int gridHeight = 71;

        char[][] grid = new char[gridHeight][gridWidth];
        for (int y = 0; y < gridWidth; y++) {
            for (int x = 0; x < gridHeight; x++) {
                grid[y][x] = '.';
            }
        }
        ArrayList<Point> bytes = new ArrayList<>(inputStrings.size());
        for (String input : inputStrings) {
            String[] split = input.split(",");
            bytes.add(new Point(Integer.parseInt(split[0]),Integer.parseInt(split[1])));
        }

        int MAX_BYTES = 1024;
        for (int i = 0; i < MAX_BYTES; i++) {
            Point point = bytes.get(i);
            grid[point.y][point.x] = '#';
        }

        Node startNode = null, destinationNode = null;
        List<List<Node>> nodes = new ArrayList<>();
        int nodeNumber = 0;

        for (int y = 0; y < grid.length; y++) {
            ArrayList<Node> row = new ArrayList<>();
            for (int x = 0; x < grid[y].length; x++) {
                int price = 1;
                if (grid[y][x] == '#') {
                    price = 1000;
                }
                Node node = new Node(nodeNumber++, price, new Point(x, y));
                if (nodeNumber - 1 == 0) {
                    startNode = node;
                } else if (nodeNumber - 1 ==gridWidth * gridHeight - 1) {
                    destinationNode = node;
                }
                row.add(node);
            }
            nodes.add(row);
        }

        Dijkstra dijkstra = new Dijkstra(gridWidth * gridHeight);
        dijkstra.dijkstra(nodes, startNode);
        int distance = dijkstra.getDistance(destinationNode);

        System.out.printf("Part 1 - Sum %s%n", distance);
    }

    @Override
    protected void calculatePart2() {
        int gridWidth = 71;
        int gridHeight = 71;

        char[][] grid = new char[gridHeight][gridWidth];
        for (int y = 0; y < gridWidth; y++) {
            for (int x = 0; x < gridHeight; x++) {
                grid[y][x] = '.';
            }
        }
        ArrayList<Point> bytes = new ArrayList<>(inputStrings.size());
        for (String input : inputStrings) {
            String[] split = input.split(",");
            bytes.add(new Point(Integer.parseInt(split[0]),Integer.parseInt(split[1])));
        }

        Map<Point, Integer> map = new LinkedHashMap<>();

        int MAX_BYTES = 1024;
        int wallPrice = 1000;
        for (int j = 0; j < inputStrings.size() - MAX_BYTES; j++) {
            Point point = new Point(0, 0);
            for (int i = 0; i < MAX_BYTES + j; i++) {
                point = bytes.get(i);
                grid[point.y][point.x] = '#';
            }

            Node startNode = null, destinationNode = null;
            List<List<Node>> nodes = new ArrayList<>();
            int nodeNumber = 0;

            for (int y = 0; y < grid.length; y++) {
                ArrayList<Node> row = new ArrayList<>();
                for (int x = 0; x < grid[y].length; x++) {
                    int price = 1;
                    if (grid[y][x] == '#') {
                        price = wallPrice;
                    }
                    Node node = new Node(nodeNumber++, price, new Point(x, y));
                    if (nodeNumber - 1 == 0) {
                        startNode = node;
                    } else if (nodeNumber - 1 == gridWidth * gridHeight - 1) {
                        destinationNode = node;
                    }
                    row.add(node);
                }
                nodes.add(row);
            }

            Dijkstra dijkstra = new Dijkstra(gridWidth * gridHeight);
            dijkstra.dijkstra(nodes, startNode);
            int distance = dijkstra.getDistance(destinationNode);

            //432 is a distance found in part 1
            if (distance > wallPrice + 432) {
                System.out.printf("Point %d added: %s%n", (MAX_BYTES + j), point);
                System.out.printf("Distance %d%n", distance);
                break;
            }
        }

        System.out.printf("Part 2%n");
    }

    static class Node implements Comparator<Node> {

        int number;
        int price;
        Point point;

        Node() {}

        Node(int number, int price) {
            this(number, price, new Point(0, 0));
        }

        Node(int number, int price, Point point) {
            this.number = number;
            this.price = price;
            this.point = point;
        }

        @Override
        public int compare(Node n1, Node n2) {
            if (n1.price < n2.price) {
                return -1;
            } else if (n1.price > n2.price) {
                return 1;
            }
            return 0;
        }

        static Comparator<Node> COMPARATOR = Comparator.comparingInt(o -> o.price);
    }

    static class Dijkstra {

        // Member variables of the class
        private final int[] distance;
        private final Set<Integer> settled;
        private final PriorityQueue<Node> pQue;

        // Total count of the vertices
        private final int totalNodes;
        List<List<Node>> adjacent;

        // Constructor of the class
        public Dijkstra(int totalNodes) {
            this.totalNodes = totalNodes;
            distance = new int[totalNodes];
            settled = new HashSet<>();
            pQue = new PriorityQueue<>(totalNodes, Node.COMPARATOR);
        }

        public void dijkstra(List<List<Node>> adjacent, Node startNode) {
            this.adjacent = adjacent;

            for (int j = 0; j < totalNodes; j++) {
                // initializing the distance of every node to infinity (a large number)
                distance[j] = Integer.MAX_VALUE;
            }

            // Adding the source node to pQue
            pQue.add(startNode);

            // distance of the source is always zero
            distance[startNode.number] = 0;

            while (settled.size() != totalNodes) {

                // Terminating condition check when
                // the priority queue contains zero elements, return
                if (pQue.isEmpty()) {
                    return;
                }

                // Deleting the node that has the minimum distance from the priority queue
                Node ux = pQue.remove();

                // Adding the node whose distance is
                // confirmed
                if (settled.contains(ux.number)) {
                    continue;
                }

                // We don't have to call eNeighbors(ux)
                // if ux is already present in the settled set.
                settled.add(ux.number);

                eNeighbours(ux);
            }
        }

        private void eNeighbours(Node ux) {
            int edgeDist = -1;
            int newDist = -1;

            // All neighbors of vx
            for (Node vx : findNeighbours(ux)) {
                // If the current node hasn't been already processed
                if (!settled.contains(vx.number)) {
                    edgeDist = vx.price;
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
            return adjacent.stream()
                    .flatMap(List::stream)
                    .filter(n -> (Math.abs(n.point.x - ux.point.x) == 1 && n.point.y == ux.point.y)
                            || (Math.abs(n.point.y - ux.point.y) == 1 && n.point.x == ux.point.x)
                    )
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