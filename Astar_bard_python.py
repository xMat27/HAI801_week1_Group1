import math

def a_star(graph, start, end):
  """
  Trouve le chemin le plus court entre deux points dans un graphe pondéré.

  Args:
    graph: Le graphe, représenté comme un dictionnaire. Les clés du dictionnaire sont les sommets du graphe, et les valeurs sont des dictionnaires contenant les voisins de chaque sommet, ainsi que les poids des arcs les reliant.
    start: Le sommet de départ.
    end: Le sommet d'arrivée.

  Returns:
    Une liste des sommets à suivre pour atteindre la destination.
  """

  # Créer une liste des noeuds ouverts, qui contient tous les noeuds qui n'ont pas encore été visités.
  open_nodes = []

  # Ajouter le noeud de départ à la liste des noeuds ouverts.
  open_nodes.append((start, 0, 0))

  # Tant que la liste des noeuds ouverts n'est pas vide :
  while open_nodes:
    # Extraire le noeud avec la plus petite estimation du coût total.
    current_node, current_cost, current_heuristic = open_nodes.pop(0)

    # Si le noeud courant est la destination, alors nous avons trouvé le chemin !
    if current_node == end:
      return reconstruct_path(current_node)

    # Pour chaque voisin du noeud courant :
    for neighbor in graph[current_node]:
      # Si le voisin n'a pas été visité :
      if neighbor not in open_nodes and neighbor not in closed_nodes:
        # Calculer l'estimation du coût total du voisin.
        new_cost = current_cost + graph[current_node][neighbor] + heuristic(neighbor, end)

        # Ajouter le voisin à la liste des noeuds ouverts.
        open_nodes.append((neighbor, new_cost, new_cost + heuristic(neighbor, end)))

  # Si nous arrivons ici, c'est que le graphe n'a pas de solution.
  return None


def heuristic(node, end):
  """
  Calcule l'estimation du coût total du noeud donné.

  Args:
    node: Le noeud dont on veut calculer l'estimation du coût total.
    end: Le sommet d'arrivée.

  Returns:
    La distance euclidienne entre le noeud et la destination.
  """

  return (node[0] - end[0]) ** 2 + (node[1] - end[1]) ** 2


def reconstruct_path(node):
  """
  Reconstruit le chemin à partir du noeud donné.

  Args:
    node: Le dernier noeud du chemin.

  Returns:
    Une liste des sommets du chemin.
  """

  path = []
  while node:
    path.append(node)
    node = node[0]

  return path[::-1]


print(a_star(graph, "A", "H"))
