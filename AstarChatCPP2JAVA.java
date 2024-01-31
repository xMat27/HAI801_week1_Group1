import java.util.*;

class Node {
    char id;
    int heuristic;
    List<Map.Entry<Node, Integer>> neighbors; // Entry of neighbor node and cost

    Node(char id, int heuristic) {
        this.id = id;
        this.heuristic = heuristic;
        this.neighbors = new ArrayList<>();
    }
}

class NodeComparator implements Comparator<Map.Entry<Node, Integer>> {
    @Override
    public int compare(Map.Entry<Node, Integer> n1, Map.Entry<Node, Integer> n2) {
        // Compare nodes by f(n) = g(n) + h(n)
        return (n1.getValue() + n1.getKey().heuristic) - (n2.getValue() + n2.getKey().heuristic);
    }
}

public class AstarCPP2JAVA {
    public static void AStar(Node start, Node goal) {
        PriorityQueue<Map.Entry<Node, Integer>> frontier = new PriorityQueue<>(new NodeComparator());
        Map<Node, Node> came_from = new HashMap<>();
        Map<Node, Integer> cost_so_far = new HashMap<>();

        frontier.add(new AbstractMap.SimpleEntry<>(start, 0));
        came_from.put(start, start);
        cost_so_far.put(start, 0);

        while (!frontier.isEmpty()) {
            Map.Entry<Node, Integer> currentEntry = frontier.poll();
            Node current = currentEntry.getKey();

            if (current == goal) {
                break; // Goal reached
            }

            for (Map.Entry<Node, Integer> nextEntry : current.neighbors) {
                Node next = nextEntry.getKey();
                int new_cost = cost_so_far.get(current) + nextEntry.getValue();
                if (!cost_so_far.containsKey(next) || new_cost < cost_so_far.get(next)) {
                    cost_so_far.put(next, new_cost);
                    int priority = new_cost + next.heuristic;
                    frontier.add(new AbstractMap.SimpleEntry<>(next, priority));
                    came_from.put(next, current);
                }
            }
        }

        // If goal is found, reconstruct path
        if (came_from.containsKey(goal)) {
            List<Node> path = new ArrayList<>();
            Node current = goal;
            while (current != start) {
                path.add(current);
                current = came_from.get(current);
            }
            path.add(start);
            Collections.reverse(path);

            System.out.print("Path found: ");
            for (Node node : path) {
                System.out.print(node.id + " ");
            }
            System.out.println("\nTotal Cost: " + cost_so_far.get(goal));
        } else {
            System.out.println("No path found.");
        }
    }

    public static void main(String[] args) {
        // Create nodes with heuristic values
        Node a = new Node('A', 9);
        Node b = new Node('B', 3);
        Node c = new Node('C', 4);
        Node d = new Node('D', 6);
        Node e = new Node('E', 8);
        Node f = new Node('F', 4);
        Node g = new Node('G', 2);
        Node h = new Node('H', 0);

        // Create graph
        a.neighbors = Arrays.asList(new AbstractMap.SimpleEntry<>(b, 2), new AbstractMap.SimpleEntry<>(c, 10), new AbstractMap.SimpleEntry<>(d, 3));
        b.neighbors = Arrays.asList(new AbstractMap.SimpleEntry<>(a, 2), new AbstractMap.SimpleEntry<>(e, 18));
        c.neighbors = Arrays.asList(new AbstractMap.SimpleEntry<>(a, 10), new AbstractMap.SimpleEntry<>(d, 2), new AbstractMap.SimpleEntry<>(g, 2));
        d.neighbors = Arrays.asList(new AbstractMap.SimpleEntry<>(a, 3), new AbstractMap.SimpleEntry<>(c, 2), new AbstractMap.SimpleEntry<>(f, 4));
        e.neighbors = Arrays.asList(new AbstractMap.SimpleEntry<>(b, 18), new AbstractMap.SimpleEntry<>(f, 5), new AbstractMap.SimpleEntry<>(h, 10));
        f.neighbors = Arrays.asList(new AbstractMap.SimpleEntry<>(d, 4), new AbstractMap.SimpleEntry<>(g, 5), new AbstractMap.SimpleEntry<>(e, 5));
        g.neighbors = Arrays.asList(new AbstractMap.SimpleEntry<>(c, 2), new AbstractMap.SimpleEntry<>(f, 5), new AbstractMap.SimpleEntry<>(h, 2));
        h.neighbors = Arrays.asList(new AbstractMap.SimpleEntry<>(e, 10), new AbstractMap.SimpleEntry<>(g, 2));

        AStar(a, h); // Find path from A to H
    }
}
