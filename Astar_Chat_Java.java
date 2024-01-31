import java.util.*;

public class Astar_Chat_Java {

    static class Node implements Comparable<Node> {
        public final String name;
        public int gCost; // cost from start node
        public final int hCost; // heuristic cost
        public int fCost; // gCost + hCost
        public Edge[] adjacencies;
        public Node parent;

        public Node(String name, int hCost) {
            this.name = name;
            this.gCost = Integer.MAX_VALUE;
            this.hCost = hCost;
            this.fCost = Integer.MAX_VALUE;
        }

        @Override
        public int compareTo(Node other) {
            return Integer.compare(this.fCost, other.fCost);
        }
    }

    static class Edge {
        public final Node target;
        public final int cost;

        public Edge(Node target, int cost) {
            this.target = target;
            this.cost = cost;
        }
    }

    public static void aStarSearch(Node start, Node goal) {
        PriorityQueue<Node> closedList = new PriorityQueue<>();
        PriorityQueue<Node> openList = new PriorityQueue<>();

        start.gCost = 0;
        start.fCost = start.gCost + start.hCost;
        openList.add(start);

        while (!openList.isEmpty()) {
            Node current = openList.poll();
            if (current.name.equals(goal.name)) {
                System.out.println("Path found: " + printPath(goal));
                return;
            }

            closedList.add(current);

            for (Edge edge : current.adjacencies) {
                Node child = edge.target;
                int cost = current.gCost + edge.cost;
                int childEstimation = cost + child.hCost;

                if (cost < child.gCost && !closedList.contains(child)) {
                    child.parent = current;
                    child.gCost = cost;
                    child.fCost = childEstimation;

                    if (!openList.contains(child)) {
                        openList.add(child);
                    }
                }
            }
        }
        System.out.println("Path not found");
    }

    public static String printPath(Node target) {
        Node current = target;
        String path = current.name;
        while (current.parent != null) {
            path = current.parent.name + " -> " + path;
            current = current.parent;
        }
        return path;
    }

    public static void main(String[] args) {
        // Create nodes
        Node nodeA = new Node("A", 9);
        Node nodeB = new Node("B", 3);
        Node nodeC = new Node("C", 4);
        Node nodeD = new Node("D", 6);
        Node nodeE = new Node("E", 8);
        Node nodeF = new Node("F", 4);
        Node nodeG = new Node("G", 2);
        Node nodeH = new Node("H", 0);

        // Initialize edges
        nodeA.adjacencies = new Edge[]{new Edge(nodeB, 2), new Edge(nodeC, 10)};
        nodeB.adjacencies = new Edge[]{new Edge(nodeD, 3), new Edge(nodeA, 2)};
        nodeC.adjacencies = new Edge[]{new Edge(nodeA, 10), new Edge(nodeD, 2), new Edge(nodeG, 5)};
        nodeD.adjacencies = new Edge[]{new Edge(nodeB, 3), new Edge(nodeC, 2), new Edge(nodeE, 18), new Edge(nodeF, 4)};
        nodeE.adjacencies = new Edge[]{new Edge(nodeD, 18), new Edge(nodeH, 10)};
        nodeF.adjacencies = new Edge[]{new Edge(nodeD, 4), new Edge(nodeG, 5)};
        nodeG.adjacencies = new Edge[]{new Edge(nodeF, 5), new Edge(nodeC, 5), new Edge(nodeH, 2)};
        nodeH.adjacencies = new Edge[]{new Edge(nodeE, 10), new Edge(nodeG, 2)};

        // Start A* search
        aStarSearch(nodeA, nodeH);
    }
}