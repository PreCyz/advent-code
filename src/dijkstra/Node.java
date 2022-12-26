package dijkstra;

import java.util.Comparator;

/** The Node class implementing the Comparator interface
 *  The object of this class represents a node of the graph
*/
class Node implements Comparator<Node> {

    public int n;
    public int price;
    public String name;

// Constructors of this class

    // Constructor 1
    public Node() {}

    // Constructor 2
    public Node(int n, int price) {
        this(n, price, "");
    }

    public Node(int n, int price, String name) {
        this.n = n;
        this.price = price;
        this.name = name;
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
}
