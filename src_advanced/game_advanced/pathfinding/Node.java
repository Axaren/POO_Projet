package game_advanced.pathfinding;

import java.io.Serializable;
import java.util.Objects;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Represents a Node in the Grid representing the game_basic map. Holds x y coordinates relative to
 * the grid. blocked : true if the node is blocked by something, used when calculating line of
 * sights and paths. parent : used to reconstruct the path found between two nodes. g : distance
 * from the starting node in a path to this node h : distance from this node to the destination node
 * in a path.
 *
 * Adapted from https://github.com/BlueWalker/Pathfinding
 */
public class Node implements Comparable<Node>, Serializable {

  private final int x;
  private final int y;
  private boolean blocked;
  private Node parent;
  private int g;
  private int h;

  public Node(int x, int y, boolean blocked) {
    this.x = x;
    this.y = y;
    this.blocked = blocked;

    this.g = 0;
    this.h = 0;
  }

  public boolean isBlocked() {
    return blocked;
  }

  public void setBlocked(boolean blocked) {
    this.blocked = blocked;
  }

  public Node getParent() {
    return parent;
  }

  public void setParent(Node parent) {
    this.parent = parent;
  }

  public int getG() {
    return g;
  }

  public void setG(int g) {
    this.g = g;
  }

  public int getH() {
    return h;
  }

  public void setH(int h) {
    this.h = h;
  }

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }

  @Override
  public int hashCode() {
    return Objects.hash(x, y);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Node node = (Node) o;
    return x == node.x &&
        y == node.y &&
        blocked == node.blocked;
  }

  @Override
  public String toString() {
    return "Node{" +
        "x=" + x +
        ", y=" + y +
        ", blocked=" + blocked +
        ", g=" + g +
        ", h=" + h +
        '}';
  }

  /**
   * Displays a square representing the node Changes its color according to its blocked attribute
   *
   * @param gc The GraphicsContext of the canvas
   */
  public void render(GraphicsContext gc) {
    if (blocked) {
      gc.setFill(Color.RED);
    } else {
      gc.setFill(Color.WHITE);
    }
    gc.fillRect(game_advanced.pathfinding.Graph.getRealNodeX(this),
        game_advanced.pathfinding.Graph.getRealNodeY(this), 2, 2);
  }

  /**
   * Renders the calculated path by rendering a line between this node and its parent
   *
   * @param gc The GraphicsContext of the canvas
   * @param color Color of the line representign the calculated path
   */
  public void renderPath(GraphicsContext gc, Color color) {
    render(gc);
    if (parent != null) {
      gc.strokeLine(game_advanced.pathfinding.Graph.getRealNodeX(parent),
          game_advanced.pathfinding.Graph.getRealNodeY(parent),
          game_advanced.pathfinding.Graph.getRealNodeX(this),
          game_advanced.pathfinding.Graph.getRealNodeY(this));
    }
  }

  /**
   * Compares this node to another with the sum of their g and h values
   *
   * @param node another node
   * @return an int representing the relative order between this node and the other
   */
  @Override
  public int compareTo(Node node) {
    double f = this.h + this.g;
    double anotherF = node.h + node.g;
    return Double.compare(f, anotherF);
  }

  /**
   * Calculates the pixel distance between this node and an other node.
   *
   * @param other another node
   * @return the distance from this node to the other node in pixels
   */
  public int distanceTo(Node other) {
    int realX = game_advanced.pathfinding.Graph.getRealNodeX(this);
    int realY = game_advanced.pathfinding.Graph.getRealNodeY(this);
    int otherX = game_advanced.pathfinding.Graph.getRealNodeX(other);
    int otherY = Graph.getRealNodeY(other);

    return (int) Math.sqrt(
        ((realX - otherX) * (realX - otherX) + (realY - otherY) * (realY - otherY)));
  }

}
