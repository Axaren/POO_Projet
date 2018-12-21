package basic.pathfinding;

/**
 * Static class containing information on the grid map of the game, aswell as useful methods to
 * convert between grid and pixel coordinates.
 */
public class Graph {

  public static int NODE_HORIZONTAL_DISTANCE = 4;
  public static int NODE_VERTICAL_DISTANCE = 4;

  public static int getRealNodeX(Node node) {
    return node.getX() * NODE_HORIZONTAL_DISTANCE;
  }

  public static int getRealNodeY(Node node) {
    return node.getY() * NODE_VERTICAL_DISTANCE;
  }

  public static int getGraphX(int x) {
    return x / NODE_HORIZONTAL_DISTANCE;
  }

  public static int getGraphY(int y) {
    return y / NODE_VERTICAL_DISTANCE;
  }

}
