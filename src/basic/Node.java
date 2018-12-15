package basic;

import java.util.Objects;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Node {

  private final int x;
  private final int y;
  private boolean blocked;
  private Node parent;
  private double g;
  private double h;

  public Node(int x, int y, boolean blocked) {
    this.x = x;
    this.y = y;
    this.blocked = blocked;

    this.g = 0.0;
    this.h = 0.0;
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

  public double getG() {
    return g;
  }

  public void setG(double g) {
    this.g = g;
  }

  public double getH() {
    return h;
  }

  public void setH(double h) {
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

  public void render(GraphicsContext gc) {
    if (blocked) {
      gc.setFill(Color.RED);
    } else {
      gc.setFill(Color.WHITE);
    }
    gc.fillRect(x, y, 3, 3);
  }
}
