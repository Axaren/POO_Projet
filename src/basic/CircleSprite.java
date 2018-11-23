package basic;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class CircleSprite extends Sprite {

  private int radius;
  private Color color;

  public CircleSprite(double x, double y, int z, int radius, Color color) {
    super(x, y, z);
    this.radius = radius;
    this.color = color;
  }

  @Override
  public void render(GraphicsContext gc) {
    gc.setFill(color);
    gc.fillOval(x, y, radius, radius);
  }
}
