import java.util.*;

class Node implements Comparable<Node> {
    String name;
    double heuristic;
    double g = Double.POSITIVE_INFINITY; // Distance from start node
    double f = Double.POSITIVE_INFINITY; // Total estimated cost
    Map<Node, Double> adjacent = new HashMap<>(); // Adjacent nodes and their costs
    Node parent = null; // Parent node in the path

    public Node(String name, double heuristic) {
        this.name = name;
        this.heuristic = heuristic;
    }

    public void addEdge(Node neighbor, double cost) {
        this.adjacent.put(neighbor, cost);
    }

    @Override
    public int compareTo(Node other) {
        return Double.compare(this.f, other.f);
    }
}

public class AstarChatPython_2_Java {
    public static List<String> astar(Map<String, Node> graph, String start, String end) {
        Node startNode = graph.get(start);
        startNode.g = 0;
        startNode.f = startNode.heuristic;

        PriorityQueue<Node> openList = new PriorityQueue<>();
        openList.add(startNode);
        Set<Node> closedSet = new HashSet<>();

        while (!openList.isEmpty()) {
            Node currentNode = openList.poll();
            closedSet.add(currentNode);

            if (currentNode.name.equals(end)) {
                List<String> path = new ArrayList<>();
                while (currentNode != null) {
                    path.add(currentNode.name);
                    currentNode = currentNode.parent;
                }
                Collections.reverse(path);
                return path;
            }

            for (Map.Entry<Node, Double> adj : currentNode.adjacent.entrySet()) {
                Node childNode = adj.getKey();
                if (closedSet.contains(childNode)) {
                    continue;
                }

                double tentativeGScore = currentNode.g + adj.getValue();

                if (tentativeGScore < childNode.g) {
                    childNode.parent = currentNode;
                    childNode.g = tentativeGScore;
                    childNode.f = tentativeGScore + childNode.heuristic;

                    if (!openList.contains(childNode)) {
                        openList.add(childNode);
                    }
                }
            }
        }
        return null; // No path found
    }

    public static void main(String[] args) {
        Map<String, Node> nodes = new HashMap<>();
        Map<String, Double> heuristics = Map.of(
            "A", 9.0, "B", 3.0, "C", 4.0, "D", 6.0, 
            "E", 8.0, "F", 4.0, "G", 2.0, "H", 0.0
        );

        heuristics.forEach((name, heuristic) -> nodes.put(name, new Node(name, heuristic)));

        List<String[]> edges = List.of(
            new String[]{"A", "B", "2"},
            new String[]{"A", "C", "10"},
            new String[]{"A", "D", "3"},
            new String[]{"B", "E", "18"},
            new String[]{"D", "C", "2"},
            new String[]{"D", "F", "4"},
            new String[]{"E", "H", "10"},
            new String[]{"F", "E", "5"},
            new String[]{"F", "G", "5"},
            new String[]{"C", "G", "2"},
            new String[]{"G", "H", "2"}
        );

        for (String[] edge : edges) {
            nodes.get(edge[0]).addEdge(nodes.get(edge[1]), Double.parseDouble(edge[2]));
        }

        List<String> path = astar(nodes, "A", "H");
        System.out.println("Path found by A*: " + path);
    }
}
