package game_advanced.pathfinding;

import java.io.Serializable;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;


/**
 * This class can be used to find a path between two nodes in a given two-dimensional list of
 * nodes.
 *
 * Adapted from https://github.com/BlueWalker/Pathfinding
 */
public class PathFinder implements Serializable {

  private List<List<game_advanced.pathfinding.Node>> map;

  public PathFinder(List<List<game_advanced.pathfinding.Node>> map) {
    this.map = map;
  }

  public List<List<game_advanced.pathfinding.Node>> getMap() {
    return map;
  }

  /**
   * Returns a List representing the path between two nodes in a two-dimensional search space.
   *
   * @param start the start node for the path search and must inherit from Node
   * @param dest the destination node for the path search and must inherit from Node
   * @return the List of nodes in the path starting from the start node and ending with the dest
   * node
   */
  public List<game_advanced.pathfinding.Node> findPath(
      game_advanced.pathfinding.Node start, game_advanced.pathfinding.Node dest) {
    HashSet<game_advanced.pathfinding.Node> closedSet = new HashSet<>();
    PriorityQueue<game_advanced.pathfinding.Node> openQueue = new PriorityQueue<>();

    start.setParent(null);
    start.setG(0);
    openQueue.add(start);

    // Run while the open list is not empty (if it is, then the destination was never found)
    // and while the open list does not contain the destination node (once it has the
    // destination node the path has been found).
    while (!openQueue.isEmpty()) {

      game_advanced.pathfinding.Node node = openQueue.remove();

      // If the destination node has been reached, then return the reconstructed path.
      if (node == dest) {
        return reconstructPath(start, node);
      }

      closedSet.add(node);

      // Find neighbors in the 3D array
      for (int i = -1; i < 2; i++) {
        for (int j = -1; j < 2; j++) {
          if (i != 0 || j != 0) {
            try {
              game_advanced.pathfinding.Node neighbor =
                  map.get(node.getY() + i).
                      get(node.getX() + j);

              // If the neighbor can be walked through and has not been visited
              // directly, then check to see if the neighbor's values can be updated.
              if (!closedSet.contains(neighbor) && !neighbor.isBlocked()) {
                // If the neighbor has not been added to the priority queue,
                // then set its parent node to null and its G value to "infinity".
                if (!openQueue.contains(neighbor)) {
                  neighbor.setParent(null);
                  neighbor.setG(Integer.MAX_VALUE);
                  neighbor.setH(distanceBetweenNodes(neighbor, dest));
                }

                double oldG = neighbor.getG();
                // Determine which of the two paths are the best option
                computeBestPath(node, neighbor);
                if (neighbor.getG() < oldG) {
                  // If the neighbor is in the open queue, then remove it and
                  // add it again, so that it can be sorted in the right place,
                  // instead of having to sort the whole queue again.
                  openQueue.remove(neighbor);
                  openQueue.add(neighbor);
                }
              }
            } // try
            catch (IndexOutOfBoundsException e) {
            }
          } // if(i != 0 || j != 0)
        } // j
      } // i
    }
    return null;
  }

  /**
   * Determines whether to set a neighbor's parent to the node or to the node's parent.
   *
   * @param node the current node being visited in the path search
   * @param neighbor a neighbor of node
   */
  private void computeBestPath(game_advanced.pathfinding.Node node,
      game_advanced.pathfinding.Node neighbor) {
    if (node.getParent() != null && lineOfSight(node.getParent(), neighbor)) {
      int parentNeighborDistance = distanceBetweenNodes(node.getParent(), neighbor);

      if (node.getParent().getG() + parentNeighborDistance < neighbor.getG()) {
        neighbor.setParent(node.getParent());
        neighbor.setG(node.getParent().getG() + parentNeighborDistance);
      }
    } else {
      int nodeNeighborDistance = distanceBetweenNodes(node, neighbor);
      if (node.getG() + nodeNeighborDistance < neighbor.getG()) {
        neighbor.setParent(node);
        neighbor.setG(node.getG() + nodeNeighborDistance);
      }
    }
  }

