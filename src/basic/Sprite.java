package basic;

import javafx.scene.canvas.GraphicsContext;

public abstract class Sprite implements Comparable<Sprite> {

  protected double x;
  protected double y;
  private int z;
  private double xSpeed;
  private double ySpeed;


  public void setPosition(double x, double y) {
    this.x = x;
    this.y = y;
  }

  public void setSpeed(double xSpeed, double ySpeed) {
    this.xSpeed = xSpeed;
    this.ySpeed = ySpeed;
  }

  public void updatePosition() {
    x += xSpeed;
    y += ySpeed;
  }

  public Sprite(double x, double y, int z) {
    this.x = x;
    this.y = y;
    this.z = z;
  }

  public abstract void render(GraphicsContext gc);

  public String toString() {
    return "Sprite<" + x + ", " + y + ">";
  }

  @Override
  public int compareTo(Sprite sprite) {
    return this.z - sprite.z;
  }
}
