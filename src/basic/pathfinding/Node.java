package basic.pathfinding;

import java.util.Objects;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Node implements Comparable<Node> {

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

  public void render(GraphicsContext gc, int horizontalDistance, int verticalDistance) {
    if (blocked) {
      gc.setFill(Color.RED);
    } else {
      gc.setFill(Color.WHITE);
    }
    gc.fillRect(x * horizontalDistance, y * verticalDistance, 2, 2);
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

  public void render(GraphicsContext gc, int horizontalDistance, int verticalDistance,
      Color color) {
    if (blocked) {
      gc.setFill(color.brighter());
    } else {
      gc.setFill(color);
    }
    gc.fillRect(x * horizontalDistance, y * verticalDistance, 1, 1);
    if (parent != null) {
      gc.strokeLine(parent.x * horizontalDistance, parent.y * verticalDistance,
          x * horizontalDistance, y * verticalDistance);
    }
  }

  @Override
  public int compareTo(Node node) {
    double f = this.h + this.g;
    double anotherF = node.h + node.g;
    return Double.compare(f, anotherF);
  }

  public int distanceTo(Node other) {
    int realX = Graph.getRealNodeX(this);
    int realY = Graph.getRealNodeY(this);
    int otherX = Graph.getRealNodeX(other);
    int otherY = Graph.getRealNodeY(other);

    return (int) Math.sqrt(
        ((realX - otherX) * (realX - otherX) + (realY - otherY) * (realY - otherY)));
  }

}