  /**
   * Returns true if the two nodes are within line of sight of one another, false otherwise.
   *
   * @param a first node
   * @param b second node
   * @return Returns a boolean for whether or not the two nodes are in line of sight of one another.
   */
  private boolean lineOfSight(game_advanced.pathfinding.Node a, game_advanced.pathfinding.Node b) {
    int xA = a.getX();
    int yA = a.getY();
    int xB = b.getX();
    int yB = b.getY();

    int rise = yB - yA;
    int run = xB - xA;

    if (run == 0) {
      if (yB < yA) {
        int temp = yB;
        yB = yA;
        yA = temp;
      }
      for (int y = yA; y < yB + 1; y++) {
        if (map.get(y).get(xA).isBlocked()) {
          return false;
        }
      }
    } else {
      float slope = (float) rise / run;
      int adjust = 1;
      if (slope < 0) {
        adjust = -1;
      }
      int offset = 0;
      // For when run is greater than rise, else when rise is greater than run.
      if (slope <= 1 && slope >= -1) {
        int delta = Math.abs(rise) * 2;
        int threshold = Math.abs(run);
        int thresholdInc = Math.abs(run) * 2;
        int y = yA;
        // Used to swap endpoints so that the increment is always in the same direction.
        if (xB < xA) {
          int temp = xB;
          xB = xA;
          xA = temp;
          y = yB;
        }
        for (int x = xA; x < xB; x++) {
          if (map.get(y).get(x).isBlocked()) {
            return false;
          }
          offset += delta;
          if (offset >= threshold) {
            y += adjust;
            threshold += thresholdInc;
          }
        }
      } else {
        int delta = Math.abs(run) * 2;
        int threshold = Math.abs(rise);
        int thresholdInc = Math.abs(rise) * 2;
        int x = xA;
        if (yB < yA) {
          int temp = yB;
          yB = yA;
          yA = temp;
          x = xB;
        }
        for (int y = yA; y < yB + 1; y++) {
          if (map.get(y).get(x).isBlocked()) {
            return false;
          }
          offset += delta;
          if (offset >= threshold) {
            x += adjust;
            threshold += thresholdInc;
          }
        }
      }
    }
    return true;
  }

  /**
   * Returns the approximate distance between two nodes in the search area.
   *
   * @param a first node
   * @param b second node
   * @return an int representing the approximate distance between the two nodes
   */
  private int distanceBetweenNodes(game_advanced.pathfinding.Node a,
      game_advanced.pathfinding.Node b) {
    int xDelta = Math.abs(b.getX() - a.getX());
    int yDelta = Math.abs(b.getY() - a.getY());
    return (int) (10 * Math.sqrt(xDelta + yDelta));
  }

  /**
   * Reconstructs the path by traversing from the destination node back through each parent node
   * until the start node is reached.
   *
   * @param start the node used as the starting point for the path search
   * @param dest the destination node that ends the path search
   * @return Returns a List of Nodes, which are the path with the start node at the head of the List
   * and the dest node at the tail
   */
  private List<game_advanced.pathfinding.Node> reconstructPath(
      game_advanced.pathfinding.Node start, game_advanced.pathfinding.Node dest) {
    LinkedList<game_advanced.pathfinding.Node> path = new LinkedList<>();
    game_advanced.pathfinding.Node node = dest;

    while (true) {
      path.addFirst(node);
      if (node == start) {
        break;
      }

      node = node.getParent();
    }
    return path;
  }

  public void renderMap(GraphicsContext gc) {
    for (List<game_advanced.pathfinding.Node> row : map) {
      for (game_advanced.pathfinding.Node node : row) {
        node.render(gc);
      }
    }
  }

  public void renderPath(GraphicsContext gc, List<game_advanced.pathfinding.Node> path) {
    for (game_advanced.pathfinding.Node node : path) {
      node.renderPath(gc, Color.BLACK);
    }
  }

  /**
   * Prints the given search area in an easy-to-view format.
   */
  public void printMap() {
    for (int i = 0; i < map.size(); i++) {
      for (int j = 0; j < map.get(0).size(); j++) {
        if (!map.get(i).get(j).isBlocked()) {
          System.out.print('O');
        } else {
          System.out.print('X');
        }
      }
      System.out.println();
    }
  }

  /**
   * Prints the search area along with the given path in an easy-to-view format.
   *
   * @param path the path that is printed over the search area
   */
  public void printPath(List<Node> path) {
    if (path == null) {
      System.out.println("No path.");
    } else {
      char[][] printedPath = new char[map.size()][map.get(0).size()];
      for (int i = 0; i < map.size(); i++) {
        for (int j = 0; j < map.get(0).size(); j++) {
          if (!map.get(i).get(j).isBlocked()) {
            printedPath[i][j] = 'O';
          } else {
            printedPath[i][j] = 'X';
          }
        }
      }

      for (int i = 0; i < path.size(); i++) {
        if (i == 0) {
          printedPath[path.get(i).getY()][path.get(i).getX()] = 'S';
        } else if (i == path.size() - 1) {
          printedPath[path.get(i).getY()][path.get(i).getX()] = 'D';
        } else {
          printedPath[path.get(i).getY()][path.get(i).getX()] = 'P';
        }
      }

      for (int i = 0; i < printedPath.length; i++) {
        for (int j = 0; j < printedPath[0].length; j++) {
          System.out.print(printedPath[i][j]);
        }
        System.out.println();
      }
    }
  }
}