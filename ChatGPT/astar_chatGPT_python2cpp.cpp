#include <iostream>
#include <queue>
#include <unordered_map>
#include <vector>
#include <limits>
#include <functional>
#include <string>
#include <algorithm> 
class Node {
public:
    std::string name;
    double heuristic;
    double g;
    double f;
    std::unordered_map<Node*, double> adjacent;
    Node* parent;

    Node(std::string name, double heuristic)
        : name(name), heuristic(heuristic), g(std::numeric_limits<double>::infinity()),
          f(std::numeric_limits<double>::infinity()), parent(nullptr) {}

    void add_edge(Node* neighbor, double cost) {
        adjacent[neighbor] = cost;
    }

    // The comparison operator for priority queue
    bool operator>(const Node& other) const {
        return f > other.f;
    }
};

std::vector<std::string> astar(std::unordered_map<std::string, Node*>& graph, std::string start, std::string end) {
    auto cmp = [](Node* left, Node* right) { return *left > *right; };
    std::priority_queue<Node*, std::vector<Node*>, decltype(cmp)> open_list(cmp);
    std::unordered_map<std::string, Node*> closed_set;

    Node* start_node = graph[start];
    start_node->g = 0;
    start_node->f = start_node->heuristic;

    open_list.push(start_node);

    while (!open_list.empty()) {
        Node* current_node = open_list.top();
        open_list.pop();

        if (current_node->name == end) {
            std::vector<std::string> path;
            while (current_node != nullptr) {
                path.push_back(current_node->name);
                current_node = current_node->parent;
            }
            std::reverse(path.begin(), path.end());
            return path;
        }

        closed_set[current_node->name] = current_node;

        for (auto& adj_pair : current_node->adjacent) {
            Node* child_node = adj_pair.first;
            double cost = adj_pair.second;

            if (closed_set.find(child_node->name) != closed_set.end()) {
                continue;
            }

            double tentative_g_score = current_node->g + cost;

            if (tentative_g_score < child_node->g) {
                child_node->parent = current_node;
                child_node->g = tentative_g_score;
                child_node->f = tentative_g_score + child_node->heuristic;

                // Since std::priority_queue does not support decrease-key operation,
                // we push the updated node anyway. The closed set will ensure we don't process a node twice.
                open_list.push(child_node);
            }
        }
    }

    return {}; // Return an empty path if no path is found
}

int main() {
    std::unordered_map<std::string, Node*> nodes;
    std::vector<std::string> node_names = {"A", "B", "C", "D", "E", "F", "G", "H"};
    std::unordered_map<std::string, double> heuristics = {{"A", 9}, {"B", 3}, {"C", 5}, {"D", 6}, {"E", 8}, {"F", 4}, {"G", 2}, {"H", 0}};

    for (const auto& name : node_names) {
        nodes[name] = new Node(name, heuristics[name]);
    }

    std::vector<std::tuple<std::string, std::string, double>> edges = {
        {"A", "B", 2}, {"A", "C", 10}, {"A", "D", 3}, {"B", "E", 18}, {"C", "D", 2},
        {"D", "C", 2}, {"D", "E", 5}, {"D", "F", 4}, {"E", "H", 10}, {"F", "E", 5},
        {"F", "G", 5}, {"G", "H", 2}
    };

    for (const auto& edge : edges) {
        nodes[std::get<0>(edge)]->add_edge(nodes[std::get<1>(edge)], std::get<2>(edge));
    }

    auto path = astar(nodes, "A", "H");
    std::cout << "Path found by A*:";
    for (const auto& p : path) {
        std::cout << " " << p;
    }
    std::cout << std::endl;

    // Clean up dynamically allocated nodes
    for (const auto& pair : nodes) {
        delete pair.second;
    }

    return 0;
}
