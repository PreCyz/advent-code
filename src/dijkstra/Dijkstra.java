package dijkstra;

import java.util.*;

public class Dijkstra {

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
        pQue = new PriorityQueue<>(totalNodes, new Node());
    }

    public void dijkstra(List<List<Node>> adjacent, int s) {
        this.adjacent = adjacent;

        for (int j = 0; j < totalNodes; j++) {
            // initializing the distance of every node to infinity (a large number)
            distance[j] = Integer.MAX_VALUE;
        }

        // Adding the source node to pQue
        pQue.add(new Node(s, 0));

        // distance of the source is always zero
        distance[s] = 0;

        while (settled.size() != totalNodes) {

            // Terminating condition check when
            // the priority queue contains zero elements, return
            if (pQue.isEmpty()) {
                return;
            }

            // Deleting the node that has the minimum distance from the priority queue
            int ux = pQue.remove().n;

            // Adding the node whose distance is
            // confirmed
            if (settled.contains(ux)) {
                continue;
            }

            // We don't have to call eNeighbors(ux)
            // if ux is already present in the settled set.
            settled.add(ux);

            eNeighbours(ux);
        }
    }

    private void eNeighbours(int ux) {

        int edgeDist = -1;
        int newDist = -1;

        // All of the neighbors of vx
        for (int j = 0; j < adjacent.get(ux).size(); j++) {
            Node vx = adjacent.get(ux).get(j);

            // If the current node hasn't been already processed
            if (!settled.contains(vx.n)) {
                edgeDist = vx.price;
                newDist = distance[ux] + edgeDist;

                // If the new distance is lesser in the cost
                if (newDist < distance[vx.n]) {
                    distance[vx.n] = newDist;
                }

                // Adding the current node to the priority queue pQue
                pQue.add(new Node(vx.n, distance[vx.n]));
            }
        }
    }

    // Main method
    public static void main(String[] argvs) {

        int totalNodes = 10;
        int s = 1;

        // representation of the connected edges
        // using the adjacency list
        // by declaration of the List class object

        // Declaring and object of the type List<Node>
        List<List<Node>> adjacent = new ArrayList<>();

        // Initialize list for every node
        for (int i = 0; i < totalNodes; i++) {
            List<Node> itm = new ArrayList<>();
            adjacent.add(itm);
        }

        // adding the edges
        // The statement adjacent.get(0).add(new Node(1, 3)); means
        // to travel from node 0 to 1, one has to cover 3 units of distance
        // it does not mean one has to travel from 1 to 0
        // To travel from 1 to 0, we have to add the statement
        // adjacent.get(1).add(new Node(0, 3));
        // Note that the distance is the same i.e., 3 units in both the cases.
        // Similarly, we have added other edges too.

        adjacent.get(0).add(new Node(3, 13, "DD"));
        adjacent.get(0).add(new Node(8, 0, "II"));
        adjacent.get(0).add(new Node(1, 13, "BB"));
        adjacent.get(1).add(new Node(2, 2, "CC"));
        adjacent.get(1).add(new Node(0, 0, "AA"));
        adjacent.get(2).add(new Node(3, 20, "DD"));
        adjacent.get(2).add(new Node(1, 13, "BB"));
        adjacent.get(3).add(new Node(2, 2, "CC"));
        adjacent.get(3).add(new Node(0, 0, "AA"));
        adjacent.get(3).add(new Node(4, 3, "EE"));
        adjacent.get(4).add(new Node(5, 0, "FF"));
        adjacent.get(4).add(new Node(4, 20, "DD"));
        adjacent.get(5).add(new Node(4, 3, "EE"));
        adjacent.get(5).add(new Node(6, 0, "GG"));
        adjacent.get(6).add(new Node(5, 0, "FF"));
        adjacent.get(6).add(new Node(7, 22, "HH"));
        adjacent.get(7).add(new Node(6, 0, "GG"));
        adjacent.get(8).add(new Node(0, 0, "AA"));
        adjacent.get(8).add(new Node(9, 21, "JJ"));
        adjacent.get(9).add(new Node(8, 0, "II"));

        // creating an object of the class DijkstraExample1
        Dijkstra obj = new Dijkstra(totalNodes);
        obj.dijkstra(adjacent, s);

        // Printing the shortest path to all the nodes
        // from the source node
        System.out.println("The shortest path from the node :");

        for (int j = 0; j < obj.distance.length; j++) {
            System.out.println(s + " to " + j + " is " + obj.distance[j]);
        }
    }
}
