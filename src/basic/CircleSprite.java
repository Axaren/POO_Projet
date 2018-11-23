package basic;

import javafx.scene.canvas.GraphicsContext;

public class CircleSprite extends Sprite {

  private int radius;

  public CircleSprite(double x, double y, int z, int radius) {
    super(x, y, z);
    this.radius = radius;
  }

  @Override
  public void render(GraphicsContext gc) {
    gc.fillOval(x, y, radius, radius);
  }
}
