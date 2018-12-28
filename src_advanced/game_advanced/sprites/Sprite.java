package game_advanced.sprites;

import java.io.Serializable;
import javafx.scene.canvas.GraphicsContext;

/**
 * A sprite represents an object that can move on the screen. It holds a position and a speed
 * vector
 */
public abstract class Sprite implements Comparable<Sprite>, Serializable {

  double x;
  double y;
  int z;
  private double xSpeed;
  private double ySpeed;

  public Sprite(Sprite sprite) {
    this.x = sprite.x;
    this.y = sprite.y;
    this.z = sprite.z;
    this.xSpeed = sprite.xSpeed;
    this.ySpeed = sprite.ySpeed;
  }

  public Sprite(double x, double y, int z) {
    this.x = x;
    this.y = y;
    this.z = z;
  }

  public double getX() {
    return x;
  }

  public double getY() {
    return y;
  }

  public void setPosition(double x, double y) {
    this.x = x;
    this.y = y;
  }

  /**
   * Updates the sprite's position according to its speed vector
   * @param delta time since last update in seconds
   */
  public void updatePosition(double delta) {
    x += xSpeed * delta;
    y += ySpeed * delta;
  }

  public void setSpeed(double xSpeed, double ySpeed) {
    this.xSpeed = xSpeed;
    this.ySpeed = ySpeed;
  }

  /**
   * Used to show the sprite on the sprite
   *
   * @param gc The GraphicsContext of the canvas
   */
  public abstract void render(GraphicsContext gc);

  /**
   * Checks if a point is inside the sprite
   *
   * @param x coordinate
   * @param y coordinate
   * @return True if the point is inside the sprite
   */
  public abstract boolean contains(int x, int y);

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Sprite sprite = (Sprite) o;
    return this.x == sprite.x
        && this.y == sprite.y;
  }

  public String toString() {
    return "Sprite<" + this.x + "," + this.y + "," + this.z + ">";
  }

  @Override
  public int compareTo(Sprite sprite) {
    return this.z - sprite.z;
  }
}
