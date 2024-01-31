#include <iostream>
#include <vector>
#include <queue>
#include <map>
#include <climits>
#include <algorithm>
using namespace std;

struct Node {
    char id;
    int heuristic;
    vector<pair<Node*, int>> neighbors; // Pair of neighbor node and cost
    Node(char id, int heuristic) : id(id), heuristic(heuristic) {}
};

struct NodeComparator {
    bool operator()(const pair<Node*, int>& n1, const pair<Node*, int>& n2) {
        // Compare nodes by f(n) = g(n) + h(n)
        return (n1.second + n1.first->heuristic) > (n2.second + n2.first->heuristic);
    }
};

void AStar(Node* start, Node* goal) {
    priority_queue<pair<Node*, int>, vector<pair<Node*, int>>, NodeComparator> frontier;
    map<Node*, Node*> came_from;
    map<Node*, int> cost_so_far;
    
    frontier.push(make_pair(start, 0));
    came_from[start] = start;
    cost_so_far[start] = 0;

    while (!frontier.empty()) {
        auto current = frontier.top().first;
        frontier.pop();

        if (current == goal) {
            break; // Goal reached
        }

        for (auto& next : current->neighbors) {
            int new_cost = cost_so_far[current] + next.second;
            if (cost_so_far.find(next.first) == cost_so_far.end() || new_cost < cost_so_far[next.first]) {
                cost_so_far[next.first] = new_cost;
                int priority = new_cost + next.first->heuristic;
                frontier.push(make_pair(next.first, priority));
                came_from[next.first] = current;
            }
        }
    }

    // If goal is found, reconstruct path
    if (came_from.find(goal) != came_from.end()) {
        vector<Node*> path;
        Node* current = goal;
        while (current != start) {
            path.push_back(current);
            current = came_from[current];
        }
        path.push_back(start);
        reverse(path.begin(), path.end());

        cout << "Path found: ";
        for (auto node : path) {
            cout << node->id << " ";
        }
        cout << "\nTotal Cost: " << cost_so_far[goal] << endl;
    } else {
        cout << "No path found." << endl;
    }
}

int main() {
    // Create nodes with heuristic values
    Node a('A', 9), b('B', 3), c('C', 4), d('D', 6), e('E', 8), f('F', 4), g('G', 2), h('H', 0);

    // Create graph
    a.neighbors = {{&b, 2}, {&c, 10},{&d, 3}};
    b.neighbors = {{&a, 2}, {&e, 18}};
    c.neighbors = {{&a, 10}, {&d, 2}, {&g, 2}};
    d.neighbors = {{&a, 3},{&c, 2}, {&f, 4}};
    e.neighbors = {{&b, 18}, {&f, 5}, {&h, 10}};
    f.neighbors = {{&d, 4}, {&g, 5}, {&e, 5}};
    g.neighbors = {{&c, 2}, {&f, 5}, {&h, 2}};
    h.neighbors = {{&e, 10}, {&g, 2}};

    AStar(&a, &h); // Find path from A to H

    return 0;
}
