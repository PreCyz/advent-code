package dijkstra;

import java.util.Comparator;

/** The Node class implementing the Comparator interface
 *  The object of this class represents a node of the graph
*/
public class Node implements Comparator<Node> {

    public int number;
    public int price;
    public String name;

// Constructors of this class

    // Constructor 1
    public Node() {}

    // Constructor 2
    public Node(int number, int price) {
        this(number, price, "");
    }

    public Node(int number, int price, String name) {
        this.number = number;
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
