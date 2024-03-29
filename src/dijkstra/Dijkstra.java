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

    public void dijkstra(List<List<Node>> adjacent, int startNodeNumber) {
        this.adjacent = adjacent;

        for (int j = 0; j < totalNodes; j++) {
            // initializing the distance of every node to infinity (a large number)
            distance[j] = Integer.MAX_VALUE;
        }

        // Adding the source node to pQue
        pQue.add(new Node(startNodeNumber, 0));

        // distance of the source is always zero
        distance[startNodeNumber] = 0;

        while (settled.size() != totalNodes) {

            // Terminating condition check when
            // the priority queue contains zero elements, return
            if (pQue.isEmpty()) {
                return;
            }

            // Deleting the node that has the minimum distance from the priority queue
            int ux = pQue.remove().number;

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

        // All neighbors of vx
        for (int j = 0; j < adjacent.get(ux).size(); j++) {
            Node vx = adjacent.get(ux).get(j);

            // If the current node hasn't been already processed
            if (!settled.contains(vx.number)) {
                edgeDist = vx.price;
                newDist = distance[ux] + edgeDist;

                // If the new distance is lesser in the cost
                if (newDist < distance[vx.number]) {
                    distance[vx.number] = newDist;
                }

                // Adding the current node to the priority queue pQue
                pQue.add(new Node(vx.number, distance[vx.number]));
            }
        }
    }

    public void print(int startNodeNumber) {
        for (int j = 0; j < distance.length; j++) {
            System.out.printf("%d to %d is %d%n", startNodeNumber, j, distance[j]);
        }
    }

    public int getDistance(int destinationNodeNumber) {
        return distance[destinationNodeNumber];
    }
}
